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
            if(date('w') == 1){
                if($dias >= 0){
                    $lunes = strtotime("today " . " + $dias days");
                }
                else{
                    $dias = $dias * (-1);
                    $lunes = strtotime("today " . " - $dias days");
                }
            }
            else{
                if($dias >= 0){
                    $lunes = strtotime("previous monday " . " + $dias days");
                }
                else{
                    $dias = $dias * (-1);
                    $lunes = strtotime("previous monday " . " - $dias days");
                }
            }

            $tareas = getTareasSemana($mysqli, $_SESSION['cod_usuario'], $lunes);
            $dias_semana = array(
                "lunes" => array(),
                "martes" => array(),
                "miercoles" => array(),
                "jueves" => array(),
                "viernes" => array(),
                "sabado" => array(),
                "domingo" => array()
            );
            if($tareas){
                foreach ($tareas as &$tarea){
                    if(date('w') == 1){
                        if(strtotime("today " . " - " . $dias . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["lunes"][] = $tarea;
                        }
                        elseif(strtotime("today " . " - " . $dias . "days + " . 1 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["martes"][] = $tarea;
                        }
                        elseif(strtotime("today " . " - " . $dias . "days + " . 2 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["miercoles"][] = $tarea;
                        }
                        elseif(strtotime("today " . " - " . $dias . "days + " . 3 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["jueves"][] = $tarea;
                        }
                        elseif(strtotime("today " . " - " . $dias . "days + " . 4 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["viernes"][] = $tarea;
                        }
                        elseif(strtotime("today " . " - " . $dias . "days + " . 5 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["sabado"][] = $tarea;
                        }
                        elseif(strtotime("today " . " - " . $dias . "days + " . 6 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["domingo"][] = $tarea;
                        }
                    }
                    else{
                        if(strtotime("previous monday " . " - " . $dias . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["lunes"][] = $tarea;
                        }
                        elseif(strtotime("previous monday " . " - " . $dias . "days + " . 1 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["martes"][] = $tarea;
                        }
                        elseif(strtotime("previous monday " . " - " . $dias . "days + " . 2 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["miercoles"][] = $tarea;
                        }
                        elseif(strtotime("previous monday " . " - " . $dias . "days + " . 3 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["jueves"][] = $tarea;
                        }
                        elseif(strtotime("previous monday " . " - " . $dias . "days + " . 4 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["viernes"][] = $tarea;
                        }
                        elseif(strtotime("previous monday " . " - " . $dias . "days + " . 5 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["sabado"][] = $tarea;
                        }
                        elseif(strtotime("previous monday " . " - " . $dias . "days + " . 6 . "days") == strtotime($tarea["fecha_limite"]) ){
                            $dias_semana["domingo"][] = $tarea;
                        }
                    }
                }
            }

            echo $twig->render('calendario.html', ['usuario'=>$usuario, 'dias'=>$dias_semana, 'semana'=>$semana,'titulo'=>"Calendario"]);
        }
    }
    else{
        echo $twig->render('login.html');
    }
   


?>