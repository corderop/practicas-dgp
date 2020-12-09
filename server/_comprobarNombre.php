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

    $encontrado = existeNombre($mysqli, $nombre);
    echo $encontrado;

?>