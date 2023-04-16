package colinzhu.sqltemplatebuilder;

public class SqlTemplateBuilderExample {
    public static void main(String[] args) {
        selectTest();
        updateTest();
    }

    private static void selectTest() {
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

    private static void updateTest() {
        SqlTemplateBuilder builder = new SqlTemplateBuilder();
        builder.update(true)
                .table("T1")
                .set("C1", 2)
                .set("C2", "abc")
                .set("C3", null)
                .eq("C1", 1)
                .gtEq("C2", null);
        System.out.println(builder.buildTemplate());
        System.out.println(builder.buildParams());
    }

}