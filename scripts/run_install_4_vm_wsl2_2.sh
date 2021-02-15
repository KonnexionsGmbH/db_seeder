#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_install_4_vm_wsl2_2.sh: Install an environment for Ubuntu 20.04 - Step 2.
#
# ------------------------------------------------------------------------------

cd ~/

echo ""
echo "Script $0 is now running"

export LOG_FILE=run_install_4_vm_wsl2_2.log

echo ""
echo "You can find the run log in the file ${LOG_FILE}"
echo ""

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
rm -rf ~/.asdf
touch ~/.tool-versions
# echo "================================================================================"
# asdf plugin list all
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install CMake - Version ${VERSION_CMAKE}"
echo "--------------------------------------------------------------------------------"
asdf plugin add cmake
# asdf list all cmake
asdf install cmake ${VERSION_CMAKE}
asdf global cmake ${VERSION_CMAKE}
echo "================================================================================"
ls -ll ~/.asdf/installs/cmake
echo "================================================================================"
echo "Current CMake version is: $(cmake --version)"
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
echo "Step: Install Erlang - Version ${VERSION_ERLANG}"
echo "--------------------------------------------------------------------------------"
asdf plugin add erlang
# asdf list all erlang
asdf install erlang ${VERSION_ERLANG}
asdf global erlang ${VERSION_ERLANG}
echo "================================================================================"
ls -ll ~/.asdf/installs/erlang
echo "================================================================================"
echo "Current Erlang version is: $(erl -eval '{ok, Version} = file:read_file(filename:join([code:root_dir(), "releases", erlang:system_info(otp_release), "OTP_VERSION"])), io:fwrite(Version), halt().' -noshell)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install rebar3 - Version ${VERSION_REBAR}"
echo "--------------------------------------------------------------------------------"
asdf plugin add rebar
# asdf list all rebar
asdf install rebar ${VERSION_REBAR}
asdf global rebar ${VERSION_REBAR}
echo "================================================================================"
ls -ll ~/.asdf/installs/rebar
echo "================================================================================"
echo "Current rebar3 version is: $(rebar3 version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install Elixir - Version ${VERSION_ELIXIR}"
echo "--------------------------------------------------------------------------------"
asdf plugin add elixir
# asdf list all elixir
asdf install elixir ${VERSION_ELIXIR}
asdf global elixir ${VERSION_ELIXIR}
mix local.hex --force
mix local.rebar --force
echo "================================================================================"
ls -ll ~/.asdf/installs/elixir
echo "================================================================================"
echo "Current Elixir version is: $(elixir -v)"
echo "Current Mix version is: $(mix --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install GCC - Version ${VERSION_GCC} / ${VERSION_GCC_ORIG}"
echo "--------------------------------------------------------------------------------"
sudo gcc --version
sudo apt install -qy gcc-${VERSION_GCC} \
                     g++-${VERSION_GCC}
sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-${VERSION_GCC} 100 --slave /usr/bin/g++ g++ /usr/bin/g++-${VERSION_GCC} --slave /usr/bin/gcov gcov /usr/bin/gcov-${VERSION_GCC}
sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-${VERSION_GCC_ORIG} 90 --slave /usr/bin/g++ g++ /usr/bin/g++-${VERSION_GCC_ORIG} --slave /usr/bin/gcov gcov /usr/bin/gcov-${VERSION_GCC_ORIG}
echo "================================================================================"
echo "Current GCC version is: $(gcc --version)"
echo "Current G++ version is: $(g++ --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install Go - Version ${VERSION_GO}"
echo "--------------------------------------------------------------------------------"
asdf plugin add golang
# asdf list all golang
asdf install golang ${VERSION_GO}
asdf global golang ${VERSION_GO}
echo "================================================================================"
ls -ll ~/.asdf/installs/golang
echo "================================================================================"
echo "Current Go version is: $(go version)"
echo "Current Go environment is: $(go env)"
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
echo "Step: Install Kotlin - Version ${VERSION_KOTLIN}"
echo "--------------------------------------------------------------------------------"
asdf plugin add kotlin
# asdf list all kotlin
asdf install kotlin ${VERSION_KOTLIN}
asdf global kotlin ${VERSION_KOTLIN}
echo "================================================================================"
ls -ll ~/.asdf/installs/kotlin
echo "================================================================================"
echo "Current Kotlin version is: $(kotlin -version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install Node - Version ${VERSION_NODEJS}"
echo "--------------------------------------------------------------------------------"
asdf plugin add nodejs
# asdf list all nodejs
bash -c '${ASDF_DATA_DIR:=$HOME/.asdf}/plugins/nodejs/bin/import-release-team-keyring'
asdf install nodejs ${VERSION_NODEJS}
asdf global nodejs ${VERSION_NODEJS}
echo "================================================================================"
ls -ll ~/.asdf/installs/nodejs
echo "================================================================================"
echo "Current Node version is: $(node --version)"
echo "Current npm version is: $(npm --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install Python3 - Version ${VERSION_PYTHON}"
echo "--------------------------------------------------------------------------------"
sudo apt-get install -qy libbz2-dev \
                         libreadline-dev \
                         libsqlite3-dev \
                         libssl-dev \
                         llvm \
                         tk-dev \
                         xz-utils \
                         zlib1g-dev
asdf plugin add python
# asdf list all python
asdf install python ${VERSION_PYTHON}
asdf global python ${VERSION_PYTHON}
echo "================================================================================"
ls -ll ~/.asdf/installs/python
echo "================================================================================"
echo "Current Python3 version is: $(python3 --version)"
echo "================================================================================"
wget -q https://bootstrap.pypa.io/get-pip.py
python3 get-pip.py --use-feature=2020-resolver
python3 -m pip install -r kxn_dev/requirements.txt
echo "================================================================================"
echo "Current pip version is: $(pip --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install Rust - Version ${VERSION_RUST}"
echo "--------------------------------------------------------------------------------"
sudo curl -sSL https://sh.rustup.rs -sSf | sh -s -- -y
source ${HOME}/.cargo/env
echo "================================================================================"
echo "Current Rust version is: $(rustc --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Install tmux - Version ${VERSION_TMUX}"
echo "--------------------------------------------------------------------------------"
sudo apt-get install -qy byacc
asdf plugin add tmux
# asdf list all tmux
asdf install tmux ${VERSION_TMUX}
asdf global tmux ${VERSION_TMUX}
echo "================================================================================"
ls -ll ~/.asdf/installs/tmux
echo "================================================================================"
echo "Current tmux version is: $(tmux -V)"
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
echo "Step: Install Yarn - Version ${VERSION_YARN}"
echo "--------------------------------------------------------------------------------"
asdf plugin add yarn
# asdf list all yarn
asdf install yarn ${VERSION_YARN}
asdf global yarn ${VERSION_YARN}
echo "================================================================================"
ls -ll ~/.asdf/installs/yarn
echo "================================================================================"
echo "Current Yarn version is: $(yarn --version)"
echo "================================================================================"

echo "--------------------------------------------------------------------------------"
echo "Step: Cleanup"
echo "--------------------------------------------------------------------------------"
sudo apt-get -qy autoremove
rm -f *.gz
rm -f *.gz.*
rm -f *.rpm
rm -f *.rpm.*
rm -f *.zip
rm -f *.zip.*
echo "================================================================================"
ls -ll /
echo "================================================================================"

sudo mkdir -p /usr/opt/dderl
sudo cp -r dderl/* /usr/opt/dderl/

echo "=====================================================================> Current Date: "
date
# Show Environment Variables ---------------------------------------------------
echo "=====================================================================> Environment variable GOROOT: "
echo ${GOROOT}
echo "=====================================================================> Environment variable GRADLE_HOME: "
echo ${GRADLE_HOME}
echo "=====================================================================> Environment variable LANG: "
echo ${LANG}
echo "=====================================================================> Environment variable LANGUAGE: "
echo ${LANGUAGE}
echo "=====================================================================> Environment variable LC_ALL: "
echo ${LC_ALL}
echo "=====================================================================> Environment variable LD_LIBRARY_PATH: "
echo ${LD_LIBRARY_PATH}
echo "=====================================================================> Environment variable ORACLE_HOME: "
echo ${ORACLE_HOME}
echo "=====================================================================> Environment variable PATH: "
echo ${PATH}
# Show component versions ------------------------------------------------------
echo "=====================================================================> Components"
echo "=====================================================================> Version  Alien: "
echo "Current version of Alien: $(alien --version)"
echo "=====================================================================> Version  asdf: "
echo "Current version of asdf: $(asdf --version)"
echo "=====================================================================> Version  CMake: "
echo "Current version of CMake: $(cmake --version)"
echo "=====================================================================> Version  cURL: "
echo "Current version of cURL: $(curl --version)"
echo "=====================================================================> Version  Docker Compose: "
echo "Current version of Docker Compose: $(docker-compose version)"
if [ "${HOST_ENVIRONMENT}" = "vm" ]; then
    echo "=====================================================================> Version  Docker Desktop: "
    echo "Current version of Docker Desktop: $(docker version)"
fi
echo "=====================================================================> Version  dos2unix: "
echo "Current version of dos2unix: $(dos2unix --version)"
echo "=====================================================================> Version  Eclipse: "
echo "Current version of Eclipse: ${VERSION_ECLIPSE_1}-${VERSION_ECLIPSE_2}"
echo "=====================================================================> Version  Elixir "
echo "Current version of Elixir: $(elixir -v)"
echo "Current version of Mix: $(mix --version)"
echo "=====================================================================> Version  Erlang: "
echo "Current version of Erlang: $(erl -eval '{ok, Version} = file:read_file(filename:join([code:root_dir(), "releases", erlang:system_info(otp_release), "OTP_VERSION"])), io:fwrite(Version), halt().' -noshell)"
echo "=====================================================================> Version  GCC / G++: "
echo "Current version of GCC: $(gcc --version)"
echo "Current version of G++: $(g++ --version)"
echo "=====================================================================> Version  Git: "
echo "Current version of Git: $(git --version)"
echo "=====================================================================> Version  GNU Autoconf: "
echo "Current version of GNU Autoconf: $(autoconf -V)"
echo "=====================================================================> Version  GNU Automake: "
echo "Current version of GNU Automake: $(automake --version)"
echo "=====================================================================> Version  GNU Make: "
echo "Current version of GNU Make: $(make --version)"
echo "=====================================================================> Version  Go: "
echo "Current version of Go: $(go version)"
echo "Current version of Go environment is: $(go env)"
echo "=====================================================================> Version  Gradle: "
echo "Current version of Gradle: $(gradle --version)"
echo "=====================================================================> Version  htop: "
echo "Current version of htop: $(htop --version)"
echo "=====================================================================> Version  Java: "
echo "Current version of Java SE Development Kit: $(java -version)"
echo "=====================================================================> Version  Kotlin: "
echo "Current version of Kotlin: $(kotlin -version)"
echo "=====================================================================> Version  LCOV: "
echo "Current version of LCOV: $(lcov --version)"
echo "=====================================================================> Version  nginx: "
echo "Current version of nginx: $(nginx -v)"
echo "=====================================================================> Version  Node.js /npm: "
echo "Current version of Node: $(node --version)"
echo "Current version of npm: $(npm --version)"
echo "=====================================================================> Version  OpenSSL: "
echo "Current version of OpenSSL: $(openssl version -a)"
echo "=====================================================================> Version  Oracle Instant Client: "
echo "Current version of Oracle Instant Client: $(sqlplus -V)"
echo "=====================================================================> Version  Procps-ng: "
echo "Current version of Procps-ng: $(ps --version)"
echo "=====================================================================> Version  Python3: "
echo "Current version of Python3: $(python3 --version)"
echo "Current version of pip: $(pip --version)"
echo "=====================================================================> Version  rebar3: "
echo "Current version of rebar3: $(rebar3 version)"
echo "=====================================================================> Version  Rust: "
echo "Current version of Rust: $(rustc --version)"
echo "=====================================================================> Version  tmux: "
echo "Current version of tmux: $(tmux -V)"
echo "=====================================================================> Version  Ubuntu: "
echo "Current version of Ubuntu: $(lsb_release -a)"
echo "=====================================================================> Version  Vim: "
echo "Current version of Vim: $(vim --version)"
echo "=====================================================================> Version  wget: "
echo "Current version of Wget: $(wget --version)"
echo "=====================================================================> Version  Yarn: "
echo "Current version of Yarn: $(yarn --version)"

echo ""
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"

exit
