$(document).ready(function () {
    let co = getQueryParam('co');

    fetchCountry(co);
});

function fetchCountry(co) {
    doGetRequest('/service/locations/countries/' + co, (country) => {
        $('#countryName').html(country.name);
        for (const [index, city] of country.cities.entries()) {
            $('#cities').append(
                `<tr>
                     <th scope="row">${index + 1}</th>
                     <td>${city.name}</td>
                 </tr>`);
        }
        // create data table
        $('#cities-table').DataTable({language: {url: dataTablesTranslations()}});
    });
}
