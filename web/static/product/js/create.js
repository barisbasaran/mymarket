$(document).ready(function () {
    let s = getQueryParam('s');

    fetchProductCategories();
    fetchStores(s);

    $("#submit").on('click', function () {
        createProduct();
    });
});

function fetchStores(s) {
    doGetRequest('/service/stores', (stores) => {
        stores.forEach(store => {
            $('#store').append(
                `<option value="${store.id}">${store.name}</option>`
            );
        });
        if (s) {
            $('#store').val(s).change();
        }
    });
}

function fetchProductCategories() {
    doGetRequest('/service/product-categories', (productCategories) => {
        productCategories.forEach(productCategory => {
            $('#productCategory').append(
                `<option value="${productCategory.id}">${productCategory.name}</option>`
            );
        });
    });
}

function createProduct() {
    let active = $('input[name="active"]:checked').val();
    let priceAmount = $("#price-amount").val().replace(",", ".");
    let priceCurrency = $("#price-currency").val();

    doPostRequest('/service/products', {
        name: $("#name").val(),
        description: $("#description").val(),
        specs: $("#specs").val(),
        active: active,
        productCategory: getProductCategory(),
        store: getStore(),
        price: priceAmount || priceCurrency ? {
            amount: priceAmount,
            currency: priceCurrency ? priceCurrency : null
        } : null,
        images: []
    }, (data) => {
        uploadImages(data.id);
    }, (xhr) => {
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
