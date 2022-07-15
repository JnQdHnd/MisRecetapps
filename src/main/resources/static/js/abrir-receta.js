/**
 * @author Julián Quenard *
 * @ 01-09-2021
 */
$(document).ready(function(){});
var url = '/receta/listarMias';
$("#tablaDeRecetas").load(url);
		 		
$('[name="btnRadio"]').click(function () {
	var checkedradio = $('[name="btnRadio"]:radio:checked').val();
	if(checkedradio=='mias'){ 
		$('#seleccionaListado').attr('soloFavoritas', false);
		filtraFavoritas(); 	 			      	
	}
    if(checkedradio=='favoritas'){
		$('#seleccionaListado').attr('soloFavoritas', true);
		filtraFavoritas();  	
 	}
});	

function enviarRecetaId(casillero) {	
	console.log('ID de la receta: ' + $(casillero).parent().attr('recetaId'));	
	var tienePreparaciones = $(casillero).parent().attr('tienePreparaciones') > 0;
	console.log('tienePreparaciones: ' + tienePreparaciones);
	if(tienePreparaciones){
		$('#formVerReceta').attr('action', '/receta/verRecetaConPreparaciones/' + $(casillero).parent().attr('recetaId'));
	}	
	else{					
		$('#formVerReceta').attr('action', '/receta/verReceta/' + $(casillero).parent().attr('recetaId'));
	}
	$('#formVerReceta').submit();
};

function llamarModalEliminar(casillero) {	
	console.log('LLAMANDO MODAL ELIMINAR')
	var idAEliminar = $(casillero).attr('idAEliminar');
	var hrefConPreparacion = $('.modalConPreparacion').attr('href');
	var hrefSinPreparacion = $('.modalSinPreparacion').attr('href');
	$('.modalConPreparacion').attr('href', hrefConPreparacion + idAEliminar);
	$('.modalSinPreparacion').attr('href', hrefSinPreparacion + idAEliminar);
	$("#eliminarConPreparacionesModal").modal("show");
};

