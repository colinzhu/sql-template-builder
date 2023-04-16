# SqlTemplateBuilder

## Overview

`SqlTemplateBuilder` is a Java class that allows you to easily build SQL SELECT or UPDATE statements with dynamic criteria, ORDER BY clauses, and FETCH FIRST clauses. This can be especially useful when working with database systems that require dynamic queries with user-defined criteria.

## Usage

To use `SqlTemplateBuilder`, simply create a new instance of the class and set the `select`, `update`, `set` and `table` properties using the `select`, `update`, `set` and `table` methods, respectively. You can then chain together various criteria methods such as `eq`, `in`, `gtEq`, and `ltEq` to build up your WHERE clause. You can also set an ORDER BY clause using the `orderBy` method and a FETCH FIRST clause using the `fetchFirstX` method. Finally, you can call the `buildTemplate` method to get the resulting SQL statement and the `buildParams` method to get the parameter values to be used with the statement.

Here's an example usage:

```java
SqlTemplateBuilder builder = new SqlTemplateBuilder()
        .select("*")
        .table("employees")
        .eq("last_name", null)
        .in("department", Set.of("Sales", "Marketing"))
        .gtEq("salary", 5000)
        .orderBy("last_name ASC")
        .fetchFirstX(10);
String sql = builder.buildTemplate();
Map<String, Object> params = builder.buildParams();
```

The resulting SQL statement would be:
```oracle-sql
SELECT * FROM employees
WHERE department IN (#{departmentin0},#{departmentin1})
ORDER BY #{orderBy} ASC FETCH FIRST #{fetchFirstX} ROWS ONLY
```

And the params map would contain the following key-value pairs:
```
{departmentin0=Marketing, departmentin1=Sales, fetchFirstX=10, orderBy=last_name ASC, salarygtEq=5000}
```


```java
SqlTemplateBuilder builder = new SqlTemplateBuilder()
        .update(true)
        .table("employees")
        .set("last_name", "Flower")
        .set("first_name", null)
        .eq("last_name", null)
        .in("department", Set.of("Sales", "Marketing"))
        .gtEq("salary", 5000);
String sql = builder.buildTemplate();
Map<String, Object> params = builder.buildParams();
```

The resulting SQL statement would be:
```oracle-sql
UPDATE employees SET last_name = #{last_nameset}
WHERE department IN (#{departmentin0},#{departmentin1})
```

And the params map would contain the following key-value pairs:
```
{last_nameset=Flower, departmentin0=Marketing, departmentin1=Sales, salarygtEq=5000}
```

Example with Vert.x SqlTemplate:
```java

SqlTemplateBuilder builder = new SqlTemplateBuilder()
        .select("*")
        .table("employees")
        .eq("last_name", "Smith")
        .in("department", Set.of("Sales", "Marketing"))
        .gtEq("salary", 50000)
        .orderBy("last_name ASC")
        .fetchFirstX(10);

SqlTemplate
        .forQuery(client, builder.buildTemplate())
        .execute(builder.buildParams())
        .onSuccess(employees -> {
            employees.forEach(row -> {
                System.out.println(row.getString("last_name") + " " + row.getString("last_name"));
            });
        });
  ```