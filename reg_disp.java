package uno.prueba.sanchez.augusto.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class reg_disp extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    RequestQueue rq;
    JsonObjectRequest jsonObjectRequest;
    private TextView txt;
    private Spinner S_marca;
    private Spinner S_modelo;

    String ArrayMarca[], ArrayModelo[],correo,mac,m,c;
    int ArrayIdMarca[], ArrayIdModelo[];
    int id_marca, id_modelo;
    int TamMarca = 0, TamModelo = 0;

    Boolean band;

    reg_disp()
    {
        ArrayMarca = new String[18];
        ArrayIdMarca = new int[18];
        ArrayIdModelo = new int[18];
        ArrayModelo = new String [18];
        //rq = Volley.newRequestQueue(this);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        //setContentView(R.layout.activity_reg_disp);
        band=false;
        rq = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        correo = intent.getAction();

        consultar();

        if (band == true){
            //SI ESTA REGISTRADO Y PASA DIRECTAMENTE A LA PRINCIPAL
        }
        else
        {
            setContentView(R.layout.activity_reg_disp);
            //AUN NO ESTA REGISTRADO EL TELEFONO
            txt = (TextView)findViewById(R.id.textMac);
            txt.setText(getMacAddr());
            S_marca = (Spinner)findViewById(R.id.spinnerMarca);
            S_modelo = (Spinner)findViewById(R.id.spinnerModelo);

            rq = Volley.newRequestQueue(this);

            for(int i=0; i<18; i++) {
                ArrayMarca[i] = "";
                ArrayModelo[i] = "";
            }
            ObtenerMarcaDB();


            ArrayAdapter<String> adapterMarca = new ArrayAdapter <String> (this, android.R.layout.simple_spinner_dropdown_item, ArrayMarca);
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
    }

    public void ObtenerMarcaDB()
    {
        String url = "https://ingrid06.000webhostapp.com/ObtenerMarca.php";
        //String url = "https://192.168.1.64:8080/App/ObtenerMarca.php";
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
        //String url = "https://192.168.1.64:8080/App/ObtenerModelo.php?id="+Integer.toString(ArrayIdMarca[i]);
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

    public void GuardarDatos(View view)
    {


            //String url = "http://192.168.1.67:8080/EncuentraMe/Registro.php/";
            String url = "https://ingrid06.000webhostapp.com/Registro_disp.php?correo="+correo+"&id_marca="+id_marca+"&id_modelo="+id_modelo+"&mac="+getMacAddr();
            //String url = "https://192.168.1.64:8080/App/Registro_disp.php?correo="+correo+"&id_marca="+id_marca+"&id_modelo="+id_modelo;
            url = url.replaceAll(" ", "%20");
            System.out.println(url);
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
                                Intent intento = new Intent(reg_disp.this,Principal.class);
                                intento.setAction(correo);
                                startActivity(intento);
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

    private void consultar(){
        System.out.println("paso");
        mac=getMacAddr();
        System.out.println(mac);
        //String url = "https://192.168.1.64:8080/App/busquedaMac.php?correo="+correo+"&mac="+mac;
        String url="https://ingrid06.000webhostapp.com/busquedaMac.php?correo="+correo+"&mac="+mac;
        System.out.println(url);
        url = url.replaceAll(" ", "%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jsonObjectRequest);
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
                m = jsonObject.getString("mac");
                c=jsonObject.getString("correo");
                if ((correo.equals(c)) &&(m.equals(mac))) {
                    Toast.makeText(getApplicationContext(), "Su telefono ya está registrado" , Toast.LENGTH_LONG).show();
                    band = true;
                    Intent intento = new Intent(reg_disp.this,Principal.class);
                    intento.setAction(correo);
                    startActivity(intento);
                }
                else {
                    band = false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_disp);
    }*/
}
