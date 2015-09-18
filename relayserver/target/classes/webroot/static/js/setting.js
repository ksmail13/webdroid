$(document).ready(function(){
  
  ///////////////git아이디 등록////////////////////
    var git_modal_option = {
      title:'GitHub 아이디 등록',
      form:{action:'/gitsubmit', method:'post', id:'frm-test', enctype:'multipart/form-data', 'success':git_frmsuccess, 'error': git_frmfailed},
      contents:[{ name:'git_id', 'placeholder':'아이디', 'id':'git_id', 'type':'email'}],
      buttons:[{'id':'gitsubmit', text:'등록하기', actiontype:'submit'}]
    };
    
    function git_frmsuccess(result) {
      function gitClick(e) {
        modalAlert('GitHub 아이디 등록', '등록되었습니다',function(){});
      }
      gitClick();
      
      $('.modal-back').remove();
    };
    
    function git_frmfailed(error) {
      function git_failClick(e) {
        modalAlert('GitHub 아이디 등록', '실패하였습니다.<br> 다시 등록해주세요',function(){});
      }
      git_failClick();
    };
    
    $("#gitgit").common_modal_box(git_modal_option);



  /////////////////비밀번호 변경////////////////////////
  
  
  //////////////비밀번호 확인///////////////////////
    var old_pw_modal_option={
      title:'비밀번호 변경',
      form:{action:'/new_pwsubmit', method:'post', id:'frm-update', enctype:'multipart/form-data', 'success':pwUpsuccess, 'error': pwUpFail},
      contents:[{ name:'old_pw', 'placeholder':'이전 비밀번호', 'id':'old_pw', 'type':'password', 'knownname':'이전 비밀번호','msgclass':'old_pw_msgcheck'},
                { name:'new_pw', 'placeholder':'새로운 비밀번호', 'id':'new_pw', 'type':'password','knownname':'새 비밀번호', 'msgclass':'new_pw_msgcheck'},
                { name:'new_pw_confirm', 'placeholder':'비밀번호 확인', 'id':'new_pw_confirm', 'type':'password','knownname':'새 비밀번호 확인','msgclass':'new_pw_confirm_msgcheck'}],
      buttons:[{'type':'success','id':'pwsubmit', text:'변경하기',  actiontype:'submit'}],
      onload : function(){
          $('#pwsubmit').prop('disabled', true);
          $('#old_pw').focusout(function () {
              
              $('#pwsubmit').prop('disabled', true);
              requestAysnc('/pwvalidate', 'post', {'old_pw ':$("#old_pw").val ()}, pw_frmsuccess, pw_frmfailed);
            
          });
        
          $('#new_pw').focusout(function() {
           
            if($('#new_pw').val()==''){
              
              $('.new_pw_msgcheck').html(msgcolor('danger','비밀번호를 입력해주세요'));              
              return false;
            }
            else{
               $('.new_pw_msgcheck').html('');
            }
            $('#pwsubmit').prop('disabled', true);
            
          });
        
          $('#new_pw_confirm').focusout(function(){
            
             if($("#new_pw").val() != $("#new_pw_confirm").val()){
                $('.new_pw_confirm_msgcheck').html(msgcolor('danger','비밀번호가 일치하지 않습니다.'));
                $('#pwsubmit').prop('disabled', true);
                return false;
              }
             else if($("#new_pw").val() != '' &&$("#new_pw").val() !='' && $("#new_pw").val() == $("#new_pw_confirm").val()){
               $('.new_pw_confirm_msgcheck').html(msgcolor('success','비밀번호가 일치합니다.'));
               $('#pwsubmit').prop('disabled', false);
             }
          });
      }
    };
  
    $("#pwpw").common_modal_box(old_pw_modal_option);
    
  
  
    function pw_frmsuccess(result){
      
      
      if(!result.result) {        
        $('.old_pw_msgcheck').html(msgcolor('danger','비밀번호가 맞지 않습니다. 다시 입력해주세요.'));
        
      } 
      else{
        $('.old_pw_msgcheck').html(msgcolor('success','비밀번호가 확인되었습니다.'));
      }
    }
    
    function pw_frmfailed(error){
      modalAlert("에러", '서버에러', function () {});
    }
    
    function pwUpsuccess(result) {
      if($("#new_pw").val() == $("#new_pw_confirm").val()){
        modalAlert('성공', '비밀번호가 수정되었습니다.');
        $('.modal-back').remove();
      }
      else{
        modalAlert('실패', '비밀번호호.');
      }
    }
    
    function pwUpFail(error) {
      modalAlert("에러", '서버에러', function () {});
    }

    
    
    /////////////////회원탈퇴////////////////

    var member_modal_option={
      title:'회원을 탈퇴하시겠습니까? ',
      form:{action:'/pwvalidate', method:'post', id:'frm-test', enctype:'multipart/form-data', 'success':member_frmsuccess, 'error': pw_frmfailed},
      contents:[{ name:'old_pw', 'placeholder':'비밀번호 입력', 'id':'check_pw', 'type':'password','knownname':'비밀번호'}],
      buttons:[{'type':'success','id':'membersubmit', text:'탈퇴하기', actiontype:'submit'}]
    };
    
       
    $("#members").common_modal_box(member_modal_option);
    
    function member_frmsuccess(result){
      
      if(result.result) {
        function memberClick(e) {
          modalConfirm('탈퇴하기', '정말 탈퇴하시겠습니까',
          function(){requestAysnc('/unsubscribe', 'post', null, 
                    function(){modalAlert('탈퇴하기', '탈퇴되었습니다',function(){ location.href = '/'});}, function(){});},
          function() {alert('cancel');});
        }
        memberClick();
        $('.modal-back').remove();
      } else {
        modalAlert('확인 실패', result.message);
      }
    };
    
    function frmsuccess(result) {
      //location.reload();
      if(!result.result)
        modalAlert('실패', result.message);
    }
    
    function frmfailed(error) {
    // error 내용 {readyState: 4, responseText: "Cannot POST /test.html", status: 404, statusText: "Not Found"}
      modalAlert("에러", '서버에러', function () {});
    }
    
    
    
    });
