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
        
        if($usuario['tipo'] == "ADMIN"){
            header("Location: index.php");
        }
        if($usuario['tipo'] == "TUTOR"){
            $tareas = getTareasTutor($mysqli, $_SESSION['cod_usuario']);
            echo $twig->render('calendario.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=>"Calendario"]);
        }
        if($usuario['tipo'] == "USUARIO"){
            $semana = 0;
            if(isset($_GET['semana'])){
                $semana = $_GET['semana'];
            }

            $dias = $semana*7;
            if($dias >= 0)
                $lunes = strtotime("previous monday " . " + $dias days");
            else{
                $dias = $dias * (-1);
                $lunes = strtotime("previous monday " . " - $dias days");
            }

            $tareas = getTareasSemana($mysqli, $_SESSION['cod_usuario'], $lunes);
            echo $twig->render('calendario.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'semana'=>$semana, 'lunes'=>$lunes,'titulo'=>"Calendario"]);
        }
    }
    else{
        echo $twig->render('login.html');
    }
   


?>