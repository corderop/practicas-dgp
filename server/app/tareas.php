<?php
    require_once "../../vendor/autoload.php";
    include('../funciones.php');

    //Conexion a la base de datos
    $mysqli=conectar_bd();

    $body = file_get_contents('php://input');
    $usuario = json_decode($body);

    $cod_usuario = $usuario->cod_usuario;

    $tareas_aux = array();
    $tareas_aux[] = getTareas($mysqli, $cod_usuario);

    $tareas = $tareas_aux[0];


    foreach($tareas as $tarea){
        echo ($tarea);
        echo "\nOtra tarea\n";
    }
    
/*
    if (!empty($tareas)) {
        $respuesta = "{";
        for($i=0; $i<count($tareas); $i++){
            $respuesta = $respuesta . '"' . $i . '":'. json_encode($tareas[$i]);
        }
        $respuesta = $respuesta . "}";
        http_response_code(201);
        echo $respuesta;
    } else {
        http_response_code(401);
        $respuesta["error"] = "no existen tareas para ese usuario";
        return ["estado" => 1, "usuario" => $respuesta];
    }
    */
?>