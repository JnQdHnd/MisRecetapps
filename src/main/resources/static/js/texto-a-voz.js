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
// if(getBrowserInfo().includes('Chrome') || getBrowserInfo().includes('Edg')){}

var SpeechRecognition = window.SpeechRecognition || webkitSpeechRecognition
var SpeechGrammarList = window.SpeechGrammarList || webkitSpeechGrammarList
var SpeechRecognitionEvent = window.SpeechRecognitionEvent || webkitSpeechRecognitionEvent

var cantidadDeInstrucciones = $('#cantidadDeInstrucciones').val();
var instrucciones = [];
for(let i = 0; i<cantidadDeInstrucciones; i++){
	var num = i + 1;
	var titulo = 'paso ' + num;
	instrucciones.push(titulo);
}
var grammar = '#JSGF V1.0; grammar pasos; public <paso> = comenzar | siguiente | anterior | previa | previo | repetir | detener | ' + instrucciones.join(' | ') + ' ;'
var recognition = new SpeechRecognition();
var speechRecognitionList = new SpeechGrammarList();
speechRecognitionList.addFromString(grammar, 1);
recognition.grammars = speechRecognitionList;
recognition.continuous = true;
recognition.lang = 'es-ES';

var reproduciendo = false;
document.body.onclick = function() {
	if(reproduciendo == false){
		recognition.start();
		reproduciendo = true;
		console.log('Recepción comenzada');
	}
	else{
		recognition.stop();
		reproduciendo = false;
		console.log('Recepción detenida');
	}  		
}
var num = 1;
var instruccionSeleccionada

recognition.onresult = function(event) {
	for (var i = event.resultIndex; i < event.results.length; ++i) {
      	if (event.results[i].isFinal) {
			instruccionSeleccionada = event.results[i][0].transcript.trim();					
      	}
    }
	if(getBrowserInfo().includes('Edg')){
		instruccionSeleccionada = instruccionSeleccionada.replace('.', '').toLowerCase(); 
	}
	console.log('Intruccion seleccionada: ' + instruccionSeleccionada);
	switch(instruccionSeleccionada)
    {
        case 'comenzar':
        	num = 1;
        	seleccionaPasoReproduce();
        	break;
        case 'siguiente': 
        	num = num + 1;
        	seleccionaPasoReproduce();
        	break;
        case 'anterior': 
        	num = num - 1;
        	seleccionaPasoReproduce();
        	break;
        case 'previo': 
        	num = num - 1;
        	seleccionaPasoReproduce();
        	break;
        case 'previa': 
        	num = num - 1;
        	seleccionaPasoReproduce();
        	break;		
        case 'repetir': 
        	num = num;
        	seleccionaPasoReproduce();
        	break;
        case 'detener': 
        	recognition.stop();
        	break;
    }	
}

function seleccionaPasoReproduce(){
	var texto
	var idPasoTexto = '#pasoTexto' + num;
	console.log('idPasoTexto: ' + idPasoTexto);
	if(num > cantidadDeInstrucciones){
		texto = 'No hay más instrucciones para esta receta.'
		num = num - 1;
	}
	else if(num < 1){
		texto = 'No hay instrucción previa, esta es la primera.'
		num = num + 1;
	}
	else{
		texto = 'Paso ' + num + ': ' + $(idPasoTexto).text();	
	}	
	console.log('texto: ' + texto);
	var u = new SpeechSynthesisUtterance();
 	u.text = texto;
	speechSynthesis.speak(u);	
}

recognition.onspeechend = function() {
  recognition.stop();
  console.log('Recepción detenida');
}