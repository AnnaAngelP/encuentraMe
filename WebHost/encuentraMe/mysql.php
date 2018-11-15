<?php
class MySQL
{
	private $conexion;
	private $total_consultas;
	public function MySQL()
	{
		if(!isset($this->conexion)){
			$this->conexion = (mysqli_connect("localhost","id5363087_root","17061997","id5363087_encuentrame")) or die(mysqli_error());
		}
	}
	public function consulta($consulta){
		$this->total_consultas++;
		$resultado = mysqli_query($this->conexion,$consulta);
		return $resultado;
	}
	function get_id(){
		return mysqli_insert_id($this->conexion);
	}
	function get_arreglo($consulta){
		$count=0;
		$resultado = mysqli_query($this->conexion,$consulta);
		if( !empty( $resultado ) ){
			if ($row = mysqli_fetch_array($resultado)){
				do{
					$var[$count] = $row;
					$count++;
				}
				while($row = mysqli_fetch_array($resultado));
				return $var;
			}
			else{
				//return $var;
			}
		}
	}

	public function fetch_array($consulta){
		return mysqli_fetch_array($consulta);
	}

	public function num_rows($consulta){
		return mysqli_num_rows($consulta);
	}

    public function getID(){
		return mysqli_insert_id($this->conexion);
	}

	public function getTotalConsultas(){
		return $this->total_consultas;
	}
	function get_error(){
		return $this->conexion->error;
	}
}
?>
