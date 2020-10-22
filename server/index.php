<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    session_start();
    if(isset($_SESSION['usuario'])){
        $usuario=$_SESSION['usuario'];
        echo $twig->render('index.html', ['usuario'=>$usuario]);
    }
    else{
        echo $twig->render('login.html');
    }
   


?>