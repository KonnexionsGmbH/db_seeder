# DBSeeder - Release Notes

![Travis (.com)](https://img.shields.io/travis/com/KonnexionsGmbH/db_seeder.svg?branch=master)
![GitHub release](https://img.shields.io/github/release/KonnexionsGmbH/db_seeder.svg)
![GitHub Release Date](https://img.shields.io/github/release-date/KonnexionsGmbH/db_seeder.svg)
![GitHub commits since latest release](https://img.shields.io/github/commits-since/KonnexionsGmbH/db_seeder/3.0.0.svg)

----

### Table of Contents

**[1. Version History](#version_history)**<br>
**[2. Open Issues](#open_issues)**<br>
----

## <a name="version_history"></a> 1. Version History

### 3.0.0

Release Date: dd.mm.2021

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant

- Docker Desktop Community: 3.0.4
- Eclipse IDE: 2021.06 (e.g. from [Eclipse Download Page](https://www.eclipse.org/downloads/))
- Gradle Build Tool: 7 (e.g. from [here](https://gradle.org/releases/))
- Java Development Kit 15, (e.g. from [here](https://jdk.java.net/java-se-ri/15))

- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2021-06\eclipse`

#### New Features

- new control parameter `DB_SEEDER_BATCH_SIZE`: the maximum number of DML operations of type `addBatch` - `0` represents all DML operations
- new control parameter `DB_SEEDER_DROP_CONSTRAINTS`: if the value is `yes`, all constraints of the types FOREIGN KEY, PRIMARY KEY and UNIQUE KEY are removed before the first DML operation and are enabled again after the last DML operation
- TimescaleDB: DBMS 2.3.1-pg13 / JDBC PostgreSQL

#### Modified Features

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

#### Open issues

- CockroachDB: [see here](#issues_cockroach)

- CrateDB: [see here](#issues_cratedb)

- Firebird: [see here](#issues_firebird)

- H2 Database Engine: [see here](#issues_h2)

- HSQLDB: [see here](#issues_hsqldb)

- IBM Db2 Database: [see here](#issues_ibmdb2)

- OmnisciDB: [see here](#issues_omnisci)

- trino: [see here](#issues_trino)

- VoltDB: [see here](#issues_voltdb)

- YugabyteDB: [see here](#issues_yugabyte)

#### Windows 10 Performance Snapshot

----------

### 2.9.1

Release Date: 12.06.2021

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant

- Docker Desktop Community: 3.0.4
- Eclipse IDE: 2021.03 (e.g. from [Eclipse Download Page](https://www.eclipse.org/downloads/))
- Gradle Build Tool: 7 (e.g. from [here](https://gradle.org/releases/))
- Java Development Kit 15, (e.g. from [here](https://jdk.java.net/java-se-ri/15))

- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2021-03\eclipse`

#### Modified Features

- CockroachDB: DBMS v21.1.2

- Exasol: DBMS 7.0.10

- HSQLDB embedded: big performance improvement after fixing a bug

- MonetDB: big performance improvement after introducing manual commit

#### Deleted Features

- Docker Compose functionality removed

----------

### 2.9.0

Release Date: 04.06.2021

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 16 (e.g.: 16.0.1 from [here](https://jdk.java.net/16/))

- Gradle Build Tool: 7 (e.g.: v7.0.2 from [here](https://gradle.org/releases/))

- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2021-03\eclipse`

#### New Features

- OmniSciDB: DBMS 5.6.1 / JDBC 5.6.0

#### Modified Features

- CockroachDB: DBMS v21.1.1

- CUBRID: JDBC 11.0.1.0286

- Firebird: DBMS v4.0.0rc1

- MariaDB Server: DBMS 10.6.1

- trino: DBMS 358 / JDBC 358

- YugabyteDB: DBMS 2.7.1.1-b1

----------

### 2.8.2

Release Date: 28.05.2021

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 16 (e.g.: 16.0.1 from [here](https://jdk.java.net/16/))

- Gradle Build Tool: 7 (e.g.: v7.0.2 from [here](https://gradle.org/releases/))

- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2021-03\eclipse`

#### Modified Features

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

### 2.8.1

Release Date: 01.04.2021

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15/))

- Gradle Build Tool: 6.8.3
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2020-12\eclipse`

#### Modified Features

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

### 2.8.0

Release Date: 03.03.2021

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15/))

- Gradle Build Tool: 6.8.3
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2020-12\eclipse`

#### New Features

- New DBMS: CockroachDB

#### Modified Features

- Mimer SQL: DBMS v11.0.5a

----------

### 2.7.1

Release Date: 27.02.2021

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15/))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2020-12\eclipse`

#### Modified Features

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

### 2.7.0

Release Date: 28.01.2021

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15/))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse IDE, e.g.: `C:\Software\eclipse\java-2020-12\eclipse`

#### New Features

Rebranding of Presto to trino

#### Modified Features

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

### 2.6.1

Release Date: 28.11.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15/))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Exasol

    - JDBC Driver: java.sql.SQLException: Invalid character in connection string (see [here](https://community.exasol.com/t5/discussion-forum/jdbc-driver-java-sql-sqlexception-invalid-character-in/td-p/2224)).
    - Ubuntu 20.10: com.exasol.jdbc.ConnectFailed: Connection reset (see [here](https://community.exasol.com/t5/discussion-forum/ubuntu-20-10-com-exasol-jdbc-connectfailed-connection-reset/td-p/2362))

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Oracle Database

    - Database 19c: ORA-12637: Packet receive failed (see [here](https://github.com/KonnexionsGmbH/db_seeder/issues/87)).

- Presto Distributed Query Engine

    - All Connectors: Absolutely unsatisfactory performance (see [here](https://github.com/prestosql/presto/issues/5681)).
    - Oracle Connector: Oracle session not disconnected (see [here](https://github.com/prestosql/presto/issues/5648)).
    - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).

- YugabyteDB

    - Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

#### Modified Features

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

### 2.6.0

Release Date: 27.10.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15/))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Exasol

    - JDBC Driver: java.sql.SQLException: Invalid character in connection string (see [here](https://community.exasol.com/t5/discussion-forum/jdbc-driver-java-sql-sqlexception-invalid-character-in/td-p/2224)).
    - Ubuntu 20.10: com.exasol.jdbc.ConnectFailed: Connection reset (see [here](https://community.exasol.com/t5/discussion-forum/ubuntu-20-10-com-exasol-jdbc-connectfailed-connection-reset/td-p/2362))

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Oracle Database

    - Database 19c: ORA-12637: Packet receive failed (see [here](https://github.com/KonnexionsGmbH/db_seeder/issues/87)).

- Presto Distributed Query Engine

    - All Connectors: Absolutely unsatisfactory performance (see [here](https://github.com/prestosql/presto/issues/5681)).
    - Oracle Connector: Oracle session not disconnected (see [here](https://github.com/prestosql/presto/issues/5684)).
    - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).

- YugabyteDB

    - Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

#### New Features

- new scripts for creating a summary file from the benchmark data

#### Modified Features

- CrateDB: DBMS 4.3.0

- Exasol: DBMS 7.0.3 / JDBC 7.0.3

- Firebird: DBMS 3.0.7

- MariaDB Server: DBMS 10.5.6

- MySQL Database: DBMS 8.0.22 / JDBC 8.0.22

- Oracle Database: JDBC 19.8.0.0

- PostgreSQL: JDBC 42.2.18

- Presto Distributed Query Engine: DBMS 345 / JDBC 345

----------

### 2.5.2

Release Date: 05.10.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15/))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Exasol

    - JDBC Driver: java.sql.SQLException: Invalid character in connection string (see [here](https://community.exasol.com/t5/discussion-forum/jdbc-driver-java-sql-sqlexception-invalid-character-in/td-p/2224)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Oracle Database

    - Database 19c: ORA-12637: Packet receive failed (see [here](https://github.com/KonnexionsGmbH/db_seeder/issues/87)).

- Presto Distributed Query Engine

    - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
    - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

- YugabyteDB

    - Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

#### New Features

- Docker Compose functionality added

#### Modified Features

- YugabyteDB: DBMS 2.3.2.0-b37

----------

### 2.5.1

Release Date: 29.09.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 15 (e.g.: from [here](https://jdk.java.net/15/))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Oracle Database

    - Database 19c: ORA-12637: Packet receive failed (see [here](https://github.com/KonnexionsGmbH/db_seeder/issues/87)).

- Presto Distributed Query Engine

    - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
    - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

- YugabyteDB

    - Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

#### Modified Features

- CrateDB: DBMS 4.2.4

- CUBRID: JDBC 10.2.2.8874

- Exasol: DBMS 7.0.2

- MariaDB Server: JDBC 2.7.0

- PostgreSQL: DBMS 13

- Presto Distributed Query Engine: DBMS 343
- Presto Distributed Query Engine: JDBC 343

----------

### 2.5.0

Release Date: 30.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

    - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
    - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

- YugabyteDB

    - Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

#### New Features

- New DBMS: Exasol

#### Modified Features

- SQL Server: JDBC 8.4.1.jre14

----------

### 2.4.0

Release Date: 27.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

    - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/trinodb/presto/issues/4764)).
    - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

- YugabyteDB

    - Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

#### New Features

- New DBMS: AgensGraph
- New DBMS: VoltDB

----------

### 2.3.0

Release Date: 26.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

    - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
    - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

- YugabyteDB

    - Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

#### New Features

- New DBMS: Percona Server for MySQL
- New DBMS: YugabyteDB

#### Modified Features

- CrateDB: DBMS 4.2.3

- PostgreSQL: DBMS 12.4 / JDBC 42.2.15

----------

### 2.2.0

Release Date: 18.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- MonetDB

    - Exception in thread "main" java.sql.BatchUpdateException.

- Presto Distributed Query Engine

    - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
    - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

#### New Features

- New DBMS: MonetDB

#### Modified Features

- the [original image from Presto](https://hub.docker.com/r/prestosql/presto) is now used

----------

### 2.1.3

Release Date: 17.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

    - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
    - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

#### Modified Features

- PostgreSQL: JDBC 42.2.15

- Presto Distributed Query Engine: use of Docker network

- minor script fixes

----------

### 2.1.2

Release Date: 13.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

    - MySQL Connector: Connection failure (see [here](https://github.com/prestosql/presto/issues/4812)).
    - PostgreSQL Connector: Connection failure (see [here](https://github.com/prestosql/presto/issues/4813)).
    - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

#### Modified Features

- minor script fixes.

----------

### 2.1.1

Release Date: 13.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

    - MySQL Connector: JDBC INSERT limited to about 100 rows - observed 98 to 115 (see [here](https://github.com/prestosql/presto/issues/4732)).
    - SQL Server Connector: Issues with BLOB and CLOB (see [here](https://github.com/prestosql/presto/issues/4757)).

#### New Features

- Demonstration programs for the Presto Connectors

#### Modified Features

- Firebird: JDBC 4.0.1.java11

- MariaDB Server: DBMS 10.5.5

- Presto Distributed Query Engine: JDBC 340
- solved: PostgreSQL Connector: Cannot insert BLOB using Presto JDBC (see [here](https://github.com/prestosql/presto/issues/4751)).
- solved: SQL Server Connector: Login failed (see [here](https://github.com/prestosql/presto/issues/4757)).

- solved: gradle warning with [http://maven.cubrid.org (see [here](http://jira.cubrid.org/browse/CBRD-23727](http://maven.cubrid.org (see [here](http://jira.cubrid.org/browse/CBRD-23727))).

----------

### 2.1.0

Release Date: 11.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- CUBRID

    - gradle warning with [http://maven.cubrid.org (see [here](http://jira.cubrid.org/browse/CBRD-23727](http://maven.cubrid.org (see [here](http://jira.cubrid.org/browse/CBRD-23727))).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

    - MySQL Connector: JDBC INSERT limited to about 100 rows - observed 98 to 115 (see [here](https://github.com/prestosql/presto/issues/4732)).
    - PostgreSQL Connector: Cannot insert BLOB using Presto JDBC (see [here](https://github.com/prestosql/presto/issues/4751)).
    - SQL Server Connector: Login failed (see [here](https://github.com/prestosql/presto/issues/4757)).

#### New Features

- Adding the JDBC driver of the Presto Distributed Query Enginge (see [Issue #5](https://github.com/KonnexionsGmbH/db_seeder/issues/5)).

#### Modified Features

- MariaDB Server: JDBC 2.6.2

- SQL Server: JDBC 8.4.0.jre14

----------

### 2.0.0

Release Date: 02.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

#### New Features

- The underlying database schema can now be freely defined using a JSON-based parameter file (Issues [37](https://github.com/KonnexionsGmbH/db_seeder/issues/37), [38](https://github.com/KonnexionsGmbH/db_seeder/issues/38), [39](https://github.com/KonnexionsGmbH/db_seeder/issues/39) and [50](https://github.com/KonnexionsGmbH/db_seeder/issues/50)).

#### Modified Features

- CrateDB: DBMS 4.1.8

- SQLite: DBMS 3.32.3 / JDBC 3.32.3.2

----------

### 2.15.10

Release Date: 14.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

#### Modified Features

- Config.java: reduce number of properties (issue #36)

- MySQL Database: DBMS & JDBC 8.0.22

----------

### 2.15.8

Release Date: 13.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

#### Modified Features

- Added complete run and upload of statistics data to Travis CI (issue #42)

- Scripts restructured and extended (issue #40)

- Solved IBM Informix issue (issue #41)

----------

### 2.15.5

Release Date: 10.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))
- grep utility, e.g. for Windows from [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- IBM Informix

    - Sporadically during Informix startup: "shared memory not initialized for INFORMIXSERVER 'informix'" (see [here](https://community.ibm.com/community/user/hybriddatamanagement/communities/community-home/digestviewer/viewthread?GroupId=4147&MessageKey=65106f54-ae71-4c3f-afec-92ce84587989&CommunityKey=cf5a1f39-c21f-4bc4-9ec2-7ca108f0a365&tab=digestviewer&ReturnUrl=%2fcommunity%2fuser%2fhybriddatamanagement%2fcommunities%2fcommunity-home%2fdigestviewer%3fcommunitykey%3dcf5a1f39-c21f-4bc4-9ec2-7ca108f0a365%26tab%3ddigestviewer%26SuccessMsg%3dThank%2byou%2bfor%2bcontributing%2bto%2bthe%2bIBM%2bCommunity.%2bYour%2bmessage%2bis%2bin%2bqueue%2band%2bwill%2bbe%2breviewed%2bsoon.)).

- Mimer SQL & DBeaver

    - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

#### New Features

- New batch script: `run_db_seeder_complete`: Run all DBMS variations

- New DBMS: Mimer SQL

#### Modified Features

- CrateDB: DBMS 4.1.8

- Firebird: DBMS 3.0.6

- IBM Db2 Database: DBMS & JDBC 11.5.4.0

- IBM Informix: JDBC 4.50.4.1

- Oracle Database: JDBC 19.7.0.0

----------

### 2.14.0

Release Date: 06.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### New Features

- New DBMS: IBM Informix

----------

### 2.13.0

Release Date: 01.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### New Features

- New DBMS: H2 Database Engine

----------

### 2.12.0

Release Date: 29.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### New Features

- New DBMS: H2 Database Engine

----------

### 2.11.2

Release Date: 26.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### Modified Features

- introducing encoding support for ISO-8859-1 and UTF-8

- Tested DBMS version: MariaDB Server 10.5.4

----------

### 2.11.0

Release Date: 26.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL

    - Previewing BLOB columns with DBeaver gives the error message "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203))

#### New Features

- New DBMS: Firebird

----------

### 2.8.3

Release Date: 21.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### Current Issues

- Apache Derby

    - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- CUBRID

    - It is not possible to construct a valid URL for JDBC (see [here](http://jira.cubrid.org/browse/CBRD-23695)).

#### New Features

- New DBMS: Apache Derby (both client and embedded version)
- New DBMS: CUBRID

#### Modified Features

- Tested DBMS version: MariaDB Server 10.5.3

- Tested DBMS version: Oracle database 12c
- Tested DBMS version: Oracle database 18c

----------

### 2.6.0

Release Date: 16.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### New Features

- New DBMS: SQLite

----------

### 2.5.0

Release Date: 15.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### New Features

- New DBMS: CrateDB

----------

### 2.4.0

Release Date: 14.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### New Features

- New DBMS: IBM Db2 Database

----------

### 2.3.0

Release Date: 12.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### New Features

- New DBMS: MariaDB Server

----------

### 2.2.0

Release Date: 10.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### New Features

- New DBMS: PostgreSQL

----------

### 2.1.0

Release Date: 06.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### New Features

- Documentation

- New DBMS: SQL Server

- Travis CI Integration

- Verification of the data storage

#### Modified Features

- Adding BLOB and CLOB support

----------

### 2.0.0

Release Date: 01.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- open-source JDK Version 14 (e.g.: from [here](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot))

#### New Features

- New DBMS: MySQL Database
- New DBMS: Oracle Database

----

## <a name="open_issues"></a> 2. Open Issues

### <a name="issues_cockroach"></a> CockroachDB

- Issue: dropping unique key constraints - SQL statement `DROP INDEX "country_name_key" CASCADE`:

`2021-07-23 09:28:10,007 [DatabaseSeeder.java] INFO  Start CockroachDB
org.postgresql.util.PSQLException: ERROR: requested table does not have a primary key
    at org.postgresql.core.v3.QueryExecutorImpl.receiveErrorResponse(QueryExecutorImpl.java:2434)
    at org.postgresql.core.v3.QueryExecutorImpl.processResults(QueryExecutorImpl.java:2179)
    at org.postgresql.core.v3.QueryExecutorImpl.execute(QueryExecutorImpl.java:307)
    at org.postgresql.jdbc.PgStatement.executeInternal(PgStatement.java:441)
    at org.postgresql.jdbc.PgStatement.execute(PgStatement.java:365)
    at org.postgresql.jdbc.PgStatement.executeWithFlags(PgStatement.java:307)
    at org.postgresql.jdbc.PgStatement.executeCachedSql(PgStatement.java:293)
    at org.postgresql.jdbc.PgStatement.executeWithFlags(PgStatement.java:270)
    at org.postgresql.jdbc.PgStatement.execute(PgStatement.java:266)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeDdlStmnts(AbstractJdbcSeeder.java:1260)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:1174)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:415)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:94)
Processing of the script was aborted, error code=1`

### <a name="issues_cratedb"></a> CrateDB

- Issue: dropping constraints - SQL statement `ALTER TABLE COUNTRY DROP CONSTRAINT country_pk`:

Deleting constraints seems to be very rudimentary, see [here](https://crate.io/docs/crate/reference/en/4.5/sql/statements/drop-constraint.html)

`2021-07-23 10:14:44,871 [DatabaseSeeder.java] INFO  Start CrateDB
io.crate.shade.org.postgresql.util.PSQLException: ERROR: Cannot find a CHECK CONSTRAINT named [country_pk], available constraints are: []
    at io.crate.shade.org.postgresql.core.v3.QueryExecutorImpl.receiveErrorResponse(QueryExecutorImpl.java:2440)
    at io.crate.shade.org.postgresql.core.v3.QueryExecutorImpl.processResults(QueryExecutorImpl.java:2183)
    at io.crate.shade.org.postgresql.core.v3.QueryExecutorImpl.execute(QueryExecutorImpl.java:308)
    at io.crate.shade.org.postgresql.jdbc.PgStatement.executeInternal(PgStatement.java:442)
    at io.crate.shade.org.postgresql.jdbc.PgStatement.execute(PgStatement.java:366)
    at io.crate.shade.org.postgresql.jdbc.PgStatement.executeWithFlags(PgStatement.java:308)
    at io.crate.shade.org.postgresql.jdbc.PgStatement.executeCachedSql(PgStatement.java:294)
    at io.crate.shade.org.postgresql.jdbc.PgStatement.executeWithFlags(PgStatement.java:271)
    at io.crate.shade.org.postgresql.jdbc.PgStatement.execute(PgStatement.java:267)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeDdlStmnts(AbstractJdbcSeeder.java:1260)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:1174)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:415)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:100)
Processing of the script was aborted, error code=1`

### <a name="issues_firebird"></a> Firebird

- Issue: incomplete index delivery with `getIndexInfo`, DDL statement:

`CREATE TABLE COUNTRY (
    PK_COUNTRY_ID INTEGER NOT NULL,
    COUNTRY_MAP BLOB SUB_TYPE BINARY,
    CREATED TIMESTAMP NOT NULL,
    ISO3166 VARCHAR(50),
    MODIFIED TIMESTAMP,
    NAME VARCHAR(100) NOT NULL,
    CONSTRAINT INTEG_101 UNIQUE (NAME)
);
CREATE UNIQUE INDEX RDB$38 ON COUNTRY (NAME);`

`2021-07-23 13:06:16,610 [DatabaseSeeder.java] INFO  Start Firebird [client]
java.sql.SQLSyntaxErrorException: unsuccessful metadata update; ALTER TABLE COUNTRY failed; CONSTRAINT RDB$38 does not exist. [SQLState:42000, ISC error code:335544351]
    at org.firebirdsql.gds.ng.FbExceptionBuilder$Type$1.createSQLException(FbExceptionBuilder.java:534)
    at org.firebirdsql.gds.ng.FbExceptionBuilder.toFlatSQLException(FbExceptionBuilder.java:304)
    at org.firebirdsql.gds.ng.wire.AbstractWireOperations.readStatusVector(AbstractWireOperations.java:140)
    at org.firebirdsql.gds.ng.wire.AbstractWireOperations.processOperation(AbstractWireOperations.java:204)
    at org.firebirdsql.gds.ng.wire.AbstractWireOperations.readSingleResponse(AbstractWireOperations.java:171)
    at org.firebirdsql.gds.ng.wire.AbstractWireOperations.readResponse(AbstractWireOperations.java:155)
    at org.firebirdsql.gds.ng.wire.AbstractFbWireDatabase.readResponse(AbstractFbWireDatabase.java:211)
    at org.firebirdsql.gds.ng.wire.version10.V10Statement.execute(V10Statement.java:329)
    at org.firebirdsql.jdbc.FBStatement.internalExecute(FBStatement.java:869)
    at org.firebirdsql.jdbc.FBStatement.executeImpl(FBStatement.java:496)
    at org.firebirdsql.jdbc.FBStatement.execute(FBStatement.java:482)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeDdlStmnts(AbstractJdbcSeeder.java:1259)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:1173)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:415)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:131)
Caused by: org.firebirdsql.jdbc.FBSQLExceptionInfo: unsuccessful metadata update
Processing of the script was aborted, error code=1`

### <a name="issues_h2"></a> H2 Database Engine

- Issue: `DROP CONSTRAINT`, DDL statement:

`CREATE TABLE KXN_SCHEMA.COUNTRY (
    PK_COUNTRY_ID BIGINT NOT NULL,
    COUNTRY_MAP BLOB,
    CREATED TIMESTAMP NOT NULL,
    ISO3166 VARCHAR(50),
    MODIFIED TIMESTAMP,
    NAME VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX CONSTRAINT_INDEX_6 ON KXN_SCHEMA.COUNTRY (NAME);
`

`2021-07-23 13:16:56,340 [DatabaseSeeder.java] INFO  Start H2 Database Engine [client]
org.h2.jdbc.JdbcSQLSyntaxErrorException: Constraint "CONSTRAINT_INDEX_6" not found; SQL statement:
ALTER TABLE COUNTRY DROP CONSTRAINT CONSTRAINT_INDEX_6 [90057-200]
    at org.h2.message.DbException.getJdbcSQLException(DbException.java:576)
    at org.h2.message.DbException.getJdbcSQLException(DbException.java:429)
    at org.h2.message.DbException.get(DbException.java:205)
    at org.h2.message.DbException.get(DbException.java:181)
    at org.h2.command.ddl.AlterTableDropConstraint.update(AlterTableDropConstraint.java:41)
    at org.h2.command.CommandContainer.update(CommandContainer.java:198)
    at org.h2.command.Command.executeUpdate(Command.java:251)
    at org.h2.server.TcpServerThread.process(TcpServerThread.java:406)
    at org.h2.server.TcpServerThread.run(TcpServerThread.java:183)
    at java.base/java.lang.Thread.run(Thread.java:832)
    at org.h2.message.DbException.getJdbcSQLException(DbException.java:576)
    at org.h2.engine.SessionRemote.done(SessionRemote.java:611)
    at org.h2.command.CommandRemote.executeUpdate(CommandRemote.java:237)
    at org.h2.jdbc.JdbcStatement.executeInternal(JdbcStatement.java:228)
    at org.h2.jdbc.JdbcStatement.execute(JdbcStatement.java:201)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeDdlStmnts(AbstractJdbcSeeder.java:1259)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:1173)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:415)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:138)
Processing of the script was aborted, error code=1`

### <a name="issues_hsqldb"></a> HSQLDB

- Issue: `DROP CONSTRAINT` privilege with SQL statement `ALTER TABLE COUNTRY DROP CONSTRAINT SYS_IDX_SYS_PK_10095_10099`, DDL statement:

`CREATE TABLE PUBLIC.KXN_SCHEMA.COUNTRY (
    PK_COUNTRY_ID BIGINT NOT NULL,
    COUNTRY_MAP BLOB,
    CREATED TIMESTAMP NOT NULL,
    ISO3166 VARCHAR(50),
    MODIFIED TIMESTAMP,
    NAME VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX SYS_IDX_SYS_CT_10098_10103 ON PUBLIC.KXN_SCHEMA.COUNTRY (NAME);
`

`2021-07-23 13:40:37,554 [DatabaseSeeder.java] INFO  Start HSQLDB [client]
java.sql.SQLSyntaxErrorException: user lacks privilege or object not found: SYS_IDX_SYS_PK_10095_10099
    at org.hsqldb.jdbc.JDBCUtil.sqlException(Unknown Source)
    at org.hsqldb.jdbc.JDBCUtil.sqlException(Unknown Source)
    at org.hsqldb.jdbc.JDBCStatement.fetchResult(Unknown Source)
    at org.hsqldb.jdbc.JDBCStatement.execute(Unknown Source)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeDdlStmnts(AbstractJdbcSeeder.java:1261)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:1175)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:415)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:152)
    Caused by: org.hsqldb.HsqlException: user lacks privilege or object not found: SYS_IDX_SYS_PK_10095_10099
    at org.hsqldb.error.Error.error(Unknown Source)
    at org.hsqldb.result.Result.getException(Unknown Source)
... 7 more
Processing of the script was aborted, error code=1`

### <a name="issues_ibmdb2"></a> IBM Db2 Database

- Issue: version 11.5.6.0:

`2021-07-03 05:31:17,235 [DatabaseSeeder.java] INFO  Start
2021-07-03 05:31:17,237 [DatabaseSeeder.java] INFO  tickerSymbolExtern='ibmdb2'
2021-07-03 05:31:17,237 [DatabaseSeeder.java] INFO  Start IBM Db2 Database
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/D:/SoftDevelopment/Projects/db_seeder/lib/db_seeder.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/D:/SoftDevelopment/Projects/db_seeder/lib/jdbc-yugabytedb-42.2.7-yb-3.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]
com.ibm.db2.jcc.am.DisconnectNonTransientConnectionException: [jcc][t4][2030][11211][4.29.24] A communication error occurred during operations on the connection's underlying socket, socket input stream,
or socket output stream.  Error location: Reply.fill() - insufficient data (-1).  Message: Insufficient data. ERRORCODE=-4499, SQLSTATE=08001
    at com.ibm.db2.jcc.am.b7.a(b7.java:338)
    at com.ibm.db2.jcc.t4.a.a(a.java:573)
    at com.ibm.db2.jcc.t4.a.a(a.java:557)
    at com.ibm.db2.jcc.t4.a.a(a.java:552)
    at com.ibm.db2.jcc.t4.y.b(y.java:315)
    at com.ibm.db2.jcc.t4.y.c(y.java:342)
    at com.ibm.db2.jcc.t4.y.c(y.java:455)
    at com.ibm.db2.jcc.t4.y.v(y.java:1230)
    at com.ibm.db2.jcc.t4.z.a(z.java:53)
    at com.ibm.db2.jcc.t4.b.c(b.java:1482)
    at com.ibm.db2.jcc.t4.b.b(b.java:1354)
    at com.ibm.db2.jcc.t4.b.b(b.java:889)
    at com.ibm.db2.jcc.t4.b.a(b.java:860)
    at com.ibm.db2.jcc.t4.b.a(b.java:455)
    at com.ibm.db2.jcc.t4.b.a(b.java:428)
    at com.ibm.db2.jcc.t4.b.<init>(b.java:366)
    at com.ibm.db2.jcc.DB2SimpleDataSource.getConnection(DB2SimpleDataSource.java:243)
    at com.ibm.db2.jcc.DB2SimpleDataSource.getConnection(DB2SimpleDataSource.java:200)
    at com.ibm.db2.jcc.DB2Driver.connect(DB2Driver.java:491)
    at com.ibm.db2.jcc.DB2Driver.connect(DB2Driver.java:117)
    at java.sql/java.sql.DriverManager.getConnection(DriverManager.java:677)
    at java.sql/java.sql.DriverManager.getConnection(DriverManager.java:251)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.connect(AbstractJdbcSeeder.java:264)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.connect(AbstractJdbcSeeder.java:155)
    at ch.konnexions.db_seeder.jdbc.ibmdb2.Ibmdb2Seeder.setupDatabase(Ibmdb2Seeder.java:104)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:348)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:142)
Processing of the script was aborted, error code=1`

### <a name="issues_omnisci"></a> OmniSciDB

- Issue: user can not be dropped (problem with second DBSeeder run) (see [here](https://github.com/trinodb/trino/issues/5681)).

`2021-07-26 09:32:44,326 [DatabaseSeeder.java] INFO  Start OmniSciDB
java.sql.SQLException: Query failed : [OmniSci.java:read:43901:TOmniSciException(error_msg:Exception: User kxn_user already exists.)]
    at com.omnisci.jdbc.OmniSciStatement.executeQuery(OmniSciStatement.java:100)
    at com.omnisci.jdbc.OmniSciStatement.execute(OmniSciStatement.java:238)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeSQLStmnts(AbstractJdbcSeeder.java:1332)
    at ch.konnexions.db_seeder.jdbc.omnisci.OmnisciSeeder.setupDatabase(OmnisciSeeder.java:122)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:407)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:215)
Processing of the script was aborted, error code=1`

### <a name="issues_trino"></a> trino

- Issue: all connectors: absolutely unsatisfactory performance (see [here](https://github.com/trinodb/trino/issues/5681)).
    
- Issue: Oracle connector: Oracle session not disconnected (see [here](https://github.com/trinodb/trino/issues/5648)).

`2021-01-14 17:44:35,322 [DatabaseSeeder.java] INFO  Start
2021-01-14 17:44:35,328 [DatabaseSeeder.java] INFO  tickerSymbolExtern='oracle_trino'
2021-01-14 17:44:35,328 [DatabaseSeeder.java] INFO  Start Oracle Database via trino
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/D:/SoftDevelopment/Projects/db_seeder/lib/db_seeder.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/D:/SoftDevelopment/Projects/db_seeder/lib/jdbc-yugabytedb-42.2.7-yb-3.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
java.sql.SQLSyntaxErrorException: ORA-01940: Ein Benutzer, der gerade mit der DB verbunden ist, kann nicht gelöscht werden
    at oracle.jdbc.driver.T4CTTIoer11.processError(T4CTTIoer11.java:509)
    at oracle.jdbc.driver.T4CTTIoer11.processError(T4CTTIoer11.java:461)
    at oracle.jdbc.driver.T4C8Oall.processError(T4C8Oall.java:1104)
    at oracle.jdbc.driver.T4CTTIfun.receive(T4CTTIfun.java:553)
    at oracle.jdbc.driver.T4CTTIfun.doRPC(T4CTTIfun.java:269)
    at oracle.jdbc.driver.T4C8Oall.doOALL(T4C8Oall.java:655)
    at oracle.jdbc.driver.T4CStatement.doOall8(T4CStatement.java:229)
    at oracle.jdbc.driver.T4CStatement.doOall8(T4CStatement.java:41)
    at oracle.jdbc.driver.T4CStatement.executeForRows(T4CStatement.java:928)
    at oracle.jdbc.driver.OracleStatement.doExecuteWithTimeout(OracleStatement.java:1205)
    at oracle.jdbc.driver.OracleStatement.executeInternal(OracleStatement.java:1823)
    at oracle.jdbc.driver.OracleStatement.execute(OracleStatement.java:1778)
    at oracle.jdbc.driver.OracleStatementWrapper.execute(OracleStatementWrapper.java:303)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropUser(AbstractJdbcSeeder.java:795)
    at ch.konnexions.db_seeder.jdbc.oracle.OracleSeeder.setupDatabase(OracleSeeder.java:133)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:328)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:181)
Caused by: Error : 1940, Position : 0, Sql = DROP USER  KXN_USER CASCADE, OriginalSql = DROP USER  KXN_USER CASCADE, Error Msg = ORA-01940: Ein
 Benutzer, der gerade mit der DB verbunden ist, kann nicht gelöscht werden
    at oracle.jdbc.driver.T4CTTIoer11.processError(T4CTTIoer11.java:513)
        ... 16 more
Processing of the script was aborted, error code=1`
    
- Issue: Oracle connector: Support Oracle's NUMBER data type (see [here](https://github.com/trinodb/trino/issues/2274)).

- Issue: DatabaseMetaData support incomplete (see [here](https://github.com/trinodb/trino/issues/xxxx)).

`2021-07-25 15:01:28,399 [DatabaseSeeder.java] INFO  Start MySQL Database via trino
java.sql.SQLFeatureNotSupportedException: imported keys not supported
    at io.trino.jdbc.TrinoDatabaseMetaData.getImportedKeys(TrinoDatabaseMetaData.java:1066)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:976)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:417)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:202)
Processing of the script was aborted, error code=1`

### <a name="issues_voltdb"></a> VoltDB

- Issue: Java 16 not yet supported

`2021-07-26 05:41:19,299 [DatabaseSeeder.java] INFO  Start VoltDB
java.lang.NullPointerException: Cannot invoke "io.netty_voltpatches.NinjaKeySet.size()" because "this.m_ninjaSelectedKeys" is null
    at org.voltcore.network.VoltNetwork.optimizedInvokeCallbacks(VoltNetwork.java:478)
    at org.voltcore.network.VoltNetwork.run(VoltNetwork.java:329)
    at java.base/java.lang.Thread.run(Thread.java:831)
    Juli 26, 2021 5:41:19 AM org.voltcore.logging.VoltUtilLoggingLogger log
    SEVERE: NULL : Throwable: java.lang.NullPointerException: Cannot invoke "io.netty_voltpatches.NinjaKeySet.size()" because "this.m_ninjaSelectedKeys" is null
    java.lang.NullPointerException: Cannot invoke "io.netty_voltpatches.NinjaKeySet.size()" because "this.m_ninjaSelectedKeys" is null
    at org.voltcore.network.VoltNetwork.optimizedInvokeCallbacks(VoltNetwork.java:478)
    at org.voltcore.network.VoltNetwork.run(VoltNetwork.java:329)
    at java.base/java.lang.Thread.run(Thread.java:831)
    Juli 26, 2021 5:41:19 AM org.voltcore.logging.VoltUtilLoggingLogger log
    SEVERE: NULL : Throwable: java.lang.NullPointerException: Cannot invoke "io.netty_voltpatches.NinjaKeySet.size()" because "this.m_ninjaSelectedKeys" is null
    Juli 26, 2021 5:43:20 AM org.voltcore.logging.VoltUtilLoggingLogger log
    WARNING: Connection to VoltDB node at: localhost:21212 was lost.
    java.sql.SQLException: Connection closed (CONNECTION_LOST): 'Connection to database host (localhost/127.0.0.1:21212) was lost before a response was received'
    at org.voltdb.jdbc.SQLError.get(SQLError.java:60)
    at org.voltdb.jdbc.JDBC4Statement$VoltSQL.execute(JDBC4Statement.java:133)
    at org.voltdb.jdbc.JDBC4Statement.execute(JDBC4Statement.java:376)
    at org.voltdb.jdbc.JDBC4Statement.execute(JDBC4Statement.java:387)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeSQLStmnts(AbstractJdbcSeeder.java:1319)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropAllTablesIfExists(AbstractJdbcSeeder.java:784)
    at ch.konnexions.db_seeder.jdbc.voltdb.VoltdbSeeder.setupDatabase(VoltdbSeeder.java:105)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:407)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:269)
    Caused by: org.voltdb.client.ProcCallException: Connection to database host (localhost/127.0.0.1:21212) was lost before a response was received
    at org.voltdb.client.ClientImpl.internalSyncCallProcedure(ClientImpl.java:461)
    at org.voltdb.client.ClientImpl.callProcedureWithClientTimeoutImpl(ClientImpl.java:311)
    at org.voltdb.client.ClientImpl.callProcedureWithClientTimeout(ClientImpl.java:285)
    at org.voltdb.jdbc.JDBC4ClientConnection.execute(JDBC4ClientConnection.java:351)
    at org.voltdb.jdbc.JDBC4Statement$VoltSQL.execute(JDBC4Statement.java:122)
    ... 7 more
Processing of the script was aborted, error code=1`

- Statement `ALTER TABLE COUNTRY DROP CONSTRAINT VOLTDB_AUTOGEN_CT__PK_COUNTRY_PK_COUNTRY_ID` 

`2021-07-26 05:33:06,496 [DatabaseSeeder.java] INFO  Start VoltDB
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by io.netty_voltpatches.NinjaKeySet (file:/D:/SoftDevelopment/Projects/db_seeder/lib/voltdbclient-10.1.1.jar) to field sun.nio.ch.SelectorImpl.selectedKeys
WARNING: Please consider reporting this to the maintainers of io.netty_voltpatches.NinjaKeySet
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
java.sql.SQLException: General Provider Error (GRACEFUL_FAILURE): '[Ad Hoc DDL Input:1]: DDL Error: "object not found: VOLTDB_AUTOGEN_CT__PK_COUNTRY_PK_COUNTRY_ID"'
    at org.voltdb.jdbc.SQLError.get(SQLError.java:60)
    at org.voltdb.jdbc.JDBC4Statement$VoltSQL.execute(JDBC4Statement.java:143)
    at org.voltdb.jdbc.JDBC4Statement.execute(JDBC4Statement.java:376)
    at org.voltdb.jdbc.JDBC4Statement.execute(JDBC4Statement.java:387)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeSQLStmnts(AbstractJdbcSeeder.java:1319)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:1216)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:417)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:269)
    Caused by: org.voltdb.client.ProcCallException: [Ad Hoc DDL Input:1]: DDL Error: "object not found: VOLTDB_AUTOGEN_CT__PK_COUNTRY_PK_COUNTRY_ID"
    at org.voltdb.client.ClientImpl.internalSyncCallProcedure(ClientImpl.java:461)
    at org.voltdb.client.ClientImpl.callProcedureWithClientTimeoutImpl(ClientImpl.java:311)
    at org.voltdb.client.ClientImpl.callProcedureWithClientTimeout(ClientImpl.java:285)
    at org.voltdb.jdbc.JDBC4ClientConnection.execute(JDBC4ClientConnection.java:351)
    at org.voltdb.jdbc.JDBC4Statement$VoltSQL.execute(JDBC4Statement.java:122)
    ... 6 more
Processing of the script was aborted, error code=1`

### <a name="issues_yugabyte"></a> YugabyteDB

- Dropping primary key constraints not yet supported (see [here](https://github.com/trinodb/trino/issues/xxxx)).

`2021-07-26 05:20:41,803 [DatabaseSeeder.java] INFO  Start YugabyteDB
org.postgresql.util.PSQLException: ERROR: dropping a primary key constraint is not yet supported
    at org.postgresql.core.v3.QueryExecutorImpl.receiveErrorResponse(QueryExecutorImpl.java:2434)
    at org.postgresql.core.v3.QueryExecutorImpl.processResults(QueryExecutorImpl.java:2179)
    at org.postgresql.core.v3.QueryExecutorImpl.execute(QueryExecutorImpl.java:307)
    at org.postgresql.jdbc.PgStatement.executeInternal(PgStatement.java:441)
    at org.postgresql.jdbc.PgStatement.execute(PgStatement.java:365)
    at org.postgresql.jdbc.PgStatement.executeWithFlags(PgStatement.java:307)
    at org.postgresql.jdbc.PgStatement.executeCachedSql(PgStatement.java:293)
    at org.postgresql.jdbc.PgStatement.executeWithFlags(PgStatement.java:270)
    at org.postgresql.jdbc.PgStatement.execute(PgStatement.java:266)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeSQLStmnts(AbstractJdbcSeeder.java:1319)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:1216)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:417)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:274)
Processing of the script was aborted, error code=1`

----------

