<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    session_start();

    //Captura de variables
    if(isset($_POST['pass'])){
        $pass1 = mysqli_real_escape_string($mysqli,$_REQUEST['pass']);
    }
    
    if(isset($_POST['pass2'])){
        $pass2 = mysqli_real_escape_string($mysqli,$_REQUEST['pass2']);
    }

    if($pass1 == $pass2){
        cambiarPass($mysqli, $_SESSION['cod_usuario'], $pass1);
        $msg = "Contraseña cambiada con éxito";
    }
    else{
        $msg = "Error al cambiar contraseña";
    }

    $usuario = getUsuario($mysqli, $_SESSION['cod_usuario']);
    

    echo $twig->render('perfilAdminResponsive.html', ['usuario'=> $usuario, 'msg'=> $msg]);


?>