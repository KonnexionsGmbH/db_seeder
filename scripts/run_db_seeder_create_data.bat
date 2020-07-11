@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder_create_data.bat: Creation of dummy data in an empty database schema / user.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - Creation of dummy data in an empty database schema / user.
echo --------------------------------------------------------------------------------
echo DBMS                            : %DB_SEEDER_DBMS%
echo DBMS_EMBEDDED                   : %DB_SEEDER_DBMS_EMBEDDED%
echo --------------------------------------------------------------------------------
echo FILE_CONFIGURATION_NAME         : %DB_SEEDER_FILE_CONFIGURATION_NAME%
echo JAVA_CLASSPATH                  : %DB_SEEDER_JAVA_CLASSPATH%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

if exist db_seeder.log del /f /q db_seeder.log

java --enable-preview -cp %DB_SEEDER_JAVA_CLASSPATH% ch.konnexions.db_seeder.DatabaseSeeder %DB_SEEDER_DBMS%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
