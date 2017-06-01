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