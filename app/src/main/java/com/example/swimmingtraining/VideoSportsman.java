package com.example.swimmingtraining;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VideoSportsman extends AppCompatActivity {
    private final FirebaseListeners listeners = new FirebaseListeners();

    List<UVideoTrainer> uvideo;
    ListView listvideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_sportsman);
        listvideo = findViewById(R.id.list_video_sportsman);
        uvideo = new ArrayList<>();
        registerForContextMenu(listvideo);
        DatabaseReference dbvideo = FirebaseDatabase.getInstance().getReference("zagvideo");
        DatabaseReference dbvideotrainer = dbvideo.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        listeners.add(dbvideotrainer, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uvideo.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UVideoTrainer b = postSnapshot.getValue(UVideoTrainer.class);
                    uvideo.add(b);
                }

                String[] uploads = new String[uvideo.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uvideo.get(i).getName_video() + "\n"
                            + uvideo.get(i).getAuthor() + "\n"
                            + uvideo.get(i).getData_video() + "\n"
                            + uvideo.get(i).getUrl() + "\n";
                }

//displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                listvideo.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Обработка нажатия на элемент listview
        listvideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0,
                                    View arg1, int position, long arg3)
            {
                String selectedFromList = (listvideo.getItemAtPosition(position)).toString();

                //Получение url видео
                String[] parts = selectedFromList.split("\n");
                String part1 = parts[3]; // UID книги

                //Перелача данных в другое активити
                Intent intent = new Intent(VideoSportsman.this, WhatchVideoToSsilka.class);

                // в ключ username пихаем текст из первого текстового поля
                intent.putExtra("pssilka", part1);

                //запуск активити
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        listeners.removeAll();
        super.onDestroy();
    }
}
