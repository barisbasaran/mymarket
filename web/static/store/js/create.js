$(document).ready(function () {

    $("#submit").on('click', function () {
        createStore();
    });
});

function createStore() {
    doPostRequest('/service/stores', {
        name: $("#name").val()
    }, (data) => {
        window.location.href = "/store/my-stores.html";
    }, (xhr) => {
        handleFormError(xhr);
    });
}
