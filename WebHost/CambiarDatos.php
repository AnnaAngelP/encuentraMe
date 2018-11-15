<?php 
	require_once('Conexion.php');
	$json = array();
	$resulta = array();
	$resulta['a']=0;
	$resulta['b']=0;
	if (isset($_GET["id_usuario"]) && isset($_GET["nick"]) && isset($_GET["password"])) {
		$id_usuario=$_GET['id_usuario'];
		$pwd=$_GET['password'];
		$nick=$_GET['nick'];

		$consulta=	"UPDATE usuario
					SET nick = '$nick', password = '$pwd'
					WHERE id_usuario='$id_usuario'";
		if ($conexion->query($consulta) === TRUE) {
		    $resulta['a']=123456789;
	        $resulta['b']=123456789;
		    $json['dato'][]=$resulta;
			echo json_encode($json);
		} else {
		   // echo "Error updating record: " . $conexion->error;
		    $json['dato'][]=$resulta;
			echo json_encode($json);
		}

		//$conexion->close();

	}
	else{
		$json['dato'][]=$resulta;
		echo json_encode($json);
	}
	mysqli_close($conexion);
 ?>