/*
 * Find Max salary of the employee per depatment
 * Tested on Pig version 0.12
 * Author: Satish Kumar
 * Date: Dec 27th, 2014
 * Note: Probably only works with ASCII English text
 */

-- toRun Local Mode:  pig -x local -param inputPath=/home/satishkumar/HADOOP/WORKSHOP/HIVE/sample-inputs/employee_details.txt  dept_max_salary.pig 
-- toRun MR Mode:  pig -param input-path=/home/satishkumar/HADOOP/WORKSHOP/HIVE/sample-inputs/employee_details.txt  dept_max_salary.pig 

employee_data = LOAD '/home/satishkumar/HADOOP/WORKSHOP/HIVE/sample-inputs/employee_details.txt' USING PigStorage('\t') AS (id:int, name:chararray, dob:chararray, salary:int, department:chararray, address:chararray);

employee_group_by_dept = GROUP employee_data BY department;

employee_order_by_salary = FOREACH employee_group_by_dept generate group, MAX(employee_data.salary) as deptMaxSalary;
DUMP employee_order_by_salary;
