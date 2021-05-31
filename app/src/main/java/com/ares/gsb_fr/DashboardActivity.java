package com.ares.gsb_fr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    //on declare les parametres des choses à récuperer dans le xml
    private TextView openTextDsh;
    private Button goFicheFraisDsh;
    private Button goUtilisateurDsh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //on recupere les elements par leur id, a inserer dans le xml
        this.openTextDsh = (TextView) findViewById(R.id.titleDashboard);
        this.goFicheFraisDsh = (Button) findViewById(R.id.DshFicheFraisListButton);
        this.goUtilisateurDsh = (Button) findViewById(R.id.DshUtilisateurListButton);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("connected_user", MODE_PRIVATE);
        String userName = prefs.getString("nom_user","toto");
        String userSurname = prefs.getString("prenom_user","tutu");

        //On affiche une phrase de bienvenue en récupérant le nom du user et prenom du user
        openTextDsh.setText("Bonjour " + userName + " " + userSurname );

        goUtilisateurDsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUtilisateur();
            }
        });

        goFicheFraisDsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFicheFrais();
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
/////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
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
    /////////////////////////////////////////////////////
}