package uno.prueba.sanchez.augusto.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class AddDispositivo extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject>{
    RequestQueue rq;
    JsonObjectRequest jsonObjectRequest;
    TextView txt;
    String mac;
    View vista;

    private Spinner S_marca;
    private Spinner S_modelo;
    String ArrayMarca[], ArrayModelo[];
    int ArrayIdMarca[], ArrayIdModelo[];
    int id_marca, id_modelo;
    int TamMarca = 0, TamModelo = 0;
    public AddDispositivo() {
        // Required empty public constructor
    }
    public void inicializa(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState){
        vista = inflater.inflate(R.layout.fragment_dispositivo, container, false);
        rq = Volley.newRequestQueue(getContext());

        S_marca = (Spinner)vista.findViewById(R.id.SpinnerMarca);
        S_modelo = (Spinner)vista.findViewById(R.id.SpinnerModelo);


        ObtenerMarcaDB();


        //ArrayAdapter<String> adapterMarca = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ArrayMarca);
        ArrayAdapter<String> adapterMarca = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,ArrayMarca);
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
                    ArrayAdapter<String> adapterModelo = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, ArrayModelo);
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

        txt = (TextView) vista.findViewById(R.id.textMac);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment txt = (TextView) findViewById(R.id.textprueba);
        inicializa(inflater,container,savedInstanceState);

       // txt = (TextView) container.findViewById(R.id.textMac);
       // mac = getMac();
        txt.setText(getMac());
        return vista;
    }
    public void registrar(View view){

    }

    public void ObtenerMarcaDB()
    {
        //String url = "https://ingrid06.000webhostapp.com/ObtenerMarca.php";
        String url = "http://192.168.1.64:8080/App/ObtenerMarca.php";
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
                Toast.makeText(getContext(), "ERROR: " + error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonObjectRequest);

    }
    public void ObtenerModeloDB(int i)
    {
        //String url = "https://ingrid06.000webhostapp.com/ObtenerModelo.php?id="+Integer.toString(ArrayIdMarca[i]);
        String url = "http://192.168.1.64:8080/App/ObtenerModelo.php?id="+Integer.toString(ArrayIdMarca[i]);
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
                Toast.makeText(getContext(), "ERROR: " + error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonObjectRequest);
    }
    private String getMac(){
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

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
