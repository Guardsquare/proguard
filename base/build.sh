#!/bin/bash
#
# GNU/Linux build script for ProGuard.

cd $(dirname "$0")

source ../buildscripts/functions.sh

MAIN_CLASS=proguard.ProGuard

GSON_VERSION=2.8.5
GSON_URL=https://jcenter.bintray.com/com/google/code/gson/gson/${GSON_VERSION}/gson-${GSON_VERSION}.jar
GSON_JAR=$LIB/gson-${GSON_VERSION}.jar

# Make sure the ProGuard core has been compiled.
if [[ ! -d ../core/$OUT || ! -f "$PROGUARD_JAR" ]]; then
  ../core/build.sh || exit 1
fi

# Compile and package.
download  "$GSON_URL" "$GSON_JAR" && \
compile   $MAIN_CLASS "../core/$OUT:$GSON_JAR" && \
updatejar "$PROGUARD_JAR" || exit 1
