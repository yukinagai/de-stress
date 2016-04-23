var http = require('http');
//var request = require('request');
var serialport = require('serialport');

var portName = '/dev/cu.usbmodem1411'; 

var sp = new serialport.SerialPort(portName, {
    baudRate: 115200,
    dataBits: 8,
    parity: 'none',
    stopBits: 1,
    flowControl: false,
    parser: serialport.parsers.readline("\n")   // ※修正：パースの単位を改行で行う
});








sp.on("open",function(){
  console.log('open');
  /*
  setInterval(function(){
  	v = v ? 0 :1;
  	if(v){
  		console.log("on");
  		//sp.write("on");
  		buffer[0] = 0xFF;
  		sp.write(buffer);
  	}else{
  		console.log("off");
  		//sp.write("off");
  		buffer[0] = 0x00;
  		sp.write(buffer);
  	}
    
  },5000);
*/
  var server = http.createServer(function (req, res) {
	  res.writeHead(200, {'Content-Type': 'text/plain',
	                      'Access-Control-Allow-Origin' : '*'});

	  var urlinfo = require('url').parse( req.url , true );
	  console.log( urlinfo );
	  var param = urlinfo.pathname.split("/");
	  for(var i in param){
	  	console.log(param[i]);
	  }
	  if(param.length == 3){
	  	switch(param[1]){
  	  	case "motor":
  	  	  motorDrive(param[2]);
  	  	  break;
  		  default:
  		    break;
  	  }
	  }else{
	  	console.log("wrong parameter");
	  }
	  

	  res.end('Hello World\n');
	}).listen(8001);
  
});

function motorDrive(power){
  console.log("motor power = "+power);
  var buffer = new Buffer(1);
  buffer[0] = power;
  sp.write(buffer);
}

function sendTemp(temp){
  console.log("temparature:"+temp);
  var url = "http://157.7.242.70/bath/temperature/"+temp;
  console.log(url);
  http.get(url, function(res) {

    var body = '';
    res.setEncoding('utf8');

    res.on('data', function(chunk) {
        body += chunk;
    });

    res.on('end', function() {
        ret = JSON.parse(body);
        console.log(body);
    });

  }).on('error', function(e) {
      console.log(e.message);
  });
}

sp.on('data', function(data) {
  console.log('data received: ' + data);
  var param = data.split("/");
  for(var i in param){
    console.log(param[i]);
  }
  if(param.length == 2){
    switch(param[0]){
      case "temperature":
          sendTemp(param[1]);
        break;
      default:
        break;
    }
  }
});




