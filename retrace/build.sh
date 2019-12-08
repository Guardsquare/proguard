#!/bin/bash
#
# GNU/Linux build script for ReTrace.

cd $(dirname "$0")

source ../buildscripts/functions.sh

MAIN_CLASS=proguard.retrace.ReTrace

# Make sure ProGuard has been compiled.
if [[ ! -d ../core/$OUT ||
      ! -f "$PROGUARD_JAR" ]]; then
  ../core/build.sh || exit 1
fi

compile   $MAIN_CLASS "../core/$OUT" && \
createjar "$RETRACE_JAR" || exit 1
