SUMMARY = "bitbake-alexa recipe"
DESCRIPTION = "Alexa SDK" 
SECTION = "alexa"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/alexa/avs-device-sdk.git;branch=master;protocol=https"

SRCREV = "8bf0160c5e56a3d5ebc1e1caeab14afc8658b0da"


S = "${WORKDIR}/git"


TARGET_CXXFLAGS += " -D_GLIBCXX_USE_CXX11_ABI=0 -Wno-error=class-memaccess"
inherit cmake
INSANE_SKIP_${PN} = "install-vs-shipped"
INSANE_SKIP_${PN} = "ldflags"

AVS_DIR ?= "/home/root/Alexa_SDK"


EXTRA_OECMAKE = " \
	-DCMAKE_BUILD_TYPE=DEBUG \
	-DGSTREAMER_MEDIA_PLAYER=ON \
        -DCMAKE_INSTALL_PREFIX=${D}${AVS_DIR}/avs-sdk-client \
        -DCMAKE_INSTALL_LIBDIR = ${D}${libdir}  \
        -DCMAKE_INSTALL_INCLUDEDIR = ${D}${includedir} \
        -DCMAKE_SKIP_RPATH=TRUE \
	-DPORTAUDIO=ON \
	-DPORTAUDIO_LIB_PATH=${STAGING_LIBDIR}/libportaudio.so \
	-DPORTAUDIO_INCLUDE_DIR=${STAGING_INCDIR} \
"
RDEPENDS_${PN} += "bash perl"
DEPENDS = " \
	curl \
	sqlite3 \
        portaudio-v19 \
	gstreamer1.0-plugins-base \
	gstreamer1.0-plugins-base \
        gstreamer1.0-plugins-good \
        gstreamer1.0-plugins-bad \
	gstreamer1.0-plugins-ugly \
	gstreamer1.0-libav \
"
FILES_${PN}  += "${bindir}/SampleApp"
FILES_${PN}-dev  += "${libdir} ${includedir}"
do_install() {
        install -d ${D}${bindir}
        install -m 0755 ${B}/SampleApp/src/SampleApp ${D}${bindir}
        install -d -m 0755 ${D}${libdir}
        install -d -m 0755 ${D}${includedir}
}


 





