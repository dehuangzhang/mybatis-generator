package top.sven.generator.adapter;

import top.sven.generator.util.GeneratorUtil;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.PropertyRegistry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static top.sven.generator.util.GeneratorUtil.*;

/**
 * @author liulang
 * @date 2018/10/21
 **/
public class DefaultPluginAdapter extends PluginAdapter {
    protected List<GeneratedJavaFile> javaFileList = new ArrayList<>(4);
    protected static String entityName;
    protected static String baseTypeName;
    protected static String shortName;
    protected static String viewName;
    protected static String listViewName;
    protected static FullyQualifiedJavaType viewType;
    protected String currentDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        baseTypeName = toHumpStructure(introspectedTable.getFullyQualifiedTable().getIntrospectedTableName());
        entityName = introspectedTable.getBaseRecordType();
        shortName = getShortName(entityName);
        return true;
    }


    /**
     * 生成文件
     *
     * @param compilationUnit
     * @param targetProject
     */
    protected void generateJavaFile(CompilationUnit compilationUnit,
                                    String targetProject) {
        GeneratedJavaFile file = new GeneratedJavaFile(compilationUnit, targetProject,
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());

        if (GeneratorUtil.isExistExtFile(targetProject, file.getTargetPackage(), file.getFileName())) {
            return;
        }
        javaFileList.add(file);
    }


    @Override
    public boolean validate(List<String> warnings) {

        return true;
    }
}
