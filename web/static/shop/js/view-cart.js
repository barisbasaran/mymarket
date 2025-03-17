$(document).ready(function () {
    populateCart('#cart-items', populateProductRow, () => {
        return `<tr>
                    <td colSpan="3"></td>
                    <td class="fw-semibold text-md-end total-sum-text"></td>
                    <td class="fw-semibold text-md-end total-sum"></td>
                    <td></td>
                </tr>`;
    });
});

function emptyCartConfirm() {
    new bootstrap.Modal('#emptyCartModal', {}).show();
}

function emptyCart() {
    localStorage.removeItem('cart');
    location.reload();
}

function removeItem(productId) {
    let shoppingCart = getCart();
    shoppingCart.items = shoppingCart.items.filter(item => item.productId !== productId);
    setCart(shoppingCart);
    location.reload();
}

function populateItemQuantity(item) {
    let price = productMap.get(item.productId).price;
    let itemTotalPrice = formatTotalPrice(price, item.quantity);
    $("#cart-item-" + item.productId + " td span.quantity").html(item.quantity);
    $("#cart-item-" + item.productId + " td span.total-price").html(itemTotalPrice);
}

function populateProductRow(product, item) {
    return `<tr id="cart-item-${product.id}">
                <td>
                    <a href="/product/view-details.html?p=${product.id}">
                        <img src="${product.images[0].url}" style="height: 80px; width: 80px" alt="${product.name}">
                    </a>
                </td>
                <td><a href="/product/view-details.html?p=${product.id}">${product.name}</a></td>
                <td class="text-md-end"><span class="item-price">${formatPrice(product.price)}</span></td>
                <td class="text-md-end">
                    <button name="" class="btn btn-primary" onclick="decreaseItemQuantity(${product.id})">-</button>
                    <button name="" class="btn btn-primary" onclick="increaseItemQuantity(${product.id})">+</button>
                    <span class="quantity" style="margin: 1rem;">${item.quantity}</span>   
                </td>
                <td class="text-md-end">
                    <span class="total-price">${formatTotalPrice(product.price, item.quantity)}</span> 
                </td>
                <td class="text-md-end"><button name="" class="btn btn-primary" onclick="removeItem(${product.id})">X</button></td>
            </tr>`;
}

function increaseItemQuantity(productId) {
    let shoppingCart = getCart();
    shoppingCart.items.forEach(item => {
        if (item.productId === productId) {
            item.quantity++;
            populateItemQuantity(item);
        }
    });
    setCart(shoppingCart);
    populateCartNav();
    populateTotalSum();
}

function decreaseItemQuantity(productId) {
    let shoppingCart = getCart();
    shoppingCart.items.forEach(item => {
        if (item.productId === productId) {
            if (item.quantity > 1) item.quantity--;
            populateItemQuantity(item);
        }
    });
    setCart(shoppingCart);
    populateCartNav();
    populateTotalSum();
}

function checkout() {
    let $message = $("#message");
    $message.html("");
    let shoppingCart = getCart();
    if (shoppingCart.items.length === 0) {
        $message.html("Your shopping cart is empty!");
        return;
    }
    if (loggedIn) {
        window.location.href = "/shop/checkout.html";
    } else {
        new bootstrap.Modal('#loginForCheckoutModal', {}).show();
    }
}
