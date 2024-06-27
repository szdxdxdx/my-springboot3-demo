package team.project.base.controller.queryparam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，以支持使用对象来接收查询参数
 * */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParam {
}



/* 2024-05-11 szdxdxdx

下文将介绍：

    1. 为什么要定义这个注解？
    2. 如何使用此注解？       <- ! important
    3. 这个注解是如何工作的？
    4. 这个注解有什么缺陷？    <- ! important

==========================



------------
什么是查询参数？
------------

查询参数（Query parameters）指的是， URL 中跟在 ”?“ 后面的”key=value“键值对形式的参数



--------------------
1. 为什么要定义这个注解？
--------------------

目前 spring-boot 没有提供合适的注解来支持“用对象接收多个‘查询参数’”

考虑下述情形：

GET 请求的 api 中有多个小写下划线风格的查询参数：
    xxx?user_name=xxx&user_age=xxx&user_email=xxx

要保持 java 代码中变量名使用小驼峰的命名风格，需要使用 @RequestParam 来指定别名：
    GetMapping("xxx")
    Object api(
        @RequestParam("user_name")  String userName,
        @RequestParam("user_age")   Integer userAge,
        @RequestParam("user_email") String userEmail
    ) {
        ...
    }

如果希望用一个类来封装这些参数，并修改 controller 的代码：
    class UserInfoReq {
        String  userName;
        Integer userAge;
        String  userEmail;
    }

    GetMapping( "xxx?user_name=xxx&user_age=xxx&user_email=xxx" )
    Object api(UserInfoReq req) {
        ...
    }

此时，不能再使用 @RequestParam 指定别名：
    class UserInfoReq {
        @RequestParam("user_name")  String  userName; // <- 编译报错，@RequestParam 不能修饰类字段：
        @RequestParam("user_age")   Integer userAge;
        @RequestParam("user_email") String  userEmail;
    }

    GetMapping( "xxx?user_name=xxx&user_age=xxx&user_email=xxx" )
    Object api(@RequestParam("xxx") UserInfoReq req) { // <- 无效，无法指定类内字段的别名
        ...
    }

这里不应使用 @JsonProperty 和 @RequestBody，因为：
    @RequestBody 要求入参来自请求体，而不是 URL 中的查询参数
    而 HTTP 协议未定义 GET 请求的 body 语义，部分浏览器不支持 GET 请求携带请求体
    软件工程中有一条原则：不要依赖未定义的行为

    class UserInfoReq {
        @JsonProperty("user_name")  String  userName;  // <- @JsonProperty 指定 json 序列化和反序列化的别名，是有效的
        @JsonProperty("user_age")   Integer userAge;
        @JsonProperty("user_email") String  userEmail;
    }

    GetMapping( "xxx?user_name=xxx&user_age=xxx&user_email=xxx" )
    Object api(@RequestBody UserInfoReq req) { // <- 但是 @RequestBody 要求入参来自请求体，而不是 URL 中的查询参数
        ...
    }

另外的，使用 @ModelAttribute 注解也不能解决问题，因为它不能和 @JsonProperty 配合使用

如果为了迎合 api 将字段改成小写下划线风格，则不符合 java 代码规范：
    class UserInfoReq {
        String  user_name;
        Integer user_age;
        String  user_email;
    }

所以，需要自定义新的注解，以支持使用对象来接收查询参数



---------------
2. 如何使用此注解？
---------------

定义请求包装类来封装入参，类字段支持 @JsonProperty 起别名，也支持 jsr303 校验：
    class UserInfoReq {
        @JsonProperty("user_name")  @NotBlank
        String userName;
        @JsonProperty("user_age")   @Min(0)
        Integer userAge;
        @JsonProperty("user_email") @Email
        String userEmail;
    }

controller 中使用此注解修饰形参：
    GetMapping("xxx")
    Object api( @Valid @QueryParam UserInfoReq req ) {
        ...
    }

注意，使用 @Valid 开启校验



--------------------
3. 这个注解是如何工作的？
--------------------

详见 QueryParamResolver.java



-------------------
4. 这个注解有什么缺陷？
-------------------

不支持数据结构、不支持类嵌套！
    class ClassA {
        @JsonProperty("l") long    l;   // <- 支持基本的数据类型
        @JsonProperty("i") Integer i;   // <- 支持基本的数据类型（包装类）
    }

    class ClassB {
        @JsonProperty("lst") List<Integer> lst; // <- 不支持数据结构
        @JsonProperty("a")   ClassA a;          // <- 不支持类嵌套
    }

如果你希望这个注解支持类嵌套、支持数据结构，那请你去建设它
详见 QueryParamResolver.java 的 parseInputValueToJavaObject() 方法



*/
