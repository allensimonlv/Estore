
var xmlrequest;//
function createXMLHttpRequest() {
  
	if (window.ActiveXObject) {
		xmlrequest = new ActiveXObject("Microsoft.XMLHTTP");
	} else {
		xmlrequest = new XMLHttpRequest();
	}
}

function showUniTbl(){
	$("#uniDiv").show();
}

function closeUniTbl(){
	document.getElementById("uniDiv").style.display="none";
}

function showMyUni(o) {
	//
	//document.getElementById("university").value=o.innerHTML;
	$("#university").val(o.innerHTML);//
	//
	//document.getElementById("uniDiv").style.display="none";
	$("#uniDiv").css("display","none");//
	//document.getElementById("uuniversity").value=o.id;
	$("#uuniversity").val(o.id);
}