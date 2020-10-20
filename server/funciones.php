<?php

    function conectar_bd(){
        $mysqli = new mysqli("localhost", "u681824297_dgp", "dgp.7.host.DB", "u681824297_dgp");
        if ($mysqli->connect_errno) {
            echo ("Fallo al conectar: " . $mysqli->connect_error);
        }
        else {
            echo("funciona");
        }

        return $mysqli;
    }

    

?>