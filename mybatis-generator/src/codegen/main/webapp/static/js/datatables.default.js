$.fn.dataTable.ext.errMode = "none";

$.extend(true, $.fn.dataTable.defaults, {
    "lengthMenu": [[100, 200, 500, 1000], [100, 200, 500, 1000]],
    "pageLength": 100,
    "language": {
        "sProcessing": "处理中...",
        "sLengthMenu": "每页 _MENU_ 项",
        "sZeroRecords": "没有匹配结果",
        "sInfo": " 当前显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
        "sInfoEmpty": " 当前显示第 0 至 0 项结果，共 0 项",
        "sInfoFiltered": "",
        "sInfoPostFix": "",
        "sSearch": "搜索:",
        "sUrl": "",
        "sEmptyTable": "表中数据为空",
        "sLoadingRecords": "载入中...",
        "sInfoThousands": ",",
        "oPaginate": {
            "sFirst": "首页",
            "sPrevious": "上页",
            "sNext": "下页",
            "sLast": "末页"
        },
        "oAria": {
            "sSortAscending": ": 以升序排列此列",
            "sSortDescending": ": 以降序排列此列"
        }
    },

    "scrollX": true,
    "sScrollXInner": "110%", //表格的内容宽度
    "processing": true,
    "serverSide": true,
    "bDeferRender": true,
    "searching": false,
    /*fixedHeader: {
     header: true,
     footer: true
     }*/
});

if (document.body.clientHeight > 500) {
    $.extend(true, $.fn.dataTable.defaults, {"scrollY": (document.body.clientHeight - 350)});
} else {
    $.extend(true, $.fn.dataTable.defaults, {"scrollY": 500});
}

$(window).resize(function () {
    var width = $('.dataTables_scroll').width();
    if (width < 800) {
        $.extend(true, $.fn.dataTable.defaults, {"scrollXInner": "800px"});
    }
});

$.extend(true, $.fn.dataTable.defaults, {
    dom: "<'ui stackable  grid'" +
    "<'row'" +
    "<'sixteen wide column'tr>" +
    ">" +
    "<'two column row'" +
    "<'column'li>" +
    "<'right aligned column'p>" +
    ">" +
    ">",
    renderer: 'semanticUI'
});

$.fn.dataTable.Api.register('rows().treeTable()', function (index, option) {
    var defaultOptions = {
        id: "id",
        pid: "parentId",
        depthName: "grade",
        levelName: 'grade',
        path: "fullPath",
        iconDefault: "fa fa-caret-down",
        iconToggle: "fa fa-caret-right",
        initLevel: 1
    };
    var table = this;
    var trs = table.nodes();
    var tableData = table.data();

    var options = $.extend({}, defaultOptions, option);

    var _data = null;
    var _td = null;
    var tmpDepth = options.initLevel;
    $.each(trs, function (key, value) {
        _data = table.row(this).data();
        _td = $(this).children('td:eq(' + index + ')');

        _td.html('<div><div class="table-tree-item"><span class="table-tree-item-name "> ' + _td.html() + '</span></div></div>');

        if (_data[options.levelName] != options.initLevel) {
            _td.css('padding-left', ((_data[options.levelName] - 1) * 30) + 'px');
        }

        if (_data[options.levelName] > tmpDepth) {
            $(this).prev('tr').children('td:eq(' + index + ')').find('.table-tree-item-name').addClass(options.iconDefault);
        }

        tmpDepth = _data[options.levelName];
    });

    for (var i = 0; i < tableData.length; i++) {

        /*var $td = $($(_nodes[i]).children("td:eq(" + index + ")"));
         var depth = _data[i][option.depth];
         $td.attr('data-depth', depth);
         if(!$td.children().hasClass("indent")){
         $td.addClass('table-tree-item');
         var tree = '<div class="table-tree-item"><span><i class="indent fa"></i></span><span>%s</span></div>';
         $td.html(tree.replace("%s",$td.html()));
         if (depth != 1) {
         $td.css('padding-left', ((depth - 1) * 30) + 'px');
         }
         if (depth > tmpDepth) {
         $($(this.nodes()[i - 1]).children('td:eq(' + index + ')')).children('i').addClass(option.iconDefault);
         }
         tmpDepth = depth;
         }*/
    }

    $(".table-tree-item-name").unbind("click").click(function () {
        var table = $(this).closest("table").DataTable();
        var tableData = table.data();
        var tableTrNodes = table.rows().nodes();

        var $tr = $(this).closest('tr');
        var $this = $(this);
        var trData = table.row($tr).data();
        for (var i = 0; i < tableData.length; i++) {
            var data = tableData[i];
            if (data[options.path].indexOf(trData[options.path]) != -1
                && data[options.path] != trData[options.path]) {
                if ($this.hasClass(options.iconDefault)) {
                    $(tableTrNodes[i]).addClass('hidden');
                } else {
                    $(tableTrNodes[i]).removeClass('hidden');
                }
            }
        }
        $this.toggleClass(options.iconToggle).toggleClass(options.iconDefault);
    });

    //checkbox选择事件
    $('.table-tree-item-name').parents('tr').find('input[type="checkbox"]').unbind('click').on('click', function () {
        var table = $(this).closest("table").DataTable();
        var tableData = table.data();
        var tableTrNodes = table.rows().nodes();

        var $tr = $(this).closest('tr');
        var $this = $(this);
        var $thisCheckStatus = $this.prop('checked');
        var trData = table.row($tr).data();
        for (var i = 0; i < tableData.length; i++) {
            var data = tableData[i];
            if (data[options.path].indexOf(trData[options.path]) != -1
                && data[options.path] != trData[options.path]) {
                $checkbox = $(tableTrNodes[i]).find('input[type="checkbox"]');
                $checkbox.prop('checked', $thisCheckStatus);
                $checkbox.trigger('change');
            }
        }
        $this.toggleClass(option.iconToggle).toggleClass(option.iconDefault);
    });
    return this;
});

$(".table")
    .on("draw.dt", function (event, data) {
        var table = $(event.target);
        var scrollTable = table.parents('.dataTables_scroll');
        var thead = scrollTable.find('.dataTables_scrollHead');
        var tbody = scrollTable.find('.dataTables_scrollBody');
        $(tbody).find('.ui.checkbox').checkbox();
        thead.find('.ui.checkbox').checkbox().checkbox({
            onChecked: function () {
                $(tbody).find('.ui.checkbox').checkbox('set checked');
            },
            onUnchecked: function () {
                $(tbody).find('.ui.checkbox').checkbox('set unchecked');
            }
        });
    })
    /**
     * preXhr.dt
     * ajax请求发起前事件
     */
    .on("preXhr.dt", function (e, settings, data) {
        var table = $(e.target);
        var scrollTable = table.parents('.dataTables_scroll');
        var thead = scrollTable.find('.dataTables_scrollHead');
        thead.find('.ui.checkbox').checkbox('set unchecked');
        data.orderColumns = [];
        data.orderDirs = [];
        var ordersLength = data.order.length;
        if (ordersLength == 1) {
            data.orderColumn = data.columns[data.order[0].column].data;
            data.orderDir = data.order[0].dir;
        } else if (ordersLength > 1) {
            $.each(data.order, function (key, value) {
                data.orderColumns.push(data.columns[value.column].data);
                data.orderDirs.push(value.dir);
            });
        }
    });