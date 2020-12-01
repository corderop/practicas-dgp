<?php
    require_once "../../vendor/autoload.php";
    include('../funciones.php');

    //Conexion a la base de datos
    $mysqli=conectar_bd();

    $body = file_get_contents('php://input');
    $aux = json_decode($body);

    $fotos = mysqli_real_escape_string($mysqli,$aux->fotos);

    $usuarios=array();
    $usuarios=getUsuarios($mysqli);
    $err_login=true;

    foreach($usuarios as $usuario){
        if(checkLogin($mysqli,$usuario['nombre'],$fotos)){
            $err_login=false;
            $usuariofinal = getUsuario($mysqli, $usuario['cod_usuario']);
        }
    }

    if($err_login){
        http_response_code(402);
        $respuesta["error"] = "no existe uauario con ese nombre y/o contraseña";
        echo $respuesta;
    }else{
        http_response_code(201);
        $respuesta = json_encode(array("nombre" => $usuariofinal["nombre"], "cod_usuario" => $usuariofinal["cod_usuario"]));
        echo $respuesta;
    }
?>