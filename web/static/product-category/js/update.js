$(document).ready(function () {
    let pc = getQueryParam('pc');

    fetchProductCategory(pc);

    $("#submit").on('click', function () {
        updateProductCategory(pc);
    });

    $("#delete").on('click', function () {
        deleteProductCategory(pc);
    });
});

function fetchProductCategory(pc) {
    doGetRequest('/service/admin/product-categories/' + pc, (productCategory) => {
        $('#name').val(productCategory.name);
        $('#description').val(productCategory.description);
        if (!productCategory.active) {
            $('#inactive').prop("checked", true);
        }
        productCategory.potentialParents.forEach(productCategory => {
            $('#parent').append(
                `<option value="${productCategory.id}">${productCategory.name}</option>`
            );
        });
        if (productCategory.parent) {
            $('#parent').val(productCategory.parent.id);
        }
    }, (xhr) => {
        handleFormError(xhr);
    });
}

function updateProductCategory(pc) {
    let active = $('input[name="active"]:checked').val();
    let parent = $("#parent").val();
    doPutRequest('/service/admin/product-categories/' + pc, {
        name: $("#name").val(),
        parent: parent ? {id: parent} : null,
        description: $("#description").val(),
        active: active
    }, (data) => {
        window.location.href = "/product-category/view-all.html";
    }, (xhr,) => {
        handleFormError(xhr);
    });
}

function deleteProductCategory(pc) {
    doDeleteRequest('/service/admin/product-categories/' + pc, (data) => {
        window.location.href = "/product-category/view-all.html";
    }, (xhr) => {
        handleFormError(xhr);
    });
}
