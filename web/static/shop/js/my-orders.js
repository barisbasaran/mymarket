$(document).ready(function () {
    fetchOrders();
});

function fetchOrders() {
    doGetRequest('/service/shop/orders', (orders) => {
        for (const [index, order] of orders.entries()) {
            let products = order.items.map(item => item.product.name).join(', ');
            $('#orders').append(
                `<tr>
                     <th scope="row">${index + 1}</th>
                     <td><a href="/shop/view-order.html?o=${order.name}">${order.name}</a></td>
                     <td>${order.dateCreated}</td>
                     <td>${products}</td>
                 </tr>`);
        }
        // create data table
        $('#orders-table').DataTable({language: {url: dataTablesTranslations()}});
    });
}
