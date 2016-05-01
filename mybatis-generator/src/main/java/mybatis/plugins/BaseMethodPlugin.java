package mybatis.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.StringUtils;

import java.util.*;

/**
 * Created by Beeant on 2016/1/17 0017.
 */
public class BaseMethodPlugin extends PluginAdapter {

    private String targetProject;
    private String servicePackage;
    private String serviceImplPackage;
    private String controllerPackage;
    private String dtoName;
    private String dtoNameAlias;
    private String dtoExampleName;
    private String serviceNameAlias;
    private String daoNameAlias;
    private String baseServiceInterfaceName;
    private String baseServiceImplementsName;
    private String baseControllerName;
    private String baseDaoName;
    private String commonReturnType;
    private String requiresPermissionsAnnotation;
    private DefaultJavaFormatter defaultJavaFormatter = new DefaultJavaFormatter();

    private Parameter paramModel;
    private Parameter paramKey;
    private Parameter paramDto;
    private Parameter paramRowBounds;

    private IntrospectedColumn keyColumn;

    private FullyQualifiedJavaType dtoFqjt;
    private FullyQualifiedJavaType dtoExampleFqjt;
    private FullyQualifiedJavaType serviceInterfaceFqjt;
    private FullyQualifiedJavaType daoFqjt;
    private FullyQualifiedJavaType baseDaoFqjt;
    private FullyQualifiedJavaType baseControllerFqjt;
    private FullyQualifiedJavaType commonReturnTypeFqjt;
    private FullyQualifiedJavaType modelAndViewFqjt = new FullyQualifiedJavaType("org.springframework.web.servlet.ModelAndView");
    private FullyQualifiedJavaType modelFqjt = new FullyQualifiedJavaType("org.springframework.ui.Model");
    private FullyQualifiedJavaType requestMappingFqjt = new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping");
    private FullyQualifiedJavaType requestParamFqjt = new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestParam");
    private FullyQualifiedJavaType rowBoundsFqjt = new FullyQualifiedJavaType("org.apache.ibatis.session.RowBounds");
    private FullyQualifiedJavaType requiresPermissionsFqjt = new FullyQualifiedJavaType("org.apache.shiro.authz.annotation.RequiresPermissions");

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean validate(List<String> warnings) {
        targetProject = properties.getProperty("targetProject");
        servicePackage = properties.getProperty("servicePackage");
        controllerPackage = properties.getProperty("controllerPackage");
        serviceImplPackage = properties.getProperty("serviceImplPackage");
        baseServiceInterfaceName = properties.getProperty("baseServiceInterface");
        baseServiceImplementsName = properties.getProperty("baseServiceImplements");
        baseDaoName = properties.getProperty("baseDao");
        baseControllerName = properties.getProperty("baseController");
        commonReturnType = properties.getProperty("commonReturnType");
        commonReturnTypeFqjt = new FullyQualifiedJavaType(commonReturnType);
        paramModel = new Parameter(modelFqjt,"model");
        paramKey = new Parameter(FullyQualifiedJavaType.getStringInstance(),"key");
        paramKey.addAnnotation("@RequestParam");
        paramRowBounds = new Parameter(rowBoundsFqjt,"rowBounds");
        return true;
    }

    private void baseValue(IntrospectedTable introspectedTable){
        IntrospectedTable table = introspectedTable;
        List<IntrospectedColumn> keyColumns = table.getPrimaryKeyColumns();
        if(keyColumns.size() > 0){
            keyColumn = keyColumns.get(0);
        }
        String baseRecordType = introspectedTable.getBaseRecordType();
        dtoName = JavaBeansUtil.getCamelCaseString(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime(), true);
        dtoNameAlias = StringUtils.lowerString(dtoName, 0, 1);
        dtoFqjt = new FullyQualifiedJavaType(baseRecordType);
        paramDto = new Parameter(dtoFqjt, "record");
        requiresPermissionsAnnotation = "@RequiresPermissions(\"%s\")";
        requiresPermissionsAnnotation = requiresPermissionsAnnotation.replace("%s",dtoNameAlias+":%s:*");
        dtoExampleName = dtoName.concat("Example");
        dtoExampleFqjt = new FullyQualifiedJavaType(baseRecordType.concat("Example"));

        daoFqjt = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        daoNameAlias = StringUtils.lowerString(daoFqjt.getShortName(), 0, 1);

        serviceNameAlias = StringUtils.lowerString(dtoName,0,1).concat("Service");

        baseDaoFqjt =  new FullyQualifiedJavaType(baseDaoName);
        baseDaoFqjt.addTypeArgument(dtoFqjt);
        baseDaoFqjt.addTypeArgument(dtoExampleFqjt);

        baseControllerFqjt = new FullyQualifiedJavaType(baseControllerName);
        baseControllerFqjt.addTypeArgument(dtoFqjt);
        baseControllerFqjt.addTypeArgument(dtoExampleFqjt);
    };

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
        files.add(generateServiceInterface());
        files.add(generateServiceImpl());
        files.add(generateController());
        return files;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        baseValue(introspectedTable);

        interfaze.addSuperInterface(baseDaoFqjt);
        interfaze.addImportedType(baseDaoFqjt);

        //remove functions that IBaseMybatisDao defined
        List<Method> methods = interfaze.getMethods();
        Map<String, Boolean> removeMethods = new HashMap<String, Boolean>();
        removeMethods.put("countByCondition", true);
        removeMethods.put("deleteByCondition", true);
        removeMethods.put("deleteByPrimaryKey", true);
        removeMethods.put("insert", true);
        removeMethods.put("insertSelective", true);
        removeMethods.put("selectByConditionWithRowbounds", true);
        removeMethods.put("selectByCondition", true);
        removeMethods.put("selectByPrimaryKey", true);
        removeMethods.put("updateByConditionSelective", true);
        removeMethods.put("updateByCondition", true);
        removeMethods.put("updateByPrimaryKeySelective", true);
        removeMethods.put("updateByPrimaryKey", true);
        // get a methods list copy
        List<Method> methodsClone = new ArrayList<Method>();
        for (Method method : methods) {
            methodsClone.add(method);
        }
        // reset imports for interfaze

        for (Method method : methodsClone) {
            if (removeMethods.get(method.getName()) != null) {
                interfaze.getMethods().remove(method);
            }
        }

        Map<String, Boolean> removeImports = new HashMap<String, Boolean>();
        removeImports.put("PackageEnum",true);
        removeImports.put("List",true);
        removeImports.put("Param",true);
        HashSet h=new HashSet();
        for(FullyQualifiedJavaType fqj:interfaze.getImportedTypes()){
            if (removeImports.get(fqj.getShortName()) == null) {
                h.add(fqj);
            }
        }
        interfaze.setImportedTypes(h);

        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        /**
         * add isNotNull method
         */
        Method method = new Method("isNotNull");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        boolean firstField = true;
        StringBuilder sb = new StringBuilder();
        sb.append("return ");
        for (Field field:topLevelClass.getFields()){
            if (StringUtils.pathEquals(field.getName(),"serialVersionUID")){
                continue;
            }
            if (firstField){
                sb.append(" null != this." + field.getName());
                firstField = false;
            } else {
                sb.append(" || null != this." + field.getName());
            }
        }
        sb.append(";");
        method.addBodyLine(sb.toString());

        topLevelClass.addMethod(method);

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    /**
     * generate interface
     *
     */
    private GeneratedJavaFile generateServiceInterface() {
        String serviceInterfaceName = "I" + dtoName + "Service";
        serviceInterfaceFqjt = new FullyQualifiedJavaType(servicePackage + "." + serviceInterfaceName);
        Interface serviceInterface = new Interface(serviceInterfaceFqjt);
        //base service interface
        FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(baseServiceInterfaceName);
        superInterface.addTypeArgument(dtoFqjt);
        superInterface.addTypeArgument(dtoExampleFqjt);

        serviceInterface.addImportedType(superInterface);
        serviceInterface.addSuperInterface(superInterface);

        serviceInterface.setVisibility(JavaVisibility.PUBLIC);
        serviceInterface.addImportedType(dtoExampleFqjt);
        serviceInterface.addImportedType(dtoFqjt);

        return new GeneratedJavaFile(serviceInterface, targetProject, defaultJavaFormatter);
    }

    /**
     * generate service implements
     *
     */
    private GeneratedJavaFile generateServiceImpl() {
        String serviceImpl = dtoName.concat("ServiceImpl");

        FullyQualifiedJavaType supperClass = new FullyQualifiedJavaType(baseServiceImplementsName);
        supperClass.addTypeArgument(dtoFqjt);
        supperClass.addTypeArgument(dtoExampleFqjt);

        FullyQualifiedJavaType serviceImplFqjt = new FullyQualifiedJavaType(serviceImplPackage.concat(".").concat(serviceImpl));

        TopLevelClass topLevelClass = new TopLevelClass(serviceImplFqjt);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        // spring Component annotation
        topLevelClass.addAnnotation("@Component(\"".concat(serviceImpl).concat("\")"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Component"));

        // implements base service implements
        topLevelClass.setSuperClass(supperClass);
        topLevelClass.addImportedType(supperClass);

        // super interface
        topLevelClass.addSuperInterface(serviceInterfaceFqjt);
        topLevelClass.addImportedType(serviceInterfaceFqjt);

        topLevelClass.addImportedType(dtoExampleFqjt);
        topLevelClass.addImportedType(dtoFqjt);

        /**
         * java file body
         */

        // dao annotation
        Field dao = new Field(daoNameAlias, daoFqjt);
        dao.addAnnotation("@Autowired");
        dao.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(dao);
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        topLevelClass.addImportedType(daoFqjt);

        // getDao Method
        Method getDao = new Method("getDao");
        getDao.addAnnotation("@Override");
        getDao.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addImportedType(baseDaoFqjt);
        getDao.setReturnType(baseDaoFqjt);
        getDao.addBodyLine("return this.".concat(daoNameAlias).concat(";"));
        topLevelClass.addMethod(getDao);

        // setKey Method
        Method setKey = new Method("setKey");
        setKey.addAnnotation("@Override");
        setKey.setVisibility(JavaVisibility.PUBLIC);
        setKey.addParameter(paramDto);
        topLevelClass.addImportedType(dtoFqjt);
        if (keyColumn.getFullyQualifiedJavaType().getShortName().equals("String")){
            StringBuilder sb = new StringBuilder();
            String key = JavaBeansUtil.getCamelCaseString(keyColumn.getActualColumnName(), true);
            sb.append("if(record.get");
            sb.append(key);
            sb.append("() == null || \"\".equals(record.get");
            sb.append(key);
            sb.append("()) {");
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
        topLevelClass.addMethod(setKey);

        return new GeneratedJavaFile(topLevelClass, targetProject, defaultJavaFormatter);
    }

    /**
     * generate controller
     *
     */
    private GeneratedJavaFile generateController(){
        FullyQualifiedJavaType controllerFqjt = new FullyQualifiedJavaType(controllerPackage + "." + dtoName + "Controller");
        TopLevelClass topLevelClass = new TopLevelClass(controllerFqjt);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        // @Controller annotation
        topLevelClass.addAnnotation("@Controller");
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Controller"));
        // @RequestMapping annotation
        topLevelClass.addAnnotation("@RequestMapping(\"" + dtoNameAlias + "\")");
        topLevelClass.addImportedType(requestMappingFqjt);

        topLevelClass.setSuperClass(baseControllerFqjt);
        topLevelClass.addImportedType(baseControllerFqjt);

        topLevelClass.addImportedType(dtoFqjt);
        topLevelClass.addImportedType(dtoExampleFqjt);

        /**
         * fields
         */
        /**
         * java file body
         */
        Field service = new Field(serviceNameAlias, serviceInterfaceFqjt);
        service.addAnnotation("@Autowired");
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        service.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(service);
        topLevelClass.addImportedType(serviceInterfaceFqjt);
        topLevelClass.addImportedType(FullyQualifiedJavaType.getNewListInstance());
        topLevelClass.addImportedType(commonReturnTypeFqjt);
        /**
         * methods
         */
        generateControllerMethod(topLevelClass);

        topLevelClass.addImportedType(requiresPermissionsFqjt);
        topLevelClass.addImportedType(modelFqjt);
        topLevelClass.addImportedType(modelAndViewFqjt);
        topLevelClass.addImportedType(requestParamFqjt);
        topLevelClass.addImportedType(rowBoundsFqjt);

        return new GeneratedJavaFile(topLevelClass, targetProject, defaultJavaFormatter);
    }

    private void generateControllerMethod(TopLevelClass topLevelClass){
        // getBasePath
        controllerGetBasePathMethod(topLevelClass);

        // getService
        controllerGetServiceMethod(topLevelClass);

        // Override create
        controllerCreateMethod(topLevelClass);

        // Override delete
        controllerDeleteMethod(topLevelClass);

        // Override edit
        controllerEditMethod(topLevelClass);

        // Override view
        controllerViewMethod(topLevelClass);

        // Override browse
        controllerBrowseMethod(topLevelClass);
    }

    private void controllerBrowseMethod(TopLevelClass topLevelClass) {
        Method method = new Method("browse");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.addAnnotation(requiresPermissionsAnnotation.replace("%s", "browse"));
        method.setReturnType(modelAndViewFqjt);
        method.addParameter(paramDto);
        method.addParameter(paramRowBounds);
        method.addParameter(paramModel);

        method.addBodyLine("ModelAndView mav = new ModelAndView(getBasePath().concat(\"browse\"));");
        method.addBodyLine(dtoExampleName + " example = new " + dtoExampleName + "();");
        method.addBodyLine("Message<List<" + dtoName + ">> msg = getService().selectByConditionWithRowbounds(example, rowBounds);");
        method.addBodyLine("mav.addObject(msg);");
        method.addBodyLine("return mav;");
        topLevelClass.addMethod(method);
    }

    private void controllerViewMethod(TopLevelClass topLevelClass) {
        Method method = new Method("view");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.addAnnotation(requiresPermissionsAnnotation.replace("%s", "view"));
        method.setReturnType(modelAndViewFqjt);
        method.addParameter(paramKey);
        method.addParameter(paramModel);

        method.addBodyLine("return super.view(key, model);");
        topLevelClass.addMethod(method);
    }

    private void controllerEditMethod(TopLevelClass topLevelClass) {
        Method method = new Method("edit");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.addAnnotation(requiresPermissionsAnnotation.replace("%s", "edit"));
        method.setReturnType(modelAndViewFqjt);
        method.addParameter(paramDto);
        method.addParameter(paramModel);

        method.addBodyLine("return super.edit(record, model);");
        topLevelClass.addMethod(method);
    }

    private void controllerDeleteMethod(TopLevelClass topLevelClass) {
        Method method = new Method("delete");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.addAnnotation(requiresPermissionsAnnotation.replace("%s", "delete"));
        method.setReturnType(modelAndViewFqjt);
        method.addParameter(paramKey);
        method.addParameter(paramModel);

        method.addBodyLine("return super.delete(key, model);");
        topLevelClass.addMethod(method);
    }

    private void controllerCreateMethod(TopLevelClass topLevelClass) {
        Method method = new Method("create");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.addAnnotation(requiresPermissionsAnnotation.replace("%s", "create"));
        method.setReturnType(modelAndViewFqjt);
        method.addParameter(paramDto);
        method.addParameter(paramModel);

        method.addBodyLine(" return super.create(record, model);");
        topLevelClass.addMethod(method);
    }

    private void controllerGetServiceMethod(TopLevelClass topLevelClass) {
        Method method = new Method("getService");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        FullyQualifiedJavaType baseServiceFqjt = new FullyQualifiedJavaType(baseServiceInterfaceName);
        method.setReturnType(new FullyQualifiedJavaType(baseServiceFqjt.getShortName() + "<" + dtoName + "," + dtoExampleName + ">"));
        topLevelClass.addImportedType(baseServiceFqjt);

        method.addBodyLine("return this." + serviceNameAlias + ";");
        topLevelClass.addMethod(method);
    }

    private void controllerGetBasePathMethod(TopLevelClass topLevelClass) {
        Method method = new Method("getBasePath");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addBodyLine("return \"" + dtoNameAlias + "/\";");
        topLevelClass.addMethod(method);
    }

}
