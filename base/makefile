# GNU/Linux makefile for ProGuard.

GSON_VERSION = 2.8.5
GSON_URL     = https://jcenter.bintray.com/com/google/code/gson/gson/$(GSON_VERSION)/gson-${GSON_VERSION}.jar
GSON_JAR     = $(LIB)/gson-$(GSON_VERSION).jar

KOTLIN_VERSION           = 1.3.31
KOTLIN_STDLIB_JAR        = $(LIB)/kotlin-stdlib-$(KOTLIN_VERSION).jar
KOTLIN_STDLIB_COMMON_JAR = $(LIB)/kotlin-stdlib-common-$(KOTLIN_VERSION).jar

KOTLINX_METADATA_VERSION = 0.1.0
KOTLINX_METADATA_JVM_JAR = $(LIB)/kotlinx-metadata-jvm-$(KOTLINX_METADATA_VERSION).jar

MAIN_CLASS       = proguard/ProGuard
CLASSPATH        = ../core/$(OUT):$(GSON_JAR):$(KOTLIN_STDLIB_JAR):$(KOTLIN_STDLIB_COMMON_JAR):$(KOTLINX_METADATA_JVM_JAR)
TARGET           = proguard
UPDATE_JAR       = true
INCLUDE_MANIFEST = true

include ../buildscripts/functions.mk

$(GSON_JAR):
	$(DOWNLOAD) $(GSON_JAR) $(GSON_URL)
