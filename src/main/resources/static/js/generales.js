$(document).ready(function(){});

function cambiaColorSelect(casillero) {
	console.log('Cambio en color')
	var valor = $(casillero).val();
	console.log('Valor de casillero: ' +valor);
	if(valor!='0'){		
		$(casillero).removeClass('text-secondary').addClass('text-dark');
	}
};

