let productMap = new Map();

function populateCart(cartSelector, populateRow, populateSum) {
    let shoppingCart = getCart();
    let productIdParams = shoppingCart.items.map(item => 'p=' + item.productId).join('&');
    if (productIdParams) {
        doGetRequest('/service/products/grouped?' + productIdParams, (products) => {
            products.forEach(product => {
                productMap.set(product.id, product);
            });
            shoppingCart.items.forEach(item => {
                $(cartSelector).append(populateRow(productMap.get(item.productId), item));
            });
            $(cartSelector).append(populateSum());
            populateTotalSum();
        });
    }
}

function populateTotalSum() {
    let shoppingCart = getCart();
    let currency = productMap.get(shoppingCart.items[0].productId).price.currency;
    let totalSum = shoppingCart.items.map(item => {
        let product = productMap.get(item.productId);
        return product.price.amount * item.quantity;
    }).reduce((a, b) => a + b, 0);
    translateKeys(["total"], (translations) => {
        $(".total-sum-text").html(translations.total + ":");
        $(".total-sum").html(formatPrice({amount: totalSum, currency: currency}));
    })
}
