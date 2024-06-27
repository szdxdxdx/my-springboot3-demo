package team.project.module.auth.export.model.enums;

public enum AuthRole {
    ;
    public static final String ROLE_1 = "ROLE_1";
    public static final String ROLE_2 = "ROLE_2";
    public static final String ROLE_3 = "ROLE_3";

    /* 使用 @SaCheckRole 注解鉴权时，不要直接写字符串字面量，而是用本枚举类封装的常量 */
}
