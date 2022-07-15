/**
 * @author Julián Quenard *
 * @ 01-09-2021
 */
$(document).ready(function(){});

function recuperaReceta() {
	$('#recetaJson').trigger('click');
}

$('#recetaJson').change(function() {
	var tieneArchivos = this.files.length != 0;
	console.log("tieneArchivos: " + tieneArchivos);
    if(tieneArchivos){
		$('#recuperaRecetaForm').submit();
	}
});



