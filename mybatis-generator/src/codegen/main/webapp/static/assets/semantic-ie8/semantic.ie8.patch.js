$(document).ready(function (e) {
    resizeUiMiddle();
    resizeUiCenter();
    $('.ui.menu  .ui.tab').each(function () {
        if (!$(this).hasClass('active')) {
            $(this).css('display', 'none');
        }
    })
    $('#navbar a.item').not('.dropdown').unbind('click').on('click', function () {
        var tab = $(this).data('tab');
        $('.sidebar.menu .ui.tab').siblings().css('display', 'none');
        $('#' + tab).removeAttr("style");
    })
});

window.onresize = function () {
    resizeUiMiddle();
    resizeUiCenter();
};

function resizeUiMiddle() {
    $element = $('.ui.middle > .column');
    if (typeof(eval($element.height)) == "function") {
        $element.css('top', ((document.documentElement.clientHeight - $element.height()) / 2));
    }
}

function resizeUiCenter() {
    $element = $('.ui.center > .column');
    if (typeof(eval($element.width)) == "function") {
        $element.css('left', ((document.documentElement.clientWidth - $element.width()) / 2));
    }
}