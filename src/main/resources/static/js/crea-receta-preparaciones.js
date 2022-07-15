/**
 *  Código javascript para la vista de creación de recetas.
 *	Julián Quenard
 *	01-09-2021
 */
$(document).ready(function(){});

var dispositivo = $('#dispositivo').attr('dispositivo');
var contenedor = '.card';
var esEdicion = $('.recetaId').val() != null && $('.recetaId').val() > 0;


$('.form-select').keyup(function( event ) {
  if ( event.which == 8 || event.which == 46 ) {
     event.preventDefault();
     console.log('CLICK EN SUPR o RETROCESO')
  }
  $(this).val(0);
  var clase = $(this).attr('class');
  if(clase.includes("text-secondary") == false){
	$(this).attr('class', clase + ' text-secondary');
  }
});

function cambiaColorValidado(casillero) {
	console.log('Cambio en color')
	$(casillero).removeClass('alert-danger');
};

function cambiaColor(casillero) {
	console.log('Cambio en color')
	if($(casillero).val() > 0){
		$(casillero).removeClass('text-secondary');
	};	
};

function recetaNueva(btn) {
	var orden = $(btn).attr('orden');
	$('.btnModal').attr('orden', orden);
};

function agregaRecetaEnListado(btn) {
	var orden = $(btn).attr('orden');
	var ordenNeg = orden * -1;
	var id = '#preparacion' + orden;
	var preparacionSelect = id + ' .recetaPreparacionSelect'
	var preparacionInput = id + ' .recetaPreparacionInput'
	var nombreReceta = $('.recetaPreparacionInputModal').val();
	
	$(preparacionSelect).append($('<option>', {value: ordenNeg}));	
	$(preparacionSelect).val(ordenNeg);
	$(preparacionInput).val(nombreReceta);
	
	var url = '/receta/agregaRecetaEnListado/' + orden;
	
	console.log('Porciones ' + $('#porciones').val());
	
	if($('#porciones').val() == null || $('#porciones').val() == ''){
		console.log('PORCIONES NULL O VACIO');
		$('#porciones').val(0);
	}
	$('#formulario').attr('action', url);
	$('#formulario').submit();
};

function editaPreparacion(btn) {
	var orden = $(btn).attr('orden');
	var index = orden - 1;	
	var url = '/receta/editaPreparacion/' + index;	
	if($('#porciones').val() == null || $('#porciones').val() == ''){
		console.log('PORCIONES NULL O VACIO');
		$('#porciones').val(0);
	}
	$('#formulario').attr('action', url);
	$('#formulario').submit();
};

function agregaPreparacion() {	
	console.log('Agregando preparación...')
	var numPreparacion = parseInt($('#cantidadDePreparaciones').val());
	console.log('cantidad de Preparaciones antes: ' + numPreparacion);
	var numOrigen = numPreparacion;
	numPreparacion++;
	var id = 'preparacion' + numOrigen;
	var nuevoId = 'preparacion' + numPreparacion;		
	console.log('nuevoId ' + nuevoId);
	
	$("#" + id).clone().attr('id', nuevoId).insertAfter("#" + id);
	$('#' + nuevoId).attr('orden', numPreparacion);
	$('#' + nuevoId).attr('name', 'preparacion' + numPreparacion);	
	$('#' + nuevoId + ' .recetaPreparacionSelect').attr('id', 'recetaPreparacion' + numPreparacion);
	$('#' + nuevoId + ' .recetaPreparacionSelect').attr('name', 'preparaciones[' + numOrigen + '].recetaId');
	$('#' + nuevoId + ' .recetaPreparacionInput').attr('id', 'recetaPreparacionInput' + numPreparacion);
	$('#' + nuevoId + ' .recetaPreparacionInput').attr('name', 'recetaPreparacionInput' + numPreparacion);
	$('#' + nuevoId + ' .recetaPreparacionSelect').addClass('text-secondary');
	$('#' + nuevoId + ' .recetaPreparacionSelect').attr('orden', numPreparacion);
	$('#' + nuevoId + ' .recetaPreparacionInput').attr('orden', numPreparacion);
	$('#' + nuevoId + ' .recetaNueva').attr('orden', numPreparacion);	
	$('#' + nuevoId + ' .recetaNueva').attr('estadoActivo', 'select');	
	$('#' + nuevoId + ' .recetaPreparacionSelect').val(0);
	$('#' + nuevoId + ' .recetaPreparacionInput').val(null); 	
	$('#' + nuevoId + ' .preparacionTitulo').attr('name', 'preparacionTitulo' + numPreparacion);
	$('#' + nuevoId + ' .preparacionTitulo').text('Preparacion ' + numPreparacion);
	$('#' + nuevoId + ' .btnPreparacion').attr('name', 'btnPreparacion' + numPreparacion);
	$('#' + nuevoId + ' .btnPreparacion').attr('id', 'btnPreparacion' + numPreparacion);
	$('#' + nuevoId + ' .btnPreparacion').attr('orden', numPreparacion);
	$('#' + nuevoId + ' .casilleroSelectInput').show();
	$('#' + nuevoId + ' .casilleroComplete').remove();
	$('#' + nuevoId + ' .preparacionCheck').remove();
	$('#' + nuevoId).addClass('bg-light');
	$('#cantidadDePreparaciones').val(numPreparacion);	
	if(numPreparacion > 1){
		$('.btnPreparacion').removeClass('disabled');
	}
};

function eliminaPreparacion(esteBtn) {
	console.log('Eliminando...')
	var orden = $(esteBtn).attr('orden');
	var preparacionId = '#preparacion' + orden;
	var isComplete = $(preparacionId + ' .preparacionCheck').is(':visible');
	console.log('isComplete: ' + isComplete);
	
	if(isComplete){
		console.log('Llamando eliminaPreparacion del controller RecetaPreparacionCreacionController')
		var index = $(esteBtn).attr('orden') - 1;
		var url = '/receta/eliminaPreparacion/' + index;
		$('#formulario').attr('action', url);
		$('#formulario').submit();
	}
	else{
		var numPreparacion = parseInt($('#cantidadDePreparaciones').val());
		numPreparacion--;
		var filaNum = $(esteBtn).attr('orden');	
		var idAEliminar = 'preparacion' + filaNum;
		$('#' + idAEliminar).remove();
		
		var i = 1;
		var index = i-1;
		$("#cuerpoPreparaciones .card").each(function() {
			$(this).attr('id', 'preparacion' + i);
			$(this).attr('name', 'preparacion' + i);
			$(this).attr('orden', i);					
			$('#preparacion' + i + ' .recetaPreparacionSelect').attr('orden', i);		
			$('#preparacion' + i + ' .recetaPreparacionInput').attr('orden', i);
			$('#preparacion' + i + ' .recetaNueva').attr('orden', i);							
			$('#preparacion' + i + ' .preparacionTitulo').attr('name', 'preparacionTitulo' + i);
			$('#preparacion' + i + ' .preparacionTitulo').text('Preparacion ' + i);
			$('#preparacion' + i + ' .btnPreparacion').attr('orden', i);
			$('#preparacion' + i + ' .btnPreparacion').attr('id', 'btnPreparacion' + i);
			$('#preparacion' + i + ' .btnPreparacion').attr('name', 'btnPreparacion' + i);
			$('#preparacion' + i + ' .recetaPreparacionSelect').attr('name', 'preparaciones[' + index + '].recetaId');
			$('#preparacion' + i + ' .recetaPreparacionSelect').attr('id', 'recetaPreparacion' + i);
			$('#preparacion' + i + ' .recetaPreparacionInput').attr('name', 'recetaPreparacionInput' + i);
			$('#preparacion' + i + ' .recetaPreparacionInput').attr('id', 'recetaPreparacionInput' + i);		
			i++
			index++
		});		
		$('#cantidadDePreparaciones').val(numPreparacion);	
		if(numPreparacion == 1){
			$('.btnPreparacion').addClass('disabled');
		}
	}
};

function verifica() {	
	console.log('--VALIDANDO FORMULARIO JAVASCRIPT--');
	var hayErrorEnNombre = validaNombre();
	var hayErrorEnPorciones = validaPorciones();
	var hayErrorEnPreparaciones = validaPreparaciones();
		
	if (hayErrorEnNombre == false && 
	    hayErrorEnPorciones == false &&
		hayErrorEnPreparaciones == false) {
		$(' .aDesactivar').prop("disabled", true);	
		$('#formulario').submit();
	}
	else {
		return false;
	}
};

function replicaFormReceta() {
	console.log('REPLICANDO FORMULARIO')
	var inputsReceta = $('#formulario').serializeArray();
	jQuery.each( inputsReceta, function( i, inputReceta ) {
		$(document.createElement('input'))
		.attr('name', inputReceta.name + 'Modal')
		.attr('type', 'hidden')
		.val(inputReceta.value)
		.appendTo('.escondidos');
		console.log(inputReceta.name + ': ' + inputReceta.value);
    });	
};

function validaNombre(){
	console.log('VALIDANDO NOMBRE');
	var hayError = false;
	if ($('#nombre').val().length == 0) {
		$('#nombre').addClass('alert-danger');
		$('#errorNombre').show();
		hayError = true;
		console.log('hayError: ' + hayError);
	} 
	else {
		$('#nombre').removeClass('alert-danger');
		$('#errorNombre').hide();
	};
	return hayError;
}

function validaPorciones(){
	console.log('VALIDANDO PORCIONES');
	var hayError = false;
	if ($('#porciones').val().length == 0) {
		$('#porciones').addClass('alert-danger');
		$('#errorPorciones').show();
		hayError = true;
		console.log('hayError: ' + hayError);
	} 
	else {
		$('#porciones').removeClass('alert-danger');
		$('#errorPorciones').hide();
	};
	return hayError;
}

function validaPreparaciones(){
	console.log('VALIDANDO PREPARACIONES');
	var hayError = false;
	$("#cuerpoPreparaciones " + contenedor).each(function() {
		var id = $(this).attr('id');
		var isComplete = $(id + ' .preparacionCheck').is(':visible');		
		var estadoActivo = $('#' + id + ' .recetaNueva').attr('estadoActivo');
		var casillero = ' .recetaPreparacionSelect';
		if(estadoActivo != 'complete'){
			if(estadoActivo == 'input'){
				casillero = ' .recetaPreparacionInput';
			}
			if ($('#' + id + casillero).val() == null || $('#' + id + casillero).val() == 0) {
				$('#' + id + ' .recetaPreparacionSelect').addClass('alert-danger');
				$('#' + id + ' .recetaPreparacionInput').addClass('alert-danger');
				hayError = true;
				console.log('hayError: ' + hayError + ' En  ' + id + ', casillero ' + casillero);
			}
			else {
				$('#' + id + ' .recetaPreparacionSelect').removeClass('alert-danger');
				$('#' + id + ' .recetaPreparacionInput').removeClass('alert-danger');
			};				
		};	
	});
	return hayError;
};

