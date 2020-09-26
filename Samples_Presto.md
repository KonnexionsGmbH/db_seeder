# Brief operating instructions for the Presto demonstration programs 

## 1. Available Files

- `src/ch/konnexions/db_seeder/samples/presto/Sample[mysql|oracle|postgresql|sqlserver].java`
- `Samples_Presto.md` (this file)
- `scripts/samples/presto/run_sample_[mysql|oracle|postgresql|sqlserver].[bat/sh]`

## 2. Compiling

The best way to compile is with Gradle and the statement `build copyJarToLib`. 
This creates the JAR file `db_seeder.jar` in the directory `lib`.
Java must be installed at least in version 14.

## 3. Running

Prerequisite for this is an executable Presto version and the successful installation of the corresponding DBMS.

The Presto catalog properties can be generated with the script `scripts/run_db_seeder_presto_environment`.

Presto and the DBMS can be made available with the script `run_db_seeder [mysql|oracle|postgresql|sqlserver] yes 0`, whereby corresponding Docker container are created in Windows and Linux.0

The **`db_seeder`** demonstration program for Presto can then be executed with the DBMS specific script `scripts/samples/presto/run_sample_[mysql|oracle|postgresql|sqlserver]`. 

## 4. Sample run for the PostgreSQL Connector

Menu items 2 (Run the demo with the original JDBC driver) and 3 (Run the demo with the Presto JDBC driver) differ only in the selection of the driver. 
By default, 2500 rows are inserted in the following database table (PostgreSQL version):

    CREATE TABLE issue_table (
        column_pk        BIGINT         NOT NULL
                                        PRIMARY KEY,
        column_blob      BYTEA,
        column_clob      TEXT,
        column_timestamp TIMESTAMP      NOT NULL,
        column_varchar   VARCHAR(100)   NOT NULL
                                        UNIQUE
    )

The Connector proprties files are located in the directory `resources/docker/presto/catalog`.  If adjustments are necessary, the scripts `scripts/run_db_seeder_presto_environment` and `scripts/run_db_seeder_setup_presto`must be run again. 

All driver and SQL relevant events are logged at the console.

**Windows example:**

    D:\SoftDevelopment\Projects\Konnexions\db_seeder>docker start db_seeder_presto
    db_seeder_presto

    D:\SoftDevelopment\Projects\Konnexions\db_seeder>docker start db_seeder_db
    db_seeder_db

    D:\SoftDevelopment\Projects\Konnexions\db_seeder>docker ps
    CONTAINER ID        IMAGE                             COMMAND                  CREATED             STATUS              PORTS                    NAMES
    6408751738ca        postgres:12.3-alpine              "docker-entrypoint.s…"   8 hours ago         Up 5 seconds        0.0.0.0:5432->5432/tcp   db_seeder_db
    4d18bdee0064        konnexionsgmbh/db_seeder_presto   "presto-server/bin/l…"   19 hours ago        Up 20 seconds       0.0.0.0:8080->8080/tcp   db_seeder_presto

    D:\SoftDevelopment\Projects\Konnexions\db_seeder>scripts\samples\presto\run_sample_postgresql
    ================================================================================
    Start scripts\samples\presto\run_sample_postgresql
    --------------------------------------------------------------------------------
    DB Seeder - Demonstration Issues Presto PostgreSQL Connector.
    --------------------------------------------------------------------------------
    The current time is:  9:01:51.34
    Enter the new time:
    --------------------------------------------------------------------------------
    [main] Start SamplePostgresql
    [main]
    [main] ----------------------------------------------
    [main] 1 - Setup the database (includes reset)
    [main] 2 - Run the demo with the original JDBC driver
    [main] 3 - Run the demo with the Presto JDBC driver
    [main] 4 - Reset the database
    [main] 9 - Terminate the processing
    [main] ----------------------------------------------
    [main]
    [main] Please make a choice:
    1
    [main] Reset the database
    [main]
    [main] driver  ='org.postgresql.Driver'
    [main] url     ='jdbc:postgresql://localhost:5432/kxn_db_sys?user=kxn_user_sys&password=postgresql'
    [main] sqlStmnt='DROP DATABASE IF EXISTS kxn_db'
    [main] sqlStmnt='DROP USER IF EXISTS kxn_user'
    [main]
    [main] Setup the database
    [main]
    [main] driver  ='org.postgresql.Driver'
    [main] url     ='jdbc:postgresql://localhost:5432/kxn_db_sys?user=kxn_user_sys&password=postgresql'
    [main] sqlStmnt='CREATE DATABASE kxn_db'
    [main] sqlStmnt='CREATE USER kxn_user WITH ENCRYPTED PASSWORD 'postgresql''
    [main] sqlStmnt='GRANT ALL PRIVILEGES ON DATABASE kxn_db TO kxn_user'
    [main] driver  ='org.postgresql.Driver'
    [main] url     ='jdbc:postgresql://localhost:5432/kxn_db?user=kxn_user&password=postgresql'
    [main] sqlStmnt='CREATE TABLE issue_table (
        column_pk        BIGINT         NOT NULL
                                        PRIMARY KEY,
        column_blob      BYTEA,
        column_clob      TEXT,
        column_timestamp TIMESTAMP      NOT NULL,
        column_varchar   VARCHAR(100)   NOT NULL
                                        UNIQUE
    )
    '
    [main]
    [main] ----------------------------------------------
    [main] 1 - Setup the database (includes reset)
    [main] 2 - Run the demo with the original JDBC driver
    [main] 3 - Run the demo with the Presto JDBC driver
    [main] 4 - Reset the database
    [main] 9 - Terminate the processing
    [main] ----------------------------------------------
    [main]
    [main] Please make a choice:
    2
    [main] Run the demo with the original JDBC driver
    [main]
    [main] driver  ='org.postgresql.Driver'
    [main] url     ='jdbc:postgresql://localhost:5432/kxn_db?user=kxn_user&password=postgresql'
    [main]
    [main]     50 rows inserted so far
    [main]    100 rows inserted so far
    [main]    150 rows inserted so far
    [main]    200 rows inserted so far
    [main]    250 rows inserted so far
    [main]    300 rows inserted so far
    [main]    350 rows inserted so far
    [main]    400 rows inserted so far
    [main]    450 rows inserted so far
    [main]    500 rows inserted so far
    [main]    550 rows inserted so far
    [main]    600 rows inserted so far
    [main]    650 rows inserted so far
    [main]    700 rows inserted so far
    [main]    750 rows inserted so far
    [main]    800 rows inserted so far
    [main]    850 rows inserted so far
    [main]    900 rows inserted so far
    [main]    950 rows inserted so far
    [main]   1000 rows inserted so far
    [main]   1050 rows inserted so far
    [main]   1100 rows inserted so far
    [main]   1150 rows inserted so far
    [main]   1200 rows inserted so far
    [main]   1250 rows inserted so far
    [main]   1300 rows inserted so far
    [main]   1350 rows inserted so far
    [main]   1400 rows inserted so far
    [main]   1450 rows inserted so far
    [main]   1500 rows inserted so far
    [main]   1550 rows inserted so far
    [main]   1600 rows inserted so far
    [main]   1650 rows inserted so far
    [main]   1700 rows inserted so far
    [main]   1750 rows inserted so far
    [main]   1800 rows inserted so far
    [main]   1850 rows inserted so far
    [main]   1900 rows inserted so far
    [main]   1950 rows inserted so far
    [main]   2000 rows inserted so far
    [main]   2050 rows inserted so far
    [main]   2100 rows inserted so far
    [main]   2150 rows inserted so far
    [main]   2200 rows inserted so far
    [main]   2250 rows inserted so far
    [main]   2300 rows inserted so far
    [main]   2350 rows inserted so far
    [main]   2400 rows inserted so far
    [main]   2450 rows inserted so far
    [main]   2500 rows inserted so far
    [main]
    [main]   2500 rows inserted totally - duration in seconds:         13
    [main]
    [main] ----------------------------------------------
    [main] 1 - Setup the database (includes reset)
    [main] 2 - Run the demo with the original JDBC driver
    [main] 3 - Run the demo with the Presto JDBC driver
    [main] 4 - Reset the database
    [main] 9 - Terminate the processing
    [main] ----------------------------------------------
    [main]
    [main] Please make a choice:
    1
    [main] Reset the database
    [main]
    [main] driver  ='org.postgresql.Driver'
    [main] url     ='jdbc:postgresql://localhost:5432/kxn_db_sys?user=kxn_user_sys&password=postgresql'
    [main] sqlStmnt='DROP DATABASE IF EXISTS kxn_db'
    [main] sqlStmnt='DROP USER IF EXISTS kxn_user'
    [main]
    [main] Setup the database
    [main]
    [main] driver  ='org.postgresql.Driver'
    [main] url     ='jdbc:postgresql://localhost:5432/kxn_db_sys?user=kxn_user_sys&password=postgresql'
    [main] sqlStmnt='CREATE DATABASE kxn_db'
    [main] sqlStmnt='CREATE USER kxn_user WITH ENCRYPTED PASSWORD 'postgresql''
    [main] sqlStmnt='GRANT ALL PRIVILEGES ON DATABASE kxn_db TO kxn_user'
    [main] driver  ='org.postgresql.Driver'
    [main] url     ='jdbc:postgresql://localhost:5432/kxn_db?user=kxn_user&password=postgresql'
    [main] sqlStmnt='CREATE TABLE issue_table (
        column_pk        BIGINT         NOT NULL
                                        PRIMARY KEY,
        column_blob      BYTEA,
        column_clob      TEXT,
        column_timestamp TIMESTAMP      NOT NULL,
        column_varchar   VARCHAR(100)   NOT NULL
                                        UNIQUE
    )
    '
    [main]
    [main] ----------------------------------------------
    [main] 1 - Setup the database (includes reset)
    [main] 2 - Run the demo with the original JDBC driver
    [main] 3 - Run the demo with the Presto JDBC driver
    [main] 4 - Reset the database
    [main] 9 - Terminate the processing
    [main] ----------------------------------------------
    [main]
    [main] Please make a choice:
    3
    [main] Run the demo with the Presto JDBC driver
    [main]
    [main] driver  ='io.prestosql.jdbc.PrestoDriver'
    [main] url     ='jdbc:presto://localhost:8080/db_seeder_postgresql/public?user=presto'
    [main]
    [main]     50 rows inserted so far

