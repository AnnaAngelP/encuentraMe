package uno.prueba.sanchez.augusto.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Perfil extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    TextView mensaje1;
    TextView mensaje2;
    String direccion;
    double latitud = 0;
    double longitud = 0;
    //String mac = "";
    //String correo = "";
    RequestQueue rq;
    JsonRequest jrq;
    String user = "", pass = "", marca = "", modelo = "", mac = "", correo = "", nombre;

    View vista;

    TextView cajaUser, cajaPass, cajaMarca, cajaModelo, cajaMac, cajaCorreo;
    //Button consultar;
    Usuario us;

    public void inicializa(LayoutInflater inflater, ViewGroup container) {
        us = new Usuario();
        vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        cajaUser = (TextView) vista.findViewById(R.id.nick);
        cajaPass = (TextView) vista.findViewById(R.id.txtPass);
        cajaMarca = (TextView) vista.findViewById(R.id.marca);
        cajaModelo = (TextView) vista.findViewById(R.id.modelo);
        cajaCorreo = (TextView) vista.findViewById(R.id.txtCorreo);
        cajaMac = (TextView) vista.findViewById(R.id.mac);
        mensaje1 = (TextView) vista.findViewById(R.id.mensaje_id);
        mensaje2 = (TextView) vista.findViewById(R.id.mensaje_id2);
    }

    public void agregaDatosACajas(String usuario, String pwd, String ma, String mo) {
        cajaUser.setText(usuario);
        cajaPass.setText(pass);
        cajaMarca.setText(ma);
        cajaModelo.setText(mo);
        cajaMac.setText(getMacAddr());
        cajaCorreo.setText(correo);

        us.setModelo(mo);
        us.setMarca(ma);
        us.setPassword(pass);
        us.setNick(usuario);

        mac = getMacAddr();


        System.out.println("nick: " + us.getNick());
        System.out.println("pass: " + us.getPassword());
        System.out.println("marca: " + us.getMarca());
        System.out.println("modelo:" + us.getModelo());
        System.out.println("mac:" + mac);


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
                    res1.append(String.format("%02X:", b));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View vista;

        inicializa(inflater, container);
        rq = Volley.newRequestQueue(getContext());
        iniciarConsulta();

        return vista;
    }

    public void iniciarConsulta() {
        System.out.println(correo);

        String url = "https://ingrid06.000webhostapp.com/datos_tel_mysqli.php?correo=" + correo + "&mac=" + getMacAddr();

        System.out.println(url);
        //jrq= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);


        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // Toast.makeText(getContext(),"No se ha encontrado el usuario" + cajaUser.getText().toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "No se ha encontrado el usuario " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getContext(), "Se ha encontrado el usuario" + cajaUser.getText().toString(), Toast.LENGTH_SHORT).show();
        System.out.println("paso");
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("dato");
            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println("lenght: " + jsonArray.length());
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //nick = jsonObject.getString("nick");
                us.setNick(user);
                us.setPassword(pass);
                us.setMarca(jsonObject.getString("marca"));
                us.setModelo(jsonObject.getString("modelo"));
                marca = jsonObject.getString("marca");
                modelo = jsonObject.getString("modelo");
                user = jsonObject.getString("nick");
                pass = jsonObject.getString("pass");
                nombre = jsonObject.getString("nombre");
                us.setMarca(marca);
                us.setModelo(modelo);
                agregaDatosACajas(user, pass, marca, modelo);
                // marca = jsonObject.getString("marca");
                //modelo = jsonObject.getString("modelo");
                System.out.println("marca en volley " + marca);
                System.out.println("modelo en volley " + modelo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    public void setCorreo(String c) {
        correo = c;
    }
}