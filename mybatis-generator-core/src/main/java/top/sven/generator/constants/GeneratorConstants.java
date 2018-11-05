package top.sven.generator.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @author liulang
 * @date 2018/10/27
 **/
public interface GeneratorConstants {
    /**
     * 主键类型
     */
     String ID_TYPE = "java.lang.Integer";
    /**
     * 主键列
     */
     String ID = "id";

    /**
     * 是否删除 列名
     */
     String DEFAULT_DELETED_COLUMN = "is_deleted";

    /**
     * 修改人 列名
     */
     String DEFAULT_MODIFIER_COLUMN = "modifier";

    /**
     * 创建人 列名
     */
     String DEFAULT_CREATOR_COLUMN = "modifier";

    /**
     * 创建时间 列名
     */
     String DEFAULT_CREATE_TIME_COLUMN = "create_time";

    /**
     * 修改时间 列名
     */
     String DEFAULT_UPDATE_TIME_COLUMN = "update_time";

    /**
     * 默认不删除删除 条件
     */
     String DEFAULT_DELETED_CONDITION = " and " + DEFAULT_DELETED_COLUMN + " = 0 ";
    /**
     * 不允许查询列
     */
     List<String> DEFAULT_COLUMN_LIST = Arrays.asList(ID, DEFAULT_DELETED_COLUMN, DEFAULT_CREATE_TIME_COLUMN, DEFAULT_UPDATE_TIME_COLUMN, DEFAULT_CREATOR_COLUMN, DEFAULT_MODIFIER_COLUMN);

    /**
     * 不允许查询列
     */
     List<String> NOT_PERMIT_SELECT_COLUMN_LIST = Arrays.asList(DEFAULT_DELETED_COLUMN, DEFAULT_CREATE_TIME_COLUMN, DEFAULT_UPDATE_TIME_COLUMN, DEFAULT_CREATOR_COLUMN, DEFAULT_MODIFIER_COLUMN);
    /**
     * 不允许更新列集合
     */
     List<String> NOT_PERMIT_UPDATE_COMMON_LIST = Arrays.asList(DEFAULT_CREATE_TIME_COLUMN, DEFAULT_CREATOR_COLUMN, DEFAULT_DELETED_COLUMN);
    /**
     * model 后缀名
     */
     String MODEL_SUFFIX = "Entity";
    /**
     * java文件后缀
     */
     String JAVA_FILE_SUFFIX = ".java";
    /**
     * mapper后缀
     */
    String MAPPER_POSTFIX = "Ext";
    /**
     * service文件后缀
     */
    String SERVICE_SUFFIX = "Service";
    /**
     * service实现类文件后缀
     */
    String SERVICE_IMPL_SUFFIX = "Impl";
    /**
     * view 文件后缀
     */
    String VIEW_SUFFIX = "View";
    /**
     * listView后缀
     */
    String LIST_VIEW_SUFFIX = "ListView";

    /**
     * 文件路径属性名
     */
    String TARGET_PROJECT = "targetProject";
    /**
     * 文件包属性名
     */
    String TARGET_PACKAGE = "targetPackage";


}
