Welcome to the manual for **ProGuard** version 7.5 ([what's new?](releasenotes.md)).

ProGuard is an open-sourced Java class file shrinker, optimizer, obfuscator, and
preverifier. As a result, ProGuard processed applications and libraries are smaller and faster.

- The ***shrinking step*** detects and removes unused classes, fields, methods, and
attributes. 
- The ***optimizer step*** optimizes bytecode and removes unused instructions. 
- The ***name obfuscation step*** renames the remaining classes, fields, and methods using short meaningless names. 
- The final ***preverification step*** adds preverification information to the classes, which is required for Java Micro Edition and for Java 6 and higher.

The default Android shrinker, R8, is compatible with ProGuard [configuration](configuration/usage.md).

If you are getting started with ProGuard, please follow the [Quick Start](building.md) guide in order to arrive at a basic setup for your application or library as quickly as possible.

Experienced users can directly consult the [Configuration section](configuration/usage.md) where all features are described.

If during the process you run into any issues, please make sure to check the [Troubleshooting section](troubleshooting/troubleshooting.md).

## How it works

<div class="center">
  <div class="diagram">
    <div class="row">
      <div class="green box">Input jars</div>
      <div class="right arrow">shrink</div>
      <div class="right arrow">optimize</div>
      <div class="right arrow">obfuscate</div>
      <div class="right arrow">preverify</div>
      <div class="green box">Output jars</div>
    </div>
    <div class="distributed">
      <div class="green box">Library jars</div>
      <div class="right arrow">(unchanged)</div>
      <div class="green box">Library jars</div>
    </div>
  </div>
</div>

ProGuard first reads the **input jars** (or aars, wars, ears, zips, apks, or
directories). It then subsequently shrinks, optimizes, obfuscates, and
preverifies them. You can optionally let ProGuard perform multiple
optimization passes. ProGuard writes the processed results to one or more
**output jars** (or aars, wars, ears, zips, apks, or directories). The input
may contain resource files, whose names and contents can optionally be updated
to reflect the obfuscated class names.

ProGuard requires the **library jars** (or aars, wars, ears, zips, apks, or
directories) of the input jars to be specified. These are essentially the
libraries that you would need for compiling the code. ProGuard uses them to
reconstruct the class dependencies that are necessary for proper processing.
The library jars themselves always remain unchanged. You should still put them
in the class path of your final application.

## Entry points

In order to determine which code has to be preserved and which code can be
discarded or obfuscated, you have to specify one or more *entry points* to
your code. These entry points are typically classes with main methods,
applets, midlets, activities, etc.

- In the **shrinking step**, ProGuard starts from these seeds and recursively
  determines which classes and class members are used. All other classes and
  class members are discarded.
- In the **optimization step**, ProGuard further optimizes the code. Among
  other optimizations, classes and methods that are not entry points can be
  made private, static, or final, unused parameters can be removed, and some
  methods may be inlined.
- In the **name obfuscation step**, ProGuard renames classes and class members that
  are not entry points. In this entire process, keeping the entry points
  ensures that they can still be accessed by their original names.
- The **preverification step** is the only step that doesn't have to know the
  entry points.

The [Usage section](configuration/usage.md) of this manual describes the necessary [`-keep`
options](configuration/usage.md#keepoptions) and the [Examples section](configuration/examples.md)
provides plenty of examples.

## Reflection

Reflection and introspection present particular problems for any automatic
processing of code. In ProGuard, classes or class members in your code that
are created or invoked dynamically (that is, by name) have to be specified as
entry points too. For example, `Class.forName()` constructs may refer to any
class at run-time. It is generally impossible to compute which classes have to
be preserved (with their original names), since the class names might be read
from a configuration file, for instance. You therefore have to specify them in
your ProGuard configuration, with the same simple [`-keep`](configuration/usage.md#keep)
options.

However, ProGuard already detects and handles the following cases for you:

- `Class.forName("SomeClass")`
- `SomeClass.class`
- `SomeClass.class.getField("someField")`
- `SomeClass.class.getDeclaredField("someField")`
- `SomeClass.class.getMethod("someMethod", null)`
- `SomeClass.class.getMethod("someMethod", new Class[] { A.class,... })`
- `SomeClass.class.getDeclaredMethod("someMethod", null)`
- `SomeClass.class.getDeclaredMethod("someMethod", new Class[] { A.class,... })`
- `AtomicIntegerFieldUpdater.newUpdater(SomeClass.class, "someField")`
- `AtomicLongFieldUpdater.newUpdater(SomeClass.class, "someField")`
- `AtomicReferenceFieldUpdater.newUpdater(SomeClass.class, SomeType.class, "someField")`

The names of the classes and class members may of course be different,
but the constructs should be literally the same for ProGuard to
recognize them. The referenced classes and class members are preserved
in the shrinking phase, and the string arguments are properly updated in
the obfuscation phase.

Furthermore, ProGuard will offer some suggestions if keeping some
classes or class members appears necessary. For example, ProGuard will
note constructs like
"`(SomeClass)Class.forName(variable).newInstance()`". These might be an
indication that the class or interface `SomeClass` and/or its
implementations may need to be preserved. You can then adapt your
configuration accordingly.

!!! tip
    Generate an instrumented build to allow ProGuard finding cases of reflection at *run-time*. The tailored configuration advice for your application will be outputted to the console, and can be copy/pasted to your configuration. To do so, just enable the option [`-addconfigurationdebugging`](configuration/usage.md#addconfigurationdebugging)


For proper results, you should at least be somewhat familiar with the
code that you are processing. Obfuscating code that performs a lot of
reflection may require trial and error, especially without the necessary
information about the internals of the code.
