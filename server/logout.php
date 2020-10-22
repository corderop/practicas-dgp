<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    
    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);


    session_start();
    session_destroy();

    header("Location: index.php");
?>