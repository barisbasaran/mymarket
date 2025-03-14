$(document).ready(function () {
    let q = new URLSearchParams(window.location.search).get('q');
    let page = new URLSearchParams(window.location.search).get('page');
    doSearch(q, page ? parseInt(page) : 0);
});

function doSearch(q, page) {
    const size = 10;
    let $results = $("#results");
    doGetRequest(`/service/search?page=${page}&size=${size}&query=${q}`, (searchResult) => {
        searchResult.items.forEach(item => {
            $results.append(
                `<div class="mb-3 row">
                     <div class="row">
                         <div class="col h5">
                             <a style="color: blue" href="${item.url}">${item.name}</a>
                         </div>
                     </div>
                     <div class="row h6">
                         <div class="col ms-6">${item.keywords || ''}</div>
                     </div>
                 </div>`);
        });
        let totalHits = searchResult.totalHits;
        for (let i = 0; i < Math.ceil(totalHits / size); i++) {
            let link;
            if (i === page) {
                link = `<a class="btn btn-secondary me-1" href="#">${i + 1}</a>`;
            } else {
                link = `<a class="btn btn-primary me-1" href="/common/search.html?q=${q}&page=${i}">${i + 1}</a>`;
            }
            $("#pagination").append(link);
        }
    }, (xhr) => {
        handleFormError(xhr);
    });
}
