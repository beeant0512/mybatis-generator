$('.ui.menu .ui.dropdown').dropdown({
    on: 'hover'
});

$('#navbar .ui.menu a.item').on('click', function () {
    $(this).addClass('active').siblings().removeClass('active');
    /*$('.sidebar.menu .item').siblings().removeClass('active');*/
});

$('.sidebar.menu .item').not('.level').on('click', function () {
    var _this = $(this);
    $('.sidebar.menu .item').removeClass('active');
    _this.addClass('active');
    var child = _this.parent().children('.menu');
    if (child.length != 0) {
        child.toggle();
    } else {
        var a = _this.find("a")[0];
        ajax(a);
        return false;
    }
});

/* tab top menu */
$('#navbar .item').tab();

/**
 * 统一ajax请求
 * @param url
 * @param data
 * @param onSuccess
 */
function ajax(link, data, onSuccess) {
    var newLocation = link;
    $.ajax({
        type: 'post',
        url: link,
        data: data,
        success: function (data, textStatus, jqXhr) {
            var isLogin = jqXhr.getResponseHeader('login');
            if (isLogin == 'true') {
                window.location = getProjectName() + '/app/login';
                return false;
            } else {
                if(onSuccess){
                    onSuccess(data);
                }
            }
        }
    });
    console.log(newLocation);
    try{
        window.history.pushState("",document.title, newLocation);
    } catch (e){
        console.log("浏览器不支持window.history.pushState方法" + e);
    }

}

/**
 * 设置Option
 *
 * @param selector
 * @param data
 * @param initValue
 */
function setOption(selector, data, initValue) {
    $(selector).append("<option value=''>&nbsp;请选择</option>");
    $.each(data, function (key, item) {
        if (item.value == initValue) {
            $(selector).append('<option value="' + item.value + '" selected="selected">&nbsp;' + item.name + '</option>');
        } else {
            $(selector).append('<option value="' + item.value + '">&nbsp;' + item.name + '</option>');
        }
    });
    //$(selector).dropdown();
}

/**
 * 获取当前位置
 */
function getMyLocation() {
    var location = JSON.parse($.cookie('location'));
    if (location == null || typeof(location) == undefined) {
        location = {lng: 116.404, lat: 39.915, city: '北京', status: 'failed'};
    }
    var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function (r) {
        if (this.getStatus() == BMAP_STATUS_SUCCESS) {
            console.log(r);
            location = {lng: r.point.lng, lat: r.point.lat, city: r.address.city};
            console.log(location);
            $.cookie('location', JSON.stringify(location)); // 存储 cookie
        }
    }, {enableHighAccuracy: true});
    $.cookie('location', JSON.stringify(location)); // 存储 cookie
    return location;
}

/**
 * 设置元素 全屏
 *
 * @param selector
 */
function setFullScreen(selector) {
    if ($(selector).offset()) {
        $(selector).css('height', document.body.clientHeight - $(selector).offset().top);
    }
}

/**
 * 获取项目路径
 *
 * @returns {string}
 */
function getProjectName() {
    //获取当前网址，如： http://localhost:8080/Tmall/index.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录如：/Tmall/index.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8080
    var localhostPaht = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/Tmall
    return pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
}

//开启ajax请求监控
jQuery.ajaxSetup({
    global: true
});

$(document).ajaxStart(function () {
    //console.log("Triggered ajaxStart handler.");
});

$(document).ajaxSend(function (event, jqxhr, settings) {
    //console.log("Triggered ajaxSend handler.");
    //console.log(jqxhr);
    //console.log(settings);
});

$(document).ajaxStop(function () {
    // console.log("Triggered ajaxStop handler.");
});

$(document).ajaxSuccess(function (event, xhr, settings) {
    //console.log("Triggered ajaxSuccess handler. The Ajax response was: " + xhr.responseText);
    //console.log(xhr);
    //console.log(settings);
});

$(document).ajaxComplete(function (event, xhr, settings) {
    // console.log(xhr);
    //console.log(xhr);
    //console.log(settings);
});

$(document).ajaxError(function (event, jqxhr, settings, thrownError) {
    // console.log("Triggered ajaxError handler.");
    //console.log(jqxhr);
    //console.log(settings);
    //console.log(thrownError)
});



