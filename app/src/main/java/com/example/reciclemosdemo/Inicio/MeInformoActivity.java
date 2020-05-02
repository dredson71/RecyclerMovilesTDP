package com.example.reciclemosdemo.Inicio;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Grafico.ProfileActivity;
import com.example.reciclemosdemo.R;
import com.google.android.material.navigation.NavigationView;

public class MeInformoActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_informo);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();


        dbHelper helper = new dbHelper(this, "Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor filaUsuario = db.rawQuery("select codigo, nombre from Usuario", null);

        filaUsuario.moveToFirst();
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.txtUser);
        navUsername.setText(filaUsuario.getString(1));
        TextView navViewProfile = headerView.findViewById(R.id.txtMiPerfil);
        navViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_info:
                startActivity(new Intent(getApplicationContext(), MeInformoActivity.class));
                break;
            case R.id.nav_message:
                Toast.makeText(this, "Proximamente 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}