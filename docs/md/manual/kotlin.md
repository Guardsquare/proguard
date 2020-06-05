The Kotlin compiler injects code and metadata into the classes that it generates to support features not natively supported by the Java and Android environments. The metadata injected by the Kotlin compiler takes the shape of an annotation added to classes which leaks semantic information that can aid attackers.



## Configuration

In most cases, you do not need to keep Kotlin metadata for app projects - therefore, no configuration changes are necessary and the Kotlin metadata can be safely removed.

ProGuard will only keep the Kotlin metadata of a class if you explicitly keep that class or one of its members and you add `-keepkotlinmetadata` option to your configuration.

For example, if you have the following keep rule for a Kotlin class named `com.example.KotlinExample`, by default the class will be kept but its metadata will not:

```
# Keep the class com.example.KotlinExample
-keep class com.example.KotlinExample
```

You can add `-keepkotlinmetadata` to your configuration to instruct ProGuard to keep and adapt Kotlin metadata:

```
# Add this option to tell ProGuard to keep and adapt Kotlin metadata
-keepkotlinmetadata
```


### App Projects


The most common case to keep Kotlin metadata would be if you use the [kotlin-reflect](https://kotlinlang.org/docs/reference/reflection.html) library. Just like when using Java reflection, you will need `-keep` rules in your configuration to keep the specific classes and members accessed through reflection.

In this case, to instruct ProGuard to keep and adapt the corresponding Kotlin metadata, add the following to your configuration:

```
-keepkotlinmetadata
```

A popular framework that relies on reflection is [Jackson](https://github.com/FasterXML/jackson-module-kotlin).

### Library Projects

When developing an SDK that exposes Kotlin-specific features to its users, you need to preserve the metadata of the public API.
These include features such as named parameters, suspend functions, top-level functions and type aliases.

In the case of a library, you would already be keeping the public API so you can simply add the following
to your configuration:

```
-keepkotlinmetadata
```

## Protection

### Obfuscation

ProGuard will apply the same obfuscations to Kotlin identifiers in metadata, such as class or member names, to match those in the Java class files.
This ensures that, even where the Kotlin metadata is required, no sensitive names remain in the metadata.

### Shrinking

ProGuard will remove all unused Kotlin metadata components such as unused functions and properties.
This ensures that, even where the Kotlin metadata is required, only the used components are kept.



### Data Classes

Data classes in Kotlin have an auto-generated `toString` method that lists all properties
of the class and their value. ProGuard automatically detects these classes and adapts
the names of properties to their obfuscated counterpart.

### Intrinsics Null Checks

To better support java interoperability, Kotlin injects numerous method calls to e.g.
check that a parameter is not null when it wasn't marked as `Nullable`. In pure Kotlin
codebases, these injected method calls are unnecessary and instead leak information
via their parameters (e.g. names of checked parameters).

ProGuard automatically detects calls to these methods and removes the Strings to
ensure that the resulting code contains no references to original parameter names, member names etc.


