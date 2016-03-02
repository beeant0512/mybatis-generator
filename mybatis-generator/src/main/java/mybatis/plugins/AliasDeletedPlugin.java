package mybatis.plugins;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Beeant on 2016/2/13.
 */
public class AliasDeletedPlugin extends PluginAdapter {
    private String alias;
    private String deletedValue;

    private Map<FullyQualifiedTable, List<XmlElement>> elementsToAdd;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public AliasDeletedPlugin() {
        elementsToAdd = new HashMap<FullyQualifiedTable, List<XmlElement>>();
    }



    @Override
    public boolean validate(List<String> warnings) {
        alias = properties.getProperty("column");
        deletedValue = properties.getProperty("value");
        return true;
    }

    /**
     * We'll override this method and add any new elements generated by
     * previous calls
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document,
                                           IntrospectedTable introspectedTable) {
        List<XmlElement> elements = elementsToAdd.get(introspectedTable.getFullyQualifiedTable());
        if (elements != null) {
            for (XmlElement element : elements) {
                document.getRootElement().addElement(element);
            }
        }

        return true;
    }

    @Override
    public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            generateDeleteSql(element, introspectedTable);
        }

        return true;
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            generateDeleteSql(element, introspectedTable);
        }
        return true;
    }

    private void generateDeleteSql(XmlElement element, IntrospectedTable introspectedTable) {
        ObjectMapper mapper = new ObjectMapper();
        element.setName("update");
        Element ifElement = element.getElements().get(6);
        // remove old id attribute and add a new one with the new name
        for (Iterator<Element> iterator = element.getElements().iterator(); iterator.hasNext(); ) {
            iterator.next();
            iterator.remove();
        }

        element.addElement(new TextElement("update ".concat(introspectedTable.getFullyQualifiedTable().toString())));
        element.addElement(new TextElement("set ".concat(alias).concat(" = \"").concat(deletedValue).concat("\"")));
        element.addElement(ifElement);


    }
}