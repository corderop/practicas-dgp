<?php
    require_once "../../vendor/autoload.php";
    include('../funciones.php');

    //Conexion a la base de datos
    $mysqli=conectar_bd();

    $body = file_get_contents('php://input');
    $usuario = json_decode($body);

    $fotos = $usuario->fotos;

    $usuarios=array();
    $usuarios=getUsuarios($mysqli);
    $err_login=true;

    foreach($usuarios as $usuario){
        if(checkLogin($mysqli,$usuario['nombre'],$fotos)){
            $err_login=false;
            $usuario = getUsuario($mysqli, $usuario);
        }
    }

    if($err_login){
        http_response_code(402);
        $respuesta["error"] = "no existe uauario con ese nombre y/o contraseña";
        echo $respuesta;
    }else{
        http_response_code(201);
        $respuesta = json_encode(array("nombre" => $usuario["nombre"], "cod_usuario" => $usuario["cod_usuario"]));
        echo $respuesta;
    }
?>