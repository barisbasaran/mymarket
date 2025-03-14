$(document).ready(function () {

    fetchCountries();
});

function fetchCountries() {
    doGetRequest('/service/locations/countries', (countries) => {
        countries.forEach(country => {
            $('#countries').append(
                `<tr>
                   <th scope="row">${country.id}</th>
                   <td><a href="/location/view-country.html?co=${country.id}">${country.name}</a></td>
                 </tr>`);
        });
        // create data table
        $('#countries-table').DataTable({language: {url: dataTablesTranslations()}});
    });
}
