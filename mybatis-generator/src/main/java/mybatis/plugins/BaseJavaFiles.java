package mybatis.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import tools.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beeant on 2016/6/10.
 */
public class BaseJavaFiles extends PluginAdapter {
    private String serviceTargetPackage;
    private String superService;
    private String superDao;
    private String superServiceImpl;
    private String commonReturn;
    private String facades;
    private String bridge;
    private String controller;
    private boolean excuted = false;
    private TableConfiguration globalTableConfiguration;

    private boolean addExampleArg = false;
    private FullyQualifiedJavaType superServiceInterfaceFqjt;
    private FullyQualifiedJavaType serviceInterfaceFqjt;

    @Override
    public boolean validate(List<String> warnings) {
        serviceTargetPackage = properties.getProperty("serviceTargetPackage");
        superService = properties.getProperty("superService");
        superDao = properties.getProperty("superDao");
        superServiceImpl = properties.getProperty("superServiceImpl");
        commonReturn = properties.getProperty("commonReturn");
        facades = properties.getProperty("facades");
        bridge = properties.getProperty("bridge");
        controller = properties.getProperty("controller");
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<TableConfiguration> tableConfigurations = context.getTableConfigurations();
        for (TableConfiguration tableConfiguration : tableConfigurations) {
            if (tableConfiguration.getTableName().equals("%")) {
                globalTableConfiguration = tableConfiguration;
                break;
            }
        }
        addExampleArg = Boolean.valueOf(properties.getProperty("noExample")) && (
                null != globalTableConfiguration &&
                        (globalTableConfiguration.isCountByExampleStatementEnabled()
                                || globalTableConfiguration.isDeleteByExampleStatementEnabled()
                                || globalTableConfiguration.isSelectByExampleStatementEnabled()
                                || globalTableConfiguration.isUpdateByExampleStatementEnabled()
                        )
        );
        if (null != superDao) {
            FullyQualifiedJavaType superDaoFqjt = new FullyQualifiedJavaType(superDao);
            FullyQualifiedJavaType objectFqjt = new FullyQualifiedJavaType(context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + introspectedTable.getFullyQualifiedTable().getDomainObjectName());
            FullyQualifiedJavaType objectExample = new FullyQualifiedJavaType(objectFqjt.getFullyQualifiedName() + "Example");
            superDaoFqjt.addTypeArgument(objectFqjt);
            if (addExampleArg) {
                superDaoFqjt.addTypeArgument(objectExample);
            }
            interfaze.addSuperInterface(superDaoFqjt);
            interfaze.addImportedType(new FullyQualifiedJavaType(superDao));
        }
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
        if (!excuted) {
            excuted = true;
            if (null != superService) {
                superServiceInterfaceGenerated(introspectedTable, files);
                superDaoInterfaceGenerated(introspectedTable, files);
            }
        }
        serviceInterfaceGenerated(introspectedTable, files);
        return files;
    }

    private void superServiceInterfaceGenerated(IntrospectedTable introspectedTable, List<GeneratedJavaFile> files) {
        superServiceInterfaceFqjt = new FullyQualifiedJavaType(superService);
        superServiceInterfaceFqjt.addTypeArgument(new FullyQualifiedJavaType("T"));

        Interface interfaze = new Interface(superServiceInterfaceFqjt);

        interfaze.setVisibility(JavaVisibility.PUBLIC);
        addExampleArg = superServiceMethodGenerated(interfaze, introspectedTable);
        if (addExampleArg) {
            superServiceInterfaceFqjt.addTypeArgument(new FullyQualifiedJavaType("Example"));
        }
        GeneratedJavaFile javaFile = new GeneratedJavaFile(interfaze, context.getJavaModelGeneratorConfiguration()
                .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());
        files.add(javaFile);

        superServiceImplementGenerated(introspectedTable, files);
    }

    private void superDaoInterfaceGenerated(IntrospectedTable introspectedTable, List<GeneratedJavaFile> files) {
        FullyQualifiedJavaType superDaoInterfaceFqjt = new FullyQualifiedJavaType(superDao);
        superDaoInterfaceFqjt.addTypeArgument(new FullyQualifiedJavaType("T"));

        Interface interfaze = new Interface(superDaoInterfaceFqjt);

        interfaze.setVisibility(JavaVisibility.PUBLIC);
        addExampleArg = superDaoInterfaceMethodGenerated(interfaze, introspectedTable);
        if (addExampleArg) {
            superDaoInterfaceFqjt.addTypeArgument(new FullyQualifiedJavaType("Example"));
        }
        GeneratedJavaFile javaFile = new GeneratedJavaFile(interfaze, context.getJavaModelGeneratorConfiguration()
                .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());
        files.add(javaFile);
    }

    private boolean superDaoInterfaceMethodGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType list = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType t = new FullyQualifiedJavaType("T");
        FullyQualifiedJavaType exmple = new FullyQualifiedJavaType("Example");
        list.addTypeArgument(t);

        //countByExample
        Method method;
        Parameter obj = new Parameter(t, "obj");
        Parameter example = new Parameter(exmple, "example");
        Parameter key = new Parameter(FullyQualifiedJavaType.getStringInstance(), "key");


        if (null != globalTableConfiguration && globalTableConfiguration.isCountByExampleStatementEnabled()) {
            addExampleArg = true && Boolean.valueOf(properties.getProperty("noExample"));
            method = new Method("countByExample");
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            if (addExampleArg){
                method.addParameter(example);
            } else {
                method.addParameter(obj);
            }

            interfaze.addMethod(method);
        }
        if (null != globalTableConfiguration && globalTableConfiguration.isDeleteByExampleStatementEnabled()) {
            addExampleArg = true && Boolean.valueOf(properties.getProperty("noExample"));
            method = new Method("deleteByExample");

            if (addExampleArg){
                method.addParameter(example);
            } else {
                method.addParameter(obj);
            }

            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isDeleteByPrimaryKeyStatementEnabled()) {
            method = new Method("deleteByPrimaryKey");
            method.addParameter(key);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isInsertStatementEnabled()) {
            method = new Method("insert");
            method.addParameter(obj);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("insertSelective");
            method.addParameter(obj);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isSelectByExampleStatementEnabled()) {
            addExampleArg = true && Boolean.valueOf(properties.getProperty("noExample"));
            method = new Method("selectByExampleWithBLOBs");
            method.setReturnType(list);
            if (addExampleArg){
                method.addParameter(example);
            } else {
                method.addParameter(obj);
            }
            interfaze.addMethod(method);

            method = new Method("selectByExample");
            if (addExampleArg){
                method.addParameter(example);
            } else {
                method.addParameter(obj);
            }
            method.setReturnType(list);
            interfaze.addMethod(method);

            interfaze.addImportedType(FullyQualifiedJavaType.getNewListInstance());
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isSelectByPrimaryKeyStatementEnabled()) {
            method = new Method("selectByPrimaryKey");
            method.addParameter(key);
            method.setReturnType(t);
            interfaze.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isUpdateByExampleStatementEnabled()) {
            addExampleArg = true && Boolean.valueOf(properties.getProperty("noExample"));
            method = new Method("updateByExampleSelective");
            if (addExampleArg){
                method.addParameter(example);
            } else {
                method.addParameter(obj);
            }
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("updateByExampleWithBLOBs");
            if (addExampleArg){
                method.addParameter(example);
            } else {
                method.addParameter(obj);
            }
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("updateByExample");
            if (addExampleArg){
                method.addParameter(example);
            } else {
                method.addParameter(obj);
            }
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("updateByExampleWithoutBLOBs");
            if (addExampleArg){
                method.addParameter(example);
            } else {
                method.addParameter(obj);
            }
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }
        if (null != globalTableConfiguration && globalTableConfiguration.isUpdateByPrimaryKeyStatementEnabled()) {
            method = new Method("updateByPrimaryKeySelective");
            method.addParameter(obj);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("updateByPrimaryKeyWithBLOBs");
            method.addParameter(obj);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("updateByPrimaryKeyWithoutBLOBs");
            method.addParameter(obj);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }

        return addExampleArg;
    }

    private boolean superServiceMethodGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType list = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType t = new FullyQualifiedJavaType("T");
        FullyQualifiedJavaType exmple = new FullyQualifiedJavaType("Example");
        list.addTypeArgument(t);

        //countByExample
        Method method;
        Parameter obj = new Parameter(t, "obj");
        Parameter example = new Parameter(exmple, "example");
        Parameter key = new Parameter(FullyQualifiedJavaType.getStringInstance(), "key");


        if (null != globalTableConfiguration && globalTableConfiguration.isCountByExampleStatementEnabled()) {
            addExampleArg = true;
            method = new Method("countByExample");
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.addParameter(example);

            interfaze.addMethod(method);
        }
        if (null != globalTableConfiguration && globalTableConfiguration.isDeleteByExampleStatementEnabled()) {
            addExampleArg = true;
            method = new Method("deleteByExample");
            method.addParameter(example);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isDeleteByPrimaryKeyStatementEnabled()) {
            method = new Method("deleteByPrimaryKey");
            method.addParameter(key);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isInsertStatementEnabled()) {
            method = new Method("insert");
            method.addParameter(obj);
            method.setReturnType(new FullyQualifiedJavaType("T"));
            interfaze.addMethod(method);

            method = new Method("insertSelective");
            method.addParameter(obj);
            method.setReturnType(new FullyQualifiedJavaType("T"));
            interfaze.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isSelectByExampleStatementEnabled()) {
            addExampleArg = true;
            method = new Method("selectByExampleWithBLOBs");
            method.setReturnType(list);
            method.addParameter(example);
            interfaze.addMethod(method);

            method = new Method("selectByExample");
            method.addParameter(example);
            method.setReturnType(list);
            interfaze.addMethod(method);

            interfaze.addImportedType(FullyQualifiedJavaType.getNewListInstance());
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isSelectByPrimaryKeyStatementEnabled()) {
            method = new Method("selectByPrimaryKey");
            method.addParameter(key);
            method.setReturnType(t);
            interfaze.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isUpdateByExampleStatementEnabled()) {
            addExampleArg = true;
            method = new Method("updateByExampleSelective");
            method.addParameter(example);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("updateByExampleWithBLOBs");
            method.addParameter(example);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("updateByExample");
            method.addParameter(example);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("updateByExampleWithoutBLOBs");
            method.addParameter(example);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }
        if (null != globalTableConfiguration && globalTableConfiguration.isUpdateByPrimaryKeyStatementEnabled()) {
            method = new Method("updateByPrimaryKeySelective");
            method.addParameter(obj);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("updateByPrimaryKeyWithBLOBs");
            method.addParameter(obj);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);

            method = new Method("updateByPrimaryKeyWithoutBLOBs");
            method.addParameter(obj);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            interfaze.addMethod(method);
        }

        method = new Method("getDao");
        FullyQualifiedJavaType superDaoFqjt = new FullyQualifiedJavaType(superDao);
        superDaoFqjt.addTypeArgument(obj.getType());
        if (addExampleArg) {
            superDaoFqjt.addTypeArgument(example.getType());
        }
        method.setReturnType(superDaoFqjt);
        interfaze.addMethod(method);
        interfaze.addImportedType(new FullyQualifiedJavaType(superDao));

        method = new Method("setPrimaryKey");
        method.addParameter(obj);
        interfaze.addMethod(method);

        return addExampleArg;
    }

    private boolean superServiceImplementMethodGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType list = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType t = new FullyQualifiedJavaType("T");
        FullyQualifiedJavaType exmple = new FullyQualifiedJavaType("Example");
        list.addTypeArgument(t);

        boolean addExampleArg = false;
        //countByExample
        Method method;
        Parameter obj = new Parameter(t, "obj");
        Parameter example = new Parameter(exmple, "example");
        Parameter key = new Parameter(FullyQualifiedJavaType.getStringInstance(), "key");
        if (null != globalTableConfiguration && globalTableConfiguration.isCountByExampleStatementEnabled()) {
            addExampleArg = true;
            method = new Method("countByExample");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.addParameter(example);
            method.addBodyLine("return getDao().countByExample(example);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);
        }
        if (null != globalTableConfiguration && globalTableConfiguration.isDeleteByExampleStatementEnabled()) {
            addExampleArg = true;
            method = new Method("deleteByExample");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(example);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.addBodyLine("return getDao().deleteByExample(example);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isDeleteByPrimaryKeyStatementEnabled()) {
            method = new Method("deleteByPrimaryKey");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(key);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.addBodyLine("return getDao().deleteByPrimaryKey(key);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isInsertStatementEnabled()) {
            method = new Method("insert");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(obj);
            method.setReturnType(new FullyQualifiedJavaType("T"));
            method.addBodyLine("int num = getDao().insert(obj);");
            method.addBodyLine("if(0 == num){");
            method.addBodyLine("return null;");
            method.addBodyLine("}");
            method.addBodyLine("return obj;");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);

            method = new Method("insertSelective");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(obj);
            method.setReturnType(new FullyQualifiedJavaType("T"));
            method.addBodyLine("int num = getDao().insertSelective(obj);");
            method.addBodyLine("if(0 == num){");
            method.addBodyLine("return null;");
            method.addBodyLine("}");
            method.addBodyLine("return obj;");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isSelectByExampleStatementEnabled()) {
            addExampleArg = true;
            method = new Method("selectByExampleWithBLOBs");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setReturnType(list);
            method.addParameter(example);
            method.addBodyLine("return getDao().selectByExampleWithBLOBs(example);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);

            method = new Method("selectByExample");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(example);
            method.setReturnType(list);
            method.addBodyLine("return getDao().selectByExampleWithBLOBs(example);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);

            topLevelClass.addImportedType(FullyQualifiedJavaType.getNewListInstance());
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isSelectByPrimaryKeyStatementEnabled()) {
            method = new Method("selectByPrimaryKey");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(key);
            method.setReturnType(t);
            method.addBodyLine("return getDao().selectByPrimaryKey(key);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);
        }

        if (null != globalTableConfiguration && globalTableConfiguration.isUpdateByExampleStatementEnabled()) {
            addExampleArg = true;
            method = new Method("updateByExampleSelective");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(example);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.addBodyLine("return getDao().updateByExampleSelective(example);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);

            method = new Method("updateByExampleWithBLOBs");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(example);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.addBodyLine("return getDao().updateByExampleWithBLOBs(example);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);

            method = new Method("updateByExample");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(example);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.addBodyLine("return getDao().updateByExample(example);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);

            method = new Method("updateByExampleWithoutBLOBs");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(example);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.addBodyLine("return getDao().updateByExampleWithoutBLOBs(example);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);
        }
        if (null != globalTableConfiguration && globalTableConfiguration.isUpdateByPrimaryKeyStatementEnabled()) {
            method = new Method("updateByPrimaryKeySelective");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(obj);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.addBodyLine("return getDao().updateByPrimaryKeySelective(obj);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);

            method = new Method("updateByPrimaryKeyWithBLOBs");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(obj);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.addBodyLine("return getDao().updateByPrimaryKeyWithBLOBs(obj);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);

            method = new Method("updateByPrimaryKeyWithoutBLOBs");
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(obj);
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.addBodyLine("return getDao().updateByPrimaryKeyWithoutBLOBs(obj);");
            method.addAnnotation("@Override");
            topLevelClass.addMethod(method);
        }

        return addExampleArg;
    }

    private void superServiceImplementGenerated(IntrospectedTable introspectedTable, List<GeneratedJavaFile> files) {
        FullyQualifiedJavaType serviceImplementFqjt = new FullyQualifiedJavaType(superServiceImpl);
        serviceImplementFqjt.addTypeArgument(new FullyQualifiedJavaType("T"));

        TopLevelClass topLevelClass = new TopLevelClass(serviceImplementFqjt);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        boolean addExampleArg = superServiceImplementMethodGenerated(topLevelClass, introspectedTable);
        if (addExampleArg) {
            serviceImplementFqjt.addTypeArgument(new FullyQualifiedJavaType("Example"));
        }
        topLevelClass.addSuperInterface(superServiceInterfaceFqjt);
        topLevelClass.addImportedType(superServiceInterfaceFqjt);
        topLevelClass.setAbstract(true);

        GeneratedJavaFile javaFile = new GeneratedJavaFile(topLevelClass, context.getJavaModelGeneratorConfiguration()
                .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());

        files.add(javaFile);
    }

    private void serviceInterfaceGenerated(IntrospectedTable introspectedTable, List<GeneratedJavaFile> files) {
        String objectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        serviceInterfaceFqjt = new FullyQualifiedJavaType(serviceTargetPackage + ".I" + objectName + "Service");

        FullyQualifiedJavaType objectFqjt = new FullyQualifiedJavaType(context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + objectName);
        FullyQualifiedJavaType objectExample = new FullyQualifiedJavaType(objectFqjt.getFullyQualifiedName() + "Example");
        Interface interfaze = new Interface(serviceInterfaceFqjt);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        if (null != superService) {
            interfaze.addImportedType(new FullyQualifiedJavaType(superService));

            FullyQualifiedJavaType superServiceInterfaceFqjt = new FullyQualifiedJavaType(superService);
            superServiceInterfaceFqjt.addTypeArgument(objectFqjt);
            interfaze.addImportedType(objectFqjt);
            if (addExampleArg) {
                superServiceInterfaceFqjt.addTypeArgument(objectExample);
                interfaze.addImportedType(objectExample);
            }
            interfaze.addSuperInterface(superServiceInterfaceFqjt);
        }
        GeneratedJavaFile javaFile = new GeneratedJavaFile(interfaze, context.getJavaModelGeneratorConfiguration()
                .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());

        files.add(javaFile);

        serviceImplementGenerated(introspectedTable, files);
    }

    private void serviceImplementGenerated(IntrospectedTable introspectedTable, List<GeneratedJavaFile> files) {
        String objectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        FullyQualifiedJavaType serviceImplementFqjt = new FullyQualifiedJavaType(serviceTargetPackage + ".impl." + objectName + "ServiceImpl");
        FullyQualifiedJavaType objectFqjt = new FullyQualifiedJavaType(context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + objectName);
        FullyQualifiedJavaType objectExample = new FullyQualifiedJavaType(objectFqjt.getFullyQualifiedName() + "Example");

        TopLevelClass topLevelClass = new TopLevelClass(serviceImplementFqjt);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        if (null != superService) {
            topLevelClass.addImportedType(new FullyQualifiedJavaType(superServiceImpl));

            FullyQualifiedJavaType superServiceImplFqjt = new FullyQualifiedJavaType(superServiceImpl);
            superServiceImplFqjt.addTypeArgument(objectFqjt);
            topLevelClass.addImportedType(objectFqjt);
            if (addExampleArg) {
                superServiceImplFqjt.addTypeArgument(objectExample);
                topLevelClass.addImportedType(objectExample);
            }
            topLevelClass.setSuperClass(superServiceImplFqjt);
        }

        String daoPackage = context.getJavaClientGeneratorConfiguration().getTargetPackage();
        FullyQualifiedJavaType daoFqjt = new FullyQualifiedJavaType(daoPackage + "." + objectName + "Mapper");
        Field field = new Field("dao", daoFqjt);
        field.setVisibility(JavaVisibility.PRIVATE);
        field.addAnnotation("@Autowired");
        topLevelClass.addImportedType(daoFqjt);
        topLevelClass.addField(field);
        //getDao
        Method method = new Method("getDao");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);

        topLevelClass.addImportedType(new FullyQualifiedJavaType(superDao));
        FullyQualifiedJavaType baseDaoFqjt = new FullyQualifiedJavaType(superDao);
        baseDaoFqjt.addTypeArgument(objectFqjt);

        if (addExampleArg) {
            baseDaoFqjt.addTypeArgument(objectExample);
        }
        method.setReturnType(baseDaoFqjt);

        method.addBodyLine("return this.dao;");
        topLevelClass.addMethod(method);

        //setPrimaryKey
        method = new Method("setPrimaryKey");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        String lowerObjectName = StringUtils.lowerString(objectName, 0, 1);
        Parameter parameter = new Parameter(objectFqjt, lowerObjectName);
        method.addParameter(parameter);

        method.addBodyLine("");
        StringBuilder sb;
        boolean importedUUID = false;
        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            if (column.getFullyQualifiedJavaType().getShortName().equals("String")) {
                sb = new StringBuilder();
                sb.append("if(!StringUtils.isEmpty(");
                sb.append(lowerObjectName);
                sb.append(".");
                sb.append(JavaBeansUtil.getGetterMethodName(column.getJavaProperty(), column.getFullyQualifiedJavaType()));
                sb.append("())){");
                method.addBodyLine(sb.toString());
                sb = new StringBuilder();
                sb.append(lowerObjectName);
                sb.append(".");
                sb.append(JavaBeansUtil.getSetterMethodName(column.getJavaProperty()));
                sb.append("(UUID.randomUUID().toString());");
                method.addBodyLine(sb.toString());
                method.addBodyLine("}");
                if (!importedUUID) {
                    importedUUID = true;
                    topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.UUID"));
                }
            }
        }


        topLevelClass.addMethod(method);

        //
        topLevelClass.addSuperInterface(serviceInterfaceFqjt);
        topLevelClass.addImportedType(serviceInterfaceFqjt);

        //spring
        topLevelClass.addAnnotation("@Component(\"" + objectName + "Service\")");
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Component"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.util.StringUtils"));

        GeneratedJavaFile javaFile = new GeneratedJavaFile(topLevelClass, context.getJavaModelGeneratorConfiguration()
                .getTargetProject(),
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());

        files.add(javaFile);
    }
}
