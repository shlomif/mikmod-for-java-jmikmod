
TARGET = dest

include defs.mak


SOURCES = $(addprefix src/,$(addsuffix .html.wml,$(PAGES)))

DESTS = $(patsubst src/%.html.wml,$(TARGET)/%.html,$(SOURCES))

RAW_FILES = $(IMAGES)
RAW_FILES_SOURCES = $(addprefix src/,$(RAW_FILES))
RAW_FILES_DEST = $(addprefix $(TARGET)/,$(RAW_FILES))

PODS = $(addprefix docs/,roadmap spec whitepaper version-0.2-spec)
PODS_DESTS_HTMLS = $(patsubst %,$(TARGET)/%.html,$(PODS))
PODS_DESTS_PODS = $(patsubst %,$(TARGET)/%.pod,$(PODS))

# PACKAGES_DIR = $(TARGET)/download/arcs
# PACKAGES = $(shell cd temp && cd lk-module-compiler-final && ls)
# PACKAGES_DESTS = $(addprefix $(PACKAGES_DIR)/,$(PACKAGES))

SUBDIRS_DEST = $(addprefix $(TARGET)/,$(SUBDIRS))

WML_FLAGS += --passoption=2,-X --passoption=3,-I../lib/ --passoption=7,"-S imgsize" -DROOT~.

RSYNC = rsync --progress --verbose --rsh=ssh

ARC_NAME := $(shell cd temp && ./get-arc-name.sh)

LIBRARY_FILES = template.wml lib/books.wml

all: dest $(SUBDIRS_DEST) $(DESTS) $(RAW_FILES_DEST)

dest:
	if [ ! -e $@ ] ; then mkdir $@ ; fi

$(DESTS) : $(TARGET)/% : src/%.wml $(LIBRARY_FILES)
	(cd src && wml $(WML_FLAGS) -DFILENAME=$(patsubst src/%.wml,%,$<) $(patsubst src/%,%,$<)) > $@

$(RAW_FILES_DEST) : $(TARGET)/% : src/%
	cp -f $< $@

$(SUBDIRS_DEST) : % :
	if [ ! -e $@ ] ; then mkdir $@ ; fi

$(PODS_DESTS) : $(TARGET)/% : src/%
	cp -f $< $@

$(PODS_DESTS_HTMLS) : $(TARGET)/%.html : src/%.pod
	pod2html $< > $@

# $(PACKAGES_DESTS) : $(PACKAGES_DIR)/% : ./temp/lk-module-compiler-final/%
# 	cp -f $< $@

upload: upload_shlomif

upload_shlomif: all
	(cd dest && $(RSYNC) -r * hostgator:domains/jmikmod/public_html/)

upload_berlios: all
	(cd dest && $(RSYNC) -r * shlomif@shell.berlios.de:/home/groups/jmikmod/htdocs/)

.PHONY:


