@echo off

rem ------------------------------------------------------------------------------
rem
rem run_db_seeder.bat: Creation of dummy data in an empty databases, 
rem                    database users or schemas.
rem
rem ------------------------------------------------------------------------------

setlocal EnableDelayedExpansion

set ERRORLEVEL=

set DB_SEEDER_DBMS_DEFAULT=sqlite
set DB_SEEDER_NO_CREATE_RUNS_DEFAULT=2
set DB_SEEDER_RELEASE=2.5.0
set DB_SEEDER_SETUP_DBMS_DEFAULT=yes
set DB_SEEDER_VERSION_PRESTO=340

if ["%1"] EQU [""] (
    echo =========================================================
    echo complete_client    - All implemented client DBMSs
    echo complete_emb       - All implemented embedded DBMSs
    echo complete_presto    - All implemented Presto enabled DBMSs
    echo ---------------------------------------------------------
    echo agens              - AgensGraph [client]
    echo derby              - Apache Derby [client]
    echo derby_emb          - Apache Derby [embedded]
    echo cratedb            - CrateDB
    echo cubrid             - CUBRID
    echo exasol             - Exasol
    echo firebird           - Firebird
    echo h2                 - H2 Database Engine [client]
    echo h2_emb             - H2 Database Engine [embedded]
    echo hsqldb             - HyperSQL Database [client]
    echo hsqldb_emb         - HyperSQL Database [embedded]
    echo ibmdb2             - IBM Db2 Database
    echo informix           - IBM Informix
    echo mariadb            - MariaDB Server
    echo mimer              - Mimer SQL
    echo monetdb            - MonetDB
    echo sqlserver          - Microsoft SQL Server
    echo sqlserver_presto   - Microsoft SQL Server via Presto
    echo mysql              - MySQL Database
    echo mysql_presto       - MySQL Database via Presto
    echo oracle             - Oracle Database
    echo oracle_presto      - Oracle Database via Presto
    echo percona            - Percona Server for MySQL
    echo postgresql         - PostgreSQL Database
    echo postgresql_presto  - PostgreSQL Database via Presto
    echo sqlite             - SQLite [embedded]
    echo voltdb             - VoltDB
    echo yugabyte           - YugabyteDB
    echo ---------------------------------------------------------
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

if ["%DB_SEEDER_COMPLETE_RUN%"] EQU [""] (
    set DB_SEEDER_COMPLETE_RUN=no
)

set DB_SEEDER_DBMS_EMBEDDED=no
set DB_SEEDER_DBMS_PRESTO=no

set DB_SEEDER_FILE_CONFIGURATION_NAME=src\main\resources\db_seeder.properties
set DB_SEEDER_FILE_STATISTICS_DELIMITER=\t

if ["%DB_SEEDER_FILE_STATISTICS_NAME%"] EQU [""] (
    set DB_SEEDER_FILE_STATISTICS_NAME=resources\statistics\db_seeder_local.tsv
)    

set DB_SEEDER_DBMS_ORIG=%DB_SEEDER_DBMS% 

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=agens
)

if ["%DB_SEEDER_DBMS%"] EQU ["agens"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=5432
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:agensgraph://
    set DB_SEEDER_CONTAINER_PORT=5432
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=agens
    set DB_SEEDER_PASSWORD=agens
    set DB_SEEDER_PASSWORD_SYS=agens
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=agens
    set DB_SEEDER_VERSION=v2.1.1
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=cratedb
)

if ["%DB_SEEDER_DBMS%"] EQU ["cratedb"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=5432
    set DB_SEEDER_CONNECTION_PREFIX=crate://
    set DB_SEEDER_CONTAINER_PORT=5432
    set DB_SEEDER_PASSWORD=cratedb
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=crate
    set DB_SEEDER_VERSION=4.1.6
    set DB_SEEDER_VERSION=4.1.8
    set DB_SEEDER_VERSION=4.2.2
    set DB_SEEDER_VERSION=4.2.3
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=cubrid
)

if ["%DB_SEEDER_DBMS%"] EQU ["cubrid"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=33000
    set DB_SEEDER_CONNECTION_PREFIX="jdbc:cubrid:"
    set DB_SEEDER_CONTAINER_PORT=33000
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_PASSWORD=cubrid
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=DBA
    set DB_SEEDER_VERSION=10.2
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=derby
)

if ["%DB_SEEDER_DBMS%"] EQU ["derby"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=1527
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:derby:
    set DB_SEEDER_CONTAINER_PORT=1527
    set DB_SEEDER_DATABASE=.\tmp\derby_kxn_db
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_VERSION=10.15.2.0
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_emb"] (
    set DB_SEEDER_DBMS=derby_emb
)

if ["%DB_SEEDER_DBMS%"] EQU ["derby_emb"] (
    rem TODO Bug in Apache Derby
    if ["%DB_SEEDER_NO_CREATE_RUNS%"] EQU ["2"] (
        set DB_SEEDER_NO_CREATE_RUNS=1
    )
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:derby:
    set DB_SEEDER_DATABASE=.\tmp\derby_kxn_db
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_DBMS_EMBEDDED=yes
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=exasol
)

if ["%DB_SEEDER_DBMS%"] EQU ["exasol"] (
    set DB_SEEDER_CONNECTION_HOST=127.0.0.1
    set DB_SEEDER_CONNECTION_PORT=8899
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:exa:
    set DB_SEEDER_CONTAINER_PORT=8888
    set DB_SEEDER_PASSWORD=exasol
    set DB_SEEDER_PASSWORD_SYS=exasol
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=sys
    set DB_SEEDER_VERSION=6.2.8-d1
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=firebird
)

if ["%DB_SEEDER_DBMS%"] EQU ["firebird"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=3050
    set DB_SEEDER_CONNECTION_PREFIX="jdbc:firebirdsql://"
    set DB_SEEDER_CONNECTION_SUFFIX="?encoding=UTF8&useFirebirdAutocommit=true&useStreamBlobs=true"
    set DB_SEEDER_CONTAINER_PORT=3050
    set DB_SEEDER_DATABASE=firebird_kxn_db
    set DB_SEEDER_PASSWORD=firebird
    set DB_SEEDER_PASSWORD_SYS=firebird
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=SYSDBA
    set DB_SEEDER_VERSION=3.0.5
    set DB_SEEDER_VERSION=3.0.6
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=h2
)

if ["%DB_SEEDER_DBMS%"] EQU ["h2"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=9092
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:h2:
    set DB_SEEDER_CONTAINER_PORT=9092
    set DB_SEEDER_DATABASE=.\tmp\h2_kxn_db
    set DB_SEEDER_PASSWORD=h2
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=sa
    set DB_SEEDER_VERSION=1.4.200
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_emb"] (
    set DB_SEEDER_DBMS=h2_emb
)

if ["%DB_SEEDER_DBMS%"] EQU ["h2_emb"] (
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:h2:
    set DB_SEEDER_DATABASE=.\tmp\h2_kxn_db
    set DB_SEEDER_DBMS_EMBEDDED=yes
    set DB_SEEDER_PASSWORD=h2
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=sa
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=hsqld
)

if ["%DB_SEEDER_DBMS%"] EQU ["hsqldb"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=9001
    set DB_SEEDER_CONNECTION_PREFIX="jdbc:hsqldb:"
    set DB_SEEDER_CONNECTION_SUFFIX=";ifexists=false;shutdown=true"
    set DB_SEEDER_CONTAINER_PORT=9001
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_PASSWORD=hsqldb
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=SA
    set DB_SEEDER_VERSION=2.5.1
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_emb"] (
    set DB_SEEDER_DBMS=hsqld_emb
)

if ["%DB_SEEDER_DBMS%"] EQU ["hsqldb_emb"] (
    set DB_SEEDER_CONNECTION_PREFIX="jdbc:hsqldb:"
    set DB_SEEDER_CONNECTION_SUFFIX=";ifexists=false;shutdown=true"
    set DB_SEEDER_DATABASE=.\tmp\hsqldb_kxn_db
    set DB_SEEDER_DBMS_EMBEDDED=yes
    set DB_SEEDER_PASSWORD=hsqldb
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=SA
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=ibmdb2
)

if ["%DB_SEEDER_DBMS%"] EQU ["ibmdb2"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=50000
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:db2://
    set DB_SEEDER_CONTAINER_PORT=50000
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_PASSWORD_SYS=ibmdb2
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER_SYS=db2inst1
    set DB_SEEDER_VERSION=11.5.0.0a
    set DB_SEEDER_VERSION=11.5.4.0
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=informix
)

if ["%DB_SEEDER_DBMS%"] EQU ["informix"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=9088
    set DB_SEEDER_CONNECTION_PREFIX="jdbc:informix-sqli://"
    set DB_SEEDER_CONTAINER_PORT=9088
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=sysmaster
    set DB_SEEDER_PASSWORD_SYS=in4mix
    set DB_SEEDER_USER_SYS=informix
    set DB_SEEDER_VERSION=14.10.FC3DE
    set DB_SEEDER_VERSION=14.10.FC4DE
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=mariadb
)

if ["%DB_SEEDER_DBMS%"] EQU ["mariadb"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=3306
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:mariadb://
    set DB_SEEDER_CONTAINER_PORT=3306
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=mysql
    set DB_SEEDER_PASSWORD=mariadb
    set DB_SEEDER_PASSWORD_SYS=mariadb
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=root
    set DB_SEEDER_VERSION=10.4.13
    set DB_SEEDER_VERSION=10.5.3
    set DB_SEEDER_VERSION=10.5.4
    set DB_SEEDER_VERSION=10.5.5
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=mimer
)

if ["%DB_SEEDER_DBMS%"] EQU ["mimer"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=11360
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:mimer://
    set DB_SEEDER_CONTAINER_PORT=1360
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=mimerdb
    set DB_SEEDER_PASSWORD=mimer
    set DB_SEEDER_PASSWORD_SYS=mimer
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=SYSADM
    set DB_SEEDER_VERSION=v11.0.3c
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=monetdb
)

if ["%DB_SEEDER_DBMS%"] EQU ["monetdb"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=50000
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:monetdb://
    set DB_SEEDER_CONTAINER_PORT=50000
    set DB_SEEDER_DATABASE_SYS=demo
    set DB_SEEDER_PASSWORD=monetdb
    set DB_SEEDER_PASSWORD_SYS=monetdb
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=monetdb
    set DB_SEEDER_VERSION=Jun2020-SP1
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=mysql
)

if ["%DB_SEEDER_DBMS%"] EQU ["mysql"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=3306
    set DB_SEEDER_CONNECTION_PREFIX="jdbc:mysql://"
    set DB_SEEDER_CONNECTION_SUFFIX="&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false&rewriteBatchedStatements=true"
    set DB_SEEDER_CONTAINER_PORT=3306
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=sys
    set DB_SEEDER_PASSWORD=mysql
    set DB_SEEDER_PASSWORD_SYS=mysql
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=root
    set DB_SEEDER_VERSION=8.0.20
    set DB_SEEDER_VERSION=8.0.21
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_presto"] (
    set DB_SEEDER_DBMS=mysql_presto
)

if ["%DB_SEEDER_DBMS%"] EQU ["mysql_presto"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=3306
    set DB_SEEDER_CONNECTION_PREFIX="jdbc:mysql://"
    set DB_SEEDER_CONNECTION_SUFFIX="&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false&rewriteBatchedStatements=true"
    set DB_SEEDER_CONTAINER_PORT=3306
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=sys
    set DB_SEEDER_DBMS_PRESTO=yes
    set DB_SEEDER_PASSWORD=mysql
    set DB_SEEDER_PASSWORD_SYS=mysql
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=root
    set DB_SEEDER_VERSION=8.0.20
    set DB_SEEDER_VERSION=8.0.21
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=oracle
)

if ["%DB_SEEDER_DBMS%"] EQU ["oracle"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=1521
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:oracle:thin:@//
    set DB_SEEDER_CONNECTION_SERVICE=orclpdb1
    set DB_SEEDER_CONTAINER_PORT=1521
    set DB_SEEDER_PASSWORD=oracle
    set DB_SEEDER_PASSWORD_SYS=oracle
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=SYS AS SYSDBA
    set DB_SEEDER_VERSION=db_12_2_ee
    set DB_SEEDER_VERSION=db_18_3_ee
    set DB_SEEDER_VERSION=db_19_3_ee
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_presto"] (
    set DB_SEEDER_DBMS=oracle_presto
)

if ["%DB_SEEDER_DBMS%"] EQU ["oracle_presto"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=1521
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:oracle:thin:@//
    set DB_SEEDER_CONNECTION_SERVICE=orclpdb1
    set DB_SEEDER_CONTAINER_PORT=1521
    set DB_SEEDER_DBMS_PRESTO=yes
    set DB_SEEDER_PASSWORD=oracle
    set DB_SEEDER_PASSWORD_SYS=oracle
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=SYS AS SYSDBA
    set DB_SEEDER_VERSION=db_12_2_ee
    set DB_SEEDER_VERSION=db_18_3_ee
    set DB_SEEDER_VERSION=db_19_3_ee
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=percona
)

if ["%DB_SEEDER_DBMS%"] EQU ["percona"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=3306
    set DB_SEEDER_CONNECTION_PREFIX="jdbc:mysql://"
    set DB_SEEDER_CONNECTION_SUFFIX="&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false&rewriteBatchedStatements=true"
    set DB_SEEDER_CONTAINER_PORT=3306
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=sys
    set DB_SEEDER_PASSWORD=percona
    set DB_SEEDER_PASSWORD_SYS=percona
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=root
    set DB_SEEDER_VERSION=5.7.14
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=postgresql
)

if ["%DB_SEEDER_DBMS%"] EQU ["postgresql"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=5432
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:postgresql://
    set DB_SEEDER_CONTAINER_PORT=5432
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=kxn_db_sys
    set DB_SEEDER_PASSWORD=postgresql
    set DB_SEEDER_PASSWORD_SYS=postgresql
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=kxn_user_sys
    set DB_SEEDER_VERSION=12.4-alpine
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_presto"] (
    set DB_SEEDER_DBMS=postgresql_presto
)

if ["%DB_SEEDER_DBMS%"] EQU ["postgresql_presto"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=5432
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:postgresql://
    set DB_SEEDER_CONTAINER_PORT=5432
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=kxn_db_sys
    set DB_SEEDER_DBMS_PRESTO=yes
    set DB_SEEDER_PASSWORD=postgresql
    set DB_SEEDER_PASSWORD_SYS=postgresql
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=kxn_user_sys
    set DB_SEEDER_VERSION=12.4-alpine
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=sqlite
)

if ["%DB_SEEDER_DBMS%"] EQU ["sqlite"] (
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:sqlite:
    set DB_SEEDER_DATABASE=.\tmp\sqlite_kxn_db
    set DB_SEEDER_DBMS_EMBEDDED=yes
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=sqlserver
)

if ["%DB_SEEDER_DBMS%"] EQU ["sqlserver"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=1433
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:sqlserver://
    set DB_SEEDER_CONTAINER_PORT=1433
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=master
    set DB_SEEDER_PASSWORD=sqlserver_2019
    set DB_SEEDER_PASSWORD_SYS=sqlserver_2019
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=sa
    set DB_SEEDER_VERSION=2019-latest
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_presto"] (
    set DB_SEEDER_DBMS=sqlserver_presto
)

if ["%DB_SEEDER_DBMS%"] EQU ["sqlserver_presto"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=1433
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:sqlserver://
    set DB_SEEDER_CONTAINER_PORT=1433
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=master
    set DB_SEEDER_DBMS_PRESTO=yes
    set DB_SEEDER_PASSWORD=sqlserver_2019
    set DB_SEEDER_PASSWORD_SYS=sqlserver_2019
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=sa
    set DB_SEEDER_VERSION=2019-latest
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=voltdb
)

if ["%DB_SEEDER_DBMS%"] EQU ["voltdb"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=21212
    set DB_SEEDER_CONNECTION_PREFIX="jdbc:voltdb://"
    set DB_SEEDER_CONNECTION_SUFFIX="?autoreconnect=true"
    set DB_SEEDER_CONTAINER_PORT=21212
    set DB_SEEDER_PASSWORD=voltdb
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_VERSION=9.2.1
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=yugabyte
)

if ["%DB_SEEDER_DBMS%"] EQU ["yugabyte"] (
    set DB_SEEDER_CONNECTION_HOST=localhost
    set DB_SEEDER_CONNECTION_PORT=5433
    set DB_SEEDER_CONNECTION_PREFIX=jdbc:postgresql://
    set DB_SEEDER_CONTAINER_PORT=5433
    set DB_SEEDER_DATABASE=kxn_db
    set DB_SEEDER_DATABASE_SYS=yugabyte
    set DB_SEEDER_PASSWORD=yugabyte
    set DB_SEEDER_SCHEMA=kxn_schema
    set DB_SEEDER_USER=kxn_user
    set DB_SEEDER_USER_SYS=yugabyte
    set DB_SEEDER_VERSION=2.2.2.0-b15
)

if ["%DB_SEEDER_DBMS_ORIG%"] EQU ["complete_client"] (
    set DB_SEEDER_DBMS=complete
)

if ["%DB_SEEDER_DBMS_PRESTO%"] EQU ["yes"] (
    set DB_SEEDER_CONNECTION_HOST_PRESTO=localhost
    set DB_SEEDER_CONNECTION_PORT_PRESTO=8080
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
echo COMPLETE_RUN                    : %DB_SEEDER_COMPLETE_RUN%
echo DBMS                            : %DB_SEEDER_DBMS%
echo DBMS_DB                         : %DB_SEEDER_DBMS_DB%
echo DBMS_DEFAULT                    : %DB_SEEDER_DBMS_DEFAULT%
echo DBMS_EMBEDDED                   : %DB_SEEDER_DBMS_EMBEDDED%
echo DBMS_PRESTO                     : %DB_SEEDER_DBMS_PRESTO%
echo DIRECTORY_CATALOG_PROPERTY      : %DB_SEEDER_DIRECTORY_CATALOG_PROPERTY%
echo FILE_CONFIGURATION_NAME         : %DB_SEEDER_FILE_CONFIGURATION_NAME%
echo FILE_JSON_NAME                  : %DB_SEEDER_FILE_JSON_NAME%
echo FILE_STATISTICS_DELIMITER       : %DB_SEEDER_FILE_STATISTICS_DELIMITER%
echo FILE_STATISTICS_HEADER          : %DB_SEEDER_FILE_STATISTICS_HEADER%
echo FILE_STATISTICS_NAME            : %DB_SEEDER_FILE_STATISTICS_NAME%
echo GLOBAL_CONNECTION_HOST          : %DB_SEEDER_GLOBAL_CONNECTION_HOST%
echo JAVA_CLASSPATH                  : %DB_SEEDER_JAVA_CLASSPATH%
echo NO_CREATE_RUNS                  : %DB_SEEDER_NO_CREATE_RUNS%
echo RELEASE                         : %DB_SEEDER_RELEASE%
echo SETUP_DBMS                      : %DB_SEEDER_SETUP_DBMS%
echo VERSION_PRESTO                  : %DB_SEEDER_VERSION_PRESTO%
echo --------------------------------------------------------------------------------
echo CONNECTION_HOST_PRESTO          : %DB_SEEDER_CONNECTION_HOST_PRESTO%
echo CONNECTION_PORT_PRESTO          : %DB_SEEDER_CONNECTION_PORT_PRESTO%
echo --------------------------------------------------------------------------------
echo CATALOG                         : %DB_SEEDER_CATALOG%
echo CATALOG_SYS                     : %DB_SEEDER_CATALOG_SYS%
echo CONNECTION_HOST                 : %DB_SEEDER_CONNECTION_HOST%
echo CONNECTION_PORT                 : %DB_SEEDER_CONNECTION_PORT%
echo CONNECTION_PREFIX               : %DB_SEEDER_CONNECTION_PREFIX%
echo CONNECTION_SERVICE              : %DB_SEEDER_CONNECTION_SERVICE%
echo CONNECTION_SUFFIX               : %DB_SEEDER_CONNECTION_SUFFIX%
echo CONTAINER_PORT                  : %DB_SEEDER_CONTAINER_PORT%
echo DATABASE                        : %DB_SEEDER_DATABASE%
echo DATABASE_SYS                    : %DB_SEEDER_DATABASE_SYS%
echo PASSWORD                        : %DB_SEEDER_PASSWORD%
echo PASSWORD_SYS                    : %DB_SEEDER_PASSWORD_SYS%
echo SCHEMA                          : %DB_SEEDER_SCHEMA%
echo USER                            : %DB_SEEDER_USER%
echo USER_SYS                        : %DB_SEEDER_USER_SYS%
echo VERSION                         : %DB_SEEDER_VERSION%
echo --------------------------------------------------------------------------------
echo:| TIME
echo ================================================================================

if ["%DB_SEEDER_DBMS%"] EQU ["complete_client"] (
    call scripts\run_db_seeder_complete_client
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
    
    goto END_OF_SCRIPT
)    

if ["%DB_SEEDER_DBMS%"] EQU ["complete_emb"] (
    call scripts\run_db_seeder_complete_emb
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
    
    goto END_OF_SCRIPT
)    

if ["%DB_SEEDER_DBMS%"] EQU ["complete_presto"] (
    call scripts\run_db_seeder_complete_presto
    if %ERRORLEVEL% NEQ 0 (
        echo Processing of the script was aborted, error code=%ERRORLEVEL%
        exit %ERRORLEVEL%
    )
    
    goto END_OF_SCRIPT
)    

if ["%DB_SEEDER_DBMS_PRESTO%"] EQU ["yes"] (
    if ["%DB_SEEDER_SETUP_DBMS%"] EQU ["yes"] (
        call scripts\run_db_seeder_presto_environment complete
        if %ERRORLEVEL% NEQ 0 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
        call scripts\run_db_seeder_setup_presto
        if %ERRORLEVEL% NEQ 0 (
            echo Processing of the script was aborted, error code=%ERRORLEVEL%
            exit %ERRORLEVEL%
        )
    )    
)    

call scripts\run_db_seeder_single %DB_SEEDER_DBMS%
if %ERRORLEVEL% NEQ 0 (
    echo Processing of the script was aborted, error code=%ERRORLEVEL%
    exit %ERRORLEVEL%
)

:END_OF_SCRIPT
echo --------------------------------------------------------------------------------
echo:| TIME
echo --------------------------------------------------------------------------------
echo End   %0
echo ================================================================================
