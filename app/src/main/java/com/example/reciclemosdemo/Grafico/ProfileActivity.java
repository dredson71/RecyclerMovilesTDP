package com.example.reciclemosdemo.Grafico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Inicio.BolsaActivity;
import com.example.reciclemosdemo.Inicio.LectorActivity;
import com.example.reciclemosdemo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private Dialog myDialog;
    private ActionBarDrawerToggle mToggle;
    private  TextView txtNombre,txtApellido,txtCorreo,txtPassword,txtDni;
    private TextView txtDepartamento,txtDistrito,txtCondominio,txtDireccion,txtFechaNacimiento,txtSexo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myDialog = new Dialog(ProfileActivity.this);
        txtNombre = findViewById(R.id.txtInputNombre);
        txtApellido = findViewById(R.id.txtInputApellidos);
        txtCorreo = findViewById(R.id.txtInputCorreo);
        txtPassword = findViewById(R.id.txtInputPassword);
        txtDepartamento = findViewById(R.id.txtInputDepartamento);
        txtDistrito = findViewById(R.id.txtInputDistrito);
        txtCondominio = findViewById(R.id.txtInputCondominio);
        txtDireccion = findViewById(R.id.txtInputDireccion);
        txtFechaNacimiento = findViewById(R.id.txtInputFechaNacimiento);
        txtSexo = findViewById(R.id.txtInputSexo);
        txtDni = findViewById(R.id.txtInputDNI);
        myDialog.setContentView(R.layout.editable_profile);
        TextView txtValor = myDialog.findViewById(R.id.txtValor);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper helper = new dbHelper(this,"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor fila2 = db.rawQuery("select codigo, nombre, apellido ,email, condominio , direccion  , email , fecha_Nacimiento , sexo ,distrito_name ,condominio_direccion ,departamento_name ,dni from Usuario", null);

        fila2.moveToFirst();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.txtUser);
        navUsername.setText(fila2.getString(1));
        txtNombre.setText(fila2.getString(1));
        txtApellido.setText(fila2.getString(2));
        txtCorreo.setText(fila2.getString(3));
        txtDepartamento.setText(fila2.getString(11));
        txtDistrito.setText(fila2.getString(9));
        txtCondominio.setText(fila2.getString(4));
        txtDireccion.setText( fila2.getString(10) + " N° "+fila2.getString(5));
        txtDni.setText(fila2.getString(12));
        String[] parts = fila2.getString(7).split("-");
        String year = parts[0];
        String month = parts[1];
        String dateAux = parts[2];
        String[] partsDate = dateAux.split("T");
        String date = partsDate[0];
        txtFechaNacimiento.setText(date+"/"+month+"/"+year);
        txtSexo.setText(fila2.getString(8));
        txtDni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtValor.setText("DNI");
                myDialog.show();
            }
        });





        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.txtMiPerfil:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    }

