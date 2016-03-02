<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  User: Beeant
  Date: 2016/1/17
  Time: 9:54
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set value="<%=request.getContextPath()%>" var="ctp"/>
<jsp:include page="../common/header.jsp"/>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">Logo</h3>
    </div>
    <div class="panel-body">
        <form modelAttribute="sysUserModel" method="post" role="form" id="form_login">
            <div class="form-group">
                <label for="userName">用户名</label>
                <input id="userName" name="userName" class="form-control"
                       data-bv-trigger="blur"
                       data-bv-notempty="true" data-bv-notempty-message="用户名不能为空"
                       data-bv-stringlength="true" data-bv-stringlength-max="50" data-bv-stringlength-message="用户名不能超过50个字符"
                />
            </div>

            <div class="form-group">
                <label for="pwd">密码</label>
                <input id="pwd" name="password" class="form-control" type="password"
                       data-bv-notempty="true" data-bv-notempty-message="密码不能为空"
                />
            </div>

            <button type="submit" class="btn btn-primary">登陆</button>
        </form>
    </div>
</div>


<jsp:include page="../common/footer.jsp"/>
<jsp:include page="../common/assets/bootstrapValidator.jsp"/>
<script>
    jQuery(document).ready(function ($) {
        $('#form_login').bootstrapValidator();
    })
</script>

