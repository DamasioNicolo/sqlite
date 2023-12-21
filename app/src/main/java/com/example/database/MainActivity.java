package com.example.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private EditText editText1;
    private Button btnInvia, btnShow;
    private TextView resultText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = findViewById(R.id.editText1);
        btnInvia = findViewById(R.id.btnInvia);
        btnShow = findViewById(R.id.btnShow);
        resultText = findViewById(R.id.resultText);

        dbHelper = new DatabaseHelper(this);

        btnInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStringToDatabase();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayStrings();
            }
        });
    }

    private void saveStringToDatabase() {
        String inputString = editText1.getText().toString();

        if (!inputString.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_STRING, inputString);
            long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);

            if (newRowId != -1) {
                Toast.makeText(this, "Stringa salvata con successo", Toast.LENGTH_SHORT).show();
                editText1.setText("");
            } else {
                Toast.makeText(this, "Errore nel salvataggio della stringa", Toast.LENGTH_SHORT).show();
            }

            db.close();
        } else {
            Toast.makeText(this, "Inserisci una stringa prima di salvare", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayStrings() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DatabaseHelper.COLUMN_STRING};
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        StringBuilder result = new StringBuilder("Stringhe salvate:\n");

        while (cursor.moveToNext()) {
            String savedString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STRING));
            result.append(savedString).append("\n");
        }

        cursor.close();
        db.close();

        resultText.setText(result.toString());
    }
}