$("#forward").val(window.location.pathname);

$("#frm_delete").submit(function(event) {
    return confirm("Are you sure you want to delete it?");
});

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

$(document).ready(function() {
    $.getJSON("/api" + window.location.pathname + "/balance/history", function(data, status) {
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
});