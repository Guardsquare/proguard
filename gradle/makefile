# GNU/Linux makefile for the ProGuard Gradle task.

ifndef GRADLE_HOME
GRADLE_HOME = /usr/local/java/gradle
endif

MAIN_CLASS  = proguard/gradle/ProGuardTask
GRADLE_JARS = $(wildcard \
              $(GRADLE_HOME)/lib/gradle-base-services-?.*.jar \
              $(GRADLE_HOME)/lib/gradle-base-services-groovy-*.jar \
              $(GRADLE_HOME)/lib/gradle-core-?.*.jar \
              $(GRADLE_HOME)/lib/gradle-core-api-*.jar \
              $(GRADLE_HOME)/lib/gradle-logging-*.jar \
              $(GRADLE_HOME)/lib/gradle-model-core-*.jar \
              $(GRADLE_HOME)/lib/gradle-process-services-*.jar \
              $(GRADLE_HOME)/lib/plugins/gradle-workers-*.jar \
              $(GRADLE_HOME)/lib/groovy-all-*.jar \
              $(GRADLE_HOME)/lib/slf4j-api-*.jar)
NOTHING    :=
SPACE      := $(NOTHING) $(NOTHING)
CLASSPATH   = ../core/$(OUT):$(subst $(SPACE),:,$(GRADLE_JARS))
TARGET      = proguard
UPDATE_JAR  = true

ifeq ($(GRADLE_JARS),)
  $(error Please make sure GRADLE_HOME is set correctly)
endif

include ../buildscripts/functions.mk

