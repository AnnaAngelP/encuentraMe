<?php
	include("Conexion.php");
	if( isset($_GET["correo"]) )
	{
		$json = array();
		$correo = $_GET['correo'];
		$consulta = "SELECT * FROM usuario WHERE correo = '$correo' ";
		$resultado = mysqli_query($conexion, $consulta);
		//$resultado=$conexion-->query($consulta);
		//print("<table>");
		$resulta=array();
		$resulta=mysqli_fetch_array($resultado,MYSQLI_ASSOC);

		if($resulta==null){
				$resulta['correo']=0;
				$resulta['password']=0;
		}
		
			$json['dato'][]=$resulta;
		echo json_encode($json);
		//echo $json;
		$resultado =null;
	}
	else
	{
		$resulta=array();
		$resulta['correo']=0;
		$resulta['password']=0;
		$json['dato'][] = $resulta;
		echo json_encode($json);
	} 
?>