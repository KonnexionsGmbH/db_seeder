# Release Notes

![Travis (.com)](https://img.shields.io/travis/com/KonnexionsGmbH/db_seeder.svg?branch=master)
![GitHub release](https://img.shields.io/github/release/KonnexionsGmbH/db_seeder.svg)
![GitHub Release Date](https://img.shields.io/github/release-date/KonnexionsGmbH/db_seeder.svg)
![GitHub commits since latest release](https://img.shields.io/github/commits-since/KonnexionsGmbH/db_seeder/2.7.0.svg)

## 1. Current Issues

### 1.1 Apache Derby

- The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

### 1.2 Exasol

- JDBC Driver: java.sql.SQLException: Invalid character in connection string (see [here](https://community.exasol.com/t5/discussion-forum/jdbc-driver-java-sql-sqlexception-invalid-character-in/td-p/2224)).
    
- Ubuntu 20.10: com.exasol.jdbc.ConnectFailed: Connection reset (see [here](https://community.exasol.com/t5/discussion-forum/ubuntu-20-10-com-exasol-jdbc-connectfailed-connection-reset/td-p/2362))
    
### 1.3 Mimer SQL & DBeaver

- DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

### 1.4 Oracle Database

- Database 19c: ORA-12637: Packet receive failed (see [here](https://github.com/KonnexionsGmbH/db_seeder/issues/87)).

### 1.5 Trino Distributed Query Engine

- All Connectors: Absolutely unsatisfactory performance (see [here](https://github.com/trinodb/trino/issues/5681)).
    
- Microsoft SQL Connector: SQL Server "Expected zero to one elements, but found multiple" (see [here](https://github.com/trinodb/trino/issues/6464)).

    2021-01-13 19:42:51,763 [DatabaseSeeder.java] INFO  Start
    2021-01-13 19:42:51,769 [DatabaseSeeder.java] INFO  tickerSymbolExtern='sqlserver_trino'
    2021-01-13 19:42:51,769 [DatabaseSeeder.java] INFO  Start Microsoft SQL Server via Trino
    SLF4J: Class path contains multiple SLF4J bindings.
    SLF4J: Found binding in [jar:file:/D:/SoftDevelopment/Projects/db_seeder/lib/db_seeder.jar!/org/slf4j/impl/StaticLoggerBinder.class]
    SLF4J: Found binding in [jar:file:/D:/SoftDevelopment/Projects/db_seeder/lib/jdbc-yugabytedb-42.2.7-yb-3.jar!/org/slf4j/impl/StaticLoggerBinder.class]
    SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
    SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
    java.sql.SQLException: Query failed (#20210113_184254_00000_jve99): Expected zero to one elements, but found multiple
            at io.trino.jdbc.AbstractTrinoResultSet.resultsException(AbstractTrinoResultSet.java:1912)
            at io.trino.jdbc.TrinoResultSet.getColumns(TrinoResultSet.java:242)
            at io.trino.jdbc.TrinoResultSet.create(TrinoResultSet.java:53)
            at io.trino.jdbc.TrinoStatement.internalExecute(TrinoStatement.java:249)
            at io.trino.jdbc.TrinoStatement.execute(TrinoStatement.java:227)
            at io.trino.jdbc.TrinoStatement.executeQuery(TrinoStatement.java:76)
            at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.countData(AbstractJdbcSeeder.java:289)
            at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:364)
            at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:331)
            at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:217)
    Caused by: java.lang.IllegalStateException: Expected zero to one elements, but found multiple
            at org.jdbi.v3.core.result.ResultIterable.findOne(ResultIterable.java:163)
            at io.trino.plugin.sqlserver.SqlServerClient.getTableDataCompression(SqlServerClient.java:437)
            at io.trino.plugin.sqlserver.SqlServerClient.getTableProperties(SqlServerClient.java:378)
            at io.trino.plugin.jdbc.ForwardingJdbcClient.getTableProperties(ForwardingJdbcClient.java:288)
            at io.trino.plugin.jdbc.jmx.StatisticsAwareJdbcClient.getTableProperties(StatisticsAwareJdbcClient.java:305)
            at io.trino.plugin.jdbc.CachingJdbcClient.getTableProperties(CachingJdbcClient.java:365)
            at io.trino.plugin.jdbc.CachingJdbcClient.getTableProperties(CachingJdbcClient.java:365)
            at io.trino.plugin.jdbc.JdbcMetadata.getTableMetadata(JdbcMetadata.java:344)
            at io.trino.metadata.MetadataManager.getTableMetadata(MetadataManager.java:508)
            at io.trino.sql.analyzer.StatementAnalyzer$Visitor.visitTable(StatementAnalyzer.java:1231)
            at io.trino.sql.analyzer.StatementAnalyzer$Visitor.visitTable(StatementAnalyzer.java:329)
            at io.trino.sql.tree.Table.accept(Table.java:53)
            at io.trino.sql.tree.AstVisitor.process(AstVisitor.java:27)
            at io.trino.sql.analyzer.StatementAnalyzer$Visitor.process(StatementAnalyzer.java:346)
            at io.trino.sql.analyzer.StatementAnalyzer$Visitor.analyzeFrom(StatementAnalyzer.java:2529)
            at io.trino.sql.analyzer.StatementAnalyzer$Visitor.visitQuerySpecification(StatementAnalyzer.java:1553)
            at io.trino.sql.analyzer.StatementAnalyzer$Visitor.visitQuerySpecification(StatementAnalyzer.java:329)
            at io.trino.sql.tree.QuerySpecification.accept(QuerySpecification.java:144)
            at io.trino.sql.tree.AstVisitor.process(AstVisitor.java:27)
            at io.trino.sql.analyzer.StatementAnalyzer$Visitor.process(StatementAnalyzer.java:346)
            at io.trino.sql.analyzer.StatementAnalyzer$Visitor.process(StatementAnalyzer.java:356)
            at io.trino.sql.analyzer.StatementAnalyzer$Visitor.visitQuery(StatementAnalyzer.java:1061)
            at io.trino.sql.analyzer.StatementAnalyzer$Visitor.visitQuery(StatementAnalyzer.java:329)
            at io.trino.sql.tree.Query.accept(Query.java:107)
            at io.trino.sql.tree.AstVisitor.process(AstVisitor.java:27)
            at io.trino.sql.analyzer.StatementAnalyzer$Visitor.process(StatementAnalyzer.java:346)
            at io.trino.sql.analyzer.StatementAnalyzer.analyze(StatementAnalyzer.java:315)
            at io.trino.sql.analyzer.Analyzer.analyze(Analyzer.java:91)
            at io.trino.sql.analyzer.Analyzer.analyze(Analyzer.java:83)
            at io.trino.execution.SqlQueryExecution.analyze(SqlQueryExecution.java:263)
            at io.trino.execution.SqlQueryExecution.<init>(SqlQueryExecution.java:186)
            at io.trino.execution.SqlQueryExecution$SqlQueryExecutionFactory.createQueryExecution(SqlQueryExecution.java:768)
            at io.trino.dispatcher.LocalDispatchQueryFactory.lambda$createDispatchQuery$0(LocalDispatchQueryFactory.java:129)
            at io.trino.$gen.Trino_351____20210113_184151_2.call(Unknown Source)
            at com.google.common.util.concurrent.TrustedListenableFutureTask$TrustedFutureInterruptibleTask.runInterruptibly(TrustedListenableFutureTask.java:125)
            at com.google.common.util.concurrent.InterruptibleTask.run(InterruptibleTask.java:69)
            at com.google.common.util.concurrent.TrustedListenableFutureTask.run(TrustedListenableFutureTask.java:78)
            at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
            at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
            at java.base/java.lang.Thread.run(Thread.java:834)
    Processing of the script was aborted, error code=1

- Oracle Connector: Oracle session not disconnected (see [here](https://github.com/trinodb/trino/issues/5648)).

    2021-01-14 17:44:35,322 [DatabaseSeeder.java] INFO  Start
    2021-01-14 17:44:35,328 [DatabaseSeeder.java] INFO  tickerSymbolExtern='oracle_trino'
    2021-01-14 17:44:35,328 [DatabaseSeeder.java] INFO  Start Oracle Database via Trino
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
    Caused by: Error : 1940, Position : 0, Sql = DROP USER  KXN_USER CASCADE, OriginalSql = DROP USER  KXN_USER CASCADE, Error Msg = ORA-01940: Ein Benutzer, der gerade mit der DB verbunden ist, kann nicht gelöscht werden
    
            at oracle.jdbc.driver.T4CTTIoer11.processError(T4CTTIoer11.java:513)
            ... 16 more
    Processing of the script was aborted, error code=1
    
  - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/trinodb/trino/issues/4764)).

### 1.6 YugabyteDB

- Windows 10: Creation of Docker Container fails (see [here](https://github.com/yugabyte/yugabyte-db/issues/5497)).

    2021-01-13 19:47:01,235 [DatabaseSeeder.java] INFO  Start
    2021-01-13 19:47:01,241 [DatabaseSeeder.java] INFO  tickerSymbolExtern='yugabyte'
    2021-01-13 19:47:01,241 [DatabaseSeeder.java] INFO  Start YugabyteDB
    SLF4J: Class path contains multiple SLF4J bindings.
    SLF4J: Found binding in [jar:file:/D:/SoftDevelopment/Projects/db_seeder/lib/db_seeder.jar!/org/slf4j/impl/StaticLoggerBinder.class]
    SLF4J: Found binding in [jar:file:/D:/SoftDevelopment/Projects/db_seeder/lib/jdbc-yugabytedb-42.2.7-yb-3.jar!/org/slf4j/impl/StaticLoggerBinder.class]
    SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
    SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
    Jan. 13, 2021 7:47:03 PM org.postgresql.core.v3.ConnectionFactoryImpl log
    WARNING: ConnectException occurred while connecting to localhost:5433
    java.net.ConnectException: Connection refused: no further information
            at java.base/sun.nio.ch.Net.pollConnect(Native Method)
            at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:660)
            at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:549)
            at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:597)
            at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:333)
            at java.base/java.net.Socket.connect(Socket.java:648)
            at org.postgresql.core.PGStream.<init>(PGStream.java:69)
            at org.postgresql.core.v3.ConnectionFactoryImpl.openConnectionImpl(ConnectionFactoryImpl.java:156)
            at org.postgresql.core.ConnectionFactory.openConnection(ConnectionFactory.java:49)
            at org.postgresql.jdbc.PgConnection.<init>(PgConnection.java:195)
            at org.postgresql.Driver.makeConnection(Driver.java:452)
            at org.postgresql.Driver.connect(Driver.java:254)
            at java.sql/java.sql.DriverManager.getConnection(DriverManager.java:677)
            at java.sql/java.sql.DriverManager.getConnection(DriverManager.java:251)
            at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.connect(AbstractJdbcSeeder.java:246)
            at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.connect(AbstractJdbcSeeder.java:152)
            at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.setupPostgresql(AbstractJdbcSeeder.java:1597)
            at ch.konnexions.db_seeder.jdbc.yugabyte.YugabyteSeeder.setupDatabase(YugabyteSeeder.java:97)
            at ch.konnexions.db_seeder.jdbc.AbstractJdbcSeeder.createData(AbstractJdbcSeeder.java:328)
            at ch.konnexions.db_seeder.DatabaseSeeder.main(DatabaseSeeder.java:229)


    Docker create db_seeder_db (YugabyteDB 2.5.1.0-b153)
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/conf/yugabyted.conf': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/master-info': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/consensus-meta/00000000000000000000000000000000': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000/000003.log': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000/CURRENT': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000/IDENTITY': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000/LOCK': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000/MANIFEST-000001': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000/OPTIONS-000005': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000.intents/000003.log': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000.intents/CURRENT': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000.intents/IDENTITY': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000.intents/LOCK': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000.intents/MANIFEST-000001': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000.intents/OPTIONS-000005': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/data/rocksdb/table-sys.catalog.uuid/tablet-00000000000000000000000000000000.snapshots': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/instance': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/logs/yb-master.b2ff6eb1fb90.root.log.ERROR.20210113-184632.18': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/logs/yb-master.b2ff6eb1fb90.root.log.FATAL.20210113-184632.18': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/logs/yb-master.b2ff6eb1fb90.root.log.INFO.20210113-184631.18': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/logs/yb-master.b2ff6eb1fb90.root.log.WARNING.20210113-184631.18': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/logs/yb-master.ERROR': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/logs/yb-master.FATAL': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/logs/yb-master.FATAL.details.2021-01-13T18_46_32.pid18.txt': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/logs/yb-master.INFO': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/logs/yb-master.WARNING': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/tablet-meta/00000000000000000000000000000000': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/data/yb-data/master/wals/table-sys.catalog.uuid/tablet-00000000000000000000000000000000/wal-000000001': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/logs/master': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/logs/master.err': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/logs/master.out': Permission denied
    rm: cannot remove '/mnt/d/SoftDevelopment/Projects/db_seeder/tmp/yb_data/logs/yugabyted.log': Permission denied



## 2. Version History

### 2.7.0

Release Date: xx.01.2021

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Docker Desktop Community: 3.0.4  
- Java Version 15 (e.g.: https://jdk.java.net/15/)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)
- an environment variable called `HOME_ECLIPSE` that points to the installation directory of Eclipse, e.g.: `C:\Software\eclipse\java-2020-12\eclipse`

#### New Features

Rebranding of Presto to Trino

#### Modified Features

- CrateDB: DBMS 4.3.4

- Exasol: DBMS 7.0.6 / JDBC 7.0.4

- Firebird: JDBC 4.0.2.java11

- IBM Db2 Database: DBMS 11.5.5.0

- IBM Informix: DBMS 14.10.FC5DE

- Mimer SQL: JDBC 3.41a

- Oracle Database: JDBC 19.9.0.0

- SQLite: DBMS 3.34.0 / JDBC 3.34.0

- Trino Distributed Query Engine: DBMS 351 / JDBC 351

- YugabyteDB: DBMS 2.5.1.0-b153

#### Deleted Features

n/a

----------

### 2.6.1

Release Date: 28.11.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 15 (e.g.: https://jdk.java.net/15/)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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

- PostgreSQL Database: DBMS 13.1

- Presto Distributed Query Engine: DBMS 347 / JDBC 347

- Travis CI  has been limited to the compilation functionality 

- VoltDB: JDBC 10.1.1

- YugabyteDB: DBMS 2.5.0.0-b2

----------

### 2.6.0

Release Date: 27.10.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 15 (e.g.: https://jdk.java.net/15/)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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

- PostgreSQL Database: JDBC 42.2.18

- Presto Distributed Query Engine: DBMS 345 / JDBC 345

----------

### 2.5.2

Release Date: 05.10.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 15 (e.g.: https://jdk.java.net/15/)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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
- Java Version 15 (e.g.: https://jdk.java.net/15/)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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

- PostgreSQL Database: DBMS 13

- Presto Distributed Query Engine: DBMS 343
- Presto Distributed Query Engine: JDBC 343

----------

### 2.5.0

Release Date: 30.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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

- Microsoft SQL Server: JDBC 8.4.1.jre14

----------

### 2.4.0

Release Date: 27.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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

- PostgreSQL Database: DBMS 12.4
- PostgreSQL Database: JDBC 42.2.15

----------

### 2.2.0

Release Date: 18.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

- Presto Distributed Query Engine

  - Oracle Connector: Support Oracle's NUMBER data type (see [here](https://github.com/prestosql/presto/issues/4764)).
  - SQL Server Connector: Failed to insert NULL for varbinary in SQL Server (see [here](https://github.com/prestosql/presto/issues/4795)).

#### Modified Features

- PostgreSQL Database: JDBC 42.2.15

- Presto Distributed Query Engine: use of Docker network

- minor script fixes

----------

### 2.1.2

Release Date: 13.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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

- solved: gradle warning with http://maven.cubrid.org (see [here](http://jira.cubrid.org/browse/CBRD-23727)).

----------

### 2.1.0

Release Date: 11.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- CUBRID

  - gradle warning with http://maven.cubrid.org (see [here](http://jira.cubrid.org/browse/CBRD-23727)).

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

- Microsoft SQL Server: JDBC 8.4.0.jre14

----------

### 2.0.0

Release Date: 02.08.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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

### 1.15.10

Release Date: 14.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

- Mimer SQL & DBeaver

  - DBeaver: Previewing BLOB column shows "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203)).

#### Modified Features

- Config.java: reduce number of properties (issue #36)

- MySQL Database: DBMS & JDBC 8.0.22

----------

### 1.15.8

Release Date: 13.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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

### 1.15.5

Release Date: 10.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
- grep utility, e.g. for Windows [here](http://gnuwin32.sourceforge.net/packages/grep.htm)

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

### 1.14.0

Release Date: 06.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### New Features

- New DBMS: IBM Informix

----------

### 1.13.0

Release Date: 01.07.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### New Features

- New DBMS: H2 Database Engine

----------

### 1.12.0

Release Date: 29.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### New Features

- New DBMS: H2 Database Engine

----------

### 1.11.2

Release Date: 26.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).

#### Modified Features

- introducing encoding support for ISO-8859-1 and UTF-8

- Tested DBMS version: MariaDB Server 10.5.4

----------

### 1.11.0

Release Date: 26.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### Current Issues

- Apache Derby

  - The second run with the embedded version of Apache Derby stumbles over a known problem of Apache Derby (see [here](https://issues.apache.org/jira/browse/DERBY-5049?jql=project%20%3D%20DERBY%20AND%20issuetype%20%3D%20Bug%20AND%20status%20%3D%20Open%20AND%20resolution%20%3D%20Unresolved%20AND%20text%20~%20jdbc%20ORDER%20BY%20updated%20DESC%2C%20priority%20DESC)).
  
- Mimer SQL

  - Previewing BLOB columns with DBeaver gives the error message "Error loading text value" (see [here](https://github.com/dbeaver/dbeaver/issues/9203))  

#### New Features

- New DBMS: Firebird

----------

### 1.8.3

Release Date: 21.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

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

### 1.6.0

Release Date: 16.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: SQLite

----------

### 1.5.0

Release Date: 15.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: CrateDB

----------

### 1.4.0

Release Date: 14.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: IBM Db2 Database

----------

### 1.3.0

Release Date: 12.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: MariaDB Server

----------

### 1.2.0

Release Date: 10.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: PostgreSQL Database

----------

### 1.1.0

Release Date: 06.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- Documentation

- New DBMS: Microsoft SQL Server

- Travis CI Integration

- Verification of the data storage 

#### Modified Features

- Adding BLOB and CLOB support

----------

### 1.0.0

Release Date: 01.06.2020

#### System Requirements

- Operating system: any Java-enabled Linux, Mac or Windows variant
- Java Version 14 (e.g.: https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)

#### New Features

- New DBMS: MySQL Database
- New DBMS: Oracle Database

----------

