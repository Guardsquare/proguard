-verbose

-keepattributes *Annotation*

-keep class kotlin.Metadata { *; }

# Entry point to the app.
-keep class com.example.AppKt { *; }
