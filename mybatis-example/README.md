# About

**This example project demonstrates:**
- programmatic `ProxyDataSource` creation using `ProxyDataSourceBuilder`.
- mybatis 3

---

**Output example**

```
Name:ProxyDataSource, Connection:1, Time:3, Success:True, Type:Statement, Batch:False, QuerySize:1, BatchSize:0, Query:["CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255))"], Params:[]
Name:ProxyDataSource, Connection:1, Time:2, Success:True, Type:Statement, Batch:False, QuerySize:1, BatchSize:0, Query:["INSERT INTO users (id, name) VALUES (1, 'Alice'), (2, 'Bob')"], Params:[]
Name:ProxyDataSource, Connection:2, Time:0, Success:True, Type:Prepared, Batch:False, QuerySize:1, BatchSize:0, Query:["INSERT INTO users (id, name) VALUES (?, ?)"], Params:[(3,Charlie)]
Name:ProxyDataSource, Connection:2, Time:2, Success:True, Type:Prepared, Batch:False, QuerySize:1, BatchSize:0, Query:["SELECT id, name FROM users"], Params:[()]
```
