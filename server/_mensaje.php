<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();
    session_start();

    $_usuario=getUsuario($mysqli, $_SESSION['cod_usuario']);

    if( $_usuario['tipo'] == "TUTOR" || $_usuario['tipo'] == "USUARIO" ){
        $mensaje = null;
        $multimedia = null;
        
        //Captura de variables
        if(isset($_POST['mensaje'])){
            $mensaje = $_REQUEST['mensaje'];
            if($mensaje == '')
                $mensaje = null;
        }
        if(isset($_FILES['multimedia'])){
            $multimedia = mysqli_real_escape_string($mysqli,$_FILES['multimedia']['name']);
        }
        if(isset($_POST['cod_tarea'])){
            $cod_tarea = $_REQUEST['cod_tarea'];
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

        sendMensaje($mysqli, $mensaje, $multimedia, $cod_tarea, $_usuario['cod_usuario']);
        
        header("Location: tarea.php?cod_tarea=$cod_tarea");
    }else {
        header("Location: index.php");
    }

    ?>