#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_install_4_vm_wsl2_2.sh: Install an environment for Ubuntu 20.04 - Step 2.
#
# ------------------------------------------------------------------------------

export PWD_PREVIOUS=${PWD}
cd ~/

echo " "
echo "Script $0 is now running"

export LOG_FILE=run_install_4_vm_wsl2_2.log

echo " "
echo "You can find the run log in the file ${LOG_FILE}"
echo " "

exec &> >(tee -i ${LOG_FILE}) 2>&1
sleep .1

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "Install an environment for Ubuntu 20.04 - Step 2."
echo "--------------------------------------------------------------------------------"
echo "USER                              : ${USER}"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"
echo "Supplement necessary system software"
echo "--------------------------------------------------------------------------------"

echo "--------------------------------------------------------------------------------"
echo "Step: Install asdf - part 2"
echo "--------------------------------------------------------------------------------"
echo "Current asdf version is: $(asdf --version)"
echo "================================================================================"
sudo rm -rf ~/.asdf/downloads/gradle
sudo rm -rf ~/.asdf/downloads/java
sudo rm -rf ~/.asdf/downloads/vim

sudo rm -rf ~/.asdf/installs/gradle
sudo rm -rf ~/.asdf/installs/java
sudo rm -rf ~/.asdf/installs/vim

sudo rm -rf ~/.asdf/plugins/gradle
sudo rm -rf ~/.asdf/plugins/java
sudo rm -rf ~/.asdf/plugins/vim

touch ~/.tool-versions
# echo "================================================================================"
# asdf plugin list all
echo "================================================================================"

if [ "${HOST_ENVIRONMENT}" = "vm" ]; then
    echo "--------------------------------------------------------------------------------"
    echo "Step: Install Docker Desktop"
    echo "--------------------------------------------------------------------------------"
    if [ "$(getent group docker)" = "" ]; then
        sudo groupadd docker
    fi
    sudo usermod -aG docker ${USER}
    echo "================================================================================"
    echo "Current Docker Desktop version is: $(sudo docker version)"
    echo "================================================================================"
fi

echo "--------------------------------------------------------------------------------"
echo "Step: Install Java SE Development Kit - Version ${VERSION_JAVA}"
echo "--------------------------------------------------------------------------------"
sudo apt-get install -qy libcurl4 \
                         libcurl4-openssl-dev
asdf plugin add java
# asdf list all java
asdf install java ${VERSION_JAVA}
asdf global java ${VERSION_JAVA}
echo "================================================================================"
ls -ll ~/.asdf/installs/java
echo "================================================================================"
echo "Current Java SE Development Kit version is: $(java -version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install Eclipse - Version /${VERSION_ECLIPSE_1}./${VERSION_ECLIPSE_2}"
echo "--------------------------------------------------------------------------------"
wget --quiet https://www.mirrorservice.org/sites/download.eclipse.org/eclipseMirror/technology/epp/downloads/release/${VERSION_ECLIPSE_1}/${VERSION_ECLIPSE_2}/eclipse-java-${VERSION_ECLIPSE_1}-${VERSION_ECLIPSE_2}-linux-gtk-x86_64.tar.gz
sudo tar -xf eclipse-java-${VERSION_ECLIPSE_1}-${VERSION_ECLIPSE_2}-linux-gtk-x86_64.tar.gz
sudo rm -rf ${HOME_ECLIPSE}
sudo cp -r eclipse ${HOME_ECLIPSE}
sudo rm -rf eclipse
echo "================================================================================"
echo "Current Eclipse version is: $(echo '${VERSION_ECLIPSE_1}-${VERSION_ECLIPSE_2}')"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install Gradle - Version ${VERSION_GRADLE}"
echo "--------------------------------------------------------------------------------"
asdf plugin add gradle
# asdf list all gradle
asdf install gradle ${VERSION_GRADLE}
asdf global gradle ${VERSION_GRADLE}
echo "================================================================================"
ls -ll ~/.asdf/installs/gradle
echo "================================================================================"
echo "Current Gradle version is: $(gradle --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install Vim - Version ${VERSION_VIM}"
echo "--------------------------------------------------------------------------------"
asdf plugin add vim
# asdf list all vim
asdf install vim ${VERSION_VIM}
asdf global vim ${VERSION_VIM}
echo "================================================================================"
ls -ll ~/.asdf/installs/vim
echo "================================================================================"
echo "Current Vim version is: $(vim --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Cleanup"
echo "--------------------------------------------------------------------------------"
sudo apt-get -qy autoremove

sudo rm -rf curl
sudo rm -rf dos2unix
sudo rm -rf wget

sudo rm -f get-pip.py

sudo rm -f *.gz
sudo rm -f *.gz.*
sudo rm -f *.rpm
sudo rm -f *.rpm.*
sudo rm -f *.zip
sudo rm -f *.zip.*
echo "================================================================================"
ls -ll /
echo "================================================================================"

cd ${PWD_PREVIOUS}

echo "=====================================================================> Current Date: "
date
# Show Environment Variables ---------------------------------------------------
echo "=====================================================================> Environment variable GRADLE_HOME: "
echo ${GRADLE_HOME}
echo "=====================================================================> Environment variable LANG: "
echo ${LANG}
echo "=====================================================================> Environment variable LANGUAGE: "
echo ${LANGUAGE}
echo "=====================================================================> Environment variable LC_ALL: "
echo ${LC_ALL}
echo "=====================================================================> Environment variable PATH: "
echo ${PATH}
# Show component versions ------------------------------------------------------
echo "=====================================================================> Components"
echo "=====================================================================> Version  asdf: "
echo "Current version of asdf: $(asdf --version)"
echo "=====================================================================> Version  cURL: "
echo "Current version of cURL: $(curl --version)"
echo "=====================================================================> Version  dos2unix: "
echo "Current version of dos2unix: $(dos2unix --version)"
echo "=====================================================================> Version  Eclipse: "
echo "Current version of Eclipse: ${VERSION_ECLIPSE_1}-${VERSION_ECLIPSE_2}"
echo "=====================================================================> Version  Git: "
echo "Current version of Git: $(git --version)"
echo "=====================================================================> Version  Gradle: "
echo "Current version of Gradle: $(gradle --version)"
echo "=====================================================================> Version  Java: "
echo "Current version of Java SE Development Kit: $(java -version)"
echo "=====================================================================> Version  Ubuntu: "
echo "Current version of Ubuntu: $(lsb_release -a)"
echo "=====================================================================> Version  Vim: "
echo "Current version of Vim: $(vim --version)"
echo "=====================================================================> Version  wget: "
echo "Current version of Wget: $(wget --version)"
echo " "
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"

exit
