package top.sven.generator.plugins;

import top.sven.generator.adapter.DefaultPluginAdapter;
import top.sven.generator.util.GeneratorUtil;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.codegen.XmlConstants;

import java.util.ArrayList;
import java.util.List;

import static top.sven.generator.constants.GeneratorConstants.*;

/**
 * @author sven.zhang
 */
public class MapperPlugin extends DefaultPluginAdapter {


    @Override
    public boolean clientGenerated(Interface it, TopLevelClass topLevelClass,
                                   IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * 生成 mapper
     *
     * @param introspectedTable
     * @return
     */

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (introspectedTable.getAllColumns().contains(DEFAULT_DELETED_COLUMN)) {
            TextElement text = new TextElement(DEFAULT_DELETED_CONDITION);
            element.addElement(text);
        }
        return super.sqlMapSelectByPrimaryKeyElementGenerated(element, introspectedTable);
    }

    /**
     * 生成XXExt.xml
     */

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        String[] splitFile = introspectedTable.getMyBatis3XmlMapperFileName().split("\\.");
        String fileNameExt = null;
        if (splitFile[0] != null) {
            fileNameExt = splitFile[0] + MAPPER_POSTFIX + ".xml";
        }
        if (GeneratorUtil.isExistExtFile(context.getSqlMapGeneratorConfiguration().getTargetProject(),
                introspectedTable.getMyBatis3XmlMapperPackage(), fileNameExt)) {
            return super.contextGenerateAdditionalXmlFiles(introspectedTable);
        }

        Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);

        XmlElement root = new XmlElement("mapper");
        document.setRootElement(root);

        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        root.addAttribute(new Attribute("namespace", namespace));
        GeneratedXmlFile gxf = new GeneratedXmlFile(document, fileNameExt,
                introspectedTable.getMyBatis3XmlMapperPackage(),
                context.getSqlMapGeneratorConfiguration().getTargetProject(), false,
                context.getXmlFormatter());

        List<GeneratedXmlFile> answer = new ArrayList<>(1);
        answer.add(gxf);

        return answer;
    }
}
