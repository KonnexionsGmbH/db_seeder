#!/bin/bash

set -e

# ------------------------------------------------------------------------------
#
# run_db_seeder_docker_compose.sh: Run the Docker Compose version of the db_seeder application.
#
# ------------------------------------------------------------------------------

export DB_SEEDER_RELEASE=2.5.2

echo "================================================================================"
echo "Start $0"
echo "--------------------------------------------------------------------------------"
echo "Run the Docker Compose version of the db_seeder application."
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "================================================================================"
echo "Run db_seeder"
echo "---------------------------------------------------------"

export DB_SEEDER_CONNECTION_HOST=db_seeder_db

export DB_SEEDER_FILE_JSON_NAME=resources/json/db_seeder_schema.company.json
export DB_SEEDER_FILE_STATISTICS_NAME=/Transfer_db_seeder/db_seeder_local_client_${DB_SEEDER_RELEASE}_company_$(date +"%Y.%m.%d_%H.%M.%S").tsv
./run_db_seeder.sh complete_client yes 2

export DB_SEEDER_FILE_JSON_NAME=resources/json/db_seeder_schema.syntax.json
export DB_SEEDER_FILE_STATISTICS_NAME=/Transfer_db_seeder/db_seeder_local_client_${DB_SEEDER_RELEASE}_syntax_$(date +"%Y.%m.%d_%H.%M.%S").tsv
./run_db_seeder.sh complete_client yes 2

export DB_SEEDER_FILE_JSON_NAME=resources/json/db_seeder_schema.company.json
export DB_SEEDER_FILE_STATISTICS_NAME=/Transfer_db_seeder/db_seeder_local_embedded_${DB_SEEDER_RELEASE}_company_$(date +"%Y.%m.%d_%H.%M.%S").tsv
./run_db_seeder.sh complete_emb yes 2

export DB_SEEDER_FILE_JSON_NAME=resources/json/db_seeder_schema.company.json
export DB_SEEDER_FILE_STATISTICS_NAME=/Transfer_db_seeder/db_seeder_local_presto_${DB_SEEDER_RELEASE}_company_$(date +"%Y.%m.%d_%H.%M.%S").tsv
./run_db_seeder.sh complete_presto yes 2

echo ""
echo "--------------------------------------------------------------------------------"
date +"DATE TIME : %d.%m.%Y %H:%M:%S"
echo "--------------------------------------------------------------------------------"
echo "End   $0"
echo "================================================================================"

exit
