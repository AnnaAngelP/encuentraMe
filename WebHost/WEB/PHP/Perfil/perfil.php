<?php 
session_start();
 ?>
<!DOCTYPE html>
<html>
<head>
	<title>EncuentraMe</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="../../CSS/perfil.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
	<!--<script src="../../SCRIPT/perfil.js"></script>-->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
 	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
 
</head>

<script> 
$(document).ready(function(){
        $("#flip1").click(function(){
            $("#panel").slideUp("slow");
        });
});
    $(document).ready(function(){
        $("#flip").click(function(){
            $("#panel").slideDown("slow");
        });
});


</script>

<body>
<div class="container">
    <nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">EncuentraMe</a>
    </div>
    <ul class="nav navbar-nav">
      <li class="active"><a href="#">Perfil</a></li>
      <li><a href="#">Configuración</a></li>
      <li><a href="#">Comunidad</a></li>
      <li><a href="#">Ayuda</a></li>
      <li><a href="#">Acerca de</a></li>
    </ul>
  </div>
</nav>

</div>	

	  	<?php
			include "../../../Conexion.php";
			//Perfil/PHP/WEB/
			$correo = $_SESSION["correo"];
			//$correo = "augustosanchez103@gmail.com";
			$cont = 1;
			$consulta="SELECT ma.nombre as marca, mo.nombre as modelo, disp.mac as mac FROM usuario as us 
			    INNER JOIN dispositivo as disp
						on disp.usuario_id_usuario=us.id_usuario
						INNER JOIN modelo as mo on mo.id_modelo=disp.modelo_id_modelo
						INNER JOIN marca as ma on ma.id_marca=mo.marca_id_marca
						WHERE us.correo='$correo'";
			$resultado=$conexion->query($consulta);
			if ($resultado != null) {
			    echo "<div class='container'>
			    <div class='row'>";
			    while($row = mysqli_fetch_array($resultado)){
					echo "<div class='col-sm-3'>
							<img src='../../images/tel.jpg' alt='Image' class='image'>
							<h4>Telefono ".$cont."</h4>
							  <div class='overlay'>
							  <ul style='margin-right:50px'>
							        <li class='list-group-item'>Marca: ".$row['marca']."</li>
                                    <li class='list-group-item'> ".$row['modelo']."</li>
                                    <li class='list-group-item'> ".$row['mac']."</li>
                                    
                                        
							  </ul>
							  </div>
							 
							    <div class='container'>
							     <h4 style='color:white''>Activar</h4>
                                        <button type='button' class='btn btn-default'>Modo Pérdido</button>
                                        <button type='button' class='btn btn-default'>Modo Robado</button>
                                </div>
							</div>"	;
							echo " <div class='col-sm-1'>        </div>";
                   	$cont = $cont+1;
				}
				echo "</div>";
			} else {
			    echo "Error updating record: " . $conexion->error;
			}

			$conexion->close();

		?>
	
		
    </div>
    <div class="container">
        <button class="btn" id="flip">Abrir Mapa</button>
        <button class="btn" id="flip1">Cerrar Mapa</button>
    </div>
    <div id="panel" class="container">
        <div id="googleMap" style="width:100%;height:400px;"></div>

    
    <script>
    function myMap() {
    var mapProp= {
        center:new google.maps.LatLng(51.508742,-0.120850),
        zoom:5,
    };
    var map=new google.maps.Map(document.getElementById("googleMap"),mapProp);
    }
    </script>
    
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB4a_v6T4Z2AwQEsjUQobCi70KsEWg1phM&callback=myMap"></script>
    
    
    
</body>
</html>