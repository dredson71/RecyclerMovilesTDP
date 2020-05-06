package com.example.reciclemosdemo.Inicio;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reciclemosdemo.R;

public class CardsFragment extends Fragment {

    CardView cvPeru, cvDistrito, cvResiduos, cvNoReciclables, cvProyecto, cvRecicladores;

    public CardsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_cards, container, false);

        cvPeru = v.findViewById(R.id.cvPeru);
        cvDistrito = v.findViewById(R.id.cvDistrito);
        cvResiduos = v.findViewById(R.id.cvResiduos);
        cvNoReciclables = v.findViewById(R.id.cvNoReciclables);
        cvProyecto = v.findViewById(R.id.cvProyecto);
        cvRecicladores = v.findViewById(R.id.cvRecicladores);

        cvPeru.setOnClickListener(onClickListener);
        cvDistrito.setOnClickListener(onClickListener);
        cvResiduos.setOnClickListener(onClickListener);
        cvNoReciclables.setOnClickListener(onClickListener);
        cvProyecto.setOnClickListener(onClickListener);
        cvRecicladores.setOnClickListener(onClickListener);

        return v;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cvPeru:
                    ((MeInformoActivity) getActivity()).cambiarFragment(2);
                    break;
                case R.id.cvDistrito:
                    ((MeInformoActivity) getActivity()).cambiarFragment(3);
                    break;
                case R.id.cvResiduos:
                    ((MeInformoActivity) getActivity()).cambiarFragment(4);
                    break;
                case R.id.cvNoReciclables:
                    ((MeInformoActivity) getActivity()).cambiarFragment(5);
                    break;
                case R.id.cvProyecto:
                    ((MeInformoActivity) getActivity()).cambiarFragment(6);
                    break;
                case R.id.cvRecicladores:
                    ((MeInformoActivity) getActivity()).cambiarFragment(7);
                    break;
            }
        }
    };
}
