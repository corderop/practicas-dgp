<?php
    require_once "../../vendor/autoload.php";
    include('../funciones.php');

    //Conexion a la base de datos
    $mysqli=conectar_bd();

    $body = file_get_contents('php://input');
    $usuario = json_decode($body);

    $cod_usuario = $usuario->cod_usuario;

    $tareas = array();

    $tareas[] = getTareas($mysqli, $cod_usuario);

    for($i=0; i < $tareas.size(); i=+2){
        unset($tareas[$i]);
    }

    if (!empty($tareas)) {
        http_response_code(201);
        $respuesta = json_encode(array($tareas));
        echo $respuesta;
    } else {
        http_response_code(401);
        $respuesta["error"] = "no existen tareas para ese usuario";
        return ["estado" => 1, "usuario" => $respuesta];
    }
?>