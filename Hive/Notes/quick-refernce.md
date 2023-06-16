## Create new table with same schema of existing table
```
CREATE EXTERNAL TABLE <new_table> LIKE <old_table> LOCATION '<new_path>'
```

## Create external table with delimter file
```
CREATE EXTERNAL TABLE tablename 
(col1 string, col2 string)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' 
STORED AS TEXTFILE
LOCATION '<location>'
TBLPROPERTIES('skip.header.line.count'='1');
```
