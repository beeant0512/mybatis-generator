<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="beeant" uri="/Beeant" %>
<%--
  User: Beeant
  Date: 2016/3/4
  Time: 18:38
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctp" value="<%=request.getContextPath()%>"/>
<c:set var="sComponents" value="${ctp}/static/assets/semantic/components"/>
<html>
<head>
    <title>登录</title>
    <style>
        body > .grid {
            height: 100%;
        }

        .column {
            max-width: 450px;
        }

        .image {
            margin-top: -100px;
        }
    </style>

</head>
<body>
<div id="smBody">
    <div class="ui middle aligned center aligned grid">
        <div class="column">
            <h2 class="ui teal image header">
            </h2>
            <form id="loginForm" class="ui large form" method="post" action="${ctp}/app/login?action=login">
                <div class="ui stacked segment">
                    <div class="field">
                        <div class="ui left icon input">
                            <i class="user icon"></i>
                            <input type="text" name="username" placeholder="请输入用户名" value="${user.username}">
                        </div>
                    </div>
                    <div class="field">
                        <div class="ui left icon input">
                            <i class="lock icon"></i>
                            <input type="password" name="password" placeholder="请输入密码" value="${user.password}">
                        </div>
                    </div>
                    <div class="ui fluid large teal submit button">登录</div>
                </div>
                <%--<div class="ui error message"></div>--%>
            </form>
        </div>
    </div>
</div>

<div id="smScripts">
    <script>
        $(document).ready(function () {
            $('.ui.form').form({
                inline: true,
                fields: {
                    username: {
                        identifier: 'username',
                        rules: [
                            {type: 'empty', prompt: '请输入用户名'}
                        ]
                    },
                    password: {
                        identifier: 'password',
                        rules: [
                            {type: 'empty', prompt: '请输入密码'},
                            {type: 'length[6]', prompt: '密码长度不能少于6位'}
                        ]
                    }
                }
            });
        });
    </script>
</div>
</body>
</html>
