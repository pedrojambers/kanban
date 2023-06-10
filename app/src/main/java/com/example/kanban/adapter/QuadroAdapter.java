package com.example.kanban.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanban.MainActivity;
import com.example.kanban.R;
import com.example.kanban.model.Quadro;

import java.util.List;

public class QuadroAdapter extends RecyclerView.Adapter<QuadroAdapter.ExibeItemLista> {

    private MainActivity mMainActivity;
    private List<Quadro> mListaQuadros;

    public QuadroAdapter(MainActivity mainActivity, List<Quadro> listaNotas){
        mMainActivity = mainActivity;
        mListaQuadros = listaNotas;
    }

    @NonNull
    @Override
    public QuadroAdapter.ExibeItemLista onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quadro, parent, false);
        return new ExibeItemLista(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuadroAdapter.ExibeItemLista holder, int position) {
        Quadro quadro = mListaQuadros.get(position);
        holder.mTitulo.setText(quadro.getmTituloQuadro());
    }

    @Override
    public int getItemCount() {
        return mListaQuadros.size();
    }

    public class ExibeItemLista extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitulo;

        public ExibeItemLista(@NonNull View itemView) {
            super(itemView);
            mTitulo = itemView.findViewById(R.id.btnTarefa);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            mMainActivity.abreQuadro(getAdapterPosition());
        }
    }
}
