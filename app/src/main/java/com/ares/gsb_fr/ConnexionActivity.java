
package com.ares.gsb_fr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnexionActivity extends AppCompatActivity {

    //on declare les parametres des choses à récuperer dans le xml
    private Button dashboardButton;
    private EditText login;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        //on récupère par leur id les élements dans le xml
        this.dashboardButton = (Button) findViewById(R.id.dashboardButton);
        this.login = (EditText) findViewById(R.id.login);
        this.password = (EditText) findViewById(R.id.password);

        //on declare une action sur le bouton de connexion
        this.dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            //on récupère ce qui a été saisi
                            String userLogin = login.getText().toString();
                            String userPassword = password.getText().toString();

                            //on enregistre ces éléments pour les envoyer en post dans l'api
                            Map<String, Object> mapJava = new HashMap<String, Object>();
                            mapJava.put("email", userLogin);
                            mapJava.put("password", userPassword);

                            //on déclare l'api service pour consommer l'api du côté symfony
                            APIService http = new APIService();


                            //on recupere la requete http
                            String urlTest = http.urlApi+"connexion";
                            String retourJson = http.sendRequest(urlTest, "POST", mapJava);
                            //System.out.println(retourJson);

                            setSession(retourJson);
                            goDashboard();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("api connexion", "Erreur d'identification");
                        }
                    }
                });
                thread.start();
            }
        });
    }

    //pour me ramener sur la page Dashboard
    private void goDashboard() {
        Intent goDashboard = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(goDashboard);
        finish();
    }

    //on stock les données
    public void setSession(String JsonResponse) throws JSONException {
        JSONObject jsonUser = new JSONObject(JsonResponse);

    //on recupere les champs de la BDD
        String name = (String)jsonUser.get("nom");
        String surname = (String)jsonUser.get("prenom");
        Integer iduser = (Integer) jsonUser.get("id");

        //on crée un fichier de savegarde
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("user_connected", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit ();

        //on integre ce qu'on récupère à ce fichier
        editor.putInt("user_id",iduser );
        editor.putString("user_prenom", surname);
        editor.putString("user_nom", name);
        editor.commit ();
    }
}