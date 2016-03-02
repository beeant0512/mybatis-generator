package mybatis.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Beeant on 2016/1/17 0017.
 */
public class ServicePlugin extends PluginAdapter {
    private IntrospectedTable table;

    private String servicePackage;
    private String serviceImplPack;
    private String targetProject;
    private String objName;
    private String daoName;
    private String commonReturnType;

    private DefaultJavaFormatter defaultJavaFormatter = new DefaultJavaFormatter();

    private FullyQualifiedJavaType objExampleFqjt;
    private FullyQualifiedJavaType objFqjt;
    private FullyQualifiedJavaType rowBoundsFqjt = new FullyQualifiedJavaType("org.apache.ibatis.session.RowBounds");
    private FullyQualifiedJavaType iServiceFqjt;
    private FullyQualifiedJavaType listOfObjsFqjt;
    private FullyQualifiedJavaType daoFqjt;

    private Parameter paramObj;
    private Parameter paramObjExample;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean validate(List<String> warnings) {
        servicePackage = properties.getProperty("servicePackage");
        serviceImplPack = properties.getProperty("implementationPackage");
        targetProject = properties.getProperty("targetProject");
        commonReturnType = properties.getProperty("commonReturnType");
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        table = introspectedTable;
        String baseRecordType = introspectedTable.getBaseRecordType();
        objName = JavaBeansUtil.getCamelCaseString(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime(), true);
        objExampleFqjt = new FullyQualifiedJavaType(baseRecordType.concat("Example"));
        objFqjt = new FullyQualifiedJavaType(baseRecordType);

        listOfObjsFqjt = new FullyQualifiedJavaType("java.util.List<".concat(objFqjt.getFullyQualifiedName()).concat(">"));
        paramObj = new Parameter(objFqjt, "record");
        paramObjExample = new Parameter(objExampleFqjt, "example");
        daoFqjt = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());

        daoName = StringUtils.lowerString(daoFqjt.getShortName(), 0, 1);
        List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
        files.add(generateServiceInterface(introspectedTable));
        files.add(generateServiceImpl());
        return files;
    }

    /**
     * generate interface
     *
     * @param introspectedTable
     * @return
     */
    private GeneratedJavaFile generateServiceInterface(IntrospectedTable introspectedTable) {
        String iService = "I".concat(objName).concat("Service");
        iServiceFqjt = new FullyQualifiedJavaType(servicePackage.concat(".").concat(iService));
        Interface interfaze = new Interface(iServiceFqjt);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        interfaze.addImportedType(objExampleFqjt);
        interfaze.addImportedType(objFqjt);
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.session.RowBounds"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));

        deleteByPrimaryKey(interfaze, null);

        insert(interfaze, null);

        insertSelective(interfaze, null);

        selectByConditionWithRowbounds(interfaze, null);

        selectByCondition(interfaze, null);

        selectByPrimaryKey(interfaze, null);

        updateByConditionSelective(interfaze, null);

        updateByCondition(interfaze, null);

        updateByPrimaryKeySelective(interfaze, null);

        updateByPrimaryKey(interfaze, null);

        return new GeneratedJavaFile(interfaze, targetProject, defaultJavaFormatter);
    }

    private GeneratedJavaFile generateServiceImpl() {
        String serviceImpl = objName.concat("ServiceImpl");
        FullyQualifiedJavaType serviceImplFqjt = new FullyQualifiedJavaType(serviceImplPack.concat(".").concat(serviceImpl));
        TopLevelClass topLevelClass = new TopLevelClass(serviceImplFqjt);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addImportedType(objExampleFqjt);
        topLevelClass.addImportedType(objFqjt);
        topLevelClass.addImportedType(iServiceFqjt);
        topLevelClass.addSuperInterface(iServiceFqjt);

        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.session.RowBounds"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.List"));

        topLevelClass.addAnnotation("@Component(\"".concat(objName.concat("ServiceImpl")).concat("\")"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Component"));

        Field dao = new Field(daoName, daoFqjt);
        dao.addAnnotation("@Autowired");
        dao.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(dao);
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        topLevelClass.addImportedType(daoFqjt);

        deleteByPrimaryKey(null, topLevelClass);

        insert(null, topLevelClass);

        insertSelective(null, topLevelClass);

        selectByConditionWithRowbounds(null, topLevelClass);

        selectByCondition(null, topLevelClass);

        selectByPrimaryKey(null, topLevelClass);

        updateByConditionSelective(null, topLevelClass);

        updateByCondition(null, topLevelClass);

        updateByPrimaryKeySelective(null, topLevelClass);

        updateByPrimaryKey(null, topLevelClass);

        if (commonReturnType != null) {
            generateReturnFunc(topLevelClass);
        }

        return new GeneratedJavaFile(topLevelClass, targetProject, defaultJavaFormatter);
    }

    private void generateReturnFunc(TopLevelClass topLevelClass) {
        // getIntegerReturn
        Method methodGetIntegerReturn = new Method();
        methodGetIntegerReturn.setVisibility(JavaVisibility.PRIVATE);
        methodGetIntegerReturn.setName("getIntegerReturn");
        methodGetIntegerReturn.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "num"));
        StringBuilder sbGetIntegerReturn = new StringBuilder();
        // getObjReturn
        Method methodGetObjReturn = new Method();
        methodGetObjReturn.setVisibility(JavaVisibility.PRIVATE);
        methodGetObjReturn.setName("getObjReturn");
        methodGetObjReturn.addParameter(new Parameter(objFqjt, "record"));
        StringBuilder sbGetObjReturn = new StringBuilder();
        // getListReturn
        Method methodGetListReturn = new Method();
        methodGetListReturn.setVisibility(JavaVisibility.PRIVATE);
        methodGetListReturn.setName("getListReturn");
        methodGetListReturn.addParameter(new Parameter(listOfObjsFqjt, "records"));
        StringBuilder sbGetListReturn = new StringBuilder();

        if (commonReturnType == null) {
            // getIntegerReturn
            methodGetIntegerReturn.setReturnType(PrimitiveTypeWrapper.getIntegerInstance());
            sbGetIntegerReturn.append("return num;");
            methodGetIntegerReturn.addBodyLine(sbGetIntegerReturn.toString());

            // getObjReturn
            methodGetObjReturn.setReturnType(objFqjt);
            sbGetObjReturn.append("return record;");
            methodGetObjReturn.addBodyLine(sbGetObjReturn.toString());
            // getListReturn
            methodGetListReturn.setReturnType(listOfObjsFqjt);
            sbGetListReturn.append("return records;");
            methodGetListReturn.addBodyLine(sbGetListReturn.toString());
        } else {
            // getIntegerReturn
            methodGetIntegerReturn.setReturnType(getReturnType(PrimitiveTypeWrapper.getIntegerInstance(), topLevelClass));
            sbGetIntegerReturn.append("Message<Integer> msg = new Message<Integer>();");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 2);
            sbGetIntegerReturn.append("if(num == 0){");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 3);
            /*sbGetIntegerReturn.append("List<String> strings = new ArrayList<>();");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 3);
            sbGetIntegerReturn.append("strings.add(\"操作数据失败\");");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 3);*/
            sbGetIntegerReturn.append("msg.setMsg(\"操作数据失败\");");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 3);
            sbGetIntegerReturn.append("return msg;");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 2);
            sbGetIntegerReturn.append("}");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 2);
            sbGetIntegerReturn.append("msg.setSuccess(true);");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 2);
            sbGetIntegerReturn.append("msg.setData(num);");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 2);
            /*sbGetIntegerReturn.append("List<String> strings = new ArrayList<>();");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 2);
            sbGetIntegerReturn.append("strings.add(\"成功操作\".concat(Integer.toString(num)).concat(\"条数据\"));");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 2);*/
            sbGetIntegerReturn.append("msg.setMsg(\"成功操作\".concat(Integer.toString(num)).concat(\"条数据\"));");
            OutputUtilities.newLine(sbGetIntegerReturn);
            OutputUtilities.javaIndent(sbGetIntegerReturn, 2);
            sbGetIntegerReturn.append("return msg;");
            methodGetIntegerReturn.addBodyLine(sbGetIntegerReturn.toString());

            // getObjReturn
            methodGetObjReturn.setReturnType(getReturnType(objFqjt, topLevelClass));
            sbGetObjReturn.append("Message<" + objFqjt.getShortName()+ "> msg = new Message<" + objFqjt.getShortName()+ ">();");
            OutputUtilities.newLine(sbGetObjReturn);
            OutputUtilities.javaIndent(sbGetObjReturn, 2);
            sbGetObjReturn.append("if(record == null){");
            OutputUtilities.newLine(sbGetObjReturn);
            OutputUtilities.javaIndent(sbGetObjReturn, 3);
            /*sbGetObjReturn.append("List<String> strings = new ArrayList<>();");
            OutputUtilities.newLine(sbGetObjReturn);
            OutputUtilities.javaIndent(sbGetObjReturn, 3);
            sbGetObjReturn.append("strings.add(\"数据不存在\");");
            OutputUtilities.newLine(sbGetObjReturn);
            OutputUtilities.javaIndent(sbGetObjReturn, 3);*/
            sbGetObjReturn.append("msg.setMsg(\"数据不存在\");");
            OutputUtilities.newLine(sbGetObjReturn);
            OutputUtilities.javaIndent(sbGetObjReturn, 3);
            sbGetObjReturn.append("return msg;");
            OutputUtilities.newLine(sbGetObjReturn);
            OutputUtilities.javaIndent(sbGetObjReturn, 2);
            sbGetObjReturn.append("}");
            OutputUtilities.newLine(sbGetObjReturn);
            OutputUtilities.javaIndent(sbGetObjReturn, 2);
            sbGetObjReturn.append("msg.setSuccess(true);");
            OutputUtilities.newLine(sbGetObjReturn);
            OutputUtilities.javaIndent(sbGetObjReturn, 2);
            sbGetObjReturn.append("msg.setData(record);");
            OutputUtilities.newLine(sbGetObjReturn);
            OutputUtilities.javaIndent(sbGetObjReturn, 2);
            sbGetObjReturn.append("return msg;");
            methodGetObjReturn.addBodyLine(sbGetObjReturn.toString());

            // getListReturn
            methodGetListReturn.setReturnType(getReturnType(listOfObjsFqjt, topLevelClass));
            sbGetListReturn.append("Message<List<" + objFqjt.getShortName() + ">> msg = new Message<List<" + objFqjt.getShortName() + ">>();");
            OutputUtilities.newLine(sbGetListReturn);
            OutputUtilities.javaIndent(sbGetListReturn, 2);
            sbGetListReturn.append("if( records.size() == 0){");
      /*      OutputUtilities.newLine(sbGetListReturn);
            OutputUtilities.javaIndent(sbGetListReturn, 3);
            sbGetListReturn.append("List<String> strings = new ArrayList<>();");
            OutputUtilities.newLine(sbGetListReturn);
            OutputUtilities.javaIndent(sbGetListReturn, 3);
            sbGetListReturn.append("strings.add(\"数据不存在\");");*/
            OutputUtilities.newLine(sbGetListReturn);
            OutputUtilities.javaIndent(sbGetListReturn, 3);
            sbGetListReturn.append("msg.setMsg(\"数据不存在\");");
            OutputUtilities.newLine(sbGetListReturn);
            OutputUtilities.javaIndent(sbGetListReturn, 3);
            sbGetListReturn.append("return msg;");
            OutputUtilities.newLine(sbGetListReturn);
            OutputUtilities.javaIndent(sbGetListReturn, 2);
            sbGetListReturn.append("}");
            OutputUtilities.newLine(sbGetListReturn);
            OutputUtilities.javaIndent(sbGetListReturn, 2);
            sbGetListReturn.append("msg.setSuccess(true);");
            OutputUtilities.newLine(sbGetListReturn);
            OutputUtilities.javaIndent(sbGetListReturn, 2);
            sbGetListReturn.append("msg.setData(records);");
            OutputUtilities.newLine(sbGetListReturn);
            OutputUtilities.javaIndent(sbGetListReturn, 2);
            sbGetListReturn.append("return msg;");
            methodGetListReturn.addBodyLine(sbGetListReturn.toString());
        }

        topLevelClass.addMethod(methodGetIntegerReturn);
        topLevelClass.addMethod(methodGetObjReturn);
        topLevelClass.addMethod(methodGetListReturn);
    }

    private FullyQualifiedJavaType getReturnType(FullyQualifiedJavaType type, CompilationUnit compilationUnit) {
        if (commonReturnType == null) {
            return type;
        }
        FullyQualifiedJavaType commonReturn = new FullyQualifiedJavaType(commonReturnType);
        String returnType = commonReturn.getShortName().concat("<").concat(type.getShortName()).concat(">");
        if (compilationUnit != null) {
            compilationUnit.addImportedType(commonReturn);
        }
        return new FullyQualifiedJavaType(returnType);
    }


    /**
     * updateByConditionSelective
     */
    private void updateByConditionSelective(Interface interfaze, TopLevelClass topLevelClass) {
        Method method = new Method("updateByConditionSelective");
        method.addParameter(paramObj);
        method.addParameter(paramObjExample);
        if (interfaze != null) {
            method.setReturnType(getReturnType(PrimitiveTypeWrapper.getIntegerInstance(), interfaze));
            interfaze.addMethod(method);
        } else if (topLevelClass != null) {
            method.setReturnType(getReturnType(PrimitiveTypeWrapper.getIntegerInstance(), topLevelClass));
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addAnnotation("@Override");
            StringBuilder sb = new StringBuilder();
            sb.append("int num = ");
            sb.append(daoName);
            sb.append(".updateByConditionSelective(record, example);");
            sb.append(getFuncReturn(sb, "int", topLevelClass));
            method.addBodyLine(sb.toString());
            topLevelClass.addMethod(method);
        }
    }

    /**
     * updateByCondition
     */
    private void updateByCondition(Interface interfaze, TopLevelClass topLevelClass) {
        Method method = new Method("updateByCondition");
        method.addParameter(paramObj);
        method.addParameter(paramObjExample);
        if (interfaze != null) {
            method.setReturnType(getReturnType(PrimitiveTypeWrapper.getIntegerInstance(), interfaze));
            interfaze.addMethod(method);
        } else if (topLevelClass != null) {
            method.setReturnType(getReturnType(PrimitiveTypeWrapper.getIntegerInstance(), topLevelClass));
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addAnnotation("@Override");
            StringBuilder sb = new StringBuilder();
            sb.append("int num = ");
            sb.append(daoName);
            sb.append(".updateByCondition(record, example);");
            sb.append(getFuncReturn(sb, "int", topLevelClass));
            method.addBodyLine(sb.toString());
            topLevelClass.addMethod(method);
        }
    }

    /**
     * updateByPrimaryKeySelective
     */
    private void updateByPrimaryKeySelective(Interface interfaze, TopLevelClass topLevelClass) {
        Method method = new Method("updateByPrimaryKeySelective");
        method.addParameter(paramObj);

        if (interfaze != null) {
            method.setReturnType(getReturnType(PrimitiveTypeWrapper.getIntegerInstance(), interfaze));
            interfaze.addMethod(method);
        } else if (topLevelClass != null) {
            method.setReturnType(getReturnType(PrimitiveTypeWrapper.getIntegerInstance(), topLevelClass));
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addAnnotation("@Override");
            StringBuilder sb = new StringBuilder();
            sb.append("int num = ");
            sb.append(daoName);
            sb.append(".updateByPrimaryKeySelective(record);");
            sb.append(getFuncReturn(sb, "int", topLevelClass));
            method.addBodyLine(sb.toString());
            topLevelClass.addMethod(method);
        }
    }

    /**
     * updateByPrimaryKey
     */
    private void updateByPrimaryKey(Interface interfaze, TopLevelClass topLevelClass) {
        Method method = new Method("updateByPrimaryKey");
        method.addParameter(paramObj);
        if (interfaze != null) {
            method.setReturnType(getReturnType(PrimitiveTypeWrapper.getIntegerInstance(), interfaze));
            interfaze.addMethod(method);
        } else if (topLevelClass != null) {
            method.setReturnType(getReturnType(PrimitiveTypeWrapper.getIntegerInstance(), topLevelClass));
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addAnnotation("@Override");
            StringBuilder sb = new StringBuilder();
            sb.append("int num = ");
            sb.append(daoName);
            sb.append(".updateByPrimaryKey(record);");
            sb.append(getFuncReturn(sb, "int", topLevelClass));
            method.addBodyLine(sb.toString());
            topLevelClass.addMethod(method);
        }
    }


    private void setKey(StringBuilder sb) {
        if (table.getPrimaryKeyColumns().size() > 0 && table.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().getShortName().equals("String")) {
            String key = table.getPrimaryKeyColumns().get(0).getJavaProperty();
            key = StringUtils.upperString(key, 0, 1);
            sb.append("if(record.get");
            sb.append(key);
            sb.append("().isEmpty()){");
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, 3);
            sb.append("record.set");
            sb.append(key);
            sb.append("(");
            sb.append("UUID.randomUUID().toString().replace(\"-\",\"\")");
            sb.append(");");
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, 2);

            sb.append("}");

            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, 2);
        }
    }

    /**
     * insert
     */
    private void insert(Interface interfaze, TopLevelClass topLevelClass) {
        Method method = new Method("insert");
        method.addParameter(paramObj);
        if (interfaze != null) {
            method.setReturnType(getReturnType(objFqjt, interfaze));
            interfaze.addMethod(method);
        } else if (topLevelClass != null) {
            method.setReturnType(getReturnType(objFqjt, topLevelClass));
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addAnnotation("@Override");
            StringBuilder sb = new StringBuilder();
            setKey(sb);
            sb.append("int num = ");
            sb.append(daoName);
            sb.append(".insert(record);");
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, 2);
            UUID.randomUUID().toString();
            sb.append(getFuncReturn(sb, "intAndObj", topLevelClass));
            method.addBodyLine(sb.toString());
            topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.UUID"));
            topLevelClass.addMethod(method);
        }
    }

    /**
     * insertSelective
     */
    private void insertSelective(Interface interfaze, TopLevelClass topLevelClass) {
        Method method = new Method("insertSelective");
        method.addParameter(paramObj);
        method.setReturnType(getReturnType(objFqjt, interfaze));
        if (interfaze != null) {
            interfaze.addMethod(method);
        } else if (topLevelClass != null) {
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addAnnotation("@Override");
            StringBuilder sb = new StringBuilder();
            setKey(sb);
            sb.append("int num = ");
            sb.append(daoName);
            sb.append(".insertSelective(record);");
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, 2);
            sb.append(getFuncReturn(sb, "intAndObj", topLevelClass));
            method.addBodyLine(sb.toString());
            topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.UUID"));
            topLevelClass.addMethod(method);
        }
    }

    /**
     * selectByCondition
     */
    private void selectByCondition(Interface interfaze, TopLevelClass topLevelClass) {
        Method method = new Method("selectByCondition");
        method.addParameter(paramObjExample);

        if (interfaze != null) {
            method.setReturnType(getReturnType(listOfObjsFqjt, interfaze));
            interfaze.addMethod(method);
        } else if (topLevelClass != null) {
            method.setReturnType(getReturnType(listOfObjsFqjt, topLevelClass));
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addAnnotation("@Override");
            StringBuilder sb = new StringBuilder();
            sb.append("List<" + objFqjt.getShortName() + "> records = ");
            sb.append(daoName);
            sb.append(".selectByCondition(example);");
            sb.append(getFuncReturn(sb, "list", topLevelClass));
            method.addBodyLine(sb.toString());

            topLevelClass.addMethod(method);
        }
    }

    /**
     * selectByConditionWithRowbounds
     */
    private void selectByConditionWithRowbounds(Interface interfaze, TopLevelClass topLevelClass) {
        Method method = new Method("selectByConditionWithRowbounds");
        method.addParameter(paramObjExample);
        method.addParameter(new Parameter(rowBoundsFqjt, "rowBounds"));
        if (interfaze != null) {
            method.setReturnType(getReturnType(listOfObjsFqjt, interfaze));
            interfaze.addMethod(method);
        } else if (topLevelClass != null) {
            method.setReturnType(getReturnType(listOfObjsFqjt, topLevelClass));
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addAnnotation("@Override");
            StringBuilder sb = new StringBuilder();
            sb.append("List<" + objFqjt.getShortName() + "> records = ");
            sb.append(daoName);
            sb.append(".selectByConditionWithRowbounds(example, rowBounds);");
            sb.append(getFuncReturn(sb, "list", topLevelClass));
            method.addBodyLine(sb.toString());
            topLevelClass.addMethod(method);
        }

    }

    /**
     * selectByPrimaryKey
     */
    private void selectByPrimaryKey(Interface interfaze, TopLevelClass topLevelClass) {
        Method method = new Method("selectByPrimaryKey");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "key"));

        if (interfaze != null) {
            method.setReturnType(getReturnType(objFqjt, interfaze));
            interfaze.addMethod(method);
        } else if (topLevelClass != null) {
            method.setReturnType(getReturnType(objFqjt, topLevelClass));
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addAnnotation("@Override");
            StringBuilder sb = new StringBuilder();
            sb.append(objFqjt.getShortName() + " record = ");
            sb.append(daoName);
            sb.append(".selectByPrimaryKey(key);");
            sb.append(getFuncReturn(sb, "obj", topLevelClass));
            method.addBodyLine(sb.toString());

            topLevelClass.addMethod(method);
        }
    }

    /**
     * deleteByPrimaryKey
     */
    private void deleteByPrimaryKey(Interface interfaze, TopLevelClass topLevelClass) {
        Method method = new Method("deleteByPrimaryKey");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "key"));
        if (interfaze != null) {
            method.setReturnType(getReturnType(PrimitiveTypeWrapper.getIntegerInstance(), interfaze));
            interfaze.addMethod(method);
        } else if (topLevelClass != null) {
            method.setReturnType(getReturnType(PrimitiveTypeWrapper.getIntegerInstance(), topLevelClass));
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addAnnotation("@Override");
            StringBuilder sb = new StringBuilder();
            sb.append("int num = ");
            sb.append(daoName);
            sb.append(".deleteByPrimaryKey(key);");
            sb.append(getFuncReturn(sb, "int", topLevelClass));
            method.addBodyLine(sb.toString());

            topLevelClass.addMethod(method);
        }
    }

    private StringBuilder getFuncReturn(StringBuilder sb, String type, TopLevelClass topLevelClass) {
        if (commonReturnType == null) {
            return sb;
        }

        StringBuilder funcReturn = new StringBuilder();
        OutputUtilities.newLine(funcReturn);
        OutputUtilities.javaIndent(funcReturn, 2);

        if (topLevelClass != null) {
            topLevelClass.addImportedType(FullyQualifiedJavaType.getNewArrayListInstance());
        }
        if (type.equals("obj")) {
            funcReturn.append("return getObjReturn(record);");
        } else if (type.equals("list")) {
            funcReturn.append("return getListReturn(records);");
        } else if (type.equals("int")) {
            funcReturn.append("return getIntegerReturn(num);");
        } else if(type.equals("intAndObj")){
            funcReturn.append("Message<"+objFqjt.getShortName()+"> msg = new Message<"+objFqjt.getShortName()+">();");
            OutputUtilities.newLine(funcReturn);
            OutputUtilities.javaIndent(funcReturn, 2);
            funcReturn.append("Message<Integer> result = getIntegerReturn(num);");
            OutputUtilities.newLine(funcReturn);
            OutputUtilities.javaIndent(funcReturn, 2);
            funcReturn.append("if (result.getSuccess()){");
            OutputUtilities.newLine(funcReturn);
            OutputUtilities.javaIndent(funcReturn, 3);
            funcReturn.append("msg.setData(record);");
            OutputUtilities.newLine(funcReturn);
            OutputUtilities.javaIndent(funcReturn, 3);
            funcReturn.append("msg.setSuccess(true);");
            OutputUtilities.newLine(funcReturn);
            OutputUtilities.javaIndent(funcReturn, 3);
            funcReturn.append("return msg;");
            OutputUtilities.newLine(funcReturn);
            OutputUtilities.javaIndent(funcReturn, 2);
            funcReturn.append("}");
/*            OutputUtilities.newLine(funcReturn);
            OutputUtilities.javaIndent(funcReturn, 2);
            funcReturn.append("List<String> strings = new ArrayList<>();");
            OutputUtilities.newLine(funcReturn);
            OutputUtilities.javaIndent(funcReturn, 2);
            funcReturn.append("strings.add(\"操作数据失败\");");*/
            OutputUtilities.newLine(funcReturn);
            OutputUtilities.javaIndent(funcReturn, 2);
            funcReturn.append("msg.setMsg(\"操作数据失败\");");
            OutputUtilities.newLine(funcReturn);
            OutputUtilities.javaIndent(funcReturn, 2);
            funcReturn.append("msg.setCode(1);");
            OutputUtilities.newLine(funcReturn);
            OutputUtilities.javaIndent(funcReturn, 2);
            funcReturn.append("return msg;");
        }

        return funcReturn;
    }

}
