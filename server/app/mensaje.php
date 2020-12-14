<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');
    
    //Conexion a la base de datos
    $mysqli=conectar_bd();
    
    $_POST = json_decode(file_get_contents('php://input'), true);

    $mensaje = null;
    $multimedia = null;
        
    // Captura de variables
    if (isset($_POST['mensaje'])){
    	mensaje = $_REQUEST['mensaje'];
    	if ($mensaje == '')
    		mensaje = null;
    }
    
    if(isset($_FILES['multimedia'])){
            $multimedia = mysqli_real_escape_string($mysqli,$_FILES['multimedia']['name']);
    }
    
    if(isset($_POST['cod_tarea'])){
        $cod_tarea = $_REQUEST['cod_tarea'];
    }
    
    if(isset($_POST['cod_usuario'])){
        $cod_usuario = $_REQUEST['cod_usuario'];
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

    if($_usuario['tipo'] == "USUARIO" ){
        modificarCorregidaTarea($mysqli, $cod_tarea, 0);
    }

    sendMensaje($mysqli, $mensaje, $multimedia, $cod_tarea, $cod_usuario);

?>
