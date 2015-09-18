$(document).ready(function(){
  
  ///////////////gitid update////////////////////
    var git_modal_option = {
      title:'Update GitHub',
      form:{action:'/gitsubmit', method:'post', id:'frm-test', enctype:'multipart/form-data', 'success':git_frmsuccess, 'error': git_frmfailed},
      contents:[{ name:'git_id', 'placeholder':'Git ID', 'id':'git_id', 'type':'email', 'maxlength':'30'}],
      buttons:[{'type':'success','id':'gitsubmit', text:'Update', actiontype:'submit'}]
    };
    
    function git_frmsuccess(result) {
      function gitClick(e) {
        if(result.result){
        modalAlert(gitTitle +" " +SC, gitSCmsg ,function(){});
        }
        else modalAlert(gitTitle +" " +ERR, gitERRmsg+'<br>'+ tryAgain,function(){});
          
      }
      gitClick();
      
      $('.modal-back').remove();
    };
    
    function git_frmfailed(error) {
      
        modalAlert(serverTitleTitle,serverERRmsg+"<br>"+ tryAgain, function(){});
      
      //git_failClick();
    }; 
  
  
    
    $("#gitgit").common_modal_box(git_modal_option);



  /////////////////비밀번호 변경////////////////////////
  
  
  //////////////비밀번호 확인///////////////////////
    var old_pw_modal_option={
      title:'Change Password',
      form:{action:'/new_pwsubmit', method:'post', id:'frm-update', enctype:'multipart/form-data', 'success':pwUpsuccess, 'error': pwUpFail},
      contents:[{ name:'old_pw', 'placeholder':'Original Password', 'id':'old_pw', 'type':'password', 'knownname':'Original password','msgclass':'old_pw_msgcheck'},
                { name:'new_pw', 'placeholder':'New Password', 'id':'new_pw', 'type':'password','maxlength':'20','knownname':'New password', 'msgclass':'new_pw_msgcheck'},
                { name:'new_pw_confirm', 'placeholder':'New Paassword check', 'id':'new_pw_confirm', 'type':'password','maxlength':'20','knownname':'New password check','msgclass':'new_pw_confirm_msgcheck'}],
      buttons:[{'type':'success','id':'pwsubmit', text:'Change',  actiontype:'submit'}],
      onload : function(){
          $('#pwsubmit').prop('disabled', true);
          $('#old_pw').focusout(function () {
              
              $('#pwsubmit').prop('disabled', true);
              requestAysnc('/pwvalidate', 'post', {'old_pw ':$("#old_pw").val ()}, pw_frmsuccess, pw_frmfailed);
            
          });
        
          $('#new_pw').focusout(function() {
           
            if($('#new_pw').val()==''){
              
              $('.new_pw_msgcheck').html(msgcolor('danger','fill your password'));              
              return false;
            }
            else{
               $('.new_pw_msgcheck').html('');
            }
            $('#pwsubmit').prop('disabled', true);
            
          });
        
          $('#new_pw_confirm').focusout(function(){
            
             if($("#new_pw").val() != $("#new_pw_confirm").val()){
                $('.new_pw_confirm_msgcheck').html(msgcolor('danger',"password doesn't match."));
                $('#pwsubmit').prop('disabled', true);
                return false;
              }
             else if($("#new_pw").val() != '' &&$("#new_pw").val() !='' && $("#new_pw").val() == $("#new_pw_confirm").val()){
               $('.new_pw_confirm_msgcheck').html(msgcolor('success','password checked'));
               $('#pwsubmit').prop('disabled', false);
             }
          });
      }
    };
  
    $("#pwpw").common_modal_box(old_pw_modal_option);
    
  
  
    function pw_frmsuccess(result){
      
      
      if(!result.result) {        
        $('.old_pw_msgcheck').html(msgcolor('danger',"password doesn't match. try again"));
        
      } 
      else{
        $('.old_pw_msgcheck').html(msgcolor('success','password checked'));
      }
    }
    
    function pw_frmfailed(error){
      modalAlert(serverTitleTitle,serverERRmsg+"<br>"+ tryAgain, function(){});
    }
    
    function pwUpsuccess(result) {
      if($("#new_pw").val() == $("#new_pw_confirm").val()){
        modalAlert(pwTitle+" "+SC, pwchSCmsg);
        $('.modal-back').remove();
      }
      else{
        modalAlert(pwTitle+" "+ERR, pwchERRmsg);
      }
    }
    
    function pwUpFail(error) {
      modalAlert(serverTitleTitle,serverERRmsg+"<br>"+ tryAgain, function(){});
    }

    
    
    /////////////////회원탈퇴////////////////

    var member_modal_option={
      title:'Do you really want to unsubscribe? ',
      form:{action:'/pwvalidate', method:'post', id:'frm-test', enctype:'multipart/form-data', 'success':member_frmsuccess, 'error': pw_frmfailed},
      contents:[{ name:'old_pw', 'placeholder':'password', 'id':'check_pw', 'type':'password', 'maxlength':'20','knownname':'password'}],

      buttons:[{'type':'success','id':'membersubmit', text:'unsubscribe', actiontype:'submit'}]
    };
    
       
    $("#members").common_modal_box(member_modal_option);
    
    function member_frmsuccess(result){
      
      if(result.result) {
        function memberClick(e) {
          modalConfirm(memberTitle, 'Do you really want to unsubscribe?',
          function(){requestAysnc('/unsubscribe', 'post', null, 
                    function(){modalAlert(memberTitle, memberSCmsg,function(){ location.href = '/'});}, function(){});},
          function() {alert('cancel');});
        }
        memberClick();
        $('.modal-back').remove();
      } else {
        modalAlert(memberTitle+ " "+ERR, memberERRmsg);
      }
    };
    
    
    
    function frmfailed(error) {
    // error 내용 {readyState: 4, responseText: "Cannot POST /test.html", status: 404, statusText: "Not Found"}
      modalAlert(serverTitleTitle,serverERRmsg+"<br>"+ tryAgain, function(){});
    }
    
    
    
    });
