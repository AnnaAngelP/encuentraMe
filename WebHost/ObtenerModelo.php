<?php
	include("Conexion.php");
	if( isset($_GET["id"]) )
	{
		$json = array();
		$id = $_GET['id'];
		$consulta = "SELECT id_modelo, nombre FROM modelo WHERE marca_id_marca = '$id' ";
		$resultado = mysqli_query($conexion, $consulta);
		if($consulta)
		{
			while($reg = mysqli_fetch_array($resultado))
			{
				$json['modelo'][] = $reg;
			}
			mysqli_close($conexion);
			echo json_encode($json);
		}
		else
		{
			$results["id_modelo"] = ' ';
			$results["nombre"] = ' ';
			$json['datos'][] = $results;
			echo json_encode($json);
		}
	}
	else
	{
		$results["id_modelo"] = ' ';
		$results["nombre"] = ' ';
		$json['modelo'][] = $results;
		echo json_encode($json);
	} 
?>