 $(document).ready(function () {

   $('#frm-update-introduce').submit(function () {
     return formRequest('#frm-update-introduce',
       function (data) {
         if (data.result) {
           modalAlert(profileTitle,profileSCmsg, function () {
             location.reload();
           });
           $('.modal-back').remove();
         } else {
           modalAlert(profileTitle, profileERRmsg+'<br>'+tryAgain, function () {});


         }
       },


       function (error) {
         modalAlert(serverTitle, serverERRmsg +'<br>'+ tryAgain , function () {});
       }
     );
   });


   $(function(){
     
     var loading = "<div class='inner-circles-loader'>";

     $('#fileup').ajaxForm({
       
        beforeSubmit: function(){
          
          $(".modal-footer").append(loading);
          $("#upload").val('loading...');
        },
       
        
       success: function(data){
         if(data.result && $("#txt-in-user-file").val()!='' && $("#txt-in-user-fileup").val()!='' && $("#txt-in-user-des").val()!='' ){
             modalAlert(fileupTitle, fileupSCmsg, function () { location.href = '/projectmain'});
            
          }
         else{
           $(".inner-circles-loader").remove();
           $("#upload").val('Upload');
           modalAlert(fileupTitle, fileupERRmsg+'<br>'+emptyCheck , function () {});
           return false;
         }
       },        
       
       error: function(data){
         modalAlert(serverTitle, serverERRmsg +'<br>'+tryAgain, function () {});
         return false;
       }
       
     });
    });
  
   
 });