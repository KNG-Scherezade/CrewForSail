function run(){
	
	var topBG = "#101849" 
	var bottomBG ="#BC9450"
	var lineColor = "#2D2951"
	var textColor = "#ffffff"
	var numColor = "#101849"
	var darkCover = "#4C4C4C"
	var mediumCover = "#A1A0A0"
	var lightCover = "#D1D0D0"
	
	var c = document.getElementById("deadline");
	var ctx = c.getContext("2d");
	W = 400
	H = 400
	c.width = W
	c.height= H
	var line = document.getElementById("deadlineInput").value.split(" ")
	var quantityPicked = line.length;
	
	ctx.fillStyle = topBG
	ctx.fillRect(0,0,W,H)
			
	var segmentY = H / 8
	var segmentX = W / 7
	
	ctx.fillStyle = bottomBG
	ctx.fillRect(0,segmentY * 2,W,H)
	
	var rows = ["Month Head", "Week Head", "1", "2", "3", "4", "5", "6"];
	var col = [" Sun", " Mon", " Tue", "Wed", " Thu", "  Fri", " Sat"] 
	var currentMonth = ""	
	ctx.strokeStyle = lineColor
	var	yStart = segmentY * 2
	function drawCol(element, index, array){
		var xAxis = segmentX * (index + 1);
		ctx.moveTo(xAxis, yStart);
		ctx.lineTo(xAxis, H);
		ctx.stroke();
		
		ctx.font = "27px Arial";
		ctx.fillStyle = textColor
		ctx.fillText(element, xAxis - segmentX, segmentY * 2 - 10);
	}
	
	function drawRow(element, index, array){
		yAxis = segmentY * (index + 1)
		ctx.moveTo(0, yAxis);
		ctx.lineTo(W, yAxis);
		ctx.stroke();
	}
	
	var forward = [370, 5, "->", true]
	var backward = [370, 5, "<-", false]
	var clear = [280, 5, "Clear",true]
	function createIcons(){
		ctx.globalAlpha = 0.7
		ctx.fillStyle = textColor
		if (forward[3]){
			ctx.fillText(forward[2], forward[0], forward[1] + 30)
		}
		else{
			ctx.fillText(backward[2], backward[0], backward[1] + 30 )
		}
		ctx.fillText(clear[2], clear[0], clear[1] + 30 )

	}
	
	var store = new Array(2);
	store[0] = -1
	function storeMonthData(){
		store[0] = numberDate.slice(0)
		store[1] = coordinates.slice(0)
	}
	function clearMonthData(){
		numberDate = new Array(6)
		coordinates = new Array(6)
		for (var count = 0; count < 6 ; count++){
			numberDate[count] = new Array(5)
			coordinates[count] = new Array(5)
		}
	}
	function clearAllData(){
		numberDate = new Array(6)
		coordinates = new Array(6)
		for (var count = 0; count < 6 ; count++){
			numberDate[count] = new Array(5)
			coordinates[count] = new Array(5)
		}
		store = new Array(2);
		store[0] = -1
		document.getElementById("deadlineInput").value = ""
		document.getElementById("deadlineTemp").value = ""
		rebuild()
	}
	function loadMonthData(arr1, arr2){
		if(typeof arr1 != undefined){
			temp1 = arr1
			temp2 = arr2
		}	
		clearMonthData()
		if(store[0] != -1){
			numberDate = store[0]
			coordinates = store[1]
		}
		store[0] = temp1
		store[1] = temp2
	}

	function setHead(){
		var date = new Date();
		if(backward[3]){
			currentMonth = date.getMonth() + 1	
			date.setMonth(currentMonth)
			date.setDate(1)
		}
		currentMonth = date.getMonth()
		fdate = date
		fdate += " "
		date = fdate.split(" ");
		var topline = date[0] + " " + date[1] + " " + date[2] + " "+ date[3]
		ctx.font = "30px Arial";
		ctx.fillStyle = textColor
		ctx.fillText(topline, 30, 35);	
	}
	
	var twoSegmentY = 2 * segmentY
	var numberDate = new Array(6)
	var coordinates = new Array(6)
	var count = 0;
	for (var count = 0; count < 6 ; count++){
		numberDate[count] = new Array(5)
		coordinates[count] = new Array(5)
	}
	function setNumbers(){		
		var date = new Date();
		var currentDay;
		if (backward[3]){
			currentDay = 1
			date.setDate(1);
		}
		else{
			currentDay = parseInt(date.getDate())
		}
		var firstDay = (new Date(date.getFullYear(), currentMonth, 1)).getDay()
		var maxDay = (new Date(date.getFullYear(), currentMonth, 33)).getDate()
		maxDay = maxDay == 5 ? 28 : maxDay == 2 ? 31: 30
		if (firstDay == 0){
			var row = 1
		}
		else{
			var row = 0
		}
		var dayCounter = 1;
		var colDay = firstDay
		var rowDay = row
		//fill this month
		while (dayCounter <= maxDay){
			if(typeof coordinates[rowDay][colDay] == 'undefined')
				coordinates[rowDay][colDay] = [segmentX * (colDay) + 25, segmentY * (rowDay + 1) + twoSegmentY - 25]		
				
			numberDate[rowDay][colDay] = dayCounter++;
			
			offsetX = coordinates[rowDay][colDay][0] - 24.5
			offsetY = coordinates[rowDay][colDay][1] - 24.5
			width = 56.5
			height = 49.5		
				
			ctx.font = "30px Arial";
			ctx.fillStyle = numColor
			if(numberDate[rowDay][colDay] < 10){
				ctx.fillText(numberDate[rowDay][colDay], segmentX * (colDay) + 20, -13 + segmentY * (rowDay + 1) + twoSegmentY);
			}
			else{
				ctx.fillText(numberDate[rowDay][colDay], segmentX * (colDay) + 10, -13 + segmentY * (rowDay + 1) + twoSegmentY);	
			}

			if(dayCounter - 1 < currentDay){
				ctx.globalAlpha=0.5;
				ctx.fillStyle = mediumCover
				ctx.fillRect(offsetX, offsetY, width,height)
				coordinates[rowDay][colDay].push(true)
				ctx.globalAlpha = 1.0				
			}
			else if(dayCounter - 1 == currentDay && !coordinates[rowDay][colDay][3]){//this day
				ctx.globalAlpha=0.5;
				ctx.fillStyle = darkCover
				ctx.fillRect(offsetX, offsetY, width,height)
				ctx.globalAlpha = 1.0
				coordinates[rowDay][colDay].push(false)
			}
			else if(coordinates[rowDay][colDay][3]){//Selected day
				ctx.globalAlpha=0.5;
				ctx.fillStyle = darkCover
				ctx.fillRect(offsetX, offsetY, width,height)
				ctx.globalAlpha=1.0;
			}
			colDay++;			
			if(colDay > 6){
				colDay = 0;
				rowDay++;
			} 
		}
		//fill beyond
		dayCounter = 1
		while (rowDay != 6){
			numberDate[rowDay][colDay] = dayCounter++;		
			ctx.font = "30px Arial";
			ctx.globalAlpha = 1.0
			ctx.fillStyle = numColor
			coordinates[rowDay][colDay] = [segmentX * (colDay) + 25, segmentY * (rowDay + 1) + twoSegmentY - 25, true]
			
			offsetX = coordinates[rowDay][colDay][0] - 24.5
			offsetY = coordinates[rowDay][colDay][1] - 24.5
			width = 56.5
			height = 49.5	
			
			ctx.fillText(numberDate[rowDay][colDay], segmentX * (colDay) + 20, segmentY * (rowDay + 1) + twoSegmentY - 13);
			colDay++;
			if(colDay > 6){
				colDay = 0;
				rowDay++;
			} 
			ctx.globalAlpha = 0.5
			ctx.fillStyle = lightCover
			ctx.fillRect(offsetX, offsetY, width,height)
		}
		//fill bellow
		var maxDay = (new Date(date.getFullYear(), currentMonth - 1, 33)).getDate()
		maxDay = maxDay == 5 ? 28 : maxDay == 2 ? 31: 30
		if (firstDay == 0 && row == 1){
			firstDay = 6
		}
		else{
			firstDay--;
		}
		while(firstDay != -1){
			coordinates[0][firstDay] = [segmentX * (firstDay) + 25, segmentY * (0 + 1) + twoSegmentY - 25, true]
			numberDate[0][firstDay] = maxDay--;
			numberDate[0][firstDay]		
			ctx.globalAlpha = 1.0
			ctx.font = "30px Arial";
			ctx.fillStyle = numColor
			ctx.fillText(numberDate[0][firstDay], segmentX * (firstDay) + 10, -13 + segmentY * (1) + twoSegmentY);			
			ctx.globalAlpha=0.5;
			ctx.fillStyle = lightCover
			ctx.fillRect(coordinates[0][firstDay][0] - 25, coordinates[0][firstDay][1] - 25, width,height)
			firstDay--
		}
	}
	

	
	//draw a grid of 1X1 
	//1X7
	//6X7
	rows.forEach(drawRow)
	col.forEach(drawCol)
	//Place Day ofM
	//Place weekdays
	//place numbers
	setHead()
	setNumbers()
	//create click events 
	//onclick put a transparent blue square over object
	//store the date
	//display a timer with OK, Cancel and a time selector
	//on OK store the date with the time
	//on CANCEL unset the square
	createIcons()
	//put string of data into an unchangable input field
	//this field is read on submit
	var displayOne = false;
	var displayTwo = false;
	var yearString = ""
	var dayOfMonth = ""
	var timeString = ""
	var hourSwitch = ""
	var hourOfDay = ""
	
	c.addEventListener('click',function(event){
			conditions(event)		
	});
	
	function conditions(event){
		var currentYear = parseInt((new Date()).getFullYear())
		//See setHead()
		currentMonth = parseInt(currentMonth)
		var currentDay = parseInt((new Date()).getDate())
		var currentHour = parseInt((new Date()).getHours())
		var currentMin = parseInt((new Date()).getMinutes())
		var rect = c.getBoundingClientRect();
		if(typeof event == "undefined"){
			console.log("err")
			var e = window.event;
			var x = e.clientX - rect.left; 
			var y = e.clientY - rect.top;
		}
		else{
			var x = event.clientX - rect.left; 
			var y = event.clientY - rect.top;
		}
		var xy = [x, y]
		var send = false;
		console.log(clear[1] - xy[1])
		console.log(clear[0] - xy[0])
		if(0 > (forward[0] - xy[0]) && 0 > (forward[1] - xy[1])&& -50 < (forward[0] - xy[0]) && -40 < (forward[1] - xy[1]) && forward[3]){
			backward[3] = true;
			forward[3] = false
			loadMonthData(numberDate, coordinates)
			
			console.log("forward")
			rebuild()
		}		
		else if(0 > (backward[0] - xy[0]) && 0 > (backward[1] - xy[1])&& -50 < (backward[0] - xy[0]) && -40 < (backward[1] - xy[1])){
			if(backward[3]){
				backward[3] = false;
				forward[3] = true
				loadMonthData(numberDate, coordinates)

				console.log("backward")
				rebuild()
			}					
		}
		else if(0 > (clear[0] - xy[0]) && 0 > (clear[1] - xy[1])&& -70 < (clear[0] - xy[0]) && -30 < (clear[1] - xy[1])){
			if(clear[3]){
				clearAllData()
				quantityPicked = 0;
				console.log("clear")
			}
		}
		if(!displayOne){
			//uses center coord instead of squares 
			breakpoint:
			for(var i = 0; i < 6; i++){
				for (var j = 0; j < 7; j++){
					if ( 25 > (coordinates[i][j][0] - xy[0]) && 25 > (coordinates[i][j][1] - xy[1])&& -25 < (coordinates[i][j][0] - xy[0]) && -25 < (coordinates[i][j][1] - xy[1])){
						offsetX = coordinates[i][j][0] - 24.5
						offsetY = coordinates[i][j][1] - 24
						width = 55.5
						height = 48
						if(coordinates[i][j][2]){}
						else if(coordinates[i][j][3]){ //pop
							coordinates[i][j][3] = false
							ctx.globalAlpha = 1.0;
							ctx.fillStyle = bottomBG
							ctx.fillRect(offsetX, offsetY, width,height)
							ctx.fillStyle = numColor
							ctx.fillText(numberDate[i][j], segmentX * (j) + 10, segmentY * (i + 1) + twoSegmentY - 13)
							if(currentDay == numberDate[i][j]){//this day
								ctx.globalAlpha= 0.5;
								ctx.fillStyle = darkCover
								ctx.fillRect(offsetX, offsetY, width,height)
							}
							ctx.globalAlpha= 1.0;
							ctx.fillStyle = numColor
							dayOfMonth = numberDate[i][j]
							yearString = currentYear + "/" + (currentMonth + 1) + "/" + numberDate[i][j]
							
							var data = ((document.getElementById("deadlineInput").value).trim()).split(" ")
							var dataLength = data.length
							document.getElementById("deadlineInput").value = ""
							for (var i = 0; i < dataLength ; i++){
								if(data[i].includes(yearString)){
									data[i] = ""
								}
								document.getElementById("deadlineInput").value = (document.getElementById("deadlineInput").value).trim() + " " + data[i] + " "
							}
							quantityPicked -= 1
							break breakpoint
						}
						else if (quantityPicked > 3){									
							alert("only 4 dates per order")
							break;
						}						
						else if(currentDay == numberDate[i][j]){//this day
							coordinates[i][j][3] = true
							ctx.globalAlpha=0.5;
							ctx.fillStyle = lightCover
							ctx.fillRect(offsetX, offsetY, width,height)
						
							dayOfMonth = numberDate[i][j]
							yearString = currentYear + "/" + (currentMonth + 1) + "/" + numberDate[i][j]
							timeString = yearString
							displayOne = true;
							displayTime(currentHour)
							hourOfDay = currentHour
							timeString = yearString + " "  + hourOfDay + " " + hourSwitch				
							break breakpoint
						}
						else if(!coordinates[i][j][3]){//future day
							coordinates[i][j][3] = true
							ctx.globalAlpha=0.5;
							ctx.fillStyle = lightCover
							ctx.fillRect(offsetX, offsetY, width,height)
							
							dayOfMonth = numberDate[i][j]
							yearString = currentYear + "/" + (currentMonth + 1) + "/" + numberDate[i][j]	
							displayOne = true;
							displayTime(currentHour)
							hourOfDay = currentHour
							timeString = yearString + " "  + hourOfDay + " " + hourSwitch
							break breakpoint
						}			
					} 
				}
			}
		}		
		else if(!displayTwo){
			breakpoint:	
			for(var i = 0; i < 4; i++){
				for (var j = 0; j < 3; j++){
					if ( 0 > (hourCoordinates[i][j][0] - xy[0]) && 0 > (hourCoordinates[i][j][1] - xy[1])&& -50 < (hourCoordinates[i][j][0] - xy[0]) && -40 < (hourCoordinates[i][j][1] - xy[1])){
							offsetX = hourCoordinates[i][j][0] + 1.0
							offsetY = hourCoordinates[i][j][1] + 1.0
							width = 48
							height = 38
						if(typeof hourCoordinates[i][j][3] == 'undefined'){
							scanClicked(xy)
							hourCoordinates[i][j].push(true)
							ctx.globalAlpha=0.5;
							ctx.fillStyle = darkCover
							ctx.fillRect(offsetX, offsetY, width, height)
							
							hourOfDay = hourCoordinates[i][j][2]
							timeString = yearString + " "  + hourOfDay + " " + hourSwitch
						}
						else if(hourCoordinates[i][j][3]){
							hourCoordinates[i][j].pop()
							ctx.globalAlpha= 1.0;
							ctx.fillStyle = bottomBG
							ctx.fillRect(offsetX, offsetY, width,height)
							ctx.fillStyle = numColor

							if(Math.floor(hourCoordinates[i][j][2] / 10) == 1)
								ctx.fillText(hourCoordinates[i][j][2], 35 - (30 * 0.333) + 50 * (i + 1), 200 + 40 * (j + 1))
							else
								ctx.fillText(hourCoordinates[i][j][2], 35 + 50 * (i + 1), 200 + 40 * (j + 1))
							
							hourOfDay = ""
							timeString = yearString 
						}
					}
				}
			}
			//Run check on AM PM
			for (var j = 0 ; j < 2; j++){
				if ( 0 > (hourCoordinates[i][j][0] - xy[0]) && 0 > (hourCoordinates[i][j][1] - xy[1])&& -50 < (hourCoordinates[i][j][0] - xy[0]) && -40 < (hourCoordinates[i][j][1] - xy[1])){
					if(typeof hourCoordinates[4][j][3] == 'undefined'){
						hourCoordinates[4][j].push(true)
						ctx.globalAlpha=0.5;
						ctx.fillStyle = darkCover
						ctx.fillRect(hourCoordinates[4][j][0], hourCoordinates[4][j][1], 55,38)
						
						hourSwitch = hourCoordinates[4][j][2]
						timeString =  yearString  + " " +  hourOfDay + " " + hourSwitch
						
						switch(j){
							case 0: j++; break;
							case 1: j--;break;
						}
						hourCoordinates[4][j].pop()
						ctx.globalAlpha= 1.0;
						ctx.fillStyle = bottomBG
						ctx.fillRect(hourCoordinates[4][j][0], hourCoordinates[4][j][1], 55,38)

						ctx.fillStyle = numColor
						ctx.fillText(hourCoordinates[4][j][2], 300, 250 + j * 50)						
					}			
				}
			}
			//run check on confirm boxes
			if (20 < xy[0] && 340 < xy[1] && 200 > xy[0] && 380 >  xy[1]){//next
				if(hourOfDay == ""){
					alert("Enter the hours")
				}
				else{
					timeString =  yearString  + " " +  hourOfDay + " " + hourSwitch
					displayTwo = true;
					displayMin(currentMin);		
					z = Math.floor(currentMin / 15)
					dayHourMinSwitch = minCoordinates[z][2]
					timeString =  yearString  + " "  + dayHourMinSwitch
				}
			}
			if (200 < xy[0] && 340 < xy[1] && 380 > xy[0] && 380 >  xy[1]){//cancel
				for (var i = 0 ; i < 6 ; i++){
					for (var j = 0; j < 7 ; j++){
						if (numberDate[i][j] == dayOfMonth){
							coordinates[i][j][3] = false;
						}
					}
				}
				//Fix this later
				displayOne = false;
				displayTwo = false;
				yearString = ""
				dayOfMonth = ""
				timeString = ""
				hourSwitch = ""
				hourOfDay = ""
				dayHourMinSwitch = ""
				displayOne = false;
				rebuild();
			}
		}
	
		else{
			for (var j = 0; j < 4; j++){
				if ( 0 > (minCoordinates[j][0] - xy[0]) && 0 > (minCoordinates[j][1] - xy[1])&& -140 < (minCoordinates[j][0] - xy[0]) && -50 < (minCoordinates[j][1] - xy[1])){
					if(typeof minCoordinates[j][3] == 'undefined'){
						for (var i = 0; i < 4; i++){
							if (typeof minCoordinates[i][3] != 'undefined'){
								minCoordinates[i].pop()
								ctx.globalAlpha= 1.0;
								ctx.fillStyle = textColor//White
								ctx.fillRect(minCoordinates[i][0], minCoordinates[i][1], 140,50)
								ctx.fillStyle = numColor
								ctx.globalAlpha= 1.0;
								ctx.fillText(minCoordinates[i][2],minCoordinates[i][0] + 5, minCoordinates[i][1] + 35 )
							}
						}
						minCoordinates[j].push(true)

						ctx.globalAlpha=0.5;
						ctx.fillStyle = darkCover
						ctx.fillRect(minCoordinates[j][0], minCoordinates[j][1], 140,50)
						
						dayHourMinSwitch = minCoordinates[j][2]
						timeString =  yearString  + " "  + dayHourMinSwitch
					}
					else{
						minCoordinates[j].pop()
						ctx.globalAlpha= 0.5;
						ctx.fillStyle = lightCover
						ctx.fillRect(minCoordinates[j][0], minCoordinates[j][1], 140,50)
						ctx.globalAlpha= 1.0;
						ctx.fillStyle = bottomBG
						ctx.fillText(minCoordinates[j][2], minCoordinates[j][0] + 5, minCoordinates[j][1] + 35)
						dayHourMinSwitch = ""
						timeString =  yearString  + " " + hourOfDay + " " + hourSwitch
					}							
				}
			}
			//run check on confirm boxes
			if (20 < xy[0] && 340 < xy[1] && 200 > xy[0] && 380 >  xy[1]){//confirm
				if(dayHourMinSwitch == ""){
					alert("Enter the Minutes")
				}
				else{
					timeString =  yearString  + "~"  + dayHourMinSwitch
					displayOne = false;
					displayTwo = false;
					send = true;
					rebuild();		
				}
			}
			if (200 < xy[0] && 340 < xy[1] && 380 > xy[0] && 380 >  xy[1]){//cancel
				//Fix this later
				timeString =  yearString  + " " + hourOfDay + " " + hourSwitch
				displayTwo = false;
				displayTime(currentHour);
			}			
		}		
		if (send){
			timeString = timeString.replace(" ","-")
			document.getElementById("deadlineInput").value += timeString + " "
			quantityPicked += 1;
			displayOne = false;
			displayTwo = false;
			yearString = ""
			dayOfMonth = ""
			timeString = ""
			hourSwitch = ""
			hourOfDay = ""
			dayHourMinSwitch = ""
		}		
		document.getElementById("deadlineTemp").value = timeString
	}
	
	// [0]X [1]Y [2]Hour [3]Push
	//uses squares instead of center coord
	var hourCoordinates = new Array(6)
	for (var i = 0 ; i < 6; i++){
		hourCoordinates[i] = new Array(3)			
	}
	function displayTime(currentHour){
		currentHourMod = currentHour  % 12
		if (currentHourMod == 0){
			currentHourMod = 12
		}
		currentHourDiv = currentHour  / 12
		ctx.globalAlpha= 1.0;
		ctx.fillStyle = bottomBG
		ctx.fillRect(20,140,360,240)
		ctx.globalAlpha= 1.0;
		ctx.fillStyle = lineColor;
		ctx.strokeRect(20,140,360,200)
		ctx.fillStyle = numColor;
		ctx.fillText("Set the hour: ", 20,180)		
		ctx.strokeRect(20,340,360,40)
		ctx.beginPath();
		ctx.moveTo(190,340)
		ctx.lineTo(190,380)
		ctx.fillText("Next", 40, 370)
		ctx.fillText("Cancel", 220, 370)
		ctx.stroke();
		var fontSize = 30
		ctx.font = fontSize + "px Arial";
		var hours = 1;
		for (var hoursY = 1; hoursY <= 3; hoursY++){
			for (var hoursX = 1; hoursX <= 4; hoursX++){
				var x = 20 + 50 * hoursX;
				var y = 170 + 40 * hoursY;
				ctx.globalAlpha= 1.0;
				ctx.strokeRect(x, y, 50, 40)
				if(hours == currentHourMod){
					ctx.globalAlpha= 0.5;
					ctx.fillStyle = mediumCover
					ctx.fillRect(x + 1, y + 1, 50, 40)
					ctx.fillStyle = numColor
				}
				hourCoordinates[hoursX - 1][hoursY - 1] = [x, y, hours]	
				ctx.globalAlpha= 1.0;
				if(Math.floor(hours / 10) == 1)
					ctx.fillText(hours++, 35 - (fontSize * 0.333) + 50 * hoursX, 200 + 40 * hoursY)
				else
					ctx.fillText(hours++, 35 + 50 * hoursX, 200 + 40 * hoursY)
			}
		}
		hourCoordinates[4][0] = [295, 220, "AM"]
		hourCoordinates[4][1] = [295, 270, "PM"]	
		ctx.globalAlpha= 0.5;
		ctx.fillStyle = darkCover
		if(currentHourDiv < 1){
			ctx.fillRect(hourCoordinates[4][0][0], hourCoordinates[4][0][1], 55,38)
			hourCoordinates[4][0].push(true);
			hourSwitch = hourCoordinates[4][0][2]
		} 
		else{
			ctx.fillRect(hourCoordinates[4][1][0], hourCoordinates[4][1][1], 55,38)
			hourCoordinates[4][1].push(true);
			hourSwitch = hourCoordinates[4][1][2]
		}
		ctx.globalAlpha = 1.0;
		ctx.fillStyle = numColor
		ctx.fillText(hourCoordinates[4][0][2], 300, 250)
		ctx.fillText(hourCoordinates[4][1][2], 300, 300)		
	}
	
	
	//function puts a complete timestring without date into minCoordinates[x][y][2]
	var minCoordinates = new Array(3)
	function displayMin(currentMin){
		currentMinMod = currentMin  % 15
		currentMinDiv = Math.floor(currentMin / 15) * 15
		ctx.globalAlpha= 1.0;
		ctx.fillStyle = textColor//White
		ctx.fillRect(20,110,360,280)
		ctx.fillStyle = lineColor
		ctx.strokeRect(20,110,360,240)
		ctx.strokeRect(20,350,360,40)
		ctx.beginPath();
		ctx.moveTo(190,350)
		ctx.lineTo(190,390)
		ctx.fillStyle = numColor
		ctx.fillText("Set the Minutes: ", 22,137)
		ctx.fillText("Confirm", 40, 380)
		ctx.fillText("Back", 220, 380)		
		ctx.stroke();
		var fontSize = 30
		ctx.font = fontSize + "px Arial";
		var mins = 0;	
		count = 0;
		for (var minsX = -2; minsX < 2; minsX++){
			var modulo = -80 * minsX.mod(2) + 140;
			var division = 70 * minsX.div(2) + 260;
			var value = (minsX + 2) * 15;
			if (value == 0){
				value = "00";
			}		
			value = hourOfDay + ":" + value + " " + hourSwitch;
			minCoordinates[count] = [modulo - 5, division - 35, value];
			ctx.fillStyle = numColor
			ctx.globalAlpha= 1.0;
			ctx.fillText(value, modulo, division)	
			if(value.includes(":" + currentMinDiv)){
				ctx.globalAlpha= 0.5;
				ctx.fillStyle = mediumCover;
				ctx.fillRect(minCoordinates[count][0], minCoordinates[count][1] , 140, 50);
				ctx.fillStyle = bottomBG;
				minCoordinates[count].push(true);
			}
			count++
		}
	}
	
	function rebuild(){
		ctx.beginPath()
		ctx.globalAlpha = 1.0
		ctx.clearRect(0,0,W,H)
		ctx.fillStyle = topBG
		ctx.fillRect(0,0,W,H)
		ctx.fillStyle = bottomBG
		ctx.fillRect(0,segmentY * 2, W, H)
		rows.forEach(drawRow)
		col.forEach(drawCol)
		setHead()
		setNumbers()
		createIcons()
	}
	
	function scanClicked(xy, arr){
		for(var i = 0; i < 4 ; i++){
			for (var j = 0; j < 3; j++){
				ctx.globalAlpha= 1.0;
				ctx.fillStyle = bottomBG
				ctx.fillRect(hourCoordinates[i][j][0]+1, hourCoordinates[i][j][1]+1.5, 48,37)
				ctx.fillStyle = numColor
				if(Math.floor(hourCoordinates[i][j][2] / 10) == 1)
					ctx.fillText(hourCoordinates[i][j][2], 35 - (30 * 0.333) + 50 * (i + 1), 200 + 40 * (j + 1))
				else
					ctx.fillText(hourCoordinates[i][j][2], 35 + 50 * (i + 1), 200 + 40 * (j + 1))
				if (hourCoordinates[i][j][3] !== undefined){
					hourCoordinates[i][j].pop()
				}
			}
		}
	}
	
	Number.prototype.mod = function(n) { return ((this < 0) ? -((this%n)+n)%n : +((this%n)+n)%n); } 
	Number.prototype.div = function(n) { return ((this < 0) ? Math.ceil(this / n) : (this == 0) ? 1 : Math.floor(this / n))}
	
	c.addEventListener('mousemove',function(event){
		var rect = c.getBoundingClientRect();
		var x = event.clientX - rect.left; 
		var y = event.clientY - rect.top;
		//console.log(x);
		//console.log(y)
	})
	

	

}
