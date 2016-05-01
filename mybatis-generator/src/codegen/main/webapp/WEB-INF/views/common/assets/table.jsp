<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  User: Beeant
  Date: 2016/4/7
  Time: 9:23
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctp" value="<%=request.getContextPath()%>" />
<c:set var="assets" value="${ctp}/static/assets"/>
<c:set var="table" value="${assets}/datatables" />

<script type="text/javascript" src="${table}/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="${table}/js/dataTables.semanticui.js"></script>
<script type="text/javascript" src="${ctp}/static/js/datatables.default.js"></script>
<link rel="stylesheet" type="text/css" href="${table}/css/dataTables.semanticui.css" />