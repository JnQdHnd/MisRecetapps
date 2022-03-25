$(document).ready(function(){});

var getBrowserInfo = function() {
     var ua= navigator.userAgent, tem, 
     M= ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
    if(/trident/i.test(M[1])){
         tem=  /\brv[ :]+(\d+)/g.exec(ua) || [];
        return 'IE '+(tem[1] || '');
    }
    if(M[1]=== 'Chrome'){
        tem= ua.match(/\b(OPR|Edg)\/(\d+)/);
        if(tem!= null) return tem.slice(1).join(' ').replace('OPR', 'Opera');
    }
    M= M[2]? [M[1], M[2]]: [navigator.appName, navigator.appVersion, '-?'];
    if((tem= ua.match(/version\/(\d+)/i))!= null) M.splice(1, 1, tem[1]);
    return M.join(' ');
};

var esEdgeOEsChrome = false;
if(getBrowserInfo().includes('Chrome') || getBrowserInfo().includes('Edg')){
	esEdgeOEsChrome = true;
	$('.btnMic').show();
}

var SpeechRecognition = window.SpeechRecognition || webkitSpeechRecognition
var SpeechGrammarList = window.SpeechGrammarList || webkitSpeechGrammarList
var SpeechRecognitionEvent = window.SpeechRecognitionEvent || webkitSpeechRecognitionEvent

var diagnostic;
var recognition = new SpeechRecognition();
recognition.continuous = true;
recognition.lang = 'es-ES';
var textoBase = '';
var texto = '';
var recibiendo;

function recibirVoz(esteBoton) {
	if(recibiendo){
		console.log('Esta grabando. Cancelando grabación en curso...');
		recognition.stop();
		recibiendo = false;
		$('.btnMic').attr('onclick', 'recibirVoz(this); return false;');
 		$('.iMic').addClass('bi-mic').removeClass('bi-mic-fill').removeClass('text-danger');
  		texto = '';
	}
	else{
		recibiendo = true;
		console.log('Recibiendo: ' + recibiendo);
		recognition.start();
		$(esteBoton).attr('onclick', 'detenerVoz(this); return false;');
	 	var id = $(esteBoton).attr('id');
	 	var num = id.substr(id.length - 1);
		diagnostic = $('#pasoTexto' + num);
		textoBase = diagnostic.val();
		$('#' + id + ' i').addClass('bi-mic-fill').removeClass('bi-mic').addClass('text-danger');
		console.log('Comienza recepción de voz para pasar a texto.');
	}
	
}
function detenerVoz(esteBoton) {
	recibiendo = false;
	recognition.stop();
	$(esteBoton).attr('onclick', 'recibirVoz(this); return false;');
  	var id = $(esteBoton).attr('id');
  	$('#' + id + ' i').addClass('bi-mic').removeClass('bi-mic-fill').removeClass('text-danger');
  	texto = '';
  	console.log('Finaliza recepción de voz para pasar a texto.');
}

recognition.onresult = function(event) {
	for (var i = event.resultIndex; i < event.results.length; ++i) {
      	if (event.results[i].isFinal) {
			if(getBrowserInfo().includes('Edg')){
				texto += ' ' + event.results[i][0].transcript;
			}
			else{
				if(i == 0){
					texto += event.results[i][0].transcript;
				}
				else{
					texto += ', ' + event.results[i][0].transcript;
				}
			}			
      	}
    }	
  	console.log('Texto: ' + texto);
  	if(getBrowserInfo().includes('Edg')){
		diagnostic.val(textoBase + texto.charAt(0).toUpperCase() + texto.slice(1) + ' ');
	}
	else{
		diagnostic.val(textoBase + texto.charAt(0).toUpperCase() + texto.slice(1) + '. ');
	}  	
}

recognition.onend = function() {
	if(recibiendo){
		recognition.start();
  		console.log('Reiniciando tras interrupción involuntaria.');
	}
}