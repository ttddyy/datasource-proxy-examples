# About

**This example project demonstrates:**
- programmatic `ProxyDataSource` creation using `ProxyDataSourceBuilder`.
- springboot auto configuration

---

**Output example**

```
**********************************************************
2023-07-04T08:29:12.682+05:30  INFO 36788 --- [           main] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:MyDS, Connection:3, Time:5, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["select * from information_schema.sequences"]
Params:[()]
2023-07-04T08:29:13.470+05:30  INFO 36788 --- [           main] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:MyDS, Connection:4, Time:0, Success:True
Type:Statement, Batch:False, QuerySize:1, BatchSize:0
Query:["drop table if exists user cascade "]
Params:[]
2023-07-04T08:29:13.471+05:30  INFO 36788 --- [           main] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:MyDS, Connection:4, Time:0, Success:True
Type:Statement, Batch:False, QuerySize:1, BatchSize:0
Query:["drop sequence user_seq if exists"]
Params:[]
2023-07-04T08:29:13.478+05:30  INFO 36788 --- [           main] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:MyDS, Connection:5, Time:0, Success:True
Type:Statement, Batch:False, QuerySize:1, BatchSize:0
Query:["create sequence user_seq start with 1 increment by 50"]
Params:[]
2023-07-04T08:29:13.482+05:30  INFO 36788 --- [           main] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:MyDS, Connection:5, Time:1, Success:True
Type:Statement, Batch:False, QuerySize:1, BatchSize:0
Query:["create table user (id integer not null, name varchar(255), primary key (id))"]
Params:[]
2023-07-04T08:29:15.108+05:30  INFO 36788 --- [           main] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:MyDS, Connection:6, Time:0, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["call next value for user_seq"]
Params:[()]
2023-07-04T08:29:15.123+05:30  INFO 36788 --- [           main] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:MyDS, Connection:6, Time:0, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["call next value for user_seq"]
Params:[()]
2023-07-04T08:29:15.149+05:30  INFO 36788 --- [           main] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:MyDS, Connection:6, Time:0, Success:True
Type:Prepared, Batch:True, QuerySize:1, BatchSize:2
Query:["insert into user (name,id) values (?,?)"]
Params:[(foo,1),(bar,2)]
2023-07-04T08:29:15.601+05:30  INFO 36788 --- [           main] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:MyDS, Connection:7, Time:0, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["select count(*) from user u1_0"]
Params:[()]
2023-07-04T08:29:15.708+05:30  INFO 36788 --- [ionShutdownHook] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:MyDS, Connection:8, Time:1, Success:True
Type:Statement, Batch:False, QuerySize:1, BatchSize:0
Query:["drop table if exists user cascade "]
Params:[]
2023-07-04T08:29:15.708+05:30  INFO 36788 --- [ionShutdownHook] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:MyDS, Connection:8, Time:0, Success:True
Type:Statement, Batch:False, QuerySize:1, BatchSize:0
Query:["drop sequence user_seq if exists"]
Params:[]
**********************************************************
```
