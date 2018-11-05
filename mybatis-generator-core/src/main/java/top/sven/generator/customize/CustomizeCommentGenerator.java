package top.sven.generator.customize;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sven.zhang
 * @date 2018/10/15
 */
public class CustomizeCommentGenerator extends DefaultCommentGenerator {
    private String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass,
                                     IntrospectedTable introspectedTable) {

        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * @author " + System.getenv().get("USER"));
        topLevelClass.addJavaDocLine(" * @date " + date);
        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    public void addFieldComment(Field field,
                                IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
        field.addJavaDocLine(" */");
    }

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
        compilationUnit.addFileCommentLine("/**");
        compilationUnit.addFileCommentLine(" * @author " + System.getenv().get("USER"));
        compilationUnit.addFileCommentLine(" * @date " + date);
        compilationUnit.addFileCommentLine(" */");
    }

    @Override
    public void addRootComment(XmlElement rootElement) {
        rootElement.addElement(new TextElement("<!--" + date + " -->"));
    }

    @Override
    public void addComment(XmlElement xmlElement) {
        return;
    }
}
