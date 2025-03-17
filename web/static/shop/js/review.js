$(document).ready(function () {
    let urlSearchParams = new URLSearchParams(window.location.search);
    let orderName = urlSearchParams.get('o');
    let itemId = urlSearchParams.get('i');

    let rating = 0;
    $("div.ratings a").on('click', function () {
        rating = parseInt($(this).find("i").attr("data-rating"));

        $("div.ratings a i").each(function () {
            let starRating = parseInt($(this).attr("data-rating"));
            if (starRating <= rating) {
                $(this).addClass("rating-color");
            } else {
                $(this).removeClass("rating-color");
            }
        });
    });

    $("#submit").on('click', function () {
        submitReview(orderName, itemId, rating);
    });
});

function submitReview(orderName, itemId, rating) {
    let comment = $("#comment").val();
    doPostRequest('/service/reviews/orders/' + orderName, {
        comment: comment,
        rating: rating,
        orderItem: {
            id: itemId
        }
    }, (review) => {
        $("#reviewForm").hide();
        translateKeys(["review-been-placed", "view-product"], (translations) => {
            let $message = $("#message");
            $message.html(`
                       <p>
                           <span>${translations["review-been-placed"]}</span>
                           <a href="/product/view-details.html?p=${review.orderItem.product.id}&r=${review.id}">
                               ${translations["view-product"]}
                           </a>
                       </p>`);
            $message.attr("style", "color:green;");
            scrollToTop();
        });
    }, (xhr) => {
        handleFormError(xhr);
    });
}
