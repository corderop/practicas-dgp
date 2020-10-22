<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    //Captura de variables
    if(isset($_POST['usuario'])){
        $usuario = mysqli_real_escape_string($mysqli,$_REQUEST['usuario']);
    }
    if(isset($_POST['pass'])){
        $pass = $_REQUEST['pass'];
    }

    if(checkLogin($mysqli,$usuario,$pass)){
        session_start();
        $_SESSION['usuario'] = $usuario;
        header("Location: index.php");
    }
    else{
        $msg="Error de login";
        echo $twig->render('login.html',['msg'=>$msg]);
    }

    


?>