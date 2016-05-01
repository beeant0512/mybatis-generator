<%@ page import="org.springframework.util.StringUtils" %>
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
    <link rel="stylesheet" type="text/css" href="${assets}/semantic/semantic.css">
    <link rel="stylesheet" type="text/css" href="${resources}/css/style.css">

    <script type="text/javascript" src="${assets}/jquery/1.12.1/jquery.min.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <link rel="stylesheet" type="text/css" href="${assets}/semantic-ie8/semantic.ie8.css">
    <script src="${assets}/DOMAssistant/DOMAssistantCompressed-2.8.1.js"></script>
    <script src="${assets}/respond/1.4.2/respond.min.js"></script>
    <script src="${assets}/html5shiv/html5shiv.js"></script>
    <script src="${assets}/es5-shim/es5-shim.min.js"></script>
    <script src="${assets}/semantic-ie8/semantic.ie8.patch.js"></script>
    <![endif]-->
    <sitemesh:write property='head'/>
</head>
<body>

<sitemesh:write property='div.smTopMenu'/>
<sitemesh:write property='div.smBody'/>
<script type="text/javascript" src="${assets}/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="${assets}/jquery/jquery.hoverIntent.minified.js"></script>
<script type="text/javascript" src="${assets}/jquery/jquery.dcjqaccordion.2.9.js"></script>

<!-- semantic -->
<script type="text/javascript" src="${assets}/semantic/semantic.js"></script>
<script type="text/javascript" src="${resources}/js/semantic-form-rules.js"></script>

<%
    if (StringUtils.isEmpty(request.getParameter("onlybody")) || !StringUtils.pathEquals(request.getParameter("onlybody"), "yes")) {
%>
<script type="text/javascript" src="${resources}/js/app.js"></script>
<%
    }
%>
<sitemesh:write property="div.smScriptLibrary"/>
<sitemesh:write property='div.smScripts'/>
</body>

</html>

