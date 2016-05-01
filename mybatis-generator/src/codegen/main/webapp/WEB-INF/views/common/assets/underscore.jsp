<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 2015/11/10 0010
  Time: 13:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="<%=request.getContextPath()%>/static/assets/underscore/underscore.js"></script>

<script>
    $(function(){
        _.templateSettings = {
            evaluate: /<#([\s\S]+?)#>/g,
            interpolate: /<#=([\s\S]+?)#>/g,
            escape: /<#-([\s\S]+?)#>/g
        };
    })
</script>
