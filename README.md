# SqlTemplateBuilder

## Overview

`SqlTemplateBuilder` is a Java class that allows you to easily build SQL SELECT statements with dynamic criteria, ORDER BY clauses, and FETCH FIRST clauses. This can be especially useful when working with database systems that require dynamic queries with user-defined criteria.

## Usage

To use `SqlTemplateBuilder`, simply create a new instance of the class and set the `select` and `table` properties using the `select` and `table` methods, respectively. You can then chain together various criteria methods such as `eq`, `in`, `gtEq`, and `ltEq` to build up your WHERE clause. You can also set an ORDER BY clause using the `orderBy` method and a FETCH FIRST clause using the `fetchFirstX` method. Finally, you can call the `buildTemplate` method to get the resulting SQL statement and the `buildParams` method to get the parameter values to be used with the statement.

Here's an example usage:

```java
SqlTemplateBuilder builder = new SqlTemplateBuilder()
        .select("*")
        .table("employees")
        .eq("last_name", "Smith")
        .in("department", Set.of("Sales", "Marketing"))
        .gtEq("salary", 50000)
        .orderBy("last_name ASC")
        .fetchFirstX(10);
String sql = builder.buildTemplate();
Map<String, Object> params = builder.buildParams();
```

The resulting SQL statement would be:
```oracle-sql
SELECT * FROM employees
WHERE last_name = #{last_nameeq} AND department IN (#{departmentin0},#{departmentin1}) AND salary >= #{salarygtEq}
ORDER BY #{orderBy} ASC FETCH FIRST #{fetchFirstX} ROWS ONLY
```

And the params map would contain the following key-value pairs:
```json
{
  "last_nameeq": "Smith",
  "departmentin0": "Sales",
  "departmentin1": "Marketing",
  "salarygtEq": 50000,
  "orderBy": "last_name ASC",
  "fetchFirstX": 10
}
```