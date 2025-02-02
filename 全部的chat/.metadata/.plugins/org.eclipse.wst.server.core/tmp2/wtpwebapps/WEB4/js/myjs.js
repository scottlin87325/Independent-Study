window.onload = function(){
	var start = document.getElementById('start');
	var chatDiv = document.getElementById('chatDiv');
	var mesg = document.getElementById('mesg');
	var send = document.getElementById('send');
	var log = document.getElementById('log');
	
	start.style.display = 'block';
	chatDiv.style.display = 'none';
	
	var webSocket;
	start.addEventListener("click",function(){
		connect("ws://10.0.104.138:8080/WEB4/myserver")
	});
	
	send.addEventListener("click", function(){
		var message = {
			message: mesg.value
		}
		webSocket.send(JSON.stringify(message));
	});
	
	function connect(url){
		webSocket = new WebSocket(url);
		webSocket.onopen = function(event){
			console.log("onOpen");
			start.style.display = 'none';
			chatDiv.style.display = 'block';
		}
		webSocket.onclose = function(event){
			console.log("onClose");	
			start.style.display = 'block';
			chatDiv.style.display = 'none';	
		}
		webSocket.onerror = function(event){
			console.log("onError");		
		}		
		webSocket.onmessage = function(event){
			console.log("OnMsg"+event.data);
			var mesgObj = JSON.parse(event.data);
			log.innerHTML += mesgObj.message+'<br>';		
		}
	}
}