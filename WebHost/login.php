<?php 
	require_once('Conexion.php');
	$json = array();
	$json1 = array();
	if (isset($_GET["correo"]) && isset($_GET["pwd"]) && isset($_GET["mac"])) {
		$correo=$_GET['correo'];
		$pwd=$_GET['pwd'];
		$mac = $_GET['mac'];
		//$conexion=new mysqli($hostname,$username,$password,$database);

		$consulta="SELECT * FROM usuario as us WHERE us.correo='$correo' AND us.password='$pwd'";
		$resultado=$conexion->query($consulta);
		
		$resulta=array();
		$resulta=mysqli_fetch_array($resultado,MYSQLI_ASSOC);
		if ($resulta == null) {
			$resulta['correo']=0;
			$resulta['password']=0;
			$resulta['respuesta']=0;
			$json['dato'][]=$resulta;
			echo json_encode($json);
			$resulta =null;
		}
		else{
		   // $json['dato'][]=$resulta;
		    //$result = array();
		    $consulta="SELECT disp.mac,us.correo as respuesta FROM usuario as us INNER JOIN dispositivo as disp 
		    on disp.usuario_id_usuario = us.id_usuario
		    WHERE us.correo='$correo' && disp.mac='$mac'";
		   	$resultado=$conexion->query($consulta);
		   
		    $result = array();
		    $result=mysqli_fetch_array($resultado,MYSQLI_ASSOC);
		    
		    if($result == null){
		        $json['correo']=$resulta['correo'];
        		$json['password']=$resulta['password'];
        		$json['respuesta']=0;
        		$json1['dato'][]=$json;
    			echo json_encode($json1);
    			$resultado =null;
		    }
		    else{
		        $json['correo']=$resulta['correo'];
        		$json['password']=$resulta['password'];
        		$json['respuesta']=$result['mac'];
        		$json1['dato'][]=$json;
    			echo json_encode($json1);
    			$resultado =null;
		    }
			
		}

	}
	else{
	$resultado = array();
		$resultado['correo']=0;
		$resultado['password']=0;
		$json['dato'][]=$resultado;
		//$json['dato']['password']=0;
		echo json_encode($json);
		$resultado =null;
	}
	mysqli_close($conexion);
 ?>