<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    //Captura de variables
    if(isset($_POST['imagenes'])){
        $pass = $_REQUEST['imagenes'];
    }

    $usuarios=array();
    $usuarios=getUsuarios($mysqli);
    $err_login=true;

    foreach($usuarios as $usuario){
        if(checkLogin($mysqli,$usuario['nombre'],$pass)){
            echo("false");
            $err_login = false;
        }
    }

    if($err_login){
        echo("true");
    }


?>