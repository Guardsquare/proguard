# Lambda inlining step by step
## Terminology
- The lambda = A Kotlin lambda that is implemented as a class that uses the singleton pattern. The class contains an `INSTANCE` field that is initialized in the static initializer of the class. Lambda classes inherit from `kotlin.jvm.internal.Lambda`. Every lambda class has an invoke method that depends on the `kotlin.jvm.functions.Function` interface they implement. This could be for example `kotlin.jvm.functions.Function2` which is a lambda that takes 2 arguments and returns something.
- Consuming method = The method that takes a lambda as an argument, when inlined this method will loose this lambda argument.
- Consuming call method = The method that calls the consuming method. This method will also contain the `getstatic` instruction  that obtains a reference to the lambda.
- Invoke method = lambda implementation method = This method is a method in the class that implements the lambda. The method contains the actual implementation of the lambda written by the programmer.
- The consuming method, the consuming call method and the invoke method are all modified during the inlining process. In the sections below each method is shown individually with it's changes.
## Changes to the consuming method
### Starting point
This is the original consuming method without any changes. As you can see in the arguments, this method takes a lambda as it's first argument.

Consuming method = `test(Lkotlin/jvm/functions/Function1;)V`
```
[0] aload_0 v0
[1] ldc #10 = String("a")
[3] invokestatic #16 = Methodref(kotlin/jvm/internal/Intrinsics.checkNotNullParameter(Ljava/lang/Object;Ljava/lang/String;)V)
[6] aload_0 v0
[7] bipush 12
[9] invokestatic #22 = Methodref(java/lang/Integer.valueOf(I)Ljava/lang/Integer;)
[12] invokeinterface #28, 512 = InterfaceMethodref(kotlin/jvm/functions/Function1.invoke(Ljava/lang/Object;)Ljava/lang/Object;)
[17] checkcast #30 = Class(java/lang/Number)
[20] invokevirtual #34 = Methodref(java/lang/Number.intValue()I)
[23] istore_1 v1
[24] getstatic #40 = Fieldref(java/lang/System.out Ljava/io/PrintStream;)
[27] iload_1 v1
[28] invokevirtual #46 = Methodref(java/io/PrintStream.println(I)V)
[31] return
```
### After copying the consuming method
We copy the consuming method because we will modify it to be specific to a particular usage of that method with a particular lambda argument. So if we have a method and we call it once with lambda 1 and once with lambda 2 we will have 2 new methods. One with lambda 1 inlined in it and one with lambda 2 inlined in it.

Consuming method = `b(Lkotlin/jvm/functions/Function1;)V`
### Removing casts and replacing call instruction
There are 2 invoke methods in the lambda class, a bridge method and a method that is called by the bridge method. This method that is called by the bridge method can have unboxed types as parameters. The bridge method has boxed parameter types because everything is erased to `Object` because we inline the non-bridge invoke method we will remove these casts. By removing the unneeded casts we are also able to improve performance.

Consuming method = `b(Lkotlin/jvm/functions/Function1;)V`
```
[0] aload_0 v0
[1] ldc #10 = String("a")
[3] invokestatic #16 = Methodref(kotlin/jvm/internal/Intrinsics.checkNotNullParameter(Ljava/lang/Object;Ljava/lang/String;)V)
[6] aload_0 v0
[7] bipush 12
[9] invokestatic #92 = Methodref(MainKt.a(Ljava/lang/Object;I)Ljava/lang/Integer;)
[12] istore_1 v1
[13] getstatic #40 = Fieldref(java/lang/System.out Ljava/io/PrintStream;)
[16] iload_1 v1
[17] invokevirtual #46 = Methodref(java/io/PrintStream.println(I)V)
[20] return
```
### After inlining static invoke method
In this step we actually copy the implementation from the invoke method and inline it into the consuming method.

Consuming method = `b(Lkotlin/jvm/functions/Function1;)V`
```
[0] aload_0 v0
[1] ldc #10 = String("a")
[3] invokestatic #16 = Methodref(kotlin/jvm/internal/Intrinsics.checkNotNullParameter(Ljava/lang/Object;Ljava/lang/String;)V)
[6] aload_0 v0
[7] bipush 12
[9] istore_3 v3
[10] astore_2 v2
[11] iload_3 v3
[12] iload_3 v3
[13] imul
[14] iconst_1
[15] iadd
[16] istore_1 v1
[17] getstatic #40 = Fieldref(java/lang/System.out Ljava/io/PrintStream;)
[20] iload_1 v1
[21] invokevirtual #46 = Methodref(java/io/PrintStream.println(I)V)
[24] return
```
### After the null check remover
We are going to remove the lambda argument, because we do that we no longer need the null check that is present on the argument.

Consuming method = `b(Lkotlin/jvm/functions/Function1;)V`
```
[0] aload_0 v0
[1] bipush 12
[3] istore_3 v3
[4] astore_2 v2
[5] iload_3 v3
[6] iload_3 v3
[7] imul
[8] iconst_1
[9] iadd
[10] istore_1 v1
[11] getstatic #40 = Fieldref(java/lang/System.out Ljava/io/PrintStream;)
[14] iload_1 v1
[15] invokevirtual #46 = Methodref(java/io/PrintStream.println(I)V)
[18] return
```
### After changing the signature and removing the usage of the argument
Removing the usage of the argument shifts all load and store instruction that have a higher index than the index of the local that stores the lambda down by 1. Attempts to store the lambda in a local will result in a `pop` instruction. Any time the lambda local is loaded the instruction will be replaced with a direct reference to the lambda so this is a form of constant propagation.

Consuming method = `c()V`
```
[0] aconst_null
[1] nop
[2] bipush 12
[4] istore_2 v2
[5] astore_1 v1
[6] iload_2 v2
[7] iload_2 v2
[8] imul
[9] iconst_1
[10] iadd
[11] istore_0 v0
[12] getstatic #40 = Fieldref(java/lang/System.out Ljava/io/PrintStream;)
[15] iload_0 v0
[16] invokevirtual #46 = Methodref(java/io/PrintStream.println(I)V)
[19] return
```
## Changes to the method that calls the consuming method
### Starting point
At the starting point the code will obtain a reference to the lambda instance through the `getstatic` instruction. It will then invoke the method using this reference as one of the arguments.
```
[0] getstatic #55 = Fieldref(MainKt$main$1.INSTANCE LMainKt$main$1;)
[3] checkcast #24 = Class(kotlin/jvm/functions/Function1)
[6] invokestatic #57 = Methodref(MainKt.test(Lkotlin/jvm/functions/Function1;)V)
[9] return
```

### After inlining the lambda
After inlining we will replace the first 3 instructions by just one `invokestatic` instruction that calls the new method that has the lambda inlined. As you can see this method takes no arguments. The method we call here is the final resulting method we get from the previous section.
```
[0] invokestatic #95 = Methodref(MainKt.c()V)
[3] return
```
## Changes to the invoke method
### The original invoke method
This is the original invoke method and its descriptor. This method contains the implementation of the lambda created by the programmer. In this case we have a lambda that multiplies the integer which is provided as an argument by itself and then adds 1 to the result. This matches the original Kotlin code of the lambda:
```kotlin
{ (it * it) + 1 }
```

At the end `valueOf` is called to take the primitive integer type and convert it to the boxed `java.lang.Integer` type.

invoke method = `invoke(I)Ljava/lang/Integer;`
```
[0] iload_1 v1
[1] iload_1 v1
[2] imul
[3] iconst_1
[4] iadd
[5] invokestatic #22 = Methodref(java/lang/Integer.valueOf(I)Ljava/lang/Integer;)
[8] areturn
```
### After copying to the consuming class
Still the same method and descriptor, just in a different class.
### After making it static
invoke method = `a(Ljava/lang/Object;I)Ljava/lang/Integer;`

The code is the same.
### After removing the cast
The cast at the end is not really needed, after calling invoke in the consuming method we will cast it back into a primitive type. So we can remove the cast at the end here and we also do the same thing in the consuming method.

invoke method = `a(Ljava/lang/Object;I)Ljava/lang/Integer;`
```
[0] iload_1 v1
[1] iload_1 v1
[2] imul
[3] iconst_1
[4] iadd
[5] areturn
```
### After inlining
This method is deleted after inlining, we no longer need it at this point.