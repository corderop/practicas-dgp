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
            //Coger los datos que necesitemos 
            $usuarios = getUsuarios($mysqli);
            echo $twig->render('indexAdmin.html', ['usuario'=>$usuario, 'usuarios'=>$usuarios]);
        }
        if($usuario['tipo'] == "TUTOR"){
            
        }
        if($usuario['tipo'] == "USUARIO"){
            
        }
    }
    else{
        echo $twig->render('login.html');
    }
   


?>