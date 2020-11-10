function buscar(busq){
    // var barra = document.getElementById("busqueda");
    // var search = $(barra).val();

    // console.log("Buscando..." + search);

    // if(search.length > 3){
    //     //Coger todos los usurios que coincidan
    //     //TODO arreglar busqueda
    //     var usuarios = document.getElementsByClassName("ui card");
    //     console.log("Numero de usuarios: " + usuarios.length);
    //     console.log("usuario: " + usuarios[1].value);
    //     for(let i = 0; i < usuarios.length; i++){
    //         if (!usuarios[i].getElementsByClassName("header")[0].value.contains(search)){
    //             usuarios[i].style.display = "none";
    //         }
    //         else{
    //             usuarios[i].style.display = "flex";
    //         }
    //     }
    //     //Esconderlos


        
    // }

    // Toma el valor introducido en la barra
    let valor = busq.value;
    // Toma las tarjetas
    let tarjetas = document.querySelectorAll('.ui .card');

    if(valor.length > 3){
        for(i=0; i<tarjetas.length; i++){
            if(tarjetas[i].textContent.trim().includes(valor))
                tarjetas[i].style.display = "";
            else
                tarjetas[i].style.display = "none";
        }
    }
    else{
        for(i=0; i<tarjetas.length; i++){
            tarjetas[i].style.display = "";
        }
    }

}
