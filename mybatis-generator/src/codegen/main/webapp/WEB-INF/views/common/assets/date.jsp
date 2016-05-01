<%--
  Created by Beeant.
  User: hp
  Date: 2015/12/4 0004
  Time: 15:16
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:set var="ctp" value="<%=request.getContextPath()%>" />
<c:set var="assets" value="${ctp}/static/assets"/>
<c:set var="datepicker" value="${assets}/semantic-ui-daterangepicker" />

<script type="text/javascript" src="${datepicker}/moment-with-locales.min.js"></script>
<!-- Include Date Range Picker -->
<script type="text/javascript" src="${datepicker}/daterangepicker.js"></script>
<script type="text/javascript" src="${ctp}/static/js/semantic-ui-daterangepicker.default.js"></script>

<link rel="stylesheet" type="text/css" href="${datepicker}/daterangepicker.css" />
