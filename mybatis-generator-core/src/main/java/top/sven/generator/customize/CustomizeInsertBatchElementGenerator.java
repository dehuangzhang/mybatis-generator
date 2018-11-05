package top.sven.generator.customize;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;
import java.util.Objects;

/**
 * @author liulang
 * @date 2018/10/26
 **/
public class CustomizeInsertBatchElementGenerator extends
        AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement insert = new XmlElement("insert");
        insert.addAttribute(new Attribute("id", "insertBatch"));
        insert.addAttribute(new Attribute("keyProperty", "id"));
        insert.addAttribute(new Attribute("useGeneratedKeys", "true"));
        insert.addAttribute(new Attribute("parameterType", "java.util.List"));
        insert.addElement(new TextElement(
                " INSERT INTO " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        StringBuilder builder = new StringBuilder();
        List<IntrospectedColumn> columns = introspectedTable.getNonPrimaryKeyColumns();
        for (IntrospectedColumn column : columns) {
            builder.append(column.getActualColumnName() + ",");
        }
        XmlElement trimElement = addTrimElement();
        trimElement.addElement(new TextElement(builder.toString()));
        insert.addElement(trimElement);
        insert.addElement(new TextElement(" values"));
        addForeachElement(columns, insert);
        parentElement.addElement(insert);
    }

    private void addIfElement(XmlElement parent, IntrospectedColumn column) {
        String property = column.getJavaProperty();
        XmlElement element = new XmlElement("if");
        element.addAttribute(new Attribute("test", "item." + property + " == null"));
        element.addElement(new TextElement(getDefaultValue(column) + ","));
        parent.addElement(element);

        String text = "#{item." + property + ",jdbcType=" + column.getJdbcTypeName() + "},";
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "item." + property + " != null"));
        ifElement.addElement(new TextElement(text));
        parent.addElement(ifElement);

    }

    private String getDefaultValue(IntrospectedColumn column) {
        String defaultValue = column.getDefaultValue();
        if (defaultValue != null && !Objects.equals(defaultValue, "")) {
            return defaultValue;
        }

        String type = column.getFullyQualifiedJavaType().getShortName();
        switch (type) {
            case "Integer":
                defaultValue = "0";
                break;
            case "Long":
                defaultValue = "0";
                break;
            case "BigDecimal":
                defaultValue = "0.00";
                break;
            case "Date":
                defaultValue = "now()";
                break;
            default:
                defaultValue = "\'\'";
        }
        return defaultValue;
    }

    private XmlElement addTrimElement() {
        XmlElement element = new XmlElement("trim");
        element.addAttribute(new Attribute("prefix", "("));
        element.addAttribute(new Attribute("suffix", ")"));
        element.addAttribute(new Attribute("suffixOverrides", ","));
        return element;
    }

    /**
     * 添加foreach 元素
     *
     * @param columns
     * @param insertBatch
     */
    private void addForeachElement(List<IntrospectedColumn> columns, XmlElement insertBatch) {
        XmlElement foreach = new XmlElement("foreach");
        foreach.addAttribute(new Attribute("collection", "list"));
        foreach.addAttribute(new Attribute("item", "item"));
        foreach.addAttribute(new Attribute("separator", ","));
        XmlElement trimElement = addTrimElement();
        for (IntrospectedColumn column : columns) {
            addIfElement(trimElement, column);
        }
        foreach.addElement(trimElement);
        insertBatch.addElement(foreach);
    }
}
