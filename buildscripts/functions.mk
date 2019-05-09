# Support functions for building ProGuard with make.

SRC = src
OUT = out
LIB = ../lib

JAVA_TARGET = 1.8

ifeq ($(CLASSPATH),)
  CLASSPATH_OPTION =
else
  CLASSPATH_OPTION = -classpath $(CLASSPATH)
endif

TARGET_JAR = $(LIB)/$(TARGET).jar

# Command to download dependencies.

DOWNLOAD = wget -O
#DOWNLOAD = curl -L -o

# Function to find the resource files of a given target.

define RESOURCES
  $(shell find $(SRC)/$(dir $(1)) -maxdepth 1 \( -name \*.properties -o -name \*.png -o -name \*.gif -o -name \*.pro \) -printf $(OUT)/$(dir $(1))%P\\n)
endef

# Rules for creating the jars.

all: $(TARGET_JAR)

$(TARGET_JAR): $(OUT)/$(MAIN_CLASS).class $(LIB)
ifeq ($(UPDATE_JAR),true)
	jar -uf  $@ -C $(OUT) $(dir $(MAIN_CLASS))
else
ifeq ($(INCLUDE_MANIFEST),true)
	jar -cfm $@ $(SRC)/META-INF/MANIFEST.MF -C $(OUT) $(dir $(MAIN_CLASS))
else
	jar -cf $@ -C $(OUT) $(dir $(MAIN_CLASS))
endif
endif

$(TARGET_JAR): $(call RESOURCES,$(MAIN_CLASS))

# Rule for compiling the class files.

$(OUT)/%.class: $(SRC)/%.java $(subst :, ,$(CLASSPATH)) $(OUT)
	javac -nowarn -Xlint:none $(CLASSPATH_OPTION) -source $(JAVA_TARGET) -target $(JAVA_TARGET) -sourcepath $(SRC) -d $(OUT) $(filter %.java,$^) 2>&1 | sed -e 's|^|  |'

# Rule for copying the resource files.

$(OUT)/%.properties $(OUT)/%.png $(OUT)/%.gif $(OUT)/%.pro:
	cp $(subst $(OUT),$(SRC),$@) $@

# Rule for creating output directories.

$(OUT) $(LIB):
	mkdir -p $@

# Rule for dependencies on other modules.

../%/$(OUT):
	cd $(dir $@) && $(MAKE)

clean:
	rm -fr $(OUT)

.PHONY: all clean
