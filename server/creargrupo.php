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
        echo $twig->render('crearGrupo.html', ['usuario'=>$usuario, 'usuarios'=>$usuarios, 'titulo'=> "Crear un nuevo grupo" ]);

    }
    else{
        echo $twig->render('login.html');
    }
   


?>