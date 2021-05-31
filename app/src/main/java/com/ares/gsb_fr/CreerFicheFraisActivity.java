package com.ares.gsb_fr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class CreerFicheFraisActivity extends AppCompatActivity {

    private Button sendFicheFraisButton;
    private EditText libelleToSend;
    private EditText montantToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_fiche_frais);

        this.libelleToSend = (EditText) findViewById(R.id.libelleToSend);
        this.montantToSend = (EditText) findViewById(R.id.montantToSend);
        this.sendFicheFraisButton = (Button) findViewById(R.id.sendFicheFrais);

        sendFicheFraisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String libelleHorsForfait = libelleToSend.getText().toString();
                    String montantHorsForfait = montantToSend.getText().toString();

                    Map<String, Object> mapJava = new HashMap<String, Object>();
                    mapJava.put("libelle_hors_forfait", libelleHorsForfait);
                    mapJava.put("montant_hors_forfait", montantHorsForfait);

                    APIService http = new APIService();
                    String urlTest = http.urlApi+"fiche/frais/new";

                    String retourJson = http.sendRequest(urlTest, "PUT", mapJava);

                    goFicheFrais();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //System.out.println(retourJson);



            }
        });


    }

    private void goFicheFrais() {
        Intent goFicheFraisList = new Intent(getApplicationContext(), FicheFraisActivity.class);
        startActivity(goFicheFraisList);
        finish();
    }

    private void goUtilisateur() {
        Intent goUtilisateur = new Intent(getApplicationContext(), UtilisateurActivity.class);
        startActivity(goUtilisateur);
        finish();
    }

    private void goDashboard() {
        Intent goDashboard = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(goDashboard);
        finish();
    }



    //////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cree_fiche_frais, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuDshDashboard:
                goDashboard();
                return true;
            case R.id.menuDshUtilisateur:
                goUtilisateur();
                return true;
            case R.id.menuDshFichesFrais:
                goFicheFrais();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}