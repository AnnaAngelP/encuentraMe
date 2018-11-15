package uno.prueba.sanchez.augusto.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.sql.Array;
import java.util.Collections;
import java.util.List;

public class Registro extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>{

    RequestQueue rq,rqw;//= Volley.newRequestQueue(this);
    JsonObjectRequest jsonObjectRequest;
    private EditText ET_nombre;
    private EditText ET_nick;
    private EditText ET_correo;
    private EditText ET_password;
    private Spinner S_marca;
    private Spinner S_modelo;
    private TextView txt;

    String ArrayMarca[], ArrayModelo[],mac;
    int ArrayIdMarca[], ArrayIdModelo[];
    int id_marca, id_modelo, tam_marcas;
    int TamMarca = 0, TamModelo = 0;

    Registro()
    {
        ArrayMarca = new String[18];
       ArrayIdMarca = new int[18];
        //rq = Volley.newRequestQueue(this);
        //consultarMarcas();
        ArrayIdModelo = new int[18];
        ArrayModelo = new String [18];
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mac = getMacAddr();

        txt = (TextView)findViewById(R.id.txtMac);
        ET_nombre = (EditText)findViewById(R.id.TxtNombre);
        ET_nick = (EditText)findViewById(R.id.TxtNick);
        ET_correo = (EditText)findViewById(R.id.TxtCorreo);
        ET_password = (EditText)findViewById(R.id.TxtPassword);
        S_marca = (Spinner)findViewById(R.id.SpinnerMarca);
        S_modelo = (Spinner)findViewById(R.id.SpinnerModelo);
        tam_marcas=0;
        rq = Volley.newRequestQueue(this);
        rqw = Volley.newRequestQueue(this);
        //consultarMarcas();


        txt.setText(getMacAddr());
        for(int i=0; i<18; i++) {
            ArrayMarca[i] = "";
            ArrayModelo[i] = "";
        }
        /*for(int i=0; i<tam_marcas; i++) {
            ArrayMarca[i] = "";
            //ArrayModelo[i] = "";
        }*/
        ObtenerMarcaDB();

        //txt.setText(getMacAddr());
        ArrayAdapter <String> adapterMarca = new ArrayAdapter <String> (this, android.R.layout.simple_spinner_dropdown_item, ArrayMarca);
        S_marca.setAdapter(adapterMarca);
        S_marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(ArrayIdMarca[i] >= 1)
                {
                    id_marca = ArrayIdMarca[i];
                    ObtenerModeloDB(i);
                    ArrayAdapter<String> adapterModelo = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, ArrayModelo);
                    S_modelo.setAdapter(adapterModelo);
                    S_modelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            id_modelo = ArrayIdModelo[i];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public void ObtenerMarcaDB()
    {
        String url = "https://ingrid06.000webhostapp.com/ObtenerMarca.php";
        //String url="http://192.168.1.64:8080/App/ObtenerMarca.php";
        System.out.println(url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET , url, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONArray jsonArray = response.getJSONArray("total");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    TamMarca = Integer.parseInt(jsonObject.getString("tam"));
                    System.out.println(TamMarca);
                    //ArrayMarca = new String[TamMarca];
                    //ArrayIdMarca = new int[TamMarca];


                    jsonArray = response.getJSONArray("marca");
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        jsonObject = jsonArray.getJSONObject(i);
                        System.out.println();
                        ArrayIdMarca[i] = Integer.parseInt(jsonObject.getString("id_marca"));
                        ArrayMarca[i] = jsonObject.getString("nombre");
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(getApplicationContext(), "ERROR: " + error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonObjectRequest);

    }
    public void ObtenerModeloDB(int i)
    {
        String url = "https://ingrid06.000webhostapp.com/ObtenerModelo.php?id="+Integer.toString(ArrayIdMarca[i]);
        //String url="http://192.168.1.64:8080/App/ObtenerModelo.php?id="+Integer.toString(ArrayIdMarca[i]);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET , url, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONArray jsonArray = response.getJSONArray("modelo");
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ArrayIdModelo[i] = Integer.parseInt(jsonObject.getString("id_modelo"));
                        ArrayModelo[i] = jsonObject.getString("nombre");
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(getApplicationContext(), "ERROR: " + error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonObjectRequest);
    }

    public void Registrar(View view){
        String nombre = ET_nombre.getText().toString();
        String pass = ET_password.getText().toString();
        //if(nombre.length())
    }

    public void GuardarDatos(View view)
    {
        String nombre, nick, correo, password, modelo, p;
        nombre = ET_nombre.getText().toString();
        nick = ET_nick.getText().toString();
        correo = ET_correo.getText().toString();
        password = ET_password.getText().toString();

        if ((nombre.length() > 0) && (nick.length() > 0) && (correo.length() > 0) && (password.length() > 0)){
            //String url = "http://192.168.1.67:8080/EncuentraMe/Registro.php?nombre="+nombre+"&nick="+nick+"&correo="+correo+"&password="+password+"&id_marca="+id_marca+"&id_modelo="+id_modelo+"&mac="+mac;
            String url = "https://ingrid06.000webhostapp.com/Registro.php?nombre="+nombre+"&nick="+nick+"&correo="+correo+"&password="+password+"&id_marca="+id_marca+"&id_modelo="+id_modelo+"&mac="+mac;

            url = url.replaceAll(" ", "%20");

            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET , url, null,  new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("TAG", response.toString());
                    try
                    {
                        String marcaBD = "";
                        JSONArray jsonArray = response.getJSONArray("usuario");
                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if( jsonObject.getString("id_usuario") != "-1" ) {
                                Toast.makeText(getApplicationContext(), "Registro exitoso" , Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Registro.this,Principal.class);
                                intent.setAction(ET_correo.getText().toString());
                                //intent.setAction()
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error: " + jsonObject.getString("id_usuario") , Toast.LENGTH_LONG).show();

                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Log.e("TAG", error.getMessage(), error);
                    Toast.makeText(getApplicationContext(), "ERROR: " + error.toString(), Toast.LENGTH_LONG).show();

                }
            });
            //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rq.add(jsonObjectRequest);
        }
        else{
            Toast.makeText(getApplicationContext(),"Favor de llenar todos los campos",Toast.LENGTH_LONG).show();
        }

    }

    public void consultarMarcas(){
        //System.out.println("paso");
        //mac=getMac();
        //System.out.println(mac);
        String url="https://ingrid06.000webhostapp.com/NumeroMarcas.php";
        //String url="https://ingrid06.000webhostapp.com/busquedaMac.php?correo="+correo+"&mac="+mac;
        System.out.println(url);
        url = url.replaceAll(" ", "%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rqw.add(jsonObjectRequest);
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
        int n;
        String a;
        try {
            jsonArray = response.getJSONArray("marcas");
            for(int i=0; i<jsonArray.length(); i++) {
                System.out.println("lenght: "+jsonArray.length());
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                a =jsonObject.getString("numero");
                n = Integer.parseInt(a);
                tam_marcas = n;
                System.out.println(tam_marcas);
                ArrayIdMarca = new int[n];
                ArrayMarca = new String[n];
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
