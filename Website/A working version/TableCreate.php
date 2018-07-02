<?php
error_reporting (-1);
$bars = "\"vertical-align: middle;padding:10px 15px;font-size:20;\"";
$dataBars = "\"vertical-align: middle;padding:5px 12px;font-size:19;\"";
$dataBarsLarge = "\"vertical-align: middle;padding:5px 12px;font-size:19;min-width:210px;\"";
echo("<table id=\"bars\" border=1 style=\"margin-left:-25;\">
<tr style=\"background-color:rgb(230,230,230)\">
	<th style=$bars> Request Type</th>
	<th style=$bars>Yacht Club</th>
	<th style=$bars>Boat Type</th>
	<th style=$bars>Crew Type</th>
	<th style=$bars>Deadline</th>
	<th style=$bars>Phone</th>
	<th style=$bars>Email</th>
	<th style=$bars>Boat Number</th>
	<th style=$bars>Name</th>
	<th style=$bars>Specific Info</th>
</tr>");

	$type = '3';
	$url = 'http://crewforsail.ca/relay.php';
	$querry = "id=web&type=$type";

	$ch = curl_init( $url );
	curl_setopt( $ch, CURLOPT_POST, 1);
	curl_setopt( $ch, CURLOPT_POSTFIELDS, $querry);
	curl_setopt( $ch, CURLOPT_RETURNTRANSFER, 1);

	$response = curl_exec( $ch );
	$response = str_replace("<br />","", $response);
	$responseArray = explode(" ", $response);
	foreach($responseArray as $entry){
		str_replace("_", " ", $entry);
	}
	
	$count = 0;
	$colorCount = 0;
	if(count($responseArray) > 1){
		while ($count + 1 < count($responseArray) ){		
			if($colorCount++ % 2 == 1){
				echo("<tr class=\"row\" style=\"text-align: center;background-color:rgb(180,180,180)\" onmouseover=\"this.style.background='rgb(121,137,149)';\" onmouseout=\"this.style.background='rgb(180,180,180)';\" \" >");
			}else{
				echo("<tr class=\"row\" style=\"text-align: center;background-color:white\"  onmouseover=\"this.style.background='rgb(122,146,156)';\" onmouseout=\"this.style.background='white';\" \" >");
			}//R type
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ("<td  style=$dataBars >" . $responseArray[$count++] . "</td>");
			//YC
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ("<td style=$dataBars >" . $responseArray[$count++] . "</td>");
			//B Type
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ("<td style=$dataBars >" . $responseArray[$count++] . "</td>");
			//C Type
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ("<td style=$dataBars >" . $responseArray[$count++] . "</td>");
			//DL			
			$responseArray[$count] = str_replace("_", "<br />", $responseArray[$count]);
			$responseArray[$count] = str_replace("~", "  ", $responseArray[$count]);
			echo ("<td  style=$dataBarsLarge >" . $responseArray[$count++] . "</td>");
			//Phone
			$responseArray[$count] = str_replace("_", "-", $responseArray[$count]);
			echo ("<td style=$dataBars >" . $responseArray[$count++] . "</td>");
			//Email
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ("<td style=$dataBars >" . $responseArray[$count++] . "</td>");
			//B Number
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ("<td style=$dataBars >" . $responseArray[$count++] . "</td>");
			//Name
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ("<td style=$dataBars >" . $responseArray[$count++] . "</td>");
			//Specific Info
			$responseArray[$count] = str_replace("_", " ", $responseArray[$count]);
			echo ("<td style=$dataBars >" . $responseArray[$count++] . "</td>");
			
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
?>