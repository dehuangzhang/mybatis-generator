package top.sven.generator.plugins;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import top.sven.generator.adapter.DefaultPluginAdapter;
import top.sven.land.base.BaseView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static top.sven.generator.constants.GeneratorConstants.*;
import static top.sven.generator.util.GeneratorUtil.getShortName;

/**
 * view类与listView类生成
 *
 * @author liulang
 * @date 2018/10/21
 **/
public class ViewPlugin extends DefaultPluginAdapter {
    private String serialId = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());


    /**
     * 生成view文件
     * <p>
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        String viewPath = properties.getProperty(TARGET_PROJECT);
        String viewPackage = this.properties.getProperty(TARGET_PACKAGE);
        //生成view对象
        this.generateView(viewPath, viewPackage, introspectedTable);
        return javaFileList;
    }

    /**
     * 生成view对象
     *
     * @param viewPath
     * @param viewPackage
     * @param introspectedTable
     */
    private void generateView(String viewPath, String viewPackage, IntrospectedTable introspectedTable) {
        viewName = baseTypeName + VIEW_SUFFIX;
        viewType = new FullyQualifiedJavaType(viewPackage + "." + viewName);
        TopLevelClass clazz = new TopLevelClass(viewType);
        addDefaultAnnotation(clazz);
        clazz.setSuperClass(new FullyQualifiedJavaType(getShortName(BaseView.class.getName())));
        clazz.addImportedType(BaseView.class.getName());
        clazz.setVisibility(JavaVisibility.PUBLIC);
        addSerialField(clazz);
        context.getCommentGenerator().addModelClassComment(clazz, introspectedTable);
        this.generateJavaFile(clazz, viewPath);
    }

    /**
     * 添加默认的注解
     *
     * @param clazz
     */
    private void addDefaultAnnotation(TopLevelClass clazz) {
        clazz.addAnnotation("@Data");
        clazz.addImportedType(Data.class.getName());
        clazz.addAnnotation("@JsonInclude(value = JsonInclude.Include.NON_NULL)");
        clazz.addAnnotation("@ApiModel(value=\"" + clazz.getType().getShortName() + "\", description = \"\")");
        clazz.addImportedType(JsonInclude.class.getName());
        clazz.addImportedType(ApiModel.class.getName());
    }

    /**
     * @param clazz
     */
    private void addSerialField(TopLevelClass clazz) {
        Field field = new Field();
        field.setFinal(true);
        field.setStatic(true);
        field.setName("serialVersionUID");
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setInitializationString(serialId + new Random().nextInt(100) + "L");
        field.setType(new FullyQualifiedJavaType("long"));
        clazz.addField(field);
    }


}
