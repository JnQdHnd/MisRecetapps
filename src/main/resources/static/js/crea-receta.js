/**
 *  Código javascript para la vista de creación de recetas.
 *	Julián Quenard
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

function modalProducto(producto){
	var num = $(producto).attr('orden');
	$('#producto' + num).val(0);
	$('#cantidadIngrediente' + num).val(null);
	$('#unidadDeMedida' + num).val(0);	
	$('#productoModal').load('/producto/formModal');	
}

function modalArtefacto(artefacto){
	var num = $(artefacto).attr('orden');
	$('#artefactoNombre' + num).val(0); 	
	$('#minutosArtefacto').val(null); 
	$('#intensidad').val(0); 
	$('#temperatura').val(null); 
	$('#unidadTemperatura').val(0); 
	$('#artefactoModal').load('/artefacto/formModal');		
}

function buscaUnidad(producto) {
	if($(producto).val()=='A'){
		console.log('Activa AGREGA PRODUCTO')
		modalProducto(producto);
		$(producto).attr('class', 'form-select text-secondary producto');
	}
	else{
		console.log('Cambio en producto')
		var id = producto.value + 'productoID';
		console.log(id)
		var unidad = $('#' + id).attr('unidad');
		console.log(unidad)
		var ingredienteNum = $(producto).attr('orden');
		$(producto).attr('class', 'form-select producto');
		$('#ingrediente' + ingredienteNum + ' .unidadDeMedida').val(unidad);
		$('#ingrediente' + ingredienteNum + ' .unidadDeMedida').attr('class', 'form-select unidadDeMedida');
		console.log('#ingrediente' + ingredienteNum + ' .unidadDeMedida' + ' : ' + 'valor de unidad')
	}
};

function buscaArtefacto(artefacto) {
	if($(artefacto).val()=='A'){
		console.log('Activa AGREGA ARTEFACTO')
		$(artefacto).removeClass('text-dark').addClass('text-secondary');
		modalArtefacto(this);
	}
	else{
		console.log('Cambio en artefacto')
		var id = artefacto.value + 'artefactoID';
		console.log(id)
		var artefactoNum = $(artefacto).attr('orden');
		$(artefacto).removeClass('text-secondary').addClass('text-dark');
		
		var esHorno = $('#' + id).attr('esHorno');
		console.log('ES HORNO??: ' + esHorno);
			
		if(esHorno=='true'){		
			$('#intensidadTitulo').show();
			$('#artefacto' + artefactoNum + ' .minutosCasillero').show();
			$('#artefacto' + artefactoNum + ' .temperaturaCasillero').show();
			$('#artefacto' + artefactoNum + ' .intensidadCasillero').hide();
			$('#artefacto' + artefactoNum + ' .esHorno').prop("disabled", false);
			$('#artefacto' + artefactoNum + ' .artefactosUtilizadosEsHorno').val(true);
			$('#artefacto' + artefactoNum + ' .esIntensidad').prop("disabled", true);
			$('#artefacto' + artefactoNum + ' .intensidad').val(0);
			$('#artefacto' + artefactoNum + ' .intensidad').removeClass('text-dark').addClass('text-secondary');
		}
		else if(id == 'NartefactoID'){
			$('#intensidadTitulo').hide();
			$('#artefacto' + artefactoNum + ' .nombreArtefacto').removeClass('text-dark').addClass('text-secondary');
			$('#artefacto' + artefactoNum + ' .minutosCasillero').hide();
			$('#artefacto' + artefactoNum + ' .temperaturaCasillero').hide();
			$('#artefacto' + artefactoNum + ' .intensidadCasillero').hide();
			$('#artefacto' + artefactoNum + ' .esHorno').prop("disabled", true);
			$('#artefacto' + artefactoNum + ' .esIntensidad').prop("disabled", true);
			$('#artefacto' + artefactoNum + ' .intensidad').val(0);
			$('#artefacto' + artefactoNum + ' .intensidad').removeClass('text-dark').addClass('text-secondary');
			$('#artefacto' + artefactoNum + ' .temperatura').val(null);	
		}
		else{
			$('#intensidadTitulo').show();
			$('#artefacto' + artefactoNum + ' .minutosCasillero').show();
			$('#artefacto' + artefactoNum + ' .temperaturaCasillero').hide();
			$('#artefacto' + artefactoNum + ' .intensidadCasillero').show();
			$('#artefacto' + artefactoNum + ' .esHorno').prop("disabled", true);
			$('#artefacto' + artefactoNum + ' .esIntensidad').prop("disabled", false);
			$('#artefacto' + artefactoNum + ' .artefactosUtilizadosEsHorno').val(false);
			$('#artefacto' + artefactoNum + ' .temperatura').val(null);	
			$('#artefacto' + artefactoNum + ' .intensidad').val(0);
			$('#artefacto' + artefactoNum + ' .intensidad').removeClass('text-dark').addClass('text-secondary');
		}
	}	
};

function cambiaColorValidado(casillero) {
	console.log('Cambio en color')
	$(casillero).removeClass('alert-danger');
};

function agregaIngrediente() {
	console.log('Agregando ingrediente...');
	var numIngrediente = parseInt($('#cantidadDeIngredientes').val());	
	console.log('cantidad de ingredientes antes: ' + numIngrediente);
	var numOrigen = numIngrediente;
	numIngrediente++;
	var id = 'ingrediente' + numOrigen;	
	var nuevoId = 'ingrediente' + numIngrediente;
	$("#" + id).clone().attr('id', nuevoId).insertAfter("#" + id);	
	$('#' + nuevoId).attr('name', nuevoId);
	$('#' + nuevoId).attr('orden', numIngrediente);	
	$('#' + nuevoId + ' .producto').attr('id', 'producto' + numIngrediente);
	$('#' + nuevoId + ' .cantidadIngrediente').attr('id', 'cantidadIngrediente' + numIngrediente);
	$('#' + nuevoId + ' .unidadDeMedida').attr('id', 'unidadDeMedida' + numIngrediente);	
	$('#' + nuevoId + ' .producto').attr('orden', numIngrediente);
	$('#' + nuevoId + ' .btnModal').attr('orden', numIngrediente);
	$('#' + nuevoId + ' .producto').attr('name', 'ingredientes['+ numOrigen +'].productoId');
	$('#' + nuevoId + ' .cantidadIngrediente').attr('name', 'ingredientes['+ numOrigen +'].cantidad');
	$('#' + nuevoId + ' .unidadDeMedida').attr('name', 'ingredientes['+ numOrigen +'].unidadDeMedida');
	$('#' + nuevoId + ' .producto').removeClass('text-dark alert-danger').addClass('text-secondary');
	$('#' + nuevoId + ' .cantidadIngrediente').removeClass('alert-danger');
	$('#' + nuevoId + ' .unidadDeMedida').removeClass('text-dark alert-danger').addClass('text-secondary');
	$('#' + nuevoId + ' .ingredienteTitulo').attr('name', 'ingredienteTitulo' + numIngrediente);
	$('#' + nuevoId + ' .ingredienteTitulo').text('Ingrediente ' + numIngrediente);
	$('#' + nuevoId + ' .btnIngrediente').attr('id', 'btnIngrediente' + numIngrediente);
	$('#' + nuevoId + ' .btnIngrediente').attr('name', 'btnIngrediente' + numIngrediente);
	$('#' + nuevoId + ' .btnIngrediente').attr('orden', numIngrediente);
	$('#' + nuevoId + ' .btnIngrediente').attr('ingrediente', 0);
	$('#' + nuevoId + ' .producto').val(0);
	$('#' + nuevoId + ' .cantidadIngrediente').val(null);
	$('#' + nuevoId + ' .unidadDeMedida').val(0);
	
	if(esEdicion){
		$('#' + nuevoId + ' .ingredienteIdRecetaId').attr('name', 'ingredientes['+ numOrigen +'].recetaId');
		$('#' + nuevoId + ' .ingredienteIdReceta').attr('name', 'ingredientes['+ numOrigen +'].receta');
	}	
	$('#cantidadDeIngredientes').val(numIngrediente);	
	if(numIngrediente > 1){
		$('.btnIngrediente').removeClass('disabled');
	}
	console.log('cantidad de ingredientes resultante: ' + numIngrediente);
	console.log('--Fila Agregada--');
};

function eliminaIngrediente(esteBtn) {
	
	console.log('Eliminando...')
	var iNum = parseInt($(esteBtn).attr('ingrediente'));
	console.log('INGREDIENTE ID:' + iNum);
	
	if(iNum>0){
		var index = $(esteBtn).attr('orden') - 1;
		var url = '/receta/eliminaIngrediente/' + index;
		var form = $('<form/>', {action: url});
		$('body').append(form);
		form.append($("<div>",{ class:'escondidos'}));
		replicaFormReceta();		
		form.submit();
	}
	else{
		var numIngrediente = parseInt($('#cantidadDeIngredientes').val());
		numIngrediente--;
		var filaNum = $(esteBtn).attr('orden');
	
		var idAEliminar = 'ingrediente' + filaNum;
		$('#' + idAEliminar).remove();
		
		var i = 1;
		var index = i-1;
		$("#cuerpoIngredientes .card").each(function() {
			$(this).attr('id', 'ingrediente' + i);
			$(this).attr('name', 'ingrediente' + i);
			$(this).attr('orden', i);
			$('#ingrediente' + i + ' .producto').attr('orden', i);
			$('#ingrediente' + i + ' .producto').attr('id', 'producto' + i);
			$('#ingrediente' + i + ' .producto').attr('name', 'ingredientes['+ index +'].productoId');			
			$('#ingrediente' + i + ' .cantidadIngrediente').attr('name', 'ingredientes['+ index +'].cantidad');
			$('#ingrediente' + i + ' .unidadDeMedida').attr('name', 'ingredientes['+ index +'].unidadDeMedida');
			$('#ingrediente' + i + ' .btnModal').attr('orden', i);	
			$('#ingrediente' + i + ' .ingredienteTitulo').attr('name', 'ingredienteTitulo' + i);
			$('#ingrediente' + i + ' .ingredienteTitulo').text('Ingrediente ' + i);
			$('#ingrediente' + i + ' .btnIngrediente').attr('id', 'btnIngrediente' + i);
			$('#ingrediente' + i + ' .btnIngrediente').attr('name', 'btnIngrediente' + i);
			$('#ingrediente' + i + ' .btnIngrediente').attr('orden', i);	
			i++
			index++
		});	
			
		$('#cantidadDeIngredientes').val(numIngrediente);	
		if(numIngrediente == 1){
			$('.btnIngrediente').addClass('disabled');
		}
	}	
};

function agregaPaso() {
	console.log('Agregando instrucción...')

	var numPaso = parseInt($('#cantidadDeInstrucciones').val());
	console.log('cantidad de pasos antes: ' + numPaso);
	var numOrigen = numPaso;
	numPaso++;
	
	var id = 'instruccion' + numOrigen;
	var nuevoId = 'instruccion' + numPaso;

	$("#" + id).clone().attr('id', nuevoId).insertAfter("#" + id);
	$('#' + nuevoId).attr('orden', numPaso);
	$('#' + nuevoId).attr('name', 'instruccion' + numPaso);
	$('#' + nuevoId + ' .pasoTitulo').attr('name', 'pasoTitulo' + numPaso);
	$('#' + nuevoId + ' .pasoTitulo').text('Paso ' + numPaso);
	$('#' + nuevoId + ' .pasoTexto').attr('name', 'instrucciones[' + numOrigen +'].instruccion');
	$('#' + nuevoId + ' .instruccionOrden').attr('name', 'instrucciones[' + numOrigen +'].orden');
	$('#' + nuevoId + ' .instruccionOrden').val(numPaso);
	$('#' + nuevoId + ' .pasoTexto').attr('id', 'pasoTexto' + numPaso);
	$('#' + nuevoId + ' .pasoTexto').removeClass('alert-danger');
	$('#' + nuevoId + ' .pasoTexto').attr('aria-label', 'Paso ' + numPaso);
	$('#' + nuevoId + ' .pasoTexto').text(null);
	$('#' + nuevoId + ' .pasoTexto').val(null);
	$('#' + nuevoId + ' .captura').attr('name', 'pasoFoto' + numPaso);
	$('#' + nuevoId + ' .captura').attr('id', 'pasoFoto' + numPaso);
	$('#' + nuevoId + ' .captura').val(null);
	$('#' + nuevoId + ' .contieneFoto').attr('name', 'contieneFoto' + numPaso);
	$('#' + nuevoId + ' .contieneFoto').attr('id', 'contieneFoto' + numPaso);
	$('#' + nuevoId + ' .contieneFoto').val('false');
	$('#' + nuevoId + ' .imagen').attr('src', null);
	$('#' + nuevoId + ' .recuadroFoto').hide();
	$('#' + nuevoId + ' .btnInstruccion').attr('name', 'btnInstruccion' + numPaso);
	$('#' + nuevoId + ' .btnInstruccion').attr('id', 'btnInstruccion' + numPaso);
	$('#' + nuevoId + ' .btnInstruccion').attr('orden', numPaso);	
	$('#' + nuevoId + ' .btnMic').attr('id', 'btnMic' + numPaso);	
	$('#cantidadDeInstrucciones').val(numPaso);	
	if(numPaso > 1){
		$('.btnInstruccion').removeClass('disabled');
	}
};

function eliminaPaso(esteBtn) {
	console.log('Eliminando...')
	var iNum = parseInt($(esteBtn).attr('instruccion'));
	console.log('INSTRUCCION ID:' + iNum);
	
	if(iNum>0){
		var index = $(esteBtn).attr('orden') - 1;
		var url = '/receta/eliminaInstruccion/' + index;
		var form = $('<form/>', {action: url});
		$('body').append(form);
		form.append($("<div>",{ class:'escondidos'}));
		replicaFormReceta();		
		form.submit();
	}
	else{	
		var numPaso = parseInt($('#cantidadDeInstrucciones').val());
		numPaso--;
		var filaNum = $(esteBtn).attr('orden');
	
		var idAEliminar = 'instruccion' + filaNum;
		$('#' + idAEliminar).remove();
		
		var i = 1;
		var index = i-1
		$("#cuerpoInstrucciones .card").each(function() {
			$(this).attr('id', 'instruccion' + i);
			$(this).attr('name', 'instruccion' + i);
			$(this).attr('orden', i);
			$('#instruccion' + i + ' .pasoTitulo').attr('name', 'pasoTitulo' + i);
			$('#instruccion' + i + ' .pasoTitulo').text('Paso ' + i);
			$('#instruccion' + i + ' .pasoTexto').attr('name', 'instrucciones['+ index +'].instruccion');
			$('#instruccion' + i + ' .pasoTexto').attr('id', 'pasoTexto' + i);
			$('#instruccion' + i + ' .captura').attr('id', 'pasoFoto' + i);
			$('#instruccion' + i + ' .captura').attr('name', 'pasoFoto' + i);
			$('#instruccion' + i + ' .contieneFoto').attr('id', 'contieneFoto' + i);
			$('#instruccion' + i + ' .contieneFoto').attr('name', 'contieneFoto' + i);
			$('#instruccion' + i + ' .instruccionOrden').attr('name', 'instrucciones['+ index +'].orden');
			$('#instruccion' + i + ' .instruccionOrden').val(i);
			$('#instruccion' + i + ' .btnInstruccion').attr('orden', i);
			$('#instruccion' + i + ' .btnInstruccion').attr('id', 'btnInstruccion' + i);
			$('#instruccion' + i + ' .btnInstruccion').attr('name', 'btnInstruccion' + i);
			$('#instruccion' + i + ' .btnMic').attr('id', 'btnMic' + i);
			i++
			index++
		});
		$('#cantidadDeInstrucciones').val(numPaso);
		if(numPaso == 1){
			$('.btnInstruccion').addClass('disabled');
		}
	}
};

function agregaArtefacto() {
	console.log('Agregando artefacto...')
	var numArtefacto = parseInt($('#cantidadDeArtefactos').val());
	console.log('cantidad de artefactos antes: ' + numArtefacto);
	var numOrigen = numArtefacto;
	numArtefacto++;
	
	var id = 'artefacto' + numOrigen;
	var nuevoId = 'artefacto' + numArtefacto;

	$("#" + id).clone().attr('id', nuevoId).insertAfter("#" + id);
	$('#' + nuevoId).attr('orden', numArtefacto);
	$('#' + nuevoId).attr('name', 'artefacto' + numArtefacto);
	$('#' + nuevoId + ' .nombreArtefacto').attr('id', 'artefactoNombre' + numArtefacto);
	$('#' + nuevoId + ' .minutosArtefacto').attr('id', 'minutosArtefacto' + numArtefacto);
	$('#' + nuevoId + ' .intensidad').attr('id', 'intensidad' + numArtefacto);
	$('#' + nuevoId + ' .temperatura').attr('id', 'temperatura' + numArtefacto);
	$('#' + nuevoId + ' .unidadTemperatura').attr('id', 'unidadTemperatura' + numArtefacto);
	$('#' + nuevoId + ' .nombreArtefacto').attr('orden', numArtefacto);
	$('#' + nuevoId + ' .btnModal').attr('orden', numArtefacto);
	$('#' + nuevoId + ' .nombreArtefacto').attr('name', 'artefactosUtilizados[' + numOrigen + '].artefactoId');
	$('#' + nuevoId + ' .minutosArtefacto').attr('name', 'artefactosUtilizados[' + numOrigen + '].minutosDeUso');
	$('#' + nuevoId + ' .intensidad').attr('name', 'artefactosUtilizados[' + numOrigen + '].intensidadDeUso');
	$('#' + nuevoId + ' .temperatura').attr('name', 'artefactosUtilizados[' + numOrigen + '].temperatura');
	$('#' + nuevoId + ' .unidadTemperatura').attr('name', 'artefactosUtilizados[' + numOrigen + '].unidadDeTemperatura');
	$('#' + nuevoId + ' .artefactosUtilizadosEsHorno').attr('name', 'artefactosUtilizados[' + numOrigen + '].esHorno');
	$('#' + nuevoId + ' .intensidad').removeClass('text-dark alert-danger').addClass('text-secondary');
	$('#' + nuevoId + ' .nombreArtefacto').removeClass('text-dark alert-danger').addClass('text-secondary');
	$('#' + nuevoId + ' .temperatura').removeClass('alert-danger');
	$('#' + nuevoId + ' .minutosArtefacto').removeClass('alert-danger');
	$('#' + nuevoId + ' .nombreArtefacto').val(0); 	 	
	$('#' + nuevoId + ' .minutosArtefacto').val(null); 
	$('#' + nuevoId + ' .intensidad').val(0); 
	$('#' + nuevoId + ' .temperatura').val(null); 
	$('#' + nuevoId + ' .intensidadCasillero').hide(); 
	$('#' + nuevoId + ' .temperaturaCasillero').hide(); 
	$('#' + nuevoId + ' .esIntensidad').prop("disabled", true);
	$('#' + nuevoId + ' .esHorno').prop("disabled", true);
	
	$('#' + nuevoId + ' .artefactoTitulo').attr('name', 'artefactoTitulo' + numArtefacto);
	$('#' + nuevoId + ' .artefactoTitulo').text('Artefacto ' + numArtefacto);
	$('#' + nuevoId + ' .btnArtefacto').attr('name', 'btnArtefacto' + numArtefacto);
	$('#' + nuevoId + ' .btnArtefacto').attr('id', 'btnArtefacto' + numArtefacto);
	$('#' + nuevoId + ' .btnArtefacto').attr('orden', numArtefacto);
	$('#cantidadDeArtefactos').val(numArtefacto);
	
	if(numArtefacto > 1){
		$('.btnArtefacto').removeClass('disabled');
	}
};

function eliminaArtefacto(esteBtn) {
	console.log('Eliminando...')
	var iNum = parseInt($(esteBtn).attr('artefacto'));
	console.log('ARTEFACTO ID:' + iNum);
	
	if(iNum>0){
		var index = $(esteBtn).attr('orden') - 1;
		var url = '/receta/eliminaArtefactoEnUso/' + index;
		var form = $('<form/>', {action: url});
		$('body').append(form);
		form.append($("<div>",{ class:'escondidos'}));
		replicaFormReceta();		
		form.submit();
	}
	else{
		var numArtefacto = parseInt($('#cantidadDeArtefactos').val());
		numArtefacto--;
		var filaNum = $(esteBtn).attr('orden');	
		var idAEliminar = 'artefacto' + filaNum;
		$('#' + idAEliminar).remove();
		
		var i = 1;
		var index = i-1;
		$("#cuerpoArtefactos .card").each(function() {
			$(this).attr('id', 'artefacto' + i);
			$(this).attr('name', 'artefacto' + i);
			$(this).attr('orden', i);					
			$('#artefacto' + i + ' .nombreArtefacto').attr('orden', i);
			
			$('#artefacto' + i + ' .nombreArtefacto').attr('name', 'artefactosUtilizados[' + index + '].artefactoId');			
			$('#artefacto' + i + ' .minutosArtefacto').attr('name', 'artefactosUtilizados[' + index + '].minutosDeUso');
			$('#artefacto' + i + ' .intensidad').attr('name', 'artefactosUtilizados[' + index + '].intensidadDeUso');
			$('#artefacto' + i + ' .temperatura').attr('name', 'artefactosUtilizados[' + index + '].temperatura');
			$('#artefacto' + i + ' .unidadTemperatura').attr('name', 'artefactosUtilizados[' + index + '].unidadDeTemperatura');
				
			$('#artefacto' + i + ' .artefactoTitulo').attr('name', 'artefactoTitulo' + i);
			$('#artefacto' + i + ' .artefactoTitulo').text('Artefacto ' + i);
			$('#artefacto' + i + ' .btnArtefacto').attr('orden', i);
			$('#artefacto' + i + ' .btnArtefacto').attr('id', 'btnArtefacto' + i);
			$('#artefacto' + i + ' .btnArtefacto').attr('name', 'btnArtefacto' + i);
			i++
			index++
		});
		
		$('#cantidadDeArtefactos').val(numArtefacto);	
		if(numArtefacto == 1){
			$('.btnArtefacto').addClass('disabled');
		}
	}
};

function verifica() {
	
	console.log('--VALIDANDO FORMULARIO JAVASCRIPT--');

	var hayErrorEnNombre = false;
	var hayErrorEnPorciones = false;
	var hayErrorEnIngrediente = false;
	var hayErrorEnArtefacto = false;
	var hayErrorEnInstruccion = false;

	if ($('#nombre').val().length == 0) {
		$('#nombre').addClass('alert-danger');
		$('#errorNombre').show();
		hayErrorEnNombre = true;
	} 
	else {
		$('#nombre').removeClass('alert-danger');
		$('#errorNombre').hide();
	};
	
	if ($('#porciones').val().length == 0) {
		$('#porciones').addClass('alert-danger');
		$('#errorPorciones').show();
		hayErrorEnPorciones = true;
	} 
	else {
		$('#porciones').removeClass('alert-danger');
		$('#errorPorciones').hide();
	};
	
	console.log('SIGUE CON LISTA DE INGREDIENTES...');
	
	$("#cuerpoIngredientes " + contenedor).each(function() {
		var id = $(this).attr('id');
		console.log('ID: ' + id);
		
		var orden = $(this).attr('orden');
		console.log('ORDEN: ' + orden);
		
		console.log('PRODUCTO VAL: ' + $('#' + id + ' .producto').val());
		console.log('ID: ' + id);	

		if ($('#' + id + ' .producto').val() == null || $('#' + id + ' .producto').val() == 0) {
			$('#' + id + ' .producto').addClass('alert-danger');
			hayErrorEnIngrediente = true;
		}
		else {
			$('#' + id + ' .producto').removeClass('alert-danger');
		};

		if ($('#' + id + ' .cantidadIngrediente').val() == null || $('#' + id + ' .cantidadIngrediente').val() == 0) {
			$('#' + id + ' .cantidadIngrediente').addClass('alert-danger');
			hayErrorEnIngrediente = true;
		}
		else {
			$('#' + id + ' .cantidadIngrediente').removeClass('alert-danger');
		};

		if ($('#' + id + ' .unidadDeMedida').val() == null) {
			$('#' + id + ' .unidadDeMedida').addClass('alert-danger');
			hayErrorEnIngrediente = true;
		}
		else {
			$('#' + id + ' .unidadDeMedida').removeClass('alert-danger');
		}
	});
	
	$("#cuerpoArtefactos " + contenedor).each(function() {
		var id = $(this).attr('id');
		var orden = $(this).attr('orden');

		console.log('ORDEN: ' + orden);
		console.log('ARTEFACTO VAL: ' + $('#' + id + ' .nombreArtefacto').val());
		console.log('ID: ' + id);	
		
		var nombreNulo = $('#' + id + ' .nombreArtefacto').val() == null;
		var minutosNulo = $('#' + id + ' .minutosArtefacto').val() == 0;
		var intensidadNulo = $('#' + id + ' .intensidad').val() == null;
		var temperaturaNulo = $('#' + id + ' .temperatura').val() == 0;
		
		console.log('nombreNulo: ' + nombreNulo);
		console.log('minutosNulo: ' + minutosNulo);
		console.log('intensidadNulo: ' + intensidadNulo);
		console.log('temperaturaNulo: ' + temperaturaNulo);
		
		if (nombreNulo && minutosNulo && intensidadNulo && temperaturaNulo) {						
			hayErrorEnArtefacto = false;
			$('#' + id + ' .artefactoSet').addClass("aDesactivar");
		}
		else {
			if (nombreNulo) {
			hayErrorEnArtefacto = true;
			$('#' + id + ' .nombreArtefacto').addClass('alert-danger');
			}
			else {
				$('#' + id + ' .nombreArtefacto').removeClass('alert-danger');
			}
			
			if (minutosNulo) {
			hayErrorEnArtefacto = true;
			$('#' + id + ' .minutosArtefacto').addClass('alert-danger');
			}
			else {
				$('#' + id + ' .minutosArtefacto').removeClass('alert-danger');
			}
			
			if (intensidadNulo && temperaturaNulo) {
			hayErrorEnArtefacto = true;
			$('#' + id + ' .intensidad').addClass('alert-danger');
			$('#' + id + ' .temperatura').addClass('alert-danger');
			}
			else {
				$('#' + id + ' .intensidad').removeClass('alert-danger');
				$('#' + id + ' .temperatura').removeClass('alert-danger');
			}		
		}		
	});		
	
	if ($('#cantidadDeInstrucciones').val() == 1) {
		if($('#pasoTexto1').val() == 0){
			$('.instruccionesSet').addClass("aDesactivar");
		}
	}
	else if ($('#cantidadDeInstrucciones').val() > 1) {
		$("#cuerpoInstrucciones " + contenedor).each(function() {
			var id = $(this).attr('id');
			var orden = $(this).attr('orden');
	
			console.log('ORDEN: ' + orden);
			console.log('INSTRUCCION VAL: ' + $('#' + id + ' .pasoTitulo').val());
			console.log('ID: ' + id);	
			
			var pasoNulo = $('#' + id + ' .pasoTexto').val() == 0;
			
			console.log('pasoNulo: ' + pasoNulo);
			
			if (pasoNulo) {
				hayErrorEnInstruccion = true;
				$('#' + id + ' .pasoTexto').addClass('alert-danger');
				}
			else {
				$('#' + id + ' .pasoTexto').removeClass('alert-danger');
			}
		});		
	}
		
	if (hayErrorEnNombre == false && 
	    hayErrorEnPorciones == false &&
		hayErrorEnIngrediente == false &&
		hayErrorEnArtefacto == false &&
		hayErrorEnInstruccion == false) {
		$(' .aDesactivar').prop("disabled", true);	
		$('#formulario').submit();
	}
	else {
		return false;
	}
};

function replicaFormReceta() {
	var inputsReceta = $('#formulario').serializeArray();
	jQuery.each( inputsReceta, function( i, inputReceta ) {
		$(document.createElement('input'))
		.attr('name', inputReceta.name + 'Modal')
		.attr('type', 'hidden')
		.val(inputReceta.value)
		.appendTo('.escondidos');
		console.log(inputReceta.name + ': ' + inputReceta.value);
    });	
}

function validaPrecioProducto(){
	
	var hayErrorEnPrecio = false;
	
	if ($('#precio').val().length == 0) {
		$('#precio').addClass('alert-danger');
		$('#precioProductoError').text('El precio no puede estar en blanco...');
		$('#precioProductoError').show();
		hayErrorEnPrecio = true;
	} 
	else if ($.isNumeric($('#precio').val()) == false || $('#precio').val() < 0) {
		$('#precio').addClass('alert-danger');
		$('#precioProductoError').text('El precio solo puede contener valores numericos positivos...');
		$('#precioProductoError').show();
		hayErrorEnPrecio = true;
	}
	else {
		$('#precio').removeClass('alert-danger');
		$('#precioProductoError').hide();
		hayErrorEnPrecio = false;
	};
			
	console.log('Error en PRECIO :' + hayErrorEnPrecio);
	
	return hayErrorEnPrecio;
}

function validaNombreProducto(){
	
	var hayErrorEnNombre = false;
	
	if ($('#nombreProducto').val().length == 0) {
		$('#nombreProducto').addClass('alert-danger');
		$('#nombreProductoError').text('El nombre no puede estar en blanco');
		$('#nombreProductoError').show();
		hayErrorEnNombre = true;
	} 
	else if ($('#nombreProducto').val().length > 0) {
		var textoProducto = $('#nombreProducto').val();
		$("#producto1 option").each(function() {
			var textoOption = $(this).text();
			if( textoOption == textoProducto){
				$('#nombreProducto').addClass('alert-danger');
				$('#nombreProductoError').text('Ya existe un producto con ese nombre');
				$('#nombreProductoError').show();
				hayErrorEnNombre = true;
				return false;
			}
		});
	}
	else {
		$('#nombreProducto').removeClass('alert-danger');
		$('#nombreProductoError').hide();
		hayErrorEnNombre = false;
	};
			
	console.log('Error en NOMBRE :' + hayErrorEnNombre);
	
	return hayErrorEnNombre;
}

function validaNombreArtefacto(){
	
	var hayErrorEnNombre = false;
	
	if ($('#nombreArtefacto').val().length == 0) {
		$('#nombreArtefacto').addClass('alert-danger');
		$('#nombreArtefactoError').text('El nombre no puede estar en blanco');
		$('#nombreArtefactoError').show();
		hayErrorEnNombre = true;
	} 
	else if ($('#nombreArtefacto').val().length > 0) {
		var textoArtefacto = $('#nombreArtefacto').val();
		$("#artefactoNombre1 option").each(function() {
			var textoOption = $(this).text();
			if( textoOption == textoArtefacto){
				$('#nombreArtefacto').addClass('alert-danger');
				$('#nombreArtefactoError').text('Ya existe un artefacto con ese nombre');
				$('#nombreArtefactoError').show();
				hayErrorEnNombre = true;
				return false;
			}
		});
	}
	else {
		$('#nombreArtefacto').removeClass('alert-danger');
		$('#nombreArtefactoError').hide();
		hayErrorEnNombre = false;
	};
			
	console.log('Error en Artefacto Nombre :' + hayErrorEnNombre);
	
	return hayErrorEnNombre;
}

function validaCantidadProducto(){
	
	var hayErrorEnCantidad = false;
	
	if ($('#cantidad').val().length == 0) {
		$('#cantidad').addClass('alert-danger');
		$('#cantidadProductoError').text('La cantidad no puede estar en blanco...');
		$('#cantidadProductoError').show();
		hayErrorEnCantidad = true;
	} 
	else if ($.isNumeric($('#cantidad').val()) == false || $('#cantidad').val() < 0) {
		$('#cantidad').addClass('alert-danger');
		$('#cantidadProductoError').text('La cantidad solo puede contener valores numericos positivos...');
		$('#cantidadProductoError').show();
		hayErrorEnCantidad = true;
	}
	else {
		$('#cantidad').removeClass('alert-danger');
		$('#cantidadProductoError').hide();
		hayErrorEnCantidad = false;
	};
			
	console.log('Error en CANTIDAD :' + hayErrorEnCantidad);
	
	return hayErrorEnCantidad;
}

function validaConsumoEnergeticoArtefacto(){
	
	var hayErrorEnConsumo = false;
	
	if ($('#consumoEnergetico').val().length == 0) {
		$('#consumoEnergetico').addClass('alert-danger');
		$('#consumoEnergeticoError').text('El Consumo Energético no puede estar en blanco...');
		$('#consumoEnergeticoError').show();
		hayErrorEnConsumo = true;
	} 
	else if ($.isNumeric($('#consumoEnergetico').val()) == false || $('#consumoEnergetico').val() < 0) {
		$('#consumoEnergetico').addClass('alert-danger');
		$('#consumoEnergeticoError').text('El Consumo Energético solo puede contener valores numericos positivos...');
		$('#consumoEnergeticoError').show();
		hayErrorEnConsumo = true;
	}
	else {
		$('#consumoEnergetico').removeClass('alert-danger');
		$('#consumoEnergeticoError').hide();
		hayErrorEnConsumo = false;
	};
			
	console.log('Error en consumoEnergetico :' + hayErrorEnConsumo);
	
	return hayErrorEnConsumo;
}

function validaUnidadProducto(){
	
	var hayErrorEnUnidad = false;
	
	if ($('#unidadDeMedida').val() == 0 || $('#unidadDeMedida').val() == null) {
		$('#unidadDeMedida').addClass('alert-danger');
		$('#unidadProductoError').text('La unidad no puede estar en blanco...');
		$('#unidadProductoError').show();
		hayErrorEnUnidad = true;
	} 
	else {
		$('#unidadDeMedida').removeClass('alert-danger');
		$('#unidadProductoError').hide();
		hayErrorEnUnidad = false;
	};
			
	console.log('Error en UNIDAD :' + hayErrorEnUnidad);
	
	return hayErrorEnUnidad;
}

function validaUnidadConsumoArtefacto(){
	
	var hayErrorEnUnidad = false;
	
	if ($('#unidadDeConsumo').val() == 0 || $('#unidadDeConsumo').val() == null) {
		$('#unidadDeConsumo').addClass('alert-danger');
		$('#unidadDeConsumoError').text('Debe seleccionar una unidad de medida de consumo...');
		$('#unidadDeConsumoError').show();
		hayErrorEnUnidad = true;
	} 
	else {
		$('#unidadDeConsumo').removeClass('alert-danger');
		$('#unidadDeConsumoError').hide();
		hayErrorEnUnidad = false;
	};
			
	console.log('Error en UNIDAD :' + hayErrorEnUnidad);
	
	return hayErrorEnUnidad;
}

function validaDesperdicioProducto(){
	
	var hayErrorEnDesperdicio = false;
	
	if ($('#desperdicio').val().length == 0) {
		$('#desperdicio').addClass('alert-danger');
		$('#desperdicioError').text('El desperdicio no puede estar en blanco...');
		$('#desperdicioError').show();
		hayErrorEnDesperdicio = true;
	} 
	else if ($.isNumeric($('#desperdicio').val()) == false || 
			 $('#desperdicio').val() < 0 ||
			 $('#desperdicio').val() > 99) {
		$('#desperdicio').addClass('alert-danger');
		$('#desperdicioError').text('El desperdicio solo puede contener valores numericos positivos menores a 100...');
		$('#desperdicioError').show();
		hayErrorEnDesperdicio = true;
	}
	else {
		$('#desperdicio').removeClass('alert-danger');
		$('#desperdicioError').hide();
		hayErrorEnDesperdicio = false;
	};
			
	console.log('Error en DESPERDICIO :' + hayErrorEnDesperdicio);
	
	return hayErrorEnDesperdicio;
}

function verificaProducto() {
	
	console.log('--VALIDANDO FORMULARIO PRODUCTO JAVASCRIPT--');
	var hayErrorEnNombre = validaNombreProducto();
	var hayErrorEnPrecio = validaPrecioProducto();
	var hayErrorEnCantidad = validaCantidadProducto();
	var hayErrorEnUnidad = validaUnidadProducto();
	var hayErrorEnDesperdicio = validaDesperdicioProducto();	
	
	if (hayErrorEnNombre == false && 
		hayErrorEnPrecio == false &&
		hayErrorEnCantidad == false &&
		hayErrorEnUnidad == false &&
		hayErrorEnDesperdicio == false) {
			
       	replicaFormReceta();
               
		$('#formProducto').submit();
	}
	else {
		return false;
	}
};

function verificaArtefacto() {
	
	console.log('--VALIDANDO FORMULARIO ARTEFACTO JAVASCRIPT--');
	var hayErrorEnNombre = validaNombreArtefacto();
	var hayErrorEnCantidad = validaConsumoEnergeticoArtefacto();
	var hayErrorEnUnidad = validaUnidadConsumoArtefacto();
	
	if (hayErrorEnNombre == false && 
		hayErrorEnCantidad == false &&
		hayErrorEnUnidad == false) {
			
       	replicaFormReceta();
               
		$('#formArtefacto').submit();
	}
	else {
		return false;
	}
};