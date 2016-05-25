package br.com.maceda.todo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by josias on 30/04/2016.
 */
public class BaseDao extends SQLiteOpenHelper {

    private static final String nomeDoBanco = "Todo";
    private static final int versaoDoBanco = 1;

    private static final String[] scriptSQLCreate =
            new String[]{" Create table if not exists Repositorio (" +
                         " _id integer primary key autoincrement, " +
                         " descricao text not null, " +
                         " data_hora DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                         " data_hora_long long "+
                         " );"};


    public BaseDao(Context context) {
        super(context, nomeDoBanco, null, versaoDoBanco);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        int scripts = scriptSQLCreate.length;

        for (int i = 0; i < scripts; i++) {
            String sql = scriptSQLCreate[i];
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
