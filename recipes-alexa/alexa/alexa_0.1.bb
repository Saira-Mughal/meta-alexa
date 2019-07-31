SUMMARY = "bitbake-alexa recipe"
DESCRIPTION = "Alexa SDK" 
SECTION = "alexa"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/alexa/avs-device-sdk.git;branch=master;protocol=https \
           file://libsInstall.sh \
"

SRCREV = "1255f3398b9c9bdd92d8fcde89c90f19f49eb21d"


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
FILES_${PN}  += "${AVS_DIR}/avs-sdk-client ${AVS_DIR}/avs-device-sdk"
FILES_${PN}-dev  += "${AVS_DIR}/libs"

do_install() {
    install -d -m 0755 ${D}${AVS_DIR}
    install -d -m 0755 ${D}${AVS_DIR}/avs-sdk-client
    find ./ -executable -type f -exec cp --parents -v {} ${D}/${AVS_DIR}/avs-sdk-client \;
    find ./ -name *.py -exec cp --parents -v {} ${D}/${AVS_DIR}/avs-sdk-client \;

    find ${D}/${AVS_DIR}/avs-sdk-client -name "*.py" -exec sed -e s#${B}#${AVS_DIR}/avs-sdk-client#g -i {} \;

    mkdir ${D}/${AVS_DIR}/libs
    cd ${D}/${AVS_DIR}/libs
    find ../avs-sdk-client -executable -type f -exec ${WORKDIR}/libsInstall.sh {} \;

    cp -r -L ${S} ${D}/${AVS_DIR}/avs-device-sdk
    cd ${D}/${AVS_DIR}/avs-device-sdk
    git repack -a -d
    rm .git/objects/info/alternates
}
BBCLASSEXTEND = "native"

