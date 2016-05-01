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
    <div class="ui menu" id="topMenu">
        <div class="header item"><img src="${ctp}/static/img/logo.png" width="97px" class="ui "/>车联网运营管理系统</div>

        <div class="ui dropdown item" tabindex="0">
            运营管理<i class="dropdown icon"></i>
            <div class="menu transition hidden" tabindex="-1">
                <div class="item"><a href="${ctp}/userCar/browse">车辆信息管理</a></div>
                <div class="item"><a href="${ctp}/device/browse">设备信息管理</a></div>
                <div class="item">
                    <i class="dropdown icon"></i>
                    <span class="text">客户信息管理</span>
                    <div class="right menu">
                        <div class="item"><a href="${ctp}/coreUser/browse">客户基本信息管理</a></div>
                        <div class="item"><a href="${ctp}/coreUserGroup/browse">客户分组管理</a></div>
                    </div>
                </div>
                <div class="item"><a href="${ctp}/simInfo/browse">SIM卡信息管理</a></div>
                <div class="item">
                    <i class="dropdown icon"></i>
                    <span class="text">车辆监控</span>
                    <div class="right menu">
                        <div class="item"><a href="${ctp}/monitor/monitor">实时监控</a></div>
                        <div class="item"><a href="${ctp}/monitor/drivingHistory">历史轨迹</a></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="ui dropdown item" tabindex="1">
            数据统计<i class="dropdown icon"></i>
            <div class="menu transition hidden" tabindex="-1">
                <div class="item"><a href="${ctp}/chart/vehicleInformation">车辆信息统计</a></div>
                <div class="item"><a href="${ctp}/chart/vehicleFault">车辆故障统计</a></div>
                <div class="item"><a href="${ctp}/chart/vehicleFuelMileage">油耗和里程统计</a></div>
                <div class="item"><a href="${ctp}/chart/simBusiness">SIM卡业务信息统计</a></div>
            </div>
        </div>
        <div class="ui dropdown item" tabindex="1">
            系统管理<i class="dropdown icon"></i>
            <div class="menu transition hidden" tabindex="-1">
                <div class="item"><a href="${ctp}/role/browse">角色管理</a></div>
                <div class="item"><a href="${ctp}/menu/browse">菜单管理</a></div>
                <div class="item">
                    系统用户
                    <div class="menu">
                        <a class="item" href="${ctp}/monitorUser/browse">运营管理平台用户</a>
                        <a class="item" href="${ctp}/productUser/browse">厂端系统用户</a>
                        <a class="item" href="${ctp}/providerUser/browse">4S店系统用户</a>
                    </div>
                </div>
                <div class="item">
                    <i class="dropdown icon"></i>
                    <span class="text">系统日志</span>
                    <div class="right menu">
                        <div class="item"><a href="${ctp}/logs/actorAction">系统使用日志</a></div>
                        <div class="item"><a href="${ctp}/logs/remoteControl">远程操作日志</a></div>
                        <div class="item"><a href="${ctp}/logs/smsSend">短信操作日志</a></div>
                    </div>
                </div>

                <div class="item"><a href="${ctp}/conf/browse">配置信息</a></div>
            </div>
        </div>
        <div class="ui dropdown item" tabindex="1">
            基础数据<i class="dropdown icon"></i>
            <div class="menu transition hidden" tabindex="-1">
                <div class="item"><a href="${ctp}/carSeries/browse">车型车系管理</a></div>
                <div class="item"><a href="${ctp}/deviceModule/browse">设备型号管理</a></div>
                <div class="item"><a href="${ctp}/diagnose/browse">诊断管理</a></div>
                <div class="item"><a href="${ctp}/simProvider/browse">供卡商管理</a></div>
                <div class="item"><a href="${ctp}/deviceProvider/browse">设备厂家管理</a></div>
                <div class="item"><a href="${ctp}/insurance/browse">保险公司管理</a></div>
                <div class="item"><a href="${ctp}/dealer/browse">经销商/服务站管理</a></div>
                <div class="item">
                    <i class="dropdown icon"></i>
                    <span class="text">地域信息管理</span>
                    <div class="right menu">
                        <div class="item"><a href="${ctp}/address/browse?type=country">国家</a></div>
                        <div class="item"><a href="${ctp}/address/browse?type=province">省</a></div>
                        <div class="item"><a href="${ctp}/address/browse?type=city">市</a></div>
                        <div class="item"><a href="${ctp}/address/browse?type=area">区</a></div>
                    </div>
                </div>
                <div class="item"><a href="${ctp}/oil/browse">油价信息查看</a></div>
            </div>
        </div>

        <div class="right menu">
            <div class="ui dropdown item" tabindex="0">
                <i class="dropdown icon"></i>
                <div class="menu transition hidden" tabindex="-1">
                    <div class="item">详情</div>
                    <div class="item"><a href="${ctp}/app/logout">注销</a></div>
                </div>
            </div>
        </div>
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