$(document).ready(function () {
    let p = new URLSearchParams(window.location.search).get('p');

    fetchProductCategories(p);
    fetchStores();

    $("#submit").on('click', function () {
        updateProduct(p);
    });

    $("#uploadImages").on('click', function () {
        uploadImages(p);
    });

    $("#delete").on('click', function () {
        deleteProduct(p);
    });
});

function fetchStores() {
    doGetRequest('/service/stores', (stores) => {
        stores.forEach(store => {
            $('#store').append(
                `<option value="${store.id}">${store.name}</option>`
            );
        });
    });
}

function fetchProductCategories(p) {
    doGetRequest('/service/product-categories', (productCategories) => {
        productCategories.forEach(productCategory => {
            $('#productCategory').append(
                `<option value="${productCategory.id}">${productCategory.name}</option>`
            );
        });
        fetchProduct(p);
    });
}

function fetchProduct(p) {
    doGetRequest('/service/products/' + p, (product) => {
        $('#name').val(product.name);
        $('#description').val(product.description);
        $('#specs').val(product.specs);
        let pc = product.productCategory.id;
        $('#productCategory').val(pc);
        $('#store').val(product.store.id);
        if (!product.active) {
            $('#inactive').prop("checked", true);
        }
        if (product.price) {
            $('#price-amount').val(product.price.amount);
            $('#price-currency').val(product.price.currency);
        }
        $('#view').attr("href", `/product/view-details.html?p=${p}`);
        product.images.forEach(image => {
            $('#currentImages').append(
                `<div class="col-sm-3" style="margin-bottom: 1.5rem;">
                     <img src="${image.url}" alt="" style="height: 75%; width: 75%" />
                     <a href="javascript:deleteImage(${product.id}, ${image.id})" class="btn btn-primary">X</a>
                 </div>`);
        });
    }, (xhr) => {
        handleFormError(xhr);
    });
}

function updateProduct(p) {
    let active = $('input[name="active"]:checked').val();
    let priceAmount = $("#price-amount").val().replace(",", ".");
    let priceCurrency = $("#price-currency").val();

    doPutRequest('/service/products/' + p, {
        name: $("#name").val(),
        description: $("#description").val(),
        specs: $("#specs").val(),
        productCategory: getProductCategory(),
        store: getStore(),
        active: active,
        price: priceAmount || priceCurrency ? {
            amount: priceAmount,
            currency: priceCurrency ? priceCurrency : null
        } : null,
        images: []
    }, (data) => {
        window.location.href = "/product/view-details.html?p=" + p;
    }, (xhr) => {
        handleFormError(xhr);
    });
}

function deleteImage(productId, imageId) {
    doDeleteRequest(`/service/products/${productId}/images/${imageId}`, (data) => {
        window.location.href = "/product/update.html?p=" + productId;
    }, (xhr) => {
        handleFormError(xhr);
    });
}

function deleteProduct(p) {
    doDeleteRequest('/service/products/' + p, (data) => {
        window.location.href = "/product/my-products.html";
    }, (xhr) => {
        $("#deleteProductModal").modal('hide');
        handleFormError(xhr);
    });
}

function getStore() {
    let store = $("#store").val();
    if (store) {
        return {
            id: store
        };
    } else {
        return null;
    }
}

function getProductCategory() {
    let productCategory = $("#productCategory").val();
    if (productCategory) {
        return {
            id: productCategory
        };
    } else {
        return null;
    }
}
