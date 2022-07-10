# We only want minification, not obfuscation.
-dontobfuscate
-verbose

# Entry point to the app.
-keep class com.example.App { *; }
