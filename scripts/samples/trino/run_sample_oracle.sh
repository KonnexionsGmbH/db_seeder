#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_test_presto_mysql.sh: Demonstration Issues Presto Oracle Connector.
#
# ------------------------------------------------------------------------------

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Demonstration Issues Presto Oracle Connector."
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"

rm -f db_seeder.log

java -cp "${CLASSPATH}:lib/*" ch.konnexions.db_seeder.samples.presto.SampleOracle

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
