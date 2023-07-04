# About

**This example project demonstrates:**
- programmatic `ProxyDataSource` creation using `ProxyDataSourceBuilder`.
- hibernate 6

---

**Output example**

```
Name:ProxyDataSource, Connection:2, Time:1, Success:True
Type:Statement, Batch:False, QuerySize:1, BatchSize:0
Query:["
    drop table if exists "Application$Usertable" cascade "]
Params:[]

Name:ProxyDataSource, Connection:3, Time:5, Success:True
Type:Statement, Batch:False, QuerySize:1, BatchSize:0
Query:["
    create table "Application$Usertable" (id integer not null, name varchar(255) not null, primary key (id))"]
Params:[]

Name:ProxyDataSource, Connection:4, Time:6, Success:True
Type:Prepared, Batch:True, QuerySize:1, BatchSize:3
Query:["
    insert 
    into
        "Application$Usertable"
        (name,id) 
    values
        (?,?)"]
Params:[(foo,1),(bar,2),(tar,3)]

Name:ProxyDataSource, Connection:5, Time:1, Success:True
Type:Statement, Batch:False, QuerySize:1, BatchSize:0
Query:["
    drop table if exists "Application$Usertable" cascade "]
Params:[]
```