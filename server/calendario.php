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
        
        if($usuario['tipo'] == "ADMIN"){
            header("Location: index.php");
        }
        if($usuario['tipo'] == "TUTOR"){
            $tareas = getTareasTutor($mysqli, $_SESSION['cod_usuario']);
            echo $twig->render('calendario.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=>"Calendario"]);
        }
        if($usuario['tipo'] == "USUARIO"){
            $tareas = getTareasTutor($mysqli, $_SESSION['cod_usuario']);
            echo $twig->render('calendario.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=>"Calendario"]);
        }
    }
    else{
        echo $twig->render('login.html');
    }
   


?>