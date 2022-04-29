$(document).ready(function(){});

var myCanvas = null;
var recuadroFoto = null;
var imgInput = $('.captura').get(0);
var canvasModal = $('#canvasModal').get(0);
var recuadroFoto = $('.recuadroFoto').get(0);

function modalFoto(foto){
	myCanvas = $(foto).get(0);
	imgInput = $(foto).next('.captura').get(0);
	recuadroFoto = $(foto).closest('.card-body').find('.recuadroFoto').get(0);
	console.log('CAPTURA MODAL: ' + $(foto).next('.captura').attr('class'));	
	console.log('CANVAS MODAL: ' + $('#canvasModal').attr('class'));
	cargarImagenModal();
}
	
function cargarImagenModal(){
	console.log('ABRIENDO IMAGEN FUNCION')
	let imagen = imgInput.files[0]; //here we get the image file
	console.log('NOMBRE DE IMAGEN: ' + imagen.name);
	var reader = new FileReader();
	reader.readAsDataURL(imagen);
	reader.onloadend = function(e) {
		console.log('Archivo cargado...');
		var myImage = new Image(); // Creates image object
		myImage.src = e.target.result; // Assigns converted image to image object
		myImage.onload = function() {
			var canvasModal = document.getElementById('canvasModal');
			var contexto = canvasModal.getContext("2d");
			var ratioCanvas = canvasModal.width / myImage.width;
			var ancho = canvasModal.width;				
			var alto = myImage.height * ratioCanvas;
			canvasModal.height = alto;				
			console.log('RATIO: ' + ratioCanvas);
			console.log('ANCHO: ' + ancho);
			console.log('ALTO: ' + alto);	
			contexto.drawImage(myImage, 0, 0, ancho, alto);
		}
	}
}

function cargarImagenFormulario(){
	console.log('ABRIENDO IMAGEN FORMULARIO FUNCION')
	let imagen = imgInput.files[0]; //here we get the image file
	console.log('NOMBRE DE IMAGEN: ' + imagen.name);
	var reader = new FileReader();
	reader.readAsDataURL(imagen);
	reader.onloadend = function(e) {
		console.log('Archivo cargado...');
		var myImage = new Image(); // Creates image object
		myImage.src = e.target.result; // Assigns converted image to image object
		myImage.onload = function() {
			var myContext = myCanvas.getContext("2d"); // Creates a contect object
			var ratio = myCanvas.height / myImage.height;
			var w = myImage.width * ratio;
			console.log('WIDTH RECUADRO FOTO: ' + $(recuadroFoto).width());	
			if ($(recuadroFoto).width() > 305){myCanvas.width = $(recuadroFoto).width()}		
			else if (w < 300) {myCanvas.width = w;}
			else {myCanvas.width = $(recuadroFoto).width()};
			var centerShift_x = (myCanvas.width - myImage.width * ratio) / 2;
			var centerShift_y = (myCanvas.height - myImage.height * ratio) / 2;
			myContext.clearRect(0, 0, myCanvas.width, myCanvas.height);
			myContext.drawImage(myImage, 0, 0, myImage.width, myImage.height,
								centerShift_x, centerShift_y, myImage.width * ratio, myImage.height * ratio);		          		
			let imgData = myCanvas.toDataURL("image/jpeg", 0.75); // Assigns image base64 string in jpeg format to a variable
			$(recuadroFoto).show();
		}
	}
}

function sacaFoto(boton) {
	console.log('SACANDO FOTO' + $(boton).closest('.card-body').find('.captura').attr('class'))
	$(boton).closest('.card-body').find('.captura').trigger("click");
	imgInput = $(boton).closest('.card-body').find('.captura').get(0);
	console.log('IMPUT: ' + $(imgInput).attr('id'))
	myCanvas = $(boton).closest('.card-body').find('.canvas').get(0);
	recuadroFoto = $(boton).closest('.card-body').find('.recuadroFoto').get(0);
	$(imgInput).change(function() {
		cargarImagenFormulario();
	});	
}

//imgInput.addEventListener('change', function(e) {
//	console.log('Cambio en input de foto...');
//	if (e.target.files) {
//		console.log('Hay archivo...');
//		let imageFile = e.target.files[0]; //here we get the image file
//		var reader = new FileReader();
//		reader.readAsDataURL(imageFile);
//		reader.onloadend = function(e) {
//			console.log('Archivo cargado...');
//			var myImage = new Image(); // Creates image object
//			myImage.src = e.target.result; // Assigns converted image to image object
//			myImage.onload = function() {
//				console.log('Archivo cargado. Dibujando...');
//				var myContext = myCanvas.getContext("2d"); // Creates a contect object
//				var ratio = myCanvas.height / myImage.height;
//				var w = myImage.width * ratio;
//				console.log('WIDTH RECUADRO FOTO: ' + $(recuadroFoto).width());	
//				if ($(recuadroFoto).width() > 305){myCanvas.width = $(recuadroFoto).width()}		
//				else if (w < 300) {myCanvas.width = w;}
//				else {myCanvas.width = $(recuadroFoto).width()};
//				var centerShift_x = (myCanvas.width - myImage.width * ratio) / 2;
//				var centerShift_y = (myCanvas.height - myImage.height * ratio) / 2;
//				myContext.clearRect(0, 0, myCanvas.width, myCanvas.height);
//				myContext.drawImage(myImage, 0, 0, myImage.width, myImage.height,
//									centerShift_x, centerShift_y, myImage.width * ratio, myImage.height * ratio);		          		
//				let imgData = myCanvas.toDataURL("image/jpeg", 0.75); // Assigns image base64 string in jpeg format to a variable
//				$(recuadroFoto).show();
//			}
//		}
//	}
//});

function eliminaFoto(){
	myCanvas.getContext("2d").clearRect(0, 0, myCanvas.width, myCanvas.height);
	$(imgInput).val(null);
	canvasModal.getContext("2d").clearRect(0, 0, canvasModal.width, canvasModal.height);
	$(recuadroFoto).hide();
	$('.btnCloseModal').click();	
}