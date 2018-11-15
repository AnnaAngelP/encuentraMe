<?php
	include("Conexion.php");
	

		$json = array();
		$tam = 0;
		$consulta = "SELECT id_marca, nombre FROM marca ";
		$resultado = mysqli_query($conexion, $consulta);
		if($consulta)
		{
			
			while($reg = mysqli_fetch_array($resultado))
			{
				$json['marca'][] = $reg;
				$tam += 1; 
			}
			$resultsTam['tam'] = $tam;
			$json['total'][] = $resultsTam;


			mysqli_close($conexion);
			echo json_encode($json);

		}
		else
		{
			$results["id_marca"] = ' ';
			$results["nombre"] = ' ';
			$json['marca'][] = $results;

			$resultsTam['tam'] = $tam;
			$json['total'][] = $resultsTam;


			echo json_encode($json);
		}
	
?>