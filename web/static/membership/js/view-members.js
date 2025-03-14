$(document).ready(function () {
    fetchMembers();
});

function fetchMembers() {
    doGetRequest('/service/admin/members', (members) => {
        for (const [index, member] of members.entries()) {
            $('#members').append(
                `<tr>
                     <th scope="row">${index + 1}</th>
                     <td>${member.name}</td>
                     <td><a href="/membership/view-member.html?memberId=${member.id}">${member.email}</a></td>
                     <td>${member.active ? "Active" : "Inactive"}</td>
                 </tr>`);
        }
        // create data table
        $('#members-table').DataTable({language: {url: dataTablesTranslations()}});
    });
}
