var isClick = false;

$('[data-toggle="tooltip"]').tooltip();


$(document).ready(function () {

  $('#frm-signup').submit(function() {

    if(isClick){
      if($("#txt-in-user-pw").val() != $("#txt-in-user-pw-re").val()){
        modalAlert("비밀번호 오류", '비밀번호가 맞지 않습니다 .<br>다시 확인해주세요', function() {});
        return false;
      }
      
      else{
        return formRequest('#frm-signup', 
        function(data){
          
          if(data.result && $("#txt-in-user-name").val()!='' && $("#txt-in-user-pw").val()!='' && $("#txt-in-user-pw-re").val()!='' ){
            modalAlert("회원가입", '회원가입이 완료되었습니다.<br>로그인 후 웹드로이드를 즐겨보세요!', function() {location.href = '/';});     
          }
        
          else{
            modalAlert("회원가입", '회원가입에 실패했습니다 .<br>다시 가입해주세요', function() {});
            return false;
          }
        },

        function(error){modalAlert("회원가입","서버에러", function(){});}
      );
      }
      
    }

    else{
      modalAlert("아이디 중복체크", '아이디 중복체크를 해주세요', function() {});
      return false;
    }
  });

  

  $('#idcheck').click( function () {
    
    if($("#txt-in-user-id").val()=='')
      modalAlert("아이디 체크", "아이디를 입력해주세요", function(){});
    else
      requestAysnc('/idcheck', 'post', {'user_id':$("#txt-in-user-id").val ()}, idSuccess, idFailed);
  });

  function idSuccess(result) {
    if(result.result) {
      modalAlert('아이디 중복체크', '사용하실 수 있는 아이디 입니다.',function(){});
      isClick = true;
    }
    else
      modalAlert('아이디 중복체크', '이미 사용중인 아이디 입니다 <br> 다시 입력해 주세요.',function(){});  
  }

  function idFailed(result) {
    modalAlert('실패','실패',function(){});
  }  
  
 
  
  
});