$(document).ready(function () {
    let token = new URLSearchParams(window.location.search).get('token');

    $("#submit").on('click', function () {
        resetPassword(token);
    });
});

function resetPassword(token) {
    doPostRequest('/service/members/self/password/reset', {
        password1: $("#password1").val(),
        password2: $("#password2").val(),
        token: token
    }, (data) => {
        translateKeys(["password-been-reset"], (translations) => {
            $("#message").html(translations["password-been-reset"]);
            $("#message").attr("style", "color:green;");
            scrollToTop();
        });
        $("#resetPasswordForm").hide();
    }, (xhr) => {
        handleFormError(xhr);
    });
}
