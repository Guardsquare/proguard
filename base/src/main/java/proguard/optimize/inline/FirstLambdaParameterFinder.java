package proguard.optimize.inline;

import proguard.classfile.util.InternalTypeEnumeration;

public class FirstLambdaParameterFinder {
    public static int findFirstLambdaParameter(String descriptor) {
        return findFirstLambdaParameter(descriptor, true);
    }

    public static int findFirstLambdaParameter(String descriptor, boolean isStatic) {
        InternalTypeEnumeration internalTypeEnumeration = new InternalTypeEnumeration(descriptor);
        int index = 0;
        while (internalTypeEnumeration.hasMoreTypes()) {
            if (internalTypeEnumeration.nextType().startsWith("Lkotlin/jvm/functions/Function")) {
                break;
            }
            index ++;
        }
        return index + (isStatic ? 0 : 1);
    }
}
