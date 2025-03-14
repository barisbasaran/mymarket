function uploadImages(productId) {
    let files = new FormData();
    let fileList = $('#images')[0].files;
    if (fileList.length === 0) {
        window.location.href = "/product/view-details.html?p=" + productId;
        return;
    }
    for (let i = 0; i < fileList.length; i++) {
        files.append('fileName', fileList[i]);
    }

    $.ajax({
        url: '/service/products/' + productId + '/images',
        type: 'POST',
        headers: getAuthHeaders(),
        processData: false,
        contentType: false,
        data: files,
        success: function (response) {
            window.location.href = "/product/view-details.html?p=" + productId;
        },
        error: function (xhr) {
            handleFormError(xhr);
        }
    });
}
