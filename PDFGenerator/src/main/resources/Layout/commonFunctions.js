var months = [
    'January', 'February', 'March', 'April', 'May',
    'June', 'July', 'August', 'September',
    'October', 'November', 'December'
    ];

function getFormattedDate(date) {
	var dateArray = date.split('-');
	return dateArray[2] + "." + dateArray[1] + "." + dateArray[0].substr(2,2);
}

function getFormattedFullDate(date) {
	var dateArray = date.split('-');
	return dateArray[2] + "." + dateArray[1] + "." + dateArray[0];
}

function getFormattedAmount(amount) {
	return amount + ",00";
}

function formatMoney(value,decimalPlaces, symbol, thousand, decimal) {
decimalPlaces = !isNaN(decimalPlaces = Math.abs(decimalPlaces)) ? decimalPlaces : 2;
symbol = symbol !== undefined ? symbol : "$";
thousand = thousand || ",";
decimal = decimal || ".";
var number = value, 
   negative = number < 0 ? "-" : "",
   i = parseInt(number = Math.abs(+number || 0).toFixed(decimalPlaces), 10) + "",
   j = (j = i.length) > 3 ? j % 3 : 0;
return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (decimalPlaces ? decimal + Math.abs(number - i).toFixed(decimalPlaces).slice(2) : "");
};

function monthNumToName(date) {
    var d = parseInt(date.substr(5,2));
    return months[d-1];
}

function getYear(date) {
    var year = parseInt(date.substr(0,4));
    return year;
}