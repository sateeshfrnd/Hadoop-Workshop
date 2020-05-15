### Calculate Date difference in Hive
```
datediff(to_date(String timestamp), to_date(String timestamp))

For example:
SELECT datediff(to_date('2019-08-03'), to_date('2019-08-01')) <= 2;
```
