package com.beeant.common.sitemesh;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.content.Content;
import org.sitemesh.webapp.WebAppContext;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Beeant on 2016/3/3.
 */
public class ParamDecoratorSelector implements DecoratorSelector<WebAppContext> {

    private DecoratorSelector<WebAppContext> defaultDecoratorSelector;

    public ParamDecoratorSelector(DecoratorSelector<WebAppContext> defaultDecoratorSelector) {
        this.defaultDecoratorSelector = defaultDecoratorSelector;
    }

    @Override
    public String[] selectDecoratorPaths(Content content, WebAppContext webAppContext) throws IOException {
        // build decorator based on the request
        HttpServletRequest request = webAppContext.getRequest();

        String servletPath = request.getServletPath();
        String onlyBody = request.getParameter("onlybody");
        if (!StringUtils.isEmpty(onlyBody) && StringUtils.pathEquals(onlyBody, "yes")) {
            return new String[]{"/sitemesh/onlybody.jsp"};
        } else if (servletPath.equals("/app/login")) {
            return new String[]{"/sitemesh/decorator.jsp"};
        }
        //return new String[]{"/sitemesh/menu-top.jsp", "/sitemesh/decorator.jsp"};

        return new String[]{"/sitemesh/menu-seperate-top.jsp", "/sitemesh/menu-seperate-sider.jsp", "/sitemesh/decorator.jsp"};

    }
}
