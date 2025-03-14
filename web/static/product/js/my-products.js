$(document).ready(function () {
    fetchMyProducts();
});

function fetchMyProducts() {
    doGetRequest('/service/products', (products) => {
        translateKeys(['active', 'inactive'], (translations) => {
            for (const [index, product] of products.entries()) {
                $('#products').append(
                    `<tr>
                         <th scope="row">${index + 1}</th>
                         <td><a href="/product/update.html?p=${product.id}">${product.name}</a></td>
                         <td>${product.productCategory.name}</td>
                         <td class="text-md-end">${formatPrice(product.price)}</td>
                         <td>${product.description}</td>
                         <td>${product.active ? translations.active : translations.inactive}</td>
                     </tr>`);
            }
            // create data table
            $('#products-table').DataTable({language: {url: dataTablesTranslations()}});
        })
    });
}
