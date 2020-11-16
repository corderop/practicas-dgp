<?php
    require_once "../vendor/autoload.php";
    include('funciones.php');

    $loader = new \Twig\Loader\FilesystemLoader('templates');
    $twig = new \Twig\Environment($loader);
    //Conexion a la base de datos
    $mysqli=conectar_bd();
    session_start();

    $_usuario=getUsuario($mysqli, $_SESSION['cod_usuario']);

    if( $_usuario['tipo'] == "TUTOR" ){
        $nombre = null;
        $multimedia = null;
        $descripcion = null;
        $fecha = "";
        $usuarios = [];
        $grupos = [];
        
        //Captura de variables
        if(isset($_POST['nombre'])){
            $nombre = $_REQUEST['nombre'];
        }
        if(isset($_POST['comentario'])){
            $descripcion = $_REQUEST['comentario'];
        }
        if(isset($_FILES['multimedia'])){
            $multimedia = mysqli_real_escape_string($mysqli,$_FILES['multimedia']['name']);
        }
        if(isset($_POST['grupos'])){
            $grupos = $_REQUEST['grupos'];
        }
        if(isset($_POST['usuarios'])){
            $usuarios = $_REQUEST['usuarios'];
        }
        if(isset($_POST['fecha'])){
            $fecha = $_REQUEST['fecha'];
        }

        if($nombre && $descripcion && ( $usuarios || $grupos ) ){
            // Toma el archivo si lo ha anadido
            if($multimedia){
                $targetDir = "multimedia/";
                $fileName = date("Y-m-d-H-i-s") . $multimedia;
                $targetFilePath = $targetDir . $fileName;

                // $allowTypes = array('jpg','png','jpeg');
                $imageFileType = strtolower(pathinfo($targetFilePath,PATHINFO_EXTENSION));
                // if(in_array($imageFileType, $allowTypes)){
                if(move_uploaded_file($_FILES["multimedia"]["tmp_name"], $targetFilePath)){
                    //echo "Bien";
                }
                else{
                    echo "Hay algun error";
                }
                // }
                $multimedia = $targetFilePath;
            }

            if($usuarios){
                foreach ($usuarios as &$user){
                    crearTarea($mysqli, $nombre, NULL, $descripcion, $multimedia, $fecha, $_SESSION['cod_usuario'], $user);
                }
            }
            if($grupos){
                foreach ($grupos as &$grupo){
                    $g = getGrupo($mysqli, $grupo);
                    if($g['integrantes']){
                        foreach ($g['integrantes'] as &$user){
                            if(array_search($user['cod_usuario'], $usuarios) === FALSE)
                                crearTarea($mysqli, $nombre, null, $descripcion, $multimedia, $fecha, $_SESSION['cod_usuario'], $user['cod_usuario']);
                        }
                    }
                }
            }
            
            $_SESSION['previous_location'] = 'crearTarea';
            header("Location: index.php");
        }
        // No añado else ya que los datos introducidos son comprobados por javascript
    }else {
        header("Location: index.php");
    }

    ?>