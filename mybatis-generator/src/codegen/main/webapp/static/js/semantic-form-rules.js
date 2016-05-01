var prompt = {
    unique: '{name} 已被使用',
    id: '{name} 证件号码格式错误',
    vin: '{name} 格式错误',
    date: '{name} 日期格式错误',
    empty: '{name} 不能为空',
    checked: '{name} 必选',
    email: '{name} 格式错误',
    url: '{name} 格式错误',
    regExp: '{name} 格式错误',
    integer: '{name} 必须是整数',
    decimal: '{name} 必须是小数',
    number: '{name} 必须是数字',
    is: '{name} 必须是 "{ruleValue}"',
    isExactly: '{name} 必须是 "{ruleValue}"',
    not: '{name} 不能为 "{ruleValue}"',
    notExactly: '{name} 不能为 "{ruleValue}"',
    contain: '{name} 不能包含 "{ruleValue}"',
    containExactly: '{name} 不能包含 "{ruleValue}"',
    doesntContain: '{name} 必须包含  "{ruleValue}"',
    doesntContainExactly: '{name} 必须包含 "{ruleValue}"',
    minLength: '{name} 长度必须大于 {ruleValue}',
    length: '{name} 长度不能小于 {ruleValue}',
    exactLength: '{name} 长度必须为 {ruleValue}',
    maxLength: '{name} 长度不能大于 {ruleValue}',
    match: '{name} 必须匹配 {ruleValue} ',
    different: '{name} 不能和 {ruleValue} 相同',
    creditCard: '{name} 格式错误',
    minCount: '{name} 选择不能少于 {ruleValue} 项',
    exactCount: '{name} 必须选择 {ruleValue} 必须选择',
    maxCount: '{name} 选择不能多于 {ruleValue} 项',
    phone: '{name} 格式错误',
    username: '{name} 必须为字母、数字的组合',
    charAndNumber: '{name} 必须为字母、数字的组合',
};

$.fn.form.settings.prompt = $.extend(
    $.fn.form.settings.prompt, prompt
);


$.fn.form.settings.rules.username = function(value){
    return /\w*\d*/g.test(value);
};

$.fn.form.settings.rules.charAndNumber = function(value){
    return /\w*\d*/g.test(value);
};

$.fn.form.settings.rules.chinese = function (value) {
    return /[\u4e00-\u9fa5]/g.test(value);
};

$.fn.form.settings.rules.phone = function (value, country) {
    if(value === ''){
        return true;
    }
    if (country == false || country == 'CN') {
        return (/^((00|\+)?(86(?:-| )))?((\d{11})|(\d{3}[- ]{1}\d{4}[- ]{1}\d{4})|((\d{2,4}[- ]){1}(\d{7,8}|(\d{3,4}[- ]{1}\d{4}))([- ]{1}\d{1,4})?))$/).test(value);
    } else {
        console.error("不支持的地区");
        return false;
    }
};

$.fn.form.settings.rules.unique = function (value, param) {
    if (value === '') {
        return true;
    }

    var result = false;
    var params = param.split(',');
    var url = params[0];
    var key = params[1];
    var data = {};
    data[key] = value;
    $.ajax({
        async: false,
        url: url,
        data: data,
        type: 'post',
        success: function (msg) {
            if (msg.success) {
                result = true;
            }
        }
    });
    return result;
};

$.fn.form.settings.rules.id = function (value, typeSelector) {
    if (value === '') {
        return true;
    }

    return true;
};

$.fn.form.settings.rules.vin = function (value) {
    if (value === '') {
        return true;
    }

    // Don't accept I, O, Q characters
    if (!/^[a-hj-npr-z0-9]{8}[0-9xX][a-hj-npr-z0-9]{8}$/i.test(value)) {
        return false;
    }

    value = value.toUpperCase();
    var chars = {
            A: 1, B: 2, C: 3, D: 4, E: 5, F: 6, G: 7, H: 8,
            J: 1, K: 2, L: 3, M: 4, N: 5, P: 7, R: 9,
            S: 2, T: 3, U: 4, V: 5, W: 6, X: 7, Y: 8, Z: 9,
            '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9, '0': 0
        },
        weights = [8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2],
        sum = 0,
        length = value.length;
    for (var i = 0; i < length; i++) {
        sum += chars[value.charAt(i) + ''] * weights[i];
    }

    var reminder = sum % 11;
    if (reminder === 10) {
        reminder = 'X';
    }

    return (reminder + '') === value.charAt(8);
};

$.fn.form.settings.rules.date = function (value) {
    if (value === '') {
        return true;
    }

    var converted = Date.parse(value);
    if (isNaN(converted)) {
        return false;
    }

    if (converted < 0) {
        return false;
    }

    return true;
}

$.fn.form.settings.rules.existVin = function (value) {
    if (value === '') {
        return true;
    }
    var result = false;
    $.ajax({
        async: false,
        url: getProjectName() + '/userCar/existVin',
        data: {vin: value},
        type: 'post',
        success: function (msg) {
            if (msg.success) {
                result = true;
            }
        }
    });
    return result;
};
