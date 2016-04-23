$(document).ready(function() {
    $('#fullpage').fullpage({
      anchors: ['page'],
      resize : false
    });
    checkPage();
});

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