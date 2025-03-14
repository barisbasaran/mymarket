$(document).ready(function () {
    let o = new URLSearchParams(window.location.search).get('o');

    fetchOrder(o);
});

function fetchOrder(o) {
    doGetRequest('/service/shop/orders/' + o, (order) => {
        $('#order-name').html(order.name);
        $('#order-date').html(order.dateCreated);
        $('#shipment-email').html(order.shipment.email);
        $('#shipment-name').html(order.shipment.name);
        $('#shipment-phone').html(order.shipment.phone);
        $('#shipment-city').html(order.shipment.cityName);
        $('#shipment-country').html(order.shipment.countryName);
        $('#shipment-addressLine').html(order.shipment.addressLine);
        $('#shipment-postalCode').html(order.shipment.postalCode);
        if (order.shipment.stateId) {
            $('#shipment-state').html(order.shipment.stateName);
            $('#state').show();
        }
        translateKeys(["total", "review"], (translations) => {
            order.items.forEach(item => {
                let rating = item.reviewRating;
                let reviewStr = rating > 0 ? getReviewStars(rating, 0, "ratings-small") :
                    `<a class="btn btn-secondary" href="javascript:reviewProduct('${order.name}', ${item.id})">${translations.review}</a>`;
                $('#order-items').append(
                    `<tr>
                     <td>
                         <a href="/product/view-details.html?p=${item.product.id}">
                             <img src="${item.product.images[0].url}" style="height: 80px; width: 80px" alt="${item.product.name}">
                         </a>
                     </td>
                     <td>
                         <a href="/product/view-details.html?p=${item.product.id}">${item.product.name}</a>
                     </td>
                     <td class="text-md-end">${formatPrice(item.price)}</td>
                     <td class="text-md-end">${item.quantity}</td>
                     <td class="text-md-end">${formatTotalPrice(item.price, item.quantity)}</td>
                     <td class="text-md-center">${reviewStr}</td>
                 </tr>`
                );
            });
            $('#order-items').append(
                `<tr>
                     <td colspan="3"></td>
                     <td class="fw-semibold text-md-end total-sum-text">${translations.total}:</td>
                     <td class="fw-semibold text-md-end total-sum">${formatPrice(order.totalPrice)}</td>
                 </tr>`
            );
        })
    }, (xhr) => {
        handleFormError(xhr);
        $("#order-summary").hide();
    });
}

function reviewProduct(orderName, itemId) {
    window.location.href = `/shop/review.html?o=${orderName}&i=${itemId}`;
}
