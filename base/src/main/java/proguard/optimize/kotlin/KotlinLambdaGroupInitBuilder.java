package proguard.optimize.kotlin;

import proguard.classfile.AccessConstants;
import proguard.classfile.ClassConstants;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.CompactCodeAttributeComposer;

public class KotlinLambdaGroupInitBuilder {

    public static final String TYPE_KOTLIN_LAMBDA_INIT = "(I)V";
    public static final String RETURN_TYPE_INIT = "V";
    public static final String METHOD_ARGUMENT_TYPE_INIT = "Ljava/lang/Object;";

    private final int closureSize;
    private final ClassBuilder classBuilder;

    public KotlinLambdaGroupInitBuilder(int closureSize, ClassBuilder classBuilder)
    {
        // TODO: support arguments of specific types
        //  e.g. by taking a list of argument types instead of a closure size
        this.closureSize = closureSize;
        this.classBuilder = classBuilder;
    }

    private static String getInitDescriptorForClosureSize(int closureSize)
    {
        StringBuilder descriptor = new StringBuilder("(");
        for (int argumentIndex = 1; argumentIndex <= closureSize; argumentIndex++)
        {
            descriptor.append(METHOD_ARGUMENT_TYPE_INIT);
        }
        descriptor.append(KotlinLambdaGroupBuilder.FIELD_TYPE_ID);
        descriptor.append(")")
                  .append(RETURN_TYPE_INIT);
        return descriptor.toString();
    }

    private CompactCodeAttributeComposer addPutFreeVariablesInFieldsInstructions(CompactCodeAttributeComposer composer)
    {
        for (int argumentIndex = 1; argumentIndex <= this.closureSize; argumentIndex++)
        {
            composer.aload(argumentIndex)
                    .putfield(this.classBuilder.getProgramClass(),
                              this.classBuilder.getProgramClass().findField(
                                      KotlinLambdaGroupBuilder.FIELD_NAME_PREFIX_FREE_VARIABLE + argumentIndex,
                                      KotlinLambdaGroupBuilder.FIELD_TYPE_FREE_VARIABLE
                              ));
        }
        return composer;
    }

    public ClassBuilder.CodeBuilder buildCodeBuilder()
    {
        return code -> {
                code
                    .aload_0(); // load this class
                    // TODO: load parameter variables and store them in their respective fields
                    addPutFreeVariablesInFieldsInstructions(code)
                    .iload(this.closureSize + 1) // load the id argument
                    .putfield(this.classBuilder.getProgramClass(), // store the id in a field
                              this.classBuilder.getProgramClass()
                                               .findField(KotlinLambdaGroupBuilder.FIELD_NAME_ID,
                                                          KotlinLambdaGroupBuilder.FIELD_TYPE_ID))
                    .aload_0() // load this class
                    .iconst_0() // push 0 on stack
                    .invokespecial(KotlinLambdaMerger.NAME_KOTLIN_LAMBDA,
                                   ClassConstants.METHOD_NAME_INIT,
                                   TYPE_KOTLIN_LAMBDA_INIT)
                    .return_();
        };
    }

    public ProgramMethod build()
    {
        // add a classId field to the lambda group
        ProgramField classIdField = classBuilder.addAndReturnField(AccessConstants.PRIVATE, "classId", "I");

        // add a constructor which takes an id as argument and stores it in the classId field
        return classBuilder.addAndReturnMethod(AccessConstants.PUBLIC,
                ClassConstants.METHOD_NAME_INIT,
                getInitDescriptorForClosureSize(this.closureSize),
                50,
                this.buildCodeBuilder());
    }
}
