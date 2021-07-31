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

`CREATE TABLE KXN_SCHEMA.COUNTRY_STATE (
    PK_COUNTRY_STATE_ID BIGINT NOT NULL,
    FK_COUNTRY_ID BIGINT NOT NULL,
    FK_TIMEZONE_ID BIGINT NOT NULL,
    COUNTRY_STATE_MAP BLOB,
    CREATED TIMESTAMP NOT NULL,
    MODIFIED TIMESTAMP,
    NAME VARCHAR(100) NOT NULL,
    SYMBOL VARCHAR(50),
    CONSTRAINT CONSTRAINT_KXN_4 UNIQUE (),
    CONSTRAINT "SQL0000000163-08900481-017a-f4fd-c9dc-00000016e126" PRIMARY KEY (),
    CONSTRAINT "SQL0000000164-11b04482-017a-f4fd-c9dc-00000016e126" FOREIGN KEY () REFERENCES KXN_SCHEMA.COUNTRY(),
    CONSTRAINT "SQL0000000165-2ad0c483-017a-f4fd-c9dc-00000016e126" FOREIGN KEY () REFERENCES KXN_SCHEMA.TIMEZONE()
);
CREATE INDEX "SQL0000000164-11b04482-017a-f4fd-c9dc-00000016e126" ON KXN_SCHEMA.COUNTRY_STATE (FK_COUNTRY_ID);
CREATE INDEX "SQL0000000165-2ad0c483-017a-f4fd-c9dc-00000016e126" ON KXN_SCHEMA.COUNTRY_STATE (FK_TIMEZONE_ID);
CREATE UNIQUE INDEX "SQL0000000166-6f554487-017a-f4fd-c9dc-00000016e126" ON KXN_SCHEMA.COUNTRY_STATE (FK_COUNTRY_ID,NAME);`

`2021-07-30 03:46:18,848 [DatabaseSeeder.java] INFO  Start Apache Derby [client]
java.sql.SQLSyntaxErrorException: ALTER TABLE failed. There is no constraint 'KXN_SCHEMA.SQL0000000166-6f554487-017a-f4fd-c9dc-00000016e126' on table '"KXN_SCHEMA"."COUNTRY_STATE"'.
    at org.apache.derby.client.am.SQLExceptionFactory.getSQLException(SQLExceptionFactory.java:94)
    at org.apache.derby.client.am.SqlException.getSQLException(SqlException.java:325)
    at org.apache.derby.client.am.ClientStatement.execute(ClientStatement.java:997)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeSQLStmnts(AbstractJdbcSeeder.java:1336)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:1206)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:411)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:129)
    Caused by: ERROR 42X86: ALTER TABLE failed. There is no constraint 'KXN_SCHEMA.SQL0000000166-6f554487-017a-f4fd-c9dc-00000016e126' on table '"KXN_SCHEMA"."COUNTRY_STATE"'.
    at org.apache.derby.client.am.ClientStatement.completeSqlca(ClientStatement.java:2116)
    at org.apache.derby.client.am.ClientStatement.completeExecuteImmediate(ClientStatement.java:1683)
    at org.apache.derby.client.net.NetStatementReply.parseEXCSQLIMMreply(NetStatementReply.java:209)
    at org.apache.derby.client.net.NetStatementReply.readExecuteImmediate(NetStatementReply.java:60)
    at org.apache.derby.client.net.StatementReply.readExecuteImmediate(StatementReply.java:47)
    at org.apache.derby.client.net.NetStatement.readExecuteImmediate_(NetStatement.java:142)
    at org.apache.derby.client.am.ClientStatement.readExecuteImmediate(ClientStatement.java:1679)
    at org.apache.derby.client.am.ClientStatement.flowExecute(ClientStatement.java:2408)
    at org.apache.derby.client.am.ClientStatement.executeX(ClientStatement.java:1002)
    at org.apache.derby.client.am.ClientStatement.execute(ClientStatement.java:988)
    ... 4 more
Processing of the script was aborted, error code=1`

### <a name="issues_h2"></a> H2 Database Engine

- Issue:  dropping unique key constraints - SQL statement `ALTER TABLE COUNTRY DROP CONSTRAINT CONSTRAINT_INDEX_6` (see [here](https://github.com/h2database/h2database/issues/3163)):

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

`2021-07-29 11:48:34,901 [DatabaseSeeder.java] INFO  Start H2 Database Engine [client]
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
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeSQLStmnts(AbstractJdbcSeeder.java:1309)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:1206)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:408)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:154)
Processing of the script was aborted, error code=1`

### <a name="issues_hsqldb"></a> HSQLDB

- Issue:  dropping unique key constraints - SQL statement `ALTER TABLE COUNTRY DROP CONSTRAINT SYS_IDX_SYS_PK_10289_10293` (see [here](https://sourceforge.net/p/hsqldb/bugs/1637/)):

`CREATE TABLE PUBLIC.KXN_SCHEMA.COUNTRY (
    PK_COUNTRY_ID BIGINT NOT NULL,
    COUNTRY_MAP BLOB,
    CREATED TIMESTAMP NOT NULL,
    ISO3166 VARCHAR(50),
    MODIFIED TIMESTAMP,
    NAME VARCHAR(100) NOT NULL,
    CONSTRAINT SYS_PK_10095 PRIMARY KEY (PK_COUNTRY_ID)
);
CREATE UNIQUE INDEX SYS_IDX_SYS_CT_10098_10103 ON PUBLIC.KXN_SCHEMA.COUNTRY (NAME);
CREATE UNIQUE INDEX SYS_IDX_SYS_PK_10095_10099 ON PUBLIC.KXN_SCHEMA.COUNTRY (PK_COUNTRY_ID);
`

`2021-07-29 12:37:21,595 [DatabaseSeeder.java] INFO  Start HSQLDB [client]
java.sql.SQLSyntaxErrorException: user lacks privilege or object not found: SYS_IDX_SYS_PK_10289_10293
    at org.hsqldb.jdbc.JDBCUtil.sqlException(Unknown Source)
    at org.hsqldb.jdbc.JDBCUtil.sqlException(Unknown Source)
    at org.hsqldb.jdbc.JDBCStatement.fetchResult(Unknown Source)
    at org.hsqldb.jdbc.JDBCStatement.execute(Unknown Source)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeSQLStmnts(AbstractJdbcSeeder.java:1309)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:1206)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:408)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:166)
    Caused by: org.hsqldb.HsqlException: user lacks privilege or object not found: SYS_IDX_SYS_PK_10289_10293
    at org.hsqldb.error.Error.error(Unknown Source)
    at org.hsqldb.result.Result.getException(Unknown Source)
    ... 7 more
Processing of the script was aborted, error code=1`

### <a name="issues_ibmdb2"></a> IBM Db2 Database

- Issue: Docker Image from `docker pull ibmcom/db2:11.5.6.0` (see [here](https://www.tek-tips.com/viewthread.cfm?qid=1811168)):

`2021-07-29 13:09:33,884 [DatabaseSeeder.java] INFO  Start IBM Db2 Database
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
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.connect(AbstractJdbcSeeder.java:321)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.connect(AbstractJdbcSeeder.java:212)
    at ch.konnexions.db_seeder.jdbc.ibmdb2.Ibmdb2Seeder.setupDatabase(Ibmdb2Seeder.java:92)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:399)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:178)
Processing of the script was aborted, error code=1`

### <a name="issues_omnisci"></a> OmniSciDB

- Issue: connection problem with existing OmnisciDB (see [here](https://github.com/omnisci/omniscidb/issues/668)).

`2021-07-29 13:52:13,809 [DatabaseSeeder.java] INFO  Start OmniSciDB
java.sql.SQLException: Omnisci connection failed - [OmniSci.java:read:15275:TOmniSciException(error_msg:Sqlite3 Error: disk I/O error)]
    at com.omnisci.jdbc.OmniSciConnection.<init>(OmniSciConnection.java:460)
    at com.omnisci.jdbc.OmniSciDriver.connect(OmniSciDriver.java:80)
    at java.sql/java.sql.DriverManager.getConnection(DriverManager.java:677)
    at java.sql/java.sql.DriverManager.getConnection(DriverManager.java:228)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.connect(AbstractJdbcSeeder.java:326)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.connect(AbstractJdbcSeeder.java:275)
    at ch.konnexions.db_seeder.jdbc.omnisci.OmnisciSeeder.setupDatabase(OmnisciSeeder.java:93)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:399)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:220)
    Caused by: TOmniSciException(error_msg:Sqlite3 Error: disk I/O error)
    at com.omnisci.thrift.server.OmniSci$connect_result$connect_resultStandardScheme.read(OmniSci.java:15275)
    at com.omnisci.thrift.server.OmniSci$connect_result$connect_resultStandardScheme.read(OmniSci.java:15253)
    at com.omnisci.thrift.server.OmniSci$connect_result.read(OmniSci.java:15195)
    at org.apache.thrift.TServiceClient.receiveBase(TServiceClient.java:88)
    at com.omnisci.thrift.server.OmniSci$Client.recv_connect(OmniSci.java:406)
    at com.omnisci.thrift.server.OmniSci$Client.connect(OmniSci.java:391)
    at com.omnisci.jdbc.OmniSciConnection.setSession(OmniSciConnection.java:431)
    at com.omnisci.jdbc.OmniSciConnection.<init>(OmniSciConnection.java:452)
    ... 8 more
Processing of the script was aborted, error code=1
`

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

- Issue:  dropping primary key constraints (see [here](https://voltdb-public.slack.com/archives/C04UPPHUL/p1627566165007800)):

1. DDL Statement: CREATE TABLE

`CREATE TABLE COUNTRY (
    COUNTRY_MAP VARBINARY(1048576),
    CREATED TIMESTAMP NOT NULL,
    ISO3166 VARCHAR(50),
    MODIFIED TIMESTAMP,
    NAME VARCHAR(100) NOT NULL,
    PK_COUNTRY_ID BIGINT NOT NULL,
    CONSTRAINT VOLTDB_AUTOGEN_CT__PK_COUNTRY_PK_COUNTRY_ID PRIMARY KEY (PK_COUNTRY_ID)
);
CREATE UNIQUE INDEX VOLTDB_AUTOGEN_IDX_CT_COUNTRY_NAME ON COUNTRY (NAME);
CREATE UNIQUE INDEX VOLTDB_AUTOGEN_IDX_PK_COUNTRY_PK_COUNTRY_ID ON COUNTRY (PK_COUNTRY_ID);`

2. DDL Statement: DROP CONSTRAINT

`ALTER TABLE COUNTRY DROP CONSTRAINT VOLTDB_AUTOGEN_CT__PK_COUNTRY_PK_COUNTRY_ID`

3. Error message:

`2021-07-29 15:12:59,724 [DatabaseSeeder.java] INFO  Start VoltDB
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
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.executeSQLStmnts(AbstractJdbcSeeder.java:1337)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.dropTableConstraints(AbstractJdbcSeeder.java:1207)
    at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:408)
    at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:280)
    Caused by: org.voltdb.client.ProcCallException: [Ad Hoc DDL Input:1]: DDL Error: "object not found: VOLTDB_AUTOGEN_CT__PK_COUNTRY_PK_COUNTRY_ID"
    at org.voltdb.client.ClientImpl.internalSyncCallProcedure(ClientImpl.java:461)
    at org.voltdb.client.ClientImpl.callProcedureWithClientTimeoutImpl(ClientImpl.java:311)
    at org.voltdb.client.ClientImpl.callProcedureWithClientTimeout(ClientImpl.java:285)
    at org.voltdb.jdbc.JDBC4ClientConnection.execute(JDBC4ClientConnection.java:351)
    at org.voltdb.jdbc.JDBC4Statement$VoltSQL.execute(JDBC4Statement.java:122)
    ... 6 more
Processing of the script was aborted, error code=1`

----------

