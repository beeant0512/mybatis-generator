package mybatis.plugins;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getSetterMethodName;

/**
 * Created by Beeant on 2016/6/11.
 */
public class FuzzySearchPlugin extends PluginAdapter {
    private List<IntrospectedColumn> keyColumns = new ArrayList<IntrospectedColumn>();
    private String suffix;
    private Boolean pager;
    private String targetPackage;
    private FullyQualifiedJavaType pageBounds;
    private FullyQualifiedJavaType pageList;


    @Override
    public boolean validate(List<String> warnings) {
        suffix = properties.getProperty("suffix");
        pager = Boolean.valueOf(properties.getProperty("pager"));
        targetPackage = properties.getProperty("targetPackage");
        pageList = new FullyQualifiedJavaType(context.getProperty("pageList"));
        pageBounds = new FullyQualifiedJavaType(context.getProperty("pageBounds"));
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (null != pager && pager) {
            addFuzzyMethod(interfaze, introspectedTable, true);
        }
        addFuzzyMethod(interfaze, introspectedTable, false);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    private void addFuzzyMethod(Interface interfaze, IntrospectedTable introspectedTable, boolean pager){
        // 模糊搜索
        Method method;
        Parameter parameter;
        FullyQualifiedJavaType baseRecordType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        FullyQualifiedJavaType methodReturn = FullyQualifiedJavaType.getNewListInstance();
        if (pager){
            method = new Method("fuzzySearchByPager");

            methodReturn = new FullyQualifiedJavaType(context.getProperty("pageList"));

            parameter = new Parameter(pageBounds, "pageBounds");
            parameter.addAnnotation("@Param(\"pageBounds\")");
            method.addParameter(parameter);

            interfaze.addImportedType(pageList);
            interfaze.addImportedType(pageBounds);
        } else {
            method = new Method("fuzzySearch");
        }

        parameter = new Parameter(baseRecordType, "record");
        parameter.addAnnotation("@Param(\"item\")");
        method.addParameter(parameter);

        methodReturn.addTypeArgument(baseRecordType);
        method.setReturnType(methodReturn);

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        interfaze.addMethod(method);
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String jdbcType;
        jdbcType = introspectedColumn.getJdbcTypeName();
        if (jdbcType.equals("TIMESTAMP") ||
                jdbcType.equals("DATE")) {
            addModelFiled(topLevelClass, introspectedColumn.getJavaProperty().concat("Begin"), introspectedColumn.getFullyQualifiedJavaType(), introspectedTable);
            addModelFiled(topLevelClass, introspectedColumn.getJavaProperty().concat("End"), introspectedColumn.getFullyQualifiedJavaType(), introspectedTable);
        }

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    private void addModelFiled(TopLevelClass topLevelClass, String fieldName, FullyQualifiedJavaType fieldType, IntrospectedTable introspectedTable) {
        Field field = new Field();
        field.setName(fieldName);
        field.setType(fieldType);
        field.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(field);
        context.getCommentGenerator().addFieldComment(field, introspectedTable);
        Method method = getGetterMethod(field, introspectedTable);
        topLevelClass.addMethod(method);
        method = getSetterMethod(field, introspectedTable);
        topLevelClass.addMethod(method);
    }

    private Method getGetterMethod(Field property, IntrospectedTable introspectedTable) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(property.getType());
        method.setName(getGetterMethodName(property.getName(), property.getType()));
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        StringBuilder sb = new StringBuilder();
        sb.append("return "); //$NON-NLS-1$
        sb.append(property.getName());
        sb.append(';');
        method.addBodyLine(sb.toString());

        return method;
    }

    private Method getSetterMethod(Field field, IntrospectedTable introspectedTable) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(getSetterMethodName(field.getName()));
        method.addParameter(new Parameter(field.getType(), field.getName()));
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        StringBuilder sb = new StringBuilder();

        sb.append("this."); //$NON-NLS-1$
        sb.append(field.getName());
        sb.append(" = "); //$NON-NLS-1$
        sb.append(field.getName());
        sb.append(';');
        method.addBodyLine(sb.toString());

        return method;
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        if (null != suffix && !"".equals(suffix)) {
            List<GeneratedXmlFile> files = new ArrayList<GeneratedXmlFile>();
            files.add(generateXmlFile(introspectedTable));
            return files;
        }
        return super.contextGenerateAdditionalXmlFiles(introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        generateFuzzyElement(introspectedTable, document.getRootElement());
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private GeneratedXmlFile generateXmlFile(IntrospectedTable introspectedTable) {
        Document document = new Document(
                XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);

        XmlElement rootElement = new XmlElement("mapper");
        Attribute attribute;
        attribute = new Attribute("namespace", introspectedTable.getMyBatis3JavaMapperType());
        rootElement.addAttribute(attribute);

        generateFuzzyElement(introspectedTable, rootElement);

        document.setRootElement(rootElement);

        String targetProject = context.getSqlMapGeneratorConfiguration().getTargetProject();
        String filename = introspectedTable.getFullyQualifiedTable().getDomainObjectName() + "Mapper" + suffix + ".xml";
        return new GeneratedXmlFile(document, filename, targetPackage, targetProject, true, context.getXmlFormatter());
    }

    private void generateFuzzyElement(IntrospectedTable introspectedTable, XmlElement rootElement) {
        if (null != pager && pager) {
            fuzzySearch(introspectedTable, rootElement, true);
        }
        fuzzySearch(introspectedTable, rootElement, false);
    }

    private void fuzzySearch(IntrospectedTable introspectedTable, XmlElement mapper, boolean pager) {
        XmlElement rootXmlElement;
        XmlElement xmlElement;
        Attribute attribute;

        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            keyColumns.add(column);
        }

        rootXmlElement = new XmlElement("select");
        context.getCommentGenerator().addComment(rootXmlElement);
        if (pager) {
            attribute = new Attribute("id", "fuzzySearchByPager");
        } else {
            attribute = new Attribute("id", "fuzzySearch");
        }
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("resultMap", "BaseResultMap");
        rootXmlElement.addAttribute(attribute);

        fromCondition(introspectedTable, rootXmlElement);

        xmlElement = new XmlElement("where");
        rootXmlElement.addElement(xmlElement);
        int columnsSize = introspectedTable.getAllColumns().size();
        String columnJdbcTypeName;
        for (int i = 0; i < columnsSize; i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);

            columnJdbcTypeName = column.getJdbcTypeName();
            if (columnJdbcTypeName.equals("VARCHAR") && !isKeyColumn(column)) {
                fuzzyWhereCondition(xmlElement, column);
            } else {
                whereCondition(xmlElement, column);
            }
            if (columnJdbcTypeName.equals("TIMESTAMP") ||
                    columnJdbcTypeName.equals("DATE")) {
                timeBeginCondition(xmlElement, column);
                timeEndCondition(xmlElement, column);
            }
        }

        mapper.addElement(rootXmlElement);
    }

    private boolean isKeyColumn(IntrospectedColumn column) {
        for (IntrospectedColumn key : keyColumns) {
            if (key.getActualColumnName().equals(column.getActualColumnName())) {
                return true;
            }
        }

        return false;
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
        sb.append("Begin, jdbcType=");
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
        sb.append("End, jdbcType=");
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
