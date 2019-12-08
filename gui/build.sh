#!/bin/bash
#
# GNU/Linux build script for the ProGuard GUI.

cd $(dirname "$0")

source ../buildscripts/functions.sh

MAIN_CLASS=proguard.gui.ProGuardGUI

# Make sure ProGuard has been compiled.
if [[ ! -d ../core/$OUT || ! -f "$PROGUARD_JAR" ]]; then
  ../core/build.sh || exit 1
fi

# Make sure ReTrace has been compiled.
if [[ ! -d ../retrace/$OUT || ! -f "$RETRACE_JAR" ]]; then
  ../retrace/build.sh || exit 1
fi

compile   $MAIN_CLASS "../core/$OUT:../retrace/$OUT" && \
createjar "$PROGUARD_GUI_JAR" || exit 1
