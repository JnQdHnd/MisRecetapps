$(document).ready(function(){});

console.log('Modernizr.speechsynthesis: ' + Modernizr.speechsynthesis);
console.log('Modernizr._domPrefixes: ' + Modernizr._domPrefixes);
console.log('BROWSER: ' + detectBrowser());
console.log('NAVIGATOR: ' + navigator.userAgent);

var browser = detectBrowser();
var esChromeEdge;
if(browser == 'Chrome' || browser == 'IE Edge'){
	esChromeEdge = true;
}

if (esChromeEdge) {
  $('.reproduceTexto').show();
  $('.megafonoInstrucciones').show();
} else {
  // not-supported
}

var SpeechRecognition = window.SpeechRecognition || webkitSpeechRecognition
var SpeechGrammarList = window.SpeechGrammarList || webkitSpeechGrammarList
var SpeechRecognitionEvent = window.SpeechRecognitionEvent || webkitSpeechRecognitionEvent

var cantidadDeInstrucciones = $('#cantidadDeInstrucciones').val();
var recognition = new SpeechRecognition();
recognition.continuous = false;
recognition.lang = 'es-ES';
var reproduciendo = false;
console.log('REPRODUCIENDO: ' + reproduciendo);

function comenzarDetener() {
	if(reproduciendo == false){
		console.log('START');
		recognition.start();
		reproduciendo = true;
		console.log('REPRODUCIENDO: ' + reproduciendo);
		$('#asistenteDeVoz').removeClass("bi bi-megaphone").addClass("bi bi-megaphone-fill").addClass("text-danger");
		$('.visor').text('...Esperando instrucción...');
		$('.visorCard').show();
		console.log('Recepción comenzada');
	}
	else{
		recognition.stop();
		console.log('STOP');
		reproduciendo = false;
		console.log('REPRODUCIENDO: ' + reproduciendo);
		$('#asistenteDeVoz').addClass("bi bi-megaphone").removeClass("bi bi-megaphone-fill").removeClass("text-danger");
		$('.visorCard').hide();
		$('.visor').text('');
		console.log('Recepción detenida');
	}  		
}

var num = 1;
var instruccionSeleccionada;
var texto = '';

recognition.onspeechend = function() {
	console.log('PARARON DE HABLAR');
}

recognition.onresult = function(event) {
	if (event.results.length > 0) {
    	instruccionSeleccionada = event.results[event.resultIndex][0].transcript;
    	instruccionSeleccionada = instruccionSeleccionada.replace('.', '').toLowerCase().trim();
    }
	console.log('Intruccion seleccionada: ' + instruccionSeleccionada);
	$('.visor').text('...' + instruccionSeleccionada + '...');
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
        case 'todo': 
        	num = num;
        	reproduceTodo();
        	break;	
        case 'detener': 
        	comenzarDetener();
        	break;        	
    }	
}

function seleccionaPasoReproduce(){
	reproduciendo = false;
	texto = '';
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
 	u.lang = 'es-ES';
 	u.onend = function() {recognition.start(); reproduciendo = true;}
	speechUtteranceChunker(u, {
	    chunkLength: 120
	}, function () {
	    recognition.start(); reproduciendo = true;
	    console.log('done');
	});
}

function reproduceTodo(){
	reproduciendo = false;
	texto = 'Paso 1: ' + $('#pasoTexto1').text();
	for(let i=2; i <= cantidadDeInstrucciones; i++){ 
		var idPasoTexto = '#pasoTexto' + i;
		texto = texto + ' Paso ' + i + ': ' + $(idPasoTexto).text();
	 }	
	console.log('texto: ' + texto);
	var u = new SpeechSynthesisUtterance();
 	u.text = texto;
 	u.lang = 'es-ES'; 	
	speechUtteranceChunker(u, {
	    chunkLength: 120
	}, function () {
	    recognition.start(); reproduciendo = true;
	    console.log('done');
	});
	u.onend = function() {recognition.start(); reproduciendo = true;}
}

recognition.onend = function() {
	if(reproduciendo){
		recognition.start();
		$('.visor').text('...recibiendo...');
		console.log('Reiniciando tras pausa involuntaria');
	}  
}

var speechUtteranceChunker = function (utt, settings, callback) {
    settings = settings || {};
    var newUtt;
    var txt = (settings && settings.offset !== undefined ? utt.text.substring(settings.offset) : utt.text);
    if (utt.voice && utt.voice.voiceURI === 'native') { // Not part of the spec
        newUtt = utt;
        newUtt.text = txt;
        newUtt.addEventListener('end', function () {
            if (speechUtteranceChunker.cancel) {
                speechUtteranceChunker.cancel = false;
            }
            if (callback !== undefined) {
                callback();
            }
        });
    }
    else {
        var chunkLength = (settings && settings.chunkLength) || 160;
        var pattRegex = new RegExp('^[\\s\\S]{' + Math.floor(chunkLength / 2) + ',' + chunkLength + '}[.!?,]{1}|^[\\s\\S]{1,' + chunkLength + '}$|^[\\s\\S]{1,' + chunkLength + '} ');
        var chunkArr = txt.match(pattRegex);

        if (chunkArr[0] === undefined || chunkArr[0].length <= 2) {
            //call once all text has been spoken...
            if (callback !== undefined) {
                callback();
            }
            return;
        }
        var chunk = chunkArr[0];
        newUtt = new SpeechSynthesisUtterance(chunk);
        var x;
        for (x in utt) {
            if (utt.hasOwnProperty(x) && x !== 'text') {
                newUtt[x] = utt[x];
            }
        }
        newUtt.addEventListener('end', function () {
            if (speechUtteranceChunker.cancel) {
                speechUtteranceChunker.cancel = false;
                return;
            }
            settings.offset = settings.offset || 0;
            settings.offset += chunk.length - 1;
            speechUtteranceChunker(utt, settings, callback);
        });
    }

    if (settings.modifier) {
        settings.modifier(newUtt);
    }
    console.log(newUtt); //IMPORTANT!! Do not remove: Logging the object out fixes some onend firing issues.
    //placing the speak invocation inside a callback fixes ordering and onend issues.
    setTimeout(function () {
        speechSynthesis.speak(newUtt);
    }, 0);
};

function detectBrowser() { 
    if((navigator.userAgent.indexOf("Opera") || navigator.userAgent.indexOf('OPR')) != -1 ) {
        return 'Opera';
    } else if(navigator.userAgent.indexOf("Edg") != -1 ){
        return 'IE Edge';
    } else if(navigator.userAgent.indexOf("Chrome") != -1 ) {
        return 'Chrome';
    } else if(navigator.userAgent.indexOf("Safari") != -1) {
        return 'Safari';
    } else if(navigator.userAgent.indexOf("Firefox") != -1 ){
        return 'Firefox';
    } else if((navigator.userAgent.indexOf("MSIE") != -1 ) || (!!document.documentMode == true )) {
        return 'IE';//crap
    } else {
        return 'Unknown';
    }
} 