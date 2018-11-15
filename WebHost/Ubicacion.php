<?php
	include("Conexion.php");
	if( isset($_GET["direccion"]) && isset($_GET["latitud"]) && isset($_GET["longitud"]) && isset($_GET["mac"]) && isset($_GET["correo"]))
	{
	    $direccion = $_GET['direccion'];
		$latitud = $_GET['latitud'];
		$longitud = $_GET['longitud'];
		$mac = $_GET['mac'];
		$correo = $_GET['correo'];
		$id_dispositivo = -1;

		$query = "SELECT id_dispositivo FROM usuario AS u INNER JOIN dispositivo AS d ON u.id_usuario =d.usuario_id_usuario WHERE u.correo = '$correo' AND d.mac = '$mac' ";
		$resultado = mysqli_query($conexion, $query);
		if($query)
		{
			while($reg = mysqli_fetch_array($resultado))
			{
				$id_dispositivo = $reg['id_dispositivo'];
			}
			if($id_dispositivo != -1)
			{
    			$query = "INSERT INTO ubicacion(direccion, latitud, longitud, dispositivo_id_dispositivo) VALUES('$direccion','$latitud', '$longitud', '$id_dispositivo')";
    			mysqli_query($conexion, $query);
			}
		}
	}
?>