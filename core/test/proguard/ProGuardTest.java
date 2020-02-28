package proguard;

import org.junit.jupiter.api.Test;

class ProGuardTest {

    @Test
    void main() {

        String targetLocation = "\\";
        String inputJar = "classes";
        String outputJar = "proguardClasses";
        String map_filename = "proguard_map.txt";
        String seed_filename = "proguard_seed.txt";
        String method_dir_filename = "method-dictionary.txt";
        String class_dir_filename = "class-dictionary.txt";
        String package_dir_filename = "package-dictionary.txt";

        String [] arguments = {
                "-dontshrink",
                "-dontoptimize",
                "-adaptclassstrings",
                "-keepparameternames",
                "-keep class com.study.Application",
                "-ignorewarnings",
                "-injars '" + targetLocation + inputJar + "'",
                "-outjars '" + targetLocation + outputJar + "'",
                "-printmapping '" + targetLocation + map_filename + "'",
                "-printseeds '" + targetLocation + seed_filename + "'",

                "-dontresetmembernaming",
                "-dontresetclassnaming",
                "-dontresetpackagenaming",

                "-obfuscationdictionary '" + targetLocation + method_dir_filename + "'",
                "-classobfuscationdictionary '" + targetLocation + class_dir_filename + "'",
                "-packageobfuscationdictionary '" + targetLocation + package_dir_filename + "'",

                "-dontwarn java.lang.**",
                "-dontwarn java.util.**"
        };

        ProGuard.main(arguments);
    }
}