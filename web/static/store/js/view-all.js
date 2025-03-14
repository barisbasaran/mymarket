$(document).ready(function () {

    fetchStores();
});

function fetchStores() {
    doGetRequest('/service/admin/stores', (stores) => {
        stores.forEach(store => {
            $('#stores').append(
                `<tr>
                     <th scope="row">${store.id}</th>
                     <td>${store.name}</td>
                 </tr>`);
        });
        // create data table
        $('#stores-table').DataTable({language: {url: dataTablesTranslations()}});
    });
}
