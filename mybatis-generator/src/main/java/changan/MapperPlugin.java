package changan;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beeant on 2016/4/29.
 */
public class MapperPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        List<Element> elements = new ArrayList<Element>();
        // baseResultMap
        genBaseResultMap(introspectedTable, elements);
        // baseColumnList
        genBaseColumnList(introspectedTable, elements);
        // getByPrimaryKey
        getByPrimaryKey(introspectedTable, elements);
        // getAllByParamsByPager
        getAllByParamsByPager(introspectedTable, elements);
        // batchCreate
        batchCreate(introspectedTable, elements);
        // create
        create(introspectedTable, elements);
        // batchDelete
        batchDelete(introspectedTable, elements);
        // delete
        delete(introspectedTable, elements);
        // updateByPrimaryKeySelective
        updateByPrimaryKeySelective(introspectedTable, elements);
        // batchUpdateSelective
        batchUpdateSelective(introspectedTable, elements);
        // update
        update(introspectedTable, elements);
        // batchUpdate
        batchUpdate(introspectedTable, elements);

        Document document = sqlMap.getDocument();
        XmlElement rootElement = document.getRootElement();
        rootElement.setElements(elements);
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }

    private void genBaseResultMap(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        Attribute attribute;
        XmlElement columnXmlElement;
        rootXmlElement = new XmlElement("resultMap");
        attribute = new Attribute("id", "BaseResultMap");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("type", introspectedTable.getBaseRecordType());
        rootXmlElement.addAttribute(attribute);
        XmlElement constructorXmlElement = new XmlElement("constructor");
        ;
        for (int i = 0; i < introspectedTable.getAllColumns().size(); i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);
            if (i == 0) {
                columnXmlElement = new XmlElement("idArg");
            } else {
                columnXmlElement = new XmlElement("arg");
            }
            attribute = new Attribute("column", column.getActualColumnName());
            columnXmlElement.addAttribute(attribute);
            attribute = new Attribute("javaType", column.getFullyQualifiedJavaType().getFullyQualifiedName());
            columnXmlElement.addAttribute(attribute);
            attribute = new Attribute("jdbcType", column.getJdbcTypeName());
            columnXmlElement.addAttribute(attribute);
            constructorXmlElement.addElement(columnXmlElement);
        }
        rootXmlElement.addElement(constructorXmlElement);
        elements.add(rootXmlElement);
    }

    private void genBaseColumnList(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        Attribute attribute;
        rootXmlElement = new XmlElement("sql");
        attribute = new Attribute("id", "Base_Column_List");
        rootXmlElement.addAttribute(attribute);

        allColumnsString(introspectedTable, rootXmlElement);

        elements.add(rootXmlElement);
    }

    private void getByPrimaryKey(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        Attribute attribute;
        TextElement textElement;
        StringBuilder sb;
        rootXmlElement = new XmlElement("select");
        attribute = new Attribute("id", "getByPrimaryKey");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("parameterType", "java.lang.String");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("resultMap", "BaseResultMap");
        rootXmlElement.addAttribute(attribute);

        fromCondition(introspectedTable, rootXmlElement);

        sb = new StringBuilder();
        sb.append("WHERE ");
        setKeyCondition(introspectedTable, sb);
        textElement = new TextElement(sb.toString());
        rootXmlElement.addElement(textElement);

        elements.add(rootXmlElement);
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

    private void setKeyCondition(IntrospectedTable introspectedTable, StringBuilder sb) {
        int primaryKeySize = introspectedTable.getPrimaryKeyColumns().size();
        for (int i = 0; i < primaryKeySize; i++) {
            if (i != 0) {
                sb.append(" AND ");
            }
            sb.append(introspectedTable.getPrimaryKeyColumns().get(i).getActualColumnName());
            sb.append(" = #{");
            sb.append(introspectedTable.getPrimaryKeyColumns().get(i).getJavaProperty());
            sb.append(", jdbcType=");
            sb.append(introspectedTable.getPrimaryKeyColumns().get(i).getJdbcTypeName());
            sb.append("}");
        }
    }

    private void getAllByParamsByPager(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        XmlElement xmlElement;
        Attribute attribute;

        rootXmlElement = new XmlElement("select");
        attribute = new Attribute("id", "getAllByParamsByPager");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("resultMap", "BaseResultMap");
        rootXmlElement.addAttribute(attribute);

        fromCondition(introspectedTable, rootXmlElement);

        xmlElement = new XmlElement("where");
        rootXmlElement.addElement(xmlElement);

        andCondition(introspectedTable, xmlElement);

        elements.add(rootXmlElement);
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

    private void andCondition(IntrospectedTable introspectedTable, XmlElement xmlElement) {
        int columnsSize = introspectedTable.getAllColumns().size();
        for (int i = 0; i < columnsSize; i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);
            whereCondition(xmlElement, column);
        }
    }

    private void batchCreate(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        Attribute attribute;
        TextElement textElement;
        XmlElement foreachXmlElement;
        StringBuilder sb;
        rootXmlElement = new XmlElement("insert");
        attribute = new Attribute("id", "batchCreate");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("parameterType", "java.util.List");
        rootXmlElement.addAttribute(attribute);

        insertInto(introspectedTable, rootXmlElement);

        textElement = new TextElement("(");
        rootXmlElement.addElement(textElement);

        allColumnsString(introspectedTable, rootXmlElement);

        textElement = new TextElement(")");
        rootXmlElement.addElement(textElement);
        /*
         * foreach
         */
        foreachXmlElement = new XmlElement("foreach");
        attribute = new Attribute("collection", "list");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("item", "item");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("index", "index");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("separator", ",");
        foreachXmlElement.addAttribute(attribute);

        textElement = new TextElement("(");
        foreachXmlElement.addElement(textElement);

        int columnsSize = introspectedTable.getAllColumns().size();
        for (int i = 0; i < columnsSize; i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);
            sb = new StringBuilder();
            sb.append("#{item.");
            sb.append(column.getJavaProperty());
            sb.append(", jdbcType=");
            sb.append(column.getJdbcTypeName());
            sb.append("}");
            if (i != columnsSize - 1) {
                sb.append(",");
            }
            textElement = new TextElement(sb.toString());
            foreachXmlElement.addElement(textElement);
            ;
        }
        textElement = new TextElement(")");
        foreachXmlElement.addElement(textElement);
        rootXmlElement.addElement(foreachXmlElement);

        elements.add(rootXmlElement);
    }

    private void insertInto(IntrospectedTable introspectedTable, XmlElement rootXmlElement) {
        StringBuilder sb;
        TextElement textElement;
        sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(introspectedTable.getFullyQualifiedTable().getIntrospectedTableName());
        textElement = new TextElement(sb.toString());
        rootXmlElement.addElement(textElement);
    }

    private void update(IntrospectedTable introspectedTable, XmlElement rootXmlElement) {
        StringBuilder sb;
        TextElement textElement;
        sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(introspectedTable.getFullyQualifiedTable().getIntrospectedTableName());
        textElement = new TextElement(sb.toString());
        rootXmlElement.addElement(textElement);
    }

    private void create(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        Attribute attribute;
        StringBuilder sb;
        XmlElement trimXmlElement;
        XmlElement ifXmlElement;
        TextElement textElement;
        rootXmlElement = new XmlElement("insert");
        attribute = new Attribute("id", "create");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("parameterType", introspectedTable.getBaseRecordType());
        rootXmlElement.addAttribute(attribute);

        insertInto(introspectedTable, rootXmlElement);

        int columnsSize = introspectedTable.getAllColumns().size();

        trimXmlElement = new XmlElement("trim");
        attribute = new Attribute("prefix", "(");
        trimXmlElement.addAttribute(attribute);

        attribute = new Attribute("suffix", ")");
        trimXmlElement.addAttribute(attribute);

        attribute = new Attribute("suffixOverrides", ",");
        trimXmlElement.addAttribute(attribute);

        for (int i = 0; i < columnsSize; i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);
            ifXmlElement = new XmlElement("if");
            sb = new StringBuilder();
            sb.append(column.getJavaProperty());
            sb.append(" != null ");
            attribute = new Attribute("test", sb.toString());
            ifXmlElement.addAttribute(attribute);
            sb = new StringBuilder();
            sb.append(column.getActualColumnName());
            sb.append(",");
            textElement = new TextElement(sb.toString());
            ifXmlElement.addElement(textElement);
            trimXmlElement.addElement(ifXmlElement);
        }
        rootXmlElement.addElement(trimXmlElement);

        trimXmlElement = new XmlElement("trim");
        attribute = new Attribute("prefix", "VALUES (");
        trimXmlElement.addAttribute(attribute);

        attribute = new Attribute("suffix", ")");
        trimXmlElement.addAttribute(attribute);

        attribute = new Attribute("suffixOverrides", ",");
        trimXmlElement.addAttribute(attribute);

        for (int i = 0; i < columnsSize; i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);
            ifXmlElement = new XmlElement("if");
            sb = new StringBuilder();
            sb.append(column.getJavaProperty());
            sb.append(" != null ");
            attribute = new Attribute("test", sb.toString());
            ifXmlElement.addAttribute(attribute);

            sb = new StringBuilder();
            sb.append("#{");
            sb.append(column.getJavaProperty());
            sb.append(",jdbcType=");
            sb.append(column.getJdbcTypeName());
            sb.append("},");
            textElement = new TextElement(sb.toString());
            ifXmlElement.addElement(textElement);
            trimXmlElement.addElement(ifXmlElement);
        }
        rootXmlElement.addElement(trimXmlElement);


        elements.add(rootXmlElement);
    }

    private void batchDelete(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        XmlElement foreachXmlElement;
        TextElement textElement;
        Attribute attribute;
        StringBuilder sb;
        rootXmlElement = new XmlElement("delete");
        attribute = new Attribute("id", "batchDelete");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("parameterType", "java.util.List");
        rootXmlElement.addAttribute(attribute);

        deleteFrom(introspectedTable, rootXmlElement);

        sb = new StringBuilder();
        sb.append("WHERE ");
        setKeyCondition(introspectedTable, sb);
        textElement = new TextElement(sb.toString());
        rootXmlElement.addElement(textElement);
        sb = new StringBuilder();
        /*
         * foreach
         */
        foreachXmlElement = new XmlElement("foreach");
        attribute = new Attribute("collection", "list");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("item", "item");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("index", "index");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("open", "(");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("separator", ",");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("close", ")");
        foreachXmlElement.addAttribute(attribute);

        foreachXmlElement.addElement(new TextElement("#{item}"));
        rootXmlElement.addElement(foreachXmlElement);
        elements.add(rootXmlElement);
    }

    private void deleteFrom(IntrospectedTable introspectedTable, XmlElement rootXmlElement) {
        TextElement textElement;
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(introspectedTable.getFullyQualifiedTable().getIntrospectedTableName());
        textElement = new TextElement(sb.toString());
        rootXmlElement.addElement(textElement);
    }

    private void delete(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        Attribute attribute;
        StringBuilder sb;

        rootXmlElement = new XmlElement("delete");
        attribute = new Attribute("id", "delete");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("parameterType", "java.lang.String");
        rootXmlElement.addAttribute(attribute);

        deleteFrom(introspectedTable, rootXmlElement);

        sb = new StringBuilder();
        sb.append("WHERE ");
        setKeyCondition(introspectedTable, sb);
        rootXmlElement.addElement(new TextElement(sb.toString()));

        elements.add(rootXmlElement);
    }

    private void updateByPrimaryKeySelective(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        Attribute attribute;
        StringBuilder sb;
        XmlElement setXmlElement;
        XmlElement ifXmlElement;
        TextElement textElement;
        rootXmlElement = new XmlElement("update");
        attribute = new Attribute("id", "updateByPrimaryKeySelective");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("parameterType", introspectedTable.getBaseRecordType());
        rootXmlElement.addAttribute(attribute);

        update(introspectedTable, rootXmlElement);

        int columnsSize = introspectedTable.getAllColumns().size();

        setXmlElement = new XmlElement("set");

        for (int i = 0; i < columnsSize; i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);
            ifXmlElement = new XmlElement("if");
            sb = new StringBuilder();
            sb.append(column.getJavaProperty());
            sb.append(" != null ");
            attribute = new Attribute("test", sb.toString());
            ifXmlElement.addAttribute(attribute);
            sb = new StringBuilder();
            sb.append(column.getActualColumnName());
            sb.append(" = #{");
            sb.append(column.getJavaProperty());
            sb.append(",jdbcType=");
            sb.append(column.getJdbcTypeName());
            sb.append("}");
            textElement = new TextElement(sb.toString());
            ifXmlElement.addElement(textElement);
            setXmlElement.addElement(ifXmlElement);
        }
        rootXmlElement.addElement(setXmlElement);
        sb = new StringBuilder();
        sb.append("WHERE ");
        setKeyCondition(introspectedTable, sb);
        textElement = new TextElement(sb.toString());

        rootXmlElement.addElement(textElement);

        elements.add(rootXmlElement);
    }

    private void batchUpdateSelective(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        XmlElement foreachXmlElement;
        XmlElement ifXmlElement;
        Attribute attribute;
        StringBuilder sb;
        XmlElement setXmlElement;
        TextElement textElement;
        rootXmlElement = new XmlElement("update");
        attribute = new Attribute("id", "batchUpdateSelective");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("parameterType", "java.util.List");
        rootXmlElement.addAttribute(attribute);

        int columnsSize = introspectedTable.getAllColumns().size();

        /*
         * foreach
         */
        foreachXmlElement = new XmlElement("foreach");
        attribute = new Attribute("collection", "list");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("item", "item");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("index", "index");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("separator", ";");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("open", "");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("close", "");
        foreachXmlElement.addAttribute(attribute);

        update(introspectedTable, foreachXmlElement);

        setXmlElement = new XmlElement("set");

        for (int i = 0; i < columnsSize; i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);
            ifXmlElement = new XmlElement("if");
            sb = new StringBuilder();
            sb.append(column.getJavaProperty());
            sb.append(" != null ");
            attribute = new Attribute("test", sb.toString());
            ifXmlElement.addAttribute(attribute);
            sb = new StringBuilder();
            sb.append(column.getActualColumnName());
            sb.append(" = #{");
            sb.append(column.getJavaProperty());
            sb.append(",jdbcType=");
            sb.append(column.getJdbcTypeName());
            sb.append("}");
            textElement = new TextElement(sb.toString());
            ifXmlElement.addElement(textElement);
            setXmlElement.addElement(ifXmlElement);
        }
        foreachXmlElement.addElement(setXmlElement);
        sb = new StringBuilder();
        sb.append("WHERE ");
        setKeyCondition(introspectedTable, sb);
        textElement = new TextElement(sb.toString());
        foreachXmlElement.addElement(textElement);
        rootXmlElement.addElement(foreachXmlElement);

        elements.add(rootXmlElement);
    }

    private void update(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        Attribute attribute;
        StringBuilder sb;
        XmlElement setXmlElement;
        TextElement textElement;
        rootXmlElement = new XmlElement("update");
        attribute = new Attribute("id", "update");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("parameterType", introspectedTable.getBaseRecordType());
        rootXmlElement.addAttribute(attribute);

        update(introspectedTable, rootXmlElement);

        int columnsSize = introspectedTable.getAllColumns().size();

        setXmlElement = new XmlElement("set");

        for (int i = 0; i < columnsSize; i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);
            sb = new StringBuilder();
            sb.append(column.getActualColumnName());
            sb.append(" = #{");
            sb.append(column.getJavaProperty());
            sb.append(",jdbcType=");
            sb.append(column.getJdbcTypeName());
            sb.append("}");
            if (i != columnsSize - 1) {
                sb.append(",");
            }
            textElement = new TextElement(sb.toString());
            setXmlElement.addElement(textElement);
        }
        rootXmlElement.addElement(setXmlElement);
        sb = new StringBuilder();
        sb.append("WHERE ");
        setKeyCondition(introspectedTable, sb);
        textElement = new TextElement(sb.toString());

        rootXmlElement.addElement(textElement);

        elements.add(rootXmlElement);
    }

    private void batchUpdate(IntrospectedTable introspectedTable, List<Element> elements) {
        XmlElement rootXmlElement;
        XmlElement foreachXmlElement;
        Attribute attribute;
        StringBuilder sb;
        XmlElement setXmlElement;
        TextElement textElement;
        rootXmlElement = new XmlElement("update");
        attribute = new Attribute("id", "batchUpdate");
        rootXmlElement.addAttribute(attribute);
        attribute = new Attribute("parameterType", "java.util.List");
        rootXmlElement.addAttribute(attribute);

        int columnsSize = introspectedTable.getAllColumns().size();

        /*
         * foreach
         */
        foreachXmlElement = new XmlElement("foreach");
        attribute = new Attribute("collection", "list");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("item", "item");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("index", "index");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("separator", ";");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("open", "");
        foreachXmlElement.addAttribute(attribute);

        attribute = new Attribute("close", "");
        foreachXmlElement.addAttribute(attribute);

        update(introspectedTable, foreachXmlElement);

        setXmlElement = new XmlElement("set");

        for (int i = 0; i < columnsSize; i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);
            sb = new StringBuilder();
            sb.append(column.getActualColumnName());
            sb.append(" = #{");
            sb.append(column.getJavaProperty());
            sb.append(",jdbcType=");
            sb.append(column.getJdbcTypeName());
            sb.append("}");
            if (i != columnsSize - 1) {
                sb.append(",");
            }
            textElement = new TextElement(sb.toString());
            setXmlElement.addElement(textElement);
        }
        foreachXmlElement.addElement(setXmlElement);
        sb = new StringBuilder();
        sb.append("WHERE ");
        setKeyCondition(introspectedTable, sb);
        textElement = new TextElement(sb.toString());
        foreachXmlElement.addElement(textElement);
        rootXmlElement.addElement(foreachXmlElement);

        elements.add(rootXmlElement);
    }

    private void allColumnsString(IntrospectedTable introspectedTable, XmlElement rootXmlElement) {
        StringBuilder sb = new StringBuilder();
        int size = introspectedTable.getAllColumns().size();
        int line = 1;
        for (int i = 0; i < size; i++) {
            IntrospectedColumn column = introspectedTable.getAllColumns().get(i);
            String shortName = column.getFullyQualifiedJavaType().getShortName();
//            if (shortName.equals("Date") || shortName.equals("Time")) {
//                sb.append("DATE_FORMAT(");
//                sb.append(column.getActualColumnName());
//                sb.append(",'%Y-%m-%d %H:%i:%s') ");
//                sb.append(column.getActualColumnName());
//            } else {
                sb.append(column.getActualColumnName());
//            }
            if (i != size - 1) {
                sb.append(", ");
            }
            if (sb.toString().length() > 100 * line) {
                line = line + 1;
                sb.append("\n    ");
            }
        }
        TextElement textElement = new TextElement(sb.toString());
        rootXmlElement.addElement(textElement);
    }
}
