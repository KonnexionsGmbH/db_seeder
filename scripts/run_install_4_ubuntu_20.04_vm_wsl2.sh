#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_install_4_ubuntu_20.04_vm_wsl2.sh: Install an environment for Ubuntu 20.04.
#
# ------------------------------------------------------------------------------


export HOME_ECLIPSE=/opt/eclipse
export HOST_ENVIRONMENT_DEFAULT=vm
export LOCALE=en_US.UTF-8
export PATH_ADD_ON=
export TIMEZONE=Europe/Zurich

export VERSION_DOCKER_COMPOSE=1.27.4
export VERSION_ECLIPSE_1=2020-12
export VERSION_ECLIPSE_2=R
export VERSION_ERLANG_SOLUTIONS=2.0
export VERSION_GO=1.15.7
export VERSION_GRADLE=6.8
export VERSION_JAVA=15.0.2
export VERSION_NODE=14.x
export VERSION_ORACLE_INSTANT_CLIENT_1=21
export VERSION_ORACLE_INSTANT_CLIENT_2=1
export VERSION_PYTHON=3.9

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

if [ -z "$2" ]; then
    read -rp "Kotlin version to be uninstalled [currently installed (9.9.99): $(kotlin -version 2>/dev/null || echo "none")] " VERSION_KOTLIN
    export VERSION_KOTLIN=${VERSION_KOTLIN}
else
    export VERSION_KOTLIN=$2
fi

echo ""
echo "Script $0 is now running"

export LOG_FILE=run_install_4_ubuntu_20.04_vm_wsl2.log

echo ""
echo "You can find the run log in the file ${LOG_FILE}"
echo ""

exec &> >(tee -i ${LOG_FILE}) 2>&1
sleep .1

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "Install an environment for Ubuntu 20.04."
echo "--------------------------------------------------------------------------------"
echo "HOST_ENVIRONMENT                  : ${HOST_ENVIRONMENT}"
echo "UNINSTALL_VERSION_KOTLIN          : ${VERSION_KOTLIN}"
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

sudo apt clean
sudo apt update
sudo apt upgrade -qy
sudo apt install -qy alien
sudo apt install -qy apt-transport-https
sudo apt install -qy apt-utils
sudo apt install -qy autoconf
sudo apt install -qy autotools-dev
sudo apt install -qy build-essential
sudo apt install -qy ca-certificates
sudo apt install -qy cmake
sudo apt install -qy curl
sudo apt install -qy dos2unix
sudo apt install -qy gnupg-agent
sudo apt install -qy htop
sudo apt install -qy iputils-ping
sudo apt install -qy language-pack-de
sudo apt install -qy lcov
sudo apt install -qy libaio1
sudo apt install -qy lsb-core
sudo apt install -qy net-tools
sudo apt install -qy nginx
sudo apt install -qy openssl
sudo apt install -qy procps
sudo apt install -qy software-properties-common
sudo apt install -qy tmux
sudo apt install -qy tzdata
sudo apt install -qy unzip
sudo apt install -qy vim
sudo apt install -qy wget
sudo apt install -qy zip

sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 7EA0A9C3F273FCD8

sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" --yes
sudo add-apt-repository ppa:ubuntu-toolchain-r/test --yes
sudo add-apt-repository ppa:git-core/ppa --yes
sudo add-apt-repository ppa:deadsnakes/ppa --yes

echo "--------------------------------------------------------------------------------"
echo "Setting Locale & Timezone"
echo "--------------------------------------------------------------------------------"
sudo ln -fs /usr/share/zoneinfo/${TIMEZONE} /etc/localtime
sudo dpkg-reconfigure --frontend noninteractive tzdata
sudo locale-gen "${LOCALE}"
sudo dpkg-reconfigure --frontend noninteractive locales

eval echo 'export LANG=${LOCALE}' >> ~/.bashrc
eval echo 'export LANGUAGE=${LOCALE}' >> ~/.bashrc
eval echo 'export LC_ALL=${LOCALE}' >> ~/.bashrc

if [ "${HOST_ENVIRONMENT}" = "vm" ]; then
    echo "--------------------------------------------------------------------------------"
    echo "Install Docker Desktop"
    echo "--------------------------------------------------------------------------------"
    sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo apt-key fingerprint 0EBFCD88
    sudo apt update
    sudo apt install -qy docker-ce docker-ce-cli containerd.io
    sudo getent group docker || sudo groupadd docker
    sudo usermod -aG docker ${USER}

    echo 'if [ `id -gn` != "docker" ]; then ( newgrp docker ) fi' >> ~/.bashrc

    echo "--------------------------------------------------------------------------------"
    echo "Install Docker Compose - Version ${VERSION_DOCKER_COMPOSE}"
    echo "--------------------------------------------------------------------------------"
    sudo curl -L "https://github.com/docker/compose/releases/download/${VERSION_DOCKER_COMPOSE}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
fi

echo "--------------------------------------------------------------------------------"
echo "Install Erlang - Version ${VERSION_ERLANG_SOLUTIONS}"
echo "--------------------------------------------------------------------------------"
wget -q https://packages.erlang-solutions.com/erlang-solutions_${VERSION_ERLANG_SOLUTIONS}_all.deb
sudo dpkg -i erlang-solutions_${VERSION_ERLANG_SOLUTIONS}_all.deb
sudo apt update
sudo apt install -qy esl-erlang
rm erlang-solutions_${VERSION_ERLANG_SOLUTIONS}_all.deb

echo "--------------------------------------------------------------------------------"
echo "Install Elixir"
echo "--------------------------------------------------------------------------------"
sudo apt install -qy elixir
sudo mix local.hex --force
sudo mix local.rebar --force

echo "--------------------------------------------------------------------------------"
echo "Install GCC"
echo "--------------------------------------------------------------------------------"
sudo apt install -qy gcc-10
sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-10 100 --slave /usr/bin/g++ g++ /usr/bin/g++-10 --slave /usr/bin/gcov gcov /usr/bin/gcov-10
sudo  update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-9 90 --slave /usr/bin/g++ g++ /usr/bin/g++-9 --slave /usr/bin/gcov gcov /usr/bin/gcov-9

echo "--------------------------------------------------------------------------------"
echo "Install Git"
echo "--------------------------------------------------------------------------------"
sudo apt update
sudo apt install -qy git
sudo git config --global credential.helper 'cache --timeout 3600'

echo "--------------------------------------------------------------------------------"
echo "Install Go - Version ${VERSION_GO}"
echo "--------------------------------------------------------------------------------"
wget -q https://dl.google.com/go/go${VERSION_GO}.linux-amd64.tar.gz
sudo tar -xf go${VERSION_GO}.linux-amd64.tar.gz
sudo rm -rf /usr/local/go
sudo cp -r go /usr/local
sudo rm -rf go

sudo rm -rf go${VERSION_GO}.linux-amd64.tar.gz

eval echo 'export GOROOT=/usr/local/go' >> ~/.bashrc
export PATH_ADD_ON="/usr/local/go/bin":${PATH_ADD_ON}

echo "--------------------------------------------------------------------------------"
echo "Install Java SE Development Kit - Version ${VERSION_JAVA}"
echo "--------------------------------------------------------------------------------"
(
    cd /
    sudo wget -q https://download.java.net/java/GA/jdk${VERSION_JAVA}/0d1cfde4252546c6931946de8db48ee2/7/GPL/openjdk-${VERSION_JAVA}_linux-x64_bin.tar.gz
    sudo tar -xf openjdk-${VERSION_JAVA}_linux-x64_bin.tar.gz
    sudo rm -rf /opt/jdk-${VERSION_JAVA}
    sudo cp -r jdk-${VERSION_JAVA} /opt/
    sudo rm -rf jdk-${VERSION_JAVA}
    sudo rm -rf openjdk-${VERSION_JAVA}_linux-x64_bin.tar.gz
)

eval echo 'export JAVA_HOME=/opt/jdk-${VERSION_JAVA}' >> ~/.bashrc
export PATH_ADD_ON="/opt/jdk-${VERSION_JAVA}/bin":${PATH_ADD_ON}

echo "--------------------------------------------------------------------------------"
echo "Install Eclipse - Version /${VERSION_ECLIPSE_1}./${VERSION_ECLIPSE_2}"
echo "--------------------------------------------------------------------------------"
sudo rm -rf ${HOME_ECLIPSE}
sudo wget -q https://www.mirrorservice.org/sites/download.eclipse.org/eclipseMirror/technology/epp/downloads/release/${VERSION_ECLIPSE_1}/${VERSION_ECLIPSE_2}/eclipse-java-${VERSION_ECLIPSE_1}-${VERSION_ECLIPSE_2}-linux-gtk-x86_64.tar.gz -P /tmp
sudo tar -xf /tmp/eclipse-java-*-linux-gtk-x86_64.tar.gz
sudo cp -r eclipse ${HOME_ECLIPSE}
sudo rm -rf eclipse

export PATH_ADD_ON="${HOME_ECLIPSE}":${PATH_ADD_ON}

echo "--------------------------------------------------------------------------------"
echo "Install Gradle - Version ${VERSION_GRADLE}"
echo "--------------------------------------------------------------------------------"
sudo rm -rf /opt/gradle
wget -q https://services.gradle.org/distributions/gradle-${VERSION_GRADLE}-bin.zip -P /tmp
sudo unzip -o -d /opt/gradle /tmp/gradle-*.zip

eval echo 'export GRADLE_HOME=/opt/gradle/gradle-${VERSION_GRADLE}' >> ~/.bashrc
export PATH_ADD_ON="/opt/gradle/gradle-${VERSION_GRADLE}/bin":${PATH_ADD_ON}

echo "--------------------------------------------------------------------------------"
echo "Install Kotlin"
echo "--------------------------------------------------------------------------------"
curl -sSL https://get.sdkman.io | bash
chmod a+x "${HOME}/.sdkman/bin/sdkman-init.sh"
source "${HOME}/.sdkman/bin/sdkman-init.sh"
if [ ! -z "${VERSION_KOTLIN}" ]; then
    sdk uninstall kotlin ${VERSION_KOTLIN}
fi
sdk install kotlin

export PATH_ADD_ON="/root/.sdkman/candidates/kotlin/current/bin":${PATH_ADD_ON}

echo "--------------------------------------------------------------------------------"
echo "Install Node - Version ${VERSION_NODE}"
echo "--------------------------------------------------------------------------------"
wget -q -O- https://deb.nodesource.com/setup_${VERSION_NODE} | sudo -E bash -
sudo apt update
sudo apt install -qy nodejs
sudo npm install -g npm@latest

export PATH_ADD_ON="/usr/local/lib/nodejs/bin":${PATH_ADD_ON}

echo "--------------------------------------------------------------------------------"
echo "Install Oracle Instant Client - Version ${VERSION_ORACLE_INSTANT_CLIENT_1}.${VERSION_ORACLE_INSTANT_CLIENT_2}.0.0.0"
echo "--------------------------------------------------------------------------------"
(
    cd /
    sudo rm -rf oracle-instantclient*
    sudo wget -q https://download.oracle.com/otn_software/linux/instantclient/${VERSION_ORACLE_INSTANT_CLIENT_1}${VERSION_ORACLE_INSTANT_CLIENT_2}000/oracle-instantclient-basic-${VERSION_ORACLE_INSTANT_CLIENT_1}.${VERSION_ORACLE_INSTANT_CLIENT_2}.0.0.0-1.x86_64.rpm
    sudo wget -q https://download.oracle.com/otn_software/linux/instantclient/${VERSION_ORACLE_INSTANT_CLIENT_1}${VERSION_ORACLE_INSTANT_CLIENT_2}000/oracle-instantclient-sqlplus-${VERSION_ORACLE_INSTANT_CLIENT_1}.${VERSION_ORACLE_INSTANT_CLIENT_2}.0.0.0-1.x86_64.rpm
    sudo alien -i *.rpm
    sudo rm -rf *.x86_64.rpm
)

eval echo 'export ORACLE_HOME=/usr/lib/oracle/${VERSION_ORACLE_INSTANT_CLIENT_1}/client64' >> ~/.bashrc
eval echo 'export LD_LIBRARY_PATH=/usr/lib/oracle/${VERSION_ORACLE_INSTANT_CLIENT_1}/client64/lib:${LD_LIBRARY_PATH}' >> ~/.bashrc
export PATH_ADD_ON="/usr/lib/oracle/${VERSION_ORACLE_INSTANT_CLIENT_1}/client64":${PATH_ADD_ON}

echo "--------------------------------------------------------------------------------"
echo "Install Python3"
echo "--------------------------------------------------------------------------------"
(
    cd /
    sudo apt-get install -qy python3-distutils
    sudo apt-get install -qy python3-apt
    sudo apt install -qy python${VERSION_PYTHON}
    sudo update-alternatives --install /usr/bin/python3 python3 /usr/bin/python${VERSION_PYTHON} 90
    sudo update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.8 80
    sudo wget -q https://bootstrap.pypa.io/get-pip.py
    sudo python3 get-pip.py --use-feature=2020-resolver
)

python3 -m pip install -r kxn_dev/requirements.txt

echo "--------------------------------------------------------------------------------"
echo "Install rebar3"
echo "--------------------------------------------------------------------------------"
(
    cd /
    sudo rm -rf /usr/bin/rebar3
    sudo wget -q https://s3.amazonaws.com/rebar3/rebar3
    sudo chmod +x rebar3
    sudo cp -r rebar3 /usr/bin
    sudo rm -rf rebar3
)

echo "--------------------------------------------------------------------------------"
echo "Install Rust"
echo "--------------------------------------------------------------------------------"
curl -sSL https://sh.rustup.rs -sSf | sh -s -- -y

source ${HOME}/.cargo/env

echo "--------------------------------------------------------------------------------"
echo "Install Yarn"
echo "--------------------------------------------------------------------------------"
wget -q -O- https://dl.yarnpkg.com/debian/pubkey.gpg | sudo apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | sudo tee /etc/apt/sources.list.d/yarn.list
sudo apt install -qy yarn

# Stefans private bash Erweiterung ---------------------------------------------
sudo cp -r kxn_dev/.bashrc.stefan /.bashrc.stefan
echo 'source /.bashrc.stefan' >> ~/.bashrc

echo "--------------------------------------------------------------------------------"
echo "Setting up environment"
echo "--------------------------------------------------------------------------------"
eval echo 'export PATH=${PATH_ADD_ON}:\"${PATH}\"' >> ~/.bashrc
source ~/.bashrc

echo "--------------------------------------------------------------------------------"
echo "Cleanup"
echo "--------------------------------------------------------------------------------"
rm -f *.gz
rm -f *.rpm 
rm -f *.zip

sudo apt autoremove -qy

sudo mkdir -p /usr/opt/dderl
sudo cp -r dderl/* /usr/opt/dderl/

export VERSION_KXN_DEV=1.4.7

echo ""
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"

exit
