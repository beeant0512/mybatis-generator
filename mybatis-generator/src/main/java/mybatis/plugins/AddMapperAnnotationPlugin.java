package mybatis.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * Created by Beeant on 2016/1/17 0017.
 */
public class AddMapperAnnotationPlugin extends PluginAdapter {

    private String annotation;
    private String annotationPackage;

    @Override
    public boolean validate(List<String> warnings) {
        annotation = properties.getProperty("annotation");
        annotationPackage = properties.getProperty("annotationPackage");
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        /*add spring repository annotation */
        interfaze.addAnnotation(annotation);
        interfaze.addImportedType(new FullyQualifiedJavaType(annotationPackage));

        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }
}
