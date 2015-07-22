//github 아이디 등록 
$(document).ready(function(){
    $("#gitsubmit").click(function(){
        $.ajax({
            'type':'POST',
            'url': "#",
            'success': function(result){
            $("#div1").html(result);  //이부분 
        }});
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
  alert('변경되었습니다');
 }
}

