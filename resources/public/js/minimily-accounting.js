function calculateAmountTo() {
    var rate = $("#rate").val();
    var amount = $("#amount").val();
    var balance = parseFloat($("#balance").text()).toFixed(2);
    var fee = $("#fee").val() ? $("#fee").val() : 0;

    if(rate > 0) {
        $("#amount_to").val((rate * amount).toFixed(2));
    } else if (amount > 0) {
        $("#amount_to").val(amount);
    } else {
        $("#amount_to").val(0);
    }
    
    $("#amount_to_view").text($("#amount_to").val());
    var finalBalance = `${balance} - ${amount} - ${fee} = ${(balance - amount - fee).toFixed(2)}`;
    $("#final_balance").text(finalBalance);
}

function getCurrencySelectedAccount() {
    var toAccount = $("#to").val();
    if (toAccount != undefined) {
        $.getJSON("/accounting/api/accounts/" + toAccount + "/currency", function(toCurrency, status) {
            var fromCurrency = $("#currency").text();
            if (toCurrency != fromCurrency) {
                $("#rate").prop("disabled", false);
                $("#fee").prop("disabled", false);
                $("#rate_label").text("Rate ("+ toCurrency +")")
                $("#amount_to_label").text("Amount To ("+ toCurrency +")");
            } else {
                $("#rate").prop("disabled", true);
                $("#fee").prop("disabled", true);
                $("#rate").val("");
                $("#fee").val("");
                $("#rate_label").text("Rate")
                $("#amount_to_label").text("Amount To");
            }
            calculateAmountTo();
        });
    }
}

function getLabelsBalanceHistory(balanceHistory) {
    const labels = [];
    balanceHistory.forEach(function(item) {
        labels.push(item.date_transaction);
    });
    return labels;
}

function getValuesBalanceHistory(balanceHistory) {
    const values = [];
    balanceHistory.forEach(function(item) {
        values.push(item.balance);
    });
    return values;
}

function loadChart(account) {
    $.getJSON("/api" + account + "/balance/history", function(data, status) {
        balanceHistory = data;
        // charts
        var balanceHistoryChart = echarts.init(document.getElementById('balance-history-chart'));
        var balanceHistoryLabels = getLabelsBalanceHistory(balanceHistory);
        var balanceHistoryValues = getValuesBalanceHistory(balanceHistory);
        
        // specify chart configuration item and data
        option = {
            tooltip: {
                show: true,
                trigger: 'axis'
            },
            legend: {
                data:['Balance']
            },
            xAxis: {
                type: 'category',
                data: balanceHistoryLabels
            },
            yAxis: {
                type: 'value',
                splitLine: {show: false}
            },
            series: [{
                name: 'Balance',
                data: balanceHistoryValues,
                type: 'line'
            }]
        };

        // use configuration item and data specified to show chart
        balanceHistoryChart.setOption(option);
    });
}

function loadCategories(categories, status) {
    var selectCategory = $("#category");
    selectCategory.empty();
    selectCategory.append($("<option></option>").attr("value", "").text("Select..."));
    categories.forEach(function(category) {
        selectCategory.append($("<option></option>").attr("value", category.id).text(category.name));
    });
}

$("#amount").change(function() {
    var amount = $(this).val() ? $(this).val() : 0;
    $(this).val(parseFloat(amount).toFixed(2));
    calculateAmountTo();
});

$("#rate").change(function() {
    var rate = $(this).val() ? $(this).val() : 0;
    $(this).val(parseFloat(rate));
    calculateAmountTo();
});

$("#fee").change(function() {
    var fee = $(this).val() ? $(this).val() : 0;
    $(this).val(parseFloat(fee).toFixed(2));
    calculateAmountTo();
});

$("#to").change(getCurrencySelectedAccount);

$("#credit").change(function() {
    $.getJSON("/accounting/api/categories/credit", loadCategories);
});

$("#debit").change(function() {
    $.getJSON("/accounting/api/categories/debit", loadCategories);
});

$(document).ready(getCurrencySelectedAccount);

$(document).ready(function() {
    var path = window.location.pathname;
    if(path.includes("accounting")) {
        loadChart(path);
    }
});