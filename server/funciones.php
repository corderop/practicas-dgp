<?php

    /**
     * Funcion para conectar con la base de datos
     */
    function conectar_bd(){
        $mysqli = new mysqli("localhost", "u681824297_dgp", "dgp.7.host.DB", "u681824297_dgp");
        if ($mysqli->connect_errno) {
            echo ("Fallo al conectar: " . $mysqli->connect_error);
        }

        return $mysqli;
    }

    /**
     * Funcion para añadir un usuario a la base de datos
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param string $usuario nombre del usuario
     * @param string $pass contraseña que vamos a cifrar
     * @param string $tipo tipo de usuario{ADMIN,TUTOR,USER}
     */
    function aniadirUsuario($mysqli, $usuario, $pass, $tipo, $avatar){
        $pass = password_hash($pass, PASSWORD_DEFAULT);

        if($avatar == ""){
            $sql = "INSERT INTO USUARIO(nombre,pass,tipo) VALUES('$usuario','$pass','$tipo')";
        }
        else{
            $sql = "INSERT INTO USUARIO(nombre,pass,tipo, avatar) VALUES('$usuario','$pass','$tipo', '$avatar')";
        }
        
        $mysqli->query($sql);
    }

    /**
     * Funcion que dado un usuario, devuelve sus datos
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param mixed $usuario String con el user o int con el codigo de usuario del que necesitamos las tareas
     * @return mixed[] Datos del usuario almacenados en la base de datos, si existe
     */
    function getUsuario($mysqli,$usuario) {
       
        $sql = "SELECT * from USUARIO where nombre='$usuario' OR cod_usuario='$usuario'";
        
        $resultado = $mysqli->query($sql);
    
        while($res = $resultado->fetch_assoc()) {
          $usuario = $res;
        }
        return $usuario;
    }

    /**
     * Funcion para devolver todos los usuarios del sistema
     * @param mysqli $mysqli Base de datos sobre la que se actua
     * @return mixed[] $resultado conjunto de usuarios del sistema
     */
    function getUsuarios($mysqli){
        $sql = "SELECT * from USUARIO";
        
        $res = $mysqli->query($sql);

        $resultado = array();

        if ($res->num_rows > 0) {
            while($row = $res->fetch_array()) {
                $resultado[] = $row;
            }
        }

        return $resultado;
    }

    /**
     * Funcion para cambiar la contraseña de un usuario. Puede hacerlo un administrador o el propio usuario
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param int $user Codigo de usuario al que se le cambia la contraseña
     * @param string $pass Contraseña nueva
     */
    function cambiarPass($mysqli, $user, $pass) {
        $pass = password_hash($pass, PASSWORD_DEFAULT);
        
        $sql = "UPDATE USUARIO set pass='$pass' where cod_usuario='$user'";
        $mysqli->query($sql);
    }

    /**
     * Funcion que comprueba que las credenciales del login son correctas
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param string $usuario String con el nombre de usuario introducido en el login
     * @param string $pass Contraseña cifrada introducida en el login 
     * @return bool TRUE si las credenciales son correctas o FALSE si no lo son
     */
    function checkLogin($mysqli,$usuario,$pass){
        $usuario = getUsuario($mysqli,$usuario);
        if(password_verify($pass,$usuario['pass']))
            return true;
        return false;
    }

    //TODO hay que tener en cuenta que descripcion o multimedia pueden ir vacios
    /**
     * Funcion para crear una tarea
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param string $titulo Titulo que se le da a la tarea
     * @param string $objetivo Objetivo hacia el que va dirigido la tarea
     * @param string $descripcion Descripcion de la tarea a realizar [Optativo]
     * @param string $multimedia Ruta del archivo multimedia relacionado con la tarea [Opcional]
     * @param Date $fecha_limite Fecha en la que la tarea debe quedar finalizada
     * @param int $tutor Codigo numerico del tutor en la base de datos
     * @param int $alumno Codigo numerico del alumno en la base de datos
     */
    function crearTarea($mysqli, $titulo, $objetivo, $descripcion, $multimedia, $fecha_limite, $tutor, $alumno){ //Sin comprobar
        $tutor=getUsuario($mysqli,$tutor);
        $alumno=getUsuario($mysqli,$alumno);

        $titulo = mysqli_real_escape_string($mysqli,$titulo);
        $objetivo = mysqli_real_escape_string($mysqli,$objetivo);
        $descripcion = mysqli_real_escape_string($mysqli,$descripcion);

        $sql = "INSERT INTO TAREA(titulo,descripcion,fecha_limite,objetivo,multimedia,crea,realiza) VALUES('$titulo','$descripcion','$fecha_limite','$objetivo','$multimedia','$tutor','$alumno')";
    
        $mysqli->query($sql);
    }

    /**
     * Función que toma todas las tareas de un usuario
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param mixed $usuario String con el user o int con el codigo de usuario del que necesitamos las tareas
     * @param bool $estado_tarea True si queremos que sean tareas finalizadas, false si tareas sin finalizar
     * @return mixed[] Array con las tareas que hemos pedido.
     */
    function getTareas($mysqli,$usuario,$estado_tarea){ //Sin comprobar
        $usuario=getUsuario($mysqli,$usuario);

        $cod_usuario = $usuario['cod_usuario'];

        $sql="SELECT * FROM TAREA WHERE(crea='$cod_usuario' OR realiza='$cod_usuario') AND WHERE realizada='$estado_tarea'";
        
        $res = $mysqli->query($sql);

        $resultado = array();

        if ($res->num_rows > 0) {
            while($row = $res->fetch_array()) {
                $resultado[] = $row;
            }
        }

        return $resultado;
    }

    /**
     * Se obtienen las tareas de un tutor con los nombres de los usuarios que la realizan
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param mixed $usuario String con el user o int con el codigo de usuario del que necesitamos las tareas
     * @return mixed[] Array con las tareas que hemos pedido.
     */
    function getTareasTutor($mysqli,$tutor){ //Sin comprobar
        // $usuario=getUsuario($mysqli,$tutor);

        // $cod_usuario = $usuario['cod_usuario'];

        $sql="SELECT * FROM TAREA T inner join USUARIO U on (T.realiza=U.cod_usuario) WHERE T.crea='$tutor'";
        
        $res = $mysqli->query($sql);

        $resultado = array();

        if ($res->num_rows > 0) {
            while($row = $res->fetch_array()) {
                $resultado[] = $row;
            }
        }

        return $resultado;
    }


    /**
     * Funcion para tomar una tarea de una base de datos
     * @param mysql $mysqli Base de datos sobre la que actuar
     * @param int $cod_tarea La tarea que se quiere tomar de la BBDD
     * @return mixed[] Datos de la tarea, en caso de que exista
     */
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

    /**
     * Devuelve los mensajes asociados a una tarea concreta
     * @param mysql $mysqli Base de datos sobre la que actuar
     * @param int $cod_tarea Codigo de la tarea de la que extraemos los mensajes 
     * @return mixed[] Datos de los mensajes, si la tarea tiene mensajes
     */
    function getMensajesTarea($mysqli, $cod_tarea){ //Sin comprobar
        $sql = "SELECT * FROM MENSAJES WHERE cod_tarea='$cod_tarea'";
        
        $resultado = $mysqli->query($sql);

        $mensajes = array();

        while($res = $resultado->fetch_assoc()) {
            $mensajes[] = $res;
        }

        return $mensajes;
    }

    //TODO hay que tener en cuenta que mutimedia y contenido pueden ir vacíos
    /**
     * Funcion para enviar un mensaje tutor-usuario/usuario-tutor
     * @param mysql $mysqli Base de datos sobre la que actuar
     * @param string $contenido Contenido textual del mensaje
     * @param string $multimedia Ubicacion del posible archivo multimedia que contiene el mensaje
     * @param int $contiene codigo de la tarea desde que se envia el mensaje
     * @param int $envia codigo del usuario que envia el mensaje (tutor o usuario)
     */
    function sendMensaje($mysqli, $contenido, $multimedia, $contiene, $envia){ //Sin comprobar

        $contenido = mysqli_real_escape_string($mysqli,$contenido);
        $fecha = date("Y-m-d H:i:s");

        $sql = "INSERT INTO MENSAJE(fecha,contenido,multimedia,contiene,envia) VALUES('$fecha','$contenido','$multimedia','$contiene','$envia')";
        $mysqli->query($sql);
    }

    //TODO Hace falta acabarla
    /**
     * Funcion que marca una tarea como finalizada
     * @param mysql $mysqli Base de datos sobre la que actuar
     * @param int $tarea codigo de la tarea a marcar como finalizada
     */
    function finalizarTarea($mysqli, $tarea){
        $sql = "UPDATE TAREA set (realizada = true) where cod_tarea='$tarea'";
        $mysqli->query($sql);
    }

    /**
     * Funcion para que un usuario deje una calificación de la tarea
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $tarea Codigo de la tarea
     * @param int $Puntuacion Numero de 1 a 5 en la satisfacción de la tarea
     */
    function calificarTarea($mysqli, $tarea, $puntuacion){
        $sql = "UPDATE TAREA set (puntuacion = '$puntuacion') where cod_tarea='$tarea'";
        $mysqli->query($sql);
    }	


    ///////////////////////////////////////
    //****** Grupos de trabajo **********//
    ///////////////////////////////////////

    /**
     * Funcion para devolver obtener todos los grupos de un tutor
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $cod_tutor Tutor que confecciona el grupo
     * @return mixed[] $grupo Todos los grupos del tutor especificado
     */
    function getGrupos($mysqli, $cod_tutor) {
        $sql="SELECT * from GRUPO_TRABAJO where confecciona='$cod_tutor'";
        
        $res = $mysqli->query($sql);

        $resultado = array();

        if ($res->num_rows > 0) {
            while($row = $res->fetch_array()) {
                $resultado[] = $row;
            }
        }

        return $resultado;
    }

    /**
     * Funcion que crea un nuevo grupo en la base de datos
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param string $nombre Nombre del grupo de trabajo
     * @param int $tutor Codigo del tutor que crea el nuevo grupo
     * @param int[] $usuarios Codigos de usuario de cada uno de los integrantes del nuevo grupo
     */
    function nuevoGrupo($mysqli, $nombre, $tutor, $usuarios){
        $nombre = mysqli_real_escape_string($mysqli,$nombre);
        $sql = "INSERT into GRUPO_TRABAJO (nombre, confecciona) values ('$nombre', '$tutor')";
        $mysqli->query($sql);

        $resultado = $mysqli->query("SELECT * from GRUPO_TRABAJO where nombre='$nombre' and confecciona='$tutor'");

        while($res = $resultado->fetch_assoc()) {
            $grupo = $res;
        }

        $cod_grupo = $grupo['cod_grupo'];
        foreach ($usuarios as $usuario){
            $mysqli->query("INSERT into INTEGRADO (cod_grupo, cod_usuario) values ('$cod_grupo', '$usuario')");
        }
    }

    /**
     * Funcion para devolver un grupo concreto
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $cod_grupo Codigo del grupo del que queremos obtener datos
     * @return mixed[] $grupo Grupo de trabajo con sus integrantes, si tiene
     */
    function getGrupo($mysqli, $cod_grupo) {
        $resultado = $mysqli->query("SELECT * from GRUPO_TRABAJO where cod_grupo='$cod_grupo'");

        while($res = $resultado->fetch_assoc()) {
            $grupo = $res;
        }

        $grupo['integrantes'] = getIntegrantesGrupo($mysqli,$cod_grupo);
        return $grupo;
    }

    /**
     * Funcion para devolver los integrantes de un grupo de trabajo
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $cod_grupo Codigo del grupo del que queremos obtener los integrantes
     * @return mixed[] $grupo Lista de integrantes del grupo
     */
    function getIntegrantesGrupo($mysqli, $cod_grupo){
        $resultado = $mysqli->query("SELECT * from INTEGRADO where cod_grupo='$cod_grupo'");

        $integrantes = array();

        while($res = $resultado->fetch_assoc()) {
            $grupo[] = $res;
        }
        return $grupo;
    }

    /**
     * Funcion para eliminar un grupo por completo de la base de datos
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $cod_grupo Codigo del grupo que queremos eliminar
     */
    function deleteGrupo($mysqli, $cod_grupo) {
        //Eliminar la asociaicon de integrante con grupo
        $mysqli->query("DELETE FROM INTEGRADO where cod_grupo='$cod_grupo'");

        //Eliminar el grupo en si
        $mysqli->query("DELETE FROM GRUPO_TRABAJO where cod_grupo='$cod_grupo'"); 
    }

    /**
     * Funcion para añadir un nuevo usuario a un grupo ya formado
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $cod_grupo Codigo del grupo al que queremos añadir un integrante
     * @param int $cod_usuario Codigo del usuario que queremos añadir al grupo
     */
    function addIntegranteGrpo($mysqli, $cod_grupo, $cod_usuario) {
        $mysqli->query("INSERT into INTEGRADO (cod_grupo, cod_usuario) values ('$cod_grupo', '$cod_usuario')");
    }

    /**
     * Funcion para eliminar un integrante determinado de un grupo
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $cod_grupo Codigo del grupo del que queremos eliminar un integrante
     * @param int $cod_usuario Codigo del usuario que queremos eliminar del grupo
     */
    function deleteIntegranteGrupo($mysqli, $cod_grupo, $cod_usuario) {
        $mysqli->query("DELETE FROM INTEGRADO where cod_grupo='$cod_grupo' and cod_usuario = '$cod_usuario'"); 
    }
    

?>