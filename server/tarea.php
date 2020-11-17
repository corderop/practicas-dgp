<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    session_start();

    if(isset($_SESSION['cod_usuario'])){
        $usuario=getUsuario($mysqli, $_SESSION['cod_usuario']);

        if(isset($_GET['cod_tarea'])){
            $cod_tarea = $_GET['cod_tarea'];
            $tarea = getTarea($mysqli, $cod_tarea);
            $tipo = mime_content_type($tarea['multimedia']);

            // Cargar la tarea
            if($usuario['tipo'] == "TUTOR"){
                echo $twig->render('tarea.html', ['usuario'=>$usuario, 'tarea'=>$tarea, 'tipo'=>$tipo, 'titulo'=>$tarea['titulo']]);
            }
            if($usuario['tipo'] == "USUARIO"){
                echo $twig->render('tarea.html', ['usuario'=>$usuario, 'tarea'=>$tarea, 'tipo'=>$tipo, 'titulo'=>$tarea['titulo']]);
            }
        }
        else{
            // Si no se ha indicado ninguna tarea se redirecciona al inicio
            header("Location: index.php");
        }        
    }
    else{
        echo $twig->render('login.html');
    }
   


?>