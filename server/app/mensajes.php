<?php
    require_once "../../vendor/autoload.php";
    include('../funciones.php');

    //Conexion a la base de datos
    $mysqli=conectar_bd();

    $body = file_get_contents('php://input');
    $tarea = json_decode($body);

    $cod_tarea = $tarea->cod_tarea;

    $mensajes_aux = array();
    $mensajes_aux[] = getMensajesTarea($mysqli, $cod_tarea);

    $mensajes = $mensajes_aux[0];
    
    if (!empty($mensajes)) {
        $respuesta = "{";
        $contador = 0;
        foreach($mensajes as $mensaje){
            if($contador != 0 ){
                $respuesta = $respuesta . ",";
            }
            $respuesta = $respuesta . '"' . $contador . '"' . ":" . json_encode(array("cod_mensaje" => $mensaje["cod_mensaje"], "fecha" => $mensaje["fecha"], "contenido" => $mensaje["contenido"], "multimedia" => $mensaje["multimedia"], "contiene" => $mensaje["contiene"], "envia" => $mensaje["envia"], "leido" => $mensaje["leido"]));
            $contador = $contador + 1;
        }
        $respuesta = $respuesta . "}";
        http_response_code(201);
        echo $respuesta;
    } else {
        http_response_code(401);
        $respuesta["error"] = "no existen mensaje para esa tarea";
        return ["estado" => 1, "usuario" => $respuesta];
    }
    
?>