/**
 * Filtra a las tarjetas en base a lo introducido en la barra de búsqueda
 * @param {*} busq Objeto input en html. Usar el parámetro this al llamarla en HTML.
 * @param {*} num Tarjeta en la que quieres que empiece. Normalmente 0. Será 1 en las ocasiones
 * en las que exista una tarjeta que sirva para añadir tareas etc.
 */
function buscar(busq, num){
    // Toma el valor introducido en la barra
    let valor = busq.value.toLowerCase();
    // Toma las tarjetas
    let tarjetas = document.querySelectorAll('.ui .card');

    if(valor.length > 3){
        for(i=num; i<tarjetas.length; i++){
            if(tarjetas[i].textContent.trim().toLowerCase().includes(valor))
                tarjetas[i].style.display = "";
            else
                tarjetas[i].style.display = "none";
        }
    }
    // Para que se muestren todas las tarjetas cuando borra la búsqueda
    else{
        for(i=num; i<tarjetas.length; i++){
            tarjetas[i].style.display = "";
        }
    }

}

function filtroCorregidas(element) {
    let estado = element.value;

    if(estado == "T"){
        let tarjetas = document.querySelectorAll('.ui .card');
        for(i=0; i<tarjetas.length; i++){
            tarjetas[i].style.display = '';
        }
    }
    else if(estado == "C"){
        let tarjetas_corregidas = document.querySelectorAll('.corregida');
        let tarjetas_no_corregidas = document.querySelectorAll('.no-corregida');

        for(i=0; i<tarjetas_corregidas.length; i++){
            tarjetas_corregidas[i].style.display = '';
        }
        for(i=0; i<tarjetas_no_corregidas.length; i++){
            tarjetas_no_corregidas[i].style.display = 'none';
        }
    }
    else if(estado == "NC"){
        let tarjetas_corregidas = document.querySelectorAll('.corregida');
        let tarjetas_no_corregidas = document.querySelectorAll('.no-corregida');
        
        for(i=0; i<tarjetas_corregidas.length; i++){
            tarjetas_corregidas[i].style.display = 'none';
        }
        for(i=0; i<tarjetas_no_corregidas.length; i++){
            tarjetas_no_corregidas[i].style.display = '';
        }
    }
}

function comprobarPass() {
    //alert("Funciona");
    let comprobada = false;
    let pass = document.getElementById("input-fotos").value;
    console.log("pass: " + pass);
    $.ajax({
        url:"_comprobarPass.php",
        method:"POST",
        data:{imagenes:pass},
        success:function(data)
        {
            console.log("Resultado: " + data);

            let alerta = document.getElementById('alerta-usuario');
            let comprobar = document.getElementById("boton-comprobar");
            let guardar = document.getElementById("boton-guardar");

            if(data === "true"){
                console.log("Es true");
                //TODO contraseña válida se muestra el boton guardar
                //Esconder la alerta
                alerta.style.display = 'none';

                //Mostrar el boton de guardar
                guardar.style.display = "";

            }
            else{
                console.log("Es false");
                //TODO contraseña inválida mostrar la alerta
                //Mostrar la alerta
                alerta.style.display = '';
                alerta.innerHTML = "Contraseña usada. Introduzca otra combinación"
                //Esconder guardar
                guardar.style.display = "none";
                //Mostrar comprobar
                comprobar.style.display = "";
            }
        }
    });
}

function alternarSiContrasena( el, cambiar ){
    if(cambiar){
        borrarContrasena();
        document.getElementById("boton-guardar").style.display = "none";
        document.getElementById("login-fotos").style.display = "block";
        el.setAttribute('onclick', 'alternarSiContrasena(this, false);');
        el.innerHTML = "Pulsa aquí para no modificar la contraseña"
    }
    else{
        borrarContrasena();
        document.getElementById("boton-guardar").style.display = "block";
        document.getElementById("login-fotos").style.display = "none";
        el.innerHTML = "Pulsa aquí para modificar la contraseña"
        el.setAttribute('onclick', 'alternarSiContrasena(this, true);');
    }
}

function usuarioBien( el ,tipo, id){
    $.ajax({
        url:"_comprobarNombre.php",
        method:"GET",
        data:{
            nombre:document.getElementById(`nombre-${tipo}`).value,
            id_nombre: id
        },
        success:function(data)
        {
            console.log(data);
            if(!data){
                if(tipo == "admin" || tipo == "tutor"){
                    if(document.getElementById(`pass-1-${tipo}`).value == document.getElementById(`pass-2-${tipo}`).value){
                        el.submit();
                    }
                    else{
                        document.getElementById(`alerta-${tipo}`).style.display="";
                        document.getElementById(`alerta-${tipo}`).innerHTML="Las contraseñas no son iguales";
                    }
                }
                else if(tipo == "usuario"){
                    el.submit();
                }
            }
            else{
                document.getElementById(`alerta-${tipo}`).style.display="";
                document.getElementById(`alerta-${tipo}`).innerHTML="El nombre ya existe";
            }
        }
    })
}