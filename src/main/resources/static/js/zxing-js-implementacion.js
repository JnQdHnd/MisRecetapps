$(document).ready(function(){});
const codeReader = new ZXing.BrowserBarcodeReader()
console.log('ZXing code reader initialized')
		
function seleccionaCamara(id) {
	$('#idCam').val($(id).val());
}

function escanea() { 
	if (document.getElementById('startButton') != null) {		
	    codeReader.getVideoInputDevices().then((videoInputDevices) => {
	    	const sourceSelect = document.getElementById('sourceSelect')
	    	const video = document.getElementById('video')
	    	const videoMarco = document.getElementById('videoMarco')
			var i = 1
			var cam = "cam"							
			videoInputDevices.forEach((element) => {
				var idOption = cam + i
		       	const sourceLI = document.createElement('li');
		       	const sourceA = document.createElement('a');
		       	sourceA.setAttribute("class", "dropdown-item");
		       	sourceA.setAttribute("id", idOption);
		       	sourceA.setAttribute("onClick", "seleccionaCamara(this.id);"); 		        	
		       	sourceA.text = element.label
		       	sourceA.value = element.deviceId
		       	sourceLI.appendChild(sourceA)
		       	sourceSelect.appendChild(sourceLI)
		       	i++
			});
				
			var idCam = document.getElementById('cam1').value; 
			
			if (videoInputDevices.length > 1) {
				const sourceSelectPanel = document.getElementById('sourceSelectPanel')
				sourceSelectPanel.style.display = 'block'
				idCam = document.getElementById('cam2').value;		
			}
			setTimeout(function() { codeReader.reset(); videoMarco.style.display = 'none'; result.scrollIntoView() }, 8000);
			videoMarco.style.display = 'block';
			video.scrollIntoView();
			idCam = document.getElementById('idCam').value;
			const devideId = idCam;
			codeReader.decodeFromInputVideoDevice(devideId, 'video').then((result) => {
				console.log(result)
				document.getElementById('result').textContent = result.text
				document.getElementById('result').value = result.text
				document.getElementById('codigoError').textContent = ''
				document.getElementById('codigoError').value = 'false'
				videoMarco.style.display = 'none'
				codeReader.reset();
			}).catch((err) => {
				console.error(err)
				document.getElementById('codigoError').textContent = 'Error al leer el Código, intente nuevamente...'
				document.getElementById('codigoError').value = 'true'
				document.getElementById('result').textContent = ''
				document.getElementById('result').value = ''
			})

		})
		.catch((err) => {
			console.error(err)
		})
	} 
}

