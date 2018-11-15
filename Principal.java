package uno.prueba.sanchez.augusto.login;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Response.ErrorListener, Response.Listener<JSONObject> {
    private String nick, pass, correo, mac,c,m;
    TextView txt;
    RequestQueue rqw;
    JsonObjectRequest jsonOR;

    Boolean band;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Intent intent = getIntent();
        correo = intent.getAction();
        nick = correo;
        band = false;
        rqw = Volley.newRequestQueue(this);
        txt = (TextView) findViewById(R.id.textprueba);
        //txt.setText(nick);
        //us.setNick(txt.getText().toString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //consultar();
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        if (id == R.id.perfil) {
            Perfil p = new Perfil();
            p.setCorreo(correo);
            System.out.println(correo);
            fragmentManager.beginTransaction().replace(R.id.contenedor,p).commit();
            // Handle the camera action
        } else if (id == R.id.confi) {
            Configuracion conf = new Configuracion();
            conf.setCorreo(correo);
            fragmentManager.beginTransaction().replace(R.id.contenedor,conf).commit();
        } else if (id == R.id.ayuda) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new Ayuda()).commit();
        } else if(id == R.id.camara){
            //camera cam=new camera(getApplicationContext(),camera);
            intent = new Intent(this,Ubicacion.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setNick(String n){
        nick = n;
    }
    public void setPass(String n){
        pass = n;
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

    private void consultar(){
        System.out.println("paso");
        mac=getMac();
        System.out.println(mac);
        //String url="http://192.168.1.64:8080/App/busquedaMac.php?correo="+correo+"&mac="+mac;
        String url="https://ingrid06.000webhostapp.com/busquedaMac.php?correo="+correo+"&mac="+mac;
        System.out.println(url);
        url = url.replaceAll(" ", "%20");
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
                m = jsonObject.getString("mac");
                c=jsonObject.getString("correo");
                if ((correo.equals(c)) &&(m.equals(mac))) {
                    Toast.makeText(getApplicationContext(), "Su telefono ya está registrado" , Toast.LENGTH_LONG).show();
                    band = true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "Su telefono aun no está registrado", Toast.LENGTH_LONG).show();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new AddDispositivo()).commit();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
