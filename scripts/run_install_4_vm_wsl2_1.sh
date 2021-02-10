#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_install_4_vm_wsl2_1.sh: Install an environment for Ubuntu 20.04 - Step 1.
#
# ------------------------------------------------------------------------------

if [ -d "db_seeder" ]; then
    cp -r db_seeder ~/
fi
if [ -d "dderl" ]; then
    cp -r dderl ~/
fi
if [ -d "kxn_dev" ]; then
    cp -r kxn_dev ~/
fi

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

eval echo '' >> ~/.bashrc
eval echo 'export DEBIAN_FRONTEND=${DEBIAN_FRONTEND}' >> ~/.bashrc
eval echo 'export HOME_ECLIPSE=${HOME_ECLIPSE}' >> ~/.bashrc
eval echo 'export LOCALE=${LOCALE}' >> ~/.bashrc

export VERSION_AUTOCONF=2.71
export VERSION_AUTOMAKE=1.16.3
export VERSION_CMAKE=3.19.4
export VERSION_CURL=7.75.0
export VERSION_DOCKER_COMPOSE=1.28.2
export VERSION_DOS2UNIX=7.4.2
export VERSION_ECLIPSE_1=2020-12
export VERSION_ECLIPSE_2=R
export VERSION_ELIXIR=1.11.3-otp-23
export VERSION_ERLANG=23.2.3
export VERSION_GCC=10
export VERSION_GCC_ORIG=9
export VERSION_GO=1.15.7
export VERSION_GRADLE=6.8.1
export VERSION_HTOP=3.0.5
export VERSION_JAVA=openjdk-15.0.2
export VERSION_KOTLIN=1.4.21
export VERSION_MAKE=4.3
export VERSION_NODE=14.x
export VERSION_NODEJS=14.15.4
export VERSION_OPENSSL=1_1_1i
export VERSION_ORACLE_INSTANT_CLIENT_1=21
export VERSION_ORACLE_INSTANT_CLIENT_2=1
export VERSION_PYTHON=3.9.1
export VERSION_REBAR=3.14.3
export VERSION_RUST=1.49.0
export VERSION_TMUX=3.1c
export VERSION_VIM=8.2.2453
export VERSION_WGET=1.21.1
export VERSION_YARN=1.22.5

eval echo '' >> ~/.bashrc
eval echo 'export VERSION_AUTOCONF=${VERSION_AUTOCONF}' >> ~/.bashrc
eval echo 'export VERSION_AUTOMAKE=${VERSION_AUTOMAKE}' >> ~/.bashrc
eval echo 'export VERSION_CMAKE=${VERSION_CMAKE}' >> ~/.bashrc
eval echo 'export VERSION_CURL=${VERSION_CURL}' >> ~/.bashrc
eval echo 'export VERSION_DOCKER_COMPOSE=${VERSION_DOCKER_COMPOSE}' >> ~/.bashrc
eval echo 'export VERSION_DOS2UNIX=${VERSION_DOS2UNIX}' >> ~/.bashrc
eval echo 'export VERSION_ECLIPSE_1=${VERSION_ECLIPSE_1}' >> ~/.bashrc
eval echo 'export VERSION_ECLIPSE_2=${VERSION_ECLIPSE_2}' >> ~/.bashrc
eval echo 'export VERSION_ELIXIR=${VERSION_ELIXIR}' >> ~/.bashrc
eval echo 'export VERSION_ERLANG=${VERSION_ERLANG}' >> ~/.bashrc
eval echo 'export VERSION_GCC=${VERSION_GCC}' >> ~/.bashrc
eval echo 'export VERSION_GCC_ORIG=${VERSION_GCC_ORIG}' >> ~/.bashrc
eval echo 'export VERSION_GO=${VERSION_GO}' >> ~/.bashrc
eval echo 'export VERSION_GRADLE=${VERSION_GRADLE}' >> ~/.bashrc
eval echo 'export VERSION_HTOP=${VERSION_HTOP}' >> ~/.bashrc
eval echo 'export VERSION_JAVA=${VERSION_JAVA}' >> ~/.bashrc
eval echo 'export VERSION_KOTLIN=${VERSION_KOTLIN}' >> ~/.bashrc
eval echo 'export VERSION_MAKE=${VERSION_MAKE}' >> ~/.bashrc
eval echo 'export VERSION_NODEJS=${VERSION_NODEJS}' >> ~/.bashrc
eval echo 'export VERSION_OPENSSL=${VERSION_OPENSSL}' >> ~/.bashrc
eval echo 'export VERSION_ORACLE_INSTANT_CLIENT_1=${VERSION_ORACLE_INSTANT_CLIENT_1}' >> ~/.bashrc
eval echo 'export VERSION_ORACLE_INSTANT_CLIENT_2=${VERSION_ORACLE_INSTANT_CLIENT_2}' >> ~/.bashrc
eval echo 'export VERSION_PYTHON=${VERSION_PYTHON}' >> ~/.bashrc
eval echo 'export VERSION_REBAR=${VERSION_REBAR}' >> ~/.bashrc
eval echo 'export VERSION_RUST=${VERSION_RUST}' >> ~/.bashrc
eval echo 'export VERSION_TMUX=${VERSION_TMUX}' >> ~/.bashrc
eval echo 'export VERSION_VIM=${VERSION_VIM}' >> ~/.bashrc
eval echo 'export VERSION_WGET=${VERSION_WGET}' >> ~/.bashrc
eval echo 'export VERSION_YARN=${VERSION_YARN}' >> ~/.bashrc

export VERSION_KXN_DEV=2.0.0

eval echo '' >> ~/.bashrc
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

eval echo '' >> ~/.bashrc
eval echo 'export HOST_ENVIRONMENT=${HOST_ENVIRONMENT}' >> ~/.bashrc

echo ""
echo "Script $0 is now running"

export LOG_FILE=run_install_4_vm_wsl2_1.log

echo ""
echo "You can find the run log in the file ${LOG_FILE}"
echo ""

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

if [ "$(dpkg -l | grep python${VERSION_PYTHON})" != "" ]; then
    sudo apt-get remove -qy python${VERSION_PYTHON}
    sudo apt autoremove -qy
    sudo update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.8 80
    sudo apt install --reinstall -qy python3
fi

sudo apt-get -qy remove python3-apt
sudo apt-get -qy autoremove
sudo apt-get -qy autoclean
sudo apt-get install -qy python3-apt

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
                         lcov \
                         libevent-dev \
                         libglib2.0-dev \
                         libxml2-utils \
                         lsb-core \
                         net-tools \
                         netcat \
                         nginx \
                         procps \
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
sudo dpkg-reconfigure locales

echo "--------------------------------------------------------------------------------"
echo "Step: Setting up the environment: 1. Setting the environment variables"
echo "--------------------------------------------------------------------------------\n"

# from Locale & Timezone -----------------------------------------------------------------
eval echo '' >> ~/.bashrc
eval echo 'export LANG=${LOCALE}' >> ~/.bashrc
eval echo 'export LANGUAGE=${LOCALE}' >> ~/.bashrc
eval echo 'export LC_ALL=${LOCALE}' >> ~/.bashrc

# from asdf ------------------------------------------------------------------------------
export PATH_ADD_ON="/usr/.asdf/bin:/usr/.asdf/shims:${PATH_ADD_ON}"

# from dos2unix --------------------------------------------------------------------------
export PATH_ADD_ON="/root/dos2unix/bin:${PATH_ADD_ON}"

# from Eclipse ---------------------------------------------------------------------------
export PATH_ADD_ON="${HOME_ECLIPSE}/bin:${PATH_ADD_ON}"

# from Go --------------------------------------------------------------------------------
export GOROOT="/usr/.asdf/installs/golang/${VERSION_GO}/go"
export PATH_ADD_ON="${GOROOT}/bin:${PATH_ADD_ON}"

# from Gradle ----------------------------------------------------------------------------
export GRADLE_HOME="/opt/gradle/gradle-${VERSION_GRADLE}"
export PATH_ADD_ON="${GRADLE_HOME}/bin:${PATH_ADD_ON}"

# from Java SE Development Kit ------------------------------------------------------------
export JAVA_HOME="/usr/.asdf/installs/java/${VERSION_JAVA}"
export PATH_ADD_ON="${JAVA_HOME}/bin:${PATH_ADD_ON}"

# from Kotlin ----------------------------------------------------------------------------
export KOTLIN_HOME="/usr/.asdf/installs/kotlin/${VERSION_KOTLIN}"
export PATH_ADD_ON="${KOTLIN_HOME}/bin:${PATH_ADD_ON}"

# from Node ------------------------------------------------------------------------------
export PATH_ADD_ON="/usr/local/lib/nodejs/bin":${PATH_ADD_ON}

# from Oracle Instant Client -------------------------------------------------------------
export ORACLE_HOME=/usr/lib/oracle/${VERSION_ORACLE_INSTANT_CLIENT_1}/client64
export LD_LIBRARY_PATH="${ORACLE_HOME}/lib:${LD_LIBRARY_PATH}"
    PATH_ADD_ON="${ORACLE_HOME}:${PATH_ADD_ON}"

# from Rust ------------------------------------------------------------------------------
export PATH_ADD_ON="/root/.cargo/bin:${PATH_ADD_ON}"

echo "--------------------------------------------------------------------------------"
echo "Step: Setting up the environment: 2. Initializing the interactive shell session"
echo "--------------------------------------------------------------------------------"
eval echo '' >> ~/.bashrc
eval echo 'alias python=python3' >> ~/.bashrc
eval echo 'alias vi=vim' >> ~/.bashrc
# from Stefans privater bash Erweiterung ------------------------------------------------
eval echo '' >> ~/.bashrc
eval echo '. ~/kxn_dev/.bashrc.stefan' >> ~/.bashrc
# PATH variable -------------------------------------------------------------------------
eval echo '' >> ~/.bashrc
eval echo 'export PATH=${PATH_ADD_ON}:${PATH_ORIG}' >> ~/.bashrc
eval echo 'export PATH_ORIG=${PATH_ORIG}' >> ~/.bashrc
# from asdf -----------------------------------------------------------------------------
eval echo '' >> ~/.bashrc
eval echo '. /usr/.asdf/asdf.sh' >> ~/.bashrc
eval echo '. /usr/.asdf/completions/asdf.bash' >> ~/.bashrc
eval echo '' >> ~/.bashrc
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
echo "Step: Install htop - Version ${VERSION_HTOP}"
echo "--------------------------------------------------------------------------------"
sudo apt-get install -qy autoconf \
                         libncurses5-dev \
                         libncursesw5-dev
wget -q https://github.com/htop-dev/htop/archive/${VERSION_HTOP}.tar.gz
sudo tar -zxf ${VERSION_HTOP}.tar.gz
sudo rm -rf htop
sudo mv htop-${VERSION_HTOP} htop
cd htop
sudo ./autogen.sh
sudo ./configure --prefix=/usr
sudo make --quiet
sudo make --quiet install
cd ..
echo "================================================================================"
echo "Current htop version is: $(htop --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install GNU Make - Version ${VERSION_MAKE}"
echo "--------------------------------------------------------------------------------"
sudo make --version
wget -q https://ftp.gnu.org/gnu/make/make-${VERSION_MAKE}.tar.gz
sudo tar -xf make-${VERSION_MAKE}.tar.gz
sudo rm -rf make
sudo mv make-${VERSION_MAKE} make
cd make
sudo ./configure --prefix=/usr
sudo make --quiet
sudo make --quiet install
cd ..
echo "================================================================================"
echo "Current GNU Make version is $(make --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install GNU Autoconf - Version ${VERSION_AUTOCONF}"
echo "--------------------------------------------------------------------------------"
sudo apt-get install -qy m4
wget -q https://mirrors.ocf.berkeley.edu/gnu/autoconf/autoconf-${VERSION_AUTOCONF}.tar.gz
sudo tar -xf autoconf-${VERSION_AUTOCONF}.tar.gz
sudo rm -rf autoconf
sudo mv autoconf-${VERSION_AUTOCONF} autoconf
cd autoconf
sudo ./configure --prefix=/usr
sudo make --quiet
sudo make --quiet install
cd ..
echo "================================================================================"
echo "Current GNU Autoconf version is: $(autoconf -V)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install GNU Automake - Version ${VERSION_AUTOMAKE}"
echo "--------------------------------------------------------------------------------"
wget -q https://ftp.gnu.org/gnu/automake/automake-${VERSION_AUTOMAKE}.tar.gz
sudo tar -xf automake-${VERSION_AUTOMAKE}.tar.gz
sudo rm -rf automake
sudo mv automake-${VERSION_AUTOMAKE} automake
cd automake
sudo ./configure --prefix=/usr
sudo make --quiet
sudo make --quiet install
cd ..
echo "================================================================================"
echo "Current GNU Automake version is: $(automake --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install OpenSSL - Version ${VERSION_OPENSSL}"
echo "--------------------------------------------------------------------------------"
wget -q https://github.com/openssl/openssl/archive/OpenSSL_${VERSION_OPENSSL}.tar.gz
sudo tar -xf OpenSSL_${VERSION_OPENSSL}.tar.gz
sudo rm -rf openssl
sudo mv openssl-OpenSSL_${VERSION_OPENSSL} openssl
cd openssl
sudo ./config
sudo make --quiet
sudo make --quiet install
cd ..
sudo ldconfig
echo "================================================================================"
echo "Current OpenSSL version is: $(openssl version -a)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install cURL - Version ${VERSION_CURL}"
echo "--------------------------------------------------------------------------------"
wget -q https://curl.se/download/curl-${VERSION_CURL}.tar.gz
sudo tar -xf curl-${VERSION_CURL}.tar.gz
sudo rm -rf curl
sudo mv curl-${VERSION_CURL} curl
cd curl
sudo ./configure --prefix=/usr
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
                             python-apt \
                             software-properties-common
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" --yes
    sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo apt-key fingerprint 0EBFCD88
    sudo apt-get install -qy docker-ce \
                             docker-ce-cli \
                             containerd.io
    echo 'if [ `id -gn` != "docker" ]; then ( newgrp docker ) fi' >> ~/.bashrc
    echo "================================================================================"
    #echo "Current Docker Desktop version is: $(docker version)"
    echo "================================================================================"
fi

echo "--------------------------------------------------------------------------------"
echo "Step: Install Docker Compose - Version ${VERSION_DOCKER_COMPOSE}"
echo "--------------------------------------------------------------------------------"
sudo curl -L "https://github.com/docker/compose/releases/download/${VERSION_DOCKER_COMPOSE}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
echo "================================================================================"
echo "Current Docker Compose version is: $(docker-compose version)"
echo "================================================================================"

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
echo "Step: Install Oracle Instant Client - Version ${VERSION_ORACLE_INSTANT_CLIENT_1}.${VERSION_ORACLE_INSTANT_CLIENT_2}.0.0.0"
echo "--------------------------------------------------------------------------------"
sudo apt-get install -qy alien \
                         libaio1
(
    cd /
    sudo rm -rf oracle-instantclient*
    sudo wget -q https://download.oracle.com/otn_software/linux/instantclient/${VERSION_ORACLE_INSTANT_CLIENT_1}${VERSION_ORACLE_INSTANT_CLIENT_2}000/oracle-instantclient-basic-${VERSION_ORACLE_INSTANT_CLIENT_1}.${VERSION_ORACLE_INSTANT_CLIENT_2}.0.0.0-1.x86_64.rpm
    sudo wget -q https://download.oracle.com/otn_software/linux/instantclient/${VERSION_ORACLE_INSTANT_CLIENT_1}${VERSION_ORACLE_INSTANT_CLIENT_2}000/oracle-instantclient-sqlplus-${VERSION_ORACLE_INSTANT_CLIENT_1}.${VERSION_ORACLE_INSTANT_CLIENT_2}.0.0.0-1.x86_64.rpm
    sudo alien -i *.rpm
    sudo rm -rf *.x86_64.rpm
)
echo "================================================================================"
echo "Current Oracle Instant Client version is: $(sqlplus -V)"
echo "================================================================================"

# Stefans privater bash Erweiterung ------------------------------------------------------
sudo cp -r kxn_dev/.bashrc.stefan /.bashrc.stefan

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
sudo rm -rf /usr/.asdf
sudo git clone https://github.com/asdf-vm/asdf.git /usr/.asdf
cd /usr/.asdf
sudo git checkout "$(git describe --abbrev=0 --tags)"
cd ~/
echo "================================================================================"

echo ""
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"

exit 0
