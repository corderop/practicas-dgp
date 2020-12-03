<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();
    session_start();

    $_usuario=getUsuario($mysqli, $_SESSION['cod_usuario']);

    if( $_usuario['tipo'] == "ADMIN" ){
        $usuario="";
        $pass="";
        $repetirPass="";
        $tipo="";
        $avatar="";
        
        //Captura de variables
        if(isset($_POST['usuario'])){
            $usuario = mysqli_real_escape_string($mysqli,$_REQUEST['usuario']);
        }
        if(isset($_POST['pass'])){
            $pass = $_REQUEST['pass'];
        }
        if(isset($_POST['repetirPass'])){
            $repetirPass = $_REQUEST['repetirPass'];
        }
        if(isset($_POST['tipo'])){
            $tipo = strtoupper($_POST['tipo']);
            $tipo = mysqli_real_escape_string($mysqli, $tipo);
        }
        if(isset($_POST['avatar'])){
            $avatar = mysqli_real_escape_string($mysqli,$_FILES['avatar']['name']);
        }
        
        if($tipo == "USUARIO" || $pass == $repetirPass){
            aniadirUsuario($mysqli,$usuario,$pass,$tipo, $avatar); 
            header("Location: index.php");
        }
        else{
            $msg="Error: las contraseñas no coinciden";
            echo $twig->render('aniadirUsuario.html', ['usuario'=> $usuario, 'titulo'=>"Añadir usuario", 'msg'=> $msg]);
        }
    }else {
        header("Location: index.php");
    }

    ?>