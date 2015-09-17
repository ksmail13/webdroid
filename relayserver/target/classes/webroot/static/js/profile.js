 $(document).ready(function () {

   $('#frm-update-introduce').submit(function () {
     return formRequest('#frm-update-introduce',
       function (data) {
         if (data.result) {
           modalAlert("자기소개", '자기소개가 수정 되었습니다.', function () {
             location.reload();
           });
           $('.modal-back').remove();
         } else {
           modalAlert("자기소개", '자기소개등록에 실패했습니다 .<br>다시 등록해주세요', function () {});


         }
       },


       function (error) {
         modalAlert("자기소개 등록", "서버에러", function () {});
       }
     );
   });


   $(function(){
     
     var loading = "<div class='inner-circles-loader'>";

    
     
     $('#fileup').ajaxForm({
       
        beforeSubmit: function(){
          alert("before");
          $(".modal-footer").append(loading);
          $("#upload").val('로딩중...');
        },
       
       success: function(data){
         if(data.error){           
           $(".inner-circles-loader").remove();
           $("#upload").val('파일 업로드');
           modalAlert("파일 업로드", '파일업로드에 실패했습니다 .<br>다 시 업로드해주세요', function () {});
           return false;
         }
         else{
           if(('#txt-in-user-file').val==''){
             modalAlert("파일 업로드 실패", '파일 이름을 입력해주세요', function () {});
             return false;
           }
           else if(('#txt-in-user-fileup').val==''){
             modalAlert("파일 업로드 실패", '파일을 업로드해주세요', function () {});
             return false;
           }
           else if(('#txt-in-user-des').val==''){
             modalAlert("파일 업로드 실패", '파일설명을 입력해주세요', function () {});
             return false;
           }
           else{
             alert("ok");
             location.href = '/projectmain';
           }
         }
         
       },
       
       fail: 
       
     });
    });
  
   
 });