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
          
          $(".modal-footer").append(loading);
          $("#upload").val('로딩중...');
        },
       
        
       success: function(data){
         if(data.result && $("#txt-in-user-file").val()!='' && $("#txt-in-user-fileup").val()!='' && $("#txt-in-user-des").val()!='' ){
             modalAlert("파일 업로드 성공", '파일을 업로드하였습니다. <br>내 프로젝트에서 확인하세요.', function () { location.href = '/projectmain'});
            
          }
         else{
           $(".inner-circles-loader").remove();
           $("#upload").val('파일 업로드');
           modalAlert("파일 업로드 실패", '빠진 항목이 있는지 확인 후 <br>다시 업로드해 주세요', function () {});
           return false;
         }
       },
         
         /*
         if(data.error){           
           $(".inner-circles-loader").remove();
           $("#upload").val('파일 업로드');
           modalAlert("파일 업로드", '파일업로드에 실패했습니다 .<br>다 시 업로드해주세요', function () {});
           return false;
         }
         
         else{ //success
           if(('#txt-in-user-file').val!='' && ('#txt-in-user-fileup').val!='' && ('#txt-in-user-des').val!=''){
            alert("yes");
           }
           
           else{
             alert("no");
             $(".inner-circles-loader").remove();
             $("#upload").val('파일 업로드');
           }
           */
          /*   
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
             
             location.href = '/projectmain';
           }*/
         //}         
       //},
       
       error: function(data){
         modalAlert("에러", '서버에러', function () {});
         return false;
       }
       
     });
    });
  
   
 });