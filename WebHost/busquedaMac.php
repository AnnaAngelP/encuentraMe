<?php 
	require_once('Conexion.php');
	$json = array();
	if (isset($_GET["correo"]) && isset($_GET["mac"])) {
		$correo=$_GET['correo'];
		$mac=$_GET['mac'];
		//$conexion=new mysqli($hostname,$username,$password,$database);

		
		$consulta="SELECT us.correo,disp.mac FROM `usuario` as us 
				INNER join `dispositivo` as disp on 
				us.id_usuario = disp.usuario_id_usuario 
				WHERE us.correo = '$correo' 
				AND disp.mac='$mac'";
		$resultado=$conexion->query($consulta);
		//print("<table>");
		$resulta=array();
		$resulta=mysqli_fetch_array($resultado,MYSQLI_ASSOC);
		if ($resulta == null) {
			$resulta['correo']=0;
			$resulta['mac']=0;
			$json['dato'][]=$resulta;
			echo json_encode($json);
			$resulta =null;
			$resulta = null;
		}
		else{
			$json['dato'][]=$resulta;
			echo json_encode($json);
			$resultado =null;
		}

	}
	else{
	$resultado = array();
		$resultado['correo']=0;
		$resultado['mac']=0;
		$json['dato'][]=$resultado;
		//$json['dato']['password']=0;
		echo json_encode($json);
		$resultado =null;
	}
	mysqli_close($conexion);
 ?>