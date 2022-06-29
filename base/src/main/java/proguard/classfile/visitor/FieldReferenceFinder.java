package proguard.classfile.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.ClassUtil;
import proguard.util.ListParser;
import proguard.util.NameParser;
import proguard.util.StringMatcher;

public class FieldReferenceFinder implements ConstantVisitor {

    private final Clazz referencedClass;
    private final StringMatcher regularExpressionMatcher;
    private final ConstantVisitor constantVisitor;
    private boolean fieldReferenceFound = false;

    public FieldReferenceFinder(Clazz referencedClass,
                                String fieldNameRegularExpression,
                                ConstantVisitor constantVisitor)
    {
        this.referencedClass           = referencedClass;
        this.regularExpressionMatcher = new ListParser(new NameParser(null)).parse(fieldNameRegularExpression);
        this.constantVisitor           = constantVisitor;
    }

    @Override
    public void visitAnyConstant(Clazz clazz, Constant constant) {}

    @Override
    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        if (fieldrefConstant.referencedClass == referencedClass
                && this.regularExpressionMatcher.matches(fieldrefConstant.getName(clazz)))
        {
            this.fieldReferenceFound = true;
            fieldrefConstant.accept(clazz, this.constantVisitor);
        }
    }

    public boolean isFieldReferenceFound()
    {
        return this.fieldReferenceFound;
    }
}
