window.onload = function(){
	var canvas, ctx, clear;
	var isDrag = false;
	var webSocket = new WebSocket("ws://10.0.104.138:8080/WEB4/myserver");
	var isConnect = false;
	
	webSocket.onopen= function(event){
		isConnect = true;
	}
	webSocket.onmessage = function(event){
		
	}
	webSocket.onclose = function(event){
		isConnect = false;
	}
	webSocket.onerror = function(event){
		
	}
	
	clear = document.getElementById('clear');
	canvas = document.getElementById('myDrawer');
	ctx = canvas.getContext('2d');
	
	clear.addEventListener("click",function(){
		ctx.clearRect(0,0,canvas.width, canvas.height);
		if(isConnect){
			var data={
				isClear:true,
			}
			if (isConnect){
							webSocket.send(JSON.stringify(data));
			}
		}
	})
	
	canvas.onmousedown = function(event){
		
		isDrag = true;
		var x = event.offsetX, y = event.offsetY;
		ctx.beginPath();
		ctx.lineWidth = 4;
		ctx.moveTo(x,y);
		
		var data = {
			isClear:false,
			isNewLine:true,
			x:x,
			y:y
		}
		if(isConnect){
			webSocket.send(JSON.stringify(data));	
		}
		
	}
	canvas.onmousemove = function(event){
		if(isDrag){
			var x = event.offsetX, y = event.offsetY;
			ctx.lineTo(x,y);
			ctx.stroke();
			
			var data = {
				isClear:false,
				isNewLine:false,
				x:x,
				y:y
			}				
			if(isConnect){
				webSocket.send(JSON.stringify(data));	
			}
		}
	}
	canvas.onmouseup = function(event){
		isDrag = false;
	}
}