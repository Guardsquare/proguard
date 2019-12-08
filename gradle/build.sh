#!/bin/bash
#
# GNU/Linux build script for the ProGuard Gradle task and plugin.

cd $(dirname "$0")

source ../buildscripts/functions.sh

MAIN_CLASS=proguard.gradle.*

ANDROID_PATH=\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.build/gradle/3.0.0/*/gradle-3.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.build/gradle-core/3.0.0/*/gradle-core-3.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.build/builder/3.0.0/*/builder-3.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.lint/lint/26.0.0/*/lint-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.lint/lint-checks/26.0.0/*/lint-checks-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.lint/lint-api/26.0.0/*/lint-api-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.build/manifest-merger/26.0.0/*/manifest-merger-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools/sdk-common/26.0.0/*/sdk-common-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools/sdklib/26.0.0/*/sdklib-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.analytics-library/tracker/26.0.0/*/tracker-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.analytics-library/shared/26.0.0/*/shared-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.google.code.gson/gson/2.8.5/*/gson-2.8.5.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.external.org-jetbrains/uast/26.0.0/*/uast-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.build/gradle-api/3.0.0/*/gradle-api-3.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.databinding/compilerCommon/3.0.0/*/compilerCommon-3.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools/repository/26.0.0/*/repository-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-reflect/1.1.3-2/*/kotlin-reflect-1.1.3-2.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib/1.1.3-2/*/kotlin-stdlib-1.1.3-2.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.build/transform-api/2.0.0-deprecated-use-gradle-api/*/transform-api-2.0.0-deprecated-use-gradle-api.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.ow2.asm/asm-analysis/5.1/*/asm-analysis-5.1.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.ow2.asm/asm-commons/5.1/*/asm-commons-5.1.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.ow2.asm/asm-util/5.1/*/asm-util-5.1.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.ow2.asm/asm-tree/5.1/*/asm-tree-5.1.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.ow2.asm/asm/5.1/*/asm-5.1.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.jacoco/org.jacoco.report/0.7.4.201502262128/*/org.jacoco.report-0.7.4.201502262128.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.jacoco/org.jacoco.core/0.7.4.201502262128/*/org.jacoco.core-0.7.4.201502262128.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/net.sf.jopt-simple/jopt-simple/4.9/*/jopt-simple-4.9.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/net.sf.proguard/proguard-gradle/5.3.3/*/proguard-gradle-5.3.3.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.analytics-library/protos/26.0.0/*/protos-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.google.protobuf/protobuf-java/3.0.0/*/protobuf-java-3.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.build/builder-model/3.0.0/*/builder-model-3.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.build/builder-test-api/3.0.0/*/builder-test-api-3.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.ddms/ddmlib/26.0.0/*/ddmlib-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.layoutlib/layoutlib-api/26.0.0/*/layoutlib-api-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools/dvlib/26.0.0/*/dvlib-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools/common/26.0.0/*/common-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.build/apksig/3.0.0/*/apksig-3.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.squareup/javawriter/2.5.0/*/javawriter-2.5.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.bouncycastle/bcpkix-jdk15on/1.56/*/bcpkix-jdk15on-1.56.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.bouncycastle/bcprov-jdk15on/1.56/*/bcprov-jdk15on-1.56.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/it.unimi.dsi/fastutil/7.2.0/*/fastutil-7.2.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.googlecode.json-simple/json-simple/1.1/*/json-simple-1.1.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.eclipse.jdt.core.compiler/ecj/4.6.1/*/ecj-4.6.1.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.google.jimfs/jimfs/1.1/*/jimfs-1.1.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.external.lombok/lombok-ast/0.2.3/*/lombok-ast-0.2.3.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.google.guava/guava/22.0/*/guava-22.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.databinding/baseLibrary/3.0.0/*/baseLibrary-3.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.antlr/antlr4/4.5.3/*/antlr4-4.5.3.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/commons-io/commons-io/2.4/*/commons-io-2.4.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.googlecode.juniversalchardet/juniversalchardet/1.0.3/*/juniversalchardet-1.0.3.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools/annotations/26.0.0/*/annotations-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.jetbrains/annotations/13.0/*/annotations-13.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/net.sf.proguard/proguard-base/5.3.3/*/proguard-base-5.3.3.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.apache.commons/commons-compress/1.12/*/commons-compress-1.12.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/httpclient/4.2.6/*/httpclient-4.2.6.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/httpmime/4.1/*/httpmime-4.1.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/net.sf.kxml/kxml2/2.3.0/*/kxml2-2.3.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.google.code.findbugs/jsr305/1.3.9/*/jsr305-1.3.9.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.google.errorprone/error_prone_annotations/2.0.18/*/error_prone_annotations-2.0.18.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.google.j2objc/j2objc-annotations/1.1/*/j2objc-annotations-1.1.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.codehaus.mojo/animal-sniffer-annotations/1.14/*/animal-sniffer-annotations-1.14.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.intellij/annotations/12.0/*/annotations-12.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.apache.httpcomponents/httpcore/4.2.5/*/httpcore-4.2.5.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/commons-logging/commons-logging/1.1.1/*/commons-logging-1.1.1.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/commons-codec/commons-codec/1.6/*/commons-codec-1.6.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/com.android.tools.external.com-intellij/intellij-core/26.0.0/*/intellij-core-26.0.0.jar):\
$(echo $HOME/.gradle/caches/modules-2/files-2.1/org.jetbrains.trove4j/trove4j/20160824/*/trove4j-20160824.jar)

# Make sure the Android jars are present.
if [[ "$ANDROID_PATH" == *\** ]]; then
  echo "Please make sure you have downloaded the Android tools jars by building"
  echo "with Gradle 5.4.1 first, if you want to compile the optional ProGuard Gradle"
  echo "task and plugin."
  exit 1
fi

GRADLE_HOME=${GRADLE_HOME:-/usr/local/java/gradle}

GRADLE_PATH=\
$(echo $HOME/.gradle/caches/5.4.1/generated-gradle-jars/gradle-api-5.4.1.jar):\
$(echo $GRADLE_HOME/lib/groovy-all-*.jar):\
$(echo $GRADLE_HOME/lib/gradle-installation-beacon-*.jar)

# Make sure the Gradle jars are present.
if [[ "$GRADLE_PATH" == *\** ]]; then
  echo "Please make sure the environment variable GRADLE_HOME is set correctly"
  echo "for Gradle 5.4.1, if you want to compile the optional ProGuard Gradle"
  echo "task and plugin."
  exit 1
fi

# Make sure ProGuard has been compiled.
if [[ ! -d ../core/$OUT || ! -f "$PROGUARD_JAR" ]]; then
  ../core/build.sh || exit 1
fi

compile   "$MAIN_CLASS" "../core/$OUT:$ANDROID_PATH:$GRADLE_PATH" && \
updatejar "$PROGUARD_JAR" || exit 1
