$(document).ready(function () {

    $("#submit").on('click', function () {
        let orderId = $('#orderId').val();
        window.location.href = '/shop/view-order.html?o=' + orderId;
    });
});
