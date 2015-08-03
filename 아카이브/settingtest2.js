(function($){
	$.fn.my_modal_box = function(prop){

		var options = $.extend(prop);
       		
		return this.click(function(e){
			add_block_page();
			add_popup_box();
		});
		 
		 function add_block_page(){
			var block_page = $('<div class="modal_block_page screen-center"></div>');						
			$(block_page).appendTo('body');
		}
		 		
		 function add_popup_box(){
             var myModal=$('<div class="modal-dialog"><div class="modal-content"></div></div>');
             $(myModal).appendTo('.modal_block_page');
                      
             var myModal_header ='<div class="modal-header">';
                 myModal_header +=' <button type="button" class="close" data-dismiss="modal" >&times;</button>';
                 myModal_header += '<h4 class="modal-title">'+options.title +'</h4></div>';
             
             var myModal_body='<div class="modal-body">';
                 myModal_body +='<div class="form-group">';
                 myModal_body +='<label class="sr-only" for="txt_in_user_id">'+options.label+'</label>';
                 myModal_body +='<input type="'+options.type+'" name="'+options.name;
                 myModal_body +='" class="form-control input-lg" id="txt_in_user_id" placeholder="'+options.placeholder+'">'
                 myModal_body +='</div>';
                 
             
            /* for(var i=0; i<contents.length;i++) {
        var content = contents[i];
        myModal_body +='<div class="form-group">'
        myModal_body+='<label class="sr-only" for="txt_in_user_id">'+content.label+'</label>';
        myModal_body+='<input type="'+content.type+' name="'+content.name+'" class="form-control input-lg" id="txt_in_user_id"                    placeholder="'+content.placeholder+'">';
        myModal_body+='</div>';
    }*/
             
                 myModal_body +='<div class="form-group">';
                 myModal_body +='<input type="button" class="setting_btn padding5 '+options.btnclass+' " value="'+options.button+'">';
                 myModal_body += '</div>';
                 myModal_body +='</div>';

			 $(myModal_header).appendTo('.modal-content');
             $(myModal_body).appendTo('.modal-content');
             
             $('.close').click(function(){
                 $('.modal-dialog').remove();
             });
             
             //////////////git등록버튼/////////////////////
             $('.gitsubmit').click(function(){
                 $.ajax({
                    'type':'POST',
                    'url': "#",
                    'success': function(result){
                    $("#div1").html(result);  //이부분 
                    }
                });
                 $('.modal-dialog').remove();
                 window.layerAlert('깃허브아이디 등록','등록되었습니다.');
             });
             
             /////////////pw변경 버튼/////////////////////////
              function validatePassword(){ 
                 var validator = $("#pw-form").validate({
                    rules: {"new-password" :"required",
                            "confirm-new-password":{qualTo: "#new-password"}
                            },                             
                    });
                 if(validator.form()){
                    // ajax
                 }
              }
                  
             $('.pwsubmit').click(function(){
                    $('.modal-dialog').remove();
                    validatePassword();                    
                    window.layerAlert(' 비밀번호 변경','변경되었습니다');
                    return false;
                });
             
             ////////////////////////////////////////////
		}
		return this;
	};

})(jQuery);

	window.layerAlert = function(title,msg,callback) {
    
    var myHTML  = '<div class="out"> <div id="dv-alert" class="dialog screen-center">';
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
        myHTML += '</div></div>';
        
    
    $('.modal .close').click();
    $('body').append(myHTML);
    $('.alert-close').click(function(event) {
        $('#dv-alert').remove();
        if(callback != null && callback != undefined)
            callback();
    });
    
     $('.out').click(function(event){
        $('#dv-alert').remove();
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
    // ajax
 }
    window.layerAlert(' 비밀번호 변경','변경되었습니다');
    return false;
}

//회원탈퇴
$(document).ready(function(){
    $("#unsubscribe-submit").click(function(){
        $.ajax({
            'type':'POST',
            'url': "#",
            'success': function(result){
            $("#div1").html(result);  //이부분
                
        }});
        
        window.layerAlert('회원 탈퇴 하기','탈퇴 되었습니다.');
         
    });
});

