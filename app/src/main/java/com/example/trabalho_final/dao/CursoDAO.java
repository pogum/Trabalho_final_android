package com.example.trabalho_final.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class CursoDAO {

    public static final String CREATE_SCRIPT =
            "CREATE TABLE IF NOT EXISTS cursos (\n" +
                    "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\tnome TEXT NOT NULL,\n" +
                    "\n" +
                    "\tFOREIGN KEY(usuario_id) REFERENCES usuarios(id)\n" +
                    ");";


    private static final String TABLE_NAME = "cursos";
    private static final String ID_COLUMN = "id";
    private static final String NOME_COLUMN = "nome";
    private static final String USUARIO_COLUMN = "usuario_id";

    private AppDB appDB;

    public CursoDAO(AppDB appDB) {
        this.appDB = appDB;
    }

    public boolean create(Curso newCurso) {
        boolean result = true;
        SQLiteDatabase writeDb = this.appDB.getWritableDatabase();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NOME_COLUMN, newCurso.getNomeCurso());
            contentValues.put(USUARIO_COLUMN, newCurso.getUsuarioId().getId());
            writeDb.insert(TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            result = false;
        } finally {
            writeDb.close();
        }

        return result;
    }

    @SuppressLint("Range")
    public Curso getById(Curso curso) {
        Curso result = null;
        UsuarioDAO usuarioDAO = new UsuarioDAO(this.appDB);
        SQLiteDatabase readDb = this.appDB.getReadableDatabase();

        try {
            String clause = ID_COLUMN + "=?";
            String[] params = { curso.getId() + "" };
            Cursor c = readDb.query(TABLE_NAME, null, clause, params, null, null, null, null);

            if (c.moveToFirst()) {
                int id = c.getInt(c.getColumnIndex(ID_COLUMN));
                String name = c.getString(c.getColumnIndex(NOME_COLUMN));

                int usuarioId = c.getInt(c.getColumnIndex(USUARIO_COLUMN));
                Usuario usuario = usuarioDAO.getById(new Usuario(usuarioId));

                result = new Curso(id,name,usuario);
            }
        } catch (Exception e) {
            result = null;
        } finally {
            readDb.close();
        }
        return result;
    }

}
