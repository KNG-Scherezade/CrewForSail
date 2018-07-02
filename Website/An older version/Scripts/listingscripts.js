function randomNumber(){
	document.getElementById("listing").value = "" + (Math.random() * 10000000);
}

function trimThings(){
	var submitCheck = checkThings()
	if(!submitCheck)
		return false
	var submitCheck = fillAsNone();
	if(!submitCheck)
		return false
	
	var club = document.getElementById("club").value.trim();
	var crew = document.getElementById("crew").value.trim();
	var deadline = document.getElementById("deadlineInput").value.trim();
	var boat = document.getElementById("boat").value.trim();
	var boatName = document.getElementById("boatName").value.trim();
	var name = document.getElementById("name").value.trim();
	var phone = document.getElementById("phone").value.trim();
	var type = document.getElementById("type").value.trim();
	var listing = document.getElementById("listing").value.trim();
	var mail = document.getElementById("mail").value.trim();
	var specific = document.getElementById("specific").value.trim();
	
	var cl = (document.getElementById("club").value = replaceAll(club, " ", "_"));
	var cr  = (document.getElementById("crew").value = replaceAll(crew ," ", "_"));
	var di = (document.getElementById("deadlineInput").value = replaceAll(deadline ," ", "_"));
	var bn = (document.getElementById("boatName").value = replaceAll( boatName," ", "_"));
	var bo = (document.getElementById("boat").value = replaceAll(boat ," ", "_"));
	var na = (document.getElementById("name").value = replaceAll( name," ", "_"));
	var ph = (document.getElementById("phone").value = replaceAll( phone," ", "_"));
	var ty = (document.getElementById("type").value = replaceAll(type ," ", "_"));
	var li = (document.getElementById("listing").value = replaceAll(listing ," ", "_"));
	var mai = (document.getElementById("mail").value = replaceAll(mail," ", "_"));
	var sp = (document.getElementById("specific").value = replaceAll(specific," ", "_"));
	if (submitCheck){
		submitForm(cl,cr,di,bn,bo,na,ph,ty,li,mai,sp)
	}
}
function checkThings(){
	var club = document.getElementById("club").value;
	var crew = document.getElementById("crew").value;
	var boat = document.getElementById("boat").value;
	var boatName = document.getElementById("boatName").value;
	var name = document.getElementById("name").value;
	var phone = document.getElementById("phone").value;
	var mail = document.getElementById("mail").value;
	var specific = document.getElementById("specific").value;
	
	var items = [club,crew,boat,boatName,name,phone,mail,specific]
	var length = items.length
	for(var i = 0; i < length ; i++){
		if (typeof items[i] != "undefined"){
			var entry = items[i].split(" ")
			var entryLength = entry.length
			for (var j = 0; j < entryLength ; j++){
				if (entry[j].length > 15){
					console.log(entry)
					alert("Error on " + entry);
					return false;
				}
			}
		}
	}
	return true
}

function fillAsNone(){
	var phone = document.getElementById("phone").value.trim();
	if(phone == "" || phone == null ){
		document.getElementById("phone").value = "N/A";
	}
	
	var mail = document.getElementById("mail").value.trim();
	if(mail == "" || mail == null){
		document.getElementById("mail").value = "N/A";
	}
	
	var specific = document.getElementById("specific").value.trim();
	if(specific == "" || specific == null){
		document.getElementById("specific").value = "N/A";
	}
	
	var deadline = document.getElementById("deadlineInput").value.trim();
	if(deadline == "" || deadline == null || deadline.split(" ").length > 3){
		console.log("Time err")
		alert("Time not set.");
		return false
	}
	
	if (document.getElementById("type").value == 1 && !(document.getElementById("boatName").value == ""
	|| document.getElementById("boatName").value == null)&& !(document.getElementById("boat").value == ""
	|| document.getElementById("boat").value == null)){
		if (document.getElementById("name").value == ""	|| document.getElementById("name").value == null){
			document.getElementById("name").value = "N/A"	
		}
		alert("Data was Submitted");
		
	}
	
	if (document.getElementById("type").value == 2 && !(document.getElementById("name").value == ""
	|| document.getElementById("name").value == null)){
		alert("Data was Submitted");		
	}
	
	return true
}

function submitForm(cl,cr,di,bn,bo,na,ph,ty,li,mai,sp){
	var e = ""
	var n = null
	if (cl == e || cl == n || cr == e || cr == n || di == e || di == n || bn == e || bn == n || bo == e || bo == n || na == e || na == n || ph == e || ph == n || ty == e || ty == n ||
		li == e || li == n || mai == e || mai == n || sp == e || sp == n){
		alert("Some information was not entered")
		return
	}
	var data = document.getElementById("data");
	data.submit();
}



function replaceAll(string, toReplace, replacement){
	while(string.includes(toReplace)){
		string = string.replace(toReplace,replacement)
	}
	return string
}

function timer(){
	return -1;
}

function dropdownVisibility(tag, details){
	
	console.log(details.id)
	
	if(tag == "Other"){
		if(details.id == "club"){
			document.getElementById("club").style.display = "none"
			document.getElementById("club").name = "None"
			document.getElementById("club").id = "None"
			document.getElementById("clubOther").style.display = "inline"
			document.getElementById("clubOther").name = "club"
			document.getElementById("clubOther").id = "club"
		}
		else if(details.id == "crew"){
			document.getElementById("crew").style.display = "none"
			document.getElementById("crew").name = "None"
			document.getElementById("crew").id = "None"
			document.getElementById("crewOther").style.display = "inline"
			document.getElementById("crewOther").name = "crew"
			document.getElementById("crewOther").id = "crew"
		}
	}
}
