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

        // Si viene de crear un grupo se especifica que se ha creado correctamente
        if (isset($_SESSION['previous_location']) && $_SESSION['previous_location'] == 'aniadirgrupo') {
            echo $twig->render('grupos.html', ['usuario'=>$usuario, 'grupos'=>$grupos, 'titulo'=> "Grupos",'msg'=>"Grupo creado correctamente"]);
            $_SESSION['previous_location'] = '';
        } 
        elseif (isset($_SESSION['previous_location']) && $_SESSION['previous_location'] == 'grupoEliminado') {
            echo $twig->render('grupos.html', ['usuario'=>$usuario, 'grupos'=>$grupos, 'titulo'=> "Grupos", 'msg'=>"Grupo eliminado correctamente"]);
            $_SESSION['previous_location'] = '';
        }
        elseif (isset($_SESSION['previous_location']) && $_SESSION['previous_location'] == 'modificargrupo') {
            echo $twig->render('grupos.html', ['usuario'=>$usuario, 'grupos'=>$grupos, 'titulo'=> "Grupos", 'msg'=>"Grupo modificado correctamente"]);
            $_SESSION['previous_location'] = '';
        }
        else {
            echo $twig->render('grupos.html', ['usuario'=>$usuario, 'grupos'=>$grupos, 'titulo'=> "Grupos" ]);
        }

    }
    else{
        echo $twig->render('login.html');
    }
   


?>