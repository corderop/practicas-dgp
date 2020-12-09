<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    
    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);

    $mysqli=conectar_bd();
    session_start();

    $usuario = getUsuario($mysqli,$_SESSION['cod_usuario']); 

    if($usuario['tipo'] == "ADMIN"){    //Restringir la accion solo a administradores
        $userEliminar = $_REQUEST['cod_usuario'];
        deleteUsuario($mysqli, $userEliminar);

    }
    $_SESSION['previous_location'] = 'eliminarUsuario';
    header("Location: index.php");
?>