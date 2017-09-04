### List all the tables in MySQL:
```
sqoop list-tables \
--connect jdbc:mysql://quickstart.cloudera:3306/retail_db \
--username retail_dba --password cloudera
```

### Execute MySQL query from Sqoop:
```
sqoop eval --query "SELECT count(1) from order_items"
```

### Import from MySQL 
```
sqoop import \
--connect jdbc:mysql://quickstart.cloudera:3306/retail_db \
--username retail_dba \
--password cloudera \
--table departments
```

### Import all the tables in MySQL:
```
sqoop import-all-tables \
--connect jdbc:mysql://quickstart.cloudera:3306/retail_db \
--username retail_dba \
--password cloudera 
```

### Set the delimiter
```
--fields-terminated-by='|' 
--lines-terminated-by \n
```

### Apply Filter  
```
--where \'category\'=22
--where \'category_id\'>22
--where "'category_id' between 1 and 22" 
```

### Encode null values for both string and non-string columns:
```
--null-string='N' --null-non-string='N' 
```

### Parameters required to set selective columns:
```
--columns=category_name,category_id 
```

### Apply custom boundary query for creating splits:
```
--boundary-query "select 1,25 from departments" 
```

### Pass query to import query result:
```
--query "SELECT * FROM orders join order_items on orders.order_id=order_items.order_item_order_id where \$CONDITIONS"\
```

### Import data in different file formats:
```
--as-avrodatafile
--as-textfile
--as-parquetfile 
```

### Apply compression while importing:
```
--compress  
--compression-codec=org.apache.hadoop.io.compress.SnappyCodec
```

### Store all the java files in directory while executing sqoop command:
```
--outdir java_output
```

### Import to Hive 
- Parameters required to import data to non-existing table in hive and create table while importing:
```
--hive-import   --hive-overwrite  --hive-table <db>.<table_name>  --create-hive-table
```
- Parameters required to import data to exisitng table while importing:
```
--hive-import  --hive-overwrite  --hive-table <db>.<table_name>  --hive-home /user/hive/warehouse
```

### Export Data to MySQL from HDFS: 
```
Source: /user/cloudera/CCA175/SQOOP/Q12/departemnts/ 
Target: MySQL table depatments_export 
Command: 
sqoop export --connect jdbc:mysql://quickstart:3306/retail_db \
--username retail_dba \
--password cloudera \
--table depatments_export \
--export-dir /user/cloudera/CCA175/SQOOP/Q12/departemnts/ \
--batch

Export updated records:
sqoop export --connect jdbc:mysql://quickstart.cloudera:3306/retail_db \
--username retail_dba \
--password cloudera \
--table depatments_export14 \
--export-dir /user/cloudera/CCA175/SQOOP/Q13/ \
--batch \
-m 1 \
--update-key department_id \
--update-mode allowinsert 
```
### Enclosed in (') single quote and separator of the field should be (~) and line needs to be terminated by : (colon) 
```
--enclosed-by \' --escaped-by \\ --fields-terminated-by ~ --lines-terminated-by : 
```

### Incremental import
- Import only new records 
```
--append --check-column "department_id" --incremental append --last-value 7
```

- Incremental import based on created_date
```
column --append --check-column create_date --incremental lastmodified --last-value "2017-04-18 07:04:16.0"
```

### Working with Sqoop Job 
- Create Sqoop job
```
sqoop job --create <job_name> --import
```
- List existing Sqoop jobs
```
sqoop job --list 
```
- View Sqoop job
```
sqoop job --show <job_name> 
```
- Execute/Run Sqoop job
```
sqoop job --exec <job_name>  
```
       
       
       
       
