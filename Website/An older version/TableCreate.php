<?php
error_reporting (0);
echo("<table id=\"bars\">
<tr style=\"background-color:rgb(230,230,230)\">
	<th id=\"bars\"> Request Type</th>
	<th id=\"bars\">Yacht Club</th>
	<th id=\"bars\">Boat Type</th>
	<th id=\"bars\">Crew Type</th>
	<th id=\"largeBars\">Deadline</th>
	<th id=\"bars\">Phone</th>
	<th id=\"bars\">Email</th>
	<th id=\"bars\">Boat Number</th>
	<th id=\"bars\">Name</th>
	<th id =\"largeBars\">Specific Info</th>
</tr>");



	$pythonCompiler = "C:\\Users\\Scherezade\\AppData\\Local\\Programs\\Python\\Python35-32\\python.exe";
	$script = "C:\\xampp\\htdocs\\DataBass.py";
	$type = "3";
	$result = exec("$pythonCompiler $script " . escapeshellarg("blank_text $type"));		
	
	$result = str_replace("<br />","", $result);
	
	$responseArray = explode(" ", $result);
	foreach($responseArray as $entry){
		str_replace("_", " ", $entry);
	}
	$count = 0;
	$colorCount = 0;
	if(count($responseArray) > 1){
		while ($count + 1 < count($responseArray) ){		
			if($colorCount++ % 2 == 1){
				echo("<tr style=\"background-color:rgb(180,180,180)\">");
			}else{
				echo("<tr style=\"background-color:white\">");
			}//R type
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ('<td>' . $responseArray[$count++] . '</td>');
			//YC
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ('<td>' . $responseArray[$count++] . '</td>');
			//B Type
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ('<td>' . $responseArray[$count++] . '</td>');
			//C Type
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ('<td>' . $responseArray[$count++] . '</td>');
			//DL			
			$responseArray[$count] = str_replace("_", "<br />", $responseArray[$count]);
			$responseArray[$count] = str_replace("~", " ", $responseArray[$count]);
			echo ('<td>' . $responseArray[$count++] . '</td>');
			//Phone
			$responseArray[$count] = str_replace("_", "-", $responseArray[$count]);
			echo ('<td>' . $responseArray[$count++] . '</td>');
			//Email
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ('<td>' . $responseArray[$count++] . '</td>');
			//B Number
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ('<td>' . $responseArray[$count++] . '</td>');
			//Name
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ('<td>' . $responseArray[$count++] . '</td>');
			//Specific Info
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ('<td>' . $responseArray[$count++] . '</td>');
			
			echo ("</tr>");
		}
	}
	else{
		echo("<tr>");
		echo("<td style=\"background-color:rgb()\" id=\"bars\">No Entries Today</td>");
		for($i = 0; $i < 9; $i++){
			echo("<td style=\"background-color:rgb()\" id=\"bars\">---</td>");
		}
		echo("</tr>");
	}
	
	
	echo("</table>");
	
	/**OLD CURL METHOD*/
	// $type = '3';
	// $url = 'http://70.83.102.219:9002/relay.php';
	// $querry = "id=web&type=$type";
	
	// $ch = curl_init( $url );
	// curl_setopt( $ch, CURLOPT_POST, 1);
	// curl_setopt( $ch, CURLOPT_POSTFIELDS, $querry);
	// curl_setopt( $ch, CURLOPT_RETURNTRANSFER, 1);

	// $response = curl_exec( $ch );
	// $response = str_replace("<br />","", $response);
	// $responseArray = explode(" ", $response);
	// foreach($responseArray as $entry){
		// str_replace("_", " ", $entry);
	// }
	
?>


