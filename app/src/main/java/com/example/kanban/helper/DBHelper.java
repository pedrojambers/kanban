package com.example.kanban.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.kanban.model.Quadro;

public class DBHelper extends SQLiteOpenHelper {

    public static int VERSION = 5;
    public static String NOME_DB = "DB_QUADROS";
    public static String TABELA_QUADROS = "quadros";

    public static String TABELA_TAREFAS = "tarefas";

    public DBHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Criacao tabela quadros
        String sqlQuadros = "CREATE TABLE IF NOT EXISTS " + TABELA_QUADROS
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " titulo TEXT NOT NULL ); ";

        //Criacao tabela tarefas
        String sqlTarefas = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFAS
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " id_quadro INTEGER, " + //foreign key referenciando o quadro
                " titulo TEXT NOT NULL, " +
                " status TEXT NOT NULL ); ";
        try {
            db.execSQL(sqlQuadros);
            db.execSQL(sqlTarefas);
            Log.i("INFO DB", "Sucesso ao criar as tabelas");
        } catch (Exception e){
            Log.i("INFO DB", "Erro ao criar as tabelas" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABELA_QUADROS + ";";
        String sqlTarefas = "DROP TABLE IF EXISTS " + TABELA_TAREFAS + ";";
        try {
            db.execSQL(sql);
            db.execSQL(sqlTarefas);
            onCreate(db);
            Log.i("INFO DB", "Sucesso ao atualizar as tabelas");
        } catch (Exception e){
            Log.i("INFO DB", "Erro ao atualizar as tabelas" + e.getMessage());
        }

    }

}
