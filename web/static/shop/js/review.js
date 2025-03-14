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
        translateKeys(["review-been-placed"], (translations) => {
            let $message = $("#message");
            $message.html(`<p>${translations["review-been-placed"]}</p>`);
            $message.attr("style", "color:green;");
            scrollToTop();
        });
    }, (xhr) => {
        handleFormError(xhr);
    });
}
