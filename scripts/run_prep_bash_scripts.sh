#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_prep_bash_scripts.sh: Configure EOL and execution rights.
#
# ------------------------------------------------------------------------------

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Configue EOL and execution rights."
echo "--------------------------------------------------------------------------------"
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"

chmod +x -- "glob" *.sh
chmod +x -- "glob" */*.sh
chmod +x -- "glob" */*/*/*.sh

dos2unix -- "glob" *.sh
dos2unix -- "glob" */*.sh
dos2unix -- "glob" */*/*/*.sh

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
