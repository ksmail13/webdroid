var displayer = null;
var	xy_field = null;
var	xy_text = null;
var	context = null;
var	spritesheet = new Image();

var	mode = 0;
var	dragging = false;
var	paused = true;
var	zoomx = 0;
var	zoomy = 0;
var	zoomrate = 50;
var	i = 0;
var	discs = [
		{x:0, y:0},
		{x:0, y:0}
		];

var sock = new SockJS('http://10.0.18.145:8080/myapp');

//----------------------------------------function
function windowToCanvas(displayer,x,y){
	var bbox = displayer.getBoundingClientRect();
	return {x:x - bbox.left*(displayer.width/bbox.width),
			y:y - bbox.top*(displayer.height/bbox.height)
	};
}

function drawBackground(){
	var VERTICAL_LINE_SPACING = 12;
	var	i = context.canvas.height;

	context.clearRect(0,0,displayer.width,displayer.height);
	context.strokeStyle = 'lightgray';
	context.lineWidth = 0.5;

	while(i > VERTICAL_LINE_SPACING){
		context.beginPath();
		context.moveTo(0,i);
		context.lineTo(context.canvas.width,i);
		context.stroke();
		i -= VERTICAL_LINE_SPACING;
	}
}
/*
function drawSpritesheet(){
	context.drawImage(spritesheet,0,0);
}*/

function drawGuidelines(x,y){
	context.strokeStyle = 'rgba(0,180,0,0.8)';
	context.lineWidth = 0.5;
	drawVerticalLine(x);
	drawHorizontalLine(y);
}

function updateReadout(x,y){
	xy_field.innerText = '(' + x.toFixed(0) + ', ' + y.toFixed(0) + ')';
}

function updateText(x,y){
	xy_text.innerText = xy_text.value + '(' + x.toFixed(0) + ', ' + y.toFixed(0) + ')\n';	
	xy_text.scrollTop = xy_text.scrollHeight;
	var buf = mode + zeroPad(x.toFixed(0),100) + zeroPad(y.toFixed(0),1000);
	send(buf);
}

function updateTextDouble(x1,y1,x2,y2){
	xy_text.innerText = xy_text.value + '(' + x1.toFixed(0) + ', ' + y1.toFixed(0) + '), ('
										 + x2.toFixed(0) + ', ' + y2.toFixed(0) + ')\n';	
	xy_text.scrollTop = xy_text.scrollHeight;
	var buf = mode + zeroPad(x1.toFixed(0),100) + zeroPad(y1.toFixed(0),1000)
				 + zeroPad(x2.toFixed(0),100) + zeroPad(y2.toFixed(0),1000);
	send(buf);
}

function clearText(){
	xy_text.innerText = '';
}

function zeroPad(nr,base){
  var  len = (String(base).length - String(nr).length)+1;
  return len > 0? new Array(len).join('0')+nr : nr;
}

function drawHorizontalLine(y){
	context.beginPath();
	context.moveTo(0,y+0.5);
	context.lineTo(context.canvas.width, y+0.5);
	context.stroke();
}

function drawVerticalLine(x){
	context.beginPath();
	context.moveTo(x+0.5,0);
	context.lineTo(x+0.5,context.canvas.height);
	context.stroke();
}

function drawRect(x,y){
	context.fillStyle = 'rgba(0,150,0,0.25)';
	context.strokeRect(zoomx,zoomy,x-zoomx,y-zoomy);
	context.fillRect(zoomx,zoomy,x-zoomx,y-zoomy);
}

function changeMode(i){
	mode = i;
}

function animateCall(e){
	paused = paused ? false : true;
	if(!paused){
		var loc = windowToCanvas(displayer, e.clientX, e.clientY);

		if(mode == 1) i = zoomrate-1;
		else if(mode == 2) i = 0;

		if(loc.x*loc.x + loc.y*loc.y < zoomx*zoomx + zoomy*zoomy){
			discs[0].x = loc.x;
			discs[0].y = loc.y;
			discs[1].x = zoomx;
			discs[1].y = zoomy;
			window.requestNextAnimationFrame(animate);
		}
		else if(loc.x*loc.x + loc.y*loc.y > zoomx*zoomx + zoomy*zoomy){
			discs[1].x = loc.x;
			discs[1].y = loc.y;
			discs[0].x = zoomx;
			discs[0].y = zoomy;
			window.requestNextAnimationFrame(animate);
		}
		else paused = true;
	}
}

function animate(time){
	if(!paused){
		context.clearRect(0,0,displayer.width,displayer.height);
		drawBackground();
		draw(update());

		window.requestNextAnimationFrame(animate);
	}
	else{
		context.clearRect(0,0,displayer.width,displayer.height);
		drawBackground();
	}
}

function update(){
	var loc = [
	{x:0,y:0},
	{x:0,y:0}
	];

	loc[0].x = discs[0].x + (((discs[1].x - discs[0].x)/2)/zoomrate)*i;
	loc[0].y = discs[0].y + (((discs[1].y - discs[0].y)/2)/zoomrate)*i;
	loc[1].x = discs[1].x - (((discs[1].x - discs[0].x)/2)/zoomrate)*i;
	loc[1].y = discs[1].y - (((discs[1].y - discs[0].y)/2)/zoomrate)*i;

	if(mode == 1){
		if(!i--) paused = true;
	}
	else if(mode == 2){
		if(i++ == zoomrate-1) paused = true;
	}

	return loc;
}

function draw(loc){
	context.fillStyle = 'rgba(0,0,0,1)';
	context.beginPath();
	context.arc(loc[0].x,loc[0].y,3,0,Math.PI*2,false);
	context.stroke();
	context.fill();
	context.fillStyle = 'rgba(0,0,0,1)';
	context.beginPath();
	context.arc(loc[1].x,loc[1].y,3,0,Math.PI*2,false);
	context.stroke();
	context.fill();
	updateTextDouble(loc[0].x*2,loc[0].y*2,loc[1].x*2,loc[1].y*2);
}

//-------------------------------event handler
function displayerInit(displayer) {
	displayer.onmousemove = function (e){
		var loc = windowToCanvas(displayer, e.clientX, e.clientY);

		e.preventDefault();

		drawBackground();
		//drawSpritesheet();
		drawGuidelines(loc.x,loc.y);
		updateReadout(loc.x*2,loc.y*2);	
		
		if(dragging == true){
			if(mode==0){
				updateText(loc.x*2,loc.y*2);	
			}
			else{
				drawRect(loc.x,loc.y);
			}
		}
	};

	displayer.onmousedown = function (e){
		var loc = windowToCanvas(displayer, e.clientX, e.clientY);

		e.preventDefault();

		dragging = true;
		if(mode==0){
			updateText(loc.x*2,loc.y*2);
		}else{
			zoomx = loc.x;
			zoomy = loc.y;
		}
	};

	displayer.onmouseup = function (e){
		context.clearRect(0,0,displayer.width,displayer.height);
		if(mode && dragging){
			dragging = false;
			animateCall(e);
		}
		dragging = false;
	};

	displayer.onmouseout = function (e){
		context.clearRect(0,0,displayer.width,displayer.height);
		drawBackground();
		dragging = false;
	};
}

sock.onopen = function() {
	console.log('open');
};

sock.onclose = function() {
	console.log('close');
};

function send(message) {
	if (sock.readyState === SockJS.OPEN) {
		console.log("sending message")
		sock.send(message);
	} else {
		console.log("The socket is not open.");
	}
}

sock.onmessage = function(e) {
	console.log('message', e.data);
	//alert('received message echoed from server: ' + e.data);
};


//----------------------------- initializing
function makeDevice(target)  {
	var html = "<div id='xy_field'></div>";
	html += "<canvas id='displayer' width='360' height='640'> \
		Canvas not supported \
		</canvas>";
	html += "<textarea id='xy_text' cols='30' rows='30' readonly></textarea>";
	html += "<table id='table'><tr><td><input type='button' value = 'Clear' onclick='clearText();'></td></tr> \
		<tr><td><input name='mode' type='radio' onclick='changeMode(0);' checked='checked'>Default</td></tr> \
		<tr><td><input name='mode' type='radio' onclick='changeMode(1);'>Zoom in</td></tr> \
		<tr><td><input name='mode' type='radio' onclick='changeMode(2);'>Zoom out</td></tr> \
		</table>";
	$(target).html(html);

	displayer = document.getElementById('displayer');
	xy_field = document.getElementById('xy_field');
	xy_text = document.getElementById('xy_text');
	context = displayer.getContext('2d');

	drawBackground();
	displayerInit(displayer);
	xy_field.innerText = '( , )';
}

/*
spritesheet.src = 'test1.jpg';
spritesheet.onload = function(e){
	drawSpritesheet();
};*/