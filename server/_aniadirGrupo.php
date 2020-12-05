<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();
    session_start();

    $_usuario=getUsuario($mysqli, $_SESSION['cod_usuario']);

    if( $_usuario['tipo'] == "TUTOR" ){
        $nombre= null;
        $usuarios= null;
        
        //Captura de variables
        if(isset($_POST['nombre-grupo'])){
            $nombre = $_REQUEST['nombre-grupo'];
        }
        if(isset($_POST['usuarios'])){
            $usuarios = $_REQUEST['usuarios'];
        }

        if($nombre && $usuarios){
            nuevoGrupo($mysqli, $nombre, $_usuario['cod_usuario'],$usuarios);
            $_SESSION['previous_location'] = 'aniadirgrupo';
            header("Location: grupos.php");
        }
        // No añado else ya que los datos introducidos son comprobados por javascript
    }else {
        header("Location: index.php");
    }

    ?>