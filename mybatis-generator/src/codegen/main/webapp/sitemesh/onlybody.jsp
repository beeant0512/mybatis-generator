<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set value="<%=request.getContextPath()%>" var="ctp"/>
<c:set value="${ctp}/static" var="resources"/>
<c:set value="${ctp}/static/assets" var="assets"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <title><sitemesh:write property='title'/></title>

    <sitemesh:write property='head'/>
</head>
<body>
<sitemesh:write property='div.smBody'/>

<sitemesh:write property="div.smScriptLibrary"/>

<script type="text/javascript" src="${resources}/js/app.js"></script>
<sitemesh:write property='div.smScripts'/>
</body>
</html>
