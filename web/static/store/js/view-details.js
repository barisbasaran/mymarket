$(document).ready(function () {
    let s = getQueryParam('s');

    fetchStoreDetails(s);
});

function fetchStoreDetails(s) {
    doGetRequest('/service/stores/' + s + '/details', (details) => {
        $('#title').html(details.name);
        $('#heading').html(details.name);
        details.products.forEach(product => {
            if (product.images && product.images.length > 0) {
                $('#products').append(
                    `<div class="col-12 col-md-4 col-lg-3 mb-5 product-block-${product.id}">
                         <a class="product-item border border-2 rounded" href="/product/view-details.html?p=${product.id}">
                             <img src="${product.images[0].url}" class="product-in-cat img-fluid product-thumbnail">
                             <h3 class="product-title">${product.name}</h3>
                             <strong class="product-price">&nbsp;${formatPrice(product.price)}</strong>
                             <span class="icon-cross">
                                 <img src="/img/theme/cross.svg" class="img-fluid">
                             </span>
                         </a>
                     </div>`);
            }
        });
    });
}
