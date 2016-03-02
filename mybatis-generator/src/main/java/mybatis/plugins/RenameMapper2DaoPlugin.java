package mybatis.plugins;

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
public class RenameMapper2DaoPlugin extends PluginAdapter {

    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        introspectedTable.setMyBatis3JavaMapperType(introspectedTable.getMyBatis3JavaMapperType().replaceAll("Mapper$", "Dao"));
    }
}