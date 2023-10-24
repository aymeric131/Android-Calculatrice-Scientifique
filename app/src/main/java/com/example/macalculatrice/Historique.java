package com.example.macalculatrice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.StatusBarManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Historique extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    private ArrayList<String> historique = new ArrayList<>();
    private ArrayAdapter adapter;

    public ArrayList<String> getHistorique() {
        return historique;
    }

    public ArrayAdapter getAdapter() {
        return adapter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enelever la barre
        setContentView(R.layout.activity_historique);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        historique = intent.getStringArrayListExtra("Liste");
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, historique);
        ListView hist = findViewById(R.id.hist_view);
        hist.setAdapter(adapter);
        hist.setOnItemLongClickListener(this::onItemLongClick);

        Button back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

        Button trash_btn = (Button) findViewById(R.id.trash_btn);
        trash_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_btn) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (view.getId() == R.id.trash_btn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Nettoyage");
            builder.setMessage("Voulez-vous vider l'historique ?");
            builder.setPositiveButton("Oui", (dialogInterface, i) -> {
                historique.clear();
                adapter.notifyDataSetChanged();
                try {
                    //Ecriture
                    FileOutputStream fout = openFileOutput("listeCalcul.txt", this.MODE_PRIVATE);
                    OutputStreamWriter out = new OutputStreamWriter(fout);
                    for (String c : historique) {
                        out.write(c + '\n');
                    }
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            builder.setNegativeButton("Non", (dialogInterface, i) -> Toast.makeText(this, "Aucun élément supprimé", Toast.LENGTH_SHORT).show());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedOperation = "";
        ArrayList tableau = new ArrayList();
        String selectedLine = historique.get(i).replaceAll("\\s+", "");
        for (int y = 0; y < selectedLine.length(); y++) {
            tableau.add(selectedLine.charAt(y));
        }
        for (int z = 0; z < tableau.size(); z++) {
            String a = tableau.get(z).toString();
            if (a.equalsIgnoreCase("=")) {
                break;
            } else {
                selectedOperation += a;
            }
        }
        System.out.println(selectedOperation);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("selectedOperation", selectedOperation);
        startActivity(intent);
        return false;
    }
}