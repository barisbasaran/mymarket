$(document).ready(function () {
    fetchMember();

    $("#submit").on('click', function () {
        updateMember();
    });
});

function fetchMember() {
    doGetRequest('/service/members/self', (member) => {
        $('#firstName').val(member.firstName);
        $('#lastName').val(member.lastName);
        $('#email').html(member.email);
        $('#phone').val(member.phone);
    });
}

function updateMember() {
    doPutRequest('/service/members/self', {
        firstName: $("#firstName").val(),
        lastName: $("#lastName").val(),
        phone: $("#phone").val()
    }, (data) => {
        window.location.href = "/membership/view-member.html";
    }, (xhr) => {
        handleFormError(xhr);
    });
}
