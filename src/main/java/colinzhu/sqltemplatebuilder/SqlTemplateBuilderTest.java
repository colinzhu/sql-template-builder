package colinzhu.sqltemplatebuilder;

public class SqlTemplateBuilderTest {
    public static void main(String[] args) {
        SqlTemplateBuilder builder = new SqlTemplateBuilder();
        builder.select("*")
                .table("T1")
                .eq("C1", 1)
                .gtEq("C2", null)
                .orderBy("C1 DESC")
                .fetchFirstX(null);
        System.out.println(builder.buildTemplate());
        System.out.println(builder.buildParams());
    }

}