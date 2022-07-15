/**
 * @author Julián Quenard *
 * @ 01-09-2021
 */
$(document).ready(function(){});

var myCanvas = null;
var recuadroFoto = null;
var imgInput = $('.captura').get(0);
var canvasModal = $('#canvasModal').get(0);

function modalFoto(foto){
	myCanvas = $(foto).get(0);
	imgInput = $(foto).next('.captura').get(0);
	console.log('CAPTURA MODAL: ' + $(foto).next('.captura').attr('class'));	
	console.log('CANVAS MODAL: ' + $('#canvasModal').attr('class'));
	if (imgInput.files) {
		console.log('Hay Archivo');
		var imagen = imgInput.files[0];
		console.log('NOMBRE DE IMAGEN: ' + imagen.name);
		var reader = new FileReader();
		reader.readAsDataURL(imagen);
		reader.onloadend = function(e) {
			console.log('Archivo cargado...');
			var myImage = new Image(); // Creates image object
			myImage.src = e.target.result; // Assigns converted image to image object
			myImage.onload = function(ev) {
				console.log('Archivo cargado. Dibujando...');
				var contexto = canvasModal.getContext("2d"); // Creates a contect object
				var hRatio = canvasModal.height / myImage.height;
				var wRatio = canvasModal.width / myImage.widht;
				console.log('CANVAS MODAL WIDTH: ' + canvasModal.width);
				console.log('IMAGEN MODAL WIDTH: ' + myImage.widht);
				var ratio = Math.min(hRatio, wRatio);
				var centerShift_x = (canvasModal.width - myImage.width * ratio) / 2;
				var centerShift_y = (canvasModal.height - myImage.height * ratio) / 2;
				contexto.clearRect(0, 0, canvasModal.width, canvasModal.height);
				contexto.drawImage(myImage, 0, 0, myImage.width, myImage.height,
									centerShift_x, centerShift_y, myImage.width * ratio, myImage.height * ratio);		          		
				let imgData = canvasModal.toDataURL("image/jpeg", 0.75); // Assigns image base64 string in jpeg format to a variable
			}
		}
	}			
}

function sacaFoto(boton) {
	console.log('SACANDO FOTO' + $(boton).closest('.card-body').find('.captura').attr('class'))
	$(boton).closest('.card-body').find('.captura').trigger("click");
	imgInput = $(boton).closest('.card-body').find('.captura').get(0);
	myCanvas = $(boton).closest('.card-body').find('.canvas').get(0);
	$(boton).closest('.card-body').find('.recuadroFoto').show();
	$(boton).closest('.card-body').find('.recuadroTexto').addClass('me-1 pe-0');
}

imgInput.addEventListener('change', function(e) {
	console.log('Cambio en input de foto...');
	if (e.target.files) {
		console.log('Hay archivo...');
		let imageFile = e.target.files[0]; //here we get the image file
		var reader = new FileReader();
		reader.readAsDataURL(imageFile);
		reader.onloadend = function(e) {
			console.log('Archivo cargado...');
			var myImage = new Image(); // Creates image object
			myImage.src = e.target.result; // Assigns converted image to image object
			myImage.onload = function(ev) {
				console.log('Archivo cargado. Dibujando...');
				var myContext = myCanvas.getContext("2d"); // Creates a contect object
				var ratio = myCanvas.height / myImage.height;
				var w = myImage.width * ratio;				
				if (w > 300) {myCanvas.width = 300;}
				else {myCanvas.width = w;}
				var centerShift_x = (myCanvas.width - myImage.width * ratio) / 2;
				var centerShift_y = (myCanvas.height - myImage.height * ratio) / 2;
				myContext.clearRect(0, 0, myCanvas.width, myCanvas.height);
				myContext.drawImage(myImage, 0, 0, myImage.width, myImage.height,
									centerShift_x, centerShift_y, myImage.width * ratio, myImage.height * ratio);		          		
				let imgData = myCanvas.toDataURL("image/jpeg", 0.75); // Assigns image base64 string in jpeg format to a variable
			}
		}
	}
});