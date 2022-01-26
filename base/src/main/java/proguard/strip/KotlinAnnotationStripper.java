/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

package proguard.strip;

import proguard.AppView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.Configuration;
import proguard.classfile.Clazz;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.RuntimeVisibleAnnotationsAttribute;
import proguard.classfile.attribute.annotation.visitor.AnnotationTypeFilter;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.editor.AnnotationsAttributeEditor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.kotlin.visitor.KotlinMetadataRemover;
import proguard.classfile.kotlin.visitor.filter.KotlinClassFilter;
import proguard.classfile.visitor.*;
import proguard.pass.Pass;
import proguard.util.ProcessingFlagSetter;
import proguard.util.ProcessingFlags;

import static proguard.util.ProcessingFlags.*;

/**
 * This pass aggressively strips the kotlin.Metadata annotation from classes. We only keep
 * the metadata for classes/members if the class isn't processed in any way.
 *
 * @author James Hamilton
 */
public class KotlinAnnotationStripper implements Pass
{
    private static final Logger logger = LogManager.getLogger(KotlinAnnotationStripper.class);

    private final Configuration configuration;

    public KotlinAnnotationStripper(Configuration configuration)
    {
        this.configuration = configuration;
    }


    @Override
    public void execute(AppView appView)
    {
        logger.info("Removing @kotlin.Metadata annotation where not kept...");

        ClassCounter               originalCounter          = new ClassCounter();
        MemberCounter              keptMemberCounter        = new MemberCounter();
        MyKotlinAnnotationStripper kotlinAnnotationStripper = new MyKotlinAnnotationStripper();

        ClassVisitor kotlinAnnotationStripperVisitor =
            new MultiClassVisitor(
                new KotlinClassFilter(
                new MultiClassVisitor(
                    originalCounter,
                    // The member counter won't increase if there are no members kept in the class.
                    new CounterConditionalClassVisitor(keptMemberCounter, CounterConditionalClassVisitor::isSame,

                    // Check how many any of the members were specifically kept.
                    new AllMemberVisitor(
                    new MemberProcessingFlagFilter(
                        DONT_OBFUSCATE | DONT_SHRINK | DONT_OPTIMIZE,
                        0,
                        keptMemberCounter)),

                    // Conditional visitor for when members weren't kept, we remove the annotation
                    // if the class does not have DONT_OBFUSCATE, DONT_SHRINK, DONT_OPTIMIZE.
                    new ClassProcessingFlagFilter(
                        0,
                        DONT_OBFUSCATE | DONT_SHRINK | DONT_OPTIMIZE,
                        kotlinAnnotationStripper)))),

                // Ensure that a class that still has the annotation is marked DONT_OPTIMIZE
                // (e.g. if originally the member was kept but class wasn't).
                new KotlinClassFilter(new ProcessingFlagSetter(ProcessingFlags.DONT_OPTIMIZE)));

        appView.programClassPool.classesAccept(kotlinAnnotationStripperVisitor);
        appView.libraryClassPool.classesAccept(kotlinAnnotationStripperVisitor);

        logger.info("  Original number of classes with @kotlin.Metadata:            {}", originalCounter.getCount());
        logger.info("  Final number of classes with @kotlin.Metadata:               {}", (originalCounter.getCount() - kotlinAnnotationStripper.getCount()));
    }


    private static class MyKotlinAnnotationStripper
    implements           ClassVisitor,
                         AttributeVisitor,
                         AnnotationVisitor
    {

        private final KotlinMetadataRemover      kotlinMetadataRemover = new KotlinMetadataRemover();
        private       AnnotationsAttributeEditor attributesEditor;

        private int count = 0;

        public int getCount()
        {
            return count;
        }

        // Implementations for ClassVisitor.

        @Override
        public void visitAnyClass(Clazz clazz)
        {
            clazz.attributesAccept(this);
        }

        // Implementations for AttributeVisitor.

        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        @Override
        public void visitRuntimeVisibleAnnotationsAttribute(Clazz                              clazz,
                                                            RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
        {
            attributesEditor = new AnnotationsAttributeEditor(runtimeVisibleAnnotationsAttribute);
            runtimeVisibleAnnotationsAttribute.annotationsAccept(
                clazz,
                new AnnotationTypeFilter(KotlinConstants.TYPE_KOTLIN_METADATA, this));
        }

        // Implementations for AnnotationVisitor.

        @Override
        public void visitAnnotation(Clazz clazz, Annotation annotation)
        {
            logger.debug("Removing Kotlin metadata annotation from {}", clazz.getName());
            attributesEditor.deleteAnnotation(annotation);
            clazz.accept(kotlinMetadataRemover);
            count++;
        }
    }
}
