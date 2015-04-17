# About

This example project demonstrates:
- JNDI datasource lookup using embedded tomcat
- Servlet filter for QueryCount(`CommonsQueryCountLoggingServletFilter`)

---

**Output example:** 

Application startup log:

```
.. CommonsQueryLoggingListener : Name:MyProxy, Time:1, Success:True, Type:Statement, Batch:False, QuerySize:1, BatchSize:0, Query:["CREATE TABLE users(id INT, name VARCHAR(255))"], Params:[]
.. CommonsQueryLoggingListener : Name:MyProxy, Time:1, Success:True, Type:Prepared, Batch:True, QuerySize:1, BatchSize:2, Query:["INSERT INTO users (id, name) VALUES (?, ?)"], Params:[(1=1,2=foo),(1=2,2=bar)]
```


Accessing application: 

```sh
> curl http://localhost:8080
{"dataSourceName":"MyProxy","queryCount":{"select":1,"insert":0,"update":0,"delete":0,"other":0,"total":1,"failure":0,"success":1,"time":6},"numOfUsers":2}
```

Log:
```
.. CommonsQueryLoggingListener    : Name:MyProxy, Time:6, Success:True, Type:Statement, Batch:False, QuerySize:1, BatchSize:0, Query:["SELECT COUNT(*) FROM users"], Params:[]
.. CommonsQueryCountLoggingServletFilter : Name:"MyProxy", Time:6, Total:1, Success:1, Failure:0, Select:1, Insert:0, Update:0, Delete:0, Other:0
```


