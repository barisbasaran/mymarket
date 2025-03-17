$(document).ready(function () {

    populateCart('#cart-items', populateProductRow, () => {
        return `<tr>
                    <td colSpan="3"></td>
                    <td class="fw-semibold text-md-end total-sum-text"></td>
                    <td class="fw-semibold text-md-end total-sum"></td>
                </tr>`;
    });

    let months = Array.from({length: 12}, (v, k) => k + 1);
    months.forEach(month => {
        let monthStr = month < 10 ? "0" + month : "" + month;
        $("#card-expiryMonth").append(`<option value="${monthStr}">${monthStr}</option>`);
    });

    let currentYear = new Date().getFullYear();
    let years = Array.from({length: 20}, (v, k) => k + currentYear);
    years.forEach(year => {
        $("#card-expiryYear").append(`<option value="${year}">${year}</option>`);
    });

    // limit card number to numbers and space only
    $("#card-number").bind({
        keydown: limitToNumbersAndSpace
    });
    // limit CVV to numbers
    $("#card-cvv").bind({
        keydown: limitToNumbers
    });

    populateShipmentInfo();

    $('#shipment-countryId').on('change', function () {
        getCountry(this.value, '', 'shipment-cityId', '', 'shipment-stateId');
    });

    $("#confirmSubmit").on('click', function () {
        $('#confirmSubmitModal').modal('hide');
        submitOrder();
    });

    $(document).on('input', 'input.custom_input, textarea.custom_input', function () {
        displayCustomLabel($(this));
    });
});

function displayCustomLabel(input) {
    let label = $("label[for='" + input.attr('id') + "']");
    if (input.is('input, textarea') && !input.is("[type='tel']")) {
        input.val().length > 0 ? label.show() : label.hide();
    }
}

function displayInputLabels() {
    $(".custom_input").each(function () {
        displayCustomLabel($(this));
    });
}

function populateProductRow(product, item) {
    return `<tr>
                <td>
                    <a href="/product/view-details.html?p=${product.id}">
                        <img src="${product.images[0].url}" style="height: 80px; width: 80px" alt="${product.name}">
                    </a>
                </td>
                <td>
                    <a href="/product/view-details.html?p=${product.id}">${product.name}</a>
                </td>
                <td class="text-md-end"><span>${formatPrice(product.price)}</span></td>
                <td class="text-md-end"><span>${item.quantity}</span></td>
                <td class="text-md-end"><span>${formatTotalPrice(product.price, item.quantity)}</span></td>
            </tr>`;
}

function populateShipmentInfo() {
    doGetRequest('/service/members/self', (member) => {
        if (member.email) {
            $('#shipment-email').val(member.email);
            $('#shipment-email').prop('disabled', true);
        }
        $('#shipment-firstName').val(member.firstName);
        $('#shipment-lastName').val(member.lastName);
        $('#shipment-phone').val(member.phone);
        if (member.address) {
            $('#shipment-addressLine').val(member.address.addressLine);
            $('#shipment-postalCode').val(member.address.postalCode);
            getCountries(member.address.countryId, 'shipment-countryId');
            getCountry(member.address.countryId, member.address.cityId, 'shipment-cityId',
                member.address.stateId, 'shipment-stateId');
        } else {
            getCountries('', 'shipment-countryId');
        }
        displayInputLabels();
    }, (xhr) => {
        getCountries('', 'shipment-countryId');
        displayInputLabels();
    });
}

function submitOrder() {
    doPostRequest('/service/shop/checkout', {
        items: getCart().items,
        shipment: {
            email: $("#shipment-email").val(),
            firstName: $("#shipment-firstName").val(),
            lastName: $("#shipment-lastName").val(),
            phone: $("#shipment-phone").val(),
            countryId: $("#shipment-countryId").val(),
            cityId: $("#shipment-cityId").val(),
            stateId: $("#shipment-stateId").val(),
            addressLine: $("#shipment-addressLine").val(),
            postalCode: $("#shipment-postalCode").val()
        },
        card: {
            name: $("#card-name").val(),
            number: $("#card-number").val().replaceAll(' ', ''),
            expiryMonth: $("#card-expiryMonth").val(),
            expiryYear: $("#card-expiryYear").val(),
            cvv: $("#card-cvv").val()
        }
    }, (order) => {
        $("#checkoutForm").hide();
        translateKeys(["order-been-placed"], (translations) => {
            let $message = $("#message");
            $message.html(
                `<p>${translations["order-been-placed"]} 
                           <a href="/shop/view-order.html?o=${order.name}">${order.name}</a>.
                       </p>`);
            $message.attr("style", "color:green;");
            scrollToTop();
        });
    }, (xhr) => {
        displayInputLabels();
        handleFormError(xhr);
    });
}
