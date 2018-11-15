<?php
		include("../../../Conexion.php");
		//include("PHP/Conexion/conexion.php");
		$correo=$_POST['correo'];
		$contraseña=$_POST['contraseña'];


		$sql="SELECT correo, password FROM usuario WHERE correo = '$correo'";
		$resultado = mysqli_query($conexion, $sql);
		if($sql)
		{
			$contraseñaDB="";
			while($reg = mysqli_fetch_array($resultado))
			{
				$contraseñaDB = $reg['password'];
			}
			if($contraseñaDB == $contraseña)
			{
				session_start();
				$_SESSION["correo"]=$correo;
				$_SESSION["password"]=$password;

				/*echo '<script> 
					swal
					({
						text: "Bienvenido '.$nombreProfesor.' '.$paternoProfesor .'",
					})
					.then((redireccionar) => 
					{
						if (redireccionar) 
						{
							location="PHP/Perfil/perfil.php";
						} 
					});
				</script>';*/
				echo '<script> 
					swal
					({
						text: "Bienvenido ",
					})
					.then((redireccionar) => 
					{
						if (redireccionar) 
						{
							location="PHP/Perfil/perfil.php";
						} 
					});
				</script>';
			}
			else if($contraseñaDB != "")
			{
				//Contraseña incorrecta
				echo '<script type="text/javascript">';
				echo 'setTimeout(function () { swal("Ops!", "La contraseña es incorrecta", "error");';
				echo '}, 100);</script>';
			}
			else
			{
			    //Correo incorrecto
    			echo '<script type="text/javascript">';
    			echo 'setTimeout(function () { swal("Ops!", "El correo electrónico es incorrecto", "error");';
    			echo '}, 100);</script>';
			}
		}
		else
		{
			//Correo incorrecto
			echo '<script type="text/javascript">';
			echo 'setTimeout(function () { swal("Ops!", "Intente mas tarde", "error");';
			echo '}, 100);</script>';
		}
?>
