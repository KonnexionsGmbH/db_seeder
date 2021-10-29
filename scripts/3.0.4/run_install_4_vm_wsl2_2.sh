#!/bin/bash

set -e

# ----------------------------------------------------------------------------------
#
# run_install_4_vm_wsl2_2.sh: Install a db_seeder_dev environment for Ubuntu 20.04 - Step 2.
#
# ------------------------------------------------------------------------------

export PWD_PREVIOUS="${PWD}"
cd ~/

echo " "
echo "Script $0 is now running"

export LOG_FILE=run_install_4_vm_wsl2_2.log

echo " "
echo "You can find the run log in the file ${LOG_FILE}"
echo " "

exec &> >(tee -i ${LOG_FILE}) 2>&1
sleep .1

echo "=============================================================================="
echo "Start $0"
echo "------------------------------------------------------------------------------"
echo "Install a ml_dev environment for Ubuntu 20.04 - Step 2."
echo "------------------------------------------------------------------------------"
echo "HOST_ENVIRONMENT                  : ${HOST_ENVIRONMENT}"
echo "USER                              : ${USER}"
echo "------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "=============================================================================="
echo "Step: Install asdf - part 2"
echo "------------------------------------------------------------------------------"
echo " "
echo "Current asdf version is: $(asdf --version)"
echo " "
echo "=============================================================================="
sudo rm -rf ~/.asdf/downloads/gradle
sudo rm -rf ~/.asdf/downloads/java

sudo rm -rf ~/.asdf/installs/gradle
sudo rm -rf ~/.asdf/installs/java

sudo rm -rf ~/.asdf/plugins/gradle
sudo rm -rf ~/.asdf/plugins/java

touch ~/.tool-versions
echo "------------------------------------------------------------------------------"
echo "Step: Install Java SE Development Kit - Version ${VERSION_JAVA}"
echo "------------------------------------------------------------------------------"
asdf plugin add java
asdf install java ${VERSION_JAVA}
asdf global java ${VERSION_JAVA}
echo " "
echo "=============================================================================> Version  Java: "
echo " "
echo "Current version of Java SE Development Kit: $(java -version)"
echo " "
echo "=============================================================================="

if [ "${HOST_ENVIRONMENT}" = "vm" ]; then
    echo "------------------------------------------------------------------------------"
    echo "Step: Install DBeaver - Version ${VERSION_DBEAVER}"
    echo "------------------------------------------------------------------------------"
    wget --quiet https://github.com/dbeaver/dbeaver/releases/download/${VERSION_DBEAVER}/dbeaver-ce-${VERSION_DBEAVER}-linux.gtk.x86_64.tar.gz
    sudo tar -xf dbeaver-ce-${VERSION_DBEAVER}-linux.gtk.x86_64.tar.gz
    sudo rm -rf ${HOME_DBEAVER}
    sudo cp -r dbeaver ${HOME_DBEAVER}
    sudo rm -rf dbeaver
    sudo rm -f dbeaver-ce-*.tar.gz
    echo " "
    echo "=============================================================================> Version  DBeaver: "
    echo " "
    echo "Current version of DBeaver: $(${HOME_DBEAVER}/dbeaver -help)"
    echo " "
    echo "=============================================================================="
fi

echo "------------------------------------------------------------------------------"
echo "Step: Install Eclipse - Version ${VERSION_ECLIPSE_1}/${VERSION_ECLIPSE_2}"
echo "------------------------------------------------------------------------------"
wget --quiet https://www.mirrorservice.org/sites/download.eclipse.org/eclipseMirror/technology/epp/downloads/release/${VERSION_ECLIPSE_1}/${VERSION_ECLIPSE_2}/eclipse-java-${VERSION_ECLIPSE_1}-${VERSION_ECLIPSE_2}-linux-gtk-x86_64.tar.gz
sudo tar -xf eclipse-java-${VERSION_ECLIPSE_1}-${VERSION_ECLIPSE_2}-linux-gtk-x86_64.tar.gz
sudo rm -rf ${HOME_ECLIPSE}
sudo cp -r eclipse ${HOME_ECLIPSE}
sudo rm -rf eclipse
sudo rm -f eclipse-*.tar.gz
cd ..
echo " "
echo "=============================================================================> Version  Eclipse: "
echo " "
echo "Current version of Eclipse: $(cat ${HOME_ECLIPSE}/configuration/config.ini | grep 'eclipse.buildId=')"
echo " "
echo "=============================================================================="

echo "------------------------------------------------------------------------------"
echo "Step: Install Gradle - Version ${VERSION_GRADLE}"
echo "------------------------------------------------------------------------------"
asdf plugin add gradle
asdf install gradle ${VERSION_GRADLE}
asdf global gradle ${VERSION_GRADLE}
echo " "
echo "=============================================================================> Version  Gradle: "
echo " "
echo "Current version of Gradle: $(gradle --version)"
echo " "
echo "=============================================================================="

echo "------------------------------------------------------------------------------"
echo "Step: Cleanup"
echo "------------------------------------------------------------------------------"
sudo apt-get -qy autoremove
sudo rm -rf /tmp/*

cd "${PWD_PREVIOUS}"

echo "=============================================================================> Current Date: "
echo " "
date
echo " "
# Show Environment Variables -------------------------------------------------------
echo "=============================================================================> Environment variable LANG: "
echo " "
echo "${LANG}"
echo " "
echo "=============================================================================> Environment variable LANGUAGE: "
echo " "
echo "${LANGUAGE}"
echo " "
echo "=============================================================================> Environment variable LC_ALL: "
echo " "
echo "${LC_ALL}"
echo " "
echo "=============================================================================> Environment variable LD_LIBRARY_PATH: "
echo " "
echo ${LD_LIBRARY_PATH}
echo " "
echo "=============================================================================> Environment variable ORACLE_HOME: "
echo " "
echo ${ORACLE_HOME}
echo " "
echo "=============================================================================> Environment variable PATH: "
echo " "
echo "${PATH}"
echo " "
# Show component versions ----------------------------------------------------------
echo "=============================================================================> Components"
( /bin/bash ~/kxn_install/run_version_check.sh )
echo " "
echo "------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "------------------------------------------------------------------------------"
echo "End   $0"
echo "=============================================================================="

exit 0
