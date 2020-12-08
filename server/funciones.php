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

        $avatar = cargarFoto();
        

        if($avatar == ""){
            $sql = "INSERT INTO USUARIO(nombre,pass,tipo) VALUES('$usuario','$pass','$tipo')";
        }
        else{
            $sql = "INSERT INTO USUARIO(nombre,pass,tipo, avatar) VALUES('$usuario','$pass','$tipo', '$avatar')";
        }
        
        $mysqli->query($sql);
    }

    /**
     * Funcion para obtener la ruta de la imagen cargada
     * @return string $targetFilePath ruta de la imagen final
     */
    function cargarFoto(){
        $targetDir = "img/";
        $fileName = date("Y-m-d-H-i-s") . basename($_FILES['avatar']['name']);
        $targetFilePath = $targetDir . $fileName;

        if(basename($_FILES['avatar']['name']) == ""){
            return "img/user.jpg";
        }

        $allowTypes = array('jpg','png','jpeg');
        $imageFileType = strtolower(pathinfo($targetFilePath,PATHINFO_EXTENSION));
        if(in_array($imageFileType, $allowTypes)){
            if(move_uploaded_file($_FILES["avatar"]["tmp_name"], $targetFilePath)){
                //echo "Bien";
              }
              else{
                echo "Hay algun error";
              }
        }
        return $targetFilePath;
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
     * Funcion para actualizar los datos de un usuario
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param string $usuario Nombre nuevo
     * @param string $pass nueva contraseña
     * @param string $avatar Nuevo avatar
     */
    function actualizarUsuario($mysqli, $usuario, $nombre, $pass, $avatar){
        if($pass){
            $pass = password_hash($pass, PASSWORD_DEFAULT);
            $sql = "UPDATE USUARIO set nombre='$nombre', pass='$pass', avatar='$avatar' where cod_usuario=".$usuario['cod_usuario'];
        }
        else{
            $sql = "UPDATE USUARIO set nombre='$nombre', avatar='$avatar' where cod_usuario=".$usuario['cod_usuario'];
        }
        
        $mysqli->query($sql);
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
     * Comprueba si ya hay un usuario con el mismo nombre
     * @param mysqli $mysqli Base de datos sobre la que se actua
     * @param nombre Nombre a comprobar
     */
    function existeNombre( $mysqli, $nombre ){
        $sql = "SELECT * FROM USUARIO WHERE nombre='$nombre'";
        $res = $mysqli->query($sql);
        
        if($res->num_rows > 0){
            return true;
        }

        return false;
    }

    /**
     * Funcion para eliminar un usuario por completo de la base de datos
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $cod_usuario Codigo del usuario que queremos eliminar
     */
    function deleteUsuario($mysqli, $cod_usuario) {
        //Eliminamos la foto del servidor
        $usuario = getUsuario($mysqli, $cod_usuario);
        if($usuario['avatar'] != 'img/user.jpg'){
            if (file_exists($usuario['avatar'])) {
                unlink($usuario['avatar']);
                echo 'File '.$usuario['avatar'].' has been deleted';
            } else {
                echo 'Could not delete '.$usuario['avatar'].', file does not exist';
            }
        }

        // Elimina las tareas asociadas al usuario
        $tareas = getTareas($mysqli, $cod_usuario);
        foreach($tareas as &$tarea){
            deleteTarea($mysqli,$tarea['cod_tarea']);
        }

        // Eliminar de la tabla intregrado 
        $mysqli->query("DELETE FROM INTEGRADO WHERE cod_usuario='$cod_usuario'");
        //Eliminar al usario
        $mysqli->query("DELETE FROM USUARIO where cod_usuario='$cod_usuario'");
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
     * @param string $fecha Fecha en la que la tarea debe quedar finalizada en formato (DD/MM/AAAA)
     * @param int $tutor Codigo numerico del tutor en la base de datos
     * @param int $alumno Codigo numerico del alumno en la base de datos
     */
    function crearTarea($mysqli, $titulo, $objetivo, $descripcion, $multimedia, $pictograma, $fecha, $tutor, $alumno){

        $titulo = mysqli_real_escape_string($mysqli,$titulo);
        $objetivo = mysqli_real_escape_string($mysqli,$objetivo);
        $descripcion = mysqli_real_escape_string($mysqli,$descripcion);

        if($multimedia == ""){
            $multimedia = "multimedia/tarea.png";
        }
        if($fecha == ""){
            $fecha = "00/00/0000";
        }

        $sql = "INSERT INTO TAREA(titulo,descripcion,fecha_limite,objetivo,multimedia,pictograma,crea,realiza) VALUES('$titulo','$descripcion',STR_TO_DATE('$fecha', '%d/%m/%Y'),'$objetivo','$multimedia','$pictograma','$tutor','$alumno')";
    
        $mysqli->query($sql);
    }

    /**
     * Función que toma todas las tareas de un usuario
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param mixed $usuario String con el user o int con el codigo de usuario del que necesitamos las tareas
     * @return mixed[] Array con las tareas que hemos pedido.
     */
    function getTareas($mysqli,$usuario){ //Sin comprobar
        // $usuario=getUsuario($mysqli,$usuario);
        // $cod_usuario = $usuario['cod_usuario'];

        $sql="SELECT * FROM TAREA WHERE(crea='$usuario' OR realiza='$usuario')";
        
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
     * Función que toma todas las tareas por realizar de un usuario
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param mixed $usuario String con el user o int con el codigo de usuario del que necesitamos las tareas
     * @return mixed[] Array con las tareas que hemos pedido.
     */
    function getTareasPorRealizar($mysqli,$usuario){ //Sin comprobar
        // $usuario=getUsuario($mysqli,$usuario);
        // $cod_usuario = $usuario['cod_usuario'];

        $sql="SELECT * FROM TAREA WHERE(crea='$usuario' OR realiza='$usuario') AND realizada=0";
        
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
     * Obtiene todas la tareas de un usuario en una semana
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param mixed $usuario String con el user o int con el codigo de usuario del que necesitamos las tareas
     * @param mixed $lunes Primer día de la semana. Obtiene las tareas a partir de ese día. Formato: string 'DD/MM/AAAA'
     * @return mixed[] Array con las tareas que hemos pedido.
     */
    function getTareasSemana($mysqli,$usuario, $lunes){
        $sql="SELECT * FROM TAREA WHERE(realiza='$usuario' AND fecha_limite>=FROM_UNIXTIME($lunes) AND fecha_limite<ADDDATE( FROM_UNIXTIME($lunes), INTERVAL 7 DAY));";
        
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
     * Se obtienen las tareas de un tutor con los nombres de los usuarios que la realizan ordenada por las no realizadas primero y dentro de esto las no corregidas primero
     * @param mysql $mysqli Base de datos sobre la que se actua
     * @param mixed $usuario String con el user o int con el codigo de usuario del que necesitamos las tareas
     * @return mixed[] Array con las tareas que hemos pedido.
     */
    function getTareasTutor($mysqli,$tutor){ //Sin comprobar
        // $usuario=getUsuario($mysqli,$tutor);

        // $cod_usuario = $usuario['cod_usuario'];

        $sql="SELECT * FROM TAREA T inner join USUARIO U on (T.realiza=U.cod_usuario) WHERE T.crea='$tutor' ORDER BY T.realizada, T.corregida";
        
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
     * Funcion para tomar una tarea de una base de datos sin los mensajes
     * @param mysql $mysqli Base de datos sobre la que actuar
     * @param int $cod_tarea La tarea que se quiere tomar de la BBDD
     * @return mixed[] Datos de la tarea, en caso de que exista
     */
    function getTareaSinMensajes($mysqli, $cod_tarea){ //Sin comprobar
        $sql="SELECT * FROM TAREA WHERE cod_tarea='$cod_tarea'";

        $resultado = $mysqli->query($sql);
    
        while($res = $resultado->fetch_assoc()) {
          $tarea = $res;
        }

        return $tarea;
    }

    /**
     * Devuelve los mensajes asociados a una tarea concreta
     * @param mysql $mysqli Base de datos sobre la que actuar
     * @param int $cod_tarea Codigo de la tarea de la que extraemos los mensajes 
     * @return mixed[] Datos de los mensajes, si la tarea tiene mensajes
     */
    function getMensajesTarea($mysqli, $cod_tarea){ //Sin comprobar
        $sql = "SELECT * FROM MENSAJE WHERE contiene='$cod_tarea'";
        
        $resultado = $mysqli->query($sql);

        $mensajes = array();

        if($resultado->num_rows > 0){
            while($res = $resultado->fetch_assoc()) {
                $mensajes[] = $res;
            }
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

        if($contenido)
            $contenido = mysqli_real_escape_string($mysqli,$contenido);
        $fecha = date("Y-m-d");

        $sql = "";
        if($multimedia && $contenido)
            $sql = "INSERT INTO MENSAJE(fecha,contenido,multimedia,contiene,envia) VALUES(STR_TO_DATE('$fecha', '%Y-%m-%d'),'$contenido','$multimedia','$contiene','$envia')";
        else if($multimedia)
            $sql = "INSERT INTO MENSAJE(fecha,contenido,multimedia,contiene,envia) VALUES(STR_TO_DATE('$fecha', '%Y-%m-%d'),NULL,'$multimedia','$contiene','$envia')";
        else if($contenido)
            $sql = "INSERT INTO MENSAJE(fecha,contenido,multimedia,contiene,envia) VALUES(STR_TO_DATE('$fecha', '%Y-%m-%d'),'$contenido',NULL,'$contiene','$envia')";
        

        $mysqli->query($sql);
    }

    /**
     * Modifica el campo corregida de la tarea
     * @param mysql $mysqli Base de datos sobre la que actuar
     * @param string $tarea Código de la tarea a modificar
     * @param bool $valor Valor true o false al que cambiar
     */
    function modificarCorregidaTarea($mysqli, $tarea, $valor){
        $sql="UPDATE TAREA SET corregida=$valor WHERE cod_tarea=$tarea;";
        
        $res = $mysqli->query($sql);
    }

    //TODO Hace falta acabarla
    /**
     * Funcion que marca una tarea como finalizada
     * @param mysql $mysqli Base de datos sobre la que actuar
     * @param int $tarea codigo de la tarea a marcar como finalizada
     */
    function finalizarTarea($mysqli, $tarea){
        $sql = "UPDATE TAREA set realizada = true where cod_tarea='$tarea'";
        $mysqli->query($sql);
    }

    /**
     * Funcion para que un usuario deje una calificación de la tarea
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $tarea Codigo de la tarea
     * @param int $Puntuacion Numero de 1 a 5 en la satisfacción de la tarea
     */
    function calificarTarea($mysqli, $tarea, $puntuacion){
        $sql = "UPDATE TAREA set puntuacion = '$puntuacion' where cod_tarea='$tarea'";
        $mysqli->query($sql);
    }	

    /**
     * Funcion para eliminar una tarea
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $cod_tarea Codigo de la tarea a eliminar
     */
    function deleteTarea($mysqli, $cod_tarea) {
        //Elimina los mensajes de la tarea
        $mysqli->query("DELETE FROM MENSAJE where contiene='$cod_tarea'");

        //Eliminar una tarea
        $mysqli->query("DELETE FROM TAREA where cod_tarea='$cod_tarea'"); 
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
     * Funcion que edita un grupo
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $cod_grupo Codigo del grupo a editar
     * @param string $nombre Nombre del grupo de trabajo
     * @param int[] $usuarios Codigos de usuario de cada uno de los integrantes del grupo
     */
    function editarGrupo($mysqli, $cod_grupo, $nombre, $usuarios){
        $sql = "UPDATE GRUPO_TRABAJO set nombre='$nombre' where cod_grupo='$cod_grupo'";
        $mysqli->query($sql);

        $del = "DELETE FROM INTEGRADO WHERE cod_grupo='$cod_grupo' ";
        foreach ($usuarios as $usuario){
            $mysqli->query("INSERT IGNORE INTO INTEGRADO VALUES ('$cod_grupo', '$usuario')");
            $del = $del . "AND cod_usuario!='$usuario' ";
        }
        echo $del;
        $mysqli->query($del);
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
        $resultado = $mysqli->query("SELECT U.* from INTEGRADO I inner join USUARIO U on I.cod_usuario=U.cod_usuario where cod_grupo=$cod_grupo;");

        $integrantes = array();

        $grupo = [];
        while($res = $resultado->fetch_assoc()) {
            $grupo[] = $res;
        }

        return $grupo;
    }

    /**
     * Funcion para devolver los usuarios que no pertenecen a un grupo de trabajo
     * @param mysql $msqli Base de datos sobre la que actuar
     * @param int $cod_grupo Codigo del grupo
     * @return mixed[] $grupo Lista de no integrantes del grupo
     */
    function getNoIntegrantesGrupo($mysqli, $cod_grupo){
        $resultado = $mysqli->query("SELECT * from USUARIO U where not exists (select * from INTEGRADO I where I.cod_grupo=$cod_grupo and U.cod_usuario=I.cod_usuario ) AND U.tipo='USUARIO';");

        $integrantes = array();

        $grupo = [];

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
    function addIntegranteGrupo($mysqli, $cod_grupo, $cod_usuario) {
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