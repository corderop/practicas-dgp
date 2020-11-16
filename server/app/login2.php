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

    $usuario = mysqli_real_escape_string($mysqli,$nombre);

    if(checkLogin($mysqli,$nombre,$contrasena)){
        $usuario = getUsuario($mysqli, $usuario);
        $cod_usuario = $usuario['cod_usuario'];

        if ($cod_usuario != NULL) {
            http_response_code(201);
            $respuesta["nombre"] = "hola";
            $respuesta["cod_usuario"] = $usuario["cod_usuario"];
            return ["estado" => 1, "usuario" => $respuesta];
        } else {
            http_response_code(401);
            $respuesta["error"] = "no existe cod_usuario con ese usuario";
            return ["estado" => 1, "usuario" => $respuesta];
        }
    }else{
        http_response_code(401);
        $respuesta["error"] = "no existe uauario con ese nombre y/o contraseña";
        return ["estado" => 1, "usuario" => $respuesta];
    }
?>