package top.sven.generator.customize;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.springframework.stereotype.Repository;
import top.sven.generator.constants.GeneratorConstants;
import top.sven.generator.util.GeneratorUtil;
import top.sven.land.base.BaseMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @author liulang
 * @date 2018/10/26
 **/
public class CustomizeJavaClientGenerator extends AbstractJavaClientGenerator {
    public CustomizeJavaClientGenerator() {
        super(true);
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new CustomizeXmlMapperGenerator();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.17",
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getMyBatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);
        String baseType = introspectedTable.getBaseRecordType();
        FullyQualifiedJavaType superType = new FullyQualifiedJavaType(BaseMapper.class.getSimpleName() + "<" + GeneratorUtil.getShortName(baseType) + ">");
        interfaze.addSuperInterface(superType);
        interfaze.addAnnotation("@Repository");
        interfaze.addImportedType(new FullyQualifiedJavaType(Repository.class.getName()));
        interfaze.addImportedType(new FullyQualifiedJavaType(BaseMapper.class.getName()));
        interfaze.addImportedType(new FullyQualifiedJavaType(baseType));
        String rootInterface = introspectedTable
                .getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (!stringHasValue(rootInterface)) {
            rootInterface = context.getJavaClientGeneratorConfiguration()
                    .getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        }

        if (stringHasValue(rootInterface)) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(
                    rootInterface);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(fqjt);
        }
        List<CompilationUnit> answer = new ArrayList<>();
        String fileName = GeneratorUtil.getShortName(introspectedTable.getMyBatis3JavaMapperType()) + GeneratorConstants.JAVA_FILE_SUFFIX;
        boolean isExist = GeneratorUtil.isExistExtFile(context.getJavaClientGeneratorConfiguration().getTargetProject(),
                context.getJavaClientGeneratorConfiguration().getTargetPackage(), fileName);
        if (context.getPlugins().clientGenerated(interfaze, null,
                introspectedTable) && !isExist) {

            answer.add(interfaze);
        }

        List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
        if (extraCompilationUnits != null) {
            answer.addAll(extraCompilationUnits);
        }

        return answer;

    }

    public List<CompilationUnit> getExtraCompilationUnits() {
        return null;
    }
}
