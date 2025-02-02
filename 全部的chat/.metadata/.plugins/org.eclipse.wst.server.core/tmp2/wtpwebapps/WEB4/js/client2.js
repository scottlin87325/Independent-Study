window.onload = function(){
	var canvas, ctx;
	
	canvas = document.getElementById('myDrawer');
	ctx = canvas.getContext('2d');
	
	var webSocket = new WebSocket("ws://10.0.104.138:8080/WEB4/myserver");
	var isConnect = false;
		
	webSocket.onopen= function(event){
		isConnect = true;
	}
	webSocket.onmessage = function(event){
		var mesgObj = JSON.parse(event.data);
		if(mesgObj.isClear){
			clear();
		}else{
			if(mesgObj.isNewLine){
				newLine(mesgObj.x,mesgObj.y);
			}else{
				drawLine(mesgObj.x,mesgObj.y);
			}
		}
	}
	webSocket.onclose = function(event){
		isConnect = false;
	}
	webSocket.onerror = function(event){
			
	}
	function newLine(x,y){
		ctx.beginPath();
		ctx.lineWidth = 4;
		ctx.moveTo(x,y);
	}	
	
	function drawLine(x,y){
		ctx.lineTo(x,y);
		ctx.stroke();
	}
	
	function clear(){
		ctx.clearRect(0,0,canvas.width, canvas.height);
	}
}