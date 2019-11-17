#!/bin/bash
#
# GNU/Linux build script for ProGuard.

cd $(dirname "$0")

source ../buildscripts/functions.sh

MAIN_CLASS=proguard.*

KOTLIN_VERSION=1.3.31
KOTLIN_STDLIB_URL=https://jcenter.bintray.com/org/jetbrains/kotlin/kotlin-stdlib/${KOTLIN_VERSION}/kotlin-stdlib-${KOTLIN_VERSION}.jar
KOTLIN_STDLIB_JAR=$LIB/kotlin-stdlib-${KOTLIN_VERSION}.jar

KOTLIN_STDLIB_COMMON_URL=https://jcenter.bintray.com/org/jetbrains/kotlin/kotlin-stdlib-common/${KOTLIN_VERSION}/kotlin-stdlib-common-${KOTLIN_VERSION}.jar
KOTLIN_STDLIB_COMMON_JAR=$LIB/kotlin-stdlib-common-${KOTLIN_VERSION}.jar

KOTLINX_METADATA_VERSION=0.1.0
KOTLINX_METADATA_JVM_URL=https://jcenter.bintray.com/org/jetbrains/kotlinx/kotlinx-metadata-jvm/${KOTLINX_METADATA_VERSION}/kotlinx-metadata-jvm-${KOTLINX_METADATA_VERSION}.jar
KOTLINX_METADATA_JVM_JAR=$LIB/kotlinx-metadata-jvm-${KOTLINX_METADATA_VERSION}.jar

download  "$KOTLIN_STDLIB_URL"        "$KOTLIN_STDLIB_JAR"        && \
download  "$KOTLIN_STDLIB_COMMON_URL" "$KOTLIN_STDLIB_COMMON_JAR" && \
download  "$KOTLINX_METADATA_JVM_URL" "$KOTLINX_METADATA_JVM_JAR" && \
compile   $MAIN_CLASS "$KOTLIN_STDLIB_JAR:$KOTLIN_STDLIB_COMMON_JAR:$KOTLINX_METADATA_JVM_JAR" && \
createjar "$PROGUARD_JAR" || exit 1
