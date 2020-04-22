package com.example.reciclemosdemo.Inicio;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.aware.DiscoverySession;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reciclemosdemo.Adicionales.AnyOrientationCaptureActivity;
import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.Entities.Producto;
import com.example.reciclemosdemo.Entities.Usuario;
import com.example.reciclemosdemo.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProductoFragment extends Fragment {

    Button btnEscanear, btnAgregar, btnAlerta;
    TextView txtTipo, txtNombre, txtContenido, txtPeso, txtPuntos;
    ImageView imgProducto;
    NumberPicker numberPicker;
    private Retrofit retrofit;
    private double numpeso;
    private int bolsa, producto;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    int nuevo = -1;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case 1:
                    progressDialog.cancel();
                    txtTipo.setText("Tipo");
                    txtNombre.setText("Nombre Producto");
                    txtPeso.setText("Peso");
                    txtContenido.setText("Contenido");
                    numberPicker.setMinValue(0);
                    numberPicker.setMaxValue(0);
                    txtPuntos.setText("Puntos");
                    Toast.makeText(getActivity(), "Producto agregado a la bolsa activa", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    progressDialog.cancel();
                    Toast.makeText(getActivity().getApplicationContext(), "Escanee un producto", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_producto, container, false);

        txtTipo = v.findViewById(R.id.txtTipo);
        txtNombre = v.findViewById(R.id.txtNombre);
        txtContenido = v.findViewById(R.id.txtContenido);
        txtPeso = v.findViewById(R.id.txtPeso);
        txtPuntos = v.findViewById(R.id.txtTotalPuntos);
        btnEscanear = v.findViewById(R.id.btnEscanear);
        btnAgregar = v.findViewById(R.id.btnAgregar);
        numberPicker = v.findViewById(R.id.numberPicker);
        imgProducto = v.findViewById(R.id.imageView2);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://recyclerapiresttdp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        numberPicker.setOnValueChangedListener(onValueChangeListener);

        btnEscanear.setOnClickListener(mOnClickListener);
        btnAgregar.setOnClickListener(mOnClickListener);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Añadiendo Producto...");

        GetBolsa();
        return v;
    }

    private void mostrarDialogo(){
        new AlertDialog.Builder(getActivity())
                .setTitle("ALERTA")
                .setMessage("El producto que acabas de escanear ya se encuentra en tu bolsa, si deseas agregarlo selecciona la cantidad que deseas adicionar.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }


    private NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if(txtPeso.getText().toString().compareTo("Peso") != 0) {
                txtPuntos.setText((numberPicker.getValue() * 10) + " pts.");
                if((numpeso * numberPicker.getValue()) < 500.0)
                    txtPeso.setText((numpeso * numberPicker.getValue()) + " gr");
                else{
                    txtPeso.setText(((numpeso * numberPicker.getValue())/1000.0) + " kg");
                }
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
            if (result.getContents() != null) {
                GetProducto(result.getContents());
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Error al escanear", Toast.LENGTH_LONG).show();
            }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnEscanear:
                    IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(ProductoFragment.this);
                    integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
                    integrator.setOrientationLocked(false);
                    integrator.initiateScan();
                    break;
                case R.id.btnAgregar:
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    new Buscar(numberPicker.getValue()).execute();
                    break;
            }
        }
    };

    private void AddProBolsa(int numpicker) throws IOException {
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor fila2 = db.rawQuery("select codigo from Usuario", null);

        double dnumpicker = numpicker;

        fila2.moveToFirst();
        Probolsa pro = new Probolsa();
        Bolsa b = new Bolsa();
        Usuario u = new Usuario();
        Producto p = new Producto();
        b.setCodigo(bolsa);
        u.setCodigo(fila2.getInt(0));
        p.setCodigo(producto);
        pro.setBolsa(b);
        pro.setPeso(numpeso * dnumpicker);
        pro.setProducto(p);
        pro.setPuntuacion((numpicker * 10));
        pro.setCantidad(dnumpicker);

        if(nuevo == -1){
            JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
            Call<Probolsa> call=jsonPlaceHolderApi.addProBolsa(pro);
            Response<Probolsa> response = call.execute();
            Log.e("TAG","onResponse:" + response.toString());

            Call<List<Probolsa>> call2 = jsonPlaceHolderApi.getProbolsaByUsuario("/probolsa/usuario/" + fila2.getInt(0));
            Response<List<Probolsa>> response2 = call2.execute();
            List<Probolsa> aux = response2.body();
            Log.e("TAG","onResponse:" + response2.toString());

            db.execSQL("insert into Probolsa(bolsa, cantidad, codigo, peso, producto, puntuacion) values ("+ bolsa +", " + numpicker + ", "
                    + aux.get(aux.size() - 1).getCodigo() + ", " + (numpeso * numpicker) + ", " + producto + ", " + (numpicker * 10) + ")");
        }else{
            JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
            Call<Probolsa> call=jsonPlaceHolderApi.putCantidadProbolsa("/probolsa/" + nuevo + "/" + numpicker);
            Response<Probolsa> response = call.execute();
            Log.e("TAG","onResponse:" + response.toString());

            db.execSQL("update Probolsa set cantidad = cantidad +" + numpicker + " where codigo = " + nuevo);
            db.execSQL("update Probolsa set puntuacion = puntuacion +" + (numpicker * 10) + " where codigo = " + nuevo);
        }

    }

    public void GetProducto(String barCode) {
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select categoria, contenido, nombre, peso, tipo_contenido, codigo,urlimagen from Producto where barcode = '"+ barCode+ "'", null);

        if(f.moveToFirst()) {
            Cursor f2 = db.rawQuery("select codigo from Probolsa where bolsa = "+ bolsa + " and producto = " + f.getInt(5), null);

            if(f2.moveToFirst()){
                nuevo = f2.getInt(0);
                mostrarDialogo();
            } else {
                nuevo = -1;
            }

            Cursor fila = db.rawQuery("select nombre from Categoria where codigo = " + f.getInt(0), null);

            fila.moveToFirst();

            producto = f.getInt(5);
            txtTipo.setText(fila.getString(0));
            txtNombre.setText(f.getString(2));
            txtPeso.setText(f.getDouble(3) + " gr");
            numpeso = f.getDouble(3);
            txtContenido.setText(f.getDouble(1) + " " + f.getString(4));
            Picasso.get()
                    .load(f.getString(6))
                    .error(R.drawable.login)
                    .into(imgProducto);
            numberPicker.setMaxValue(50);
            numberPicker.setMinValue(1);
            numberPicker.setValue(1);
            txtPuntos.setText((numberPicker.getValue() * 10) + " pts.");
            if ((numpeso * numberPicker.getValue()) < 500.0)
                txtPeso.setText((numpeso * numberPicker.getValue()) + " gr");
            else {
                txtPeso.setText(((numpeso * numberPicker.getValue()) / 1000.0) + " kg");
            }
        } else {
            txtTipo.setText("Tipo");
            txtNombre.setText("Nombre Producto");
            txtPeso.setText("Peso");
            txtContenido.setText("Contenido");
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(0);
            txtPuntos.setText("Puntos");
            Toast.makeText(getActivity(), "No existe producto", Toast.LENGTH_LONG).show();
        }
    }

    public void GetBolsa()  {
        dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor f = db.rawQuery("select codigo from Bolsa where activa = 'true'", null);

        f.moveToFirst();

        bolsa = f.getInt(0);
    }

    public class Buscar extends AsyncTask<Void,Void,Void> {

        int numpicker;

        public Buscar(int numpicker){
            this.numpicker = numpicker;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(txtNombre.getText().toString().compareTo("Nombre Producto") != 0){
                try {
                    AddProBolsa(numpicker);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                Message msg = new Message();
                msg.what = 2;
                mHandler.sendMessage(msg);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid){
        }
    }
}


