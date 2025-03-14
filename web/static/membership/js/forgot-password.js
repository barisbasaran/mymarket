$(document).ready(function () {

    $("#submit").on('click', function () {
        forgotPassword();
    });
});

function forgotPassword() {
    let spinner = $("#spinner");
    spinner.show();
    doPostRequest('/service/members/self/password/forgot', {
        email: $("#email").val()
    }, (data) => {
        translateKeys(["password-reset-link"], (translations) => {
            $("#message").html(translations["password-reset-link"]);
            $("#message").attr("style", "color:green;");
            scrollToTop();
        });
        spinner.hide();
        $("#forgotPasswordForm").hide();
    }, (xhr) => {
        spinner.hide();
        handleFormError(xhr);
    });
}
