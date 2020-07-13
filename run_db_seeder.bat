@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder.bat: Creation of dummy data in an empty databases, 
rem                    database users or schemas.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set DB_SEEDER_DBMS_DEFAULT=sqlite
set DB_SEEDER_SETUP_DBMS_DEFAULT=yes
set DB_SEEDER_NO_CREATE_RUNS_DEFAULT=2

if ["%1"] EQU [""] (
    echo ===========================================
    echo complete    - All implemented DBMSs
    echo derby       - Apache Derby [client]
    echo derby_emb   - Apache Derby [embedded]
    echo cratedb     - CrateDB
    echo cubrid      - CUBRID
    echo firebird    - Firebird
    echo h2          - H2 Database Engine [client]
    echo h2_emb      - H2 Database Engine [embedded]
    echo hsqldb      - HyperSQL Database [client]
    echo hsqldb_emb  - HyperSQL Database [embedded]
    echo ibmdb2      - IBM Db2 Database
    echo informix    - IBM Informix
    echo mariadb     - MariaDB Server
    echo mimer       - Mimer SQL
    echo mssqlserver - Microsoft SQL Server
    echo mysql       - MySQL
    echo oracle      - Oracle Database
    echo postgresql  - PostgreSQL Database
    echo sqlite      - SQLite [embedded]
    echo -------------------------------------------
    set /P DB_SEEDER_DBMS="Enter the desired database management system [default: %DB_SEEDER_DBMS_DEFAULT%] "

    if ["!DB_SEEDER_DBMS!"] EQU [""] (
        set DB_SEEDER_DBMS=%DB_SEEDER_DBMS_DEFAULT%
    )
) else (
    set DB_SEEDER_DBMS=%1
)

if ["%2"] EQU [""] (
    set /P DB_SEEDER_SETUP_DBMS="Setup the DBMS (yes/no) [default: %DB_SEEDER_SETUP_DBMS_DEFAULT%] "

    if ["!DB_SEEDER_SETUP_DBMS!"] EQU [""] (
        set DB_SEEDER_SETUP_DBMS=%DB_SEEDER_SETUP_DBMS_DEFAULT%
    )
) else (
    set DB_SEEDER_SETUP_DBMS=%2
)

if ["%3"] EQU [""] (
    set /P DB_SEEDER_NO_CREATE_RUNS="Number of data creation runs (0-2) [default: %DB_SEEDER_NO_CREATE_RUNS_DEFAULT%] "

    if ["!DB_SEEDER_NO_CREATE_RUNS!"] EQU [""] (
        set DB_SEEDER_NO_CREATE_RUNS=%DB_SEEDER_NO_CREATE_RUNS_DEFAULT%
    )
) else (
    set DB_SEEDER_NO_CREATE_RUNS=%3
)

set DB_SEEDER_JAVA_CLASSPATH=%CLASSPATH%;lib/*

rem ------------------------------------------------------------------------------
rem Start Properties.
rem ------------------------------------------------------------------------------

set DB_SEEDER_DBMS_ORIG=%DB_SEEDER_DBMS% 

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=cratedb
)

if ["%DB_SEEDER_DBMS%"] EQU ["cratedb"] (
    set DB_SEEDER_CRATEDB_CONNECTION_HOST=
    set DB_SEEDER_CRATEDB_CONNECTION_PORT=
    set DB_SEEDER_CRATEDB_CONNECTION_PREFIX=
    set DB_SEEDER_CRATEDB_PASSWORD=
    set DB_SEEDER_CRATEDB_USER=
    set DB_SEEDER_CRATEDB_USER_SYS=
    set DB_SEEDER_CRATEDB_VERSION=4.1.6
    set DB_SEEDER_CRATEDB_VERSION=4.1.8
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=cubrid
)

if ["%DB_SEEDER_DBMS%"] EQU ["cubrid"] (
    set DB_SEEDER_ENCODING_UTF_8=false
    set DB_SEEDER_CUBRID_CONNECTION_HOST=
    set DB_SEEDER_CUBRID_CONNECTION_PORT=
    set DB_SEEDER_CUBRID_CONNECTION_PREFIX=
    set DB_SEEDER_CUBRID_CONNECTION_SUFFIX=
    set DB_SEEDER_CUBRID_DATABASE=kxn_db
    set DB_SEEDER_CUBRID_PASSWORD=
    set DB_SEEDER_CUBRID_USER=
    set DB_SEEDER_CUBRID_USER_SYS=
    set DB_SEEDER_CUBRID_VERSION=10.2
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=derby
)

if ["%DB_SEEDER_DBMS%"] EQU ["derby"] (
    set DB_SEEDER_DERBY_CONNECTION_HOST=
    set DB_SEEDER_DERBY_CONNECTION_PORT=
    set DB_SEEDER_DERBY_CONNECTION_PREFIX=
    set DB_SEEDER_DERBY_DATABASE=.\tmp\derby_kxn_db
    set DB_SEEDER_DERBY_VERSION=10.15.2.0
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=derby_emb
)

if ["%DB_SEEDER_DBMS%"] EQU ["derby_emb"] (
    rem TODO Bug in Apache Derby
    if ["!DB_SEEDER_NO_CREATE_RUNS!"] EQU ["2"] (
        set DB_SEEDER_NO_CREATE_RUNS=1
    )
    set DB_SEEDER_DERBY_CONNECTION_PREFIX=
    set DB_SEEDER_DERBY_DATABASE=.\tmp\derby_kxn_db
)

set DB_SEEDER_ENCODING_ISO_8859_1=
set DB_SEEDER_ENCODING_UTF_8=

set DB_SEEDER_FILE_CONFIGURATION_NAME=src\main\resources\db_seeder.properties

set DB_SEEDER_FILE_STATISTICS_DELIMITER=
set DB_SEEDER_FILE_STATISTICS_HEADER=

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=firebird
)

if ["%DB_SEEDER_DBMS%"] EQU ["firebird"] (
    set DB_SEEDER_FIREBIRD_CONNECTION_HOST=
    set DB_SEEDER_FIREBIRD_CONNECTION_PORT=
    set DB_SEEDER_FIREBIRD_CONNECTION_PREFIX=
    set DB_SEEDER_FIREBIRD_CONNECTION_SUFFIX=
    set DB_SEEDER_FIREBIRD_DATABASE=firebird_kxn_db
    set DB_SEEDER_FIREBIRD_PASSWORD=
    set DB_SEEDER_FIREBIRD_PASSWORD_SYS=
    set DB_SEEDER_FIREBIRD_USER=
    set DB_SEEDER_FIREBIRD_USER_SYS=
    set DB_SEEDER_FIREBIRD_VERSION=3.0.5
    set DB_SEEDER_FIREBIRD_VERSION=3.0.6
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=h2
)

if ["%DB_SEEDER_DBMS%"] EQU ["h2"] (
    set DB_SEEDER_H2_CONNECTION_HOST=
    set DB_SEEDER_H2_CONNECTION_PORT=
    set DB_SEEDER_H2_CONNECTION_PREFIX=
    set DB_SEEDER_H2_DATABASE=.\tmp\h2_kxn_db
    set DB_SEEDER_H2_PASSWORD=
    set DB_SEEDER_H2_SCHEMA=
    set DB_SEEDER_H2_USER=
    set DB_SEEDER_H2_VERSION=1.4.200
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=h2_emb
)

if ["%DB_SEEDER_DBMS%"] EQU ["h2_emb"] (
    set DB_SEEDER_H2_CONNECTION_PREFIX=
    set DB_SEEDER_H2_DATABASE=.\tmp\h2_kxn_db
    set DB_SEEDER_H2_PASSWORD=
    set DB_SEEDER_H2_SCHEMA=
    set DB_SEEDER_H2_USER=
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=hsqld
)

if ["%DB_SEEDER_DBMS%"] EQU ["hsqld"] (
    set DB_SEEDER_HSQLDB_CONNECTION_HOST=
    set DB_SEEDER_HSQLDB_CONNECTION_PORT=
    set DB_SEEDER_HSQLDB_CONNECTION_PREFIX=
    set DB_SEEDER_HSQLDB_CONNECTION_SUFFIX=
    set DB_SEEDER_HSQLDB_DATABASE=kxn_db
    set DB_SEEDER_HSQLDB_PASSWORD=
    set DB_SEEDER_HSQLDB_SCHEMA=
    set DB_SEEDER_HSQLDB_USER=
    set DB_SEEDER_HSQLDB_USER_SYS=
    set DB_SEEDER_HSQLDB_VERSION=2.5.1
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=hsqld_emb
)

if ["%DB_SEEDER_DBMS%"] EQU ["hsqld_emb"] (
    set DB_SEEDER_HSQLDB_CONNECTION_PREFIX=
    set DB_SEEDER_HSQLDB_CONNECTION_SUFFIX=
    set DB_SEEDER_HSQLDB_DATABASE=.\tmp\hsqldb_kxn_db
    set DB_SEEDER_HSQLDB_PASSWORD=
    set DB_SEEDER_HSQLDB_SCHEMA=
    set DB_SEEDER_HSQLDB_USER=
    set DB_SEEDER_HSQLDB_USER_SYS=
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=ibmdb2
)

if ["%DB_SEEDER_DBMS%"] EQU ["ibmdb2"] (
    set DB_SEEDER_IBMDB2_CONNECTION_HOST=
    set DB_SEEDER_IBMDB2_CONNECTION_PORT=
    set DB_SEEDER_IBMDB2_CONNECTION_PREFIX=
    set DB_SEEDER_IBMDB2_DATABASE=kxn_db
    set DB_SEEDER_IBMDB2_PASSWORD_SYS=
    set DB_SEEDER_IBMDB2_SCHEMA=
    set DB_SEEDER_IBMDB2_USER_SYS=
    set DB_SEEDER_IBMDB2_VERSION=11.5.0.0a
    set DB_SEEDER_IBMDB2_VERSION=11.5.4.0
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=informix
)

if ["%DB_SEEDER_DBMS%"] EQU ["informix"] (
    set DB_SEEDER_INFORMIX_CONNECTION_HOST=
    set DB_SEEDER_INFORMIX_CONNECTION_PORT=
    set DB_SEEDER_INFORMIX_CONNECTION_PREFIX=
    set DB_SEEDER_INFORMIX_CONNECTION_SUFFIX=
    set DB_SEEDER_INFORMIX_DATABASE=
    set DB_SEEDER_INFORMIX_DATABASE_SYS=
    set DB_SEEDER_INFORMIX_PASSWORD_SYS=
    set DB_SEEDER_INFORMIX_USER_SYS=
    set DB_SEEDER_INFORMIX_VERSION=14.10.FC3DE
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=mariadb
)

if ["%DB_SEEDER_DBMS%"] EQU ["mariadb"] (
    set DB_SEEDER_MARIADB_CONNECTION_HOST=
    set DB_SEEDER_MARIADB_CONNECTION_PORT=
    set DB_SEEDER_MARIADB_CONNECTION_PREFIX=
    set DB_SEEDER_MARIADB_DATABASE=
    set DB_SEEDER_MARIADB_DATABASE_SYS=
    set DB_SEEDER_MARIADB_PASSWORD=
    set DB_SEEDER_MARIADB_PASSWORD_SYS=
    set DB_SEEDER_MARIADB_USER=
    set DB_SEEDER_MARIADB_USER_SYS=
    set DB_SEEDER_MARIADB_VERSION=10.4.13
    set DB_SEEDER_MARIADB_VERSION=10.5.3
    set DB_SEEDER_MARIADB_VERSION=10.5.4
)

set DB_SEEDER_MAX_ROW_CITY=
set DB_SEEDER_MAX_ROW_COMPANY=
set DB_SEEDER_MAX_ROW_COUNTRY=
set DB_SEEDER_MAX_ROW_COUNTRY_STATE=
set DB_SEEDER_MAX_ROW_TIMEZONE=

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=mimer
)

if ["%DB_SEEDER_DBMS%"] EQU ["mimer"] (
    set DB_SEEDER_MIMER_CONNECTION_HOST=
    set DB_SEEDER_MIMER_CONNECTION_PORT=
    set DB_SEEDER_MIMER_CONNECTION_PREFIX=
    set DB_SEEDER_MIMER_DATABASE=
    set DB_SEEDER_MIMER_DATABASE_SYS=
    set DB_SEEDER_MIMER_PASSWORD=
    set DB_SEEDER_MIMER_PASSWORD_SYS=
    set DB_SEEDER_MIMER_USER=
    set DB_SEEDER_MIMER_USER_SYS=
    set DB_SEEDER_MIMER_VERSION=v11.0.3c
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=mssqlserver
)

if ["%DB_SEEDER_DBMS%"] EQU ["mssqlserver"] (
    set DB_SEEDER_ENCODING_UTF_8=false
    set DB_SEEDER_MSSQLSERVER_CONNECTION_HOST=
    set DB_SEEDER_MSSQLSERVER_CONNECTION_PORT=
    set DB_SEEDER_MSSQLSERVER_CONNECTION_PREFIX=
    set DB_SEEDER_MSSQLSERVER_DATABASE=
    set DB_SEEDER_MSSQLSERVER_DATABASE_SYS=
    set DB_SEEDER_MSSQLSERVER_PASSWORD=
    set DB_SEEDER_MSSQLSERVER_PASSWORD_SYS=
    set DB_SEEDER_MSSQLSERVER_SCHEMA=
    set DB_SEEDER_MSSQLSERVER_USER=
    set DB_SEEDER_MSSQLSERVER_USER_SYS=
    set DB_SEEDER_MSSQLSERVER_VERSION=2019-latest
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=mysql
)

if ["%DB_SEEDER_DBMS%"] EQU ["mysql"] (
    set DB_SEEDER_MYSQL_CONNECTION_HOST=
    set DB_SEEDER_MYSQL_CONNECTION_PORT=
    set DB_SEEDER_MYSQL_CONNECTION_PREFIX=
    set DB_SEEDER_MYSQL_CONNECTION_SUFFIX=
    set DB_SEEDER_MYSQL_DATABASE=
    set DB_SEEDER_MYSQL_DATABASE_SYS=
    set DB_SEEDER_MYSQL_PASSWORD=
    set DB_SEEDER_MYSQL_PASSWORD_SYS=
    set DB_SEEDER_MYSQL_USER=
    set DB_SEEDER_MYSQL_USER_SYS=
    set DB_SEEDER_MYSQL_VERSION=8.0.20
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=oracle
)

if ["%DB_SEEDER_DBMS%"] EQU ["oracle"] (
    set DB_SEEDER_ORACLE_CONNECTION_HOST=
    set DB_SEEDER_ORACLE_CONNECTION_PORT=
    set DB_SEEDER_ORACLE_CONNECTION_PREFIX=
    set DB_SEEDER_ORACLE_CONNECTION_SERVICE=
    set DB_SEEDER_ORACLE_PASSWORD=
    set DB_SEEDER_ORACLE_PASSWORD_SYS=
    set DB_SEEDER_ORACLE_USER=
    set DB_SEEDER_ORACLE_USER_SYS=
    set DB_SEEDER_ORACLE_VERSION=db_12_2_ee
    set DB_SEEDER_ORACLE_VERSION=db_18_3_ee
    set DB_SEEDER_ORACLE_VERSION=db_19_3_ee
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=postgresql
)

if ["%DB_SEEDER_DBMS%"] EQU ["postgresql"] (
    set DB_SEEDER_POSTGRESQL_CONNECTION_HOST=
    set DB_SEEDER_POSTGRESQL_CONNECTION_PORT=
    set DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX=
    set DB_SEEDER_POSTGRESQL_DATABASE=
    set DB_SEEDER_POSTGRESQL_DATABASE_SYS=
    set DB_SEEDER_POSTGRESQL_PASSWORD=
    set DB_SEEDER_POSTGRESQL_PASSWORD_SYS=
    set DB_SEEDER_POSTGRESQL_USER=
    set DB_SEEDER_POSTGRESQL_USER_SYS=
    set DB_SEEDER_POSTGRESQL_VERSION=12.3-alpine
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=sqlite
)

if ["%DB_SEEDER_DBMS%"] EQU ["sqlite"] (
    set DB_SEEDER_SQLITE_CONNECTION_PREFIX=
    set DB_SEEDER_SQLITE_DATABASE=.\tmp\sqlite_kxn_db
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=complete
)

rem ------------------------------------------------------------------------------
rem End   Properties.
rem ------------------------------------------------------------------------------

echo ================================================================================
echo Start %0
echo --------------------------------------------------------------------------------
echo DB Seeder - Creation of dummy data in an empty databases, database users or 
echo             schemas.
echo --------------------------------------------------------------------------------
echo DBMS                            : %DB_SEEDER_DBMS%
echo NO_CREATE_RUNS                  : %DB_SEEDER_NO_CREATE_RUNS%
echo SETUP_DBMS                      : %DB_SEEDER_SETUP_DBMS%
echo JAVA_CLASSPATH                  : %DB_SEEDER_JAVA_CLASSPATH%
echo --------------------------------------------------------------------------------

rem ------------------------------------------------------------------------------
rem Start Properties.
rem ------------------------------------------------------------------------------

set DB_SEEDER_DBMS_ORIG=%DB_SEEDER_DBMS% 

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=cratedb
)

if ["%DB_SEEDER_DBMS%"] EQU ["cratedb"] (
    echo CRATEDB_CONNECTION_HOST         : %DB_SEEDER_CRATEDB_CONNECTION_HOST%
    echo CRATEDB_CONNECTION_PORT         : %DB_SEEDER_CRATEDB_CONNECTION_PORT%
    echo CRATEDB_CONNECTION_PREFIX       : %DB_SEEDER_CRATEDB_CONNECTION_PREFIX%
    echo CRATEDB_PASSWORD                : %DB_SEEDER_CRATEDB_PASSWORD%
    echo CRATEDB_USER                    : %DB_SEEDER_CRATEDB_USER%
    echo CRATEDB_USER_SYS                : %DB_SEEDER_CRATEDB_USER_SYS%
    echo CRATEDB_VERSION                 : %DB_SEEDER_CRATEDB_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=cubrid
)

if ["%DB_SEEDER_DBMS%"] EQU ["cubrid"] (
    echo CUBRID_CONNECTION_HOST          : %DB_SEEDER_CUBRID_CONNECTION_HOST%
    echo CUBRID_CONNECTION_PORT          : %DB_SEEDER_CUBRID_CONNECTION_PORT%
    echo CUBRID_CONNECTION_PREFIX        : %DB_SEEDER_CUBRID_CONNECTION_PREFIX%
    echo CUBRID_CONNECTION_SUFFIX        : %DB_SEEDER_CUBRID_CONNECTION_SUFFIX%
    echo CUBRID_DATABASE                 : %DB_SEEDER_CUBRID_DATABASE%
    echo CUBRID_PASSWORD                 : %DB_SEEDER_CUBRID_PASSWORD%
    echo CUBRID_PASSWORD_SYS             : %DB_SEEDER_CUBRID_PASSWORD_SYS%
    echo CUBRID_USER                     : %DB_SEEDER_CUBRID_USER%
    echo CUBRID_USER_SYS                 : %DB_SEEDER_CUBRID_USER_SYS%
    echo CUBRID_VERSION                  : %DB_SEEDER_CUBRID_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=derby
)

if ["%DB_SEEDER_DBMS%"] EQU ["derby"] (
    echo DERBY_CONNECTION_HOST           : %DB_SEEDER_DERBY_CONNECTION_HOST%
    echo DERBY_CONNECTION_PORT           : %DB_SEEDER_DERBY_CONNECTION_PORT%
    echo DERBY_CONNECTION_PREFIX         : %DB_SEEDER_DERBY_CONNECTION_PREFIX%
    echo DERBY_DATABASE                  : %DB_SEEDER_DERBY_DATABASE%
    echo DERBY_VERSION                   : %DB_SEEDER_DERBY_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=derby_emb
)

if ["%DB_SEEDER_DBMS%"] EQU ["derby_emb"] (
    echo DERBY_CONNECTION_PREFIX         : %DB_SEEDER_DERBY_CONNECTION_PREFIX%
    echo DERBY_DATABASE                  : %DB_SEEDER_DERBY_DATABASE%
    echo --------------------------------------------------------------------------------
)

echo ENCODING_ISO_8859_1             : %DB_SEEDER_ENCODING_ISO_8859_1%
echo ENCODING_UTF_8                  : %DB_SEEDER_ENCODING_UTF_8%
echo --------------------------------------------------------------------------------

echo FILE_CONFIGURATION_NAME         : %DB_SEEDER_FILE_CONFIGURATION_NAME%
echo --------------------------------------------------------------------------------

echo FILE_STATISTICS_DELIMITER       : %DB_SEEDER_FILE_STATISTICS_DELIMITER%
echo FILE_STATISTICS_HEADER          : %DB_SEEDER_FILE_STATISTICS_HEADER%
echo FILE_STATISTICS_NAME            : %DB_SEEDER_FILE_STATISTICS_NAME%
echo --------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=firebird
)

if ["%DB_SEEDER_DBMS%"] EQU ["firebird"] (
    echo FIREBIRD_CONNECTION_HOST        : %DB_SEEDER_FIREBIRD_CONNECTION_HOST%
    echo FIREBIRD_CONNECTION_PORT        : %DB_SEEDER_FIREBIRD_CONNECTION_PORT%
    echo FIREBIRD_CONNECTION_PREFIX      : %DB_SEEDER_FIREBIRD_CONNECTION_PREFIX%
    echo FIREBIRD_CONNECTION_SUFFIX      : %DB_SEEDER_FIREBIRD_CONNECTION_SUFFIX%
    echo FIREBIRD_DATABASE               : %DB_SEEDER_FIREBIRD_DATABASE%
    echo FIREBIRD_PASSWORD               : %DB_SEEDER_FIREBIRD_PASSWORD%
    echo FIREBIRD_PASSWORD_SYS           : %DB_SEEDER_FIREBIRD_PASSWORD_SYS%
    echo FIREBIRD_USER                   : %DB_SEEDER_FIREBIRD_USER%
    echo FIREBIRD_USER_SYS               : %DB_SEEDER_FIREBIRD_USER_SYS%
    echo FIREBIRD_VERSION                : %DB_SEEDER_FIREBIRD_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=h2
)

if ["%DB_SEEDER_DBMS%"] EQU ["h2"] (
    echo H2_CONNECTION_HOST              : %DB_SEEDER_H2_CONNECTION_HOST%
    echo H2_CONNECTION_PORT              : %DB_SEEDER_H2_CONNECTION_PORT%
    echo H2_CONNECTION_PREFIX            : %DB_SEEDER_H2_CONNECTION_PREFIX%
    echo H2_DATABASE                     : %DB_SEEDER_H2_DATABASE%
    echo H2_PASSWORD                     : %DB_SEEDER_H2_PASSWORD%
    echo H2_SCHEMA                       : %DB_SEEDER_H2_SCHEMA%
    echo H2_USER                         : %DB_SEEDER_H2_USER%
    echo H2_VERSION                      : %DB_SEEDER_H2_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=h2_emb
)

if ["%DB_SEEDER_DBMS%"] EQU ["h2_emb"] (
    echo H2_CONNECTION_PREFIX            : %DB_SEEDER_H2_CONNECTION_PREFIX%
    echo H2_DATABASE                     : %DB_SEEDER_H2_DATABASE%
    echo H2_PASSWORD                     : %DB_SEEDER_H2_PASSWORD%
    echo H2_SCHEMA                       : %DB_SEEDER_H2_SCHEMA%
    echo H2_USER                         : %DB_SEEDER_H2_USER%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=hsqld
)

if ["%DB_SEEDER_DBMS%"] EQU ["hsqldb"] (
    echo HSQLDB_CONNECTION_HOST          : %DB_SEEDER_HSQLDB_CONNECTION_HOST%
    echo HSQLDB_CONNECTION_PORT          : %DB_SEEDER_HSQLDB_CONNECTION_PORT%
    echo HSQLDB_CONNECTION_PREFIX        : %DB_SEEDER_HSQLDB_CONNECTION_PREFIX%
    echo HSQLDB_CONNECTION_SUFFIX        : %DB_SEEDER_HSQLDB_CONNECTION_SUFFIX%
    echo HSQLDB_DATABASE                 : %DB_SEEDER_HSQLDB_DATABASE%
    echo HSQLDB_PASSWORD                 : %DB_SEEDER_HSQLDB_PASSWORD%
    echo HSQLDB_SCHEMA                   : %DB_SEEDER_HSQLDB_SCHEMA%
    echo HSQLDB_USER                     : %DB_SEEDER_HSQLDB_USER%
    echo HSQLDB_USER_SYS                 : %DB_SEEDER_HSQLDB_USER_SYS%
    echo HSQLDB_VERSION                  : %DB_SEEDER_HSQLDB_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=hsqld_emb
)

if ["%DB_SEEDER_DBMS%"] EQU ["hsqldb_emb"] (
    echo HSQLDB_CONNECTION_PREFIX        : %DB_SEEDER_HSQLDB_CONNECTION_PREFIX%
    echo HSQLDB_CONNECTION_SUFFIX        : %DB_SEEDER_HSQLDB_CONNECTION_SUFFIX%
    echo HSQLDB_DATABASE                 : %DB_SEEDER_HSQLDB_DATABASE%
    echo HSQLDB_PASSWORD                 : %DB_SEEDER_HSQLDB_PASSWORD%
    echo HSQLDB_SCHEMA                   : %DB_SEEDER_HSQLDB_SCHEMA%
    echo HSQLDB_USER                     : %DB_SEEDER_HSQLDB_USER%
    echo HSQLDB_USER_SYS                 : %DB_SEEDER_HSQLDB_USER_SYS%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=ibmdb2
)

if ["%DB_SEEDER_DBMS%"] EQU ["ibmdb2"] (
    echo IBMDB2_CONNECTION_HOST          : %DB_SEEDER_IBMDB2_CONNECTION_HOST%
    echo IBMDB2_CONNECTION_PORT          : %DB_SEEDER_IBMDB2_CONNECTION_PORT%
    echo IBMDB2_CONNECTION_PREFIX        : %DB_SEEDER_IBMDB2_CONNECTION_PREFIX%
    echo IBMDB2_DATABASE                 : %DB_SEEDER_IBMDB2_DATABASE%
    echo IBMDB2_PASSWORD_SYS             : %DB_SEEDER_IBMDB2_PASSWORD_SYS%
    echo IBMDB2_SCHEMA                   : %DB_SEEDER_IBMDB2_SCHEMA%
    echo IBMDB2_USER_SYS                 : %DB_SEEDER_IBMDB2_USER_SYS%
    echo IBMDB2_VERSION                  : %DB_SEEDER_IBMDB2_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=informix
)

if ["%DB_SEEDER_DBMS%"] EQU ["informix"] (
    echo INFORMIX_CONNECTION_HOST        : %DB_SEEDER_INFORMIX_CONNECTION_HOST%
    echo INFORMIX_CONNECTION_PORT        : %DB_SEEDER_INFORMIX_CONNECTION_PORT%
    echo INFORMIX_CONNECTION_PREFIX      : %DB_SEEDER_INFORMIX_CONNECTION_PREFIX%
    echo INFORMIX_CONNECTION_SUFFIX      : %DB_SEEDER_INFORMIX_CONNECTION_SUFFIX%
    echo INFORMIX_DATABASE               : %DB_SEEDER_INFORMIX_DATABASE%
    echo INFORMIX_DATABASE_SYS           : %DB_SEEDER_INFORMIX_DATABASE.SYS%
    echo INFORMIX_PASSWORD_SYS           : %DB_SEEDER_INFORMIX_PASSWORD.SYS%
    echo INFORMIX_USER_SYS               : %DB_SEEDER_INFORMIX_USER.SYS%
    echo INFORMIX_VERSION                : %DB_SEEDER_INFORMIX_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=mariadb
)

if ["%DB_SEEDER_DBMS%"] EQU ["mariadb"] (
    echo MARIADB_CONNECTION_HOST         : %DB_SEEDER_MARIADB_CONNECTION_HOST%
    echo MARIADB_CONNECTION_PORT         : %DB_SEEDER_MARIADB_CONNECTION_PORT%
    echo MARIADB_CONNECTION_PREFIX       : %DB_SEEDER_MARIADB_CONNECTION_PREFIX%
    echo MARIADB_DATABASE                : %DB_SEEDER_MARIADB_DATABASE%
    echo MARIADB_DATABASE_SYS            : %DB_SEEDER_MARIADB_DATABASE_SYS%
    echo MARIADB_PASSWORD                : %DB_SEEDER_MARIADB_PASSWORD%
    echo MARIADB_PASSWORD_SYS            : %DB_SEEDER_MARIADB_PASSWORD_SYS%
    echo MARIADB_USER                    : %DB_SEEDER_MARIADB_USER%
    echo MARIADB_USER_SYS                : %DB_SEEDER_MARIADB_USER_SYS%
    echo MARIADB_VERSION                 : %DB_SEEDER_MARIADB_VERSION%
    echo --------------------------------------------------------------------------------
)

echo MAX_ROW_CITY                    : %DB_SEEDER_MAX_ROW_CITY%
echo MAX_ROW_COMPANY                 : %DB_SEEDER_MAX_ROW_COMPANY%
echo MAX_ROW_COUNTRY                 : %DB_SEEDER_MAX_ROW_COUNTRY%
echo MAX_ROW_COUNTRY_STATE           : %DB_SEEDER_MAX_ROW_COUNTRY_STATE%
echo MAX_ROW_TIMEZONE                : %DB_SEEDER_MAX_ROW_TIMEZONE%
echo --------------------------------------------------------------------------------

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=mimer
)

if ["%DB_SEEDER_DBMS%"] EQU ["mimer"] (
    echo MIMER_CONNECTION_HOST           : %DB_SEEDER_MIMER_CONNECTION_HOST%
    echo MIMER_CONNECTION_PORT           : %DB_SEEDER_MIMER_CONNECTION_PORT%
    echo MIMER_CONNECTION_PREFIX         : %DB_SEEDER_MIMER_CONNECTION_PREFIX%
    echo MIMER_DATABASE                  : %DB_SEEDER_MIMER_DATABASE%
    echo MIMER_DATABASE_SYS              : %DB_SEEDER_MIMER_DATABASE_SYS%
    echo MIMER_PASSWORD                  : %DB_SEEDER_MIMER_PASSWORD%
    echo MIMER_PASSWORD_SYS              : %DB_SEEDER_MIMER_PASSWORD_SYS%
    echo MIMER_USER                      : %DB_SEEDER_MIMER_USER%
    echo MIMER_USER_SYS                  : %DB_SEEDER_MIMER_USER_SYS%
    echo MIMER_VERSION                   : %DB_SEEDER_MIMER_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=mssqlserver
)

if ["%DB_SEEDER_DBMS%"] EQU ["mssqlserver"] (
    echo MSSQLSERVER_CONNECTION_HOST     : %DB_SEEDER_MSSQLSERVER_CONNECTION_HOST%
    echo MSSQLSERVER_CONNECTION_PORT     : %DB_SEEDER_MSSQLSERVER_CONNECTION_PORT%
    echo MSSQLSERVER_CONNECTION_PREFIX   : %DB_SEEDER_MSSQLSERVER_CONNECTION_PREFIX%
    echo MSSQLSERVER_DATABASE            : %DB_SEEDER_MSSQLSERVER_DATABASE%
    echo MSSQLSERVER_DATABASE_SYS        : %DB_SEEDER_MSSQLSERVER_DATABASE_SYS%
    echo MSSQLSERVER_PASSWORD            : %DB_SEEDER_MSSQLSERVER_PASSWORD%
    echo MSSQLSERVER_PASSWORD_SYS        : %DB_SEEDER_MSSQLSERVER_PASSWORD_SYS%
    echo MSSQLSERVER_SCHEMA              : %DB_SEEDER_MSSQLSERVER_SCHEMA%
    echo MSSQLSERVER_USER                : %DB_SEEDER_MSSQLSERVER_USER%
    echo MSSQLSERVER_USER_SYS            : %DB_SEEDER_MSSQLSERVER_USER_SYS%
    echo MSSQLSERVER_VERSION             : %DB_SEEDER_MSSQLSERVER_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=mysql
)

if ["%DB_SEEDER_DBMS%"] EQU ["mysql"] (
    echo MYSQL_CONNECTION_HOST           : %DB_SEEDER_MYSQL_CONNECTION_HOST%
    echo MYSQL_CONNECTION_PORT           : %DB_SEEDER_MYSQL_CONNECTION_PORT%
    echo MYSQL_CONNECTION_PREFIX         : %DB_SEEDER_MYSQL_CONNECTION_PREFIX%
    echo MYSQL_CONNECTION_SUFFIX         : %DB_SEEDER_MYSQL_CONNECTION_SUFFIX%
    echo MYSQL_DATABASE                  : %DB_SEEDER_MYSQL_DATABASE%
    echo MYSQL_DATABASE_SYS              : %DB_SEEDER_MYSQL_DATABASE_SYS%
    echo MYSQL_PASSWORD                  : %DB_SEEDER_MYSQL_PASSWORD%
    echo MYSQL_PASSWORD_SYS              : %DB_SEEDER_MYSQL_PASSWORD_SYS%
    echo MYSQL_USER                      : %DB_SEEDER_MYSQL_USER%
    echo MYSQL_USER_SYS                  : %DB_SEEDER_MYSQL_USER_SYS%
    echo MYSQL_VERSION                   : %DB_SEEDER_MYSQL_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=oracle
)

if ["%DB_SEEDER_DBMS%"] EQU ["oracle"] (
    echo ORACLE_CONNECTION_HOST          : %DB_SEEDER_ORACLE_CONNECTION_HOST%
    echo ORACLE_CONNECTION_PORT          : %DB_SEEDER_ORACLE_CONNECTION_PORT%
    echo ORACLE_CONNECTION_PREFIX        : %DB_SEEDER_ORACLE_CONNECTION_PREFIX%
    echo ORACLE_CONNECTION_SERVICE       : %DB_SEEDER_ORACLE_CONNECTION_SERVICE%
    echo ORACLE_PASSWORD                 : %DB_SEEDER_ORACLE_PASSWORD%
    echo ORACLE_PASSWORD_SYS             : %DB_SEEDER_ORACLE_PASSWORD_SYS%
    echo ORACLE_USER                     : %DB_SEEDER_ORACLE_USER%
    echo ORACLE_USER_SYS                 : %DB_SEEDER_ORACLE_USER_SYS%
    echo ORACLE_VERSION                  : %DB_SEEDER_ORACLE_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=postgresql
)

if ["%DB_SEEDER_DBMS%"] EQU ["postgresql"] (
    echo POSTGRESQL_CONNECTION_HOST      : %DB_SEEDER_POSTGRESQL_CONNECTION_HOST%
    echo POSTGRESQL_CONNECTION_PORT      : %DB_SEEDER_POSTGRESQL_CONNECTION_PORT%
    echo POSTGRESQL_CONNECTION_PREFIX    : %DB_SEEDER_POSTGRESQL_CONNECTION_PREFIX%
    echo POSTGRESQL_DATABASE             : %DB_SEEDER_POSTGRESQL_DATABASE%
    echo POSTGRESQL_DATABASE_SYS         : %DB_SEEDER_POSTGRESQL_DATABASE_SYS%
    echo POSTGRESQL_PASSWORD             : %DB_SEEDER_POSTGRESQL_PASSWORD%
    echo POSTGRESQL_PASSWORD_SYS         : %DB_SEEDER_POSTGRESQL_PASSWORD_SYS%
    echo POSTGRESQL_USER                 : %DB_SEEDER_POSTGRESQL_USER%
    echo POSTGRESQL_USER_SYS             : %DB_SEEDER_POSTGRESQL_USER_SYS%
    echo POSTGRESQL_VERSION              : %DB_SEEDER_POSTGRESQL_VERSION%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=sqlite
)

if ["%DB_SEEDER_DBMS%"] EQU ["sqlite"] (
    echo SQLITE_CONNECTION_PREFIX        : %DB_SEEDER_SQLITE_CONNECTION_PREFIX%
    echo SQLITE_DATABASE                 : %DB_SEEDER_SQLITE_DATABASE%
    echo --------------------------------------------------------------------------------
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete"] (
    set DB_SEEDER_DBMS=complete
)

rem ------------------------------------------------------------------------------
rem End   Properties.
rem ------------------------------------------------------------------------------

echo:| TIME
echo ================================================================================

if ["%DB_SEEDER_DBMS%"] EQU ["complete"] (
    call scripts\run_db_seeder_complete
) else (
    call scripts\run_db_seeder_single %DB_SEEDER_DBMS%
)

echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
