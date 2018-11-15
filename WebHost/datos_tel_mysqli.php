<?php 
	require_once('Conexion.php');
	$json = array();
	if (isset($_GET["correo"]) && isset($_GET["mac"])) {
		$correo=$_GET['correo'];
		$mac=$_GET['mac'];
		//$conexion=new mysqli($hostname,$username,$password,$database);

		$consulta="SELECT ma.nombre as marca,mo.nombre as modelo, us.nick as nick, us.password as pass , us.nombre as nombre FROM usuario as us inner join dispositivo as disp on us.id_usuario=disp.usuario_id_usuario inner join modelo as mo on disp.modelo_id_modelo=mo.id_modelo inner join marca as ma on ma.id_marca=mo.marca_id_marca WHERE us.correo='$correo' AND disp.mac='$mac'";
		$resultado=$conexion->query($consulta);
		//print("<table>");
		$resulta=array();
		$resulta=mysqli_fetch_array($resultado,MYSQLI_ASSOC);

		if($resulta==null){
				$resulta['modelo']=0;
				$resulta['marca']=0;
				$resulta['nick']=0;
				$resulta['pass']=0;
				$resulta['nombre']=0;
		}
		
			$json['dato'][]=$resulta;
		echo json_encode($json);
		//echo $json;
		$resultado =null;
			//$json['datos'][]=mysqli_fetch_array($resultado,MYSQLI_ASSOC);

			

	}
	else{
		$resulta=array();
		$resulta['modelo']=0;
		$resulta['marca']=0;
		$resulta['nick']=0;
		$resulta['pass']=0;
		$resulta['nombre']=0;
		
		$json['dato'][]=$resulta;
		echo json_encode($json);
		//echo $json;
		$resultado =null;
		
	}
 ?>