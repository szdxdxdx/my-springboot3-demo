package team.project.base.exceptionhandler.order;

/* 指定异常处理器指定优先级，数值越小优先级越高 */
public class ExceptionHandlerOrder {
    static public final int springframeworkExceptionHandler = 1;
    static public final int saTokenExceptionHandler         = 2;
    static public final int customExceptionHandler          = 3;
    static public final int mybatisPlusExceptionHandler     = 4;
    static public final int generalExceptionHandler         = 5; /* <- 保持最低优先级 */
}
