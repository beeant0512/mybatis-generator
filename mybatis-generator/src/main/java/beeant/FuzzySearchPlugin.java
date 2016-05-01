package beeant;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beeant on 2016/4/30.
 */
public class FuzzySearchPlugin extends PluginAdapter {
    private List<IntrospectedTable> tables = new ArrayList<IntrospectedTable>();

    private List<GeneratedXmlFile> sqlMaps = new ArrayList<GeneratedXmlFile>();

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        List<GeneratedXmlFile> files = new ArrayList<GeneratedXmlFile>();
        for(int i=0;i<tables.size();i++){
            files.add(generateXmlFile(sqlMaps.get(i), tables.get(i)));
        }
        return files;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        tables.add(introspectedTable);
        sqlMaps.add(sqlMap);
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }

    private GeneratedXmlFile generateXmlFile(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        Document document = new Document(
                XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        XmlElement xmlElement = new XmlElement("mapper");
        Attribute attribute;
        attribute = new Attribute("namespace", introspectedTable.getMyBatis3JavaMapperType());
        xmlElement.addAttribute(attribute);
        fuzzySearchByParamsByPager(introspectedTable, xmlElement);
        document.setRootElement(xmlElement);

        return new GeneratedXmlFile(document, sqlMap.getFileName(), sqlMap.getTargetPackage().concat(".ext"), sqlMap.getTargetProject(), false, context.getXmlFormatter());
    }

    private void fuzzySearchByParamsByPager(IntrospectedTable introspectedTable, XmlElement mapper) {
        XmlElement rootXmlElement;
        XmlElement xmlElement;
        Attribute attribute;

        rootXmlElement = new XmlElement("select");
        attribute = new Attribute("id", "fuzzySearchByParamsByPager");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("resultMap", "BaseResultMap");
        rootXmlElement.addAttribute(attribute);

        fromCondition(introspectedTable, rootXmlElement);

        xmlElement = new XmlElement("where");
        rootXmlElement.addElement(xmlElement);
        int columnsSize = introspectedTable.getAllColumns().size();
        String columnJavaTypeShortName;
        for (int i = 0; i < columnsSize; i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);

            columnJavaTypeShortName = column.getFullyQualifiedJavaType().getShortName();
            if (columnJavaTypeShortName.equals("String")) {
                fuzzyWhereCondition(xmlElement, column);
            } else if (columnJavaTypeShortName.equals("Time") ||
                    columnJavaTypeShortName.equals("Date")) {
                timeBeginCondition(xmlElement, column);
                timeEndCondition(xmlElement, column);
            } else {
                whereCondition(xmlElement, column);
            }
        }

        mapper.addElement(rootXmlElement);
    }

    private void fromCondition(IntrospectedTable introspectedTable, XmlElement rootXmlElement) {
        StringBuilder sb;
        TextElement textElement;

        textElement = new TextElement("SELECT");
        rootXmlElement.addElement(textElement);

        textElement = new TextElement("<include refid=\"Base_Column_List\" />");
        rootXmlElement.addElement(textElement);

        sb = new StringBuilder();
        sb.append("FROM ");
        sb.append(introspectedTable.getFullyQualifiedTable().getIntrospectedTableName());
        textElement = new TextElement(sb.toString());
        rootXmlElement.addElement(textElement);
    }

    private void fuzzyWhereCondition(XmlElement xmlElement, IntrospectedColumn column) {
        XmlElement andXmlElement;
        StringBuilder sb;
        String columnJavaTypeShortName;
        Attribute attribute;
        TextElement textElement;
        andXmlElement = new XmlElement("if");
        sb = new StringBuilder();
        sb.append("item.");
        sb.append(column.getJavaProperty());
        sb.append(" != null");
        columnJavaTypeShortName = column.getFullyQualifiedJavaType().getShortName();
        if (columnJavaTypeShortName.equals("String") ||
                columnJavaTypeShortName.equals("Date") ||
                columnJavaTypeShortName.equals("Time")) {
            sb.append(" and item.");
            sb.append(column.getJavaProperty());
            sb.append(" != ''");
        }
        attribute = new Attribute("test", sb.toString());
        andXmlElement.addAttribute(attribute);

        sb = new StringBuilder();
        sb.append("and ");
        sb.append(column.getActualColumnName());
        sb.append(" LIKE CONCAT(\"%\", #{item.");
        sb.append(column.getJavaProperty());
        sb.append(", jdbcType=");
        sb.append(column.getJdbcTypeName());
        sb.append("}, \"%\")");
        textElement = new TextElement(sb.toString());

        andXmlElement.addElement(textElement);
        xmlElement.addElement(andXmlElement);
    }

    private void timeBeginCondition(XmlElement xmlElement, IntrospectedColumn column) {
        XmlElement andXmlElement;
        StringBuilder sb;
        String columnJavaTypeShortName;
        Attribute attribute;
        TextElement textElement;
        andXmlElement = new XmlElement("if");
        sb = new StringBuilder();
        sb.append("item.");
        sb.append(column.getJavaProperty());
        sb.append("Begin != null");
        columnJavaTypeShortName = column.getFullyQualifiedJavaType().getShortName();
        if (columnJavaTypeShortName.equals("String") ||
                columnJavaTypeShortName.equals("Date") ||
                columnJavaTypeShortName.equals("Time")) {
            sb.append(" and item.");
            sb.append(column.getJavaProperty());
            sb.append("Begin != ''");
        }
        attribute = new Attribute("test", sb.toString());
        andXmlElement.addAttribute(attribute);

        sb = new StringBuilder();
        sb.append("and ");
        sb.append(column.getActualColumnName());
        sb.append(" &gt;= #{item.");
        sb.append(column.getJavaProperty());
        sb.append(", jdbcType=");
        sb.append(column.getJdbcTypeName());
        sb.append("}");
        textElement = new TextElement(sb.toString());

        andXmlElement.addElement(textElement);
        xmlElement.addElement(andXmlElement);
    }

    private void timeEndCondition(XmlElement xmlElement, IntrospectedColumn column) {
        XmlElement andXmlElement;
        StringBuilder sb;
        String columnJavaTypeShortName;
        Attribute attribute;
        TextElement textElement;
        andXmlElement = new XmlElement("if");
        sb = new StringBuilder();
        sb.append("item.");
        sb.append(column.getJavaProperty());
        sb.append("End != null");
        columnJavaTypeShortName = column.getFullyQualifiedJavaType().getShortName();
        if (columnJavaTypeShortName.equals("String") ||
                columnJavaTypeShortName.equals("Date") ||
                columnJavaTypeShortName.equals("Time")) {
            sb.append(" and item.");
            sb.append(column.getJavaProperty());
            sb.append("End != ''");
        }
        attribute = new Attribute("test", sb.toString());
        andXmlElement.addAttribute(attribute);

        sb = new StringBuilder();
        sb.append("and ");
        sb.append(column.getActualColumnName());
        sb.append(" &lt;= #{item.");
        sb.append(column.getJavaProperty());
        sb.append(", jdbcType=");
        sb.append(column.getJdbcTypeName());
        sb.append("}");
        textElement = new TextElement(sb.toString());

        andXmlElement.addElement(textElement);
        xmlElement.addElement(andXmlElement);
    }

    private void whereCondition(XmlElement xmlElement, IntrospectedColumn column) {
        XmlElement andXmlElement;
        StringBuilder sb;
        String columnJavaTypeShortName;
        Attribute attribute;
        TextElement textElement;
        andXmlElement = new XmlElement("if");
        sb = new StringBuilder();
        sb.append("item.");
        sb.append(column.getJavaProperty());
        sb.append(" != null");
        columnJavaTypeShortName = column.getFullyQualifiedJavaType().getShortName();
        if (columnJavaTypeShortName.equals("String") ||
                columnJavaTypeShortName.equals("Date") ||
                columnJavaTypeShortName.equals("Time")) {
            sb.append(" and item.");
            sb.append(column.getJavaProperty());
            sb.append(" != ''");
        }
        attribute = new Attribute("test", sb.toString());
        andXmlElement.addAttribute(attribute);

        sb = new StringBuilder();
        sb.append("and ");
        sb.append(column.getActualColumnName());
        sb.append(" = #{item.");
        sb.append(column.getJavaProperty());
        sb.append(", jdbcType=");
        sb.append(column.getJdbcTypeName());
        sb.append("}");
        textElement = new TextElement(sb.toString());

        andXmlElement.addElement(textElement);
        xmlElement.addElement(andXmlElement);
    }
}
