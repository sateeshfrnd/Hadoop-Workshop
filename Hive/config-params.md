## Print Database name hive prompt
- set hive.cli.print.current.db=true;

## Print Column Headers
- set hive.cli.print.header=true;


## Query partiton table without partition field filter conditions 
- set hive.strict.checks.no.partition.filter=false;
- set hive.mapred.mode=nonstrict;
