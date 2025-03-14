$(document).ready(function () {

    fetchStores();
});

function fetchStores() {
    doGetRequest('/service/stores', (stores) => {
        stores.forEach(store => {
            $('#stores').append(
                `<tr>
                     <th scope="row">${store.id}</th>
                     <td><a href="/store/update.html?s=${store.id}">${store.name}</a></td>
                 </tr>`);
        });
        // create data table
        $('#stores-table').DataTable({language: {url: dataTablesTranslations()}});
    });
}
