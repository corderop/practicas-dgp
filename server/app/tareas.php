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

    $respuesta = json_encode(array(array("cod_tarea" => $tarea["cod_tarea"], "cod_facilitador" => $tarea["crea"], "titulo" => $tarea["titulo"], "descripcion" => $tarea["descripcion"], "fecha_limite" => $tarea["fecha_limite"], "objetivo" => $tarea["objetivo"], "multimedia" => $tarea["multimedia"], "realizada" => $tarea["realizada"], "calificacion" => $tarea["calificacion"])));

    echo $respuesta;
    /*
    if (!empty($tareas)) {
        $respuesta = "{";
        $contador = 0;
        foreach($tareas as $tarea){
            if($contador != 0 ){
                $respuesta = $respuesta . ",";
            }
            $respuesta = $respuesta . '"' . $contador . '"' . ":" . json_encode(array("cod_tarea" => $tarea["cod_tarea"], "cod_facilitador" => $tarea["crea"], "titulo" => $tarea["titulo"], "descripcion" => $tarea["descripcion"], "fecha_limite" => $tarea["fecha_limite"], "objetivo" => $tarea["objetivo"], "multimedia" => $tarea["multimedia"], "realizada" => $tarea["realizada"], "calificacion" => $tarea["calificacion"]));
            $contador = $contador + 1;
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