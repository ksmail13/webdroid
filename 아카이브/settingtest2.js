//**************************Modal-alert****************************************//
(function($){ //modal alert
    $.fn.modal_opts = [];
	$.fn.my_modal_box = function(prop){

        $.fn.modal_opts[this.attr('id')] = $.extend(true, {}, $.extend(prop));
        
		return this.click(function(e){
            var id = $(this).attr('id');
            var opt = $.fn.modal_opts[$(this).attr('id')];
			add_block_page();
			add_popup_box(opt);
		});
		 
		 function add_block_page(){
			var block_page = $('<div class="out"><div class="modal_block_page screen-center"></div></div>');						
			$(block_page).appendTo('body');
		}
		 		
		 function add_popup_box(options){
             var myModal=$('<div class="modal-dialog"><div class="modal-content content"></div></div>');
             $(myModal).appendTo('.modal_block_page');
                      
             var myModal_header ='<div class="modal-header">';
                 myModal_header +=' <button type="button" class="close" data-dismiss="modal" >&times;</button>';
                 myModal_header += '<h4 class="modal-title">'+options.title +'</h4></div>';
             
             var myModal_body='<div class="modal-body">';
             /*
                 myModal_body +='<div class="form-group">';
                 myModal_body +='<label class="sr-only" for="txt_in_user_id">'+options.label+'</label>';
                 myModal_body +='<input type="'+options.type+'" name="'+options.name;
                 myModal_body +='" class="form-control input-lg" id="txt_in_user_id" placeholder="'+options.placeholder+'">'
                 myModal_body +='</div>';
               */  
             
                for(var i=0; i<options.contents.length;i++) {
                    var content = options.contents[i];
                    myModal_body +='<div class="form-group">'
                    myModal_body+='<label class="sr-only" for="txt_in_user_id">'+content.label+'</label>';
                    myModal_body+='<input type="'+content.type+'" name="'+content.name+'" class="form-control input-lg" id="txt_in_user_id"                    placeholder="'+content.placeholder+'">';
                    myModal_body+='</div>';
                }
             
                 myModal_body +='<div class="form-group">';
                 myModal_body +='<input type="button" class="setting_btn padding5 '+options.btnclass+'" value="'+options.button+'">';
                 myModal_body += '</div>';
                 myModal_body +='</div>';

			 $(myModal_header).appendTo('.content');
             $(myModal_body).appendTo('.content');
             
             $('.close').click(function(){
                 $('.modal-dialog').remove();
             });
            
             /*
             $('.out').click(function(){
                 $('.modal-dialog').remove();
             });
             */
             
             
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
                 window.layerAlert('GIT hub 아이디 등록','등록되었습니다.','gitok','확인');
             });
             
             /////////////pw변경 버튼/////////////////////////
             function validatePassword(){ 
                 var validator = $(".modal-body").validate({
                    rules: {"new-pw" :"required",
                            "new-pw-confirm":{qualTo: "#new-pw"}
                            },                             
                    });
                 if(validator.form()){
                    // ajax
                 }
             }
                  
             $('.pwsubmit').click(function(){
                   
                if(validatePassword()==true){
                    $('.modal-dialog').remove();
                    window.layerAlert(' 비밀번호 변경','변경되었습니다','pwok','확인');
                }
                else{}
                return false;
            });
           
             ////////////회원탈퇴//////////////////////////
            $('.members-pw-submit').click(function(){
                
                $.ajax({
                    'type':'POST',
                    'url': "#",
                    'success': function(result){
                    $("#div1").html(result);  //이부분 
                    }
                });
                 $('.modal-dialog').remove();
                 window.layerAlert('탈퇴하기','탈퇴되었습니다.','unsubscribeok','확인');
             });
             
             /////////////////////////////////////////////
		}
		return this;
	};

})(jQuery);

//**************************확인alert****************************************//
window.layerAlert = function(title,msg,btnclass, btnvalue, callback) {
    
    var myHTML ='<div class="modal_alert_page screen-center">';
        myHTML += '<div class="modal-dialog"><div class="modal-content">';
    
        myHTML += '<div class="modal-header">';
        myHTML += '<button type="button" class="close" data-dismiss="modal" >&times;</button>'
        myHTML += '<h4 class="modal-title">'+title +'</h4>';
        myHTML += '</div>';
    
        myHTML += '<div class="modal-body">';
        myHTML += '<p align="center">'+msg+'</p>';
        myHTML += '<div class="form-group">';
        myHTML += '<input type="button" class="setting_btn padding5 '+btnclass+ '"  value="'+btnvalue+ '">';
        myHTML += '</div>';
        myHTML += '</div>';
        
        myHTML += '</div>';
        myHTML += '</div>';
        myHTML += '</div>';
        
    $('body').append(myHTML);
    
    ///////////////////회원탈퇴//////////////////////
   
    $('.members-submit').my_modal_box({
            title:'탈퇴를 위한 비밀번호 확인',
            contents:[{label:'confirmPW', name:'confrim-pw', type:'password', placeholder:"비밀번호"}],
            btnclass:'members-pw-submit',                  
            button:'탈퇴하기'
    });
    //////////////////////////////////////////////
    
    $('.'+btnclass).click(function(){
        if(btnclass!='members-submit'){
            $('.modal-dialog').remove();
            if(callback != null && callback != undefined)
                callback();
            }
        });
    
    
     $('.close').click(function(event){
        $('.modal-dialog').remove();
    });
};
