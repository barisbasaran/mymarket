$(document).ready(function () {
    populateLogoutMessage();

    $("#submit").on('click', function () {
        login();
    });
});

function populateLogoutMessage() {
    let logout = new URLSearchParams(window.location.search).get('logout');
    if (logout) {
        let key = 'logged-out';
        translateKeys([key], (translations) => {
            let generalError = $(".general-error");
            generalError.html(translations[key]);
            generalError.show();
        })
    }
}

function login() {
    doGetRequest('/service/sites/csrf', (csrf) => {
        doCsrfPostRequest(csrf.token, '/service/login', {
            email: $("#email").val(),
            password: $("#password").val()
        }, (data) => {
            localStorage.setItem("jwtToken", data.token);
            let page = new URLSearchParams(window.location.search).get('page');
            if (page) {
                window.location.href = page;
            } else {
                window.location.href = "/";
            }
        }, (xhr) => {
            handleFormError(xhr);
        });
    });
}
