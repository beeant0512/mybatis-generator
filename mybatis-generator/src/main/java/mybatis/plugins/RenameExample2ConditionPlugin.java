package mybatis.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

/**
 * Created by Beeant on 2016/1/17 0017.
 */
public class RenameExample2ConditionPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    private String example2Condition(String stringWithExample) {
        return stringWithExample.replace("Example", "Condition");
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        introspectedTable.setCountByExampleStatementId(example2Condition(introspectedTable.getCountByExampleStatementId()));
        introspectedTable.setDeleteByExampleStatementId(example2Condition(introspectedTable.getDeleteByExampleStatementId()));

        introspectedTable.setExampleWhereClauseId(example2Condition(introspectedTable.getExampleWhereClauseId()));
        introspectedTable.setMyBatis3UpdateByExampleWhereClauseId(example2Condition(introspectedTable.getMyBatis3UpdateByExampleWhereClauseId()));

        introspectedTable.setSelectByExampleStatementId(example2Condition(introspectedTable.getSelectByExampleStatementId()));
        introspectedTable.setSelectByExampleWithBLOBsStatementId(example2Condition(introspectedTable.getSelectByExampleWithBLOBsStatementId()));

        introspectedTable.setUpdateByExampleStatementId(example2Condition(introspectedTable.getUpdateByExampleStatementId()));
        introspectedTable.setUpdateByExampleSelectiveStatementId(example2Condition(introspectedTable.getUpdateByExampleSelectiveStatementId()));
        introspectedTable.setUpdateByExampleWithBLOBsStatementId(example2Condition(introspectedTable.getUpdateByExampleWithBLOBsStatementId()));
        super.initialized(introspectedTable);
    }
}
