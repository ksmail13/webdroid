$(document).ready(function(){

    requestAysnc('/show_projectlist','post',null,function(data){

        var myHTML = '';
        for (var i = 0; i < data.projects.length; i++) {

                myHTML += '<div class="col-md-4 col-sm-4 project-block" pid="'+data.projects[i].id+'">';
                myHTML += '<div class="thumbnail"  ppath="">';

                myHTML += '<div class="row margin0">';
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

       
        $('.project-block .deletebutton').click(function(){
            var spid = $(this).attr("pid");
            requestAysnc('/delete_projectlist','post',{id:spid},function (data){
                $('.project-block').each(function(){
                    if($(this).attr('pid') == spid) $(this).remove();
                });
                 $('.favorate-block').each(function(){
                    if($(this).attr('pid') == spid) $(this).remove();
                });
            },null);
        });

        $('.project-block .starbutton').click(function(){
            var spid = $(this).attr("pid");
            requestAysnc('/favorate_projectlist','post',{id:spid},function(data) {
                $('.project-block[pid='+spid+']').appendTo('#dv-favorate');
            },null);
        });


        myHTML = '';
        for (var i = 0; i < data.favorates.length; i++) {

        

            myHTML += '<div class="col-md-4 col-sm-4 favorate-block">';
                myHTML += '<div class="thumbnail" pid="'+data.favorates[i].id+'" ppath="">';

                myHTML += '<div class="row margin0">';
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

        $('.favorate-block .deletebutton').click(function(){
            var spid = $(this).attr("pid");
            requestAysnc('/delete_projectlist','post',{id:spid},function (data){
                $('.project-block').each(function(){
                    if($(this).attr('pid') == spid) $(this).remove();
                });
                $('.favorate-block').each(function(){
                    if($(this).attr('pid') == spid) $(this).remove();
                });
            },null);
        });

        $('.favorate-block .starbutton').click(function(){
            var spid = $(this).attr("pid");
            requestAysnc('/cancel_favorate_projectlist','post',{id:spid},function(data) {
                 $('.favorate-block').each(function(){
                    if($(this).attr('pid') == spid) $(this).remove();
                });
            },null);
        });

    },null);
});