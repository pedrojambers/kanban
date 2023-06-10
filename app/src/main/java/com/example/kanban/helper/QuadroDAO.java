package com.example.kanban.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kanban.model.Quadro;

import java.util.ArrayList;
import java.util.List;

public class QuadroDAO implements iQuadroDAO {

    private SQLiteDatabase escreve;
    private SQLiteDatabase le;


    public QuadroDAO(Context context) {
        DBHelper db = new DBHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Quadro quadro) {

        ContentValues cv = new ContentValues();
        cv.put("titulo", quadro.getmTituloQuadro());

        try {
            Long id = escreve.insert(DBHelper.TABELA_QUADROS, null, cv);
            Log.e("INFO", "Quadro salvo com sucesso id:" + id);
        } catch(Exception e) {
            Log.e("INFO", "Erro ao salvar quadro" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean atualizar(Quadro quadro) {
        ContentValues cv = new ContentValues();
        cv.put("titulo", quadro.getmTituloQuadro());

        String[] args = {String.valueOf(quadro.getId())};

        try{
            int linhasAfetadas = escreve.update(DBHelper.TABELA_QUADROS, cv, "id=?", args);
            return linhasAfetadas > 0;
        } catch (Exception e){
            Log.e("INFO", "Erro ao atualizar o quadro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletar(Quadro quadro) {
        String[] args = {String.valueOf(quadro.getId())};

        try{
            int linhasAfetadas = escreve.delete(DBHelper.TABELA_QUADROS, "id=?", args);
            Log.e("INFO", "Quadro removido caraio" );
            return linhasAfetadas > 0;
        } catch (Exception e){
            Log.e("INFO", "Erro ao deletar quadro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Quadro> listar() {

        List<Quadro> quadros = new ArrayList<>();

        String sql = "SELECT * FROM " + DBHelper.TABELA_QUADROS + " ;" ;
        Cursor c = le.rawQuery(sql, null);

        while(c.moveToNext()){
            Quadro quadro = new Quadro();

            @SuppressLint("Range") Long id = c.getLong(c.getColumnIndex("id"));
            @SuppressLint("Range") String nomeQuadro = c.getString(c.getColumnIndex("titulo"));
            quadro.setId(id);
            quadro.setmTituloQuadro(nomeQuadro);

            quadros.add(quadro);
        }
        c.close();
        return quadros;
    }
}
