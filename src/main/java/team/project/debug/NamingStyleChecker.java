package team.project.debug;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

// @Component /* <- 开启命名风格检测 */
final class NamingStyleChecker {
    private static final Logger log = LoggerFactory.getLogger("【命名风格检测】");

    private static final String rootPackage = "team.project";

    private static final String templatePackageName = "team.project.module._template";

    private static final String tmplPrefix = "Tmpl";

    private static final Pattern packageNamePattern  = Pattern.compile("^[a-z.]*$");
    private static final Pattern classNamePattern    = Pattern.compile("^[A-Z][a-zA-Z0-9]*$");
    private static final Pattern variableNamePattern = Pattern.compile("^[a-z][a-zA-Z0-9]*$");
    private static final Pattern constantNamePattern = Pattern.compile("^[A-Z][_A-Z0-9]*$");
    private static final Pattern methodNamePattern   = Pattern.compile("^[a-z][a-zA-Z0-9]*$");

    private static HashSet<String>     invalidPackageNames;
    private static ArrayList<String[]> invalidClassName;
    private static ArrayList<String[]> invalidTmplPrefix;
    private static ArrayList<String[]> invalidFieldName;
    private static ArrayList<String[]> invalidMethodName;
    private static ArrayList<String[]> invalidParamName;

    @PostConstruct
    private static void postConstruct() {
        Thread thread = new Thread(() -> {
            try {
                check();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    private static void prepare() {
        invalidPackageNames = new HashSet<>();
        invalidClassName    = new ArrayList<>();
        invalidTmplPrefix   = new ArrayList<>();
        invalidFieldName    = new ArrayList<>();
        invalidMethodName   = new ArrayList<>();
        invalidParamName    = new ArrayList<>();
    }

    private static void check() throws ClassNotFoundException {
        prepare();
        for (String className : getAllClass(rootPackage)) {
            checkClass(Class.forName(className));
        }
        report();
    }

    public static void report() {
        StringBuilder report = new StringBuilder();

        if ( ! invalidPackageNames.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String packageName : invalidPackageNames) {
                sb.append("\t").append(String.format("%-20s", packageName)).append("\n");
            }
            report.append("""
                包名使用全小写形式，不使用下划线分隔单词，
                请修改如下包名：
                """).append(sb);
        }

        if ( ! invalidClassName.isEmpty() || ! invalidTmplPrefix.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String[] className : invalidClassName) {
                sb.append("\t").append(String.format("%-20s", className[0]))
                    .append("\t").append(className[1]).append("\n");
            }
            for (String[] className : invalidTmplPrefix) {
                sb.append("\t").append(String.format("%-20s", className[0]))
                    .append("\t").append(className[1]).append("\n");
            }
            report.append("""
                类名使用大驼峰形式，每个单词的首字母都大写，不使用下划线分隔单词。不要使用模板包的 Tmpl 前缀，
                请修改如下类名：
                """).append(sb);
        }

        if ( ! invalidMethodName.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String[] methodAndClass : invalidMethodName) {
                sb.append("\t").append(String.format("%-20s", methodAndClass[0]))
                    .append("\t").append(methodAndClass[1]).append("\n");
            }
            report.append("""
                函数名采用小驼峰形式，第一个单词的首字母小写，其他单词的首字母都大写，不使用下划线分隔单词
                请修改如下函数名：
                """).append(sb);
        }

        if ( ! invalidParamName.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String[] paramAndMethod : invalidParamName) {
                sb.append("\t").append(String.format("%-20s", paramAndMethod[0]))
                    .append("\t").append(paramAndMethod[1]).append("\n");
            }
            report.append("""
                函数入参采用小驼峰形式，第一个单词的首字母小写，其他单词的首字母都大写，不使用下划线分隔单词
                请修改如下入参名：
                """).append(sb);
        }

        if ( ! invalidFieldName.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String[] fieldAndClass : invalidFieldName) {
                sb.append("\t").append(String.format("%-20s", fieldAndClass[0]))
                    .append("\t").append(fieldAndClass[1]).append("\n");
            }
            report.append("""
                字段名采用小驼峰形式，第一个单词的首字母小写，其他单词的首字母都大写，不使用下划线分隔单词
                常量可以采用所有单词的字母全大写，使用下划线分隔单词
                请修改如下字段名：
                """).append(sb);
        }

        if (report.length() > 0) {
            report.insert(0, "\033[31;40m");
            report.append("\033[0m\033[33m" + """
                上述命名不符合规范，请 右键点击变量名 -> 重构 -> 重命名
                提示：重构 Java 代码的变量名后，可能还要同步修改配置文件（如 xml 等）中的变量名
                """ + "\033[0m");
            log.warn(report.toString());
        }
    }

    private static List<String> getAllClass(String packageName) {
        List<String> classList = new ArrayList<>();

        URL basePathURL = Thread.currentThread().getContextClassLoader().getResource(packageName.replace('.', '/'));
        if ( basePathURL == null)
            return classList;

        File baseDirectory = new File(basePathURL.getPath());
        if ( ! baseDirectory.exists() || ! baseDirectory.isDirectory())
            return classList;

        File[] files = baseDirectory.listFiles();
        if (files == null)
            return classList;

        for (File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith(".class")) {
                classList.add( packageName + '.' + fileName.substring(0, fileName.length() - 6) );
            }
            else if (file.isDirectory()) {
                classList.addAll( getAllClass(packageName + "." + fileName) );
            }
        }

        return classList;
    }

    private static void checkPageName(String packageName) {
        if ( ! packageNamePattern.matcher(packageName).matches()) {
            invalidPackageNames.add(packageName);
        }
    }

    private static void checkClassName(Class<?> clazz) {
        String className = clazz.getName();
        String classSimpleName = clazz.getSimpleName();
        if (   ! className.contains("$")
            && ! "package-info".equals(classSimpleName)
            && ! classNamePattern.matcher(classSimpleName).matches()
        ) {
            invalidClassName.add(new String[]{classSimpleName, className});
        }
        else if (classSimpleName.startsWith(tmplPrefix)) {
            invalidTmplPrefix.add(new String[]{classSimpleName, className});
        }
    }

    private static void checkFieldName(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            if (   ! fieldName.contains("$")
                && ! variableNamePattern.matcher(fieldName).matches()
                && ! constantNamePattern.matcher(fieldName).matches()
            ) {
                invalidFieldName.add(new String[]{fieldName, clazz.getName()});
            }
        }
    }

    private static void checkMethodName(Class<?> clazz) {
        String className = clazz.getName();
        for (Method method : clazz.getDeclaredMethods()) {
            String methodName = method.getName();
            if (   ! methodName.contains("$")
                && ! methodNamePattern.matcher(methodName).matches()
            ) {
                invalidMethodName.add(new String[]{methodName + "()", className});
            }

            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                String paramName = parameter.getName();
                if (! variableNamePattern.matcher(paramName).matches()) {
                    invalidParamName.add(new String[]{paramName, className + "." + methodName + "()"});
                }
            }
        }
    }

    private static void checkClass(Class<?> clazz) {
        String packageName = clazz.getPackage().getName();

        if ( ! packageName.startsWith(rootPackage)
            || packageName.startsWith(templatePackageName))
            return;

        checkPageName(packageName);
        checkClassName(clazz);
        checkFieldName(clazz);
        checkMethodName(clazz);
    }
}
