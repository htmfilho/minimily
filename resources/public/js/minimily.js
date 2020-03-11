const urlParams = new URLSearchParams(window.location.search);
$("#forward").val(urlParams.get('forward'));

$("#frm_delete").submit(function(event) {
    return confirm("Are you sure you want to delete it?");
});

$(document).ready(function() {
    $('form:first *:input[type!=hidden]:first').focus();
});