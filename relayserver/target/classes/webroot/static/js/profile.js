 $(document).ready(function() {
       
      $('#frm-update-introduce').submit(function() {
        return formRequest('#frm-update-introduce', 
          function(data){
            if(data.result){
              modalAlert("자기소개", '자기소개가 수정 되었습니다.', function() {location.reload();});
              $('.modal-back').remove();
            }
            else{
              modalAlert("자기소개", '자기소개등록에 실패했습니다 .<br>다시 등록해주세요', function() {});
            }
          });
          function(error){
            modalAlert("자기소개 등록","서버에러", function(){});
          }
      });

      $('#fileup').submit(function() {
      
      //로딩
        var loading= "<div class='inner-circles-loader'>";
                        
        $(".modal-footer").append(loading);
        $("#upload").val('로딩중...');
        
        return formRequest('#fileup', 
          function(data){
          //로딩끝
            if(data.result){            
              alert("업로드");
              location.href = '#';
            }

            else{
              modalAlert("파일 업로드", '파일업로드에 실패했습니다 .<br>다시 업로드해주세요', function() {});
              $(".inner-circles-loader").remove();
              $("#upload").val('파일 업로드'); 
              return false;
            }
          },
                           
          function(error){
            modalAlert("회원가입","서버에러", function(){});
            $(".inner-circles-loader").remove();
            $("#upload").val('파일 업로드'); 
          },
                           
          function (){
            $(".inner-circles-loader").remove();
            $("#upload").val('파일 업로드'); 
            return false;
          }
        );
        
      });
    });