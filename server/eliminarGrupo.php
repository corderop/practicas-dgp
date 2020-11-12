<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();
    session_start();

    $_usuario=getUsuario($mysqli, $_SESSION['cod_usuario']);

    if( $_usuario['tipo'] == "TUTOR" && isset($_POST['cod_grupo'])){
        $cod_grupo = $_POST['cod_grupo'];
        $grupo = getGrupo($mysqli, $cod_grupo);

        if($grupo['confecciona'] == $_usuario['cod_usuario']){
            deleteGrupo($mysqli, $cod_grupo);
            $_SESSION['previous_location'] = 'grupoEliminado';
            header("Location: grupos.php");
        }
        else{
            header("Location: grupos.php");
        }
    }else {
        header("Location: index.php");
    }

    ?>