function buscar(busq){
    // Toma el valor introducido en la barra
    let valor = busq.value;
    // Toma las tarjetas
    let tarjetas = document.querySelectorAll('.ui .card');

    if(valor.length > 3){
        for(i=1; i<tarjetas.length; i++){
            if(tarjetas[i].textContent.trim().includes(valor))
                tarjetas[i].style.display = "";
            else
                tarjetas[i].style.display = "none";
        }
    }
    else{
        for(i=1; i<tarjetas.length; i++){
            tarjetas[i].style.display = "";
        }
    }

}
