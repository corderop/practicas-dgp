<?php

    function conectar_bd(){
        $mysqli = new mysqli("localhost", "u681824297_dgp", "dgp.7.host.DB", "u681824297_dgp");
        if ($mysqli->connect_errno) {
            echo ("Fallo al conectar: " . $mysqli->connect_error);
        }

        return $mysqli;
    }

    function aniadirUsuario($mysqli, $usuario, $pass, $tipo){
        $pass = password_hash($pass, PASSWORD_DEFAULT);

        $sql = "INSERT INTO USUARIO(nombre,pass,tipo) VALUES('$usuario','$pass','$tipo')";
        $mysqli->query($sql);

    }

    function getUsuario($mysqli,$usuario) {
       
        $stmt = $mysqli->prepare("SELECT * from USUARIO where nombre=? OR cod_usuario=?");
        $stmt->bind_param("s", $usuario);
        $stmt->bind_param("s", $usuario);
        $stmt->execute();
    
        $resultado = $stmt->get_result();
    
        while($res = $resultado->fetch_assoc()) {
          $usuario = $res;
        }
        return $usuario;
    }

    function checkLogin($mysqli,$usuario,$pass){
        $usuario = getUsuario($mysqli,$usuario);
        if(password_verify($pass,$usuario['pass']))
            return true;
        return false;
    }

//TODO hay que tener en cuenta que descripcion o multimedia pueden ir vacios
    function crearTarea($mysqli, $titulo, $objetivo, $descripcion, $multimedia, $fecha_limite, $tutor, $alumno){ //Sin comprobar
        $tutor=getUsuario($mysqli,$tutor);
        $alumno=getUsuario($mysqli,$alumno);

        $titulo = mysqli_real_escape_string($mysqli,$titulo);
        $objetivo = mysqli_real_escape_string($mysqli,$objetivo);
        $descripcion = mysqli_real_escape_string($mysqli,$descripcion);

        $sql = "INSERT INTO TAREA(titulo,descripcion,fecha_limite,objetivo,multimedia,crea,realiza) VALUES('$titulo','$descripcion','$fecha_limite','$objetivo','$multimedia','$tutor','$alumno')";
    
        $mysqli->query($sql);
    }

    function getTareas($mysqli,$usuario,$estado_tarea){ //Sin comprobar
        $usuario=getUsuario($mysqli,$usuario);

        $sql="SELECT * FROM TAREA WHERE(crea='$usuario['cod_usuario']' OR realiza='$usuario['cod_usuario']') AND WHERE realizada='$estado_tarea'";
        
        $res = $mysqli->query($sql);

        $resultado = array();

        if ($res->num_rows > 0) {
            while($row = $res->fetch_array()) {
                $resultado[] = $row;
            }
        }

        return $resultado;
    }

    function getTarea($mysqli, $cod_tarea){ //Sin comprobar
        $sql="SELECT * FROM TAREA WHERE cod_tarea='$cod_tarea'";

        $resultado = $mysqli->query($sql);
    
        while($res = $resultado->fetch_assoc()) {
          $tarea = $res;
        }

        //Añadir los mensajes
        $mensajes = getMensajesTarea($mysqli, $cod_tarea);
        $tarea['mensajes'] = $mensajes;

        return $tarea;
    }

    function getMensajesTarea($mysqli, $cod_tarea){ //Sin comprobar
        $sql = "SELECT * FROM MENSAJES WHERE cod_tarea='$cod_tarea'";
        
        $mysqli->query($sql);
    }

//TODO hay que tener en cuenta que mutimedia y contenido pueden ir vacíos
    function sendMensaje($mysqli, $contenido, $multimedia, $contiene, $envia){ //Sin comprobar

        $contenido = mysqli_real_escape_string($mysqli,$contenido);
        $fecha = date("Y-m-d H:i:s");

        $sql = "INSERT INTO MENSAJE(fecha,contenido,multimedia,contiene,envia) VALUES('$fecha','$contenido','$multimedia','$contiene','$envia')";
        $mysqli->query($sql);
    }


    function finalizarTarea($mysqli, ){

    }

    	
   
?>