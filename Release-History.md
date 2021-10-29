# DBSeeder - Release History

![Travis (.com)](https://img.shields.io/travis/com/KonnexionsGmbH/db_seeder.svg?branch=master)
![GitHub release](https://img.shields.io/github/release/KonnexionsGmbH/db_seeder.svg)
![GitHub Release Date](https://img.shields.io/github/release-date/KonnexionsGmbH/db_seeder.svg)
![GitHub commits since latest release](https://img.shields.io/github/commits-since/KonnexionsGmbH/db_seeder/3.0.4.svg)

----

## Version 3.0.3

Release Date: 12.09.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 4.0.0 (e.g. from [Docker for Windows release notes](https://docs.docker.com/docker-for-windows/release-notes))
- Eclipse IDE: 2021.06 (e.g. from [Eclipse Download Page](https://www.eclipse.org/downloads))
- Gradle Build Tool: 7 (e.g. from [here](https://gradle.org/releases))
- Java Development Kit 16, (e.g. from [here](https://jdk.java.net/java-se-ri/16))
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2021-06\eclipse`

### New Features

- Automatic creation of statistics files regarding improvement with and without constraints and total overview of all releases

### Modified Features

- Updating and expanding the documentation

### Open issues

- AgensGraph: (see [here](#issues_agensgraph))
- CockroachDB: (see [here](#issues_cockroach))
- HSQLDB: (see [here](#issues_hsqldb))
- IBM Db2 Database: (see [here](#issues_ibmdb2))
- OmnisciDB: (see [here](#issues_omnisci))
- trino: (see [here](#issues_trino))
- VoltDB: (see [here](#issues_voltdb))

----

## Version 3.0.2

Release Date: 06.09.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.6.0 (e.g. from [Docker for Windows release notes](https://docs.docker.com/docker-for-windows/release-notes))
- Eclipse IDE: 2021.06 (e.g. from [Eclipse Download Page](https://www.eclipse.org/downloads))
- Gradle Build Tool: 7 (e.g. from [here](https://gradle.org/releases))
- Java Development Kit 16, (e.g. from [here](https://jdk.java.net/java-se-ri/16))
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2021-06\eclipse`

### Modified Features

- CockroachDB: DBMS v21.1.8
- Exasol: DBMS 7.1.0-d1 / JDBC 7.1.0
- IBM Informix: DBMS 14.10.FC5DE-rhm
- MariaDB Server: DBMS 10.6.4-focal / JDBC 2.7.4
- MonetDB: DBMS Jul2021
- OmniSciDB: DBMS 5.7.0 / JDBC 5.7.0
- Oracle Database: DBMS 21.3.0 / JDBC 21.1.0.0
- PostgreSQL: DBMS 13.4
- SQL Server: DBMS 2019-CU12-ubuntu-20.04 / JDBC 9.4.0.jre16
- SQLite: JDBC 3.36.0.3
- TimescaleDB: DBMS 2.4.1-pg13
- trino: DBMS 361 / JDBC 361
- VoltDB: JDBC 11.0
- YugabyteDB: DBMS 2.9.0.0-b4

### Open issues

- CockroachDB: (see [here](#issues_cockroach))
- HSQLDB: (see [here](#issues_hsqldb))
- IBM Db2 Database: (see [here](#issues_ibmdb2))
- OmnisciDB: (see [here](#issues_omnisci))
- trino: (see [here](#issues_trino))
- VoltDB: (see [here](#issues_voltdb))

----

## Version 3.0.1

Release Date: 03.08.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- Eclipse IDE: 2021.06 (e.g. from [Eclipse Download Page](https://www.eclipse.org/downloads))
- Gradle Build Tool: 7 (e.g. from [here](https://gradle.org/releases))
- Java Development Kit 15, (e.g. from [here](https://jdk.java.net/java-se-ri/15))
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2021-06\eclipse`

### Modified Features

- CUBRID: JDBC 11.0.1.0291
- Exasol: JDBC 7.0.11
- TimescaleDB: DBMS 2.4.0-pg13
- trino: DBMS 360 / JDBC 360

----

## Version 3.0.0

Release Date: 01.08.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- Eclipse IDE: 2021.06 (e.g. from [Eclipse Download Page](https://www.eclipse.org/downloads))
- Gradle Build Tool: 7 (e.g. from [here](https://gradle.org/releases))
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
- MariaDB Server: DBMS 10.6.3
- MonetDB: JDBC 3.1.jre8
- MySQL Database: DBMS 8.0.26 / JDBC 8.0.26
- Percona Server for MySQL: DBMS 8.0.25-15
- PostgreSQL: JDBC 42.2.23
- SQLite: DBMS 3.36.0 / JDBC 3.36.0.1
- trino: DBMS 359 / JDBC 359
- YugabyteDB: DBMS 2.7.2.0-b216

----

## Version 2.9.1

Release Date: 12.06.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- Eclipse IDE: 2021.03 (e.g. from [Eclipse Download Page](https://www.eclipse.org/downloads))
- Gradle Build Tool: 7 (e.g. from [here](https://gradle.org/releases))
- Java Development Kit 15, (e.g. from [here](https://jdk.java.net/java-se-ri/15))
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2021-03\eclipse`

### Modified Features

- CockroachDB: DBMS v21.1.2
- Exasol: DBMS 7.0.10
- HSQLDB embedded: big performance improvement after fixing a bug
- MonetDB: big performance improvement after introducing manual commit

### Deleted Features

- Docker Compose functionality removed

----------

## Version 2.9.0

Release Date: 04.06.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 16 (e.g.: 16.0.1 from [here](https://jdk.java.net/16))
- Gradle Build Tool: 7 (e.g.: v7.0.2 from [here](https://gradle.org/releases))
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2021-03\eclipse`

### New Features

- OmniSciDB: DBMS 5.6.1 / JDBC 5.6.0

### Modified Features

- CockroachDB: DBMS v21.1.1
- CUBRID: JDBC 11.0.1.0286
- Firebird: DBMS v4.0.0rc1
- MariaDB Server: DBMS 10.6.1
- trino: DBMS 358 / JDBC 358
- YugabyteDB: DBMS 2.7.1.1-b1

----------

## Version 2.8.2

Release Date: 28.05.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 16 (e.g.: 16.0.1 from [here](https://jdk.java.net/16))
- Gradle Build Tool: 7 (e.g.: v7.0.2 from [here](https://gradle.org/releases))
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2021-03\eclipse`

### Modified Features

- in Windows 10: replacing `grep` by `find`
- CockroachDB: DBMS v21.1.0
- CrateDB: DBMS 4.5.1
- Exasol: DBMS 7.0.8 & shutting down the database in the Docker container
- HSQLDB: JDBC 2.6.0
- MariaDB Server: DBMS 10.6.0 / JDBC 2.7.3
- MonetDB: DBMS Oct2020-SP5
- MySQL Database: DBMS 8.0.25 / JDBC 8.0.25
- Percona Server for MySQL: DBMS 8.0.23-14
- PostgreSQL: DBMS 13.3 / JDBC 42.2.20
- trino: DBMS 356 / JDBC 356
- YugabyteDB: DBMS 2.7.1.0-b131

----------

## Version 2.8.1

Release Date: 01.04.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15))
- Gradle Build Tool: 6.8.3
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2020-12\eclipse`

### Modified Features

- AgensGraph: DBMS v2.1.3
- CockroachDB: DBMS v20.2.7
- CrateDB: DBMS 4.5.0
- CUBRID: DBMS 11.0
- Exasol: DBMS 7.0.8
- Firebird: JDBC 4.0.3.java11
- HSQLDB: DBMS 2.6.0
- IBM Db2 Database: DBMS 11.5.5.1
- SQL Server: JDBC 9.2.1.jre15
- trino: DBMS 354 / JDBC 354
- YugabyteDB: DBMS 2.5.3.1-b10

----------

## Version 2.8.0

Release Date: 03.03.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15))
- Gradle Build Tool: 6.8.3
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2020-12\eclipse`

### New Features

- New DBMS: CockroachDB

### Modified Features

- Mimer SQL: DBMS v11.0.5a

----------

## Version 2.7.1

Release Date: 27.02.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2020-12\eclipse`

### Modified Features

- CrateDB: DBMS 4.4.1
- CUBRID: JDBC 11.0.0.0248
- Exasol: DBMS 7.0.7 / JDBC 7.0.7
- MariaDB Server: DBMS 10.5.9 / JDBC 2.7.2
- MonetDB: DBMS Oct2020-SP3 / JDBC 3.0.jre8
- Oracle Database: JDBC 21.1.0.0
- PostgreSQL: DBMS 13.2 / JDBC 42.2.19
- SQL Server: JDBC 9.2.0.jre15
- trino: DBMS 352 / JDBC 352
- YugabyteDB: DBMS 2.5.2.0-b104

----------

## Version 2.7.0

Release Date: 28.01.2021

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2020-12\eclipse`

### New Features

Rebranding of Presto to trino

### Modified Features

- CrateDB: DBMS 4.3.4
- Exasol: DBMS 7.0.6 / JDBC 7.0.4
- Firebird: JDBC 4.0.2.java11
- IBM Db2 Database: DBMS 11.5.5.0
- IBM Informix: DBMS 14.10.FC5DE
- Mimer SQL: JDBC 3.41a
- Oracle Database: JDBC 19.9.0.0
- SQLite: DBMS 3.34.0 / JDBC 3.34.0
- trino: DBMS 351 / JDBC 351
- YugabyteDB: DBMS 2.5.1.0-b153

----------

## Version 2.6.1

Release Date: 28.11.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### Modified Features

- CrateDB: DBMS 4.3.1
- IBM Db2 Database: JDBC 11.5.5.0
- MariaDB Server: DBMS 10.5.8 / JDBC 2.7.1
- Mimer SQL: DBMS v11.0.4b
- MySQL Database: DBMS 8.0.23 / JDBC 8.0.23
- PostgreSQL: DBMS 13.1
- Presto Distributed Query Engine: DBMS 347 / JDBC 347
- Travis CI  has been limited to the compilation functionality
- VoltDB: JDBC 10.1.1
- YugabyteDB: DBMS 2.5.0.0-b2

----------

## Version 2.6.0

Release Date: 27.10.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### New Features

- new scripts for creating a summary file from the benchmark data

### Modified Features

- CrateDB: DBMS 4.3.0
- Exasol: DBMS 7.0.3 / JDBC 7.0.3
- Firebird: DBMS 3.0.7
- MariaDB Server: DBMS 10.5.6
- MySQL Database: DBMS 8.0.22 / JDBC 8.0.22
- Oracle Database: JDBC 19.8.0.0
- PostgreSQL: JDBC 42.2.18
- Presto Distributed Query Engine: DBMS 345 / JDBC 345

----------

## Version 2.5.2

Release Date: 05.10.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### New Features

- Docker Compose functionality added

### Modified Features

- YugabyteDB: DBMS 2.3.2.0-b37

----------

## Version 2.5.1

Release Date: 29.09.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### Modified Features

- CrateDB: DBMS 4.2.4
- CUBRID: JDBC 10.2.2.8874
- Exasol: DBMS 7.0.2
- MariaDB Server: JDBC 2.7.0
- PostgreSQL: DBMS 13
- Presto Distributed Query Engine: DBMS 343 / JDBC 343

----------

## Version 2.5.0

Release Date: 30.08.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### New Features

- New DBMS: Exasol

### Modified Features

- SQL Server: JDBC 8.4.1.jre14

----------

## Version 2.4.0

Release Date: 27.08.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### New Features

- New DBMS: AgensGraph
- New DBMS: VoltDB

----------

## Version 2.3.0

Release Date: 26.08.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### New Features

- New DBMS: Percona Server for MySQL
- New DBMS: YugabyteDB

### Modified Features

- CrateDB: DBMS 4.2.3
- PostgreSQL: DBMS 12.4 / JDBC 42.2.15

----------

## Version 2.2.0

Release Date: 18.08.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### New Features

- New DBMS: MonetDB

### Modified Features

- the [original image from Presto](https://hub.docker.com/r/prestosql/presto) is now used

----------

## Version 2.1.3

Release Date: 17.08.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### Modified Features

- PostgreSQL: JDBC 42.2.15
- Presto Distributed Query Engine: use of Docker network
- minor script fixes

----------

## Version 2.1.2

Release Date: 13.08.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### Modified Features

- minor script fixes.

----------

## Version 2.1.1

Release Date: 13.08.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### New Features

- Demonstration programs for the Presto Connectors

### Modified Features

- Firebird: JDBC 4.0.1.java11
- MariaDB Server: DBMS 10.5.5
- Presto Distributed Query Engine: JDBC 340
- solved: PostgreSQL Connector: Cannot insert BLOB using Presto JDBC (see [here](https://github.com/prestosql/presto/issues/4751)).
- solved: SQL Server Connector: Login failed (see [here](https://github.com/prestosql/presto/issues/4757)).
- solved: gradle warning with [http://maven.cubrid.org (see [here](http://jira.cubrid.org/browse/CBRD-23727](http://maven.cubrid.org (see [here](http://jira.cubrid.org/browse/CBRD-23727))).

----------

## Version 2.1.0

Release Date: 11.08.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### New Features

- Adding the JDBC driver of the Presto Distributed Query Enginge (see [Issue #5](https://github.com/KonnexionsGmbH/db_seeder/issues/5)).

### Modified Features

- MariaDB Server: JDBC 2.6.2
- SQL Server: JDBC 8.4.0.jre14

----------

## Version 2.0.0

Release Date: 02.08.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### New Features

- The underlying database schema can now be freely defined using a JSON-based parameter file (Issues [37](https://github.com/KonnexionsGmbH/db_seeder/issues/37), [38](https://github.com/KonnexionsGmbH/db_seeder/issues/38), [39](https://github.com/KonnexionsGmbH/db_seeder/issues/39) and [50](https://github.com/KonnexionsGmbH/db_seeder/issues/50)).

### Modified Features

- CrateDB: DBMS 4.1.8
- SQLite: DBMS 3.32.3 / JDBC 3.32.3.2

----------

## Version 2.15.10

Release Date: 14.07.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### Modified Features

- Config.java: reduce number of properties (issue #36)
- MySQL Database: DBMS & JDBC 8.0.22

----------

## Version 2.15.8

Release Date: 13.07.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### Modified Features

- Added complete run and upload of statistics data to Travis CI (issue #42)
- Scripts restructured and extended (issue #40)
- Solved IBM Informix issue (issue #41)

----------

## Version 2.15.5

Release Date: 10.07.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

### New Features

- New batch script: `run_db_seeder_complete`: Run all DBMS variations
- New DBMS: Mimer SQL

### Modified Features

- CrateDB: DBMS 4.1.8
- Firebird: DBMS 3.0.6
- IBM Db2 Database: DBMS & JDBC 11.5.4.0
- IBM Informix: JDBC 4.50.4.1
- Oracle Database: JDBC 19.7.0.0

----------

## Version 2.14.0

Release Date: 06.07.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- New DBMS: IBM Informix

----------

## Version 2.13.0

Release Date: 01.07.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- New DBMS: H2 Database Engine

----------

## Version 2.12.0

Release Date: 29.06.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- New DBMS: H2 Database Engine

----------

## Version 2.11.2

Release Date: 26.06.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### Modified Features

- introducing encoding support for ISO-8859-1 and UTF-8
- Tested DBMS version: MariaDB Server 10.5.4

----------

## Version 2.11.0

Release Date: 26.06.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- New DBMS: Firebird

----------

## Version 2.8.3

Release Date: 21.06.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- New DBMS: Apache Derby (both client and embedded version)
- New DBMS: CUBRID

### Modified Features

- Tested DBMS version: MariaDB Server 10.5.3
- Tested DBMS version: Oracle database 12c
- Tested DBMS version: Oracle database 18c

----------

## Version 2.6.0

Release Date: 16.06.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- New DBMS: SQLite

----------

## Version 2.5.0

Release Date: 15.06.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- New DBMS: CrateDB

----------

## Version 2.4.0

Release Date: 14.06.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- New DBMS: IBM Db2 Database

----------

## Version 2.3.0

Release Date: 12.06.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- New DBMS: MariaDB Server

----------

## Version 2.2.0

Release Date: 10.06.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- New DBMS: PostgreSQL

----------

## Version 2.1.0

Release Date: 06.06.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- Documentation
- New DBMS: SQL Server
- Travis CI Integration
- Verification of the data storage

### Modified Features

- Adding BLOB and CLOB support

----------

## Version 2.0.0

Release Date: 01.06.2020

### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

### New Features

- New DBMS: MySQL Database
- New DBMS: Oracle Database

----
