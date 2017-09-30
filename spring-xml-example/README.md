# About

**This example project demonstrates:**
- XML based spring configuration to create `ProxyDataSource`

---

**Output example**

```
**********************************************************
[1][success][5ms][conn=1] ProxyDataSource#getConnection()
[2][success][7ms][conn=1] JDBCConnection#createStatement()
Name:my-ds, Connection:1, Time:0, Success:True, Type:Statement, Batch:False, QuerySize:1, BatchSize:0, Query:["CREATE TABLE users (id INT, name VARCHAR(20))"], Params:[]
[3][success][2ms][conn=1] JDBCStatement#execute("CREATE TABLE users (id INT, name VARCHAR(20))...")
[4][success][0ms][conn=1] JDBCStatement#close()
[5][success][0ms][conn=1] JDBCConnection#close()
**********************************************************
```