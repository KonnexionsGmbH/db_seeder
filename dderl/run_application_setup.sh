#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_application_setup.sh: Setup the DDErl application.
#
# ------------------------------------------------------------------------------

export HOME_DDERL=/dderl

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "Setup the DDErl application."
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

if [ ! -d "$HOME_DDERL" ]; then
    echo "Create the DDErl repository"
    echo "---------------------------------------------------------"
    git clone https://walter-weinmann:56a0cdaecdcd70a22671509d5513f0bca13eedc7@github.com/KonnexionsGmbH/dderl.git

    echo "Create the snapshot directory"
    echo "---------------------------------------------------------"
    mkdir $HOME_DDERL/snapshot
    cp /usr/opt/dderl/backup_snapshot_docker_compose.zip $HOME_DDERL/snapshot

    echo "Create the frontend to DDErl"
    echo "---------------------------------------------------------"
    cd $HOME_DDERL
    ./build_fe.sh

else
    cd $HOME_DDERL
fi

echo "Compile and start DDErl"
echo "---------------------------------------------------------"
rebar3 shell

echo ""
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"

exit
