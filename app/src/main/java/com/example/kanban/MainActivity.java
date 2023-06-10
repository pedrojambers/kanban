package com.example.kanban;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.kanban.adapter.QuadroAdapter;
import com.example.kanban.databinding.ActivityMainBinding;
import com.example.kanban.helper.QuadroDAO;
import com.example.kanban.helper.RecyclerItemClickListener;
import com.example.kanban.model.Quadro;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewQuadros;
    private ActivityMainBinding binding;
    private List<Quadro> listaQuadros = new ArrayList<>();
    private QuadroAdapter mAdaptadorQuadro;
    private QuadroDAO quadroDAO;

    public void criarNovoQuadro(Quadro quadro, boolean sucesso){
//        QuadroDAO quadroDAO = new QuadroDAO(getApplicationContext());
//        boolean sucesso = quadroDAO.salvar(quadro);

        if (sucesso) {
            Toast.makeText(this, "Quadro inserido com sucesso", Toast.LENGTH_SHORT).show();
            listaQuadros.add(quadro);
            atualizarListaQuadros();
            mAdaptadorQuadro.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Erro ao inserir quadro no banco de dados", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quadroDAO = new QuadroDAO(getApplicationContext());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddQuadroDialog addQuadroDialog = new AddQuadroDialog();
                addQuadroDialog.show(getSupportFragmentManager(), "");
            }
        });

        recyclerViewQuadros = findViewById(R.id.recyclerViewQuadros);
        mAdaptadorQuadro = new QuadroAdapter(this, listaQuadros);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager((getApplicationContext()));
        recyclerViewQuadros.setLayoutManager(mLayoutManager);
        recyclerViewQuadros.setItemAnimator(new DefaultItemAnimator());
        recyclerViewQuadros.setAdapter(mAdaptadorQuadro);
        atualizarListaQuadros();


        recyclerViewQuadros.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerViewQuadros, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }

                    @Override
                    public void onItemClick(View view, int position) {
                        abreQuadro(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        exibirOpcoesQuadro(position);
                    }

                })
        );
    }

    private void atualizarListaQuadros() {
        QuadroDAO quadroDAO = new QuadroDAO(getApplicationContext());
        listaQuadros.clear();
        listaQuadros.addAll(quadroDAO.listar());
        mAdaptadorQuadro.notifyDataSetChanged();

        Log.d("INFO", "Quantidade de quadros: " + listaQuadros.size());
    }

    private void exibirOpcoesQuadro(int position) {
        // Obtenha o quadro selecionado com base na posição
        Quadro quadroSelecionado = listaQuadros.get(position);

        // Crie um AlertDialog para exibir as opções de remover e editar
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opções do Quadro")
                .setItems(new CharSequence[]{"Editar", "Remover"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //editar
                                exibirDialogEditarQuadro(quadroSelecionado, position);
                                break;
                            case 1:
                                //deletar
                                boolean sucessoDelecao = quadroDAO.deletar(quadroSelecionado);
                                if(sucessoDelecao){
                                    Toast.makeText(MainActivity.this, "Quadro removido!", Toast.LENGTH_SHORT).show();
                                    listaQuadros.remove(quadroSelecionado);
                                    atualizarListaQuadros();
                                    mAdaptadorQuadro.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MainActivity.this, "Erro ao remover quadro", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                })
                .show();
    }

    private void exibirDialogEditarQuadro(final Quadro quadro, int position){



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Quadro");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.editar_quadro_dialog, null);

        final EditText editTextTitulo = viewInflated.findViewById(R.id.editTextTituloQuadro);

        editTextTitulo.setText(quadro.getmTituloQuadro());

        builder.setView(viewInflated);

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String novoTitulo = editTextTitulo.getText().toString();
                if(!novoTitulo.isEmpty()){
                    quadro.setmTituloQuadro(novoTitulo);
                    boolean sucessoAtualizacao = quadroDAO.atualizar(quadro);
                    if (sucessoAtualizacao) {
                        Toast.makeText(MainActivity.this, "Quadro editado!", Toast.LENGTH_SHORT).show();
                        mAdaptadorQuadro.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Erro ao atualizar o quadro!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Digite um título para o quadro!", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void abreQuadro(int position) {

        Quadro quadro = listaQuadros.get(position);
        Log.d(TAG, "abreQuadro: " + quadro.getId());
        Intent intent = new Intent(this, QuadroDetailActivity.class);
        intent.putExtra("quadro", quadro);
        startActivity(intent);
    }


}