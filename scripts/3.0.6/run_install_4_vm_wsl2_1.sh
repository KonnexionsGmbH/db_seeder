#!/bin/bash

set -e

# ----------------------------------------------------------------------------------
#
# run_install_4_vm_wsl2_1.sh: Install a db_seeder_dev environment for Ubuntu 20.04 - Step 1.
#
# ----------------------------------------------------------------------------------

sudo rm -rf /tmp/*

export HOST_ENVIRONMENT_DEFAULT=vm

export VERSION_DB_SEEDER_DEV=3.0.7

export VERSION_DBEAVER=21.3.5
export VERSION_ECLIPSE_1=2021-12
export VERSION_ECLIPSE_2=R
export VERSION_GRADLE=7.4
export VERSION_JAVA=openjdk-17.0.2
export VERSION_ORACLE_INSTANT_CLIENT_1=21
export VERSION_ORACLE_INSTANT_CLIENT_2=5
export VERSION_PYTHON3=3.10.2

if [ -z "$1" ]; then
    echo "=============================================================================="
    echo "vm   - Virtual Machine"
    echo "wsl2 - Windows Subsystem for Linux Version 2"
    echo "------------------------------------------------------------------------------"
    read -rp "Enter the underlying host environment type [default: ${HOST_ENVIRONMENT_DEFAULT}] " HOST_ENVIRONMENT
    export HOST_ENVIRONMENT=${HOST_ENVIRONMENT}

    if [ -z "${HOST_ENVIRONMENT}" ]; then
    export HOST_ENVIRONMENT=${HOST_ENVIRONMENT_DEFAULT}
    fi
else
    export HOST_ENVIRONMENT=$1
fi

if [ "${HOST_ENVIRONMENT}" = "vm" ]; then
    cp -r ../../../config_data/config_dbeaver/dbeaver.desktop ${HOME}/.local/share/applications/dbeaver.desktop
    cp -r ../../../config_data/config_eclipse/eclipse.desktop ${HOME}/.local/share/applications/eclipse.desktop
fi

mkdir -p ${HOME}/kxn_install
rm -rf ${HOME}/kxn_install/*

cp -r ../../../scripts/run_version_check.sh ${HOME}/kxn_install
cp -r config_dbeaver ${HOME}/kxn_install

cd ${HOME}

# Setting Environment Variables ----------------------------------------------------
export DEBIAN_FRONTEND=noninteractive
export LOCALE=en_US.UTF-8

PATH_ADD_ON=
PATH_ORIG=${PATH_ORIG}

if [ -z "${PATH_ORIG}" ]; then
    PATH_ORIG=\"${PATH}\"
else
    PATH_ORIG=\"${PATH_ORIG}\"
fi

export TIMEZONE=Europe/Zurich

echo '' >> ${HOME}/.bashrc
echo '# ----------------------------------------------------------------------------' >> ${HOME}/.bashrc
echo '# Environment db_seeder_dev for Ubuntu 20.04 - Start' >> ${HOME}/.bashrc
echo '# ----------------------------------------------------------------------------' >> ${HOME}/.bashrc
echo " "
echo "Script $0 is now running"

export LOG_FILE=run_install_4_vm_wsl2_1.log

echo ""
echo "You can find the run log in the file ${LOG_FILE}"
echo ""

exec &> >(tee -i ${LOG_FILE}) 2>&1
sleep .1

echo "=============================================================================="
echo "Start $0"
echo "------------------------------------------------------------------------------"
echo "Install a db_seeder_dev environment for Ubuntu 20.04 - Step 1."
echo "------------------------------------------------------------------------------"
echo "HOST_ENVIRONMENT                  : ${HOST_ENVIRONMENT}"
echo "USER                              : ${USER}"
echo "------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "=============================================================================="
echo "Supplement necessary system software"
echo "------------------------------------------------------------------------------"
sudo apt-get clean -qy
sudo apt-get update -qy
sudo apt-get upgrade -qy

sudo apt-get install -qy alien \
                         build-essential \
                         curl \
                         dos2unix \
                         git \
                         jq \
                         lcov \
                         libaio1 \
                         libbz2-dev \
                         libffi-dev \
                         liblzma-dev \
                         libncursesw5-dev \
                         libreadline-dev \
                         libsqlite3-dev \
                         libssl-dev \
                         libxml2-dev \
                         libxmlsec1-dev \
                         llvm \
                         locales \
                         locales \
                         lsb-release \
                         mpg123 \
                         procps \
                         software-properties-common \
                         tk-dev \
                         tzdata \
                         unzip \
                         vim \
                         wget \
                         xz-utils \
                         zlib1g-dev


echo "------------------------------------------------------------------------------"
echo "Step: Setting Locale & Timezone"
echo "------------------------------------------------------------------------------"
sudo ln -fs /usr/share/zoneinfo/${TIMEZONE} /etc/localtime
sudo dpkg-reconfigure --frontend noninteractive tzdata
sudo locale-gen "${LOCALE}"
sudo update-locale "LANG=de_CH.UTF-8 UTF-8"
sudo locale-gen --purge "de_CH.UTF-8"
sudo dpkg-reconfigure --frontend noninteractive locales

echo "------------------------------------------------------------------------------"
echo "Step: Setting up the environment: 1. Setting the environment variables"
echo "------------------------------------------------------------------------------"

# from asdf ------------------------------------------------------------------------
PATH_ADD_ON=${HOME}/.asdf/bin:${HOME}/.asdf/shims:${PATH_ADD_ON}
# from DBeaver ---------------------------------------------------------------------
export HOME_DBEAVER=/opt/dbeaver
PATH_ADD_ON=${HOME_DBEAVER}:${PATH_ADD_ON}
# from Eclipse ---------------------------------------------------------------------
export HOME_ECLIPSE=/opt/eclipse
PATH_ADD_ON=${HOME_ECLIPSE}:${PATH_ADD_ON}
# from Oracle Instant Client -------------------------------------------------------
export ORACLE_HOME=/usr/lib/oracle/${VERSION_ORACLE_INSTANT_CLIENT_1}/client64
eval echo 'export ORACLE_HOME=/usr/lib/oracle/${VERSION_ORACLE_INSTANT_CLIENT_1}/client64' >> ${HOME}/.bashrc
export LD_LIBRARY_PATH=${ORACLE_HOME}/lib:${LD_LIBRARY_PATH}
eval echo 'export LD_LIBRARY_PATH=${ORACLE_HOME}/lib:${LD_LIBRARY_PATH}' >> ${HOME}/.bashrc
PATH_ADD_ON=${ORACLE_HOME}:${PATH_ADD_ON}
# from Python 3 --------------------------------------------------------------------
PATH_ADD_ON=${HOME}/.asdf/installs/python/${VERSION_PYTHON3}/bin:${PATH_ADD_ON}

# from Locale & Timezone -----------------------------------------------------------
echo '' >> ${HOME}/.bashrc
eval echo 'export DEBIAN_FRONTEND=${DEBIAN_FRONTEND}' >> ${HOME}/.bashrc
eval echo 'export HOST_ENVIRONMENT=${HOST_ENVIRONMENT}' >> ${HOME}/.bashrc
eval echo 'export LANG=${LOCALE}' >> ${HOME}/.bashrc
eval echo 'export LANGUAGE=${LOCALE}' >> ${HOME}/.bashrc
eval echo 'export LC_ALL=${LOCALE}' >> ${HOME}/.bashrc
eval echo 'export LOCALE=${LOCALE}' >> ${HOME}/.bashrc

echo '' >> ${HOME}/.bashrc
eval echo 'export VERSION_DB_SEEDER_DEV=${VERSION_DB_SEEDER_DEV}' >> ${HOME}/.bashrc

echo '' >> ${HOME}/.bashrc
eval echo 'export VERSION_DBEAVER=${VERSION_DBEAVER}' >> ${HOME}/.bashrc
eval echo 'export VERSION_ECLIPSE_1=${VERSION_ECLIPSE_1}' >> ${HOME}/.bashrc
eval echo 'export VERSION_ECLIPSE_2=${VERSION_ECLIPSE_2}' >> ${HOME}/.bashrc
eval echo 'export VERSION_GRADLE=${VERSION_GRADLE}' >> ${HOME}/.bashrc
eval echo 'export VERSION_JAVA=${VERSION_JAVA}' >> ${HOME}/.bashrc
eval echo 'export VERSION_ORACLE_INSTANT_CLIENT_1=${VERSION_ORACLE_INSTANT_CLIENT_1}' >> ${HOME}/.bashrc
eval echo 'export VERSION_ORACLE_INSTANT_CLIENT_2=${VERSION_ORACLE_INSTANT_CLIENT_2}' >> ${HOME}/.bashrc
eval echo 'export VERSION_PYTHON3=${VERSION_PYTHON3}' >> ${HOME}/.bashrc

echo "------------------------------------------------------------------------------"
echo "Step: Setting up the environment: 2. Initializing the interactive shell session"
echo "------------------------------------------------------------------------------"
echo '' >> ${HOME}/.bashrc
echo 'alias python=python3' >> ${HOME}/.bashrc
echo 'alias vi=vim' >> ${HOME}/.bashrc
# PATH variable --------------------------------------------------------------------
echo '' >> ${HOME}/.bashrc
# from DBeaver ---------------------------------------------------------------------
eval echo 'export HOME_DBEAVER=${HOME_DBEAVER}' >> ${HOME}/.bashrc
# from Eclipse ---------------------------------------------------------------------
eval echo 'export HOME_ECLIPSE=${HOME_ECLIPSE}' >> ${HOME}/.bashrc

eval echo 'export PATH=${PATH_ORIG}:${PATH_ADD_ON}' >> ${HOME}/.bashrc
eval echo 'export PATH_ORIG=${PATH_ORIG}' >> ${HOME}/.bashrc

# from asdf ------------------------------------------------------------------------
echo '' >> ${HOME}/.bashrc
eval echo '. ${HOME}/.asdf/asdf.sh' >> ${HOME}/.bashrc
eval echo '. ${HOME}/.asdf/completions/asdf.bash' >> ${HOME}/.bashrc
# from Docker Desktop --------------------------------------------------------------
if [ "${HOST_ENVIRONMENT}" = "vm" ]; then
    echo '' >> ${HOME}/.bashrc
    echo 'if [ `id -gn` != "docker" ]; then ( newgrp docker ) fi' >> ${HOME}/.bashrc
fi

echo '' >> ${HOME}/.bashrc
echo '# ----------------------------------------------------------------------------' >> ${HOME}/.bashrc
echo '# Environment db_seeder_dev for Ubuntu 20.04 - End' >> ${HOME}/.bashrc
echo '# ----------------------------------------------------------------------------' >> ${HOME}/.bashrc

# Initializing the interactive shell session ---------------------------------------
source ${HOME}/.bashrc

echo "------------------------------------------------------------------------------"
echo "Step: Install asdf - part 1"
echo "------------------------------------------------------------------------------"
sudo rm -rf ${HOME}/.asdf
git clone https://github.com/asdf-vm/asdf.git ${HOME}/.asdf
echo "=============================================================================="

if [ "${HOST_ENVIRONMENT}" = "vm" ]; then
    echo "------------------------------------------------------------------------------"
    echo "Step: Install Docker Desktop"
    echo "------------------------------------------------------------------------------"
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" --yes
    sudo apt-key fingerprint 0EBFCD88
    sudo apt-get install -qy docker-ce \
                             docker-ce-cli \
                             containerd.io
    sudo chmod 666 /var/run/docker.sock
    if ! [ $(getent group docker | grep -q "\b$USER\b") ]; then
        sudo usermod -aG docker $USER
    fi
    docker ps     | grep "portainer"           && docker stop portainer
    docker ps -a  | grep "portainer"           && docker rm --force portainer
    docker images | grep "portainer/portainer" && docker rmi -f portainer/portainer
    docker run -d --name portainer -p 8000:8000 -p 9000:9000 -v "//var/run/docker.sock/":/var/run/docker.sock -v "/Home/portainer/":/data portainer/portainer
    echo " "
    echo "=============================================================================> Version  Docker Desktop: "
    echo " "
    echo "Current version of Docker Desktop: $(docker version)"
    echo " "
    echo "=============================================================================="
fi

echo "=============================================================================="
echo "Step: Install Oracle Instant Client - Version ${VERSION_ORACLE_INSTANT_CLIENT_1}.${VERSION_ORACLE_INSTANT_CLIENT_2}.0.0.0"
echo "------------------------------------------------------------------------------"
(
  cd /
  sudo rm -rf oracle-instantclient*
  sudo wget --no-check-certificate -nv https://download.oracle.com/otn_software/linux/instantclient/${VERSION_ORACLE_INSTANT_CLIENT_1}${VERSION_ORACLE_INSTANT_CLIENT_2}000/oracle-instantclient-basic-${VERSION_ORACLE_INSTANT_CLIENT_1}.${VERSION_ORACLE_INSTANT_CLIENT_2}.0.0.0-1.x86_64.rpm
  sudo wget --no-check-certificate -nv https://download.oracle.com/otn_software/linux/instantclient/${VERSION_ORACLE_INSTANT_CLIENT_1}${VERSION_ORACLE_INSTANT_CLIENT_2}000/oracle-instantclient-sqlplus-${VERSION_ORACLE_INSTANT_CLIENT_1}.${VERSION_ORACLE_INSTANT_CLIENT_2}.0.0.0-1.x86_64.rpm
  sudo alien -i *.rpm
  sudo rm -rf *.x86_64.rpm
)
echo " "
echo "=============================================================================> Version  Oracle Instant Client: "
echo " "
echo "Current version of Oracle Instant Client: $(sqlplus -V)"
echo " "
echo "=============================================================================="

echo " "
echo "------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "------------------------------------------------------------------------------"
echo "End   $0"
echo "=============================================================================="

exit 0
