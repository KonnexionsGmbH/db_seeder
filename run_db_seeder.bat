@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder.bat: Creation of dummy data in an empty database schema / user.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_DATABASE_BRAND_DEFAULT=oracle

set DB_SEEDER_FILE_CONFIGURATION_NAME=src\main\resources\db_seeder.properties

set DB_SEEDER_JAVA_CLASSPATH=.;lib/*;JAVA_HOME/lib;

set DB_SEEDER_JDBC_CONNECTION_HOST=192.168.1.109
set DB_SEEDER_JDBC_CONNECTION_PORT=

set DB_SEEDER_MAX_ROW_CITY=
set DB_SEEDER_MAX_ROW_COMPANY=
set DB_SEEDER_MAX_ROW_COUNTRY=
set DB_SEEDER_MAX_ROW_COUNTRY_STATE=
set DB_SEEDER_MAX_ROW_TIMEZONE=

set DB_SEEDER_ORACLE_CONNECTION_SERVICE=
set DB_SEEDER_ORACLE_PASSWORD=
set DB_SEEDER_ORACLE_PASSWORD_SYS=
set DB_SEEDER_ORACLE_USER=kxn_user
set DB_SEEDER_ORACLE_USER_SYS=sys

if ["%1"] EQU [""] (
    set /P DB_SEEDER_DATABASE_BRAND="Enter the desired database brand (mysql/oracle) [default: %DB_SEEDER_DATABASE_BRAND_DEFAULT%] "

    if ["!DB_SEEDER_DATABASE_BRAND!"] EQU [""] (
        set DB_SEEDER_DATABASE_BRAND=%DB_SEEDER_DATABASE_BRAND_DEFAULT%
    )
) else (
    set DB_SEEDER_DATABASE_BRAND=%1
)

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - Creation of dummy data in an empty database schema / user.
echo --------------------------------------------------------------------------------
echo DATABASE_BRAND            : %DB_SEEDER_DATABASE_BRAND%
echo --------------------------------------------------------------------------------
echo FILE_CONFIGURATION_NAME   : %DB_SEEDER_FILE_CONFIGURATION_NAME%
echo JAVA_CLASSPATH            : %DB_SEEDER_JAVA_CLASSPATH%
echo --------------------------------------------------------------------------------
echo CONNECTION_HOST           : %DB_SEEDER_JDBC_CONNECTION_HOST%
echo CONNECTION_PORT           : %DB_SEEDER_JDBC_CONNECTION_PORT%
echo --------------------------------------------------------------------------------
echo MAX_ROW_CITY              : %DB_SEEDER_MAX_ROW_CITY%
echo MAX_ROW_COMPANY           : %DB_SEEDER_MAX_ROW_COMPANY%
echo MAX_ROW_COUNTRY           : %DB_SEEDER_MAX_ROW_COUNTRY%
echo MAX_ROW_COUNTRY_STATE     : %DB_SEEDER_MAX_ROW_COUNTRY_STATE%
echo MAX_ROW_TIMEZONE          : %DB_SEEDER_MAX_ROW_TIMEZONE%
echo --------------------------------------------------------------------------------
if ["%DB_SEEDER_DATABASE_BRAND%"] EQU ["oracle"] (
    echo ORACLE_CONNECTION_SERVICE : %DB_SEEDER_ORACLE_CONNECTION_SERVICE%
    echo ORACLE_USER               : %DB_SEEDER_ORACLE_USER%
    echo ORACLE_USER_PASSWORD      : %DB_SEEDER_ORACLE_PASSWORD%
    echo ORACLE_USER_SYS           : %DB_SEEDER_ORACLE_USER_SYS%
    echo ORACLE_USER_PASSWORD_SYS  : %DB_SEEDER_ORACLE_PASSWORD_SYS%
    echo --------------------------------------------------------------------------------
)
echo:| TIME
echo ================================================================================

java --enable-preview -cp %DB_SEEDER_JAVA_CLASSPATH% ch.konnexions.db_seeder.DatabaseSeeder %DB_SEEDER_DATABASE_BRAND%

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
