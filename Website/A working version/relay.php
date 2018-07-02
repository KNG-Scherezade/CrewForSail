<?php
$pythonCompiler = "python /usr/bin/python";
$script = "/home4/discoun7/public_html/DataBass.py";


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
	
	$spacedPost = ("$id $type $club $boatType $crew $deadline $phone $mail $boatName $name $delete $listing $specific");
	
	ob_start();
	echo $spacedPost . "<br><br>";
	$result = shell_exec("python $script " . escapeshellarg($spacedPost));		
	echo($result);

	echo "<br><br>Data was written";

	header('Location: index.php');
	ob_end_flush();

}

else if ($_POST['type'] == 3){
	//read the file and echo the data
	$type = $_POST["type"];
	$result = shell_exec("python $script " . escapeshellarg("blank_text $type"));		
	echo($result);
}
else if($_POST['type'] == 5){
	echo("Connected");
} 
else{
	echo "Error";
}
?>