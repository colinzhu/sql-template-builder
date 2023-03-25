package colinzhu.sqltemplatebuilder;

import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;

@Accessors(fluent = true)
public class SqlTemplateBuilder {
    private String orderBy = ""; // default no order by
    private String fetch = ""; // default no fetch first X rows only
    private List<String> criteria = new LinkedList<>();
    private Map<String, Object> params = new TreeMap<>();
    @Setter
    private String select;
    @Setter
    private String table;

    public String buildTemplate() {
        String actionOnTable = String.format("SELECT %s FROM %s", select, table);
        String where = criteria.size() == 0 ? "" : "WHERE " + String.join(" AND ", criteria);;
        return List.of(actionOnTable, where, orderBy, fetch).stream().filter(s -> !s.isBlank()).collect(Collectors.joining(" "));
    }
    public Map<String, Object> buildParams() {
        return params;
    }

    public <T> SqlTemplateBuilder eq(String column, T value) {
        if (value != null) {
            String key = column + "eq";
            criteria.add(String.format("%s = #{%s}", column, key));
            params.put(key, value);
        }
        return this;
    }

    public <T> SqlTemplateBuilder in(String column, Set<T> values) {
        if (values != null && values.size() > 0) {
            String key = column + "in";
            Map<String, T> inKeyValueMap = getInKeyValueMap(values, key);
            criteria.add(String.format("%s IN (%s)", column, inKeyValueMap.keySet().stream().sorted().map(k -> "#{" + k + "}").collect(Collectors.joining(","))));
            params.putAll(inKeyValueMap);
        }
        return this;
    }

    public <T> SqlTemplateBuilder gtEq(String column, T value) {
        if (value != null) {
            String key = column + "gtEq";
            criteria.add(String.format("%s >= #{%s}", column, key));
            params.put(key, value);
        }
        return this;
    }
    public <T> SqlTemplateBuilder ltEq(String column, T value) {
        if (value != null) {
            String key = column + "ltEq";
            criteria.add(String.format("%s <= #{%s}", column, key));
            params.put(key, value);
        }
        return this;
    }

    public SqlTemplateBuilder orderBy(String by) {
        if (by != null) {
            String key = "orderBy";
            orderBy = String.format("ORDER BY #{%s}", key);
            params.put(key, by);
        }
        return this;
    }

    public SqlTemplateBuilder fetchFirstX(Integer count) {
        if (count != null) {
            String key = "fetchFirstX";
            fetch = String.format("FETCH FIRST #{%s} ROWS ONLY", key);
            params.put(key, count);
        }
        return this;
    }

    private <T> Map<String, T> getInKeyValueMap(Set<T> values, String key) {
        SortedSet<T> valueSet = new TreeSet<>(values);
        Map<String, T> templateKeyValueMap = new HashMap<>();
        int i = 0;
        for (T value : valueSet) {
            templateKeyValueMap.put(key + i, value);
            i++;
        }
        return templateKeyValueMap;
    }
}