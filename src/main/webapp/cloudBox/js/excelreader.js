function ReadExcel() {
	var tempStr = "";
	var filePath = document.all.upfile.value;
	var oXL = new ActiveXObject("Excel.application");
	var oWB = oXL.Workbooks.open(filePath);
	oWB.worksheets(1).select();
	var oSheet = oWB.ActiveSheet;
	try {
		for ( var i = 2; i < 46; i++) {
			if (oSheet.Cells(i, 2).value == "null"|| oSheet.Cells(i, 3).value == "null")
				break;
			var a = oSheet.Cells(i, 2).value.toString() == "undefined" ? "": oSheet.Cells(i, 2).value;
			tempStr += ("  " + oSheet.Cells(i, 2).value + "  "
					+ oSheet.Cells(i, 3).value + "  "
					+ oSheet.Cells(i, 4).value + "  "
					+ oSheet.Cells(i, 5).value + "  "
					+ oSheet.Cells(i, 6).value + "\n");
		}
	} catch (e) {
		document.all.txtArea.value = tempStr;
	}
	document.all.txtArea.value = tempStr;
	oXL.Quit();
	CollectGarbage();
}