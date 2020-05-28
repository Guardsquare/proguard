# Contributing to ProGuard

Thank you for contributing!

- You can report issues on our [issue tracker](issues) on GitHub. Please be
  concise and complete. If we can't reproduce an issue, we most likely can't
  fix it.

- You can also clone the [source code](.) yourself and create [pull
  requests](pulls) on GitHub.

These are a few guidelines for our code and documentation.

## Basic principle

Above all, code should be consistent. Study, respect, and follow the style of
the existing code.

## Code design

The code generally defines many short classes, as the building blocks for
classes that contain the real logic. This is sometimes taken to the extreme:
even loops and conditional statements can often be implemented as separate
classes. Even though these classes are verbose and repetitive, the resulting
main code becomes much more compact, flexible, and robust.

### Data classes

Basic data classes define the data structures, with just a minimum number of
operations: Java bytecode classes, Dex bytecode classes, XML data, native
libraries, etc. They typically reflect their respective specifications
literally. The data classes have one or more `accept` methods to let the
visitor classes below operate on them.

### Visitors

The code relies a lot on the visitor pattern. Visitor classes define the
operations on the data: reading, writing, editing, transforming, etc. The
visitor classes have one or more `visit` methods to operate on data classes of
the same basic type.

For example, a Java bytecode class contains a constant pool with constants of
different types: integer constants, float constants, string constants, etc.
The data classes IntegerConstant, FloatConstant, StringConstant, etc. all
implement the basic type Constant. The visitor interface ConstantVisitor
contains methods 'visitIntegerConstant', 'visitFloatConstant',
'visitStringConstant', etc. Implementations of this visitor interface can
perform all kinds of operations on the constants.

The reasoning behind this pattern is that the data classes are very stable,
because they are directly based on the specifications (class files, dex files,
binary XML files, ELF files, etc). The operations are more dynamic. They are
changed and extended all the time. It is practically impossible to add all
possible operations in the data classes, but it is easy to add another
implementation of a visitor interface. Implementing an interface in practice
helps a lot to think of all possible cases.

The visitor pattern uses visitor interfaces to operate on the similar elements
of a data structure. Each interface often has many implementations. A great
disadvantage at this time is that visitor methods can invoke one another
(directly or indirectly), but they can't communicate easily. Since the
implementations can't add their own parameters or return values, they often
have to rely on fields to pass values back and forth. This is more
error-prone. Still, the advantages of the visitor pattern outweigh the
disadvantages.

We prefer (visitor) interfaces with many classes that implement them, so the
implementations can be used as building blocks.

### Facade classes

With many small visitor classes available as building blocks, the important
code chains these visitors together. The main ProGuard packages contain a
number of facade classes that construct and run these chains. Notably:

- `ProGuard` is the main entry point, with a long linear sequence of
  operations that it delegates to the facade classes.

- `InputReader` reads the code and resources.

- `Initializer` initializes links between the code and resources, to traverse
  the data structures more easily.

- `Marker` marks code and resources to be kept, encrypted, etc., based on the
  configuration.

- `Backporter` backports code to older versions of Java.

- `ConfigurationLoggingAdder` instruments code to give feedback about
  potentially missing configuration.

- `Shrinker` removes unused code and resources.

- `Optimizer` optimizes the code and resources.

- `Obfuscator` obfuscates the code and resources.

- `Preverifier` adds preverification metadata to code for Java 6 and higher.

### Data flow

At a high level, the flow of data inside ProGuard is as follows:

- Traverse the input data, to parse any useful data structures.

- Process the data structures in a number of steps (mainly shrinking, string
  encryption, optimization, obfuscation, class encryption).

- Traverse the input data again, this time to write to the output, by copying,
  transforming, replacing, or removing data entries. The transformations can
  be small (e.g. replacing class names in properties files) or large (e.g.
  replacing all classes and resources by obfuscated counterparts).

Important interfaces for this flow:

- `DataEntryReader` uses a push mechanism. It typically traverses data entries
  (files and nested zip entries) in the input and pushes them to data entry
  visitors. The visitors can access the actual data through input streams.

- `DataEntryWriter` uses a pull mechanism. It can return output streams for
  data entries.

The first traversal and parsing of the input data is based on implementations
of DataEntryReader.

The second traversal of the input data is based on the same implementations of
DataEntryReader, with additional implementations that use implementations of
DataEntryWriter to transform and write the output data.

### Dependency injection

We heavily use **constructor-based dependency injection**, to create immutable
objects. Notably the visitors are often like commands that are combined in an
immutable structure, via constructors. The commands are then executed by
applying the visitors to the classes or other data structures.

### Getters and setters

We try to **avoid getters and setters** outside of pure data classes.
Generally, getters and setters break data encapsulation -- "Tell, don't ask".
In our architecture, visitors often delegate to a chain of other visitors,
with the final visitor applying actual changes to the data structures.
Visitors that change the data classes often access fields directly, since they
conceptually have the same purpose as methods inside the data classes.

## Code style

Over time, we've adopted a few guidelines:

- Historically, ProGuard hasn't used generics, enums, iterable loops, variable
  arguments, or closures. More recent code can use the features of Java 8, but
  be consistent.

- Prefer **arrays** over lists, if their sizes are relatively static. The
  resulting code is easier to read, without casts or generics.

- Prefer **short classes** over long classes, although long classes that simply
  implement the many methods of their interfaces are okay.

- Prefer **short methods**, although long methods with a linear flow are okay.

- **Declare and initialize** fields and variables at the same time, when
  possible.

- Make fields **final**, when possible:
    ```
    private final CodeAttributeComposer composer = new CodeAttributeComposer();
    ```

- We generally don't make local variables final, to avoid cluttering the code
  too much.

- Maintain a **consistent order** for fields, methods (e.g.. getters and
  setters), case statements, etc. A good basic rule for new classes is that
  their contents should be in chronological order or respecting the natural
  hierarchical order of the data -- Which fields are accessed first? Which
  methods are accessed first? Which data comes first in the data structures?
  For classes that implement or resemble other classes, the contents should
  respect the same order.

## Documentation

All classes must have main javadoc:

    /**
     * This {@link ClassVisitor} collects the names of the classes that it
     * visits in the given collection.
     *
     * @author Eric Lafortune
     */

Methods should have javadoc:

    /**
     * Returns the number of parameters of the given internal method descriptor.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(ID)Z</code>".
     * @param isStatic                 specifies whether the method is static,
     *                                 e.g. false.
     * @return the number of parameters,
     *                                 e.g. 3.
     */

In practice, fields rarely have javadoc (this is debatable...)

Methods that implement interfaces generally don't have javadoc, since they
simply implement the behavior specified by the interfaces. Instead, a group of
methods that implements an interface is documented with:

    // Implementations for SomeInterface.

Most code has comments for each block of statements. The idea is to make the
code readable by just glancing over the comments (which works well if they are
colored in an IDE).

Comments are generally short sentences, starting with a capital and ending
with a period:

    // This is a comment.

For the manual (written as markdown), you should follow the excellent advice
of the [Google Developer Documentation Style
Guide](https://developers.google.com/style/).

## Code format

We strive for code that is super-aligned. Ideally, the code should look like a
set of tables, for variable declarations, parameter declarations, ternary
operators, etc.

- The basic indentation is 4 spaces (never tabs).

- Curly braces are on separate lines.

- The ideal maximum line length is 120 characters. Documentation and comments
  should stick to this limit. Code that has a nice table-like structure can
  exceed it.

- Imports of multiple classes in the same package are specified with a
  wildcard '*'.

- Try to preserve a logical, consistent order in fields, getters/setters,
  methods, variables.

- Whenever overriding a method, add the @Override annotation.

In practice, an IDE can help to obtain a clean indentation and structure, but
it is often not sufficient. Be careful to never upset a cleanly formatted
source file by automatically reformatting it.

### Naming Conventions

- Names (class names, field names, etc) should be descriptive.

- We generally don't abbreviate names, not even index variables:
    ```
    for (int index = 0; index < count; index++)
    ```
