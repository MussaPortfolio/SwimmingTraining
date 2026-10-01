package com.example.swimmingtraining;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListVideoTrainer extends AppCompatActivity {

    ListView listVideoTrainer;
    List<USsilka> ussilka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video_trainer);
        ussilka = new ArrayList<>();
        listVideoTrainer = (ListView) findViewById(R.id.list_trainer);
        registerForContextMenu(listVideoTrainer);

        //Подключение к бд
        DatabaseReference dbrol = FirebaseDatabase.getInstance().getReference("rol");
        DatabaseReference dbsportsman = dbrol.child("trainer");

        //Вывод списка
        dbsportsman.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ussilka.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    USsilka plist = postSnapshot.getValue(USsilka.class);
                    ussilka.add(plist);
                }

                String[] uplist = new String[ussilka.size()];

                for (int i = 0; i < uplist.length; i++) {
                    uplist[i] = ussilka.get(i).getOt() + "\n"+ ussilka.get(i).getUrl();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uplist);
                listVideoTrainer.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Обработка нажатия на элемент listview
        listVideoTrainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0,
                                    View arg1, int position, long arg3)
            {

                //вычисление uid нажатого
                String selectedFromList = (listVideoTrainer.getItemAtPosition(position)).toString();
                String[] parts = selectedFromList.split("\n");
                String part1 = parts[1]; // UID книги

                //переход по uid
                DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference("users");
                DatabaseReference dbuser = dbusers.child(part1);

                //Перелача данных в другое активити
                Intent intent = new Intent(ListVideoTrainer.this, WhatchVideoToSsilka.class);

                // в ключ username пихаем текст из первого текстового поля
                intent.putExtra("pssilka", part1);

                //запуск активити
                startActivity(intent);
            }
        });
    }
}
