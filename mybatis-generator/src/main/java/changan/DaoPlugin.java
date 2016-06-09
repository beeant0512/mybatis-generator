package changan;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Beeant on 2016/4/29.
 */
public class DaoPlugin extends PluginAdapter {
    private FullyQualifiedJavaType superface;
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }


    @Override
    /**
     * dao生成
     */
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        superface = new FullyQualifiedJavaType(properties.getProperty("superface"));
        //remove functions that IBaseMybatisDao defined
        List<Method> methods = new ArrayList<Method>();
        Parameter parameter;

        // 模糊搜索
        Method method = new Method("fuzzySearchByParamsByPager");

        FullyQualifiedJavaType pageList = new FullyQualifiedJavaType(PackageEnum.pageList.getPackge());
        FullyQualifiedJavaType pageBounds = new FullyQualifiedJavaType(PackageEnum.pageBounds.getPackge());
        FullyQualifiedJavaType baseRecordType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

        pageList.addTypeArgument(baseRecordType);
        method.setReturnType(pageList);

        parameter = new Parameter(baseRecordType, "record");
        parameter.addAnnotation("@Param(\"item\")");
        method.addParameter(parameter);

        parameter = new Parameter(pageBounds, "pageBounds");
        parameter.addAnnotation("@Param(\"pageBounds\")");
        method.addParameter(parameter);

        methods.add(method);

        Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        imports.add(new FullyQualifiedJavaType(PackageEnum.baseMybatis.getPackge()));
        imports.add(new FullyQualifiedJavaType(PackageEnum.mybaisRepository.getPackge()));
        imports.add(baseRecordType);
        imports.add(pageBounds);
        imports.add(pageList);
        imports.add(new FullyQualifiedJavaType(PackageEnum.param.getPackge()));
        interfaze.setImportedTypes(imports);
        interfaze.setMethods(methods);
        interfaze.addAnnotation("@MyBatisRepository(".concat(baseRecordType.getShortName()).concat("MybatisDao)"));

        superface.addTypeArgument(baseRecordType);
        interfaze.addSuperInterface(superface);

        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }
}
