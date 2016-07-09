package mybatis.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.JavaBeansUtil;

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
    private FullyQualifiedJavaType superService;
    private FullyQualifiedJavaType superServiceImpl;
    private FullyQualifiedJavaType service;
    private FullyQualifiedJavaType serviceImpl;
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
        superService = new FullyQualifiedJavaType(properties.getProperty("superService"));
        superServiceImpl = new FullyQualifiedJavaType(properties.getProperty("superServiceImpl"));
        service = new FullyQualifiedJavaType(properties.getProperty("service"));
        serviceImpl = new FullyQualifiedJavaType(properties.getProperty("serviceImpl"));
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
            generatedSuperService(introspectedTable, files);
        }

        /**
         * 每个表的service实现
         */
        generatedService(introspectedTable, files);

        return files;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        /**
         * 添加 继承
         */
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

            /**
             * 移除基础类中的方法
             */
            Iterator iterList = interfaze.getMethods().iterator();//List接口实现了Iterable接口
            while (iterList.hasNext()) {
                Method method = (Method) iterList.next();
                if (method.getName().indexOf("Search") != -1) {
                    continue;
                }
                iterList.remove();
            }
        }


        return true;
    }

    private void generatedSuperClient(IntrospectedTable introspectedTable, List<GeneratedJavaFile> files) {
        Interface interfaze = new Interface(superClient);
        interfaze.setVisibility(JavaVisibility.PUBLIC);

        generateMethod(introspectedTable, interfaze, true);

        superClient.addTypeArgument(genericType);
        if (generateExample) {
            superClient.addTypeArgument(genericExampleType);
        }

        interfaze.addImportedType(paramAnnotationFqjt);

        GeneratedJavaFile javaFile = new GeneratedJavaFile(interfaze, context.getJavaModelGeneratorConfiguration()
                .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());
        files.add(javaFile);

    }

    private void generatedSuperService(IntrospectedTable introspectedTable, List<GeneratedJavaFile> files) {
        Interface interfaze = new Interface(superService);
        interfaze.setVisibility(JavaVisibility.PUBLIC);

        generateMethod(introspectedTable, interfaze, false);

        Method method = new Method("getDao");
        method.setReturnType(superClient);
        interfaze.addImportedType(new FullyQualifiedJavaType(properties.getProperty("superClient")));
        interfaze.addMethod(method);

        method = new Method("setKey");
        method.addParameter(genericParam);
        interfaze.addMethod(method);

        superService.addTypeArgument(genericType);
        if (generateExample) {
            superService.addTypeArgument(genericExampleType);
        }

        GeneratedJavaFile javaFile = new GeneratedJavaFile(interfaze, context.getJavaModelGeneratorConfiguration()
                .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());
        files.add(javaFile);

        /**
         * service impl
         */
        FullyQualifiedJavaType superServiceImpl = new FullyQualifiedJavaType(properties.getProperty("superServiceImpl"));
        TopLevelClass topLevelClass = new TopLevelClass(superServiceImpl);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        generateMethodImpl(introspectedTable, topLevelClass);

        topLevelClass.addSuperInterface(superService);
        topLevelClass.setAbstract(true);

        topLevelClass.addImportedType(new FullyQualifiedJavaType(properties.getProperty("superService")));
        superServiceImpl.addTypeArgument(genericType);
        if (generateExample) {
            superServiceImpl.addTypeArgument(genericExampleType);
        }

        javaFile = new GeneratedJavaFile(topLevelClass, context.getJavaModelGeneratorConfiguration()
                .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());
        files.add(javaFile);

    }

    private void generatedService(IntrospectedTable introspectedTable, List<GeneratedJavaFile> files) {
        String objectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        FullyQualifiedJavaType objectFqjt = new FullyQualifiedJavaType(context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + objectName);
        String serviceInterfaceString = service.getFullyQualifiedName() + ".I" + objectName + "Service";
        FullyQualifiedJavaType serviceInterface = new FullyQualifiedJavaType(serviceInterfaceString);
        Interface interfaze = new Interface(serviceInterface);
        interfaze.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType superServiceFqjt = new FullyQualifiedJavaType(properties.getProperty("superService"));
        superServiceFqjt.addTypeArgument(objectFqjt);
        interfaze.addImportedType(objectFqjt);

        //generateMethod(introspectedTable, interfaze, false);
        interfaze.addSuperInterface(superServiceFqjt);
        interfaze.addImportedType(new FullyQualifiedJavaType(properties.getProperty("superService")));
        GeneratedJavaFile javaFile = new GeneratedJavaFile(interfaze, context.getJavaModelGeneratorConfiguration()
                .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());
        files.add(javaFile);

        /**
         * service impl
         */
        FullyQualifiedJavaType currentServiceImpl = new FullyQualifiedJavaType(serviceImpl.getFullyQualifiedName() + "." + objectName + "ServiceImpl");
        TopLevelClass topLevelClass = new TopLevelClass(currentServiceImpl);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        //generateMethodImpl(introspectedTable, topLevelClass);
        FullyQualifiedJavaType superServiceImpl = new FullyQualifiedJavaType(properties.getProperty("superServiceImpl"));
        superServiceImpl.addTypeArgument(objectFqjt);
        if (generateExample) {
            superServiceImpl.addTypeArgument(genericExampleType);
        }
        topLevelClass.setSuperClass(superServiceImpl);
        topLevelClass.addImportedType(new FullyQualifiedJavaType(properties.getProperty("superServiceImpl")));

        serviceInterface = new FullyQualifiedJavaType(serviceInterfaceString);
        topLevelClass.addImportedType(serviceInterface);
        topLevelClass.addSuperInterface(serviceInterface);
        this.superServiceImpl.addTypeArgument(objectFqjt);
        if (generateExample) {
            this.superServiceImpl.addTypeArgument(genericExampleType);
        }

        // dao annotation
        FullyQualifiedJavaType mybatis3JavaMapperType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());

        String shortName = mybatis3JavaMapperType.getShortName();

        String daoInterfaceAlias = shortName.substring(1, 2).toLowerCase() + shortName.substring(2);
        Field dao = new Field(daoInterfaceAlias, mybatis3JavaMapperType);
        dao.addAnnotation("@Autowired");
        dao.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(dao);
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        topLevelClass.addImportedType(objectFqjt);

        // getDao Method
        Method getDao = new Method("getDao");
        getDao.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType superClientFqjt = new FullyQualifiedJavaType(properties.getProperty("superClient"));
        superClientFqjt.addTypeArgument(objectFqjt);
        getDao.setReturnType(superClientFqjt);
        topLevelClass.addImportedType(new FullyQualifiedJavaType(properties.getProperty("superClient")));
        topLevelClass.addImportedType(mybatis3JavaMapperType);

        getDao.addBodyLine("return this.".concat(daoInterfaceAlias).concat(";"));
        topLevelClass.addMethod(getDao);

        // setKey Method
        Method setKey = new Method("setKey");
        setKey.setVisibility(JavaVisibility.PUBLIC);
        setKey.addParameter(new Parameter(objectFqjt, "record"));
        topLevelClass.addImportedType(objectFqjt);

        List<IntrospectedColumn> keyColumns = introspectedTable.getPrimaryKeyColumns();
        IntrospectedColumn keyColumn = new IntrospectedColumn();
        if (keyColumns.size() > 0) {
            keyColumn = keyColumns.get(0);

            if (keyColumn.getFullyQualifiedJavaType().getShortName().equals("String")) {
                StringBuilder sb = new StringBuilder();
                String key = JavaBeansUtil.getCamelCaseString(keyColumn.getActualColumnName(), true);
                sb.append("if(record.get");
                sb.append(key);
                sb.append("() == null || \"\".equals(record.get");
                sb.append(key);
                sb.append("())) {");
                setKey.addBodyLine(sb.toString());
                sb = new StringBuilder();
                sb.append("record.set");
                sb.append(key);
                sb.append("(");
                sb.append("UUID.randomUUID().toString()");
                sb.append(");");
                setKey.addBodyLine(sb.toString());
                setKey.addBodyLine("}");
                topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.UUID"));
            } else {
                setKey.addBodyLine("");
            }
        } else {
            setKey.addBodyLine("");
        }


        topLevelClass.addMethod(setKey);

        topLevelClass.addAnnotation("@Component(\"".concat(currentServiceImpl.getShortNameWithoutTypeArguments()).concat("\")"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Component"));

        javaFile = new GeneratedJavaFile(topLevelClass, context.getJavaModelGeneratorConfiguration()
                .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());
        files.add(javaFile);

    }

    private void generateMethod(IntrospectedTable introspectedTable, Interface interfaze, boolean addParamAnnotation) {
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
            addGenericParam(method, addParamAnnotation);
            addPageBoundsParam(addParamAnnotation, method);

            interfaze.addMethod(method);
            interfaze.addImportedType(pageBounds);
            interfaze.addImportedType(pageList);

            method = new Method("selectByExample");
            addGenericParam(method, false);
            method.setReturnType(listReturn);
            interfaze.addMethod(method);

            method = new Method("selectByExampleByPager");
            method.setReturnType(pageListReturnFqjt);
            addGenericParam(method, addParamAnnotation);
            addPageBoundsParam(addParamAnnotation, method);
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
            addUpdateMethod(interfaze, "updateByExampleSelective");
            addUpdateMethod(interfaze, "updateByExampleWithBLOBs");
            addUpdateMethod(interfaze, "updateByExample");
        }

        if (globalTableConfiguration.isUpdateByPrimaryKeyStatementEnabled()) {
            addUpdateMethod(interfaze, "updateByPrimaryKeySelective");
            addUpdateMethod(interfaze, "updateByPrimaryKeyWithBLOBs");
            addUpdateMethod(interfaze, "updateByPrimaryKey");
        }

        /**
         * fuzzySearchByPager
         */
        method = new Method("fuzzySearchByPager");
        method.setReturnType(pageListReturnFqjt);
        addGenericParam(method, addParamAnnotation);
        addPageBoundsParam(addParamAnnotation, method);
        interfaze.addImportedType(pageBounds);
        interfaze.addImportedType(pageList);

        interfaze.addMethod(method);

        /**
         * fuzzySearch
         */
        method = new Method("fuzzySearch");
        method.setReturnType(listReturn);
        addGenericParam(method, addParamAnnotation);
        interfaze.addImportedType(pageList);

        interfaze.addMethod(method);
    }

    private void generateMethodImpl(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
        Method method;

        /**
         * countByExample
         */
        if (globalTableConfiguration.isCountByExampleStatementEnabled()) {
            method = new Method("countByExample");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            addGenericParam(method, false);

            addMethodBody("countByExample", method);
            topLevelClass.addMethod(method);
        }

        /**
         * deleteByExample
         */
        if (globalTableConfiguration.isDeleteByExampleStatementEnabled()) {
            method = new Method("deleteByExample");
            method.setVisibility(JavaVisibility.PUBLIC);
            addGenericParam(method, false);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());

            addMethodBody("deleteByExample", method);
            topLevelClass.addMethod(method);
        }

        /**
         * deleteByPrimaryKey
         */
        if (globalTableConfiguration.isDeleteByPrimaryKeyStatementEnabled()) {
            method = new Method("deleteByPrimaryKey");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(keyParam);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());

            method.addBodyLine("return getDao().deleteByPrimaryKey(" + keyParam.getName() + ");");
            topLevelClass.addMethod(method);
        }
        /**
         * insert & insertSelective
         */
        if (globalTableConfiguration.isInsertStatementEnabled()) {
            method = new Method("insert");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(genericParam);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());

            method.addBodyLine("return getDao().insert(" + genericParam.getName() + ");");
            topLevelClass.addMethod(method);

            method = new Method("insertSelective");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(genericParam);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());

            method.addBodyLine("return getDao().insertSelective(" + genericParam.getName() + ");");
            topLevelClass.addMethod(method);
        }

        /**
         * selectByExampleWithBLOBs & selectByExample
         */
        if (globalTableConfiguration.isSelectByExampleStatementEnabled()) {
            method = new Method("selectByExampleWithBLOBs");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setReturnType(listReturn);
            addGenericParam(method, false);

            addMethodBody("selectByExampleWithBLOBs", method);
            topLevelClass.addMethod(method);

            method = new Method("selectByExampleWithBLOBsByPager");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setReturnType(pageListReturnFqjt);
            addGenericParam(method, false);
            addPageBoundsParam(false, method);
            addPagerMethodBody("selectByExampleWithBLOBsByPager", method);

            topLevelClass.addMethod(method);
            topLevelClass.addImportedType(pageBounds);
            topLevelClass.addImportedType(pageList);

            method = new Method("selectByExample");
            method.setVisibility(JavaVisibility.PUBLIC);
            addGenericParam(method, false);
            method.setReturnType(listReturn);
            addMethodBody("selectByExample", method);
            topLevelClass.addMethod(method);

            method = new Method("selectByExampleByPager");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setReturnType(pageListReturnFqjt);
            addGenericParam(method, false);
            addPageBoundsParam(false, method);

            addPagerMethodBody("selectByExampleByPager", method);
            topLevelClass.addMethod(method);
            topLevelClass.addImportedType(pageBounds);
            topLevelClass.addImportedType(pageList);
        }

        topLevelClass.addImportedType(FullyQualifiedJavaType.getNewListInstance());

        if (globalTableConfiguration.isSelectByPrimaryKeyStatementEnabled()) {
            method = new Method("selectByPrimaryKey");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(keyParam);
            method.setReturnType(genericType);

            method.addBodyLine("return getDao().selectByPrimaryKey(" + keyParam.getName() + ");");
            topLevelClass.addMethod(method);
        }

        if (globalTableConfiguration.isUpdateByExampleStatementEnabled()) {
            addUpdateMethodImpl(topLevelClass, "updateByExampleSelective");
            addUpdateMethodImpl(topLevelClass, "updateByExampleWithBLOBs");
            addUpdateMethodImpl(topLevelClass, "updateByExample");
        }

        if (globalTableConfiguration.isUpdateByPrimaryKeyStatementEnabled()) {
            addUpdateMethodImpl(topLevelClass, "updateByPrimaryKeySelective");
            addUpdateMethodImpl(topLevelClass, "updateByPrimaryKeyWithBLOBs");
            addUpdateMethodImpl(topLevelClass, "updateByPrimaryKey");
        }

        /**
         * fuzzySearchByPager
         */
        method = new Method("fuzzySearchByPager");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(pageListReturnFqjt);
        addGenericParam(method, false);
        addPageBoundsParam(false, method);
        addPagerMethodBody("fuzzySearchByPager", method);
        topLevelClass.addImportedType(pageBounds);
        topLevelClass.addImportedType(pageList);

        topLevelClass.addMethod(method);

        /**
         * fuzzySearch
         */
        method = new Method("fuzzySearch");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(listReturn);
        addGenericParam(method, false);
        addMethodBody("fuzzySearch", method);

        topLevelClass.addMethod(method);
    }

    private void addPageBoundsParam(boolean addParamAnnotation, Method method) {
        if (addParamAnnotation) {
            method.addParameter(pageBoundsParam);
        } else {
            method.addParameter(pageBoundsParamNoAnnotation);
        }
    }

    private void addUpdateMethod(Interface interfaze, String methodName) {
        Method method;
        method = new Method(methodName);
        addGenericParam(method, false);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        interfaze.addMethod(method);
    }

    private void addUpdateMethodImpl(TopLevelClass interfaze, String methodName) {
        Method method;
        method = new Method(methodName);
        method.setVisibility(JavaVisibility.PUBLIC);
        addGenericParam(method, false);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        addMethodBody(methodName, method);
        interfaze.addMethod(method);
    }

    private void addMethodBody(String methodName, Method method) {
        if (generateExample) {
            method.addBodyLine("return getDao()." + methodName + "(" + genericParam.getName() + ", " + exampleParam.getName() + ");");
        } else {
            method.addBodyLine("return getDao()." + methodName + "(" + genericParam.getName() + ");");
        }
    }

    private void addPagerMethodBody(String methodName, Method method) {
        if (generateExample) {
            method.addBodyLine("return getDao()." + methodName + "(" + genericParam.getName() + ", " + exampleParam.getName() + ", " + pageBoundsParam.getName() + ");");
        } else {
            method.addBodyLine("return getDao()." + methodName + "(" + genericParam.getName() + ", " + pageBoundsParam.getName() + ");");
        }
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
