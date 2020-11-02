<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();
    session_start();

    $usuario = getUsuario($mysqli, $_SESSION['cod_usuario']);
    if($usuario['tipo'] == "ADMIN"){
        echo $twig->render('aniadirUsuario.html', ['usuario'=> $usuario, 'titulo'=>"Añadir usuario"]);
    }
    else{
        header("Location: index.php");
    }
    
    ?>