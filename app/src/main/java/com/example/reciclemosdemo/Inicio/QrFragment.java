package com.example.reciclemosdemo.Inicio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

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
import android.widget.Toast;

import com.example.reciclemosdemo.Adicionales.AnyOrientationCaptureActivity;
import com.example.reciclemosdemo.Adicionales.dbHelper;
import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.Entities.QrCode;
import com.example.reciclemosdemo.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QrFragment extends Fragment {

    private Button btnQr;
    private Retrofit retrofit;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case 1:
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    break;
                case 2:
                    progressDialog.cancel();
                    Toast.makeText(getActivity(), "No existe código QR", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    progressDialog.cancel();
                    ((RegistrarBolsaActivity) getActivity()).registroExitoso();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_qr, container, false);

        btnQr = v.findViewById(R.id.btnQr);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://recyclerapiresttdp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Enlazando QR a la bolsa...");

        btnQr.setOnClickListener(mOnClickListener);
        return v;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnQr:
                    IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(QrFragment.this);
                    integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
                    integrator.setOrientationLocked(false);
                    integrator.initiateScan();
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
            if (result.getContents() != null) {
                new Background(result.getContents()).execute();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Error al escanear", Toast.LENGTH_LONG).show();
            }
    }

    public void GetQr(String qr) throws IOException {
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<QrCode> call=jsonPlaceHolderApi.getQrCode("qrcode/qrCode/" + qr);
        Response<QrCode> response = call.execute();
        Log.e("TAG","onResponse:" + response.toString());

        try{
            if(response.body().getQrCode().compareTo(qr) == 0){

                dbHelper helper = new dbHelper(getActivity(),"Usuario.sqlite", null, 1);
                SQLiteDatabase db = helper.getReadableDatabase();

                Cursor fila = db.rawQuery("select codigo from Bolsa where activa = 'true'", null);

                fila.moveToFirst();

                db.execSQL("update Bolsa set activa = 'false' where codigo = " + fila.getInt(0));

                Call<Bolsa> call2=jsonPlaceHolderApi.putQrBolsa("bolsa/" + fila.getInt(0) + "/qrCode/" + qr);
                Response<Bolsa> response2 = call2.execute();
                Log.e("TAG","onResponse:" + response2.toString());

                Cursor fila2 = db.rawQuery("select codigo from Usuario", null);

                fila2.moveToFirst();

                Call<Bolsa> call3=jsonPlaceHolderApi.getBolsaActiva("bolsa/activa/" + fila2.getInt(0));
                Response<Bolsa> response3 = call3.execute();
                Log.e("TAG","onResponse:" + response3.toString());

                Bolsa nueva = response3.body();

                String query = "insert into Bolsa (codigo, activa, creadoFecha, puntuacion, qrcode, recojoFecha) " +
                        "values (" + nueva.getCodigo() + ", '" + nueva.getActiva() + "', " + null + ", " + nueva.getPuntuacion() + ", " + null
                        + ", " + null + ")";
                db.execSQL(query);
                System.out.println(query);

                Message msg = new Message();
                msg.what = 3;
                mHandler.sendMessage(msg);
            }
        }catch (Exception e){
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);
        }

    }

    public class Background extends AsyncTask<Void,Void,Void> {

        String qr;

        public Background(String qr){
            this.qr = qr;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                GetQr(qr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid){
        }
    }
}
