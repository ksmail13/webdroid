
/**
 * request to server asynchnously
 */
function requestAysnc(url, type, param, success, failed) {
    $.ajax({'url':url, 'type':type, 'data':param, 'success': success, 'error':failed});  
}

//**************************Modal-alert****************************************//
(function($){ //modal alert
    $.fn.modal_opts = [];
    $.fn.common_modal_box = function(prop){

        $.fn.modal_opts[this.attr('id')] = $.extend(true, {}, $.extend(prop));

        return this.click(function(e){
            var id = $(this).attr('id');
            var opt = $.fn.modal_opts[$(this).attr('id')];
            //add_block_page();
            //add_popup_box(opt);
            $('body').append(createModalTag(opt));
            setModalEvent(opt);
        });
        function setModalEvent(options) {
            $('.modal-close').click(function (e) {$('.modal-back').remove();});
            $('.modal-block-page').click(function(e) {e.stopPropagation();});
            
            for(var i=0;i<options.buttons.length;i++) {
                var button = options.buttons[i];
                $("#"+button.id).click(button.onclick);
            }
        }
        
        function createModalTag(options) {
        var html =
            '<div class="out modal-back modal-close">'+
                '<div class="modal-block-page">'+
                    '<div class="modal-dialog">'+
                        '<div class="modal-content content">'+
                            '<div class="modal-header">'+
                                '<button type="button" class="close modal-close" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
                                '<h4 class="modal-title">'+options.title+'</h4>'+
                            '</div>'+
                            '<div class="modal-body">';
            for(var i=0; i<options.contents.length;i++) {
                    var content = options.contents[i];
                        html += '<div class="form-group">'+
                                    '<label class="sr-only" for="'+content.id+'">'+content.placeholder+'</label>'+
                                    '<input type="'+content.type+'" name="'+content.name+'" class="form-control input-lg" id="'+content.id+'" placeholder="'+content.placeholder+'"/>'+
                                '</div>';
            }
                        html +='<div class="form-group text-center">';
            for(var i=0; i<options.buttons.length;i++) {
                    var button = options.buttons[i];
                if(button.type == null || button.type == undefined) button.type = 'default';
                            html += '<button class="btn btn-'+button.type+'" id="'+button.id+'" type="submit">'+button.text+'</button> ';
            }
                        html += '</div>'+
                            '</div>'+
                        '</div>'+
                    '</div>'+
                '</div>'+
            '</div>';
            
            return html;
        }
        


             

             /////////////////////////////////////////////
        }
    
    
    return this;
    

})(jQuery);

//**************************확인alert****************************************//

function modalAlert(title, msg, callback) {
    var myHTML ='<div class="modal-back-dark modal-alert-back ">';
        myHTML += '<div class="modal-dialog modal-alert screen-center"><div class="modal-content">';

        myHTML += '<div class="modal-header">';
        //myHTML += '<button type="button" class="close alert-close" data-dismiss="modal" >&times;</button>'
        myHTML += '<h4 class="modal-title">'+title +'</h4>';
        myHTML += '</div>';

        myHTML += '<div class="modal-body">';
        myHTML += '<p align="center">'+msg+'</p>';
        myHTML += '<div class="form-group text-center">';
        myHTML += '<input type="button" class="btn btn-success setting_btn padding5" id="btn-alert"  value="확인">';
        myHTML += '</div>';
        myHTML += '</div>';

        myHTML += '</div>';
        myHTML += '</div>';
        myHTML += '</div>';

    $('body').append(myHTML);
    $('#btn-alert').click(function (e) {
        callback.call(this, e);
        $('.modal-alert-back').remove();
    });
}

function modalConfirm(title, msg, okCallback, cancelCallback) {
    var myHTML ='<div class="modal-back-dark modal-alert-back ">';
        myHTML += '<div class="modal-dialog modal-alert screen-center"><div class="modal-content">';

        myHTML += '<div class="modal-header">';
        //myHTML += '<button type="button" class="close alert-close" data-dismiss="modal" >&times;</button>'
        myHTML += '<h4 class="modal-title">'+title +'</h4>';
        myHTML += '</div>';

        myHTML += '<div class="modal-body">';
        myHTML += '<p align="center">'+msg+'</p>';
        myHTML += '<div class="form-group text-center">';
        myHTML += '<input type="button" class="btn btn-success setting_btn padding5" id="btn-alert-ok"  value="확인">';
        myHTML += ' <input type="button" class="btn btn-danger setting_btn padding5" id="btn-alert-cancel"  value="취소">';
        myHTML += '</div>';
        myHTML += '</div>';

        myHTML += '</div>';
        myHTML += '</div>';
        myHTML += '</div>';

    $('body').append(myHTML);
    $('#btn-alert-ok').click(function (e) {
        okCallback.call(this, e);
        $('.modal-alert-back').remove();
    });
    $('#btn-alert-cancel').click(function (e) {
        cancelCallback.call(this, e);
        $('.modal-alert-back').remove();
    });
}
/*
window.layerAlert = function(title,msg,btnclass, btnvalue, callback) {

    

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
*/