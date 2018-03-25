# About

**This example project demonstrates:**
- programmatic `ProxyDataSource` creation using `ProxyDataSourceBuilder`.
- spring java based configuration

---

**Output example**

```
**********************************************************
Name:MyDS, Time:1, Success:True, Type:Statement, Batch:False, QuerySize:1, BatchSize:0, Query:["CREATE TABLE users (id INT, name VARCHAR(20))"], Params:[]
Name:MyDS, Time:0, Success:True, Type:Prepared, Batch:True, QuerySize:1, BatchSize:2, Query:["INSERT INTO users (id, name) VALUES (?, ?)"], Params:[(1=1,2=foo),(1=2,2=bar)]
Name:MyDS, Time:1, Success:True, Type:Prepared, Batch:True, QuerySize:1, BatchSize:2, Query:["INSERT INTO users (id, name) VALUES (?, ?)"], Params:[(1=3,2=FOO),(1=4,2=BAR)]
Name:MyDS, Time:3, Success:True, Type:Statement, Batch:False, QuerySize:1, BatchSize:0, Query:["SELECT COUNT(*) FROM users"], Params:[]
**********************************************************
```