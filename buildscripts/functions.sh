#!/bin/bash
#
# Support functions for building ProGuard.

SRC=src
OUT=out
LIB=../lib

PROGUARD_JAR=$LIB/proguard.jar
RETRACE_JAR=$LIB/retrace.jar
PROGUARD_GUI_JAR=$LIB/proguardgui.jar
ANNOTATIONS_JAR=$LIB/annotations.jar

set -o pipefail

function compile {
  # Compile java source files.
  echo "Compiling $(basename $PWD) ($1)..."
  mkdir -p "$OUT" && \
  javac -nowarn -Xlint:none -sourcepath "$SRC" -d "$OUT" \
    "$SRC"/${1//.//}.java 2>&1 \
    | sed -e 's|^|  |' || return 1

  # Copy resource files.
  (cd "$SRC" && \
   find proguard \
     \( -name \*.properties -o -name \*.png -o -name \*.gif -o -name \*.pro \) \
     -exec cp --parents {} "../$OUT" \; )
}

function createjar {
  echo "Creating $1..."
  if [ -f "$SRC/META-INF/MANIFEST.MF" ]; then
    jar -cfm "$1" "$SRC/META-INF/MANIFEST.MF" -C "$OUT" proguard
  else
    jar -cf "$1" -C "$OUT" proguard
  fi
}

function updatejar {
  echo "Updating $1..."
  jar -uf "$1" -C "$OUT" proguard
}
