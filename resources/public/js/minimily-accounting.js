function getCurrencySelectedAccount() {
    var toAccount = $("#to").val();
    $.getJSON("/api/accounts/" + toAccount + "/currency", function(data, status) {
        var fromCurrency = $("#currency").text();
        if (data != fromCurrency) {
            $("#rate").prop("disabled", false);
            $("#rate_label").text("Rate ("+ data +")")
            $("#amount_to_label").text("Amount To ("+ data +")");
        } else {
            $("#rate").prop("disabled", true);
            $("#rate").val("");
            $("#rate_label").text("Rate")
            $("#amount_to_label").text("Amount To");
        }
    });
}

function calculateAmountTo() {
    var rate = $("#rate").val();
    var amount = $("#amount").val();
    if(rate) {
        $("#amount_to").val((rate * amount).toFixed(2));
    } else if (amount) {
        $("#amount_to").val(amount);
    } else {
        $("#amount_to").val(0);
    }
    $("#amount_to_view").text($("#amount_to").val());
    var balance = parseFloat($("#balance").text());
    $("#final_balance").text((balance - amount).toFixed(2));
}

$("#amount").change(function() {
    $(this).val(parseFloat($(this).val()).toFixed(2));
    calculateAmountTo();
});

$("#rate").change(function() {
    $(this).val(parseFloat($(this).val()));
    calculateAmountTo();
});

$("#to").change(getCurrencySelectedAccount);

$(document).ready(function() {
    getCurrencySelectedAccount();
    calculateAmountTo();
});