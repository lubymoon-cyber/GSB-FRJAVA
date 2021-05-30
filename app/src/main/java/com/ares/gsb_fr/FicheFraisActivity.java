package com.ares.gsb_fr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FicheFraisActivity extends AppCompatActivity {
    private TextView openDetailFicheFrais;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_frais);

        this.openDetailFicheFrais = (TextView) findViewById(R.id.openDetailFicheFrais);

        final String[] retourJson = new String[1];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("connected_user", MODE_PRIVATE);

                    Integer userid = prefs.getInt("id_user", 0);
                    String userName = prefs.getString("nom_user","toto");
                    String userSurname = prefs.getString("prenom_user","tutu");

                    TextView textOpen = new TextView(FicheFraisActivity.this,null);
                    textOpen.findViewById(R.id.openDetailFicheFrais);
                    openTextFicheFrais.setText("de " + userName + " " + userSurname);

                    Map<String, Object> mapJava = new HashMap<String, Object>();
                    APIService http = new APIService();
                    //mettre d'url de la machine sur la VM
                    String urlTest = http.urlApi+"fiche/frais/list/"+userid.toString();
                    retourJson[0] = http.sendRequest(urlTest, "GET", mapJava);
                    //System.out.println(retourJson);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Lancement du thread
        thread.start();
        try {
            thread.join();
            createTableLayout(retourJson[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void createTableLayout(String retourJson) {

        TableLayout containerTable = (TableLayout) findViewById(R.id.tableFicheFrais);
        //entete du tableau des factures
        List<String> colonnes = new ArrayList<String>();
        colonnes.add("Utilisateur");
        colonnes.add("Date");
        colonnes.add("");

        JSONArray arrayJSON = new JSONArray();

        try {
            arrayJSON = new JSONArray(retourJson);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("api", "retourne rien");
        }

        TableRow tableRow = new TableRow(FicheFraisActivity.this);
        containerTable.addView(tableRow,
                new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.setLayoutParams(new TableRow.LayoutParams(colonnes.size()));

        //DEFINITION DES COLONNES DU TABLEAU
        //ligne entete
        int i = 0;
        for (String texteColonne : colonnes) {
            TextView text = createTextView(false, i == colonnes.size() - 1);
            text.setText(texteColonne);
            text.setTextSize(20);
            text.setTextColor(Color.parseColor("#3446eb"));
            text.setTypeface(null, Typeface.BOLD);
            text.setGravity(Gravity.CENTER);
            tableRow.addView(text, i++);
        }

        //contenu
        for (int d = 0; d < arrayJSON.length(); d++) {
            //ON RECUPERE L'OBJET JSON DE CHAQUE LIGNE DE FICHE FRAIS
            try {
                JSONObject jsonFicheFrais = arrayJSON.getJSONObject(d);

                tableRow = new TableRow(FicheFraisActivity.this);
                containerTable.addView(tableRow,
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                i = 0;
                String FicheFraisId = jsonFicheFrais.get("id").toString();

                //je récuperer le user des fiche de frais
                JSONObject userFicheFrais = jsonFicheFrais.getJSONObject("user");
                String nameUserFicheFrais = userFicheFrais.get("nom").toString();
                String surnameUserFicheFrais = userFicheFrais.get("prenom").toString();
                Integer idUserFicheFrais = (Integer) userFicheFrais.get("id");


                String dateFicheFrais = jsonFicheFrais.get("date_fiche_frais").toString();
                dateFicheFrais = dateFicheFrais.substring(0,10);


                TextView text = createTextView(d == 10, i == 2);

                //Utilisateur de la fiche de frais
                text = createTextView(d == 10, i == 2);
                text.setText(nameUserFicheFrais);
                tableRow.addView(text, i++);
                text.setGravity(Gravity.CENTER);

                //Date de la fiche de frais
                text = createTextView(d == 10, i == 2);
                text.setText(dateFicheFrais);
                tableRow.addView(text, i++);
                text.setGravity(Gravity.CENTER);


                //Bouton pour voir les lignes de fiches de frais
                Button button = new Button(FicheFraisActivity.this);
                button.setText("Consulter");

                int bottom = d==10 ? 1 : 0;
                int right = i==2 ? 1 : 0;
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
                params.setMargins(1, 1, right, bottom);
                button.setLayoutParams(params);
                button.setPadding(4, 4, 10, 4);

                button.setTextColor(Color.parseColor("#3446eb"));
                button.setTypeface(null,Typeface.BOLD);
                tableRow.addView(button, i++);
                button.setGravity(Gravity.CENTER);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goFicheFraisSelected(FicheFraisId);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private TextView createTextView(boolean endline, boolean endcolumn) {
        TextView text = new TextView(this, null);
        int bottom = endline ? 1 : 0;
        int right = endcolumn ? 1 : 0;
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.1f);
        params.setMargins(1, 1, right, bottom);
        text.setLayoutParams(params);
        text.setPadding(4, 4, 10, 4);
        text.setBackgroundColor(this.getColor(R.color.white));
        return text;
    }


    private void goDashboard() {
        Intent goDashboard = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(goDashboard);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_FicheFrais, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuFicheFraisGoDsh:
                goDashboard();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void goFicheFraisSelected(String FicheFraisId) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("id_FicheFrais_to_display", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit ();
        editor.putString("id_FicheFrais", FicheFraisId);
        editor.commit ();

        Intent goFicheFraisLigns = new Intent(getApplicationContext(),FicheFraisDetailActivity.class);
        startActivity(goFicheFraisLigns);
        finish();
    }
}