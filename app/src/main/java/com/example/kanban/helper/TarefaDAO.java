package com.example.kanban.helper;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kanban.helper.DBHelper;
import com.example.kanban.helper.iTarefaDAO;
import com.example.kanban.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO implements iTarefaDAO {

    private SQLiteDatabase escreve;
    private SQLiteDatabase le;

    public TarefaDAO(Context context) {
        DBHelper db = new DBHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("titulo", tarefa.getmTituloTarefa());
        cv.put("status", tarefa.getStatus());
        cv.put("id_quadro", tarefa.getIdQuadro());

        try {
            Long id = escreve.insert(DBHelper.TABELA_TAREFAS, null, cv);
            Log.e("INFO", "Tarefa salva com sucesso. ID: "
                    + id + " Status: " + tarefa.getStatus() + " id_quadro: " + tarefa.getIdQuadro());
            return true;
        } catch (Exception e) {
            Log.e("INFO", "Erro ao salvar tarefa: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("titulo", tarefa.getmTituloTarefa());
        cv.put("status", tarefa.getStatus());
        cv.put("id_quadro", tarefa.getIdQuadro());

        String[] args = {String.valueOf(tarefa.getId())};

        try {
            int linhasAfetadas = escreve.update(DBHelper.TABELA_TAREFAS, cv, "id=?", args);
            return linhasAfetadas > 0;
        } catch (Exception e) {
            Log.e("INFO", "Erro ao atualizar a tarefa: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletar(Tarefa tarefa) {
        String[] args = {String.valueOf(tarefa.getId())};

        try {
            int linhasAfetadas = escreve.delete(DBHelper.TABELA_TAREFAS, "id=?", args);
            return linhasAfetadas > 0;
        } catch (Exception e) {
            Log.e("INFO", "Erro ao deletar tarefa: " + e.getMessage());
            return false;
        }
    }

    @SuppressLint("Range")
    @Override
    public List<Tarefa> listarTarefasPorSituacao(long idQuadro, String situacao) {
        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM " + DBHelper.TABELA_TAREFAS + " WHERE id_quadro = " + idQuadro + " AND status = '" + situacao + "'";
        Cursor cursor = le.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            Tarefa tarefa = new Tarefa();
            tarefa.setId(cursor.getLong(cursor.getColumnIndex("id")));
            tarefa.setmTituloTarefa(cursor.getString(cursor.getColumnIndex("titulo")));
            tarefa.setStatus(cursor.getString(cursor.getColumnIndex("status")));
            tarefa.setIdQuadro(cursor.getLong(cursor.getColumnIndex("id_quadro")));

            tarefas.add(tarefa);
        }
        cursor.close();
        return tarefas;
    }
}
