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
      msg = xhr.responseText;
      console.log(msg);
      if(currentPage != msg){
        switch(msg){
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