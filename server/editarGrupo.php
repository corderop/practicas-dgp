<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    if(isset($_POST['cod_grupo'])){
        $cod_grupo = $_POST['cod_grupo'];
        $grupo_users = getGrupo($mysqli, $cod_grupo);
    }

    session_start();
    if(isset($_SESSION['cod_usuario'])){
        $usuario=getUsuario($mysqli, $_SESSION['cod_usuario']);
        $no_integrantes = getNoIntegrantesGrupo($mysqli, $cod_grupo);
        echo $twig->render('editarGrupo.html', ['usuario'=>$usuario, 'integrantes'=>$grupo_users['integrantes'], 'no_integrantes'=>$no_integrantes, 'titulo'=> "Crear un nuevo grupo", 'grupo'=>$grupo_users]);

    }
    else{
        echo $twig->render('login.html');
    }
?>