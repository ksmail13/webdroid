$(document).ready(function () {

  $('#frm-signup').submit(function() {

      
      if($("#txt-in-user-pw").val() != $("#txt-in-user-pw-re").val()){
        modalAlert("Password error", "password doesn't match <br> Please check it", function() {});
        return false;
      }
      
      else{
        return formRequest('#frm-signup', 
        function(data){
          
          if(data.result && $("#txt-in-user-name").val()!='' && $("#txt-in-user-pw").val()!='' && $("#txt-in-user-pw-re").val()!='' ){
            modalAlert("Signup", 'Successfully signed up! <br> Enjoy WEBDROID after sign-in!', function() {location.href = '/';});     
          }
        
          else{
            modalAlert("Signup", 'Signup failed.<br>Please try again', function() {});
            return false;
          }
        },

        function(error){modalAlert("Signup","Server Error!!<br>We apologize for inconvenience.<br> Please try again.", function(){});}
      );
      }
      
    

  });

  

   $('#txt-in-user-id').focusout(function(){
   
    var email = $('#txt-in-user-id').val();  
    var regex=/^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
    
    if(!regex.test(email)) {   
      if(email==='') {        
        $('.msg_here').html(msgcolor('danger','Please write your email address.'));
        $('#signup_original').prop('disabled', false);
        return false;
      }
      else{
        $('.msg_here').html(msgcolor('danger','Wrong email address. Please check it.'));
        $('#signup_original').prop('disabled', false);
        return false; 
      }
    } 
    
    else {      
      requestAysnc('/idcheck', 'post', {'user_id ':$("#txt-in-user-id").val ()}, idSuccess, idFailed);
    }
  
     function idSuccess(result) {
        if(result.result) {
          $('.msg_here').html(msgcolor('success','You can use this email address.'));

        }
        else{
          $('.msg_here').html(msgcolor('danger','This email address is already used!'));
          $('#signup_original').prop('disabled', false);
        }
      }

      function idFailed(result) {
        modalAlert('실패','실패',function(){});
      }  
  
     
    /*
    if(sEmail=='')
      modalAlert("아이디 체크", "아이디를 입력해주세요", function(){});
    else
      requestAysnc('/idcheck', 'post', {'user_id':$("#txt-in-user-id").val ()}, idSuccess, idFailed);
      */
  });

  
 
  
  
});