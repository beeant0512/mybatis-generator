package beeant;

/**
 * Created by Beeant on 2016/1/17 0017.
 */
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

/**
 * 将Mapper类改成DAO类
 *
 */
public class AddDtoPlugin extends PluginAdapter {

    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        introspectedTable.setBaseRecordType(introspectedTable.getBaseRecordType().concat("Dto"));
    }
}