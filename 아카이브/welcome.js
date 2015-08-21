 $('#loginbtn').click( function() {
                var loginData{
                    user-id: $("#user-id").val(),
                    user-pw: $("#user-pw").val()
                };
                $.ajax({
                    url:'/signin',
                    dataType:'json',
                    type:'POST',
                    data: loginData,
                    success:function(result){
                        if(result==true){
                            alert("로그인 성공");
                        }
                        else{
                            alert("실패");
                        }
                    }
                });
            });