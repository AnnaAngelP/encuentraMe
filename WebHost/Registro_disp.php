<?php
	include("Conexion.php");
	if( isset($_GET["correo"]) && isset($_GET["id_marca"]) && isset($_GET["id_modelo"]) && isset($_GET["mac"]))
	{
		$json = array();
		$correo = $_GET['correo'];
		$id_marca = $_GET['id_marca'];
		$id_modelo = $_GET['id_modelo'];
		$mac = $_GET['mac'];


		$consulta = "SELECT id_usuario FROM usuario WHERE correo = '$correo' ";
		$resultado = mysqli_query($conexion, $consulta);
		if($consulta)
		{
			while($reg = mysqli_fetch_array($resultado))
			{
				$id_usuario = $reg['id_usuario'];
			}

			$consulta = "INSERT INTO dispositivo(usuario_id_usuario, modelo_id_modelo,mac) VALUES('$id_usuario', '$id_modelo','$mac')";
			mysqli_query($conexion, $consulta);
			//mysqli_close($conexion);
			
			$results["id_usuario"] = $id_usuario;
			$json['usuario'][] = $results;
			echo json_encode($json);
		}
		else
		{
			$results["id_usuario"] = '-1';
			$json['usuario'][] = $results;
			echo json_encode($json);
		}

	}
	else
	{
		$results["id_usuario"] = 'Llena todos los campos';
		$json['usuario'][] = $results;
		echo json_encode($json);
	} 
?>