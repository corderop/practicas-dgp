<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    //Captura de variables
    if(isset($_POST['fotos'])){
        $fotos = mysqli_real_escape_string($mysqli,$_REQUEST['fotos']);
    }

    $usuarios=array();
    $usuarios=getUsuarios($mysqli);
    $err_login=true;

    foreach($usuarios as $usuario){
        if(checkLogin($mysqli,$usuario['nombre'],$fotos)){
            $err_login=false;
            session_start();
            $usuario = getUsuario($mysqli, $usuario);
            $cod_usuario = $usuario['cod_usuario'];
            $_SESSION['cod_usuario'] = $cod_usuario;
            header("Location: index.php");
        }
    }

    if($err_login){
        $msg="Error de login";
        echo $twig->render('login.html',['msg'=>$msg, 'titulo'=> "Login"]);
    }


?>