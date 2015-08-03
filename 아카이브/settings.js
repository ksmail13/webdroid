window.layerAlert = function(title,msg,callback) {
    var myHTML  = '<div id="dv-alert" class="dialog screen-center">';
        myHTML += '<div class="dialog-bg">';
        myHTML += '<div class="dialog-title">'+title;
        myHTML += '<button type="button" class="close-modal alert-close">'
        myHTML += '<span aria-hidden="true">&times;</span>';
        myHTML += '</button>';
        myHTML += '</div>';
        myHTML += '<div class="dialog-msg">'+msg+'</div>';
        myHTML += '<div class="dialog-buttons">';
        myHTML += '<button onclick=""  class="large green button alert-close">확 인</button>';
        myHTML += '</div>';
        myHTML += '</div>';
        myHTML += '</div>';
    
    /*
    myWrap = document.createElement('div');

    myWrap.innerHTML = myHTML;

    document.body.appendChild(myWrap);
    */
    $('.modal .close').click();
    $('body').append(myHTML);
    $('.alert-close').click(function() {
        $('#dv-alert').remove();
        if(callback != null && callback != undefined)
            callback();
    });
    
    //jQuery(document).on("click", function() {
         //       allMenus.removeClass("open");
         //   });
    
    $('#outside').on('click', function() {
        $('#dv-alert').remove();
        //$('#modal-messages').hide();
    });

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
        window.layerAlert('깃허브아이디 등록','등록되었습니다.');
        
       
    });
});

// 비밀번호 변경 


$(document).ready(function(){
    $(".pwsubmit").click(function validatePassword(){ 
         var validator = $("#pw-form").validate({
          rules: {                   
           "new-password" :"required",
           "confirm-new-password":{
            equalTo: "#new-password"
            }  
            },                             
         });


         if(validator.form()){
            // ajax
         }
            window.layerAlert(' 비밀번호 변경','변경되었습니다');
            return false;
        });



$(document).ready(function(){
    $("#unsubscribe-submit").click(function(){
        $.ajax({
            'type':'POST',
            'url': "#",
            'success': function(result){
            $("#div1").html(result);  //이부분
                
        }});
        
        window.layerAlert('회원 탈퇴 하기','탈퇴 되었습니다.');
        stopPropagation();
    });
});





