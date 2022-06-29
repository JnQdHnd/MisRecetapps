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

function enviarRecetaId(receta) {	
	console.log('ID de la receta: ' + $(receta).attr('recetaId'));	
	var tienePreparaciones = $(receta).attr('tienePreparaciones') > 0;
	console.log('tienePreparaciones: ' + tienePreparaciones);
	if(tienePreparaciones){
		$('#formVerReceta').attr('action', '/receta/verRecetaConPreparaciones/' + $(receta).attr('recetaId'));
	}	
	else{					
		$('#formVerReceta').attr('action', '/receta/verReceta/' + $(receta).attr('recetaId'));
	}
	$('#formVerReceta').submit();
};

