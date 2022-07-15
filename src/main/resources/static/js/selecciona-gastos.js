/**
 * @author Julián Quenard *
 * @ 01-09-2021
 */
$(document).ready(function(){});		

$('[name="btnRadio"]').click(function () {
	var checkedradio = $('[name="btnRadio"]:radio:checked').val();
	$('#empresaSeleccionado').attr("hidden",false);
	 		       
	if(checkedradio=='hogar'){ 		        	 
		$('#hogarSeleccionado').show();
	 	$('#empresaSeleccionado').hide();
	}
	if(checkedradio=='empresa'){ 		        	 
	  	$('#hogarSeleccionado').hide();
	   	$('#empresaSeleccionado').show();
	}
});	
 		    