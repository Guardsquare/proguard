# ReTrace

**ReTrace** is a companion tool for ProGuard and DexGuard that
'de-obfuscates' stack traces.

When an obfuscated program throws an exception, the resulting stack
trace typically isn't very informative. Class names and method names
have been replaced by short meaningless strings. Source file names and
line numbers are missing altogether. While this may be intentional, it
can also be inconvenient when debugging problems.

<table class="diagram" align="center">
  <tr>
    <td><div class="lightgreen box">Original code</div></td>
    <td><div class="right arrow">ProGuard / DexGuard</div></td>
    <td><div class="darkgreen box">Obfuscated code</div></td>
  </tr>
  <tr>
    <td/>
    <td><div class="overlap"><div class="down arrow"></div></div>
        <div class="overlap"><div class="green box">Mapping file</div></div></td>
    <td><div class="down arrow">Crash!</div></td>
  </tr>
  <tr>
    <td><div class="lightgreen box">Readable stack trace</div></td>
    <td><div class="left arrow">ReTrace</div></td>
    <td><div class="darkgreen box">Obfuscated stack trace</div></td>
  </tr>
</table>

ReTrace can read an obfuscated stack trace and restore it to what it
would look like without obfuscation. The restoration is based on the
mapping file that an obfuscator (like ProGuard, DexGuard or R8) can write out while obfuscating. The mapping
file links the original class names and class member names to their
obfuscated names.

## Usage {: #usage }

You can find the ReTrace jar in the `lib` directory of the ProGuard
distribution. To run ReTrace, just type:

`java -jar retrace.jar `\[*options...*\] *mapping\_file*
\[*stacktrace\_file*\]

Alternatively, the `bin` directory contains some short Linux and Windows
scripts containing this command. These are the arguments:

*mapping\_file*
: Specifies the name of the mapping file.

*stacktrace\_file*
: Optionally specifies the name of the file containing the stack trace. If
  no file is specified, a stack trace is read from the standard input. The
  stack trace must be encoded with UTF-8 encoding. Blank lines and
  unrecognized lines are ignored.

The following options are supported:

`-verbose`
: Specifies to print out more informative stack traces that include not only
  method names, but also method return types and arguments.

`-regex` *regular\_expression*

: Specifies the regular expression that is used to parse the lines in the
  stack trace. Specifying a different regular expression allows to
  de-obfuscate more general types of input than just stack traces. A relatively
  simple expression like this works for basic stack trace formats:

        (?:.*? at %c\.%m\(%s(?::%l)?\))|(?:(?:.*?[:"] +)?%c(?::.*)?)

  It for instance matches the following lines:

    Exception in thread "main" myapplication.MyException: Some message
        at com.example.MyClass.myMethod(MyClass.java:123)

  The regular expression is a Java regular expression (cfr. the
  documentation of `java.util.regex.Pattern`), with a few additional
  wildcards:

  | Wildcard | Description                                | Example
  |----------|--------------------------------------------|-------------------------------------------
  | `%c`     | matches a class name                       | `com.example.MyClass`
  | `%C`     | matches a class name with slashes          | `com/example/MyClass`
  | `%t`     | matches a field type or method return type | `com.example.MyClass[]`
  | `%f`     | matches a field name                       | `myField`
  | `%m`     | matches a method name                      | `myMethod`
  | `%a`     | matches a list of method arguments         | `boolean,int`
  | `%s`     | matches a source file name                 | `MyClass.java`
  | `%l`     | matches a line number inside a method      | `123`

  Elements that match these wildcards are de-obfuscated,
  when possible. Note that regular expressions must not contain any
  capturing groups. Use non-capturing groups instead: `(?:`...`)`

  You can print out the default regular expression by running ReTrace without
  arguments. It also matches more complex stack traces.

The restored stack trace is printed to the standard output. The
completeness of the restored stack trace depends on the presence of line
number tables in the obfuscated class files:

- If all line numbers have been preserved while obfuscating the
  application, ReTrace will be able to restore the stack
  trace completely.
- If the line numbers have been removed, mapping obfuscated method
  names back to their original names has become ambiguous. Retrace
  will list all possible original method names for each line in the
  stack trace. The user can then try to deduce the actual stack trace
  manually, based on the logic of the program.

Source file names are currently restored based on the names of the
outer-most classes. If you prefer to keep the obfuscated name, you can
replace `%s` in the default regular expression by `.*`

Unobfuscated elements and obfuscated elements for which no mapping is
available will be left unchanged.


## Examples {: #examples }

### Restoring a stack trace with line numbers {: #with}

Assume for instance an application has been obfuscated using the
following extra options:

    -printmapping mapping.txt

    -renamesourcefileattribute MyApplication
    -keepattributes SourceFile,LineNumberTable

Now assume the processed application throws an exception:

    java.io.IOException: Can't read [dummy.jar] (No such file or directory)
        at proguard.y.a(MyApplication:188)
        at proguard.y.a(MyApplication:158)
        at proguard.y.a(MyApplication:136)
        at proguard.y.a(MyApplication:66)
        at proguard.ProGuard.c(MyApplication:218)
        at proguard.ProGuard.a(MyApplication:82)
        at proguard.ProGuard.main(MyApplication:538)
    Caused by: java.io.IOException: No such file or directory
        at proguard.d.q.a(MyApplication:50)
        at proguard.y.a(MyApplication:184)
        ... 6 more

If we have saved the stack trace in a file `stacktrace.txt`, we can use
the following command to recover the stack trace:

    retrace mapping.txt stacktrace.txt

The output will correspond to the original stack trace:

    java.io.IOException: Can't read [dummy.jar] (No such file or directory)
        at proguard.InputReader.readInput(InputReader.java:188)
        at proguard.InputReader.readInput(InputReader.java:158)
        at proguard.InputReader.readInput(InputReader.java:136)
        at proguard.InputReader.execute(InputReader.java:66)
        at proguard.ProGuard.readInput(ProGuard.java:218)
        at proguard.ProGuard.execute(ProGuard.java:82)
        at proguard.ProGuard.main(ProGuard.java:538)
    Caused by: java.io.IOException: No such file or directory
        at proguard.io.DirectoryPump.pumpDataEntries(DirectoryPump.java:50)
        at proguard.InputReader.readInput(InputReader.java:184)
        ... 6 more

### Restoring a stack trace with line numbers (verbose) {: #withverbose}

In the previous example, we could also use the verbose flag:

    java -jar retrace.jar -verbose mapping.txt stacktrace.txt

The output will then look as follows:

    java.io.IOException: Can't read [dummy.jar] (No such file or directory)
        at proguard.InputReader.void readInput(java.lang.String,proguard.ClassPathEntry,proguard.io.DataEntryReader)(InputReader.java:188)
        at proguard.InputReader.void readInput(java.lang.String,proguard.ClassPath,int,int,proguard.io.DataEntryReader)(InputReader.java:158)
        at proguard.InputReader.void readInput(java.lang.String,proguard.ClassPath,proguard.io.DataEntryReader)(InputReader.java:136)
        at proguard.InputReader.void execute(proguard.classfile.ClassPool,proguard.classfile.ClassPool)(InputReader.java:66)
        at proguard.ProGuard.void readInput()(ProGuard.java:218)
        at proguard.ProGuard.void execute()(ProGuard.java:82)
        at proguard.ProGuard.void main(java.lang.String[])(ProGuard.java:538)
    Caused by: java.io.IOException: No such file or directory
        at proguard.io.DirectoryPump.void pumpDataEntries(proguard.io.DataEntryReader)(DirectoryPump.java:50)
        at proguard.InputReader.void readInput(java.lang.String,proguard.ClassPathEntry,proguard.io.DataEntryReader)(InputReader.java:184)
        ... 6 more

### Restoring a stack trace without line numbers {: #without}

Assume for instance the application has been obfuscated using
the following extra options, this time without preserving the line
number tables:

    -printmapping mapping.txt

A stack trace `stacktrace.txt` will then lack line number information,
showing "Unknown source" instead:

    java.io.IOException: Can't read [dummy.jar] (No such file or directory)
        at proguard.y.a(Unknown Source)
        at proguard.y.a(Unknown Source)
        at proguard.y.a(Unknown Source)
        at proguard.y.a(Unknown Source)
        at proguard.ProGuard.c(Unknown Source)
        at proguard.ProGuard.a(Unknown Source)
        at proguard.ProGuard.main(Unknown Source)
    Caused by: java.io.IOException: No such file or directory
        at proguard.d.q.a(Unknown Source)
        ... 7 more

We can still use the same command to recover the stack trace:

    java -jar retrace.jar mapping.txt stacktrace.txt

The output will now list all alternative original method names for each
ambiguous obfuscated method name:

    java.io.IOException: Can't read [dummy.jar] (No such file or directory)
        at proguard.InputReader.execute(InputReader.java)
                                readInput(InputReader.java)
        at proguard.InputReader.execute(InputReader.java)
                                readInput(InputReader.java)
        at proguard.InputReader.execute(InputReader.java)
                                readInput(InputReader.java)
        at proguard.InputReader.execute(InputReader.java)
                                readInput(InputReader.java)
        at proguard.ProGuard.readInput(ProGuard.java)
        at proguard.ProGuard.execute(ProGuard.java)
                             optimize(ProGuard.java)
                             createPrintStream(ProGuard.java)
                             closePrintStream(ProGuard.java)
                             fileName(ProGuard.java)
        at proguard.ProGuard.main(ProGuard.java)
    Caused by: java.io.IOException: No such file or directory
        at proguard.io.DirectoryPump.pumpDataEntries(DirectoryPump.java)
                                     readFiles(DirectoryPump.java)

For instance, ReTrace can't tell if the method `a` corresponds to
`execute` or to `readInput`, so it lists both. You need to figure it out
based on your knowledge of the application. Having line numbers and
unambiguous names clearly is a lot easier, so you should consider
[preserving the line numbers](../configuration/examples.md#stacktrace) when you
obfuscate your application.
