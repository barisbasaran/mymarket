$(document).ready(function () {

    $("#submit").on('click', function () {
        createProductCategory();
    });

    fetchProductCategories();
});

function createProductCategory() {
    let active = $('input[name="active"]:checked').val();
    let parent = $("#parent").val();
    doPostRequest('/service/admin/product-categories', {
        name: $("#name").val(),
        parent: parent ? {id: parent} : null,
        description: $("#description").val(),
        active: active
    }, (data) => {
        window.location.href = "/product-category/view-all.html";
    }, (xhr) => {
        handleFormError(xhr);
    });
}

function fetchProductCategories() {
    doGetRequest('/service/admin/product-categories', (productCategories) => {
        productCategories.forEach(productCategory => {
            $('#parent').append(
                `<option value="${productCategory.id}">${productCategory.name}</option>`
            );
        });
    }, (xhr) => {
        handleFormError(xhr);
    });
}
