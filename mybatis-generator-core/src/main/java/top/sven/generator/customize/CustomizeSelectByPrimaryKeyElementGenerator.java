package top.sven.generator.customize;

import top.sven.generator.constants.GeneratorConstants;
import top.sven.generator.util.GeneratorUtil;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;

/**
 * @author liulang
 * @date 2018/10/27
 **/
public class CustomizeSelectByPrimaryKeyElementGenerator extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getSelectByPrimaryKeyStatementId()));

        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));

        answer.addAttribute(new Attribute("parameterType", GeneratorConstants.ID_TYPE));

        context.getCommentGenerator().addComment(answer);
        List<IntrospectedColumn> columnList = GeneratorUtil.removeNotPermitSelectColumns(introspectedTable.getAllColumns());
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        for (IntrospectedColumn column : columnList) {
            sb.append(column.getActualColumnName()).append(",");
        }
        answer.addElement(new TextElement(sb.toString().substring(0, sb.length() - 1)));
        sb.setLength(0);
        sb.append("from ");
        answer.addElement(new TextElement("from " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and ");
            } else {
                sb.append("where ");
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        if (context.getPlugins()
                .sqlMapSelectByPrimaryKeyElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
