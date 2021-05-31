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

public class FicheFraisDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_frais_detail);

        SharedPreferences id = getApplicationContext().getSharedPreferences("fiche_frais_selected", MODE_PRIVATE);
        String idFicheFrais = id.getString("id_fiche_frais","0");

        final String[] retourJson = new String[1];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {

                    Map<String, Object> mapJava = new HashMap<String, Object>();
                    APIService http = new APIService();
                    //mettre d'url de la machine sur la VM
                    String url = http.urlApi+"fiche/fais/detail/"+idFicheFrais.toString();
                    retourJson[0] = http.sendRequest(url, "GET", mapJava);
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

    }

    private void createTableLayout(String retourJson) {

        TableLayout containerTable = (TableLayout) findViewById(R.id.tableFicheFraisDetail);
        //entete du tableau des factures
        List<String> colonnes = new ArrayList<String>();
        colonnes.add("Date");
        colonnes.add("Quantité");
        colonnes.add("libelle");
        colonnes.add("Etat");
        colonnes.add("Montant unitaire");


        JSONArray arrayJSON = new JSONArray();


        try {
            arrayJSON = new JSONArray(retourJson);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("api", "retourne rien");
        }

        TableRow tableRow = new TableRow(FicheFraisDetailActivity.this);
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
                JSONObject jsonFicheFraisLign = arrayJSON.getJSONObject(d);

                tableRow = new TableRow(FicheFraisDetailActivity.this);
                containerTable.addView(tableRow,
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                i = 0;
                String quantite = "";
                String libelle = "";
                String date = "";
                String montant = "";

                //recupe du libelle d'un forfait
                if(jsonFicheFraisLign.has("ligne_frais_forfaits")) {
                    JSONArray arrayJSONForfait = new JSONArray(jsonFicheFraisLign.getJSONObject("ligne_frais_forfaits"));
                    for (int j = 0; j < arrayJSONForfait.length(); j++ ) {

                        JSONObject jsonForfaitLign = arrayJSON.getJSONObject(j);
                        String quantiteForfait = jsonForfaitLign.get("quantite").toString();

                        String dateForfait = jsonForfaitLign.get("date_ligne_frais_forfait").toString();
                        dateForfait = dateForfait.substring(0,10);

                        JSONObject etatLigne = jsonForfaitLign.getJSONObject("statut_ligne_frais_forfait");
                        String libelleEtat = etatLigne.get("libelle").toString();

                        JSONObject fraisForfait = jsonForfaitLign.getJSONObject("frais_forfait");
                        String libelleFraisForfait = fraisForfait.get("libelle").toString();
                        String montantFraisForfait = fraisForfait.get("montant").toString();

                        //date des ligness
                        TextView text = createTextView(d == 10, i == 2);
                        text.setText(dateForfait);
                        tableRow.addView(text, i++);
                        text.setGravity(Gravity.LEFT);
                        text.setTextColor(Color.parseColor("#3446eb"));

                        //quantité de la ligne
                        text = createTextView(d == 10, i == 2);
                        text.setText(quantiteForfait);
                        tableRow.addView(text, i++);
                        text.setGravity(Gravity.CENTER);

                        //libelleb de la ligne
                        text = createTextView(d == 10, i == 2);
                        text.setText(libelleFraisForfait);
                        tableRow.addView(text, i++);
                        text.setGravity(Gravity.CENTER);

                        //libelle de l'etat de la ligne
                        text = createTextView(d == 10, i == 2);
                        text.setText(libelleEtat);
                        tableRow.addView(text, i++);
                        text.setGravity(Gravity.CENTER);

                        //montant unitaire
                        text = createTextView(d == 10, i == 2);
                        text.setText(montantFraisForfait);
                        tableRow.addView(text, i++);
                        text.setGravity(Gravity.CENTER);

                    }
                }

                //recupe du libelle d'un hors forfait
                if(jsonFicheFraisLign.has("ligne_frais_hors_forfaits")) {
                    JSONArray arrayJSONHorsForfait = new JSONArray(jsonFicheFraisLign.getJSONObject("ligne_frais_hors_forfaits"));
                    for (int k = 0; k < arrayJSONHorsForfait.length(); k++ ) {

                        JSONObject jsonHorsForfaitLign = arrayJSON.getJSONObject(k);

                        String quantiteHorsForfait = "";

                        String dateHorsForfait = jsonHorsForfaitLign.get("date_ligne_frais_hors_forfait").toString();
                        dateHorsForfait = dateHorsForfait.substring(0,10);

                        String libelleHorsForfait = jsonHorsForfaitLign.get("libelle").toString();
                        String montantHorsForfait = jsonHorsForfaitLign.get("montant").toString();


                        JSONObject etatLigneHorsForfait = jsonHorsForfaitLign.getJSONObject("statut_ligne_frais_hors_forfait");
                        String libelleEtatHorsForfait = etatLigneHorsForfait.get("libelle").toString();


                        //date des ligness
                        TextView text = createTextView(d == 10, i == 2);
                        text.setText(dateHorsForfait);
                        tableRow.addView(text, i++);
                        text.setGravity(Gravity.LEFT);
                        text.setTextColor(Color.parseColor("#3446eb"));

                        //quantité de la ligne
                        text = createTextView(d == 10, i == 2);
                        text.setText(quantiteHorsForfait);
                        tableRow.addView(text, i++);
                        text.setGravity(Gravity.CENTER);

                        //libelleb de la ligne
                        text = createTextView(d == 10, i == 2);
                        text.setText(libelleHorsForfait);
                        tableRow.addView(text, i++);
                        text.setGravity(Gravity.CENTER);

                        //libelle de l'etat de la ligne
                        text = createTextView(d == 10, i == 2);
                        text.setText(libelleEtatHorsForfait);
                        tableRow.addView(text, i++);
                        text.setGravity(Gravity.CENTER);

                        //montant unitaire
                        text = createTextView(d == 10, i == 2);
                        text.setText(montantHorsForfait);
                        tableRow.addView(text, i++);
                        text.setGravity(Gravity.CENTER);

                    }
                }

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
        Intent goMessageList = new Intent(getApplicationContext(),UtilisateurActivity.class);
        startActivity(goMessageList);
        finish();
    }

    private void goFicheFrais() {
        Intent goBill = new Intent(getApplicationContext(),FicheFraisActivity.class);
        startActivity(goBill);
        finish();
    }

    private void goDashboard() {
        Intent goDashboard = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(goDashboard);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fichefrais_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuDshDashboard:
                goDashboard();
                return true;
            case R.id.menuDshFichesFrais:
                goFicheFrais();
                return true;
            case R.id.menuDshUtilisateur:
                goUtilisateur();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void setSessionBillForEdit(String idBill,String numBill) {

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("id_bill_to_response", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit ();

        editor.putString("num_bill", numBill);
        editor.putString("id_bill", idBill);

        editor.commit ();
    }

}