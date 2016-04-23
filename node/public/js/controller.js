var domain = "http://172.20.10.2:8001";
var motor=0;

window.addEventListener("load",function(){
  console.log("load");
  document.getElementById("power").innerHTML = "power = " + motor;
  document.getElementById("fan").addEventListener("click",function(){
    send(domain + "/page/fan");
  });
  document.getElementById("temp").addEventListener("click",function(){
    send(domain + "/page/temp");
  });
  document.getElementById("up").addEventListener("click",function(){
    if(motor < 100){
      motor = 100;
    }else{
      motor = 255;
    }
    send(domain + "/motor/"+motor);
    document.getElementById("power").innerHTML = "power = " + motor;
  });
  document.getElementById("down").addEventListener("click",function(){
    if(motor > 100){
      motor = 100;
    }else{
      motor = 0;
    }
    send(domain + "/motor/"+motor);
    document.getElementById("power").innerHTML = "power = " + motor;
  });
});

function send(url){
  var xhr = new XMLHttpRequest();
  xhr.open('GET',url,true);
  xhr.onreadystatechange = function(){
    if(xhr.readyState === 4 && xhr.status === 200){
      console.log(xhr.responseText);
    }
  };
  xhr.send(null);
}