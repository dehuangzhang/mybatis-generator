package top.sven.generator.util;


import org.mybatis.generator.api.IntrospectedColumn;
import top.sven.generator.constants.GeneratorConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * @author sven.zhang
 * @since 2018/9/2
 */
public class GeneratorUtil {

    private static final String UNDERLINE = "_";

    public static String toHumpStructure(String name) {
        return toHumpStructure(true, name);
    }

    /**
     * 将表名/列名转成驼峰结构
     *
     * @param tableName
     * @return
     */
    public static String toHumpStructure(boolean firstUpperCase, String tableName) {
        if (tableName == null) {
            return "";
        }
        if (!tableName.contains(UNDERLINE)) {
            return tableName;
        }
        String[] array = tableName.split("_");
        StringBuilder builder = new StringBuilder(array[0]);
        for (int i = 1; i < array.length; i++) {
            char[] cs = array[i].toCharArray();
            cs[0] -= 32;
            builder.append(String.valueOf(cs));
        }
        if (firstUpperCase) {
            char[] cs = builder.toString().toCharArray();
            cs[0] -= 32;
            return new String(cs);
        }
        return builder.toString();
    }

    /**
     * 获取类名，不带包
     *
     * @param className
     * @return
     */
    public static String getShortName(String className) {
        if (className == null) {
            return null;
        }
        return className.substring(className.lastIndexOf(".") + 1);
    }

    /**
     * 判断文件是否已经生成
     *
     * @param targetProject
     * @param targetPackage
     * @param fileName
     * @return
     */
    public static boolean isExistExtFile(String targetProject, String targetPackage, String fileName) {

        File project = new File(targetProject);
        if (!project.isDirectory()) {
            return true;
        }

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, ".");
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                return true;
            }
        }

        File testFile = new File(directory, fileName);
        if (testFile.exists()) {
            return true;
        }
        return false;
    }


    public static List<IntrospectedColumn> removeIdColumn(List<IntrospectedColumn> columns) {
        List<IntrospectedColumn> filteredList = new ArrayList<IntrospectedColumn>();
        for (IntrospectedColumn ic : columns) {
            if (!Objects.equals(GeneratorConstants.ID, ic.getActualColumnName())) {
                filteredList.add(ic);
            }
        }
        return filteredList;
    }

    public static List<IntrospectedColumn> removeDefaultColumns(List<IntrospectedColumn> columns) {
        List<IntrospectedColumn> filteredList = new ArrayList<IntrospectedColumn>();
        for (IntrospectedColumn ic : columns) {
            if (!GeneratorConstants.DEFAULT_COLUMN_LIST.contains(ic.getActualColumnName())) {
                filteredList.add(ic);
            }
        }
        return filteredList;
    }

    public static List<IntrospectedColumn> removeNotPermitSelectColumns(List<IntrospectedColumn> columns) {
        List<IntrospectedColumn> filteredList = new ArrayList<IntrospectedColumn>();
        for (IntrospectedColumn ic : columns) {
            if (!GeneratorConstants.NOT_PERMIT_SELECT_COLUMN_LIST.contains(ic.getActualColumnName())) {
                filteredList.add(ic);
            }
        }
        return filteredList;
    }

    /**
     * 移除不允许更新的列
     *
     * @param columns
     * @return
     */
    public static List<IntrospectedColumn> removeNotPermitUpateColumns(List<IntrospectedColumn> columns) {
        List<IntrospectedColumn> filteredList = new ArrayList<IntrospectedColumn>();
        for (IntrospectedColumn ic : columns) {
            if (!GeneratorConstants.NOT_PERMIT_UPDATE_COMMON_LIST.contains(ic.getActualColumnName())) {
                filteredList.add(ic);
            }
        }
        return filteredList;
    }
}
