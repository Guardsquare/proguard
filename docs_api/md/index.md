**ProGuard Core** is a free library to read, analyze, modify, and write Java
class files. It is the core of the well-konown shrinker, optimizer, and
obfuscator [ProGuard](https://www.guardsquare.com/proguard).

Typical applications:

- Perform peephole optimizations in Java bytecode.
- Search for instruction patterns.
- Analyze code with abstract evaluation.
- Optimize and obfuscate, like ProGuard itself.

## Design

The library defines many small classes as the building blocks for applications
that contain the processing logic. This is sometimes taken to the extreme: even
loops and conditional statements can often be implemented as separate classes.
Even though these classes are verbose and repetitive, the resulting main code
becomes much more compact, flexible, and robust.

### Data classes

Basic data classes define the structures to represent Java bytecode. They
reflect the Java bytecode specifications literally, to ensure that no data are
lost when reading, analyzing, and writing them. The data classes contain only
a minimum number of methods. They do have one or more accept methods to let
the visitor classes below operate on them.

### Visitors

The library applies the visitor pattern extensively. Visitor classes define
the operations on the data: reading, writing, editing, transforming,
analyzing, etc. The visitor classes have one or more 'visit' methods to
operate on data classes of the same basic type.

For example, a Java bytecode class contains a constant pool with constants of
different types: integer constants, float constants, string constants, etc.
The data classes IntegerConstant, FloatConstant, StringConstant, etc. all
implement the basic type Constant. The visitor interface ConstantVisitor
contains methods 'visitIntegerConstant', 'visitFloatConstant',
'visitStringConstant', etc. Implementations of this visitor interface can
perform all kinds of operations on the constants.

The reasoning behind this pattern is that the data classes are very stable,
because they are directly based on the bytecode specifications. The operations
are more dynamic, since they depend on the final application. It is
practically impossible to add all possible operations in the data classes, but
it is easy to add another implementation of a visitor interface. Implementing
an interface in practice helps a lot to think of all possible cases.

The visitor pattern uses visitor interfaces to operate on the similar elements
of a data structure. Each interface often has many implementations. A great
disadvantage at this time is that visitor methods can invoke one another
(directly or indirectly), but they can't communicate easily. Since the
implementations can't add their own parameters or return values, they often
have to rely on fields to pass values back and forth. This is more
error-prone. Still, the advantages of the visitor pattern outweigh the
disadvantages.

### Dependency injection

The library classes heavily use _constructor-based dependency injection_, to
create immutable instances. Notably the visitor classess are often like
commands that are combined in an immutable structure, via constructors. You
can execute such commands by applying the visitors to the data classes.
