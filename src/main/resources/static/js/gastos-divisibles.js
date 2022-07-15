/**
 * @author Julián Quenard *
 * @ 01-09-2021
 */
$(document).ready(function(){});

if($('#idGasto').val()==''){
	$('#precio').val(null); 
	$('#unidadDeMedida').val(0);
	$('#nombre').val(0);
}

$('#nombre').click(function () {
	var seleccionado = $('#nombre').val();
	if(seleccionado=='Agua'){ 		        	 
	  	$('#unidadDeMedida').val('METRO3');
	}
	if(seleccionado=='Energía Eléctrica'){ 		        	 
	   	$('#unidadDeMedida').val('KILOWATT_HORA');
	}
	if(seleccionado=='Gas Natural'){ 		        	 
	  	$('#unidadDeMedida').val('METRO3_HORA');
	}
});