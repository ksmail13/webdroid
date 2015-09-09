(function($){
    $.fn.imageUploader = function (url) {
        var idNum = parseInt(Math.random() * 10000000);
        var uploaderId = 'inp-'+idNum;
        var btnId = 'btn-'+idNum;
        var frmId = 'frm-'+idNum;
        var $this = $(this);
        var html = '<form action="'+url+'" method="post" id="'+frmId+'" enctype="multipart/form-data" style="position:absolute; top:-10000px;">';
            html += '<input type="file" name="img" id="'+uploaderId+'" />' ;
            html += '</form>';
            html += '<div class="padding10"></div>';
            html += '<button id="'+btnId+'" class="btn btn-success">';
            html += '<span class="glyphicon glyphicon-open-file"></span> upload';
            html += '</button>'
        
        $this.after(html);
        
        $('#'+uploaderId).change(function() {
          if($(this).val() != '') {
            $('#'+frmId).submit();
          }
        });
      
        $('#'+frmId).ajaxForm({
            beforeSend:function() {
              
            },
            complete:function(xhr) {
                var res = $.parseJSON(xhr.responseText);
                if(res.result) {
                    $this.attr('src', res.uploadPath);
                } else {
                    modalAlert('업로드 실패', res.message);   
                }
              $('#'+uploaderId).val('');
            }
        });
        
        $('#'+btnId).click(function(e) {
            $('#'+uploaderId).click();
        });
        
    }
})(jQuery);