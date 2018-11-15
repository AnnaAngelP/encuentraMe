package uno.prueba.sanchez.augusto.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class Configuracion extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject>{

    RequestQueue rq;
    JsonRequest jrq;
    private String correo, nick, pass, nvoPass;
    private TextView txtPass;
    private EditText cajaNick, cajaNvoPass;
    private int id;
    private Button btn;
    View vista;
    public Configuracion() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inicializa(inflater,container);
        rq= Volley.newRequestQueue(getContext());
        consultar();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rq= Volley.newRequestQueue(getContext());
                System.out.println();
                nvoPass = cajaNvoPass.getText().toString();
                nick=cajaNick.getText().toString();
                String url = "https://ingrid06.000webhostapp.com/CambiarDatos.php?id_usuario="+id+"&nick="+nick+"&password="+nvoPass;
                System.out.println(url);
                url = url.replaceAll(" ", "%20");

                jrq = new JsonObjectRequest(Request.Method.GET , url, null,  new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        try
                        {
                            String resp = "";
                            JSONArray jsonArray = response.getJSONArray("dato");
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                resp=jsonObject.getString("a");
                                if( resp.length()>1 ) {
                                    Toast.makeText(getContext(), "Actualizacion exitosa" , Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(getContext(), "Ha ocurrido un error: " + jsonObject.getString("id_usuario") , Toast.LENGTH_LONG).show();

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
                        Toast.makeText(getContext(), "ERROR: " + error.toString(), Toast.LENGTH_LONG).show();

                    }
                });
                //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);

                jrq.setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                rq.add(jrq);
            }
        });
        return vista;

    }
    public void inicializa(LayoutInflater inflater, ViewGroup container){
        vista = inflater.inflate(R.layout.fragment_configuracion,container,false);
        txtPass = (TextView) vista.findViewById(R.id.txtPass);
        cajaNick = (EditText) vista.findViewById(R.id.cajaNick);
        cajaNvoPass = (EditText) vista.findViewById(R.id.cajaPass);
        btn = (Button) vista.findViewById(R.id.btnAceptar);
        //consultar();
    }
    public void setCorreo(String c){
        correo = c;
    }

    public void consultar(){
        System.out.println(correo);

        String url="https://ingrid06.000webhostapp.com/Obtener_datos_usuario.php?correo="+correo;

        System.out.println(url);
        //jrq= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        jrq=new JsonObjectRequest(Request.Method.GET,url,null,this,this);



        rq.add(jrq);
    }

    public void cambiarDatos(View view){

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"No se ha encontrado el usuario " + error.getMessage(), Toast.LENGTH_SHORT).show();
        System.out.println(error.getStackTrace());
        if (error instanceof NetworkError) {
        } else if (error instanceof ServerError) {
            Toast.makeText(getContext(),
                    "Oops. Server error!",
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(getContext(),
                    "Oops. AuthFailureError!",
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(getContext(),
                    "Oops. ParseError error!   " + error.getMessage(),
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(getContext(),
                    "Oops. NoConnectionError error!",
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(getContext(),
                    "Oops. Timeout error!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        System.out.println("entro");
        //Toast.makeText(getContext(),"Se ha encontrado el usuario" + cajaUser.getText().toString(), Toast.LENGTH_SHORT).show();
        System.out.println("paso");
        JSONArray jsonArray= null;
        try {
            jsonArray = response.getJSONArray("dato");
            for(int i=0; i<jsonArray.length(); i++) {
                System.out.println("lenght: "+jsonArray.length());
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                nick = jsonObject.getString("nick");
                pass = jsonObject.getString("password");
                id = Integer.valueOf(jsonObject.getString("id_usuario"));
                cajaNvoPass.setText(pass);
                cajaNick.setText(nick);
                txtPass.setText(pass);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
