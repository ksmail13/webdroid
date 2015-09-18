$(document).ready(function(){
  
  ///////////////git아이디 등록////////////////////
    var git_modal_option = {
      title:'GitHub 아이디 등록',
      form:{action:'/gitsubmit', method:'post', id:'frm-test', enctype:'multipart/form-data', 'success':git_frmsuccess, 'error': git_frmfailed},
      contents:[{ name:'git_id', 'placeholder':'아이디', 'id':'git_id', 'type':'email', 'maxlength':'30'}],
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
      form:{action:'/pwvalidate', method:'post', id:'frm-test', enctype:'multipart/form-data', 'success':pw_frmsuccess, 'error': pw_frmfailed},
      contents:[{ name:'old_pw', 'placeholder':'이전 비밀번호', 'id':'old_pw', 'type':'password'},
                { name:'new_pw', 'placeholder':'새로운 비밀번호', 'id':'new_pw', 'type':'password'},
                { name:'new_pw_confirm', 'placeholder':'비밀번호 확인', 'id':'new_pw_confrim', 'type':'password'}],
      buttons:[{'id':'pwvalidate', text:'비밀번호 확인',actiontype:'submit'}],
      onload:function(){
        $("#pwvalidate").common_modal_box(new_pw_modal_option);
      },
    };
  
  $("#pwpw").common_modal_box(old_pw_modal_option);
  
    function pw_frmsuccess(result){
      if(result.result) {        
        $("#pwvalidate").common_modal_box(new_pw_modal_option);
      } else {
        modalAlert('확인 실패', result.message);
      }
    };
    
    function pw_frmfailed(error){
      alert("실패");
      //modalAlert('실패', error.status+' '+error.statusText);
    }
    
 ///////////////새로운 비밀번호 등록 /////////////////////////  
    var new_pw_modal_option={
      title:'새로운 비밀번호 등록',
      form:{action:'/new_pwsubmit', method:'post', id:'frm-update', enctype:'multipart/form-data', 'success':pwUpsuccess, 'error': pwUpFail},
      contents:[{ name:'new_pw', 'placeholder':'새로운 비밀번호', 'id':'new_pw', 'type':'password'},
                { name:'new_pw_confirm', 'placeholder':'비밀번호 확인', 'id':'new_pw_confrim', 'type':'password'}],
      buttons:[{'type':'success','id':'pwsubmit', text:'변경하기',  actiontype:'submit'}]
    };
    
    
    
    function pwUpsuccess(result) {
      if($("#new_pw").val() != $("#new_pw_confrim").val()){
        modalAlert("비밀번호 오류", '비밀번호가 맞지 않습니다 .<br>다시 확인해주세요', function() {});
        return false;
      }
      else{
        modalAlert('성공', '수정되었습니다.');
        $('.modal-back').remove();
      }
    }
    
    function pwUpFail(error) {
      modalAlert('실패', error.status+' '+error.statusText);
    }
        
   
      
      
    
    
    
    /////////////////회원탈퇴////////////////

    var member_modal_option={
      title:'회원을 탈퇴하시겠습니까? ',
      form:{action:'/pwvalidate', method:'post', id:'frm-test', enctype:'multipart/form-data', 'success':member_frmsuccess, 'error': pw_frmfailed},
      contents:[{ name:'old_pw', 'placeholder':'비밀번호 입력', 'id':'check_pw', 'type':'password', 'maxlength':'50'}],
      buttons:[{'type':'success','id':'membersubmit', text:'탈퇴하기', actiontype:'submit'}]
    };
    
       
    $("#members").common_modal_box(member_modal_option);
    
    function member_frmsuccess(result){
      
      if(result.result) {
        function memberClick(e) {
          modalConfirm('탈퇴하기', '정말 탈퇴하시겠습니까',
          function(){requestAysnc('/final_unsubscribe', 'post', null, function(){modalAlert('탈퇴하기', '탈퇴되었습니다',function(){});}, function(){});},
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
      modalAlert('실패', error.status+' '+error.statusText);
    }
    
    
    
    });
