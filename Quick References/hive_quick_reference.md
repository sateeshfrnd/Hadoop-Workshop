### Calculate Date difference in Days
```
datediff(to_date(String timestamp), to_date(String timestamp))

For Example:
SELECT datediff(to_date('2018-08-03'), to_date('2018-08-01')) <= 2;
```

### Calculate Date difference with seconds
```
If you need the difference in seconds (i.e.: you're comparing dates with timestamps, and not whole days), you can simply convert two date or timestamp strings in the format 'YYYY-MM-DD HH:MM:SS' (or specify your string date format explicitly) using unix_timestamp(), and then subtract them from each other to get the difference in seconds. (And can then divide by 60.0 to get minutes, or by 3600.0 to get hours, etc.)

For Example:
-- This will return 1800 (seconds). Unix_timestamp converts string dates into BIGINTs. 
UNIX_TIMESTAMP('2018-02-05 10:00:00') - UNIX_TIMESTAMP('2018-12-02 10:30:00') AS time_diff 
```
### Get previous date for the given date
*date_sub* will give the previous day, but if given date in 'yyyy-MM-dd' format.
Syntax:
```
date_sub(String date, Int days)
```

If the given date is in different format, then need to first convert your current date format into yyyy-MM-dd format.
```
SELECT date_sub(from_unixtime(unix_timestamp('20190902','yyyyMMdd'),'yyyy-MM-dd'),1) as previous_day;
```

If you need the date format as yyyyMMdd, you can apply regex_replace function to remove the '-', as below:
```
select regexp_replace(date_sub(from_unixtime(unix_timestamp('20190902','yyyyMMdd'),'yyyy-MM-dd'),1),'-','') as previous_day_yyyymmdd;
```
