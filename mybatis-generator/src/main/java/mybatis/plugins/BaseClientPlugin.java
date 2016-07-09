package mybatis.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.PluginAggregator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Beeant on 2016/7/9.
 */
public class BaseClientPlugin extends PluginAdapter {

    private static Boolean isExceted = false;
    private Boolean generateExample = false;
    private FullyQualifiedJavaType superClient;
    private FullyQualifiedJavaType pageList;
    private FullyQualifiedJavaType pageBounds;
    private FullyQualifiedJavaType genericType = new FullyQualifiedJavaType("T");
    private FullyQualifiedJavaType genericExampleType = new FullyQualifiedJavaType("Example");
    private FullyQualifiedJavaType paramAnnotationFqjt = new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param");
    private Parameter exampleParam = new Parameter(genericExampleType, "example");
    private Parameter genericParam = new Parameter(genericType, "record");
    private Parameter keyParam = new Parameter(FullyQualifiedJavaType.getStringInstance(), "key");
    private Parameter pageBoundsParam;
    private Parameter pageBoundsParamNoAnnotation;
    private FullyQualifiedJavaType pageListReturnFqjt;
    private FullyQualifiedJavaType listReturn;

    private TableConfiguration globalTableConfiguration;

    @Override
    public boolean validate(List<String> list) {
        superClient = new FullyQualifiedJavaType(properties.getProperty("superClient"));
        pageList = new FullyQualifiedJavaType(context.getProperty("pageList"));
        pageBounds = new FullyQualifiedJavaType(context.getProperty("pageBounds"));
        Boolean propertiesGenerate = Boolean.valueOf(properties.getProperty("generateExample"));
        generateExample = propertiesGenerate == null ? false : propertiesGenerate;
        listReturn = FullyQualifiedJavaType.getNewListInstance();
        listReturn.addTypeArgument(genericType);

        pageListReturnFqjt = new FullyQualifiedJavaType(context.getProperty("pageList"));
        pageListReturnFqjt.addTypeArgument(genericType);

        pageBoundsParam = new Parameter(pageBounds, "pageBounds");
        pageBoundsParam.addAnnotation("@" + paramAnnotationFqjt.getShortName() + "(\"pageBounds\")");
        pageBoundsParamNoAnnotation = new Parameter(pageBounds, "pageBounds");

        List<TableConfiguration> tableConfigurations = context.getTableConfigurations();
        for (TableConfiguration tableConfiguration : tableConfigurations) {
            if (tableConfiguration.getTableName().equals("%")) {
                globalTableConfiguration = tableConfiguration;
                break;
            }
        }

        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
        if (!isExceted) {
            isExceted = true;
            generatedSuperClient(introspectedTable, files);
        }
        return files;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (null != superClient) {
            FullyQualifiedJavaType objectFqjt = new FullyQualifiedJavaType(context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + introspectedTable.getFullyQualifiedTable().getDomainObjectName());
            FullyQualifiedJavaType objectExample = new FullyQualifiedJavaType(objectFqjt.getFullyQualifiedName() + "Example");
            FullyQualifiedJavaType superClientFqjt = new FullyQualifiedJavaType(properties.getProperty("superClient"));
            superClientFqjt.addTypeArgument(objectFqjt);
            if (generateExample) {
                superClientFqjt.addTypeArgument(objectExample);
            }
            interfaze.addSuperInterface(superClientFqjt);
            interfaze.addImportedType(superClient);
        }

        Iterator iterList= interfaze.getMethods().iterator();//List接口实现了Iterable接口

        while(iterList.hasNext()){
            Method method= (Method) iterList.next();
            if (method.getName().indexOf("Search") != -1){
                continue;
            }
            iterList.remove();
        }

        return true;
    }

    private void generatedSuperClient(IntrospectedTable introspectedTable, List<GeneratedJavaFile> files) {
        Interface interfaze = new Interface(superClient);
        interfaze.setVisibility(JavaVisibility.PUBLIC);

        generateMethod(introspectedTable, interfaze);

        superClient.addTypeArgument(genericType);
        if (generateExample){
            superClient.addTypeArgument(genericExampleType);
        }

        interfaze.addImportedType(paramAnnotationFqjt);

        GeneratedJavaFile javaFile = new GeneratedJavaFile(interfaze, context.getJavaModelGeneratorConfiguration()
                .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());
        files.add(javaFile);

    }

    private void generateMethod(IntrospectedTable introspectedTable, Interface interfaze) {
        Method method;
        /**
         * countByExample
         */
        if (globalTableConfiguration.isCountByExampleStatementEnabled()) {
            method = new Method("countByExample");
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            addGenericParam(method, false);

            interfaze.addMethod(method);
        }

        /**
         * deleteByExample
         */
        if (globalTableConfiguration.isDeleteByExampleStatementEnabled()) {
            method = new Method("deleteByExample");
            addGenericParam(method, false);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }

        /**
         * deleteByPrimaryKey
         */
        if (globalTableConfiguration.isDeleteByPrimaryKeyStatementEnabled()) {
            method = new Method("deleteByPrimaryKey");
            method.addParameter(keyParam);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }
        /**
         * insert & insertSelective
         */
        if (globalTableConfiguration.isInsertStatementEnabled()) {
            method = new Method("insert");
            method.addParameter(genericParam);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("insertSelective");
            method.addParameter(genericParam);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }

        /**
         * selectByExampleWithBLOBs & selectByExample
         */
        if (globalTableConfiguration.isSelectByExampleStatementEnabled()) {
            method = new Method("selectByExampleWithBLOBs");
            method.setReturnType(listReturn);
            addGenericParam(method, false);
            interfaze.addMethod(method);

            method = new Method("selectByExampleWithBLOBsByPager");
            method.setReturnType(pageListReturnFqjt);
            addGenericParam(method, true);
            method.addParameter(pageBoundsParam);
            interfaze.addMethod(method);
            interfaze.addImportedType(pageBounds);
            interfaze.addImportedType(pageList);

            method = new Method("selectByExample");
            addGenericParam(method, false);
            method.setReturnType(listReturn);
            interfaze.addMethod(method);

            method = new Method("selectByExampleByPager");
            method.setReturnType(pageListReturnFqjt);
            addGenericParam(method, true);
            method.addParameter(pageBoundsParam);
            interfaze.addMethod(method);
            interfaze.addImportedType(pageBounds);
            interfaze.addImportedType(pageList);
        }

        interfaze.addImportedType(FullyQualifiedJavaType.getNewListInstance());

        if (globalTableConfiguration.isSelectByPrimaryKeyStatementEnabled()) {
            method = new Method("selectByPrimaryKey");
            method.addParameter(keyParam);
            method.setReturnType(genericType);
            interfaze.addMethod(method);
        }

        if (globalTableConfiguration.isUpdateByExampleStatementEnabled()) {
            addUpdateMethod(interfaze,"updateByExampleSelective");
            addUpdateMethod(interfaze,"updateByExampleWithBLOBs");
            addUpdateMethod(interfaze,"updateByExample");
        }

        if (globalTableConfiguration.isUpdateByPrimaryKeyStatementEnabled()) {
            addUpdateMethod(interfaze,"updateByPrimaryKeySelective");
            addUpdateMethod(interfaze,"updateByPrimaryKeyWithBLOBs");
            addUpdateMethod(interfaze,"updateByPrimaryKey");
        }
    }

    private void addUpdateMethod(Interface interfaze, String methodName) {
        Method method;
        method = new Method(methodName);
        addGenericParam(method, false);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        interfaze.addMethod(method);
    }

    private void addGenericParam(Method method, boolean addAnnotation) {
        if (generateExample) {
            method.addParameter(exampleParam);
        } else {
            Parameter genericParam = new Parameter(genericType, "record");
            if (addAnnotation) {
                genericParam.addAnnotation("@" + paramAnnotationFqjt.getShortName() + "(\"item\")");
            }
            method.addParameter(genericParam);
        }
    }
}
