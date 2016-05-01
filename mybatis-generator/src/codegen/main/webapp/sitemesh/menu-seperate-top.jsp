<%@ page import="org.springframework.util.StringUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set value="<%=request.getContextPath()%>" var="ctp"/>
<c:set value="${ctp}/static" var="resources"/>
<c:set value="${ctp}/static/assets" var="assets"/>

<!DOCTYPE html>
<html>
<head>
    <title>
        <sitemesh:write property='title'/> - 车联网运营管理系统
    </title>
    <sitemesh:write property='head'/>
</head>
<body>

<div id="smTopMenu">
    <%-- 固定导航 --%>
    <%--<div class="navebar ui fixed menu" id="navbar">--%>
    <%-- 正常导航 --%>
    <div class="navebar stackable ui top menu" id="navbar">
        <div href="#" class="header item">
            <%--<img class="logo" src="assets/images/logo.png">--%>
            Project Name
        </div>
        <a href="#" class="item mobile hide" data-tab="sideMenu1">Blog</a>
        <a href="#" class="item mobile only ui dropdown item" tabindex="0">
            Blog
            <i class="dropdown icon"></i>
            <div class="menu transition hidden" tabindex="-1">
                <div class="item">Detail</div>
                <div class="item">Link Item</div>
                <div class="item">
                    <i class="dropdown icon"></i>
                    Sub Menu
                    <div class="menu transition hidden">
                        <div class="item">Link Item</div>
                        <div class="item">Link Item</div>
                    </div>
                </div>
                <div class="item">Link Item</div>
                <div class="divider"></div>
                <div class="item">Logout</div>
            </div>
        </a>
        <a href="#" class="item" data-tab="sideMenu2">Articles</a>
        <a href="#" class="ui right floated dropdown item" tabindex="0">
            User Info
            <i class="dropdown icon"></i>
            <div class="menu transition hidden" tabindex="-1">
                <div class="item">Detail</div>
                <div class="item">Link Item</div>
                <div class="item">
                    <i class="dropdown icon"></i>
                    Sub Menu
                    <div class="menu transition hidden">
                        <div class="item">Link Item</div>
                        <div class="item">Link Item</div>
                    </div>
                </div>
                <div class="item">Link Item</div>
                <div class="divider"></div>
                <div class="item">Logout</div>
            </div>
        </a>
    </div>
</div>
<div id="smBody">
    <sitemesh:write property='div.smBody'/>
</div>
<div id="smScriptLibrary">
    <sitemesh:write property="div.smScriptLibrary"/>
</div>
<div id="smScripts">
    <sitemesh:write property='div.smScripts'/>
</div>
</body>
</html>