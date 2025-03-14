$(document).ready(function () {
    let memberId = new URLSearchParams(window.location.search).get('memberId');

    fetchMember(memberId);
});

function fetchMember(memberId) {
    let url = memberId ? '/service/admin/members/' + memberId : '/service/members/self';
    doGetRequest(url, (member) => {
        $('#name').html(member.name);
        $('#email').html(member.email);
        $('#phone').html(member.phone);
        if (member.address) {
            $('#countryName').html(member.address.countryName);
            $('#cityName').html(member.address.cityName);
            $('#addressLine').html(member.address.addressLine);
            $('#postalCode').html(member.address.postalCode);
            if (member.address.hasState) {
                $('#state').show();
                $('#stateName').html(member.address.stateName);
            }
        }
        if (!memberId) {
            $('#editMemberButton').show();
            $('#editAddressButton').show();
        }
    })
}
