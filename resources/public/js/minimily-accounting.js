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

function whenToAccountSelected() {
    $("#to_user").val("");
    var toAccount = $("#to_account").val();
    if (toAccount != "") {
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
                $("#rate_label").text("Rate");
                $("#amount_to_label").text("Amount To");
            }
            calculateAmountTo();
        });
    }
}

function whenToUserSelected() {
    document.getElementById("to_account").selectedIndex = 0;
    $("#rate").prop("disabled", true);
    $("#fee").prop("disabled", true);
    $("#rate").val("");
    $("#fee").val("");
    $("#rate_label").text("Rate");
    $("#amount_to_label").text("Amount To");
    calculateAmountTo();
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
    $.getJSON("/accounting/api/accounts/" + account + "/balance/history", function(data, status) {
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

$('a[data-toggle="tab"]').on('shown.bs.tab', function (event) {
    if(event.target.id === "history-tab") {
        var path = window.location.pathname;
        loadChart(path.substring(path.lastIndexOf("/") + 1));
    }
});

function loadCategories(categories, status) {
    var selectCategory = $("#category");
    selectCategory.empty();
    selectCategory.append($("<option></option>").attr("value", "").text("Select..."));
    categories.forEach(function(category) {
        selectCategory.append($("<option></option>").attr("value", category.id).text(category.name));
    });
}

function loadThirdPartyAccounts(accounts, status) {
    var selectThirdPartyAccount = $("#third-party-account");
    selectThirdPartyAccount.empty();
    selectThirdPartyAccount.append($("<option></option>").attr("value", "").text("Select..."));
    accounts.forEach(function(account) {
        selectThirdPartyAccount.append($("<option></option>").attr("value", account.id).text(account.name));
    });

    if(accounts.length > 0) {
        document.getElementById("third-party-account").selectedIndex = 1;
    } else {
        document.getElementById("third-party-account").selectedIndex = 0;
    }
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

$("#to_account").change(whenToAccountSelected);

$("#to_user").change(whenToUserSelected);

$("#credit").change(function() {
    $.getJSON("/accounting/api/categories/credit", loadCategories);
});

$("#debit").change(function() {
    $.getJSON("/accounting/api/categories/debit", loadCategories);
});

$("#third-party").change(function() {
    $.getJSON("/accounting/api/accounts?third_party=" + $(this).val(), loadThirdPartyAccounts);
});

$(document).ready(whenToAccountSelected);