<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    //Captura de variables
    if(isset($_GET['nombre'])){
        $nombre = $_GET['nombre'];
    }
    if(isset($_GET['id_nombre'])){
        $id_nombre = $_GET['id_nombre'];
    }

    $encontrado = existeNombre($mysqli, $nombre);

    if(count($encontrado) != 0 && $encontrado[0][0] != $id_nombre)
        echo True;

?>