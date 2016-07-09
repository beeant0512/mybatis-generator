package mybatis.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beeant on 2016/7/9.
 */
public class NoExampleRepositoryPlugin extends PluginAdapter {
    private List<IntrospectedColumn> keyColumns = new ArrayList<IntrospectedColumn>();
    private String countByExampleName = "";
    private String deleteByExampleName = "";
    private String selectByExampleWithBLOBsName = "";
    private String selectByExampleWithoutBLOBsName = "";
    private String updateByExampleSelectiveName = "";
    private String updateByExampleWithBLOBsName = "";
    private String updateByExampleWithoutBLOBsName = "";

    @Override
    public boolean validate(List<String> warnings) {
        countByExampleName = "";
        deleteByExampleName = "";
        selectByExampleWithBLOBsName = "";
        selectByExampleWithoutBLOBsName = "";
        updateByExampleSelectiveName = "";
        updateByExampleWithBLOBsName = "";
        updateByExampleWithoutBLOBsName = "";
        return true;
    }


    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        remethod(method, interfaze, introspectedTable);
        return false;
    }

    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        remethod(method, topLevelClass, introspectedTable);
        return false;
    }

    @Override
    public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        remethod(method, interfaze, introspectedTable);
        return false;
    }

    @Override
    public boolean clientCountByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        remethod(method, topLevelClass, introspectedTable);
        return false;
    }


    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        remethod(method, interfaze, introspectedTable);
        addByPagerMethod(method, interfaze, introspectedTable);
        return false;
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        remethod(method, topLevelClass, introspectedTable);

        return false;
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        remethod(method, interfaze, introspectedTable);
        addByPagerMethod(method, interfaze, introspectedTable);
        return false;
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        remethod(method, topLevelClass, introspectedTable);
        return false;
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        remethod(method, interfaze, introspectedTable);
        return false;
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        remethod(method, topLevelClass, introspectedTable);
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        remethod(method, interfaze, introspectedTable);
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.clientUpdateByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        remethod(method, interfaze, introspectedTable);
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        remethod(method, topLevelClass, introspectedTable);
        return false;
    }

    private void remethod(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType objectFqjt = new FullyQualifiedJavaType(context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + introspectedTable.getFullyQualifiedTable().getDomainObjectName());

        Method newMethod = new Method(method.getName());

        Parameter parameter = new Parameter(objectFqjt, JavaBeansUtil.getCamelCaseString(introspectedTable.getFullyQualifiedTable().getIntrospectedTableName(),false));
        newMethod.addParameter(parameter);

        method.getJavaDocLines();
        for (String docLine : method.getJavaDocLines()) {
            newMethod.addJavaDocLine(docLine);
        }
        FullyQualifiedJavaType methodReturn = method.getReturnType();

        newMethod.setReturnType(methodReturn);
        if (methodReturn.getShortName().indexOf("List") != -1) {
            interfaze.addImportedType(FullyQualifiedJavaType.getNewListInstance());
        }

        interfaze.addMethod(newMethod);


    }

    private void addByPagerMethod(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType objectFqjt = new FullyQualifiedJavaType(context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + introspectedTable.getFullyQualifiedTable().getDomainObjectName());

        Method newMethod = new Method(method.getName());
        FullyQualifiedJavaType methodReturn = method.getReturnType();

        /** by pager method **/
        newMethod = new Method(method.getName() + "ByPager");

        FullyQualifiedJavaType pageList = new FullyQualifiedJavaType(properties.getProperty("pageReturn"));
        FullyQualifiedJavaType pageBounds = new FullyQualifiedJavaType(properties.getProperty("pageParam"));
        methodReturn = new FullyQualifiedJavaType(properties.getProperty("pageReturn"));

        Parameter parameter = new Parameter(objectFqjt, JavaBeansUtil.getCamelCaseString(introspectedTable.getFullyQualifiedTable().getIntrospectedTableName(),false));
        parameter.addAnnotation("@Param(\"item\")");
        newMethod.addParameter(parameter);

        parameter = new Parameter(pageBounds, "pageBounds");
        parameter.addAnnotation("@Param(\"pageBounds\")");
        newMethod.addParameter(parameter);

        interfaze.addImportedType(pageList);
        interfaze.addImportedType(pageBounds);
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        method.getJavaDocLines();
        for (String docLine : method.getJavaDocLines()) {
            newMethod.addJavaDocLine(docLine);
        }

        methodReturn.addTypeArgument(objectFqjt);
        newMethod.setReturnType(methodReturn);

        if (methodReturn.getShortName().indexOf("List") != -1) {
            interfaze.addImportedType(FullyQualifiedJavaType.getNewListInstance());
        }

        interfaze.addMethod(newMethod);
        /** /by pager method **/
    }

    private void remethod(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType objectFqjt = new FullyQualifiedJavaType(context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + introspectedTable.getFullyQualifiedTable().getDomainObjectName());

        Method newMethod = new Method(method.getName());
        Parameter parameter = new Parameter(objectFqjt, JavaBeansUtil.getCamelCaseString(introspectedTable.getFullyQualifiedTable().getIntrospectedTableName(),false));
        newMethod.addParameter(parameter);

        newMethod.setReturnType(method.getReturnType());

        topLevelClass.addMethod(newMethod);
    }


    @Override
    public boolean sqlMapCountByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        countByExampleName = element.getAttributes().get(0).getValue();
        return false;
    }

    @Override
    public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        deleteByExampleName = element.getAttributes().get(0).getValue();
        return false;
    }

    @Override
    public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        selectByExampleWithoutBLOBsName = element.getAttributes().get(0).getValue();
        return false;
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        selectByExampleWithBLOBsName = element.getAttributes().get(0).getValue();
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        updateByExampleSelectiveName = element.getAttributes().get(0).getValue();
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        updateByExampleWithBLOBsName = element.getAttributes().get(0).getValue();
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        updateByExampleWithoutBLOBsName = element.getAttributes().get(0).getValue();
        return false;
    }


    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        exampleWhereCluse(document, introspectedTable);
        exampleByPagerWhereCluse(document, introspectedTable);
        deleteByExample(document, introspectedTable);
        countByExampleName(document, introspectedTable);
        updateByExampleSelective(document, introspectedTable);

        if (introspectedTable.getBLOBColumns().size() > 0){
            selectByExampleWithBLOBs(document, introspectedTable);
            updateByExampleWithBLOBs(document, introspectedTable);
        }

        selectByExampleWithoutBLOBs(document, introspectedTable);
        updateByExampleWithoutBLOBs(document, introspectedTable);

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private void updateByExampleWithoutBLOBs(Document document, IntrospectedTable introspectedTable) {
        XmlElement xmlElement;
        Attribute attribute;
        if (!updateByExampleWithoutBLOBsName.equals("")) {
            xmlElement = new XmlElement("update");

            attribute = new Attribute("id", updateByExampleWithoutBLOBsName);
            xmlElement.addAttribute(attribute);

            context.getCommentGenerator().addComment(xmlElement);
            xmlElement.addElement(new TextElement("update " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()));
            xmlElement.addElement(new TextElement("set"));
            String sql;
            for (int i=0;i< introspectedTable.getNonBLOBColumnCount();i++){
                sql = getColumn(introspectedTable.getNonBLOBColumns().get(i),null);
                if (i< introspectedTable.getNonBLOBColumnCount()-1){
                    sql = sql + ",";
                }
                xmlElement.addElement(new TextElement(sql));
            }


            addInclude(xmlElement, "Example_Where_Clause");
            document.getRootElement().addElement(xmlElement);
        }
    }

    private void updateByExampleSelective(Document document, IntrospectedTable introspectedTable) {
        XmlElement xmlElement;
        Attribute attribute;
        if (!updateByExampleSelectiveName.equals("")) {
            xmlElement = new XmlElement("update");

            attribute = new Attribute("id", updateByExampleSelectiveName);
            xmlElement.addAttribute(attribute);

            context.getCommentGenerator().addComment(xmlElement);
            xmlElement.addElement(new TextElement("update " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()));

            XmlElement setElement = new XmlElement("set");
            addIfConditionElement(introspectedTable.getAllColumns(), setElement, null, true, null);
            xmlElement.addElement(setElement);
            addInclude(xmlElement, "Example_Where_Clause");
            document.getRootElement().addElement(xmlElement);
        }
    }

    private void updateByExampleWithBLOBs(Document document, IntrospectedTable introspectedTable) {
        XmlElement xmlElement;
        Attribute attribute;
        if (!updateByExampleWithBLOBsName.equals("")) {
            xmlElement = new XmlElement("update");

            attribute = new Attribute("id", updateByExampleWithBLOBsName);
            xmlElement.addAttribute(attribute);

            context.getCommentGenerator().addComment(xmlElement);
            xmlElement.addElement(new TextElement("update " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()));

            xmlElement.addElement(new TextElement("set"));
            String sql;
            for (int i=0;i< introspectedTable.getAllColumns().size();i++){
                sql = getColumn(introspectedTable.getAllColumns().get(i),null);
                if (i< introspectedTable.getAllColumns().size()-1){
                    sql = sql + ",";
                }
                xmlElement.addElement(new TextElement(sql));
            }

            addInclude(xmlElement, "Example_Where_Clause");
            document.getRootElement().addElement(xmlElement);
        }
    }

    private void selectByExampleWithBLOBs(Document document, IntrospectedTable introspectedTable) {
        XmlElement xmlElement;
        Attribute attribute;
        if (!selectByExampleWithBLOBsName.equals("")) {
            xmlElement = new XmlElement("select");

            attribute = new Attribute("id", selectByExampleWithBLOBsName);
            xmlElement.addAttribute(attribute);

            attribute = new Attribute("resultMap", "ResultMapWithBLOBs");
            xmlElement.addAttribute(attribute);

            context.getCommentGenerator().addComment(xmlElement);
            xmlElement.addElement(new TextElement("select"));
            addInclude(xmlElement, "Base_Column_List");
            xmlElement.addElement(new TextElement(","));
            addInclude(xmlElement, "Blob_Column_List");
            xmlElement.addElement(new TextElement("from " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()));
            addInclude(xmlElement, "Example_Where_Clause");

            document.getRootElement().addElement(xmlElement);


            xmlElement = new XmlElement("select");

            attribute = new Attribute("id", selectByExampleWithBLOBsName+"ByPager");
            xmlElement.addAttribute(attribute);

            attribute = new Attribute("resultMap", "ResultMapWithBLOBs");
            xmlElement.addAttribute(attribute);

            context.getCommentGenerator().addComment(xmlElement);
            xmlElement.addElement(new TextElement("select"));
            addInclude(xmlElement, "Base_Column_List");
            xmlElement.addElement(new TextElement(","));
            addInclude(xmlElement, "Blob_Column_List");
            xmlElement.addElement(new TextElement("from " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()));
            addInclude(xmlElement, "Example_By_Pager_Where_Clause");

            document.getRootElement().addElement(xmlElement);
        }
    }

    private void selectByExampleWithoutBLOBs(Document document, IntrospectedTable introspectedTable) {
        XmlElement xmlElement;
        Attribute attribute;
        if (!selectByExampleWithoutBLOBsName.equals("")) {
            xmlElement = new XmlElement("select");

            attribute = new Attribute("id", selectByExampleWithoutBLOBsName);
            xmlElement.addAttribute(attribute);

            attribute = new Attribute("resultMap", "BaseResultMap");
            xmlElement.addAttribute(attribute);

            context.getCommentGenerator().addComment(xmlElement);
            xmlElement.addElement(new TextElement("select"));
            addInclude(xmlElement, "Base_Column_List");
            xmlElement.addElement(new TextElement("from " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()));
            addInclude(xmlElement, "Example_Where_Clause");

            document.getRootElement().addElement(xmlElement);

            xmlElement = new XmlElement("select");

            attribute = new Attribute("id", selectByExampleWithoutBLOBsName+"ByPager");
            xmlElement.addAttribute(attribute);

            attribute = new Attribute("resultMap", "BaseResultMap");
            xmlElement.addAttribute(attribute);

            context.getCommentGenerator().addComment(xmlElement);
            xmlElement.addElement(new TextElement("select"));
            addInclude(xmlElement, "Base_Column_List");
            xmlElement.addElement(new TextElement("from " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()));
            addInclude(xmlElement, "Example_By_Pager_Where_Clause");

            document.getRootElement().addElement(xmlElement);
        }
    }

    private void deleteByExample(Document document, IntrospectedTable introspectedTable) {
        XmlElement xmlElement;
        Attribute attribute;
        if (!deleteByExampleName.equals("")) {
            xmlElement = new XmlElement("delete");

            attribute = new Attribute("id", deleteByExampleName);
            xmlElement.addAttribute(attribute);

            context.getCommentGenerator().addComment(xmlElement);
            xmlElement.addElement(new TextElement("delete from " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()));
            addInclude(xmlElement, "Example_Where_Clause");

            document.getRootElement().addElement(xmlElement);
        }
    }

    private void exampleWhereCluse(Document document, IntrospectedTable introspectedTable) {

        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            keyColumns.add(column);
        }

        XmlElement xmlElement;
        Attribute attribute;
        xmlElement = new XmlElement("sql");

        attribute = new Attribute("id", "Example_Where_Clause");
        xmlElement.addAttribute(attribute);

        context.getCommentGenerator().addComment(xmlElement);

        addWhereElement(xmlElement, introspectedTable.getAllColumns(), null);

        document.getRootElement().addElement(xmlElement);
    }

    private void exampleByPagerWhereCluse(Document document, IntrospectedTable introspectedTable) {

        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            keyColumns.add(column);
        }

        XmlElement xmlElement;
        Attribute attribute;
        xmlElement = new XmlElement("sql");

        attribute = new Attribute("id", "Example_By_Pager_Where_Clause");
        xmlElement.addAttribute(attribute);

        context.getCommentGenerator().addComment(xmlElement);

        addWhereElement(xmlElement, introspectedTable.getAllColumns(), "item.");

        document.getRootElement().addElement(xmlElement);
    }

    private void addWhereElement(XmlElement xmlElement, List<IntrospectedColumn> columns, String prefix) {
        XmlElement whereElement = new XmlElement("where");
        whereElement.addElement(new TextElement("1=1"));
        addIfConditionElement(columns, whereElement, "and", false, prefix);
        xmlElement.addElement(whereElement);
    }

    private void addIfConditionElement(List<IntrospectedColumn> columns, XmlElement whereElement, String logical, boolean comma, String prefix) {
        int columnsSize = columns.size();
        for (int i = 0; i < columnsSize; i++) {
            IntrospectedColumn column = columns.get(i);
            ifCondition(whereElement, column, logical, comma, prefix);
        }
    }

    private void ifCondition(XmlElement xmlElement, IntrospectedColumn column, String logical, boolean comma, String prefix) {
        XmlElement andXmlElement;
        StringBuilder sb;
        String columnJavaTypeShortName;
        Attribute attribute;
        TextElement textElement;
        andXmlElement = new XmlElement("if");
        if (null == logical) {
            logical = "";
        }
        if (null == prefix) {
            prefix = "";
        }
        sb = new StringBuilder();
        String commaString = "";
        if (comma) {
            commaString = ",";
        }
        sb.append(prefix);
        sb.append(column.getJavaProperty());
        sb.append(" != null");
        columnJavaTypeShortName = column.getFullyQualifiedJavaType().getShortName();
        if (columnJavaTypeShortName.equals("String") ||
                columnJavaTypeShortName.equals("Date") ||
                columnJavaTypeShortName.equals("Time")) {
            sb.append(" ");
            sb.append(logical);
            sb.append(" ");
            sb.append(prefix);
            sb.append(column.getJavaProperty());
            sb.append(" != ''");
        }
        attribute = new Attribute("test", sb.toString());
        andXmlElement.addAttribute(attribute);

        sb = new StringBuilder();
        sb.append(" ");
        sb.append(logical);
        sb.append(getColumn(column, prefix));
        sb.append(commaString);
        textElement = new TextElement(sb.toString());

        andXmlElement.addElement(textElement);

        xmlElement.addElement(andXmlElement);
    }

    private String getColumn(IntrospectedColumn column, String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        sb.append(column.getActualColumnName());
        sb.append(" = #{");
        sb.append(prefix);
        sb.append(column.getJavaProperty());
        sb.append(", jdbcType=");
        sb.append(column.getJdbcTypeName());
        sb.append("}");

        return sb.toString();
    }

    private boolean isKeyColumn(IntrospectedColumn column) {
        for (IntrospectedColumn key : keyColumns) {
            if (key.getActualColumnName().equals(column.getActualColumnName())) {
                return true;
            }
        }

        return false;
    }

    private void countByExampleName(Document document, IntrospectedTable introspectedTable) {
        XmlElement xmlElement;
        Attribute attribute;
        if (!countByExampleName.equals("")) {
            xmlElement = new XmlElement("select");

            attribute = new Attribute("id", countByExampleName);
            xmlElement.addAttribute(attribute);

            attribute = new Attribute("resultType", "java.lang.Integer");
            xmlElement.addAttribute(attribute);

            context.getCommentGenerator().addComment(xmlElement);
            xmlElement.addElement(new TextElement("select count(*) from " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()));
            addInclude(xmlElement, "Example_Where_Clause");

            document.getRootElement().addElement(xmlElement);
        }
    }

    private void addInclude(XmlElement xmlElement, String include) {
        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", include));
        xmlElement.addElement(includeElement);
    }
}
