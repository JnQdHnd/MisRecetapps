/**
 *  Código javascript para la configuracion de la vista según cuál sea el dispositivo utilizado.
 *	Julián Quenard
 */
$(document).ready(function(){});

var dispositivo = $('#dispositivo').attr('dispositivo');
console.log('Configurando vista en: ' + dispositivo);
if(dispositivo == 'browser'){
	console.log('Configurando vista en BROWSER')
	$('#logoTituloInicio').addClass('fs-4');
	$('#navbarNav').addClass('justify-content-between');
	$("#navbarNav a").each(function() {
		$(this).addClass('fs-6');
	});
	$('#iniciaSesionBtn').addClass('btn btn-primary my-1');
	$('#dropdownMenuLogingLogout').addClass('btn btn-primary my-1');
	
	$('.card-title').addClass('fs-4');
	$('#headersBtns').addClass('col-auto');
	$('#headersBtns .btnNuevo').addClass('me-2');
	
	$('#seleccionaListado').addClass('mt-2');
}
else if(dispositivo == 'mobile'){
	console.log('Configurando vista en MOBILE')
	$('#logoTituloInicio').addClass('fs-2');
	$('#navIcono').addClass('fs-6');
	$('#navBrand').addClass('col-auto ');
	$('#navbarNav').addClass('text-start');
	$("#navbarNav a").each(function() {
		$(this).addClass('fs-5');
	});
	$('#iniciaSesionBtn').addClass('nav-link text-decoration-none ms-1');
	$('#dropdownMenuLogingLogout').addClass('nav-link');
	$('#cerrarSesionBtn').addClass('dropdown-menu-dark me-1');
	
	$('.card-title').addClass('fs-3');
	$('#headersBtns').addClass('col-12 my-2');
	$('#headersBtns .btnNuevo').addClass('col mx-2');
}