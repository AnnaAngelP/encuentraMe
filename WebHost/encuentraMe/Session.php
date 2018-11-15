<?php
header('Content-Type: text/html; charset=utf-8');
setlocale(LC_TIME,'spanish');
session_start();

if (isset($_SERVER['HTTP_ORIGIN'])) {
	header("Access-Control-Allow-Origin: {$_SERVER['HTTP_ORIGIN']}");
	header('Access-Control-Allow-Credentials: true');
	header('Access-Control-Max-Age: 86400');
}

if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
	if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_METHOD'])){
		header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
	}
	if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS'])){
		header("Access-Control-Allow-Headers: {$_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']}");
	}
}
include 'Conexion.php';
if(empty($_POST)){
	echo 'null';
	exit();
}
else{
	$nombre=$_POST['username'];
	$password=$_POST['userpsw'];
}
$pass_sn=$password;
$password=($password);
$pdo = Database::connect();
$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$sql = "SELECT id_usuario , nick , password , correo , nombre
		FROM usuario WHERE nick = ?";
$q = $pdo->prepare($sql);
try
{
	$q->execute(array($nombre));
	$data = $q->fetch(PDO::FETCH_ASSOC);
	if($data == null){
		Database::disconnect();
		echo "404";//data empty
		exit();
	}
	if($nombre != $data['nick'] ){
		Database::disconnect();
		echo "404";//correo no registrado
		exit();
	}
	if( $password !=  $data['password']  ){
	//if(  $password == $data['Pass'] ){
		//echo $password.' '.$data['Pass'] . ' ';
		Database::disconnect();
		echo "406";//password incorrecto
		exit();
	}
	$_SESSION['id_usuarioGestion'] = $data['id_usuario'];
	$_SESSION['UsuarioGestion'] = $data['nick'];
	$_SESSION['Nombre'] = $data['nombre'];
	$_SESSION['Correo'] = $data['correo'];

	Database::disconnect();
	echo 1;
}
catch(PDOException $e){
	echo $e;
}
