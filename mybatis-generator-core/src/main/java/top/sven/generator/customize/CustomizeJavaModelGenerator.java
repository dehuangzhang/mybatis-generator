package top.sven.generator.customize;

import lombok.Data;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import top.sven.generator.util.GeneratorUtil;
import top.sven.land.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansField;
import static org.mybatis.generator.internal.util.messages.Messages.getString;
import static top.sven.generator.constants.GeneratorConstants.MODEL_SUFFIX;

/**
 * @author liulang
 * @date 2018/10/26
 **/
public class CustomizeJavaModelGenerator extends AbstractJavaGenerator {


    public CustomizeJavaModelGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.8", table.toString()));
        Plugin plugins = context.getPlugins();
        CommentGenerator commentGenerator = context.getCommentGenerator();
        introspectedTable.setBaseRecordType(introspectedTable.getBaseRecordType() + MODEL_SUFFIX);
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        commentGenerator.addJavaFileComment(topLevelClass);
        commentGenerator.addModelClassComment(topLevelClass, introspectedTable);
        //设置属性
        List<IntrospectedColumn> introspectedColumns = GeneratorUtil.removeDefaultColumns(introspectedTable.getAllColumns());
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
            if (plugins.modelFieldGenerated(field, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }
        }
        topLevelClass.addImportedType(new FullyQualifiedJavaType(Data.class.getName()));
        FullyQualifiedJavaType baseEntityType = new FullyQualifiedJavaType(BaseEntity.class.getName());
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addImportedType(baseEntityType);
        topLevelClass.setSuperClass(baseEntityType);
        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().modelBaseRecordClassGenerated(topLevelClass,
                introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }

}
