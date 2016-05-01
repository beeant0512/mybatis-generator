package com.beeant.common.sitemesh;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.content.tagrules.html.DivExtractingTagRuleBundle;
import org.sitemesh.webapp.WebAppContext;

/**
 * Created by Beeant on 2016/3/3.
 */
public class ParamConfigurableSiteMeshFilter extends ConfigurableSiteMeshFilter {

    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        //获取原有默认配置装饰选择器
        DecoratorSelector<WebAppContext> defaultDecoratorSelector = builder.getDecoratorSelector();

        builder.addTagRuleBundle(new DivExtractingTagRuleBundle());
        //赋给自定义装饰选择器，则自定义规则未匹配时调用默认选择器获取
        builder.setCustomDecoratorSelector(new ParamDecoratorSelector(defaultDecoratorSelector));
    }
}
