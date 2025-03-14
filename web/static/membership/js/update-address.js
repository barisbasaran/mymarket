$(document).ready(function () {
    fetchMember();

    $('#countryId').on('change', function () {
        getCountry(this.value, '', 'cityId', '', 'stateId');
    });

    $("#submit").on('click', function () {
        updateAddress();
    });
});

function fetchMember() {
    doGetRequest('/service/members/self', (member) => {
        if (member.address) {
            $('#addressLine').val(member.address.addressLine);
            $('#postalCode').val(member.address.postalCode);
            getCountries(member.address.countryId, 'countryId');
            getCountry(member.address.countryId, member.address.cityId, 'cityId'
                , member.address.stateId, 'stateId');
        } else {
            getCountries('', 'countryId');
        }
    });
}

function updateAddress() {
    doPostRequest('/service/addresses/self', {
        countryId: $("#countryId").val(),
        cityId: $("#cityId").val(),
        stateId: $("#stateId").val(),
        addressLine: $("#addressLine").val(),
        postalCode: $("#postalCode").val()
    }, (data) => {
        window.location.href = "/membership/view-member.html";
    }, (xhr) => {
        handleFormError(xhr);
    })
}
