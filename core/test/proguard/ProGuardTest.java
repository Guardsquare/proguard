package proguard;

import org.junit.jupiter.api.Test;

class ProGuardTest {

    @Test
    void main() {

        // NOTE: Change targetLocation value here to the location where
        // inputJar and all below required items are available / expected
        String targetLocation = "\\";

        String inputJar = "classes";
        String outputJar = "proguardClasses";
        String map_filename = "proguard_map.txt";
        String seed_filename = "proguard_seed.txt";
        String members_dir_filename = "members-dictionary.txt";
        String classes_dir_filename = "classes-dictionary.txt";
        String packages_dir_filename = "packages-dictionary.txt";

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

                //  This option makes sure that the obfuscation will use all unique names for members
                "-dontresetmembernaming",
                //  This option makes sure that the obfuscation will use all unique names for classes
                "-dontresetclassnaming",
                //  This option makes sure that the obfuscation will use all unique names for packages
                "-dontresetpackagenaming",

                "-obfuscationdictionary '" + targetLocation + members_dir_filename + "'",
                "-classobfuscationdictionary '" + targetLocation + classes_dir_filename + "'",
                "-packageobfuscationdictionary '" + targetLocation + packages_dir_filename + "'",

                "-dontwarn java.lang.**",
                "-dontwarn java.util.**"
        };

        ProGuard.main(arguments);
    }
}