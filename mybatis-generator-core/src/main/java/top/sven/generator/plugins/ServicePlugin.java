package top.sven.generator.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import top.sven.generator.adapter.DefaultPluginAdapter;
import top.sven.land.base.BaseService;
import top.sven.land.base.BaseServiceImpl;

import java.util.List;

import static top.sven.generator.constants.GeneratorConstants.*;
import static top.sven.generator.util.GeneratorUtil.getShortName;

/**
 * @author liulang
 * @date 2018/10/13
 */
public class ServicePlugin extends DefaultPluginAdapter {


    /**
     * 生成XXService.java和**ServiceImpl.java
     * <p>
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        String servicePath = properties.getProperty(TARGET_PROJECT);
        String servicePackage = this.properties.getProperty(TARGET_PACKAGE);
        String serviceName = baseTypeName + SERVICE_SUFFIX;
        if (viewType == null) {
            throw new RuntimeException("请将ViewPluginAdapter配置在servicePluginAdapter之前");
        }
        FullyQualifiedJavaType serviceType = new FullyQualifiedJavaType(servicePackage + "." + serviceName);
        this.generateServiceInterfaceFile(serviceType, servicePath);

        FullyQualifiedJavaType implType = new FullyQualifiedJavaType(servicePackage + ".impl." + serviceName
                + SERVICE_IMPL_SUFFIX);
        this.generateServiceImplFile(implType, serviceType, servicePath, introspectedTable);
        return javaFileList;
    }

    /**
     * 生成 service 接口类
     *
     * @param serviceType
     * @param servicePath
     */
    private void generateServiceInterfaceFile(FullyQualifiedJavaType serviceType, String servicePath) {
        Interface it = new Interface(serviceType);
        it.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType superType = new FullyQualifiedJavaType(BaseService.class.getSimpleName() + "<" + shortName + "," + viewName + ">");
        it.addSuperInterface(superType);
        it.addImportedType(viewType);
        it.addImportedType(new FullyQualifiedJavaType(BaseService.class.getName()));
        it.addImportedType(new FullyQualifiedJavaType(entityName));
        it.addJavaDocLine(" /**\n" +
                " * @author " + System.getenv().get("USER") + "\n" +
                " * @currentDate " + currentDate + "\n */"
        );
        this.generateJavaFile(it, servicePath);
    }

    /**
     * 生成serviceImpl 文件
     *
     * @param implType
     * @param superType
     * @param servicePath
     * @param introspectedTable
     */
    private void generateServiceImplFile(FullyQualifiedJavaType implType, FullyQualifiedJavaType superType, String servicePath, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        String mapperName = getShortName(introspectedTable.getMyBatis3JavaMapperType());
        TopLevelClass clazz = new TopLevelClass(implType);
        clazz.addAnnotation("@Service");
        clazz.addImportedType("org.springframework.stereotype.Service");
        clazz.addAnnotation("@Slf4j");
        clazz.addImportedType("lombok.extern.slf4j.Slf4j");
        clazz.addImportedType(superType);
        clazz.addSuperInterface(superType);
        clazz.addImportedType(BaseServiceImpl.class.getName());
        clazz.addImportedType(entityName);
        clazz.addImportedType(mapperType);
        clazz.addImportedType(viewType);
        clazz.setSuperClass(new FullyQualifiedJavaType(BaseServiceImpl.class.getName() + "<" + shortName + "," + viewName + "," + mapperName + ">"));
        clazz.setVisibility(JavaVisibility.PUBLIC);
        clazz.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        Field field = new Field(Character.toLowerCase(mapperName.charAt(0)) + mapperName.substring(1), mapperType);
        field.addAnnotation("@Autowired");
        field.setVisibility(JavaVisibility.PRIVATE);
        clazz.addField(field);
        context.getCommentGenerator().addModelClassComment(clazz, introspectedTable);
        this.generateJavaFile(clazz, servicePath);
    }
}
