#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_install_4_vm_wsl2_1.sh: Install an environment for Ubuntu 20.04 - Step 1.
#
# ------------------------------------------------------------------------------

cd ~/

export DEBIAN_FRONTEND=noninteractive
export HOME_ECLIPSE=/opt/eclipse
export HOST_ENVIRONMENT_DEFAULT=vm
export LOCALE=en_US.UTF-8

export PATH_ADD_ON=
export PATH_ORIG=${PATH_ORIG}

if [ -z "${PATH_ORIG}" ]; then
    PATH_ORIG=\"${PATH}\"
else
    PATH_ORIG=\"${PATH_ORIG}\"
fi

export TIMEZONE=Europe/Zurich

echo '' >> ~/.bashrc
echo '# ------------------------------------------------------------------------------' >> ~/.bashrc
echo '# Environment db_seeder for Ubuntu 20.04 - Start' >> ~/.bashrc
echo '# ------------------------------------------------------------------------------' >> ~/.bashrc

echo '' >> ~/.bashrc
eval echo 'export DEBIAN_FRONTEND=${DEBIAN_FRONTEND}' >> ~/.bashrc
eval echo 'export HOME_ECLIPSE=${HOME_ECLIPSE}' >> ~/.bashrc
eval echo 'export LOCALE=${LOCALE}' >> ~/.bashrc

export VERSION_CURL=7.77.0
export VERSION_DOS2UNIX=7.4.2
export VERSION_ECLIPSE_1=2021-03
export VERSION_ECLIPSE_2=R
export VERSION_GRADLE=7.0.2
export VERSION_JAVA=openjdk-15.0.2
export VERSION_VIM=8.2.2949
export VERSION_WGET=1.21.1

echo '' >> ~/.bashrc
eval echo 'export VERSION_CURL=${VERSION_CURL}' >> ~/.bashrc
eval echo 'export VERSION_DOS2UNIX=${VERSION_DOS2UNIX}' >> ~/.bashrc
eval echo 'export VERSION_ECLIPSE_1=${VERSION_ECLIPSE_1}' >> ~/.bashrc
eval echo 'export VERSION_ECLIPSE_2=${VERSION_ECLIPSE_2}' >> ~/.bashrc
eval echo 'export VERSION_GRADLE=${VERSION_GRADLE}' >> ~/.bashrc
eval echo 'export VERSION_JAVA=${VERSION_JAVA}' >> ~/.bashrc
eval echo 'export VERSION_VIM=${VERSION_VIM}' >> ~/.bashrc
eval echo 'export VERSION_WGET=${VERSION_WGET}' >> ~/.bashrc

export VERSION_DB_SEEDER=2.9.1

echo '' >> ~/.bashrc
eval echo 'export VERSION_KXN_DEV=${VERSION_KXN_DEV}' >> ~/.bashrc

if [ -z "$1" ]; then
    echo "========================================================="
    echo "vm   - Virtual Machine"
    echo "wsl2 - Windows Subsystem for Linux Version 2"
    echo "---------------------------------------------------------"
    read -rp "Enter the underlying host environment type [default: ${HOST_ENVIRONMENT_DEFAULT}] " HOST_ENVIRONMENT
    export HOST_ENVIRONMENT=${HOST_ENVIRONMENT}

    if [ -z "${HOST_ENVIRONMENT}" ]; then
        export HOST_ENVIRONMENT=${HOST_ENVIRONMENT_DEFAULT}
    fi
else
    export HOST_ENVIRONMENT=$1
fi

echo '' >> ~/.bashrc
eval echo 'export HOST_ENVIRONMENT=${HOST_ENVIRONMENT}' >> ~/.bashrc

echo " "
echo "Script $0 is now running"

export LOG_FILE=run_install_4_vm_wsl2_1.log

echo " "
echo "You can find the run log in the file ${LOG_FILE}"
echo " "

exec &> >(tee -i ${LOG_FILE}) 2>&1
sleep .1

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "Install an environment for Ubuntu 20.04 - Step 1."
echo "--------------------------------------------------------------------------------"
echo "HOST_ENVIRONMENT                  : ${HOST_ENVIRONMENT}"
echo "USER                              : ${USER}"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"
echo "Supplement necessary system software"
echo "--------------------------------------------------------------------------------"
sudo rm -rf /tmp/*

sudo apt-get clean -qy
sudo apt-get update -qy
sudo apt-get upgrade -qy
sudo apt-get install -qy apt-transport-https \
                         apt-utils \
                         ca-certificates \
                         fop \
                         gnupg-agent \
                         iputils-ping \
                         language-pack-de \
                         libevent-dev \
                         libglib2.0-dev \
                         libsnappy-dev \
                         libxml2-utils \
                         lsb-core \
                         net-tools \
                         netcat \
                         sudo \
                         xsltproc \
                         zip

echo "--------------------------------------------------------------------------------"
echo "Setting Locale & Timezone"
echo "--------------------------------------------------------------------------------"
sudo apt-get install -qy locales \
                         tzdata
sudo ln -fs /usr/share/zoneinfo/${TIMEZONE} /etc/localtime
sudo dpkg-reconfigure --frontend noninteractive tzdata
sudo locale-gen "${LOCALE}"
sudo update-locale "LANG=de_CH.UTF-8 UTF-8"
sudo locale-gen --purge "de_CH.UTF-8"
sudo dpkg-reconfigure --frontend noninteractive locales

echo "--------------------------------------------------------------------------------"
echo "Step: Setting up the environment: 1. Setting the environment variables"
echo "--------------------------------------------------------------------------------\n"

# from Locale & Timezone -----------------------------------------------------------------
echo '' >> ~/.bashrc
eval echo 'export LANG=${LOCALE}' >> ~/.bashrc
eval echo 'export LANGUAGE=${LOCALE}' >> ~/.bashrc
eval echo 'export LC_ALL=${LOCALE}' >> ~/.bashrc

# from asdf ------------------------------------------------------------------------------
export PATH_ADD_ON=~/.asdf/bin:~/.asdf/shims:${PATH_ADD_ON}

# from dos2unix --------------------------------------------------------------------------
# wwe export PATH_ADD_ON=/root/dos2unix/bin:${PATH_ADD_ON}

# from Eclipse ---------------------------------------------------------------------------
export PATH_ADD_ON=${HOME_ECLIPSE}/bin:${PATH_ADD_ON}

# from Gradle ----------------------------------------------------------------------------
export GRADLE_HOME=/opt/gradle/gradle-${VERSION_GRADLE}
eval echo 'export GRADLE_HOME=/opt/gradle/gradle-${VERSION_GRADLE}'
export PATH_ADD_ON=${GRADLE_HOME}/bin:${PATH_ADD_ON}

# from Java SE Development Kit ------------------------------------------------------------
export JAVA_HOME=~/.asdf/installs/java/${VERSION_JAVA}
eval echo 'export JAVA_HOME=~/.asdf/installs/java/${VERSION_JAVA}' >> ~/.bashrc
export PATH_ADD_ON=${JAVA_HOME}/bin:${PATH_ADD_ON}

# from Microsoft ODBC --------------------------------------------------------------------
export PATH="/opt/mssql-tools/bin:${PATH}"

echo "--------------------------------------------------------------------------------"
echo "Step: Setting up the environment: 2. Initializing the interactive shell session"
echo "--------------------------------------------------------------------------------"
echo '' >> ~/.bashrc
echo 'alias vi=vim' >> ~/.bashrc
# PATH variable -------------------------------------------------------------------------
echo '' >> ~/.bashrc
eval echo 'export PATH=${PATH_ORIG}:${PATH_ADD_ON}' >> ~/.bashrc
eval echo 'export PATH_ORIG=${PATH_ORIG}' >> ~/.bashrc
# from asdf -----------------------------------------------------------------------------
echo '' >> ~/.bashrc
eval echo '. ~/.asdf/asdf.sh' >> ~/.bashrc
eval echo '. ~/.asdf/completions/asdf.bash' >> ~/.bashrc
if [ "${HOST_ENVIRONMENT}" = "vm" ]; then
    # from Docker Desktop -------------------------------------------------------------------
    echo '' >> ~/.bashrc
    echo 'if [ `id -gn` != "docker" ]; then ( newgrp docker ) fi' >> ~/.bashrc
fi

echo '' >> ~/.bashrc
echo '# ------------------------------------------------------------------------------' >> ~/.bashrc
echo '# Environment db_seeder for Ubuntu 20.04 - End' >> ~/.bashrc
echo '# ------------------------------------------------------------------------------' >> ~/.bashrc

# Initializing the interactive shell session ---------------------------------------------
source ~/.bashrc

echo "--------------------------------------------------------------------------------"
echo "Step: Install wget - Version ${VERSION_WGET}"
echo "--------------------------------------------------------------------------------"
sudo apt-get install -qy build-essential \
                         libgnutls28-dev \
                         libpsl-dev \
                         pkg-config \
                         wget
wget --version
wget -q https://ftp.gnu.org/gnu/wget/wget-${VERSION_WGET}.tar.gz
sudo tar -xf wget-${VERSION_WGET}.tar.gz
sudo rm -rf wget
sudo mv wget-${VERSION_WGET} wget
cd wget
sudo ./configure --prefix=/usr
sudo make --quiet
sudo make --quiet install
cd ..
echo "================================================================================"
echo "Current wget version is: $(wget --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install cURL - Version ${VERSION_CURL}"
echo "--------------------------------------------------------------------------------"
wget -q https://curl.se/download/curl-${VERSION_CURL}.tar.gz
sudo tar -xf curl-${VERSION_CURL}.tar.gz
sudo rm -rf curl
sudo mv curl-${VERSION_CURL} curl
cd curl
sudo ./configure --prefix=/usr --without-ssl
sudo make --quiet
sudo make --quiet install
cd ..
echo "================================================================================"
echo "Current cURL version is: $(curl --version)"
echo "================================================================================"

sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 7EA0A9C3F273FCD8

if [ "${HOST_ENVIRONMENT}" = "vm" ]; then
    echo "--------------------------------------------------------------------------------"
    echo "Step: Install Docker Desktop"
    echo "--------------------------------------------------------------------------------"
    sudo apt-get install -qy lsb-release \
                             software-properties-common
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" --yes
    sudo apt-key fingerprint 0EBFCD88
    sudo apt-get install -qy docker-ce \
                             docker-ce-cli \
                             containerd.io
    sudo chmod 666 /var/run/docker.sock
#   if ! [ $(grep -q docker /etc/group) ]; then
#       sudo groupadd docker
#   fi
    if ! [ $(getent group docker | grep -q "\b$USER\b") ]; then
        sudo usermod -aG docker $USER
    fi
    echo "================================================================================"
    echo "Current Docker Desktop version is: $(docker version)"
    echo "================================================================================"
fi

echo "--------------------------------------------------------------------------------"
echo "Step: Install dos2unix - Version ${VERSION_DOS2UNIX}"
echo "--------------------------------------------------------------------------------"
sudo apt-get install -qy gettext
wget -q https://waterlan.home.xs4all.nl/dos2unix/dos2unix-${VERSION_DOS2UNIX}.tar.gz
sudo tar -xf dos2unix-${VERSION_DOS2UNIX}.tar.gz
sudo rm -rf dos2unix
sudo mv dos2unix-${VERSION_DOS2UNIX} dos2unix
cd dos2unix
sudo make --quiet
sudo make --quiet install
cd ..
echo "================================================================================"
echo "Current dos2unix version is: $(dos2unix --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install Git"
echo "--------------------------------------------------------------------------------"
sudo apt-get install -qy software-properties-common
sudo add-apt-repository ppa:git-core/ppa --yes
sudo apt-get install -qy git
sudo git config --global credential.helper 'cache --timeout 3600'
echo "================================================================================"
echo "Current Git version is: $(git --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install ODBC"
echo "--------------------------------------------------------------------------------"
curl https://packages.microsoft.com/keys/microsoft.asc | sudo apt-key add -
curl https://packages.microsoft.com/config/ubuntu/20.04/prod.list > my_prod.list
sudo mv my_prod.list /etc/apt/sources.list.d/mssql-release.list
sudo apt-get update
sudo ACCEPT_EULA=Y apt-get install -y msodbcsql17
sudo ACCEPT_EULA=Y apt-get install -y mssql-tools
sudo apt-get install -y unixodbc-dev
echo "================================================================================"
echo "Current version of ODBC: $(odbcinst -j)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install asdf - part 1"
echo "--------------------------------------------------------------------------------"
sudo apt-get install -qy coreutils \
                         gpg \
                         libreadline6-dev \
                         libtool \
                         libwxgtk3.0-gtk3-dev \
                         libxslt1-dev \
                         libyaml-dev \
                         unixodbc \
                         unzip
sudo rm -rf ~/.asdf
git clone https://github.com/asdf-vm/asdf.git ~/.asdf
cd ~/.asdf
git checkout "$(git describe --abbrev=0 --tags)"
cd ~/
echo "================================================================================"

echo " "
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"

exit 0
