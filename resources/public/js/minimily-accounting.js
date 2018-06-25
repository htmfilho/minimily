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
    $.getJSON("/api/accounts/" + toAccount + "/currency", function(toCurrency, status) {
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

$(document).ready(getCurrencySelectedAccount);