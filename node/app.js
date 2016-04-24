var http = require('http'),
  fs =require('fs');
//var request = require('request');
var serialport = require('serialport');

var portName = '/dev/cu.usbmodem1421'; 

var sp = new serialport.SerialPort(portName, {
    baudRate: 115200,
    dataBits: 8,
    parity: 'none',
    stopBits: 1,
    flowControl: false,
    parser: serialport.parsers.readline("\n")   // ※修正：パースの単位を改行で行う
});



var currentPage = "fan";
var currentPower = 0;
var currentTemp = 0;
var currentStatus;

var pageChenge = false;


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
	  //console.log( urlinfo );
	  var param = urlinfo.pathname.split("/");
	  /*
    for(var i in param){
	  	console.log(param[i]);
	  }
    */
	  if(param.length >= 3){
	  	switch(param[1]){
  	  	case "motor":
          console.log("############ motor speed change ########### ");
          currentPower = param[2];
  	  	  motorDrive(param[2]);
          res.end("success");
  	  	  break;
        case "temp":
          if(param[2]=="up"){
            currentStatus="tempup";
            res.end("success");
          }else if(param[2]=="down"){
            currentStatus="tempdown";
            res.end("success");
          }else{
            res.end("wrong param");
          }
          break;
        case "page":
          if(!pageChenge){
            if(param[2]=="fan"){
              console.log("############ fan page ########### ");
              currentPage = "fan";
              res.end("success");
            }else if(param[2]=="temp"){
              console.log("############ temp page ########### ");
              currentPage = "temp";
              res.end("success");
            }else{
              res.end("wrong param");
            }
            pageChenge = true;
            setTimeout(function(){
              pageChenge = false;
            },3000);
          }else{
            res.end("page changing");
          }
          break;
        case "check":

          if(param[2]=="page"){
            //var json = '{"page":'+currentPage+',"power":'+currentPower+',"temp":'+currentTemp+'}';
            var json = {page:currentPage,power:currentPower,temp:currentTemp,status:currentStatus};
            res.end(JSON.stringify(json));
            currentStatus = null;
          }else{
            res.end("wrong param");
          }
          break;
        case "public":
          //console.log("public");
          fs.readFile(__dirname + urlinfo.pathname, 'utf-8', function(err, data){
            if(err){　//err=trueならNot Foundを返します。
              res.writeHead(404, {'Content-Type': 'text/plain'});
              res.write("Not Found");
              return res.end();　
            }
            var contenttype = urlinfo.pathname.split(".");
            if(contenttype[contenttype.length - 1] == "css"){
              res.writeHead(200, {'Content-Type': 'text/css'});　//htmlファイルなんでhtml
            }else{
              res.writeHead(200, {'Content-Type': 'text/html'});　//htmlファイルなんでhtml
            }
            res.write(data);
            res.end();
          });
          break;
  		  default:
  		    break;
  	  }
	  }else{
	  	res.end("wrong parameter");
	  }
	  

	  //res.end('Hello World\n');
	}).listen(8001);
  
});

function motorDrive(power){
  console.log("motor power = "+power);
  var buffer = new Buffer(1);
  buffer[0] = power;
  sp.write(buffer);
}

function sendTemp(temp){
  //console.log("temparature:"+temp);
  var url = "http://157.7.242.70/bath/temperature/"+temp;
  //console.log(url);
  http.get(url, function(res) {

    var body = '';
    res.setEncoding('utf8');

    res.on('data', function(chunk) {
        body += chunk;
    });

    res.on('end', function() {
        ret = JSON.parse(body);
        //console.log(body);
    });

  }).on('error', function(e) {
      console.log(e.message);
  });
}

sp.on('data', function(data) {
  console.log('data received: ' + data);
  var param = data.split("/");
  if(param.length == 2){
    switch(param[0]){
      case "temperature":
        currentTemp = param[1]; 
        sendTemp(param[1]);
        break;
      default:
        break;
    }
  }
});




