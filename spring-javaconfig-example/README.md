# About

**This example project demonstrates:**
- programmatic `ProxyDataSource` creation using `ProxyDataSourceBuilder`.
- spring java based configuration

---

**Output example**

```
**********************************************************
JDBC: ProxyDataSource#getConnection
JDBC: JDBCConnection#createStatement

Name:MyDS, Connection:1, Time:0, Success:True
Type:Statement, Batch:False, QuerySize:1, BatchSize:0
Query:["
    CREATE TABLE users (id INT, name VARCHAR(20))"]
Params:[]
Query took 0msec
JDBC: JDBCStatement#execute
JDBC: JDBCStatement#close
JDBC: JDBCConnection#close
JDBC: ProxyDataSource#getConnection
JDBC: JDBCConnection#prepareStatement
JDBC: JDBCPreparedStatement#getConnection
JDBC: JDBCConnection#getMetaData
JDBC: JDBCPreparedStatement#setObject
JDBC: JDBCPreparedStatement#setString
JDBC: JDBCPreparedStatement#addBatch
JDBC: JDBCPreparedStatement#setObject
JDBC: JDBCPreparedStatement#setString
JDBC: JDBCPreparedStatement#addBatch

Name:MyDS, Connection:2, Time:0, Success:True
Type:Prepared, Batch:True, QuerySize:1, BatchSize:2
Query:["
    INSERT 
    INTO
        users
        (id, name) 
    VALUES
        (?, ?)"]
Params:[(1,foo),(2,bar)]
Query took 0msec
JDBC: JDBCPreparedStatement#executeBatch
JDBC: JDBCPreparedStatement#close
JDBC: JDBCConnection#close
JDBC: ProxyDataSource#getConnection
JDBC: JDBCConnection#prepareStatement
JDBC: JDBCPreparedStatement#setString
JDBC: JDBCPreparedStatement#setInt
JDBC: JDBCPreparedStatement#addBatch
JDBC: JDBCPreparedStatement#setInt
JDBC: JDBCPreparedStatement#setString
JDBC: JDBCPreparedStatement#addBatch

Name:MyDS, Connection:3, Time:0, Success:True
Type:Prepared, Batch:True, QuerySize:1, BatchSize:2
Query:["
    INSERT 
    INTO
        users
        (id, name) 
    VALUES
        (?, ?)"]
Params:[(3,FOO),(4,BAR)]
Query took 0msec
JDBC: JDBCPreparedStatement#executeBatch
JDBC: ProxyDataSource#getConnection
JDBC: JDBCConnection#createStatement

Name:MyDS, Connection:4, Time:2, Success:True
Type:Statement, Batch:False, QuerySize:1, BatchSize:0
Query:["
    SELECT
        COUNT(*) 
    FROM
        users"]
Params:[]
Query took 2msec
JDBC: JDBCStatement#executeQuery
JDBC: JDBCResultSet#next
JDBC: JDBCResultSet#getMetaData
JDBC: JDBCResultSet#getInt
JDBC: JDBCResultSet#wasNull
JDBC: JDBCResultSet#next
JDBC: JDBCResultSet#close
JDBC: JDBCStatement#close
JDBC: JDBCConnection#close
**********************************************************
```