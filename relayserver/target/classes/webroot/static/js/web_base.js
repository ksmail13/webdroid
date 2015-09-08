
/**
 * request to server asynchnously
 */
function requestAysnc(url, type, param, success, failed) {
    $.ajax({'url':url, 'type':type, 'data':param, 'success': success, 'error':failed});  
}

// request by form information
// use on submit
// frm : form dom object
// success : request success callback
// failed : request failed callback
// return : always false because it will prevent execute form action
function formRequest(frm, success, failed) {
    var $frm = $(frm);
    var data = {};
    var breakInfo = {};
    try{
        $frm.find('input').each(function () {
            var $this = $(this);
            var key = $this.attr('name');
            var val = $this.val().trim();
            var type = $this.attr('type');
            var knownname = $this.attr('knownname');

            if(!isTextInput(type)) {
                val = $this.is(':checked')?'true':'false';
            }
            else {
                if(val.length == 0) {
                    //modalAlert();
                    breakInfo.breakName = knownname;
                    breakInfo.breakObj = $this;
                    return false; // same as break;
                }
            }

            data[key+''] = val;
        });
        
        $frm.find('textarea').each(function () {
            var $this = $(this);
            var key = $this.attr('name');
            var val = $this.val().trim();
            var knownname = $this.attr('knownname');

            if(val.length == 0) {
                //modalAlert();
                breakInfo.breakName = knownname;
                breakInfo.breakObj = $this;
                return false; // same as break;
            }

            data[key+''] = val;
        });

        if(isValid(breakInfo.breakName)) {
            modalAlert('다시 확인해주세요',  breakInfo.breakName + '를 입력하셔야 합니다.', function () {breakInfo.breakObj.focus();});

        } 
        
        else {
            requestAysnc($frm.attr('action'), $frm.attr('method'), data, success, failed);
        }
    } catch (e) {if(isValid(console)) console.log(e);}
    return false;
}

function isTextInput(typeStr) {
    var inputType = ['text', 'email', 'password', 'phone'];
    
    
    if(!isValid(typeStr))
      return true;
  
    for(var i=0;i<4;i++) {
      if(inputType[i] == typeStr.toLowerCase())
          return true;
    }
    
    return false;
}

function isValid(variable) {
    return variable != null && variable != undefined;   
}

function validString(variable) {
    return isValid(variable) ? variable : '';   
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
            
            if(isValid(opt.form)) {
                $('#'+opt.form.id).submit(function(e) {
                    return formRequest(this, opt.form.success, opt.form.error);
                });
            }
            
            setModalEvent(opt);
        });
        function setModalEvent(options) {
            $('.modal-close').click(function (e) {$('.modal-back').remove();});
            $('.modal-block-page').click(function(e) {e.stopPropagation();});
            
            for(var i=0;i<options.buttons.length;i++) {
                var button = options.buttons[i];
                if(isValid(button.onclick)) {
                    $("#"+button.id).click(button.onclick);
                }
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
            if(isValid(options.form)) {
                    html += '<form id="'+options.form.id+'" action="'+options.form.action+'" method="'+options.form.method+'" '+
                                'enctype="'+validString(options.form.enctype)+'" >';
            }
            for(var i=0; i<options.contents.length;i++) {
                    var content = options.contents[i];
                        html += '<div class="form-group">'+
                                    '<label class="sr-only" for="'+content.id+'">'+content.placeholder+'</label>'+
                                    '<input type="'+content.type+'" name="'+content.name+'" class="form-control input-lg" id="'+content.id+'" placeholder="'+content.placeholder+'" knownname="'+content.knownname+'"/>'+
                                '</div>';
            }
                        html +='<div class="form-group text-center">';
            for(var i=0; i<options.buttons.length;i++) {
                    var button = options.buttons[i];
                    var btnActionType = isValid(button.actiontype)?button.actionType:'button';
                if(!isValid(button.type)) button.type = 'default';
                            html += '<button class="btn btn-'+button.type+'" id="'+button.id+'"'+
                                ' type="'+btnActionType+'">'+button.text+'</button> ';
            }
                        html += '</div>';
            if(isValid(options.form)) {
                        html += '</form>';
            }
                    html += '</div>'+
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
    try{
    msg = msg.replace(/\n/gi, '<br />');
    } catch(e) {
        if(console) {
            console.log(e);
        }
    }
    var myHTML ='<div class="modal-back-dark modal-alert-back ">';
        myHTML += '<div class="modal-dialog modal-alert screen-center" role="alert"><div class="modal-content">';

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

    $('.modal-alert-back').remove();
    $('body').append(myHTML);
    $('#btn-alert').focus();
    $('#btn-alert').click(function (e) {
        if(isValid(callback))
            callback.call(this, e);
        $('.modal-alert-back').remove();
    });
}

function modalConfirm(title, msg, okCallback, cancelCallback) {
    msg = msg.replace(/\n/gi, '<br />');
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
    $('#btn-alert-ok').focus();
    $('#btn-alert-ok').click(function (e) {
        if(isValid(okCallback))
            okCallback.call(this, e);
        $('.modal-alert-back').remove();
    });
    $('#btn-alert-cancel').click(function (e) {
        if(isValid(cancelCallback))
            cancelCallback.call(this, e);
        $('.modal-alert-back').remove();
    });
}

$(document).ready(function () {
    $('#a-signout').click(function() {
        requestAysnc('/signout', 'post', {}, signoutSuccess, signoutFailed);
    });
});


function signoutSuccess(result) {
    if(result.result) {
        location.reload();
    }
}

function signoutFailed(result) {
    modalAlert(result.responseText);
}


function setCookie(cname,cvalue,exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires=" + d.toGMTString();
    document.cookie = cname+"="+cvalue+"; "+expires;
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
