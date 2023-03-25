package colinzhu.sqltemplatebuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

class SqlTemplateBuilderTest {
    private SqlTemplateBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new SqlTemplateBuilder();
    }

    @Test
    @DisplayName("builds template with select and table")
    void testBuildTemplate() {
        builder.select("id, name").table("users");
        String expected = "SELECT id, name FROM users";
        String actual = builder.buildTemplate();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("builds template with where clause")
    void testBuildTemplateWithWhere() {
        builder.select("id, name").table("users").eq("status", "ACTIVE");
        String expected = "SELECT id, name FROM users WHERE status = #{statuseq}";
        String actual = builder.buildTemplate();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("builds template with where clause and multiple criteria")
    void testBuildTemplateWithMultipleCriteria() {
        builder.select("id, name").table("users").eq("status", "ACTIVE").gtEq("age", 18);
        String expected = "SELECT id, name FROM users WHERE status = #{statuseq} AND age >= #{agegtEq}";
        String actual = builder.buildTemplate();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("builds template with where clause and IN criteria")
    void testBuildTemplateWithIn() {
        Set<Integer> ids = new HashSet<>(Arrays.asList(1, 2, 3));
        builder.select("id, name").table("users").in("id", ids);
        String expected = "SELECT id, name FROM users WHERE id IN (#{idin0},#{idin1},#{idin2})";
        String actual = builder.buildTemplate();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("builds template with order by")
    void testBuildTemplateWithOrderBy() {
        builder.select("id, name").table("users").orderBy("name ASC");
        String expected = "SELECT id, name FROM users ORDER BY #{orderBy}";
        String actual = builder.buildTemplate();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("builds template with fetch first X rows")
    void testBuildTemplateWithFetchFirstX() {
        builder.select("id, name").table("users").fetchFirstX(10);
        String expected = "SELECT id, name FROM users FETCH FIRST #{fetchFirstX} ROWS ONLY";
        String actual = builder.buildTemplate();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("returns empty map if no params are added")
    void testBuildParamsWithNoParams() {
        Map<String, Object> expected = Collections.emptyMap();
        Map<String, Object> actual = builder.buildParams();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("returns map with one param added")
    void testBuildParamsWithOneParam() {
        builder.eq("status", "ACTIVE");
        Map<String, Object> expected = Collections.singletonMap("statuseq", "ACTIVE");
        Map<String, Object> actual = builder.buildParams();
        Assertions.assertEquals(expected, actual);
    }


    @Test
    public void testBuildParams() {
        builder.select("column1, column2")
                .table("my_table")
                .eq("column1", "value1")
                .in("column2", Set.of("value2", "value3"))
                .gtEq("column3", 10)
                .ltEq("column4", 20)
                .orderBy("column1 DESC")
                .fetchFirstX(10);

        Map<String, Object> expectedParams = new TreeMap<>();
        expectedParams.put("column1eq", "value1");
        expectedParams.put("column2in0", "value2");
        expectedParams.put("column2in1", "value3");
        expectedParams.put("column3gtEq", 10);
        expectedParams.put("column4ltEq", 20);
        expectedParams.put("orderBy", "column1 DESC");
        expectedParams.put("fetchFirstX", 10);

        Map<String, Object> actualParams = builder.buildParams();

        Assertions.assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testBuildParamsNoCriteria() {
        builder.select("column1, column2")
                .table("my_table")
                .orderBy("column1 DESC")
                .fetchFirstX(10);

        Map<String, Object> expectedParams = new TreeMap<>();
        expectedParams.put("orderBy", "column1 DESC");
        expectedParams.put("fetchFirstX", 10);

        Map<String, Object> actualParams = builder.buildParams();

        Assertions.assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testBuildParamsNoOrderBy() {
        builder.select("column1, column2")
                .table("my_table")
                .eq("column1", "value1")
                .in("column2", Set.of("value2", "value3"))
                .gtEq("column3", 10)
                .ltEq("column4", 20)
                .fetchFirstX(10);

        Map<String, Object> expectedParams = new TreeMap<>();
        expectedParams.put("column1eq", "value1");
        expectedParams.put("column2in0", "value2");
        expectedParams.put("column2in1", "value3");
        expectedParams.put("column3gtEq", 10);
        expectedParams.put("column4ltEq", 20);
        expectedParams.put("fetchFirstX", 10);

        Map<String, Object> actualParams = builder.buildParams();

        Assertions.assertEquals(expectedParams, actualParams);
    }
}