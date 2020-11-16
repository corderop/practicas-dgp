<?php
    require_once "../../vendor/autoload.php";
    include('../funciones.php');

    //Conexion a la base de datos
    $mysqli=conectar_bd();

    //Captura de variables
    $respuesta = array();

    $body = file_get_contents('php://input');
    $usuario = json_decode($body);

    $nombre = $usuario->nombre;
    $contrasena = $usuario->contrasena;
    $tipo = "usuario";



    if(checkLogin($mysqli,$nombre,$contrasena)){
        $usuario = getUsuario($mysqli, $usuario);
        $cod_usuario = $usuario['cod_usuario'];
    }else{
        $msg="Error de login";
        echo $twig->render('login.html',['msg'=>$msg, 'titulo'=> "Home"]);
    }


?>