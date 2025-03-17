$(document).ready(function () {
    let pc = getQueryParam('pc');

    $("#sidebar").load("/product-category/sidebar.html", function () {
        populateTranslations("#sidebar *");
        $(".product-filter").on('change', function (e) {
            filterProducts();
        });
    });

    fetchProductCategoryDetails(pc);

    // set active product category in TopNav
    $.initialize('li.product-category-' + pc, function () {
        $(this).addClass("active");
    });

    $('#edit').attr('href', '/product-category/update.html?pc=' + pc);
});

let products = [];

function fetchProductCategoryDetails(pc) {
    doGetRequest('/service/product-categories/' + pc + '/details', (details) => {
        $('#title').html(details.productCategory.name);
        $('#heading').html(details.productCategory.name);
        $('#description').html(details.productCategory.description);
        let str = '';
        let index = 0;
        details.products.forEach(product => {
            if (product.images && product.images.length > 0) {
                products.push({
                    id: product.id,
                    specs: product.specs ? JSON.parse(product.specs) : null
                });
                $('#products').append(
                    `<div class="col-12 col-md-4 col-lg-3 mb-5 product-block-${product.id}">
                         <a class="product-item border border-2 rounded" href="/product/view-details.html?p=${product.id}">
                             <img src="${product.images[0].url}" class="product-in-cat img-fluid product-thumbnail">
                             <h3 class="product-title">${product.name}</h3>
                             <strong class="product-price">&nbsp;${formatPrice(product.price)}</strong>
                             <span class="icon-cross">
                                 <img src="/images/cross.svg" class="img-fluid">
                             </span>
                         </a>
                     </div>`);
            }
        });
        details.subcategories.forEach(subcategory => {
            $('#subcategories').append(
                `<a class="btn btn-warning" href="/product-category/view-details.html?pc=${subcategory.id}">${subcategory.name}</a>`
            );
        });
        details.breadcrumb.forEach(category => {
            $('#category-breadcrumb').append(
                `<li class="breadcrumb-item">
                     <a href="/product-category/view-details.html?pc=${category.id}">${category.name}</a>
                 </li>`
            );
        });
    });
}

function filterProducts() {
    let filterSelector = $(".product-filter");
    let checkedFilters = filterSelector
        .filter(':checked')
        .map(function () {
            return $(this).val();
        }).get();
    let allFilters = filterSelector.get();
    products.forEach(product => {
        let productBlock = $(".product-block-" + product.id);
        if (checkedFilters.length === allFilters.length
            || (product.specs && checkedFilters.includes('' + product.specs.color))) {
            productBlock.show();
        } else {
            productBlock.hide();
        }
    })
}
