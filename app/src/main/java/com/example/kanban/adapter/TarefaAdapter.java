package com.example.kanban.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanban.R;
import com.example.kanban.model.Tarefa;

import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder> {

    private List<Tarefa> mListaTarefas;
    private Activity mActivity;

    public TarefaAdapter(List<Tarefa> listaTarefas, Activity activity) {
        mListaTarefas = listaTarefas;
        mActivity = activity;
    }

    public void atualizarTarefas(List<Tarefa> listaTarefas) {
        mListaTarefas.clear();
        mListaTarefas.addAll(listaTarefas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarefa, parent, false);
        return new TarefaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        Tarefa tarefa = mListaTarefas.get(position);
        holder.mTitulo.setText(tarefa.getmTituloTarefa());
    }

    public int getItemCount() {
        if (mListaTarefas == null) {
            return 0;
        }
        return mListaTarefas.size();
    }

    public class TarefaViewHolder extends RecyclerView.ViewHolder {

        TextView mTitulo;

        public TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitulo = itemView.findViewById(R.id.btnTarefa);
        }
    }
}