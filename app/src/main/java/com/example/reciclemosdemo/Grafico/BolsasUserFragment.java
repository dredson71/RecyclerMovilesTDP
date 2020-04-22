package com.example.reciclemosdemo.Grafico;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.Entities.JsonPlaceHolderApi;
import com.example.reciclemosdemo.Entities.Probolsa;
import com.example.reciclemosdemo.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BolsasUserFragment extends Fragment  implements ListaBolsaAdapter.OnNoteListener{
   View view;
    private static final String TAG ="BOLSAS";
    private ArrayList<Bolsa> dataset = new ArrayList<>();
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ListaBolsaAdapter listBolsasAdapter;
    private ArrayList<TextView> textList = new ArrayList<>();
    private int bolsasCount,puntosCount,pesoCount;
    private TextView txtBolsasCount,txtPesoCount,txtPuntuacionCount;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bolsas_fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        listBolsasAdapter = new ListaBolsaAdapter(getContext());
        txtBolsasCount =view.findViewById(R.id.txtCantBolsasHoy) ;
        txtPesoCount =view.findViewById(R.id.txtPesoBolsasHoy) ;
        txtPuntuacionCount = view.findViewById(R.id.txtPtosBolsasHoy) ;
        textList.add(txtBolsasCount);
        textList.add(txtPesoCount);
        textList.add(txtPuntuacionCount);
        recyclerView.setAdapter(listBolsasAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://recyclerapiresttdp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        String usuarioID = ((InitialValues)this.getActivity().getApplication()).getIdUsuario();

        obtenerDatos(usuarioID);
        obtenerBolsasByDay("bolsasDay/",usuarioID,textList);
        return view;

    }

    private void obtenerDatos(String usuarioID){
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Bolsa>> call=jsonPlaceHolderApi.getBolsasByUsuario("bolsa/last/"+usuarioID);
        call.enqueue(new Callback<List<Bolsa>>() {
            @Override
            public void onResponse(Call<List<Bolsa>> call, Response<List<Bolsa>> response) {
                if(response.isSuccessful()) {

                    List<Bolsa> probolsas = response.body();
                    ArrayList<Bolsa> listaprodbolsas = (ArrayList) probolsas;
                    dataset=(ArrayList) probolsas;
                    listBolsasAdapter.adicionarListaCancion(listaprodbolsas);
                }else{
                    Log.e(TAG,"onResponse:" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Bolsa>> call, Throwable t) {
                Log.e(TAG,"onFailure:" + t.getMessage());
            }
        });
    }

    public void obtenerBolsasByDay(String urlDate,String usuarioID,ArrayList<TextView> textViewsList){
        pesoCount=0;
        bolsasCount=0;
        puntosCount=0;
        Set<Integer> bolsas = new HashSet<>();
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Probolsa>> call=jsonPlaceHolderApi.getBolsasByDate("probolsa/"+urlDate+usuarioID);
        call.enqueue(new Callback<List<Probolsa>>() {
            @Override
            public void onResponse(Call<List<Probolsa>> call, Response<List<Probolsa>> response) {
                if(response.isSuccessful()) {

                    List<Probolsa> probolsas = response.body();
                    for(int i=0;i<probolsas.size();i++)
                    {
                        bolsas.add(probolsas.get(i).getBolsa().getCodigo());
                        pesoCount+=probolsas.get(i).getPeso();
                        puntosCount+=probolsas.get(i).getPuntuacion();
                    }
                    textViewsList.get(0).setText(Integer.toString(bolsas.size()));
                    textViewsList.get(2).setText(Integer.toString(puntosCount));
                    textViewsList.get(1).setText(Integer.toString(pesoCount/1000));

                }else{
                    Log.e(TAG,"onResponse:" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Probolsa>> call, Throwable t) {
                Log.e(TAG,"onFailure:" + t.getMessage());
            }
        });
    }

    @Override
    public void oneNoteClick(int position) {

    }
}
