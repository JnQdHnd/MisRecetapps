/**
 *  Código javascript para la vista de creación de recetas.
 *	Julián Quenard
 *  01-09-2021
 */
$(document).ready(function(){});
numeral.register('locale', 'es', {
    delimiters: {
        thousands: '.',
        decimal: ','
    },
    abbreviations: {
        thousand: 'k',
        million: 'm',
        billion: 'b',
        trillion: 't'
    },
    ordinal : function (number) {
		if(number === 1 || number === 3){
			return 'ero';
		}
		else if (number === 2) {
			return 'do';
		}
        else if (number === 4 || number === 5 || number === 6) {
			return 'to';
		}
		else if (number === 7 || number === 10) {
			return 'mo';
		}
		else if (number === 9) {
			return 'no';
		}
		else{
			return 'vo';
		}
	},
	currency: {
		symbol: '$'
	}
});

numeral.register('locale', 'pc', {
    delimiters: {
        thousands: '',
        decimal: '.'
    },
    abbreviations: {
        thousand: 'k',
        million: 'm',
        billion: 'b',
        trillion: 't'
    },
    ordinal : function (number) {
		if(number === 1 || number === 3){
			return 'ero';
		}
		else if (number === 2) {
			return 'do';
		}
        else if (number === 4 || number === 5 || number === 6) {
			return 'to';
		}
		else if (number === 7 || number === 10) {
			return 'mo';
		}
		else if (number === 9) {
			return 'no';
		}
		else{
			return 'vo';
		}
	},
	currency: {
		symbol: '$'
	}
});
numeral.locale('es');

var ancho = $('.contenedorPrincipal').width();
var dispositivo = $('#dispositivo').attr('dispositivo');
console.log('DISPOSITIVO: ' + dispositivo);
console.log('Ajustando ANCHO de INSTRUCCIONES a: ' + ancho);

var costoPorcion = parseFloat($('.costoPorcion').text());
$('.costoPorcion').text(formatearAMoneda(costoPorcion));
var costoTotal = costoPorcion * $('#porciones').val();
$('.costoTotal').text(formatearAMoneda(costoTotal));

$("#cuerpoCompra .productoPrecio").each(function() {
	var precio = parseFloat($(this).val());
	console.log('precio: ' + precio);
	$(this).val(formatearAMonedaSinSimbolo(precio));	
	console.log('precioFormateado: ' + precio);	
});

$("#cuerpoCompra .productoCantidadPresentacion").each(function() {
	var cantidad = parseFloat($(this).val());
	console.log('cantidad: ' + cantidad);
	$(this).val(formatearSinPunto(cantidad));	
	console.log('cantidadFormateada: ' + cantidad);	
});

if(dispositivo == 'browser'){
	$('.contenedorInstrucciones').width(ancho*0.93);
}
			
$('#porciones').keypress(function(e) {
    var keycode = (e.keyCode ? e.keyCode : e.which);
    if (keycode == '13') {
	   	calculaPorciones();
	    e.preventDefault();
	    return false;
	}
});

$('.compra').keydown(function(e) {
    var keycode = (e.keyCode ? e.keyCode : e.which);
    if (keycode == '110' || keycode == '190' || keycode == '188') {		
	   	var valor = $(this).val();
	   	if(valor.includes(',')){
			e.preventDefault();
			return false;
		}
		else{
			e.preventDefault();
	   		$(this).val(valor + ',');
	    	return false;
		}   	
	}
});
			
function calculaPorciones() {
	console.log('CALCULANDO CANTIDADES PARA NUEVA PORCION');
	var porcionesOriginal = parseInt($('#porciones').attr('porcionesOriginales'));
	console.log('porcionesOriginal : ' + porcionesOriginal);
	var porciones = $('#porciones').val();
	console.log('porciones: ' + porciones);

	$("#cuerpoIngredientes .ingrediente").each(function() {
		var id = '.' + $(this).attr('ingrediente');
		var cantidadOriginal = parseFloat($(id + ' .cantidadIngrediente').attr('cantidad'));
		console.log('cantidadOriginal : ' + cantidadOriginal);
		var cantidadFinal = porciones * cantidadOriginal / porcionesOriginal;
		console.log('cantidadFinal : ' + cantidadFinal);
		$('#porciones').attr('porcionesOriginales', porciones);


		$(id + ' .cantidadIngrediente').text(formatear(Math.round(cantidadFinal * 100) / 100));

		$(id + ' .cantidadIngrediente').attr('cantidad', cantidadFinal);

		$(id + " .unidadDeMedidaOpcion").each(function() {
			var cantidadEnEstaUnidadOriginal = parseFloat($(this).val());
			var nuevaCantidad = cantidadFinal * cantidadEnEstaUnidadOriginal / cantidadOriginal;
			$(this).val(nuevaCantidad);
		});		
		var nuevoCostoTotal = costoPorcion * porciones;
		$('.costoTotal').text(formatearAMoneda(nuevoCostoTotal));		
	});
}

function cambiaUnidad(select) {
	var cantidadNueva = $(select).val();
	console.log('¿VALOR NUEVO?: ' + cantidadNueva);

	console.log('CALCULANDO CANTIDADES PARA NUEVA PORCION');
	var esIngrediente = $(select).hasClass('unidadDeMedida');
	var esTemperatura = $(select).hasClass('unidadTemperatura');
	console.log('¿Es INGREDIENTE?: ' + esIngrediente);
	console.log('¿Es TEMPERATURA?: ' + esTemperatura);

	if (esIngrediente) {
		var id = '.' + $(select).attr('ingrediente');
		$(id + ' .cantidadIngrediente').text(formatear(Math.round(cantidadNueva * 100) / 100));
		$(id + ' .cantidadIngrediente').attr('cantidad', cantidadNueva);
	}
	else if (esTemperatura) {
		var id = '.' + $(select).attr('artefacto');
		$(id + ' .cantidadTemperatura').text(formatear(Math.round(cantidadNueva * 100) / 100));
	}

}

function formatear(numero) {
	console.log('FORMATEANDO NUMERO: ' + numero);
	var num = numeral(parseFloat(numero)).format('0,0.[0000]');	
	return num;
}

function formatearSinPunto(numero) {
	console.log('FORMATEANDO NUMERO: ' + numero);
	var num = numeral(parseFloat(numero)).format('0.[0000]');	
	return num;
}

function formatearAMoneda(numero){	
	console.log(numero);	
	var num = numeral(parseFloat(numero)).format('$ 0,0.00');	
	return num;
}

function formatearAMonedaSinSimbolo(numero){	
	console.log(numero);	
	var num = numeral(parseFloat(numero)).format('0,0.00');	
	return num;
}

function validaCompra(){
	console.log('**************VALIDANDO COMPRA***********');
	var errorEnPrecio = false;
	var errorEnCantidad = false;
	var porciones = $('#porciones').val();
	$('#porcionesDesdePrincipal').val(porciones);
	console.log('porciones: ' + porciones);
	console.log('porcionesDesdePrincipal: ' + $('#porcionesDesdePrincipal').val());
	$("#cuerpoCompra .productoPrecio").each(function() {
		$(this).removeClass('alert-danger');
		var precio = $(this).val().replace(',','.');
		precio = parseFloat(precio);
		var esNumero = validateDecimal(precio);
		console.log('esNumero: ' + esNumero);
		if(!esNumero){
			errorEnPrecio=true;
			$(this).addClass('alert-danger');
		}
		else{		
			$(this).val(formatearAMonedaSinSimbolo(precio));
		}
	});
	$("#cuerpoCompra .productoCantidadPresentacion").each(function() {
		$(this).removeClass('alert-danger');
		var cantidad = $(this).val().replace(',','.');
		cantidad = parseFloat(cantidad);
		var esNumero = validateDecimal(cantidad);
		console.log('esNumero: ' + esNumero);
		if(!esNumero){
			errorEnCantidad=true;
			$(this).addClass('alert-danger');
		}
		else{
			$(this).val(formatearSinPunto(cantidad));
		}
	});
	
	if(!errorEnPrecio && !errorEnCantidad){
		$('#formProducto').submit();
	}
}

function validateDecimal(valor) {
    var RE = /^\d*\.?\d*$/;
    if (RE.test(valor)) {
        return true;
    } else {
        return false;
    }
}

function llamarModalEliminar(casillero) {	
	console.log('LLAMANDO MODAL ELIMINAR')
	var idAEliminar = $(casillero).attr('idAEliminar');
	var hrefConPreparacion = $('.modalConPreparacion').attr('href');
	var hrefSinPreparacion = $('.modalSinPreparacion').attr('href');
	$('.modalConPreparacion').attr('href', hrefConPreparacion + idAEliminar);
	$('.modalSinPreparacion').attr('href', hrefSinPreparacion + idAEliminar);
	$("#eliminarConPreparacionesModal").modal("show");
};


