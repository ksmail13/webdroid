window.modalFunction = function(title,msg) {
    var myHTML = '<div id="dialog"><div id="dialog-bg"><div id="dialog-title">'+title+'<button type="button" class="close-modal"><span aria-hidden="true">&times;</span></button></div><div id="dialog-msg">'+msg+'</div><div id="dialog-buttons"><button id="yesbtn" onclick="yesbtn()"  class="large green button">확 인</button></div></div></div>';
    
    
    myWrap = document.createElement('div');

    myWrap.innerHTML = myHTML;

    document.body.appendChild(myWrap);
    
    function yesbtn(){
       alert("DD");
    }
    
  };


//github 아이디 등록 
$(document).ready(function(){
    $("#gitsubmit").click(function(){
        $.ajax({
            'type':'POST',
            'url': "#",
            'success': function(result){
            $("#div1").html(result);  //이부분 
        }});
        window.modalFunction('깃허브아이디 등록','등록되었습니다.');
        
    });
});

// 비밀번호 변경 

function validatePassword(){ 
 var validator = $("#pw-form").validate({
  rules: {                   
   "new-password" :"required",
   "confirm-new-password":{
    equalTo: "#new-password"
      }  
     },                             
 });
    
    
 if(validator.form()){
  window.modalFunction(' 비밀번호 변경','변경되었습니다');
  alert("Dd");
 }
}



$(document).ready(function(){
    $("#unsubscribe-submit").click(function(){
        $.ajax({
            'type':'POST',
            'url': "#",
            'success': function(result){
            $("#div1").html(result);  //이부분 
        }});
        
        window.modalFunction(' 회원 탈퇴 하기','탈퇴 되었습니다.');
        
    });
});






