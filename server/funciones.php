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
       
        $stmt = $mysqli->prepare("SELECT * from USUARIO where nombre=?");
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

    	
   
?>