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

        if ($cod_usuario != NULL) {
            http_response_code(200);
            $respuesta["nombre"] = $usuarioBD["nombre"];
            $respuesta["claveApi"] = $usuarioBD["claveApi"];
            return ["estado" => 1, "usuario" => $respuesta];
        } else {
            throw new ExcepcionApi(self::ESTADO_FALLA_DESCONOCIDA, "Ha ocurrido un error");
        }
    }else{
        throw new ExcepcionApi(self::ESTADO_PARAMETROS_INCORRECTOS, utf8_encode("Correo o contraseña inválidos"));
    }


?>