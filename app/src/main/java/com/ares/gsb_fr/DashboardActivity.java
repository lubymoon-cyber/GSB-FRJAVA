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

public class Dashboard extends AppCompatActivity {
    private TextView openTextDsh;
    private Button goFicheFraisDsh;
    private Button goBillDsh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this.openTextDsh = (TextView) findViewById(R.id.titleDashboard);
        this.goFicheFraisDsh = (Button) findViewById(R.id.goFicheFraisDshListButton);
        this.goBillDsh = (Button) findViewById(R.id.DshBillListButton);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("connected_user", MODE_PRIVATE);
        String userName = prefs.getString("nom_user","toto");
        String userSurname = prefs.getString("prenom_user","tutu");

        openTextDsh.setText("Bonjour " + userName + " " + userSurname );

        goBillDsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBill();
            }
        });

        goMessgaeDsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMessage();
            }
        });

    }

    private void goMessage() {
        Intent goMessageList = new Intent(getApplicationContext(),MessageList.class);
        startActivity(goMessageList);
        finish();
    }

    private void goBill() {
        Intent goBill = new Intent(getApplicationContext(),bill.class);
        startActivity(goBill);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dash, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuDshGoMessage:
                goMessage();
                return true;
            case R.id.menuDshGoBill:
                goBill();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}