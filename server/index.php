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
            echo $twig->render('index.html', ['usuario'=>$usuario, 'usuarios'=>$usuarios, 'titulo'=> "Home" ]);
        }
        if($usuario['tipo'] == "TUTOR"){
            if (isset($_SESSION['previous_location']) && $_SESSION['previous_location'] == 'crearTarea') {
                $tareas = getTareasTutor($mysqli, $_SESSION['cod_usuario']);
                
                foreach($tareas as &$tarea){
                    $tarea['tipo_archivo'] = mime_content_type($tarea['multimedia']);
                }

                echo $twig->render('index.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=> "Home", 'msg'=>"Tarea creada correctamente"]);
                $_SESSION['previous_location'] = '';
            }
            else if (isset($_SESSION['previous_location']) && $_SESSION['previous_location'] == 'eliminarTarea') {
                $tareas = getTareasTutor($mysqli, $_SESSION['cod_usuario']);
                
                foreach($tareas as &$tarea){
                    $tarea['tipo_archivo'] = mime_content_type($tarea['multimedia']);
                }
                
                echo $twig->render('index.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=> "Home", 'msg'=>"Tarea eliminada correctamente"]);
                $_SESSION['previous_location'] = '';
            }
            else{
                $tareas = getTareasTutor($mysqli, $_SESSION['cod_usuario']);
                
                foreach($tareas as &$tarea){
                    $tarea['tipo_archivo'] = mime_content_type($tarea['multimedia']);
                }

                echo $twig->render('index.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=> "Home"]);
            }
        }
        if($usuario['tipo'] == "USUARIO"){
            $tareas = getTareas($mysqli, $_SESSION['cod_usuario']);
            echo $twig->render('index.html', ['usuario'=>$usuario, 'tareas'=>$tareas, 'titulo'=> "Home"]);
        }
    }
    else{
        echo $twig->render('login.html');
    }
   


?>