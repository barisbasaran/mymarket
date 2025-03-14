$(document).ready(function () {

    $("#submit").on('click', function () {
        changePassword();
    });
});

function changePassword() {
    doPostRequest('/service/members/self/password', {
        password1: $("#password1").val(),
        password2: $("#password2").val()
    }, (data) => {
        translateKeys(["password-changed"], (translations) => {
            $("#message").html(translations["password-changed"]);
            $("#message").attr("style", "color:green;");
            scrollToTop();
        });
        $("#changePasswordForm").hide();
    }, (xhr) => {
        handleFormError(xhr);
    });
}
