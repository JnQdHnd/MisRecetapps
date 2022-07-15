/**
 * @author Julián Quenard *
 * @ 01-09-2021
 */
$(document).ready(function(){});

var myCanvas = null;
var myImg = null;
var imgInput = $('.captura').get(0);
var canvasModal = $('#canvasModal').get(0);
var recuadroFoto = $('.recuadroFoto').get(0);
var contieneFoto = null;

$("#cuerpoInstrucciones .recuadroFoto").each(function() {
	myImg = $(this).find('.imagen').get(0);
	var imgSrc = $(myImg).attr('src');
	console.log('HAY FOTO??? ' + imgSrc);
	if(imgSrc.length > 0){
		console.log('HAY FOTO');
		recuadroFoto = $(this).get(0);
		imgInput = $(this).find('.captura').get(0);		
		$(recuadroFoto).show();		
	}
});

function sacaFoto(boton) {
	console.log('SACANDO FOTO')
	$(boton).closest('.card-body').find('.captura').trigger("click");
	imgInput = $(boton).closest('.card-body').find('.captura').get(0);
	myImg = $(boton).closest('.card-body').find('.imagen').get(0);
	recuadroFoto = $(boton).closest('.card-body').find('.recuadroFoto').get(0);
	contieneFoto = $(boton).closest('.card-body').find('.contieneFoto').get(0);
	$(imgInput).change(function() {
		readURL(this);
	});	
}

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onloadend = function (e) {	
			var myImage = new Image();
			myImage.src = e.target.result;			
			var ratio = myImg.height / myImage.height;			
			var w = myImage.width * ratio;
			$(myImg).width(w);
            $(myImg).attr('src', e.target.result);
        }
        reader.readAsDataURL(input.files[0]);
        $(contieneFoto).val('true');        
        $(recuadroFoto).show();
    }
}

function modalFoto(foto){
	console.log('MODAL - Llamando funcion cargarImagenModal')
	myImg = $(foto).get(0);
	imgInput = $(foto).next('.captura').get(0);
	contieneFoto = $(foto).closest('.card-body').find('.contieneFoto').get(0);
	recuadroFoto = $(foto).closest('.card-body').find('.recuadroFoto').get(0);
	cargarImagenModal();
}
	
function cargarImagenModal(){
	var srcImg = $(myImg).attr('src');
	$(canvasModal).attr('src', srcImg);
	console.log('MODAL - Cargando imagen')
}

function eliminaFoto(){
	$(myImg).attr('src', null);
	$(imgInput).val(null);
	$(canvasModal).attr('src', null);
	$(contieneFoto).val('false');
	$(recuadroFoto).hide();
	$('.btnCloseModal').click();	
}

//function cargarImagenFormulario(){
//	console.log('ABRIENDO IMAGEN FORMULARIO FUNCION')
//	let imagen = imgInput.files[0]; //here we get the image file
//	console.log('NOMBRE DE IMAGEN: ' + imagen.name);
//	var reader = new FileReader();
//	reader.onloadend = function(e) {
//		var myImage = new Image();
//		myImage.src = e.target.result;
//		var ratio = myImg.height / myImage.height;			
//		var w = myImage.width * ratio;
//		$(myImg).width(w);		
//        $(myImg).attr('src', e.target.result);
//	}
//	reader.readAsDataURL(imagen);
//}

//function cargarImagenFormularioCanvas(){
//	console.log('ABRIENDO IMAGEN FORMULARIO FUNCION')
//	let imagen = imgInput.files[0]; //here we get the image file
//	console.log('NOMBRE DE IMAGEN: ' + imagen.name);
//	var reader = new FileReader();
//	reader.readAsDataURL(imagen);
//	reader.onloadend = function(e) {
//		console.log('Archivo cargado...');
//		var myImage = new Image(); // Creates image object
//		myImage.src = e.target.result; // Assigns converted image to image object
//		myImage.onload = function() {
//			var myContext = myCanvas.getContext("2d"); // Creates a contect object
//			var ratio = myCanvas.height / myImage.height;
//			var w = myImage.width * ratio;
//			console.log('WIDTH RECUADRO FOTO: ' + $(recuadroFoto).width());	
//			if ($(recuadroFoto).width() > 305){myCanvas.width = $(recuadroFoto).width()}		
//			else if (w < 300) {myCanvas.width = w;}
//			else {myCanvas.width = $(recuadroFoto).width()};
//			var centerShift_x = (myCanvas.width - myImage.width * ratio) / 2;
//			var centerShift_y = (myCanvas.height - myImage.height * ratio) / 2;
//			myContext.clearRect(0, 0, myCanvas.width, myCanvas.height);
//			myContext.drawImage(myImage, 0, 0, myImage.width, myImage.height,
//								centerShift_x, centerShift_y, myImage.width * ratio, myImage.height * ratio);		          		
//			let imgData = myCanvas.toDataURL("image/jpeg", 0.75); // Assigns image base64 string in jpeg format to a variable
//			$(recuadroFoto).show();
//		}
//	}
//}