<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();
    session_start();

    $_usuario=getUsuario($mysqli, $_SESSION['cod_usuario']);
    echo $_GET['cod_tarea'];

    if( $_usuario['tipo'] == "TUTOR" && isset($_GET['cod_tarea'])){
        $cod_tarea = $_GET['cod_tarea'];
        $tarea = getTareaSinMensajes($mysqli, $cod_tarea);

        if($tarea['crea'] == $_usuario['cod_usuario']){
            finalizarTarea($mysqli, $cod_tarea);
            $_SESSION['previous_location'] = 'finalizarTarea';
            header("Location: index.php");
        }
        else{
            header("Location: index.php");
        }
    }else {
        header("Location: index.php");
    }

?>