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

public class UtilisateurActivity extends AppCompatActivity {
    private TextView openTextBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilisateur);

        this.openTextBill = (TextView) findViewById(R.id.openTextBill);

        final String[] retourJson = new String[1];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("connected_user", MODE_PRIVATE);

                    Integer userid = prefs.getInt("id_user", 0);
                    String userName = prefs.getString("nom_user","toto");
                    String userSurname = prefs.getString("prenom_user","tutu");
                    String userPhone = prefs.getString("telephone_user","tata");

                    TextView textOpen = new TextView(UtilisateurActivity.this,null);
                    textOpen.findViewById(R.id.openTextBill);
                    openTextBill.setText("de " + userName + " " + userSurname);

                    Map<String, Object> mapJava = new HashMap<String, Object>();
                    APIService http = new APIService();
                    //mettre d'url de la machine sur la VM
                    String urlTest = http.urlApi+"user/list/"+userid.toString();
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

        TableLayout containerTable = (TableLayout) findViewById(R.id.Utilisateur);
        //entete du tableau des factures
        List<String> colonnes = new ArrayList<String>();
        colonnes.add("Nom");
        colonnes.add("Prénom");
        colonnes.add("");

        JSONArray arrayJSON = new JSONArray();

        try {
            arrayJSON = new JSONArray(retourJson);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("api", "retourne rien");
        }

        TableRow tableRow = new TableRow(UtilisateurActivity.this);
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
                JSONObject jsonUser = arrayJSON.getJSONObject(d);

                tableRow = new TableRow(UtilisateurActivity.this);
                containerTable.addView(tableRow,
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                i = 0;
                String idUser = jsonUser.get("id").toString();
                String nameUser = jsonUser.get("nom").toString();
                String surnameUser = jsonUser.get("prenom").toString();

                //je récuperer le user des factures
                //JSONObject user = jsonUser.getJSONObject("user");
                //String nameUser = user.get("nom").toString();
                //String surnameUser = user.get("prenom").toString();






                //BUTTON pour voir les lignes
                Button button = new Button(UtilisateurActivity.this);
                button.setText("voir");

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
                        goUserSelected(idUser);
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


    private void goFicheFrais() {
        Intent goFicheFrais = new Intent(getApplicationContext(),FicheFraisActivity.class);
        startActivity(goFicheFrais);
        finish();
    }

    private void goDashboard() {
        Intent goDashboard = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(goDashboard);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bill, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuBillGoMessage:
                goFicheFrais();
                return true;
            case R.id.menuBillGoDsh:
                goDashboard();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void goUserSelected(String idUser) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("id_bill_to_display", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit ();
        editor.putString("id_user", idUser);
        editor.commit ();

        Intent goUserDetail = new Intent(getApplicationContext(),UtilisateurDetailActivity.class);
        startActivity(goUserDetail);
        finish();
    }
}