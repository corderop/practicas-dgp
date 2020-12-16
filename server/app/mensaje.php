<?php
    include('../funciones.php');
    
    //Conexion a la base de datos
    $mysqli=conectar_bd();
    
    $body = file_get_contents('php://input');
    $contenido = json_decode($body);
    
    //$_POST = json_decode(file_get_contents('php://input'), true);

    $mensaje = null;
    $multimedia = null;
        
    // Captura de variables
    if (isset($contenido->mensaje)){
    	$mensaje = $contenido->mensaje;
    	if ($mensaje == '')
    		$mensaje = null;
    }
    
    if(isset($_FILES['multimedia'])){
            $multimedia = mysqli_real_escape_string($mysqli,$_FILES['multimedia']['name']);
    }
    
    if(isset($contenido->cod_tarea)){
        $cod_tarea = $contenido->cod_tarea;
    }
    
    if(isset($contenido->cod_usuario)){
        $cod_usuario = $contenido->cod_usuario;
    }

    // Toma el archivo si lo ha anadido
    if($multimedia){
        $targetDir = "multimedia/";
        $fileName = date("Y-m-d-H-i-s") . $multimedia;
        $targetFilePath = $targetDir . $fileName;

        $imageFileType = strtolower(pathinfo($targetFilePath,PATHINFO_EXTENSION));

        if(move_uploaded_file($_FILES["multimedia"]["tmp_name"], $targetFilePath)){
            //echo "Bien";
        }
        else{
            echo "Hay algun error";
        }
        $multimedia = $targetFilePath;
    }
    else{
        $multimedia = null;
    }

    sendMensaje($mysqli, $mensaje, $multimedia, $cod_tarea, $cod_usuario);
    http_response_code(200);
    $respuesta = json_encode(array("respuesta" => "OK"));
	echo $respuesta;
?>
