#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_test_presto_postgresql.sh: Demonstration Issues Presto PostgreSQL Connector.
#
# ------------------------------------------------------------------------------

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "DBSeeder - Demonstration Issues Presto PostgreSQL Connector."
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"

rm -f db_seeder.log

java -cp "${CLASSPATH}:lib/*" ch.konnexions.db_seeder.samples.presto.SamplePostgresql

echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"
