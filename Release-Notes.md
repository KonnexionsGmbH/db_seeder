# DBSeeder - Release Notes

![Travis (.com)](https://img.shields.io/travis/com/KonnexionsGmbH/db_seeder.svg?branch=master)
![GitHub release](https://img.shields.io/github/release/KonnexionsGmbH/db_seeder.svg)
![GitHub Release Date](https://img.shields.io/github/release-date/KonnexionsGmbH/db_seeder.svg)
![GitHub commits since latest release](https://img.shields.io/github/commits-since/KonnexionsGmbH/db_seeder/3.0.0.svg)

----

## Version 3.0.0

Release Date: 01.08.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- Eclipse IDE: 2021.06 (e.g. from [Eclipse Download Page](https://www.eclipse.org/downloads/))
- Gradle Build Tool: 7 (e.g. from [here](https://gradle.org/releases/))
- Java Development Kit 15, (e.g. from [here](https://jdk.java.net/java-se-ri/15))
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2021-06\eclipse`

### New Features

- new control parameter `DB_SEEDER_BATCH_SIZE`: the maximum number of DML operations of type `addBatch` - `0` represents all DML operations
- new control parameter `DB_SEEDER_DROP_CONSTRAINTS`: if the value is `yes`, all constraints of the types FOREIGN KEY, PRIMARY KEY and UNIQUE KEY are removed before the first DML operation and are enabled again after the last DML operation
- TimescaleDB: DBMS 2.3.1-pg13 / JDBC PostgreSQL

### Modified Features

- CockroachDB: DBMS v21.1.6
- CrateDB: DBMS 4.6.1
- Exasol: DBMS 7.0.11
- Firebird: DBMS v4.0.0
- IBM Db2 Database: JDBC 11.5.6.0
- MariaDB Server: DBMS 10.6.3
- MonetDB: JDBC 3.1.jre8
- MySQL Database: DBMS 8.0.26 / JDBC 8.0.26
- Percona Server for MySQL: DBMS 8.0.25-15
- PostgreSQL: JDBC 42.2.23
- SQLite: DBMS 3.36.0.1 / JDBC 3.36.0.1
- trino: DBMS 359 / JDBC 359
- YugabyteDB: DBMS 2.7.2.0-b216

### Open issues

- Apache Derby: (see [here](#issues_derby))
- H2 Database Engine: (see [here](#issues_h2))
- HSQLDB: (see [here](#issues_hsqldb))
- IBM Db2 Database: (see [here](#issues_ibmdb2))
- OmnisciDB: (see [here](#issues_omnisci))
- trino: (see [here](#issues_trino))
- VoltDB: (see [here](#issues_voltdb))

----

## Windows 10 Performance Snapshot

![](.README_images/Perf_Snap_3.0.0_win10.png)

- **DBMS** - official DBMS name
- **Type** - client version, embedded version or via trino
- **ms** - total time of DDL and DML operations in milliseconds
- **Constraints** - DML operations with active or inactive constraints (foreign, primary and unique key)
- **Improvment** - improvement of total time if constraints are inactive 

----

## Detailed Open Issues

### <a name="issues_derby"></a> Apache Derby

- Issue:  dropping unique key constraints - SQL statement `ALTER TABLE COUNTRY_STATE DROP UNIQUE "SQL0000000166-6f554487-017a-f4fd-c9dc-00000016e126"` 0(see [here](https://issues.apache.org/jira/browse/DERBY-7121?orderby=created+DESC%2C+priority+DESC%2C+updated+DESC)):

### <a name="issues_h2"></a> H2 Database Engine

- Issue:  dropping unique key constraints - SQL statement `ALTER TABLE COUNTRY DROP CONSTRAINT CONSTRAINT_INDEX_6` (see [here](https://github.com/h2database/h2database/issues/3163)):


### <a name="issues_hsqldb"></a> HSQLDB

- Issue:  dropping unique key constraints - SQL statement `ALTER TABLE COUNTRY DROP CONSTRAINT SYS_IDX_SYS_PK_10289_10293` (see [here](https://sourceforge.net/p/hsqldb/bugs/1637/)):

### <a name="issues_ibmdb2"></a> IBM Db2 Database

- Issue: Docker Image from `docker pull ibmcom/db2:11.5.6.0` (see [here](https://www.tek-tips.com/viewthread.cfm?qid=1811168)):

### <a name="issues_omnisci"></a> OmniSciDB

- Issue: connection problem with existing OmnisciDB (see [here](https://github.com/omnisci/omniscidb/issues/668)).

### <a name="issues_trino"></a> trino

- Issue: all connectors: absolutely unsatisfactory performance (see [here](https://github.com/trinodb/trino/issues/5681)).
    
- Issue: Oracle connector: Oracle session not disconnected (see [here](https://github.com/trinodb/trino/issues/5648)).
    
- Issue: Oracle connector: Support Oracle's NUMBER data type (see [here](https://github.com/trinodb/trino/issues/2274)).

### <a name="issues_voltdb"></a> VoltDB

- Issue: Java 16 not yet supported: `java.lang.NullPointerException: Cannot invoke "io.netty_voltpatches.NinjaKeySet.size()" because "this.m_ninjaSelectedKeys" is null`

- Issue:  dropping primary key constraints (see [here](https://voltdb-public.slack.com/archives/C04UPPHUL/p1627566165007800)):

----------

