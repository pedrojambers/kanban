package com.example.kanban.fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanban.R;
import com.example.kanban.adapter.TarefaAdapter;
import com.example.kanban.helper.RecyclerItemClickListener;
import com.example.kanban.helper.TarefaDAO;
import com.example.kanban.model.Quadro;
import com.example.kanban.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class FragmentoFazendo extends Fragment {

    private RecyclerView mRecyclerView;
    private TarefaAdapter mTarefaAdapter;
    private List<Tarefa> mListaTarefas = new ArrayList<>();
    private TarefaDAO tarefaDAO;
    private Quadro quadro;
    private Activity mActivity;

    private TarefaAdapter mAdaptadorTarefaAFazer;
    private TarefaAdapter mAdaptadorTarefaFazendo;
    private TarefaAdapter mAdaptadorTarefaFeito;

    private List<Tarefa> listaTarefasAFazer = new ArrayList<>();
    private List<Tarefa> listaTarefasFazendo = new ArrayList<>();
    private List<Tarefa> listaTarefasFeito = new ArrayList<>();

    public FragmentoFazendo(Activity activity){
        mActivity = activity;
        mTarefaAdapter = new TarefaAdapter(mListaTarefas, mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fazendo, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void atualizarListaTarefas(Quadro quadro) {
        System.out.println("PASSEI AQUI");
        this.quadro = quadro;
        tarefaDAO = new TarefaDAO(mActivity);
        mListaTarefas = tarefaDAO.listarTarefasPorSituacao(quadro.getId(), "FAZENDO");
        mTarefaAdapter.atualizarTarefas(mListaTarefas);
        System.out.println("FAZENDO: ");
        for (Tarefa tarefa: mListaTarefas) {
            System.out.println(tarefa.getmTituloTarefa());
        }


//        mAdaptadorTarefaAFazer = new TarefaAdapter(listaTarefasAFazer, mActivity);
//        mAdaptadorTarefaFazendo = new TarefaAdapter(listaTarefasFazendo, mActivity);
//        mAdaptadorTarefaFeito = new TarefaAdapter(listaTarefasFeito, mActivity);
//
//        // Atualizar os adaptadores com as novas listas de tarefas
//        mAdaptadorTarefaAFazer.atualizarTarefas(listaTarefasAFazer);
//        mAdaptadorTarefaFazendo.atualizarTarefas(listaTarefasFazendo);
//        mAdaptadorTarefaFeito.atualizarTarefas(listaTarefasFeito);

        //mTarefaAdapter.notifyDataSetChanged();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        quadro = (Quadro) getActivity().getIntent().getSerializableExtra("quadro");
        Log.d(TAG, "onViewCreated FragFazendo: " + quadro.getId());

        mRecyclerView = view.findViewById(R.id.recyclerViewFazendo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        mTarefaAdapter = new TarefaAdapter(mListaTarefas, mActivity);
        mRecyclerView.setAdapter(mTarefaAdapter);


        mTarefaAdapter.atualizarTarefas(mListaTarefas);
        atualizarListaTarefas(quadro);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mActivity, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }

                    @Override
                    public void onItemClick(View view, int position) {
                        exibirOpcoesTarefa(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        exibirOpcoesTarefa(position);
                    }

                })
        );
    }
    private void exibirOpcoesTarefa(int position) {
        // Obtenha o quadro selecionado com base na posição
        Tarefa tarefaSelecionada = mListaTarefas.get(position);

        // Crie um AlertDialog para exibir as opções de remover e editar
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Opções da Tarefa")
                .setItems(new CharSequence[]{"Editar", "Remover"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //editar
                                abrirDialogoEdicao(tarefaSelecionada);
                                break;
                            case 1:
                                //deletar
                                boolean sucessoDelecao = tarefaDAO.deletar(tarefaSelecionada);
                                if(sucessoDelecao){
                                    Toast.makeText(mActivity, "Tarefa removida!", Toast.LENGTH_SHORT).show();
                                    mListaTarefas.remove(tarefaSelecionada);
                                    atualizarListaTarefas(quadro);
                                    mTarefaAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(mActivity, "Erro ao remover tarefa", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                })
                .show();
    }

    private void abrirDialogoEdicao(final Tarefa tarefa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Editar Tarefa");

        // Configurar o layout do diálogo de edição (similar à AddTarefaDialog)
        View viewInflated = LayoutInflater.from(mActivity).inflate(R.layout.edit_tarefa_dialog, null);
        final EditText editTextTitulo = viewInflated.findViewById(R.id.editTextTituloTarefa);
        final RadioGroup radioGroupSituacao = viewInflated.findViewById(R.id.radioGroupSituacao);

        // Preencher os campos do diálogo de edição com os dados da tarefa
        editTextTitulo.setText(tarefa.getmTituloTarefa());
        // Definir o RadioButton correto com base na situação da tarefa
        switch (tarefa.getStatus()){
            case "A FAZER":
                radioGroupSituacao.check(R.id.radioButtonAFazer);
                break;

            case "FAZENDO":
                radioGroupSituacao.check(R.id.radioButtonFazendo);
                break;

            case "FEITO":
                radioGroupSituacao.check(R.id.radioButtonFeito);
                break;
        }

        builder.setView(viewInflated);

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Atualizar os dados da tarefa com base nos campos do diálogo
                String novoTitulo = editTextTitulo.getText().toString();
                int radioButtonId = radioGroupSituacao.getCheckedRadioButtonId();
                if(!novoTitulo.isEmpty() && radioButtonId != -1){

                    tarefa.setmTituloTarefa(novoTitulo);
                    switch (radioButtonId){
                        case 2131296613:
                            tarefa.setStatus("A FAZER");
                            break;
                        case 2131296614:
                            tarefa.setStatus("FAZENDO");
                            break;
                        case 2131296615:
                            tarefa.setStatus("FEITO");
                            break;
                    }
                    boolean sucessoAtualizacao = tarefaDAO.atualizar(tarefa);
                    if (sucessoAtualizacao) {
                        Toast.makeText(mActivity, "Tarefa editada!", Toast.LENGTH_SHORT).show();
                        atualizarListaTarefas(quadro);

                    } else {
                        Toast.makeText(mActivity, "Erro ao atualizar a tarefa!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mActivity, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }
}

