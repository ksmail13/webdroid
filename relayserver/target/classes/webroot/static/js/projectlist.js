$(document).ready(function(){

    requestAysnc('/show_projectlist','post',null,function(data){

        var myHTML = '';
        for (var i = 0; i < data.projects.length; i++) {

                myHTML += '<div class="col-md-4 col-sm-4 project-block" pid="'+data.projects[i].id+'">';
                myHTML += '<div class="thumbnail"  ppath="">';

                myHTML += '<div class="row margin0 go" pid="'+data.projects[i].id+'">';                
                myHTML += '<div class="col-xs-5"><div class="app-icon">';
                myHTML += '<img src="/images/apple-touch-icon@2.png">';
                myHTML += '</div>';
                myHTML += '</div>';
                myHTML += '<div class="col-xs-7">';
                myHTML += '<div class="text-center app-title"><h4>'+data.projects[i].name+'</h4></div>';
                myHTML += '<div class="app-descript"><p>'+data.projects[i].description+'</p></div>';                
                myHTML += '<div class="app-function">';
                myHTML += '<div class="btn-group btn-group-xs">';

                myHTML += '<a class="btn btn-default deletebutton" pid="'+data.projects[i].id+'" href="#">';
                myHTML += '<span class="glyphicon glyphicon-minus"></span></a>';

                myHTML += '<a class="btn btn-default starbutton" pid="'+data.projects[i].id+'" href="#">';
                myHTML += '<span class="glyphicon glyphicon-star-empty"></span></a>';

                myHTML += '<a class="btn btn-default" pid="'+data.projects[i].id+'" href="#">';
                myHTML += '<span class="glyphicon glyphicon-info-sign"></span></a>';

                myHTML += '</div>';
                myHTML += '</div>';
                myHTML += '</div>';
                myHTML += '</div>';
                myHTML += '</div>';
                myHTML += '</div>';

        }
        
        $('#dv-projectlist').html(myHTML);

       
        $('.project-block .deletebutton').click(function(e){
            var spid = $(this).attr("pid");
            e.stopPropagation();
            requestAysnc('/delete_projectlist','post',{id:spid},function (data){
                location.reload();
            },null);
        });

        $('.project-block .starbutton').click(function(e){
            var spid = $(this).attr("pid");
            e.stopPropagation();
            requestAysnc('/favorate_projectlist','post',{id:spid},function(data) {
                location.reload();
            },null);
        });


        myHTML = '';
        for (var i = 0; i < data.favorates.length; i++) {

        

            myHTML += '<div class="col-md-4 col-sm-4 favorate-block">';
                myHTML += '<div class="thumbnail" pid="'+data.favorates[i].id+'" ppath="">';

                myHTML += '<div class="row margin0 go" pid="'+data.favorates[i].id+'">';
                myHTML += '<div class="col-xs-5"><div class="app-icon">';
                myHTML += '<img src="/images/apple-touch-icon@2.png">';

                myHTML += '</div>';
                myHTML += '</div>';
                myHTML += '<div class="col-xs-7">';
                myHTML += '<div class="text-center app-title"><h4>'+data.favorates[i].name+'</h4></div>';
                myHTML += '<div class="app-descript"><p>'+data.favorates[i].description+'</p></div>';
                myHTML += '<div class="app-function">';

                myHTML += '<div class="btn-group btn-group-xs">';

                myHTML += '<a class="btn btn-default deletebutton" pid="'+data.favorates[i].id+'" href="#">';
                myHTML += '<span class="glyphicon glyphicon-minus"></span></a>';

                myHTML += '<a class="btn btn-default starbutton" pid="'+data.favorates[i].id+'" href="#">';
                myHTML += '<span class="glyphicon glyphicon-star"></span></a>';

                myHTML += '<a class="btn btn-default" pid="'+data.favorates[i].id+'" href="#">';
                myHTML += '<span class="glyphicon glyphicon-info-sign"></span></a>';

                myHTML += '</div>';
                myHTML += '</div>';
                myHTML += '</div>';
                myHTML += '</div>';
                myHTML += '</div>';
                myHTML += '</div>';


        }
        $('#dv-favorate').html(myHTML);

        $('.favorate-block .deletebutton').click(function(e){
            var spid = $(this).attr("pid");
            e.stopPropagation();
            requestAysnc('/delete_projectlist','post',{id:spid},function (data){
                location.reload();
            },null);
        });

        $('.favorate-block .starbutton').click(function(e){
            var spid = $(this).attr("pid");
            e.stopPropagation();
            requestAysnc('/cancel_favorate_projectlist','post',{id:spid},function(data) {
                location.reload();
            },null);
        });
        
        
          
          $('.go').click(function(e){
            alert("title");
            var spid = $(this).attr("pid");
            requestAysnc('/projectview/'+spid,'post',{},null,null);
          });
         
          $('.go2').click(function(e){
            alert("ddd");
          });

    },null);
});