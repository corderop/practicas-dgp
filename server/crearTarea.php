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
        $usuarios = getUsuarios($mysqli);
        $grupos = getGrupos($mysqli, $usuario['cod_usuario']); 
        echo $twig->render('aniadirTarea.html', ['usuario'=>$usuario, 'usuarios'=>$usuarios, 'grupos'=>$grupos, 'titulo'=> "Crear una nueva tarea" ]);
    }
    else{
        echo $twig->render('login.html');
    }
?>