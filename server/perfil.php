<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    session_start();

    if(isset($_SESSION['cod_usuario'])){
        
        $usuario = getUsuario($mysqli, $_SESSION['cod_usuario']);
    }
   

    if($usuario['tipo'] == "ADMIN"){
        //Perfil del administrador
        echo $twig->render('perfilAdminResponsive.html', ['usuario'=> $usuario]);
    }

    if($usuario['tipo'] == "TUTOR"){
        //Perfil del tutor
        echo $twig->render('perfil.html', ['usuario'=>$usuario]);
    }

    if($usuario['tipo'] == "USUARIO"){
        //Perfil del usuario
        echo $twig->render('perfil.html', ['usuario'=>$usuario]);
    }



?>