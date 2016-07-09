package mybatis.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Beeant on 2016/4/29.
 */
public class DaoAnnotationPlugin extends PluginAdapter {
    private FullyQualifiedJavaType annotation;

    @Override
    public boolean validate(List<String> warnings) {
        annotation = new FullyQualifiedJavaType(properties.getProperty("annotation"));
        return true;
    }


    @Override
    /**
     * dao生成
     */
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        interfaze.addAnnotation("@" + annotation.getShortName() + "(\""
                + JavaBeansUtil.getCamelCaseString(introspectedTable.getFullyQualifiedTable().getIntrospectedTableName(),false)
                + "MybatisDao\")");
        interfaze.addImportedType(annotation);

        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }
}
