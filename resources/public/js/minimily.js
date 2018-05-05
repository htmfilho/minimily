$("#forward").val(window.location.pathname);

$("#frm_delete").submit(function(event) {
    return confirm("Are you sure you want to delete it?");
});