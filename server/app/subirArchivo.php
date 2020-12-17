<?php

	include('../funciones.php');

	if(isset($_FILES['multimedia'])){
		$multimedia = mysqli_real_escape_string($mysqli,$_FILES['multimedia']['name']);
	}
	
	if($multimedia){
        $targetDir = "multimedia/";
        $fileName = date("Y-m-d-H-i-s") . $multimedia;
        $targetFilePath = $targetDir . $fileName;

        $imageFileType = strtolower(pathinfo($targetFilePath,PATHINFO_EXTENSION));
        
        http_response_code(201)
        echo http_response_code();
        return;

/*
        if(move_uploaded_file($_FILES["multimedia"]["tmp_name"], $targetFilePath)){
        	$multimedia = $targetFilePath;
        	echo $targetFilePath;
        	http_response_code(201);
        }
        else{
            http_response_code(400);
        }
*/
    }
    else {
    	http_response_code(400)
        echo http_response_code();
        return;
    }
    
?>
