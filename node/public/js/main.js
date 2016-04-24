$(document).ready(function() {
    $('#fullpage').fullpage({
      anchors: ['page'],
      resize : false
    });
    checkPage();
});
var Timer=null;
var currentPage;
function checkPage(){
  console.log("checkpage");

  var xhr = new XMLHttpRequest();
  xhr.open('GET',"../check/page",true);
  xhr.onreadystatechange = function(){
    if(xhr.readyState === 4 && xhr.status === 200){
      console.log(xhr.responseText);
      var json = JSON.parse(xhr.responseText);
      console.log(json);
      if(json.power){
        $('#fanpower').html(json.power);
      }
      if(json.temp){
        $('#temperature').html(json.temp);
      }
      if(json.status){
        switch(json.status){
          case "tempup":
            if(!Timer){
              $('#tempup').html("▲");
              Timer = setTimeout(function(){
                $('#tempup').html("");
                Timer=null;
              },3000);
            }
            break;
          case "tempdown":
            if(!Timer){
              $('#tempdown').html("▼");
              Timer = setTimeout(function(){
                $('#tempdown').html("");
                Timer=null;
              },3000);
            }
            break;
          default:
            break;
        }
      }
      if(json.page != currentPage){
        switch(json.page){
          case "fan":
            window.location.href = "./index.html#page/slide1";
            break;
          case "temp":
            window.location.href = "./index.html#page/slide2";
            break;
          default:
            break;
        }
      }
      setTimeout(checkPage,100);
    }
  }
  xhr.send(null);
}