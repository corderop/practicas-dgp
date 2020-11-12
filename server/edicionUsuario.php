<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();
    session_start();
    
    

    $_usuario = getUsuario($mysqli, $_SESSION['cod_usuario']);
    if($_usuario['tipo'] != "ADMIN"){  //Solo el administrador puede entrar
            header("Location: index.php");
    }
    
    if(isset($_REQUEST['cod_usuario'])){
        $usuario = getUsuario($mysqli, $_REQUEST['cod_usuario']);
    }
    else{
        header("Location: index.php");
    }

    //Captar los elementos que queremos cambiar
    $nombre = $usuario['nombre'];
    $pass = $usuario['pass'];
    $repetirPass = $usuario['pass'];
    $tipo = $usuario['tipo'];
    $avatar = $usuario['avatar'];

    if(isset($_POST['usuario'])){
        $nombre = mysqli_real_escape_string($mysqli,$_REQUEST['usuario']);
    }
    if(isset($_POST['tipo'])){
        $tipo = strtoupper($_POST['tipo']);
        $tipo = mysqli_real_escape_string($mysqli, $tipo);
    }
    if($_FILES['avatar']['name'] != ""){
        
        //Subir la foto y borrar la anterior
        $avatar = cargarFoto();
        if (file_exists($usuario['avatar']) && $usuario['avatar'] != "img/user.jpg") {
            unlink($usuario['avatar']);
            //echo 'File '.$usuario['avatar'].' has been deleted';
          } else {
            //echo 'Could not delete '.$usuario['avatar'].', file does not exist';
          }
        
    }

    
    
    if(isset($_POST['pass']) && isset($_POST['repetirPass']) && $_POST['pass'] != ""){
        $pass = $_REQUEST['pass'];
        if($_POST['pass'] == $_POST['repetirPass']){
            actualizarUsuario($mysqli, $usuario, $nombre, $pass, $tipo, $avatar);
            $usuario = getUsuario($mysqli, $usuario['cod_usuario']);
            echo $twig->render('editarUsuario.html', ['user' => $usuario, 'usuario'=> $_usuario, 'titulo'=>"Editar usuario"]);
        }
        else{
        $msg="Error: las contraseñas no coinciden";
        $usuario = getUsuario($mysqli, $usuario['cod_usuario']);
        echo $twig->render('editarUsuario.html', ['user' => $usuario, 'usuario'=> $_usuario, 'titulo'=>"Editar usuario", 'msg'=> $msg]);
        }
    }
    else{
        actualizarUsuario($mysqli, $usuario, $nombre, $pass, $tipo, $avatar);
        $usuario = getUsuario($mysqli, $usuario['cod_usuario']);
        echo $twig->render('editarUsuario.html', ['user' => $usuario, 'usuario'=> $_usuario, 'titulo'=>"Editar usuario"]);
    }
?>