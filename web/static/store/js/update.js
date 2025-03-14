$(document).ready(function () {
    let s = new URLSearchParams(window.location.search).get('s');

    fetchStore(s);

    $("#submit").on('click', function () {
        updateStore(s);
    });

    $("#addProduct").attr("href", "/product/create.html?s=" + s);
});

function fetchStore(s) {
    doGetRequest('/service/stores/' + s, (store) => {
        $('#name').val(store.name);
    }, (xhr) => {
        handleFormError(xhr);
    });
}

function updateStore(s) {
    doPutRequest('/service/stores/' + s, {
        name: $("#name").val()
    }, (data) => {
        window.location.href = "/store/my-stores.html";
    }, (xhr,) => {
        handleFormError(xhr);
    });
}
