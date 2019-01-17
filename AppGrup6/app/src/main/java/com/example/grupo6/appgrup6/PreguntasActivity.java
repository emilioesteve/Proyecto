package com.example.grupo6.appgrup6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class PreguntasActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;
    Button env;
    EditText hazPreg;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        env = findViewById(R.id.button2);
        hazPreg = findViewById(R.id.hazpreg);
        listView = findViewById(R.id.listview);

        list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, list);

        env.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String preguntas = hazPreg.getText().toString();

                list.add(preguntas);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Intent intent = new Intent(PreguntasActivity.this, SendMessage.class);
                        String message = list.get(position);
                        intent.putExtra(EXTRA_MESSAGE, message);
                        startActivity(intent);
                    }
                });
            }
        });


    }
}
