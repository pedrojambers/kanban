package com.example.kanban;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.kanban.adapter.TarefaAdapter;
import com.example.kanban.fragments.FragmentoAFazer;
import com.example.kanban.fragments.FragmentoFazendo;
import com.example.kanban.fragments.FragmentoFeito;
import com.example.kanban.helper.TarefaDAO;
import com.example.kanban.model.Quadro;
import com.example.kanban.model.Tarefa;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class QuadroDetailActivity extends AppCompatActivity {

    private Quadro quadro;
    private Tarefa tarefa;
    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    private TextView textViewTitulo;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private TarefaAdapter mAdaptadorTarefa;
    private TarefaDAO tarefaDAO;

    private List<Tarefa> listaTarefasAFazer = new ArrayList<>();
    private List<Tarefa> listaTarefasFazendo = new ArrayList<>();
    private List<Tarefa> listaTarefasFeito = new ArrayList<>();
    private TarefaAdapter mAdaptadorTarefaAFazer;
    private TarefaAdapter mAdaptadorTarefaFazendo;
    private TarefaAdapter mAdaptadorTarefaFeito;

    public void criarNovaTarefa(Tarefa tarefa, boolean sucesso){
        if (sucesso) {
            Toast.makeText(this, "Tarefa inserida com sucesso", Toast.LENGTH_SHORT).show();
            String situacao = tarefa.getStatus();
            Log.d(TAG, "criarNovaTarefa: " + quadro.getId());
            //QUADRO RECEBIDO NORMALMENTE AQUI

            if (situacao.equals("A FAZER")) {
                listaTarefasAFazer.add(tarefa);
                mAdaptadorTarefaAFazer.atualizarTarefas(listaTarefasAFazer);
            } else if (situacao.equals("FAZENDO")) {
                listaTarefasFazendo.add(tarefa);
                mAdaptadorTarefaFazendo.atualizarTarefas(listaTarefasFazendo);
            } else if (situacao.equals("FEITO")) {
                listaTarefasFeito.add(tarefa);
                mAdaptadorTarefaFeito.atualizarTarefas(listaTarefasFeito);
            }
            FragmentoAFazer fragmentoAFazer = (FragmentoAFazer) tabAdapter.getItem(0);
            fragmentoAFazer.atualizarListaTarefas(quadro);

            FragmentoFazendo fragmentoFazendo = (FragmentoFazendo) tabAdapter.getItem(1);
            fragmentoFazendo.atualizarListaTarefas(quadro);

            FragmentoFeito fragmentoFeito = (FragmentoFeito) tabAdapter.getItem(2);
            fragmentoFeito.atualizarListaTarefas(quadro);

            atualizarListasTarefas();
        } else {
            Toast.makeText(this, "Erro ao inserir tarefa no banco de dados", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarListasTarefas();
    }

    private void atualizarListasTarefas() {
        tarefaDAO = new TarefaDAO(this);
        quadro = (Quadro) getIntent().getSerializableExtra("quadro");

        mAdaptadorTarefaAFazer = new TarefaAdapter(listaTarefasAFazer, QuadroDetailActivity.this);
        mAdaptadorTarefaFazendo = new TarefaAdapter(listaTarefasFazendo, QuadroDetailActivity.this);
        mAdaptadorTarefaFeito = new TarefaAdapter(listaTarefasFeito, QuadroDetailActivity.this);

        listaTarefasAFazer = tarefaDAO.listarTarefasPorSituacao(quadro.getId(), "A FAZER");
        listaTarefasFazendo = tarefaDAO.listarTarefasPorSituacao(quadro.getId(), "FAZENDO");
        listaTarefasFeito = tarefaDAO.listarTarefasPorSituacao(quadro.getId(), "FEITO");

        mAdaptadorTarefaAFazer = new TarefaAdapter(listaTarefasAFazer, QuadroDetailActivity.this);
        mAdaptadorTarefaFazendo = new TarefaAdapter(listaTarefasFazendo, QuadroDetailActivity.this);
        mAdaptadorTarefaFeito = new TarefaAdapter(listaTarefasFeito, QuadroDetailActivity.this);

        // Atualizar os adaptadores com as novas listas de tarefas
        mAdaptadorTarefaAFazer.atualizarTarefas(listaTarefasAFazer);
        mAdaptadorTarefaFazendo.atualizarTarefas(listaTarefasFazendo);
        mAdaptadorTarefaFeito.atualizarTarefas(listaTarefasFeito);

        // Notificar os adaptadores sobre as mudanças nos dados
        mAdaptadorTarefaAFazer.notifyDataSetChanged();
        mAdaptadorTarefaFazendo.notifyDataSetChanged();
        mAdaptadorTarefaFeito.notifyDataSetChanged();

        Log.d("QuadroDetailActivity", "Lista A FAZER size: " + listaTarefasAFazer.size() + " id_quadro: " + quadro.getId());
        Log.d("QuadroDetailActivity", "Lista FAZENDO size: " + listaTarefasFazendo.size()+ " id_quadro: " + quadro.getId());
        Log.d("QuadroDetailActivity", "Lista FEITO size: " + listaTarefasFeito.size()+ " id_quadro: " + quadro.getId());


        if (listaDeTarefasVazia(listaTarefasAFazer)) {
            Log.d("QuadroDetailActivity", "Lista A FAZER vazia!");
        }

        if (listaDeTarefasVazia(listaTarefasFazendo)) {
            Log.d("QuadroDetailActivity", "Lista FAZENDO vazia!");
        }

        if (listaDeTarefasVazia(listaTarefasFeito)) {
            Log.d("QuadroDetailActivity", "Lista FEITO vazia!");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quadro_detail);

        quadro = (Quadro) getIntent().getSerializableExtra("quadro");
        Log.d(TAG, "onCreate: " + quadro.getId());
        //AQUI ESTA RECEBENDO O QUADRO NORMALMENTE

        textViewTitulo = findViewById(R.id.textTituloQuadro);
        textViewTitulo.setText(quadro.getmTituloQuadro());

        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        FragmentoAFazer fragmentoAFazer = new FragmentoAFazer(QuadroDetailActivity.this);
        Bundle bundleAFazer = new Bundle();
        bundleAFazer.putSerializable("quadro", quadro);
        fragmentoAFazer.setArguments(bundleAFazer);
        tabAdapter.addFragment(fragmentoAFazer, "A FAZER");

        FragmentoFazendo fragmentoFazendo = new FragmentoFazendo(QuadroDetailActivity.this);
        Bundle bundleFazendo = new Bundle();
        bundleFazendo.putSerializable("quadro", quadro);
        fragmentoFazendo.setArguments(bundleFazendo);
        tabAdapter.addFragment(fragmentoFazendo, "FAZENDO");

        FragmentoFeito fragmentoFeito = new FragmentoFeito(QuadroDetailActivity.this);
        Bundle bundleFeito = new Bundle();
        bundleFeito.putSerializable("quadro", quadro);
        fragmentoFeito.setArguments(bundleFeito);
        tabAdapter.addFragment(fragmentoFeito, "FEITO");


        mAdaptadorTarefaAFazer = new TarefaAdapter(listaTarefasAFazer, this);
        mAdaptadorTarefaFazendo = new TarefaAdapter(listaTarefasFazendo, this);
        mAdaptadorTarefaFeito = new TarefaAdapter(listaTarefasFeito, this);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                // Executar a lógica específica com base na posição da tab
                if (position == 0) {
                    FragmentoAFazer fragmentoAFazer = (FragmentoAFazer) tabAdapter.getItem(0);
                    fragmentoAFazer.atualizarListaTarefas(quadro);
                } else if (position == 1) {
                    FragmentoFazendo fragmentoFazendo = (FragmentoFazendo) tabAdapter.getItem(1);
                    fragmentoFazendo.atualizarListaTarefas(quadro);
                } else if (position == 2) {
                    FragmentoFeito fragmentoFeito = (FragmentoFeito) tabAdapter.getItem(2);
                    fragmentoFeito.atualizarListaTarefas(quadro);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        viewPager.setCurrentItem(1);

        ExtendedFloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTarefaDialog();
            }
        });

        FloatingActionButton fab3 = findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        tarefaDAO = new TarefaDAO(this);
        atualizarListasTarefas();
    }

    private boolean listaDeTarefasVazia(List<Tarefa> listaTarefas) {
        return listaTarefas == null || listaTarefas.isEmpty();
    }

    private void AddTarefaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Nova Tarefa");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.add_tarefa_dialog, null);

        final EditText editTextTitulo = viewInflated.findViewById(R.id.editTextTituloTarefa);
        final RadioGroup radioGroupSituacao = viewInflated.findViewById(R.id.radioGroupSituacao);

        builder.setView(viewInflated);

        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String titulo = editTextTitulo.getText().toString().trim();
                int selectedId = radioGroupSituacao.getCheckedRadioButtonId();
                Log.d("RadioButton", "Código do RadioButton: " + selectedId);
                Log.d(TAG, "Botao adicionar: " + quadro.getId());
                //AQUI ESTA RECEBENDO O QUADRO NORMALMENTE
                if (!titulo.isEmpty() && selectedId != -1) {
                    RadioButton selectedRadioButton = viewInflated.findViewById(selectedId);
                    String situacao = selectedRadioButton.getText().toString();

                    Tarefa tarefa = new Tarefa();
                    tarefa.setmTituloTarefa(titulo);
                    tarefa.setStatus(situacao);
                    tarefa.setIdQuadro(quadro.getId());

                    boolean sucesso = tarefaDAO.salvar(tarefa);
                    criarNovaTarefa(tarefa, sucesso);
                } else {
                    Toast.makeText(QuadroDetailActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



    private class TabAdapter extends FragmentPagerAdapter{
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public TabAdapter(FragmentManager fm){
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public CharSequence getPageTitle(int position){
            return fragmentTitleList.get(position);
        }
    }
}
