let loggedIn = false;
let dataTablesTranslationsMap = {
    "tr-TR": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Turkish.json",
    "en-US": "//cdn.datatables.net/plug-ins/1.10.15/i18n/English.json",
    "sv-SE": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Swedish.json",
    "az-AZ": "//cdn.datatables.net/plug-ins/1.10.15/i18n/Azerbaijan.json"
};

$(document).ready(function () {
    $("#header").load("/common/header.html", function () {
        $("#footer").load("/common/footer.html", function () {
            populateTranslations();
            populateTopNav();
            populateSearchBar();
        });
    });
});

function populateSearchBar() {
    let q = getQueryParam('q');
    let $searchQuery = $('#searchQuery');
    if (q) {
        $searchQuery.val(q);
    }
    $searchQuery.on('keypress', function (e) {
        if (e.which === 13) {
            e.preventDefault();
            goToSearch();
        }
    });
    $searchQuery.on('change', function () {
        goToSearch();
    });
    $("#searchButton").on('click', function (e) {
        e.preventDefault();
        goToSearch();
    });
}

function goToSearch() {
    let q = $("#searchQuery").val();
    window.location.href = '/common/search.html?q=' + q;
}

function dataTablesTranslations() {
    return dataTablesTranslationsMap[getAndSetLocale()];
}

function getAndSetLocale() {
    let locale = localStorage.getItem("locale");
    if (!locale) {
        locale = "tr-TR";  // setting default locale
        localStorage.setItem("locale", locale);
    }
    return locale;
}

function populateLocale(supportedLocales) {
    let $localeSel = $("#locale-sel");
    supportedLocales.forEach(locale => {
        $localeSel.append(`<option value="${locale}">${locale.substring(0, 2).toLocaleUpperCase()}</option>`);
    });
    $localeSel.on('change', function () {
        localStorage.setItem("locale", this.value);
        window.location.reload();
    });
    $localeSel.val(getAndSetLocale());
}

function populateTranslations(selector = "*") {
    let translationTags = $(selector).filter("[data-trans]");
    let translationKeys = translationTags.map(function () {
        return $(this).attr("data-trans")
    }).get();
    translateKeys(translationKeys, (translations) => {
        translationTags.each(function () {
            let key = $(this).attr("data-trans");
            if (translations[key]) {
                if ($(this).is('input, textarea')) {
                    $(this).attr("placeholder", translations[key])
                } else {
                    $(this).html(translations[key])
                }
            }
        });
    });
}

function translateKeys(keys, update) {
    doPostRequest('/service/localization/translations', {keys: keys}, update);
}

function showAdminWarning() {
    if ($('#adminWarning').length) {
        new bootstrap.Modal('#adminWarningModal', {keyboard: false}).show();
    }
}

function populateTopNav() {
    doGetRequest('/service/sites/metadata', (siteMetadata) => {
        populateLocale(siteMetadata.supportedLocales);
        populateProductCategoryNav(siteMetadata.productCategories);
        if (siteMetadata.loggedIn) {
            loggedIn = true;
            $('#navbarMemberName').html(siteMetadata.memberName);
            $('.logged-in').show();
            if (siteMetadata.admin) {
                $('.admin').show();
            } else {
                showAdminWarning();
            }
            if (siteMetadata.storeOwner) {
                $('.store-owner').show();
            }
        } else {
            let requireLogin = $("meta[data-require-login]");
            if (requireLogin && requireLogin.attr("data-require-login") === "true") {
                window.location.href = "/membership/login.html?page=" + window.location.pathname;
            } else {
                $('.not-logged-in').show();
                showAdminWarning();
            }
        }
        populateCartNav();
    });
}

function populateProductCategoryNav(productCategories) {
    let productCategoryNav = '';
    productCategories.forEach(productCategory => {
        productCategoryNav +=
            `<li class="product-category-${productCategory.id}">
                 <a class="nav-link" style="font-size: large" 
                     href="/product-category/view-details.html?pc=${productCategory.id}">${productCategory.name}</a>
             </li>`;
    });
    $('#navbarProductCategories').prepend(productCategoryNav);
}

function handleFormError(xhr) {
    let generalError = $(".general-error");
    generalError.html('');
    generalError.hide();
    getFormFieldIds().forEach(field => {
        $("#" + field).removeClass("is-invalid");
        $("." + field + "-error").html('');
    });
    if (xhr.status === 400) {
        xhr.responseJSON.errors.forEach(error => {
            $("#" + error.field).addClass("is-invalid");
            let errorMeassage = $("." + error.field + "-error");
            errorMeassage.html(error.defaultMessage);
            errorMeassage.show();
        });
    } else {
        generalError.html(xhr.responseJSON.message);
        generalError.show();
    }
    scrollToTop();
}

function getFormFieldIds() {
    return $("input, textarea, select").map(function () {
        return $(this).attr('id');
    }).get();
}

function logout() {
    localStorage.removeItem("jwtToken");
    window.location.href = '/membership/login.html?logout=true';
}

function getCart() {
    let shoppingCart = JSON.parse(localStorage.getItem("cart"));
    if (!shoppingCart) {
        shoppingCart = {
            items: []
        }
    }
    return shoppingCart;
}

function setCart(shoppingCart) {
    localStorage.setItem("cart", JSON.stringify(shoppingCart));
}

function populateCartNav() {
    let shoppingCart = getCart();
    let quantity = shoppingCart.items.map((item) => item.quantity).reduce(
        (sum, num) => sum + num, 0
    );
    $('#cartNav').html(quantity);
}

function formatPrice(price) {
    if (!price) {
        return '';
    }
    return Number(price.amount).toFixed(2) + ' ' + price.currency;
}

function formatTotalPrice(price, quantity) {
    if (!price) {
        return '';
    }
    return Number(price.amount * quantity).toFixed(2) + ' ' + price.currency;
}

function scrollToTop() {
    window.scrollTo(0, 0);
}

function limitToNumbers(e) {
    let code = (e.keyCode ? e.keyCode : e.which);
    if (!(code >= 48 && code <= 57) && code !== 8 && code !== 37 && code !== 39) {
        e.preventDefault();
    }
}

function limitToNumbersAndSpace(e) {
    let code = (e.keyCode ? e.keyCode : e.which);
    if (!(code >= 48 && code <= 57) && code !== 8 && code !== 32 && code !== 37 && code !== 39) {
        e.preventDefault();
    }
}

function getReviewStars(rating, count, ratingClass) {
    return `<div class="${ratingClass}">
                <i class="fa fa-star ${rating > 0 ? 'rating-color' : ''} "></i>
                <i class="fa fa-star ${rating > 1 ? 'rating-color' : ''} "></i>
                <i class="fa fa-star ${rating > 2 ? 'rating-color' : ''} "></i>
                <i class="fa fa-star ${rating > 3 ? 'rating-color' : ''} "></i>
                <i class="fa fa-star ${rating > 4 ? 'rating-color' : ''} "></i>
                ${count > 0 ? '<span>(' + count + ')</span>' : ''}
            </div>`;
}

function getQueryParam(p) {
    return new URLSearchParams(window.location.search).get(p);
}

function getAuthHeaders(csrfToken = '') {
    let headers = {
        "Accept-Language": getAndSetLocale()
    };
    if (csrfToken) {
        headers["X-XSRF-TOKEN"] = csrfToken;
    }
    let jwtToken = localStorage.getItem("jwtToken");
    if (jwtToken) {
        headers["Authorization"] = "Bearer " + jwtToken;
    }
    return headers;
}

function doGetRequest(url, onSuccess, onError = (xhr) => {
}) {
    $.ajax({
        url: url,
        type: "GET",
        headers: getAuthHeaders(),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (result) {
            onSuccess(result);
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            onError(xhr);
        }
    });
}

function doCsrfPostRequest(csrfToken, url, data, onSuccess, onError = (xhr) => {
}) {
    doUpdateRequest(csrfToken, 'POST', url, data, onSuccess, onError)
}

function doPostRequest(url, data, onSuccess, onError = (xhr) => {
}) {
    doUpdateRequest('', 'POST', url, data, onSuccess, onError)
}

function doPutRequest(url, data, onSuccess, onError = (xhr) => {
}) {
    doUpdateRequest('', 'PUT', url, data, onSuccess, onError)
}

function doDeleteRequest(url, onSuccess, onError = (xhr) => {
}) {
    doUpdateRequest('', 'DELETE', url, {}, onSuccess, onError)
}

function doUpdateRequest(csrfToken, method, url, data, onSuccess, onError = (xhr) => {
}) {
    $.ajax({
        url: url,
        type: method,
        headers: getAuthHeaders(csrfToken),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(data),
        success: function (result) {
            onSuccess(result);
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            onError(xhr);
        }
    });
}
