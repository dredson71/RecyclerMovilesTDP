package com.example.reciclemosdemo.Grafico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.reciclemosdemo.Inicio.BolsaActivity;
import com.example.reciclemosdemo.Inicio.LectorActivity;
import com.example.reciclemosdemo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;


public class ListBolsas extends AppCompatActivity  {
    private ViewPager viewPager;
    private com.example.reciclemosdemo.Grafico.ViewTrendAdapter adapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.reciclemosdemo.Grafico.RetrofitMain retrofitMain;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bolsas);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutTendencia);
        viewPager = findViewById(R.id.viewPagerTendencia);
        adapter = new com.example.reciclemosdemo.Grafico.ViewTrendAdapter(getSupportFragmentManager());

        adapter.AddFragment(new com.example.reciclemosdemo.Grafico.BolsasUserFragment(),"Registro");
        adapter.AddFragment(new com.example.reciclemosdemo.Grafico.TendenciaActivity(),"Tendencia");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //INICIALIZA APP BAR
        BottomNavigationView bottomNavigationView = findViewById(R.id.botton_navigation);

        //SELECCIÓN
        bottomNavigationView.setSelectedItemId(R.id.escaner);

        //CAMBIO DE SELECCIÓN
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.miaporte:
                        startActivity(new Intent(getApplicationContext(), ListBolsas.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.escaner:
                        startActivity(new Intent(getApplicationContext(), LectorActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.mibolsa:
                        startActivity(new Intent(getApplicationContext(), BolsaActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.catalogo:
                        return true;
                }
                return false;
            }
        });




    }

}
