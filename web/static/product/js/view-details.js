$(document).ready(function () {
    let p = getQueryParam('p');
    let r = getQueryParam('r');

    $('#edit').attr('href', '/product/update.html?p=' + p);

    fetchProductDetails(p, r);

    $('#addToCart').click(function () {
        addToCart(p);
        populateCartNav();
    });
});

function fetchProductDetails(p, r) {
    doGetRequest('/service/products/' + p + '/details', (productDetails) => {
        let product = productDetails.product;
        $('#title').html(product.name);
        $('#heading').html(product.name);
        $('#description').html(product.description);
        $('#store').html(`<a href="/store/view-details.html?s=${product.store.id}">${product.store.name}</a>`);
        if (product.price) {
            $('#addToCart').show();
            $('#price-amount').html(product.price.amount);
            $('#price-currency').html(product.price.currency);
        }
        if (productDetails.myProduct) {
            $('#edit').show();
        }
        populateImageCarousel(product);

        // set active product category in TopNav
        $.initialize('li.product-category-' + product.productCategory.id, function () {
            $(this).addClass("active");
        });

        populateProductBreadcrumb(productDetails.breadcrumb);

        populateSimilarProducts(productDetails.similarProducts);

        populateProductReviews(productDetails.reviews, r);

        populateProductRating(productDetails.reviewSummary);
    });
}

function populateImageCarousel(product) {
    for (const [index, image] of product.images.entries()) {
        let activeClass = index === 0 ? 'active' : '';
        $('.carousel-inner').append(
            `<div class="carousel-item ${activeClass}">
                 <a href="#" data-index="${index}">
                     <img src="${image.url}" class="img-fluid d-block w-100 product" alt="${product.name}">
                 </a>     
             </div>`);
        let currentIndicator = index === 0 ? 'class="active" aria-current="true" ' : '';
        $('.carousel-indicators').append(
            `<button type="button" data-bs-target="#myCarousel" data-bs-slide-to="${index}" 
                 ${currentIndicator} aria-label="Slide ${index}" style="width: 100px;">
                 <img class="d-block w-100" src="${image.url}" class="img-fluid" />
             </button>`);
    }
    const carousel = new bootstrap.Carousel("#myCarousel", {
        ride: 'carousel'
    });

    // zoom images
    const imageGallerySize = product.images.length;
    let imageGalleryIndex = 0;
    $('div.carousel-item a').click(function (e) {
        e.preventDefault();
        let imageUrl = $(this).find("img").attr("src");
        $("#imageModalLink img").attr("src", imageUrl);
        new bootstrap.Modal('#imageModal', {}).show();

        imageGalleryIndex = parseInt($(this).attr("data-index"));
    });
    $('#imageModalLink').click(function (e) {
        e.preventDefault();
        imageGalleryIndex = (imageGalleryIndex + 1) % imageGallerySize;
        let imageLink = $(`div.carousel-item a[data-index=${imageGalleryIndex}]`);
        let imageUrl = imageLink.find("img").attr("src");
        $(this).find("img").attr("src", imageUrl);
    });
}

function populateProductBreadcrumb(breadcrumb) {
    breadcrumb.forEach(category => {
        $('#category-breadcrumb').append(
            `<li class="breadcrumb-item">
                 <a href="/product-category/view-details.html?pc=${category.id}">${category.name}</a>
             </li>`);
    });
}

function populateSimilarProducts(similarProducts) {
    if (similarProducts.length > 0) {
        $('.similar-products').show();
    }
    similarProducts.forEach(product => {
        $('#similarProducts').append(
            `<div class="col-12 col-md-4 col-lg-3 mb-5">
                <a class="product-item border border-2 rounded" href="/product/view-details.html?p=${product.id}">
                    <img src="${product.images[0].url}" class="product-in-cat img-fluid product-thumbnail">
                    <h3 class="product-title">${product.name}</h3>
                    <strong class="product-price">${formatPrice(product.price)}</strong>
                    <span class="icon-cross">
                        <img src="/img/theme/cross.svg" class="img-fluid" alt="">
                    </span>
                </a>
            </div>`);
    });
}

function populateProductReviews(reviews, r) {
    if (reviews.length > 0) {
        $('.product-reviews').show();
    }
    reviews.forEach(review => {
        $('#productReviews').append(
            `<div class="mt-3 border-bottom" id="review-${review.id}">
                <h6 class="mb-3">${getReviewStars(review.rating, 0, 'ratings-medium')}</h6>         
                <h6>${review.comment}</h6>         
             </div>`);
    });
    if (r) {
        let reviewBlock = document.getElementById("review-" + r);
        if (reviewBlock) {
            window.scrollTo(0, reviewBlock.offsetTop);
        }
    }
}

function populateProductRating(reviewSummary) {
    if (reviewSummary.rating > 0) {
        $("#review-rating").html(getReviewStars(reviewSummary.rating, reviewSummary.count, "ratings"));
    } else {
        $("#review-rating").hide();
    }
}

function addToCart(p) {
    let productId = parseInt(p)
    let shoppingCart = getCart();
    let itemFound = false;
    shoppingCart.items.forEach(item => {
        if (item.productId === productId) {
            item.quantity++;
            itemFound = true;
        }
    });
    if (!itemFound) {
        shoppingCart.items.push({
            productId: productId,
            quantity: 1
        });
    }
    setCart(shoppingCart);
    new bootstrap.Modal('#addedToCartModal', {}).show();
}
