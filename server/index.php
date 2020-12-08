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
            //Coger los datos que necesitemos 
            $usuarios = getUsuarios($mysqli);
            echo $twig->render('index.html', ['usuario'=>$usuario, 'usuarios'=>$usuarios, 'titulo'=> "Usuarios" ]);
        }
        if($usuario['tipo'] == "TUTOR"){
            $tareas = getTareasTutor($mysqli, $_SESSION['cod_usuario']);
                
            foreach($tareas as &$tarea){
                $tarea['tipo_archivo'] = mime_content_type($tarea['multimedia']);
            }

            if (isset($_SESSION['previous_location']) && $_SESSION['previous_location'] == 'crearTarea') {

                echo $twig->render('index.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=> "Tareas", 'msg'=>"Tarea creada correctamente"]);
                $_SESSION['previous_location'] = '';
            }
            else if (isset($_SESSION['previous_location']) && $_SESSION['previous_location'] == 'eliminarTarea') {
                
                echo $twig->render('index.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=> "Tareas", 'msg'=>"Tarea eliminada correctamente"]);
                $_SESSION['previous_location'] = '';
            }
            else if (isset($_SESSION['previous_location']) && $_SESSION['previous_location'] == 'finalizarTarea') {

                echo $twig->render('index.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=> "Tareas", 'msg'=>"Tarea finalizada correctamente"]);
                $_SESSION['previous_location'] = '';
            }
            else{

                echo $twig->render('index.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=> "Tareas"]);
            }
        }
        if($usuario['tipo'] == "USUARIO"){
            $tareas = getTareasPorRealizar($mysqli, $_SESSION['cod_usuario']);
            echo $twig->render('index.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=> "Tareas"]);
        }
    }
    else{
        echo $twig->render('login.html');
    }
   


?>