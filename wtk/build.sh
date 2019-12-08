#!/bin/bash
#
# GNU/Linux build script for the ProGuard Wireless Toolkit plugin.

cd $(dirname "$0")

source ../buildscripts/functions.sh

MAIN_CLASS=proguard.wtk.ProGuardObfuscator

WTK_HOME=${WTK_HOME:-/usr/local/java/wtk}

WTK_JAR=$WTK_HOME/wtklib/kenv.zip

# Make sure the WTK jar is present.
if [ ! -f "$WTK_JAR" ]; then
  echo "Please make sure the environment variable WTK_HOME is set correctly,"
  echo "if you want to compile the optional ProGuard WTK plugin."
  exit 1
fi

# Make sure ProGuard has been compiled.
if [[ ! -d ../core/$OUT ||
      ! -f "$PROGUARD_JAR" ]]; then
  ../core/build.sh || exit 1
fi

compile   $MAIN_CLASS "../core/$OUT:$WTK_JAR" && \
updatejar "$PROGUARD_JAR" || exit 1
