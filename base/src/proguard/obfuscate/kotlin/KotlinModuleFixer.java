/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */

package proguard.obfuscate.kotlin;

import proguard.classfile.kotlin.*;
import proguard.classfile.util.ClassUtil;
import proguard.resources.file.ResourceFile;
import proguard.resources.file.visitor.*;
import proguard.resources.kotlinmodule.*;
import proguard.resources.kotlinmodule.visitor.KotlinModulePackageVisitor;

import java.util.*;
import java.util.stream.Stream;

/**
 * This class fixes the {@link KotlinModule} fileName to match the module name
 * and the strings referring to classes to match the obfuscated names. It also
 * moves the classes around into the correct package, if they were moved during
 * the obfuscation.
 *
 * @author James Hamilton
 */
public class KotlinModuleFixer
implements   ResourceFileVisitor
{
    // Implementations for ResourceFileVisitor.

    @Override
    public void visitKotlinModule(KotlinModule kotlinModule)
    {
        // Fix the filename.
        kotlinModule.fileName = moduleNameToFileName(kotlinModule.name);

        // Visit each package part to fix the strings, and collect the existing package names.
        List<String> existingPackageNames = new ArrayList<>();
        kotlinModule.modulePackagesAccept(new ModulePackageNameFixer(existingPackageNames));

        // Then collect all the files facades and multi-file parts to move
        Map<String, KotlinFileFacadeKindMetadata>    fileFacadesToMove    = new HashMap<>();
        Map<String, KotlinMultiFilePartKindMetadata> multiFilePartsToMove = new HashMap<>();
        kotlinModule.modulePackagesAccept(new ToMoveCollector(fileFacadesToMove, multiFilePartsToMove));

        // Then remove the parts to be moved from the current modules parts.
        kotlinModule.modulePackagesAccept(new ModulePartCleaner(fileFacadesToMove, multiFilePartsToMove));

        // Then create the newly required module packages because
        // if something moved packages, a new package is now required.
        createNewModulePackages(kotlinModule, existingPackageNames, fileFacadesToMove, multiFilePartsToMove);

        // Finally, add the facades and parts to move into the correct modules.
        addFileFacadesToModule(   kotlinModule, fileFacadesToMove);
        addMultiFilePartsToModule(kotlinModule, multiFilePartsToMove);
    }

    // Helper classes.

    private static class ModulePackageNameFixer
        implements KotlinModulePackageVisitor
    {
        private final List<String> packageNames;

        ModulePackageNameFixer(List<String> packageNames)
        {
            this.packageNames = packageNames;
        }

        @Override
        public void visitKotlinModulePackage(KotlinModule kotlinModule, KotlinModulePackage kotlinModulePackage)
        {
            packageNames.add(kotlinModulePackage.fqName);

            // First fix the names of file facades.
            List<String> fileFacades = kotlinModulePackage.fileFacadeNames;
            for (int i = 0; i < fileFacades.size(); i++)
            {
                KotlinFileFacadeKindMetadata referencedFileFacade = kotlinModulePackage.referencedFileFacades.get(i);
                fileFacades.set(i, referencedFileFacade.ownerReferencedClass.getName());
            }

            // Fix the names of multi-file parts.
            Map<String, String> newMultiFileClassParts = new HashMap<>();
            kotlinModulePackage.multiFileClassParts.forEach(
                (multiFilePartName, multiFileFacadeName) ->
                {
                    KotlinMultiFilePartKindMetadata referencedMultiFilePart =
                        kotlinModulePackage.referencedMultiFileParts.get(multiFilePartName);

                    newMultiFileClassParts.put(referencedMultiFilePart.ownerClassName,
                                               referencedMultiFilePart.facadeName);

                    if (!referencedMultiFilePart.ownerClassName.equals(multiFilePartName))
                    {
                        kotlinModulePackage.referencedMultiFileParts.put(referencedMultiFilePart.ownerClassName,
                                                                      kotlinModulePackage.referencedMultiFileParts.get(multiFilePartName));
                        kotlinModulePackage.referencedMultiFileParts.remove(multiFilePartName);
                    }
                });

            kotlinModulePackage.multiFileClassParts.clear();
            kotlinModulePackage.multiFileClassParts.putAll(newMultiFileClassParts);
        }
    }


    /**
     * Collects all the file facades and multi-file parts that are in the wrong packages.
     */
    private static class ToMoveCollector
    implements KotlinModulePackageVisitor
    {
        private final Map<String, KotlinFileFacadeKindMetadata>    fileFacadesToMove;
        private final Map<String, KotlinMultiFilePartKindMetadata> multiFilePartsToMove;

        private ToMoveCollector(Map<String, KotlinFileFacadeKindMetadata>    fileFacadesToMove,
                                Map<String, KotlinMultiFilePartKindMetadata> multiFilePartsToMove)
        {
            this.fileFacadesToMove    = fileFacadesToMove;
            this.multiFilePartsToMove = multiFilePartsToMove;
        }

        @Override
        public void visitKotlinModulePackage(KotlinModule kotlinModule, KotlinModulePackage kotlinModulePart)
        {
            List<String> fileFacadeNames = kotlinModulePart.fileFacadeNames;
            for (int i = 0; i < fileFacadeNames.size(); i++)
            {
                String fileFacadeName    = fileFacadeNames.get(i);
                String fileFacadePackage = ClassUtil.internalPackageName(fileFacadeName);
                if (!fileFacadePackage.equals(kotlinModulePart.fqName))
                {
                    fileFacadesToMove.put(fileFacadeName, kotlinModulePart.referencedFileFacades.get(i));
                }
            }

            kotlinModulePart.referencedMultiFileParts.forEach((multiFilePartName, referencedMultiFilePart) -> {
                if (!kotlinModulePart.fqName.equals(ClassUtil.internalPackageName(multiFilePartName)))
                {
                    multiFilePartsToMove.put(multiFilePartName, referencedMultiFilePart);
                }
            });
        }
    }


    /**
     * Remove the files facades and multi-file parts from the modules.
     */
    private static class ModulePartCleaner
    implements KotlinModulePackageVisitor
    {
        private final Map<String, KotlinFileFacadeKindMetadata>    fileFacadesToMove;
        private final Map<String, KotlinMultiFilePartKindMetadata> multiFilePartsToMove;

        private ModulePartCleaner(Map<String, KotlinFileFacadeKindMetadata>    fileFacadesToMove,
                                  Map<String, KotlinMultiFilePartKindMetadata> multiFilePartsToMove)
        {
            this.fileFacadesToMove    = fileFacadesToMove;
            this.multiFilePartsToMove = multiFilePartsToMove;
        }

        @Override
        public void visitKotlinModulePackage(KotlinModule kotlinModule, KotlinModulePackage kotlinModulePart)
        {
            for (String fileFacadeName : fileFacadesToMove.keySet())
            {
                int index = kotlinModulePart.fileFacadeNames.indexOf(fileFacadeName);
                if (index != -1)
                {
                    kotlinModulePart.fileFacadeNames      .remove(index);
                    kotlinModulePart.referencedFileFacades.remove(index);
                }
            }

            for (String multiFilePartName : multiFilePartsToMove.keySet())
            {
                kotlinModulePart.multiFileClassParts.remove(multiFilePartName);
                kotlinModulePart.referencedMultiFileParts.remove(multiFilePartName);
            }
        }
    }

    // Helper methods for adding new modules.

    private static void createNewModulePackages(KotlinModule                                 kotlinModule,
                                                List<String>                                 existingPackageNames,
                                                Map<String, KotlinFileFacadeKindMetadata>    fileFacadesToMove,
                                                Map<String, KotlinMultiFilePartKindMetadata> multiFilePartsToMove)
    {
        // Create a new package in the module for each of the classes packages
        // if they're not an already existing package.

        Stream.concat(fileFacadesToMove   .keySet().stream(),
                      multiFilePartsToMove.keySet().stream())
            .map(ClassUtil::internalPackageName)
            .distinct()
            .filter(packageName -> !existingPackageNames.contains(packageName))
            .map(   packageName -> new KotlinModulePackage(packageName, new ArrayList<>(), new HashMap<>()))
            .forEach(kotlinModule.modulePackages::add);
    }

    private static void addFileFacadesToModule(KotlinModule                              kotlinModule,
                                               Map<String, KotlinFileFacadeKindMetadata> fileFacadesToMove)
    {
        fileFacadesToMove.forEach((fileFacadeName, referencedFileFacade) ->
            kotlinModule.modulePackagesAccept((__, modulePackage) -> {
                if (modulePackage.fqName.equals(ClassUtil.internalPackageName(fileFacadeName)))
                {
                    modulePackage.fileFacadeNames      .add(fileFacadeName);
                    modulePackage.referencedFileFacades.add(referencedFileFacade);
                }
            }));
    }

    private static void addMultiFilePartsToModule(KotlinModule                                 kotlinModule,
                                                  Map<String, KotlinMultiFilePartKindMetadata> multiFilePartsToMove)
    {
        multiFilePartsToMove.forEach((multiFilePartName, referencedMultiFilePart) ->
            kotlinModule.modulePackagesAccept((__, modulePackage) -> {
                if (modulePackage.fqName.equals(ClassUtil.internalPackageName(multiFilePartName)))
                {
                    modulePackage.multiFileClassParts     .put(multiFilePartName, referencedMultiFilePart.xs);
                    modulePackage.referencedMultiFileParts.put(multiFilePartName, referencedMultiFilePart);
                }
            }));
    }

    // Small helper methods.

    private static String moduleNameToFileName(String moduleName)
    {
        return KotlinConstants.MODULE.FILE_EXPRESSION.replace("*", moduleName);
    }
}
