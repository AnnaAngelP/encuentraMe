<?php
class Database
{
	private static $dbName = 'id5363087_encuentrame';
	private static $dbHost = 'localhost';
	private static $dbUserName = 'id5363087_root';
	private static $dbPassword = '17061997';

	private static $cont  = null;

	public function __construct()
	{
		exit('Init function is not allowed');
	}

	public static function connect()
	{
       if ( null == self::$cont )
       {
        try
        {
          self::$cont =  new PDO( "mysql:host=".self::$dbHost.";"."dbname=".self::$dbName, self::$dbUserName, self::$dbPassword,array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8"));
        }
        catch(PDOException $e)
        {
          die($e->getMessage());
        }
       }
       return self::$cont;
	}

	public static function disconnect()
	{
		self::$cont = null;
	}
}
?>
