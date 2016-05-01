/**
 * Created by Beeant on 2016/4/15.
 */
$('input[class="date"]').daterangepicker({
    "singleDatePicker": true,
    "autoApply": true,
    "format": "YYYY-MM-DD",
    "locale": {
        "separator": " - ",
        "applyLabel": "确定",
        "cancelLabel": "取消",
        "fromLabel": "从",
        "toLabel": "至",
        "customRangeLabel": "自定义",
        "daysOfWeek": [
            "日",
            "一",
            "二",
            "三",
            "四",
            "五",
            "六"
        ],
        "monthNames": [
            "一月",
            "二月",
            "三月",
            "四月",
            "五月",
            "六月",
            "七月",
            "八月",
            "九月",
            "十月",
            "十一月",
            "十二月"
        ],
        "firstDay": 1
    },
    "linkedCalendars": false,
    "opens": "center"
});

$('input[class="date range"]').daterangepicker({
    singleDatePicker: true,
    showDropdowns: true,
    autoApply: true,
    format: 'YYYY-MM-DD',
    locale: {
        applyLabel: '确定',
        cancelLabel: '取消',
        fromLabel: '从',
        toLabel: '至',
    }
});