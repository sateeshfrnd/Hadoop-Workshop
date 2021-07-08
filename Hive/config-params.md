## Print Database name hive prompt
- set hive.cli.print.current.db=true;

## Print Column Headers
- set hive.cli.print.header=true;


## Query partiton table without partition field filter conditions 
- set hive.strict.checks.no.partition.filter=false;
- set hive.mapred.mode=nonstrict;

## Resolve error while running MSCK REPAIR command
Starting with Hive 1.3, MSCK will throw exceptions if directories with disallowed characters in partition values are found on HDFS. Use hive.msck.path.validation setting on the client to alter this behavior; "skip" will simply skip the directories. "ignore" will try to create partitions anyway (old behavior). This may or may not work.
- hive.msck.path.validation=ignore;
