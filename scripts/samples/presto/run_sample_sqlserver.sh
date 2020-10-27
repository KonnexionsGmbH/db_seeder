#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_test_presto_sqlserver.sh: Demonstration Issues Presto SQL Server Connector.
#
# ------------------------------------------------------------------------------

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DB Seeder - Demonstration Issues Presto SQL Server Connector."
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"

java -cp "${CLASSPATH}:lib/*" ch.konnexions.db_seeder.samples.presto.SampleSqlserver

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
