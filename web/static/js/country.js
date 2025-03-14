function getCountries(countryId, countrySelector) {
    translateKeys(['select-country'], (translations) => {
        let countryIdObj = $('#' + countrySelector);
        countryIdObj.html(`<option value="" selected>${translations["select-country"]}</option>`);
        doGetRequest('/service/locations/countries', (countries) => {
            countries.forEach(country => {
                countryIdObj.append(`<option value="${country.id}">${country.name}</option>`);
            });
            countryIdObj.val(countryId);
        });
    })
}

function getCountry(countryId, cityId, citySelector, stateId, stateSelector) {
    hideStateSelectBox();
    translateKeys(['select-city', 'select-state'], (translations) => {
        let cityIdObj = $('#' + citySelector);
        cityIdObj.html(`<option value="" selected>${translations["select-city"]}</option>`);

        let stateIdObj = $('#' + stateSelector);
        stateIdObj.html(`<option value="" selected>${translations["select-state"]}</option>`);

        doGetRequest('/service/locations/countries/' + countryId, (country) => {
            country.cities.forEach(city => {
                cityIdObj.append(`<option value="${city.id}">${city.name}</option>`);
            });
            cityIdObj.val(cityId);

            if (country.hasState) {
                country.states.forEach(state => {
                    stateIdObj.append(`<option value="${state.id}">${state.name}</option>`);
                });
                stateIdObj.val(stateId);
                showStateSelectBox();
            }
        });
    })
}

function hideStateSelectBox() {
    $('#state').hide();
    $('#city').removeClass("with-margin-top");
}

function showStateSelectBox() {
    $('#state').show();
    $('#city').addClass("with-margin-top");
}
