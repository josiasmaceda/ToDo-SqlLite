package br.com.maceda.todo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.maceda.todo.vo.Repositorio;

/**
 * Created by josias on 30/04/2016.
 */
public class RepositorioDao {

    private static final String TABELA = "Repositorio";
    private static final String TAG = "RepositorioDAO";
    private BaseDao dao;
    private static RepositorioDao INSTANCE;
    private Context context;

    public static RepositorioDao getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RepositorioDao(context);
        }
        return INSTANCE;
    }

    private RepositorioDao(Context context) {
        this.context = context;
        dao = new BaseDao(context);
    }

    public long save(Repositorio repositorio) {
        SQLiteDatabase database = dao.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("descricao", repositorio.getDescricao());
            values.put("data_hora", repositorio.getDataHora());
            values.put("data_hora_long", repositorio.getDataHoraLong());
            long id = database.insert(TABELA, null, values);
            return id;
        } finally {
            database.close();
        }
    }

    public void delete(Repositorio repositorio) {
        SQLiteDatabase database = dao.getWritableDatabase();
        try {
            String id = String.valueOf(repositorio.getId());
            String[] args = new String[]{id};
            database.delete(TABELA, "_id = ?", args);
        } finally {
            database.close();
        }
    }

    public List<Repositorio> findAll() {
        SQLiteDatabase db = dao.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABELA, null, null, null, null, null, null, null);
            return toList(cursor);
        } finally {
            db.close();
        }

    }

    private List<Repositorio> toList(Cursor cursor) {
        List<Repositorio> list = new ArrayList<Repositorio>();

        if (cursor.moveToFirst()) {
            do {
                Log.d(TAG, "toList: ");
                Repositorio repositorio = new Repositorio();
                repositorio.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
                repositorio.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                repositorio.setDataHora(cursor.getString(cursor.getColumnIndexOrThrow("data_hora")));
                repositorio.setDataHoraLong(cursor.getLong(cursor.getColumnIndexOrThrow("data_hora_long")));
                list.add(repositorio);
                Log.d(TAG, repositorio.getDescricao());

            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Repositorio> findByDescricao(String descricao) {
        SQLiteDatabase db = dao.getWritableDatabase();
        try {
            String[] args = new String[]{'%' + descricao + '%'};
            Cursor cursor = db.query(TABELA, null, "descricao like ?", args, null, null, null, null);
            return toList(cursor);
        } finally {
            db.close();
        }
    }
}
