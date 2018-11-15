package uno.prueba.sanchez.augusto.login;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static uno.prueba.sanchez.augusto.login.reg_disp.getMacAddr;

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>{
    private EditText correo, password;
    RequestQueue rqw;
    JsonObjectRequest jsonOR;
    private String c,pass,mac;
    String n,p;
    Boolean band =false;
    //private TextView info;
    private Button login, registro;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = (EditText) findViewById(R.id.nick);
        password = (EditText) findViewById(R.id.password);
        //info = (TextView) findViewById(R.id.tvInfo);
        login = (Button) findViewById(R.id.login);
        registro = (Button) findViewById(R.id.registro);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rqw = Volley.newRequestQueue(this);

    }
    public void Registrarse(View view){
        Intent intent = new Intent(MainActivity.this,Registro.class);
        startActivity(intent);
    }
    public void RecuperarPass(View view){
        Intent intent = new Intent(MainActivity.this,Recuperar_contra.class);
        startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void login(View view) {
        n = correo.getText().toString();
        p = password.getText().toString();
        if ((n.length() > 0) && (p.length() > 0)){
            iniciarConsulta(n,p);
            //progressBar.onVisibilityAggregated(true);
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                progressBar.onVisibilityAggregated(true);
                progressBar.drawableHotspotChanged(10,10);
                if (band == true){
                    Toast.makeText(getApplicationContext(), "Bienvenido" , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,Principal.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Datos incorrectos" , Toast.LENGTH_LONG).show();
                }
            }
            Toast.makeText(getApplicationContext(), "Bienvenido" , Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this,Principal.class);
            startActivity(intent);*/
        }
        else{
            Toast.makeText(getApplicationContext(), "Debes de llenar todos los campos" , Toast.LENGTH_LONG).show();
        }
        //iniciarConsulta();
    }
    public void iniciarConsulta(String email, String contra){
        System.out.println("paso");
        String url="https://ingrid06.000webhostapp.com/login.php?correo="+email+"&pwd="+contra+"&mac="+getMacAddr();
        //String url="http://192.168.1.64:8080/App/login.php?correo="+email+"&pwd="+contra;


        System.out.println(url);
        url = url.replaceAll(" ", "%20");
        //jrq= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        jsonOR=new JsonObjectRequest(Request.Method.GET,url,null,this,this);

        rqw.add(jsonOR);



    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error instanceof NetworkError) {
        } else if (error instanceof ServerError) {
            Toast.makeText(getApplicationContext(),
                    "Oops. Server error!",
                    Toast.LENGTH_LONG).show();
            System.out.println("Oops. Server error!");
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(getApplicationContext(),
                    "Oops. AuthFailureError!",
                    Toast.LENGTH_LONG).show();
            System.out.println("Oops. AuthFailureError!");
        } else if (error instanceof ParseError) {
            Toast.makeText(getApplicationContext(),
                    "Oops. ParseError error!   " + error.getMessage(),
                    Toast.LENGTH_LONG).show();
            System.out.println("Oops. ParseError error!   ");
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(getApplicationContext(),
                    "Oops. NoConnectionError error!",
                    Toast.LENGTH_LONG).show();
            System.out.println("Oops. NoConnectionError error!");
        } else if (error instanceof TimeoutError) {
            System.out.println("Oops. Timeout error!");
            Toast.makeText(getApplicationContext(),
                    "Oops. Timeout error!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        System.out.println("entro");
        System.out.println("paso");
        JSONArray jsonArray= null;
        try {
            jsonArray = response.getJSONArray("dato");
            for(int i=0; i<jsonArray.length(); i++) {
                System.out.println("lenght: "+jsonArray.length());
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                pass = jsonObject.getString("password");
                c=jsonObject.getString("correo");
                String respuesta = "no";//RESPUESTA DEL ARCHIVO PHP PARA SABER SI ESTA REGISTRADO EL DISPOSITIVO
                if ((n.equals(c)) &&(p.equals(pass))) {
                    band = true;
                    respuesta = jsonObject.getString("respuesta");
                    if (respuesta.length()>1){
                        Toast.makeText(getApplicationContext(), "Bienvenido" , Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this,Principal.class);
                        intent.setAction(c);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Antes de Inggresar debe de registrar su telefono" , Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this,reg_disp.class);
                        intent.setAction(c);
                        startActivity(intent);
                    }



                }
                else if ((n.equals(c)) && (p.compareTo(pass) != 0)){
                    Toast.makeText(getApplicationContext(),"Contraseña incorrecta ", Toast.LENGTH_SHORT).show();
                }
                else if((n.compareTo(c) != 0) &&(p.equals(pass))){
                    Toast.makeText(getApplicationContext(),"Correo incorrecto", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Datos Incorrectos ", Toast.LENGTH_SHORT).show();
                }
                System.out.println("nick: "+c);
                System.out.println("password: "+pass);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void consultar(){
       /* System.out.println("paso");
        mac=getMacAddr();
        System.out.println(mac);
        //String url = "https://192.168.1.64:8080/App/busquedaMac.php?correo="+correo+"&mac="+mac;
        String url="https://ingrid06.000webhostapp.com/busquedaMac.php?correo="+correo+"&mac="+mac;
        System.out.println(url);
        url = url.replaceAll(" ", "%20");
        jsonOR=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rqw.add(jsonOR);*/


    }
}
