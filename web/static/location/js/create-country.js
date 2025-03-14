$(document).ready(function () {

    $("#submit").on('click', function () {
        createCountry();
    });
});

function createCountry() {
    let hasState = $('input[name="hasState"]:checked').val();
    doPostRequest('/service/admin/locations/countries', {
        name: $("#name").val(),
        hasState: hasState,
        cities: $("#cities").val()
    }, (data) => {
        window.location.href = "/location/view-countries.html";
    }, (xhr) => {
        handleFormError(xhr);
    });
}
