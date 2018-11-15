<?php
require("../Conexion.php");
//require("phpsqlajax_dbinfo.php");


  if( isset($_GET["correo"]) )
  {
     // echo 'paso<br>';
    $correo=$_GET['correo'];
      function parseToXML($htmlStr)
      {
      $xmlStr=str_replace('<','&lt;',$htmlStr);
      $xmlStr=str_replace('>','&gt;',$xmlStr);
      $xmlStr=str_replace('"','&quot;',$xmlStr);
      $xmlStr=str_replace("'",'&#39;',$xmlStr);
      $xmlStr=str_replace("&",'&amp;',$xmlStr);
      return $xmlStr;
      }
//echo 'paso<br>';
      $consulta = "SELECT id_ubicacion,MAX(direccion) as address,latitud as lat,longitud as lon ,MAX(fecha) as fecha, id_dispositivo 
                  FROM ubicacion as ub 
                  INNER JOIN dispositivo ON dispositivo.id_dispositivo = ub.dispositivo_id_dispositivo 
                  INNER JOIN usuario ON usuario.id_usuario=dispositivo.usuario_id_usuario 
                  WHERE usuario.correo='$correo' 
                  GROUP BY id_dispositivo 
                  ORDER by fecha ASC;";
                  
                  //echo 'paso<br>';
                  
          $resultado = mysqli_query($conexion, $consulta);
          if($consulta)
          {
              //echo 'paso<br>';
            
            header("Content-type: text/xml");
            // Start XML file, echo parent node
            echo "<?xml version='1.0' ?>";
            echo '<markers>';
            $ind=0;
            // Iterate through the rows, printing XML nodes for each
            while ($row = @mysqli_fetch_assoc($resultado)){
              // Add to XML document node
              echo '<marker ';
              echo 'id_dispositivo="' . $row['id_dispositivo'] . '" ';
              echo 'id_ubicacion="' . $row['id_ubicacion'] . '" ';
              echo 'address="' . parseToXML($row['address']) . '" ';
              echo 'lat="' . $row['lat'] . '" ';
              echo 'lng="' . $row['lon'] . '" ';
              echo 'fecha="' . $row['fecha'] . '" ';
              echo '/>';
              $ind = $ind + 1;
            }

            // End XML file
            echo '</markers>';

          }
          else
          {
            header("Content-type: text/xml");

            // Start XML file, echo parent node
            echo "<?xml version='1.0' ?>";
            echo '<markers>';
              echo '<marker ';
              echo 'id_dispositivo=0 ';
              echo 'id_ubicacion=0 ';
              echo 'address=0';
              echo 'lat=0';
              echo 'lng=0';
              echo 'fecha=0 ';
              echo '/>';

            // End XML file
            echo '</markers>';
          }

  }
  else
  {
      header("Content-type: text/xml");
      echo "<?xml version='1.0' ?>";
      echo '<markers>';
      echo '<marker ';
      echo 'id_dispositivo=0 ';
      echo 'id_ubicacion=0 ';
      echo 'address=0';
      echo 'lat=0';
      echo 'lng=0';
      echo 'fecha=0 ';
      echo '/>';
      // End XML file
      echo '</markers>';
  }
?>