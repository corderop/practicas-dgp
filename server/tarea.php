<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    session_start();

    if(isset($_SESSION['cod_usuario'])){
        
        if(isset($_POST['tarea'])){
            $cod_tarea = $_POST['tarea'];
            $tarea = getTarea($mysqli, $cod_tarea);

            // Cargar la tarea
            if($usuario['tipo'] == "TUTOR"){
                
            }
            if($usuario['tipo'] == "USUARIO"){

            }
        }
        else{
            // Si no se ha indicado ninguna tarea se redirecciona al inicio
            header("Location: index.php");
        }
        
        $usuario=getUsuario($mysqli, $_SESSION['cod_usuario']);

        
    }
    else{
        echo $twig->render('login.html');
    }
   


?>