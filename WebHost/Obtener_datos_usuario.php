<?php 
	require_once('Conexion.php');
	$json = array();
	if (isset($_GET["correo"])) {
		$correo=$_GET['correo'];
		//$conexion=new mysqli($hostname,$username,$password,$database);

		$consulta="SELECT * FROM usuario WHERE correo='$correo'";
		$resultado=$conexion->query($consulta);
		$resulta=array();
		$resulta=mysqli_fetch_array($resultado,MYSQLI_ASSOC);
		$json['dato'][]=$resulta;
		echo json_encode($json);
		$resultado =null;

	}
	else{
		$json['dato'][]=null;
		//$json['dato']['password']=0;
		echo json_encode($json);
		$resultado =null;
	}
	mysqli_close($conexion);
 ?>