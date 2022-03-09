$(document).ready(function(){});
var url = '/receta/listarMias';
$("#tablaDeRecetas").load(url);
		 		
$('[name="btnRadio"]').click(function () {
	var checkedradio = $('[name="btnRadio"]:radio:checked').val();
	if(checkedradio=='mias'){  		 	        	
		url = '/receta/listarMias';	 			      	
	}
	if(checkedradio=='visibles'){ 		        	 
	   	url = '/receta/listarVisibles';	
	}
    if(checkedradio=='favoritas'){ 		        	 
		url = '/receta/listarFavoritas';	
 	}
	$("#tablaDeRecetas").load(url);
});	

function enviarRecetaId(receta) {	
	console.log('ID de la receta: ' + $(receta).attr('recetaId'));					
	$('#formVerReceta').attr('action', '/receta/verReceta/' + $(receta).attr('recetaId'));
	$('#formVerReceta').submit();
};

