package com.example.kanban;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.example.kanban.helper.QuadroDAO;
import com.example.kanban.model.Quadro;

public class AddQuadroDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builer = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.add_quadro_dialog,null);

        final EditText editTitulo = dialogView.findViewById(R.id.editTextTituloQuadro);


        builer.setView(dialogView)
                .setTitle("Adicionar Quadro")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //CANCELA ADIÇÃO DO QUADRO
                    }
                })
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String titulo = editTitulo.getText().toString();

                        if (!titulo.isEmpty()) {
                            QuadroDAO quadroDAO = new QuadroDAO(getActivity().getApplicationContext());
                            Quadro quadro = new Quadro();
                            quadro.setmTituloQuadro(titulo);

                            boolean sucesso = quadroDAO.salvar(quadro);

                            if (sucesso) {
                                Log.i("INFO DB", "Quadro inserido com sucesso");
                                MainActivity activity = (MainActivity) getActivity();
                                activity.criarNovoQuadro(quadro,true);
                            } else {
                                Log.e("INFO DB", "Erro ao inserir quadro no banco de dados");
                            }
                        } else {
                            Toast.makeText(getActivity(), "Digite um título para o quadro!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        return builer.create();
    }
}
