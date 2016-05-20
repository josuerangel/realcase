/**
 * Created by joshua on 19/05/16.
 */
$("#btnConsultar").on('click', getScore);

function getScore(){
    if (validateEmail($("#txtEmail").val())) {
        $.ajax({
            type: "POST",
            url: "http://localhost:8082/realcase/Scores",
            data: {
                email: $("#txtEmail").val()
            },
            dataType: "json",
            success: printTable
        });
    }
    else{
        alert('email invalido');
    }
};

var printTable = function(data, textStatus, jqXHR){
    var datos = {};
    datos.data = data;
    //console.log(datos);
    $("#bodyRender").html("");
    if (datos.data.length > 0){
    	//console.log('entro a render');
        var source   = $("#row-template").html();
        var template = Handlebars.compile(source);
        $("#bodyRender").html(template(datos));
    }
    else {
        alert('El usuario no tiene scores');
    }
};


function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
};