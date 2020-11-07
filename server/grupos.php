<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();

    session_start();
    if(isset($_SESSION['cod_usuario'])){
        $usuario=getUsuario($mysqli, $_SESSION['cod_usuario']);
        $grupos = getGrupos($mysqli, $_SESSION['cod_usuario']);
        echo $twig->render('grupos.html', ['usuario'=>$usuario, 'grupos'=>$grupos, 'titulo'=> "Grupos" ]);

    }
    else{
        echo $twig->render('login.html');
    }
   


?>