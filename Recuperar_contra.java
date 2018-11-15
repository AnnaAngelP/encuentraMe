package uno.prueba.sanchez.augusto.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Recuperar_contra extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    private String user;
    private String passwd, passTo;
    private String emailTo;
    private EditText correoTo;
    RequestQueue rqw;
    JsonObjectRequest jsonOR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contra);
        user="as7227050@gmail.com";
        passwd="(Prueba961007)";
        correoTo = (EditText) findViewById(R.id.Valcorreo);
        rqw = Volley.newRequestQueue(this);
    }
    public void enviar(View view){
        String c=correoTo.getText().toString();
        if(c.length() > 0){
            String url = "https://ingrid06.000webhostapp.com/ValidarCorreo.php?correo="+c;
            System.out.println(url);
            jsonOR = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rqw.add(jsonOR);
        }
        else {
            Toast.makeText(getApplicationContext(),
                    "Por favor ingrese un correo",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "No se ha encontrado el correo " + error.getMessage(), Toast.LENGTH_SHORT).show();
        if (error instanceof NetworkError) {
        } else if (error instanceof ServerError) {
            Toast.makeText(getApplicationContext(),
                    "Oops. Server error!",
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(getApplicationContext(),
                    "Oops. AuthFailureError!",
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(getApplicationContext(),
                    "Oops. ParseError error!   " + error.getMessage(),
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(getApplicationContext(),
                    "Oops. NoConnectionError error!",
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(getApplicationContext(),
                    "Oops. Timeout error!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        System.out.println("entro");
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("dato");
            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println("lenght: " + jsonArray.length());
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //nick = jsonObject.getString("nick");
                emailTo = jsonObject.getString("correo");
                passTo = jsonObject.getString("password");
                if(passTo.compareTo("0")==0){
                    //no existe el correo
                    Toast.makeText(getApplicationContext(), "El correo ingresado no esta REGISTRADO ", Toast.LENGTH_SHORT).show();
                }
                else{
                    new MailJob(user, passwd).execute(
                            new MailJob.Mail(user, emailTo, "Recuperación de contraseña:", passTo)
                    );

                    Toast.makeText(getApplicationContext(), "La contraseña se te ha enviado a tu correo ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Recuperar_contra.this,MainActivity.class);
                    //intent.setAction(ET_correo.getText().toString());
                    //intent.setAction()
                    startActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
