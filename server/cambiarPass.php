<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    session_start();

    $passAntigua = "";
    $passNueva = "";
    $passNuevaConf = "";

    //Captura de variables
    if(isset($_POST['passNueva'])){
        $passNueva = mysqli_real_escape_string($mysqli,$_REQUEST['passNueva']);
    }
    
    if(isset($_POST['passNuevaConf'])){
        $passNuevaConf = mysqli_real_escape_string($mysqli,$_REQUEST['passNuevaConf']);
    }

    if(isset($_POST['passAntigua'])){
        $passAntigua = mysqli_real_escape_string($mysqli,$_REQUEST['passAntigua']);
    }

    $usuario = getUsuario($mysqli, $_SESSION['cod_usuario']);
    if(checkLogin($mysqli, $usuario['nombre'], $passAntigua)){
        if($passNueva == $passNuevaConf && $passNueva != ""){
        cambiarPass($mysqli, $_SESSION['cod_usuario'], $passNueva);
        $msg = "Contraseña cambiada con éxito";
        }
        else{
            $msg = "Error: las contraseñas no coinciden";
        }
    }
    else{
        $msg = "Error: la contraseña no es correcta";
    }
    

    echo $twig->render('perfil.html', ['usuario'=> $usuario, 'msg'=> $msg, 'titulo'=> "Perfil"]);


?>