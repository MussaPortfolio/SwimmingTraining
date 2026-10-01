package com.example.swimmingtraining;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;

public class ListSportsmanAboutTrainer extends AppCompatActivity {
    //the listview
    ListView listViewListTraineer;
    public int position;
    public String vfamilia;
    public String vname;
    public String votchestvo;
    public String valueemail;
    public String vdr;
    public String vstage;
    public String vabout;
    FirebaseAuth firebaseAuth;
    //list to store uploads data
    List<UList> ulist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trainer);
        ulist = new ArrayList<>();
        listViewListTraineer = (ListView) findViewById(R.id.list_trainer);
        registerForContextMenu(listViewListTraineer);

        //Подключение к бд
        DatabaseReference dbrol = FirebaseDatabase.getInstance().getReference("rol");
        DatabaseReference dbsportsman = dbrol.child("trainer");

        //Вывод списка
        dbsportsman.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ulist.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UList plist = postSnapshot.getValue(UList.class);
                    ulist.add(plist);
                }

                String[] uplist = new String[ulist.size()];

                for (int i = 0; i < uplist.length; i++) {
                    uplist[i] = ulist.get(i).getFam() + " " + ulist.get(i).getIm() + " " + ulist.get(i).getOtch() + "\n"+ ulist.get(i).getUidik();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uplist);
                listViewListTraineer.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //контекстное меню
        listViewListTraineer.setOnCreateContextMenuListener(new AdapterView.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                ListSportsmanAboutTrainer.super.onCreateContextMenu(menu, v, menuInfo);
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.context_menu_trainer, menu);

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                position = info.position;


            }
        });
        //Обработка нажатия на элемент listview
        listViewListTraineer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0,
                                    View arg1, int position, long arg3)
            {

                //вычисление uid нажатого
                String selectedFromList = (listViewListTraineer.getItemAtPosition(position)).toString();
                String[] parts = selectedFromList.split("\n");
                String part1 = parts[1]; // UID книги

                //переход по uid
                DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference("users");
                DatabaseReference dbuser = dbusers.child(part1);

                //чтение с БД
                dbuser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        vfamilia = dataSnapshot.child("famailia").getValue(String.class);
                        vname = dataSnapshot.child("name").getValue(String.class);
                        votchestvo = dataSnapshot.child("otchestvo").getValue(String.class);
                        valueemail = dataSnapshot.child("email").getValue(String.class);
                        vdr = dataSnapshot.child("dr").getValue(String.class);
                        vstage = dataSnapshot.child("stuge").getValue(String.class);
                        vabout = dataSnapshot.child("about").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("Failed to read value.", error.toException());
                    }
                });

                //Перелача данных в другое активити
                Intent intent = new Intent(ListSportsmanAboutTrainer.this, AboutUser.class);

                // в ключ username пихаем текст из первого текстового поля
                intent.putExtra("pfamilia", vfamilia);
                intent.putExtra("pname", vname);
                intent.putExtra("potchestvo", votchestvo);
                intent.putExtra("pemail", valueemail);
                intent.putExtra("pdr", vdr);
                intent.putExtra("pstage", vstage);
                intent.putExtra("pabout", vabout);

                //запуск активити
                startActivity(intent);
            }
        });
    }

    //Дествие контектсного меню
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId())
        {
            case R.id.addtrainer:
                String selectedFromList = (listViewListTraineer.getItemAtPosition(position)).toString();
                String[] parts = selectedFromList.split("\n");
                String part1 = parts[1]; // UID книги
                String vuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                Toast.makeText(getApplicationContext(),vuid,Toast.LENGTH_SHORT).show();
                UZapros uzapros = new UZapros(vuid,part1);

                DatabaseReference dbzapros = FirebaseDatabase.getInstance().getReference("Zapros");
                DatabaseReference dbtraineer = dbzapros.child(part1);
                dbtraineer
                        .child(vuid)
                        .setValue(uzapros);
//                Toast.makeText(getApplicationContext(),firebaseAuth.getUid(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Запрос отправлен",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}