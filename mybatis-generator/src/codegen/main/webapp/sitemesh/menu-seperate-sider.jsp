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
    <sitemesh:write property='div.smTopMenu'/>
</div>
<div id="smBody">
    <div class="ui visible left vertical labeled<%-- icon--%> sidebar menu">
        <div id="sideMenu1" class="ui tab active" data-tab="sideMenu1">

            <div class="item level">
                <a class="item top"><i class="dropdown layout icon"></i>Home</a>
                <div class="menu">
                    <a class="active item"><i class="block layout icon"></i>Search</a>
                    <a class="item"><i class="block layout icon"></i>Add</a>
                    <a class="item"><i class="block layout icon"></i>Remove</a>
                </div>
            </div>
            <a class="item">
                <i class="block layout icon"></i>
                Topics1
            </a>
            <a class="item">
                <i class="smile icon"></i>
                Friends1
            </a>
        </div>
        <div id="sideMenu2" class="ui tab" data-tab="sideMenu2">
            <div class="item level">
                <a class="item top"><i class="dropdown layout icon"></i>Home</a>
                <div class="menu">
                    <a class="active item"><i class="block layout icon"></i>Search</a>
                    <a class="item"><i class="block layout icon"></i>Add</a>
                    <a class="item"><i class="block layout icon"></i>Remove</a>
                </div>
            </div>
            <a class="item">
                <i class="block layout icon"></i>
                Topics2
            </a>
            <a class="item">
                <i class="smile icon"></i>
                Friends2
            </a>
        </div>
    </div>
    <div class="pusher">
        <div class="ui basic segment" id="main-container">
            <h3 class="ui header">Hello there</h3>
            <sitemesh:write property='div.smBody'/>
        </div>
    </div>
</div>
<div id="smScriptLibrary">
    <sitemesh:write property="div.smScriptLibrary"/>
</div>
<div id="smScripts">
    <sitemesh:write property='div.smScripts'/>
</div>
</body>
</html>