package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.editor.AttributeAdder;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.ClassEditor;
import proguard.obfuscate.NameFactory;
import proguard.obfuscate.SimpleNameFactory;
import proguard.obfuscate.UniqueMemberNameFactory;
import proguard.optimize.info.ProgramMemberOptimizationInfoSetter;

import java.util.function.Function;

public class DescriptorModifier {
    private final ClassBuilder classBuilder;
    private final NameFactory nameFactory;
    private final Clazz clazz;

    DescriptorModifier(Clazz clazz) {
        classBuilder = new ClassBuilder((ProgramClass) clazz);
        nameFactory = new UniqueMemberNameFactory(new SimpleNameFactory(), clazz);
        this.clazz = clazz;
    }
    
    public ProgramMethod modify(ProgramMethod sourceMethod, Function<String, String> modifier) {
        return modify(sourceMethod, modifier, false);
    }

    public ProgramMethod modify(ProgramMethod sourceMethod, Function<String, String> modifier, boolean removeOriginal) {
        ProgramMethod newMethod = classBuilder.addAndReturnMethod(
                sourceMethod.u2accessFlags,
                nameFactory.nextName(),
                modifier.apply(sourceMethod.getDescriptor(clazz))
        );

        sourceMethod.attributesAccept((ProgramClass) clazz,
                new AttributeAdder((ProgramClass) clazz, newMethod, true));

        if (removeOriginal)
            removeOriginal(sourceMethod);

        newMethod.accept(clazz, new ProgramMemberOptimizationInfoSetter());
        return newMethod;
    }

    private void removeOriginal(ProgramMethod method) {
        ClassEditor classEditor = new ClassEditor((ProgramClass) clazz);
        classEditor.removeMethod(method);
    }
}
