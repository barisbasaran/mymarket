$(document).ready(function () {

    $("#submit").on('click', function () {
        register();
    });

    translateKeys(['role-user', 'role-store-owner'], (translations) => {
        $('#role').append(`<option value="0">${translations['role-user']}</option>`);
        $('#role').append(`<option value="2">${translations['role-store-owner']}</option>`);
    });
});

function register() {
    let spinner = $("#spinner");
    spinner.show();
    doGetRequest('/service/sites/csrf', (csrf) => {
        doCsrfPostRequest(csrf.token, '/service/members/self/register', {
            email: $("#email").val(),
            password: $("#password").val(),
            role: $("#role").val() ? $("#role").val() : null
        }, (data) => {
            translateKeys(["check-email-to-verify"], (translations) => {
                let $message = $("#message");
                $message.html(translations["check-email-to-verify"]);
                $message.attr("style", "color:green;");
                scrollToTop();
            });
            spinner.hide();
            $("#registerForm").hide();
        }, (xhr) => {
            spinner.hide();
            handleFormError(xhr);
        });
    });
}
