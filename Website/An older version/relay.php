<?php

if ($_POST['type'] == 1 || $_POST['type'] == 2){
	
	$id = $_POST["ID"];
	$type = $_POST["type"];
	$club = $_POST["club"];
	$boatType = $_POST["boat"];
	$crew = $_POST["crew"];
	$deadline = $_POST["deadline"];
	$phone = $_POST["phone"];
	$mail = $_POST["mail"];
	$boatName = $_POST["boatName"];
	$name = $_POST["name"];
	$delete = $_POST["delete"];
	$listing = $_POST["listing"];
	$specific = $_POST["specific"];
	
	$pythonCompiler = "C:\\Users\\Scherezade\\AppData\\Local\\Programs\\Python\\Python35-32\\python.exe";
	$script = "C:\\xampp\\htdocs\\DataBass.py";

	$spacedPost = ("$id $type $club $boatType $crew $deadline $phone $mail $boatName $name $delete $listing $specific");
	echo $spacedPost . "<br>";
	$result = shell_exec("$pythonCompiler $script " . escapeshellarg($spacedPost));		
	echo($result);
	
	echo "Data was written";
	Sleep(1);
	//header( 'Location: default.php' );

}

else if ($_POST['type'] == 3){
	//read the file and echo the data
	$pythonCompiler = "C:\\Users\\Scherezade\\AppData\\Local\\Programs\\Python\\Python35-32\\python.exe";
	$script = "C:\\xampp\\htdocs\\DataBass.py";
	$type = $_POST["type"];
	$result = shell_exec("$pythonCompiler $script " . escapeshellarg("blank_text $type"));		
	echo($result);
}
else if($_POST['type'] == 5){
	echo("Connected");
} 
else{
	echo "Error";
}
?>


