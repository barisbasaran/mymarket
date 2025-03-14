$(document).ready(function () {
    fetchProductCategories();
});

function fetchProductCategories() {
    doGetRequest('/service/admin/product-categories', (productCategories) => {
        for (const [index, productCategory] of productCategories.entries()) {
            $('#product-categories').append(
                `<tr>
                     <th scope="row">${index + 1}</th>
                     <td><a href="/product-category/update.html?pc=${productCategory.id}">${productCategory.name}</a></td>
                     <td>${productCategory.parent ? productCategory.parent.name : ''}</td>
                     <td>${productCategory.description}</td>
                     <td>${productCategory.active ? "Active" : "Inactive"}</td>
                 </tr>`);
        }
        // create data table
        $('#product-categories-table').DataTable({language: {url: dataTablesTranslations()}});
    })
}
