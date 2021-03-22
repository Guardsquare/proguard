package proguard.gradle.configuration;

import org.gradle.util.GradleVersion;

class GradleVersionCheck {
    public static boolean isAtLeastGradle7() {
        return GradleVersion.current().compareTo(GradleVersion.version("7.0")) >= 0;
    }
}
