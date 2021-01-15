To run ProGuard, just type:

=== "Linux/macOS"
    ```bash
    bin/proguard.sh <options...>
    ```

=== "Windows"
    ```
    bin\proguard <options...>
    ```

Typically, you'll put most options in a configuration file (say,
`myconfig.pro`), and just call:

=== "Linux/macOS"
    ```bash
    bin/proguard.sh @myconfig.pro
    ```

=== "Windows"
    ```bash
    bin\proguard @myconfig.pro
    ```

You can combine command line options and options from configuration
files. For instance:

=== "Linux/macOS"
    ```bash
    bin/proguard.sh @myconfig.pro -verbose
    ```

=== "Windows"
    ```
    bin\proguard @myconfig.pro -verbose
    ```

You can add comments in a configuration file, starting with a `#`
character and continuing until the end of the line.

Extra whitespace between words and delimiters is ignored. File names
with spaces or special characters should be quoted with single or double
quotes.

Options can be grouped arbitrarily in arguments on the command line and
in lines in configuration files. This means that you can quote arbitrary
sections of command line options, to avoid shell expansion of special
characters, for instance.

The order of the options is generally irrelevant. For quick experiments,
you can abbreviate them to their first unique characters.

All available options are described in the [Configuration section](../configuration/usage.md).
