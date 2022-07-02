$(document).ready(function(){});

var productosEnFiltros = [];
var preparacionesVisibles = new Map();
var ingredientesDisponiblesEnPreparacion = new Map();
var ingredientesTotalesEnPreparaciones = new Map();

$('.selectorDeIngrediente').select2( {
    theme: "bootstrap-5",
    width: 'resolve',
    placeholder: $(this).data('placeholder'),
    allowClear: true
});

$('.selectorDeIngrediente').change(function() {
  $('.selectorDeIngrediente').removeClass('text-secondary');
  $('.select2-selection__rendered').removeClass('text-secondary');
});

function filtraPorProducto(producto) {
	console.log('AGRGANDO PRODUCTO AL FILTRADO')	
	var productoId = $('.selectorDeIngrediente').val();
	console.log('productoId: ' + productoId);
	if(productoId > 0){	
		var productoNombre = $('.selectorDeIngrediente option:selected').text();
		console.log('productoNombre: ' + productoNombre)
		$(".listadoProductosFiltro").show();
		$("#productosParaFiltro").append(
			"<div class='col-auto card m-1'>" + 
				"<div class='row'>" + 
					"<div class='col-auto'>" + 
						"<span>" + 
							productoNombre + 
						"</span>" + 
					"</div>" + 
					"<div class='col-auto'>" + 
						"<a idProducto='" + productoId + "'onclick=eliminaProductoFiltro(this); return false; class='btn-close'></a>" + 
					"</div>" + 
				"</div>" + 
			"</div>"
		);
		productosEnFiltros.push(productoId);
		filtraListadoDeRecetas();		
	};	
	$('.selectorDeIngrediente').val(null);
	$('#select2-selectorDeIngrediente-container').text($('.selectorDeIngrediente').data('placeholder')).addClass('text-secondary');	
	console.log('Cambiando valor a NULL' + $('.selectorDeIngrediente').val());
};

function filtraListadoDeRecetas() {
	console.log('FILTRANDO LISTADO');
	var arrayIsEmpty = jQuery.isEmptyObject(productosEnFiltros);
	$(".recetaId").each(function() {				
		var receta = $(this);
		var cantidadDeIngredientes = $(this).attr('cantidadDeIngredientes');
		var idReceta = $(this).attr('recetaId');
		ingredientesTotalesEnPreparaciones.set(idReceta,cantidadDeIngredientes);		
		console.log('++++ ANALIZANDO RECETA: ' + idReceta);
		var recetaFiltrada = true;		
		var ingredientesDisponibles = 0;
		$('.idProducto', receta).each(function() {
			var idTemp = $(this).attr('value');
			var productoEnArray = jQuery.inArray(idTemp, productosEnFiltros);		
			if(productoEnArray >= 0 || arrayIsEmpty){
				ingredientesDisponibles++;
				recetaFiltrada = false;
			}
		});		
		if(recetaFiltrada){
			$(this).attr('recetaFiltrada', recetaFiltrada);
			$(this).hide();	
			if(preparacionesVisibles.has(idReceta)){				
				preparacionesVisibles.delete(idReceta);
				ingredientesDisponiblesEnPreparacion.delete(idReceta);
			}	
		}
		else{
			console.log('pppp INCORPORA PREPARACION A LISTADO: ' + idReceta);			
			if(arrayIsEmpty){
				preparacionesVisibles.delete(idReceta);
				ingredientesDisponiblesEnPreparacion.delete(idReceta);
				$('.ingredienteTitulo').hide();
				$('.ingredienteData', receta).text('0' + ' / ' +  cantidadDeIngredientes).hide();
			}
			else{
				preparacionesVisibles.set(idReceta);
				ingredientesDisponiblesEnPreparacion.set(idReceta, ingredientesDisponibles);
				$('.ingredienteTitulo').show();
				$('.ingredienteData', receta).text(ingredientesDisponibles + ' / ' +  cantidadDeIngredientes).show();
			}
			$(this).attr('recetaFiltrada', recetaFiltrada);			
			$(this).show();			
		}
	});
	$(".recetaId").each(function() {
		var preparaciones = $(this).attr('tienePreparaciones');
		var tienePreparaciones = preparaciones > 0;	
		var recetaFiltrada = true;
		var idReceta = $(this).attr('recetaId');
		console.log('++++ ANALIZANDO RECETA: ' + idReceta);
		if(tienePreparaciones){			
			var receta = $(this);
			var cantidadDeIngredientes = 0;
			var ingredientesDisponibles = 0;
			$('.preparacion', receta).each(function() {
				var idTemp = $(this).attr('value');				
				cantidadDeIngredientes = cantidadDeIngredientes + parseInt(ingredientesTotalesEnPreparaciones.get(idTemp));
				console.log('#### PREPARACION: ' + idTemp);
				var preparacionEnArray = preparacionesVisibles.has(idTemp);			
				if(preparacionEnArray || arrayIsEmpty){
					ingredientesDisponibles = ingredientesDisponibles + parseInt(ingredientesDisponiblesEnPreparacion.get(idTemp));
					recetaFiltrada = false;
				}
			});			
			if(recetaFiltrada){
				$(this).attr('recetaFiltrada', recetaFiltrada);
				$(receta).hide();			
			}
			else{
				if(arrayIsEmpty){
					$('.ingredienteData', receta).text('0' + ' / ' +  cantidadDeIngredientes).hide();
				}
				else{					
					$('.ingredienteData', receta).text(ingredientesDisponibles + ' / ' +  cantidadDeIngredientes).show();
				}
				$(this).attr('recetaFiltrada', recetaFiltrada);
				$(receta).show();
			}
		}	
	});
	var soloFavoritas = $('#seleccionaListado').attr('soloFavoritas');
	if(soloFavoritas == 'true'){
		filtraFavoritas();
	}	
};

function eliminaProductoFiltro(casillero) {
	var productoId = $(casillero).attr('idProducto');
	console.log('---- PRODUCTO A RETIRAR DEL FILTRO: ' + productoId);
	var productoEnArray = jQuery.inArray(productoId, productosEnFiltros);
	if(productoEnArray >= 0){				
		productosEnFiltros.splice(productoEnArray, 1);
	}	
	filtraListadoDeRecetas();
	$(casillero).closest('.card').remove();		
	console.log($('#productosParaFiltro').children().length);
	if ($('#productosParaFiltro').children().length == 0) {	
		$(".listadoProductosFiltro").hide();
	}
	console.log('productosEnFiltros: ' + productosEnFiltros);	
}

function filtraFavoritas() {
	console.log('--favoritas FILTRANDO FAVORITAS favoritas--')
	var soloFavoritas = $('#seleccionaListado').attr('soloFavoritas');
	console.log('soloFavoritas: ' + soloFavoritas)
	if(soloFavoritas == 'true'){
		console.log('En SOLO FAVORITAS');
		$(".recetaId").each(function() {
			var recetaFiltrada = $(this).attr('recetaFiltrada');
			console.log('recetaFiltrada: ' + recetaFiltrada);
			if(recetaFiltrada == 'false'){
				console.log('En RECETA FILTRADA');
				var esFavorita = $('.esFavorita', $(this)).attr('esFavorita');
				if(esFavorita == 'false'){
					$(this).hide();
				}
			}
		});	
	}
	else{
		$(".recetaId").each(function() {
			var recetaFiltrada = $(this).attr('recetaFiltrada');
			if(recetaFiltrada == 'false'){
				var esFavorita = $('.esFavorita', $(this)).attr('esFavorita');
				var estaEscondida = $(this).is(":hidden");
				if(esFavorita == 'false' && estaEscondida){
					$(this).show();
				}
			}
		});
	} 	
}

