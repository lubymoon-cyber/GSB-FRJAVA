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

public class UtilisateurDetailActivity extends AppCompatActivity {
    private Button addLign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilisateur_detail);

        this.addLign = (Button) findViewById(R.id.addLign);

        SharedPreferences id = getApplicationContext().getSharedPreferences("id_bill_to_display", MODE_PRIVATE);
        String idBill = id.getString("id_bill","0");

        final String[] retourJson = new String[1];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {

                    Map<String, Object> mapJava = new HashMap<String, Object>();
                    APIService http = new APIService();
                    //mettre d'url de la machine sur la VM
                    String urlTest = http.urlApi+"billlign/list/"+idBill.toString();
                    retourJson[0] = http.sendRequest(urlTest, "GET", mapJava);
                    //System.out.println(retourJson[0]);

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

        addLign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUtilisateur();
            }
        });
    }

    private void createTableLayout(String retourJson) {

        TableLayout containerTable = (TableLayout) findViewById(R.id.tableUtilisateurDetail);
        //entete du tableau des utilisateurs
        List<String> colonnes = new ArrayList<String>();
        colonnes.add("Nom");
        colonnes.add("Prénom");
        colonnes.add("Téléphone");
        colonnes.add("Email");


        JSONArray arrayJSON = new JSONArray();

        try {
            arrayJSON = new JSONArray(retourJson);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("api", "retourne rien");
        }

        TableRow tableRow = new TableRow(UtilisateurDetailActivity.this);
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
            //ON RECUPERE L'OBJET JSON DE CHAQUE LIGNE
            try {
                JSONObject jsonUserLigns = arrayJSON.getJSONObject(d);

                tableRow = new TableRow(UtilisateurDetailActivity.this);
                containerTable.addView(tableRow,
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                i = 0;
                String quantityLign = "";
                String namePackage = "";
                String valuePackage = "";
                String nameOutPackage = "";
                String valueOutPackage = "";

                //je récuperer les données de la lignes
                if(jsonUserLigns.has("package")) {
                    quantityLign = jsonUserLigns.get("quantity").toString();
                }
                String dateLign = jsonUserLigns.get("created_at").toString();
                String globalValueLign = jsonUserLigns.get("global_lign_value").toString();
                dateLign = dateLign.substring(0,10);

                //je récupère les données des packages
                if(jsonUserLigns.has("package")) {
                    JSONObject packageLign = jsonUserLigns.getJSONObject("package");
                    namePackage = packageLign.get("package_name").toString();
                    valuePackage = packageLign.get("value").toString();
                }
                //je récupère les données des out_packages
                if(jsonUserLigns.has("out_package")) {
                    JSONObject outPackageLign = jsonUserLigns.getJSONObject("out_package");
                    nameOutPackage = outPackageLign.get("out_package_name").toString();
                    valueOutPackage = outPackageLign.get("value").toString();
                }

                //je recupere l'utilisateur
                JSONObject user = jsonUserLigns.getJSONObject("user");
                String idUser = user.get("id").toString();
                setSessionUserForEdit(idUser);

                //date des lignes
                TextView text = createTextView(d == 10, i == 2);
                text.setText(dateLign);
                tableRow.addView(text, i++);
                text.setGravity(Gravity.LEFT);
                text.setTextColor(Color.parseColor("#3446eb"));

                //quantité de la ligne
                text = createTextView(d == 10, i == 2);
                text.setText(quantityLign);
                tableRow.addView(text, i++);
                text.setGravity(Gravity.CENTER);

                //nom du forfait ou hors forfait
                text = createTextView(d == 10, i == 2);
                if(namePackage != "") {
                    text.setText(namePackage);
                    text.setTextColor(Color.parseColor("#00c20a"));
                } else {
                    text.setText(nameOutPackage);
                    text.setTextColor(Color.parseColor("#ff4d00"));
                }
                tableRow.addView(text, i++);
                text.setGravity(Gravity.CENTER);

                //montant unitaire
                text = createTextView(d == 10, i == 2);
                if(valuePackage != "") {
                    text.setText(valuePackage);
                    text.setTextColor(Color.parseColor("#00c20a"));
                } else {
                    text.setText(valueOutPackage);
                    text.setTextColor(Color.parseColor("#ff4d00"));
                }
                tableRow.addView(text, i++);
                text.setGravity(Gravity.CENTER);

                //montant total
                text = createTextView(d == 10, i == 2);
                text.setText(globalValueLign);
                tableRow.addView(text, i++);
                text.setGravity(Gravity.CENTER);


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
    private void goUtilisateur() {
        Intent goUtilisateur = new Intent(getApplicationContext(),UtilisateurActivity.class);
        startActivity(goUtilisateur);
        finish();
    }

    private void goBill() {
        Intent goBill = new Intent(getApplicationContext(),FicheFraisActivity.class);
        startActivity(goBill);
        finish();
    }

    private void goDashboard() {
        Intent goDashboard = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(goDashboard);
        finish();
    }

    public void goEditFicheFrais() {
        Intent goEditbill = new Intent(getApplicationContext(),FicheFraisDetailActivity.class);
        startActivity(goEditbill);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bill_lign, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuBillLignGoMessage:
                goEditFicheFrais();
                return true;
            case R.id.menuBillLignGoDashboard:
                goDashboard();
                return true;
            case R.id.menuBillLignGoBill:
                goBill();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setSessionUserForEdit(String idUser,String numBill) {

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("id_user_to_response", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit ();

        editor.putString("id_user", idUser);

        editor.commit ();
    }

}