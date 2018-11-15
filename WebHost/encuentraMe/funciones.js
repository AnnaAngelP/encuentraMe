var modal = document.getElementById('form_login');
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

var base = '/encuentraMe/';
//var base = '';

$( "#form_login" ).submit(function( event ){

  $.ajax({
    type: "POST",
    url: "Session.php",
    data: $("#form_login").serialize(),
    cache: false,
    success: function(result){
      if(result==1){
        document.location.href="/encuentraMe/perfil.php";
      }
      else if(result==404){
        swal({
              type: 'error',
              title: '¡Error!',
              text: 'Usuario Incorrecto',
            });
      }
      else if(result==406){
        swal({
              type: 'error',
              title: '¡Error!',
              text: 'Contraseña Incorrecta',
            });
      }
      else{
        swal({
              type: 'error',
              title: '¡Error!',
              text: result,
            });
      }
    },
    error: function() {
      swal({
            type: 'error',
            title: '¡Error!',
            text: 'ERROR EN INGRESO',
          });
    }
  });
  event.preventDefault();
});


function cambiarEstatus(cve,est){
    $.ajax({
    type: 'POST',
    url:  'activarModo.php',
    data: {
      clave: cve,
      estatus: est,
    },
    success: function(data) {
      swal({
        type: 'success',
        title: '¡Hecho!',
        text: data
      })
      .then((value) => {
        location.reload();
      });
    },
    error: function() {
      swal({
        type: 'error',
        title: '¡Error!',
        text: 'Algo ha salido mal'
      });
    }
  });
}