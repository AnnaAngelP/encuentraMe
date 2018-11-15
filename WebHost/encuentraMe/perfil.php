<?php

session_start();
/*$_SESSION['id_usuarioGestion'] = $data['id_usuario'];
	$_SESSION['UsuarioGestion'] = $data['nick'];
	$_SESSION['Nombre'] = $data['nombre'];
	$_SESSION['Correo'] = $data['correo'];*/

if(!isset($_SESSION['id_usuarioGestion'])){

  //header("Location: index.php");
  die();
}
if(isset($_SESSION['id_usuarioGestion'])){
  $clave = $_SESSION['id_usuarioGestion'];
}
if(isset($_SESSION['UsuarioGestion'])){
  $usuario = $_SESSION['UsuarioGestion'];
}
if(isset($_SESSION['Nombre'])){
  $nombre = $_SESSION['Nombre'];
}
if(isset($_SESSION['Correo'])){
  $correo = $_SESSION['Correo'];
}
if(!isset($bd)){
  include("mysql.php");
  $bd = new  MySQL;
}

$sql="SELECT * FROM dispositivo WHERE usuario_id_usuario = $clave";
$consulta=$bd->consulta($sql);
$datotelefono=$bd->fetch_array($consulta);
if(!empty($datotelefono)){
	$telefono=$datotelefono['numeroTel'];
	$estatus=$datotelefono['estatus'];
	$mac=$datotelefono['mac'];
}

$sql="SELECT *, MO.nombre modelo , MA.nombre marca FROM dispositivo D
INNER JOIN modelo MO ON MO.id_modelo = D.modelo_id_modelo
INNER JOIN marca MA ON MA.id_marca = MO.marca_id_marca
WHERE usuario_id_usuario = $clave";
  $arreglo=$bd->get_arreglo($sql);
  if(!empty($arreglo)){
    foreach ($arreglo as $key){
      $Row ['ClaveDispositivo'] = $key['id_dispositivo'];
	  $Row ['Telefono'] = $key['numeroTel'];
      $Row ['Estatus'] = $key['estatus'];
      $Row ['Mac'] = $key['mac'];
      $Row ['Modelo'] = $key['modelo'];
      $Row ['Marca'] = $key['marca'];
      $datosTelefonos[] = $Row;
    }
  }
  
  $sql1 = "SELECT count(*) as Total
  FROM dispositivo
  WHERE usuario_id_usuario = $clave
  GROUP BY usuario_id_usuario ;";
  $arreglo1=$bd->get_arreglo($sql1);
  if(!empty($arreglo1)){
    foreach ($arreglo1 as $key){
      $Row ['Total'] = $key['Total'];
      $dispositivosRegistrados[] = $Row;
    }
  }
  
  
?>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="description" content="">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>EncuentraMe!</title>
  <link rel="icon" href="img/core-img/favicon.ico">
  <link href="style.css" rel="stylesheet">
  <link href="css/responsive.css" rel="stylesheet">
  <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
  <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="funciones.js"></script>
</head>

<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<body>

  <header class="header_area animated">
      <div class="container-fluid">
          <div class="row align-items-center">
              <!-- Menu Area Start -->
              <div class="col-12 col-lg-10">
                  <div class="menu_area">
                      <nav class="navbar navbar-expand-lg navbar-light">
                          <!-- Logo -->
                          <!--<a class="navbar-brand" href="#"></a>-->
                          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#ca-navbar" aria-controls="ca-navbar" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
                          <!-- Menu Area -->
                          <div class="collapse navbar-collapse" id="ca-navbar">
                              <ul class="navbar-nav ml-auto" id="nav">
                                  <!--<li class="nav-item active"><a class="nav-link" href="#perfil">Perfil</a></li>-->
                                  <!--<li class="nav-item"><a class="nav-link" href="#comunidad">Comunidad</a></li>-->
                              </ul>
                              <div class="sing-up-button d-lg-none">
                                  <a href="#">Cerrar sesión</a>
                              </div>
                          </div>
                      </nav>
                  </div>
              </div>
              <!-- Signup btn -->
              <div class="col-12 col-lg-2">
                  <div class="sing-up-button d-none d-lg-block">
                      <a href="#" >Cerrar sesión</a>
                  </div>
              </div>
          </div>
      </div>
  </header>

  <section class="perfil_area clearfix" id="home">
      <div class="container h-100">
          <div class="row h-100 align-items-center">
              <div class="col-12 col-md">
                  <div class="wellcome-heading">
                      <h2>¡Bienvenido!</h2>
                      <h3>eM</h3>
                      <p>Te ayudaremos a encontrar tu teléfono</p>
                  </div>
              </div>
          </div>
      </div>
  </section>

  <div class="container main-secction">
    <div class="row">
      <div class="row user-left-part">
        <div class="col-md-3 col-sm-3 col-xs-12 user-profil-part pull-left">
          <div class="row ">
            <div class="col-md-12 col-md-12-sm-12 col-xs-12 user-image text-center">
              <img src="img_avatar2.png" class="rounded-circle">
            </div>

            <div class="row user-detail-row ml-auto mr-auto mt-3">
              <div class="col-md-12 col-sm-12 user-detail-section2 pull-left">
                <div class="border"></div>
                <p class="text-center">Dispositivos registrados</p>
                <h5 class="text-center"><?php foreach ($dispositivosRegistrados as $key) {
                  echo $key['Total'];
                } ?></h5>
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-9 col-sm-9 col-xs-12 pull-right profile-right-section">
          <div class="row profile-right-section-row">
            <div class="col-md-12 profile-header">
              <div class="row">
                <div class=" special_description_content col-md-8 col-sm-6 col-xs-6 profile-header-section1 pull-left">
                  <h2><?=$nombre;?></h2>
                  <!--<h5>Developer</h5>-->
                </div>
              </div>
            </div>
            <div class="col-md-12">
              <div class="row">
                <div class="col-md-12">
                  <ul class="nav nav-tabs" role="tablist">
                    <li class="nav-item">
                      <a class="nav-link active" href="#profile" role="tab" data-toggle="tab"><i class="fa fa-user-circle"></i> Datos del Usuario</a>
                    </li>
                    <li class="nav-item">
                      <a class="nav-link" href="#buzz" role="tab" data-toggle="tab"><i class="fa fa-mobile-phone"></i> Dispositivos</a>
                    </li>
                    <li class="nav-item">
                      <a class="nav-link" href="#mapa" role="tab" data-toggle="tab"><i class="fa fa-map"></i> Mapa</a>
                    </li>
                  </ul>

                  <!-- Tab panes -->
                  <div class="tab-content">
                    <div role="tabpanel" class="tab-pane fade show active mt-3 ml-3" id="profile">
                      <div class="row">
                        <div class="col-md-2">
                          <label>Nombre</label>
                        </div>
                        <div class="col-md-6">
                          <p><?=$nombre;?></p>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col-md-2">
                          <label>Email</label>
                        </div>
                        <div class="col-md-6">
                          <p><?=$correo;?></p>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col-md-2">
                          <label>Usuario</label>
                        </div>
                        <div class="col-md-6">
                          <p><?=$usuario;?></p>
                        </div>
                      </div>
                      <!--<div class="col-md-4 col-sm-4 col-xs-4 user-detail-section1 text-center mt-2">
                        <button id="btn-contact" (click)="clearModal()" data-toggle="modal" data-target="#contact" class="btn submit-btn follow">Actualizar datos</button>
                      </div>-->
                    </div>
                    <div role="tabpanel" class="tab-pane fade mt-3 ml-3" id="buzz">
                      <?php
                      foreach ($datosTelefonos as $key){?>
                        <div class="col-md-8 mt-3 ">
                          <h4>Telefono: <?= $key['Telefono']; ?></h4>
                          <?php if ($key['Estatus'] == 0) { echo "(¡A salvo!)</p>"; } 
                          else if ($key['Estatus'] == 1) { echo "(Perdido)</p>"; } 
                          else if ($key['Estatus'] == 3) { echo "(Robado)</p>"; } ?>
                          <div class="col-md-12 contenedor">
                            <img src='img/tel.jpg' alt='Image' class='image'>
                            <div class='overlay2'>
                              <div class="text">
                                Marca:<br> <?= $key['Marca']; ?><br>
                                Modelo:<br> <?= $key['Modelo']; ?><br>
                                Mac:<br> <?= $key['Mac']; ?><br>
                                <?php if ($key['Estatus'] == 0) { ?>
                                  <button type='button' class='btn btn-warning mt-2' onclick="cambiarEstatus(<?=$key['ClaveDispositivo'];?>,1)">Modo Perdido</button>
                                  <button type='button' class='btn btn-danger mt-2' onclick="cambiarEstatus(<?=$key['ClaveDispositivo'];?>,2)">Modo Robado</button>
                                <?php } else if ($key['Estatus'] == 1) { ?>
                                  <button type='button' class='btn btn-success mt-2' onclick="cambiarEstatus(<?=$key['ClaveDispositivo'];?>,0)">Recuperado</button>
                                  <button type='button' class='btn btn-danger mt-2' onclick="cambiarEstatus(<?=$key['ClaveDispositivo'];?>,2)">Modo Robado</button>
                                <?php } else if ($key['Estatus'] == 3) { ?>
                                  <button type='button' class='btn btn-success mt-2' onclick="cambiarEstatus(<?=$key['ClaveDispositivo'];?>,0)">Recuperado</button>
                                <?php } ?>
                              </div>
                            </div>
                          </div>
                        </div>
										  <?php }  ?>
                    </div>
                    <div role="tabpanel" class="tab-pane fade mt-3 ml-3" id="mapa">
                      <div id="googleMap" style="width:100%;height:400px;"></div>
                    </div>
                  </div>
                </div>
                <!--<div class="col-md-4 img-main-rightPart">
                  <div class="row">
                    <div class="col-md-12">
                      <div class="row image-right-part">
                        <div class="col-md-6 pull-left image-right-detail">
                          <h6>PUBLICIDAD</h6>
                        </div>
                      </div>
                    </div>
                    <a href="http://camaradecomerciozn.com/">
                      <div class="col-md-12 image-right">
                        <img src="http://pluspng.com/img-png/bootstrap-png-bootstrap-512.png">
                      </div>
                    </a>
                    <div class="col-md-12 image-right-detail-section2">

                    </div>
                  </div>
                </div>-->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="modal fade" id="contact" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="contact">Contactarme</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">×</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="txtFullname">Nombre completo</label>
            <input type="text" id="txtFullname" class="form-control">
          </div>
          <div class="form-group">
            <label for="txtEmail">Email</label>
            <input type="text" id="txtEmail" class="form-control">
          </div>
          <div class="form-group">
            <label for="txtPhone">Teléfono</label>
            <input type="text" id="txtPhone" class="form-control">
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
          <button type="button" class="btn btn-primary" (click)="openModal()" data-dismiss="modal">Guardar</button>
        </div>
      </div>
    </div>
  </div>
  <script>
    var customLabel = {
        restaurant: {
          label: 'R'
        },
        bar: {
          label: 'B'
        }
      };
  function myMap() {
  var mapProp= {
      center:new google.maps.LatLng(18.9832493,-98.19074769999999),
      zoom:9,
  };
  var map=new google.maps.Map(document.getElementById("googleMap"),mapProp);
   var direccion;
   var correo="<?=$_SESSION['Correo']?>"; ////AQUI VA EL CORREO PARA AGREGARLO A LA URL
   

    direccion="marcadores.php?correo="+correo;
    downloadUrl(direccion, function(data) {
            var xml = data.responseXML;
            var markers = xml.documentElement.getElementsByTagName('marker');
            Array.prototype.forEach.call(markers, function(markerElem) {
              var id = markerElem.getAttribute('id_ubicacion');
              var name = markerElem.getAttribute('id_dispositivo');
              var address = markerElem.getAttribute('address');
              var type = markerElem.getAttribute('type');
              var point = new google.maps.LatLng(
                  parseFloat(markerElem.getAttribute('lat')),
                  parseFloat(markerElem.getAttribute('lng')));

              var infowincontent = document.createElement('div');
              var strong = document.createElement('strong');
              strong.textContent = name
              infowincontent.appendChild(strong);
              infowincontent.appendChild(document.createElement('br'));

              var text = document.createElement('text');
              text.textContent = address
              infowincontent.appendChild(text);
              var icon = customLabel[type] || {};
              var marker = new google.maps.Marker({
                map: map,
                position: point,
                label: icon.label
              });
              marker.addListener('click', function() {
                infoWindow.setContent(infowincontent);
                infoWindow.open(map, marker);
              });
            });
          });
    }
    function downloadUrl(url, callback) {
        var request = window.ActiveXObject ?
            new ActiveXObject('Microsoft.XMLHTTP') :
            new XMLHttpRequest;

        request.onreadystatechange = function() {
          if (request.readyState == 4) {
            request.onreadystatechange = doNothing;
            callback(request, request.status);
          }
        };

        request.open('GET', url, true);
        request.send(null);
      }

      function doNothing() {}

  </script>
  <!--<script src="https://maps.googleapis.com/maps/api/js?key=https://maps.googleapis.com/maps/api/js?key=AIzaSyBCWsSKncAY2IIb1AVvuVi0YRa6Pi8be58&callback=initMap&callback=myMap"></script>-->
  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB4a_v6T4Z2AwQEsjUQobCi70KsEWg1phM&callback=myMap"></script>
</body>
</html>
