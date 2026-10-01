package com.example.swimmingtraining;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class WhatchVideo extends AppCompatActivity {
    private final FirebaseListeners listeners = new FirebaseListeners();

    List<UVideo> uvideo;
    ListView listvideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatch_video);
        listvideo = findViewById(R.id.listviewvideo);
        uvideo = new ArrayList<>();
        registerForContextMenu(listvideo);
        DatabaseReference dbvideo = FirebaseDatabase.getInstance().getReference("video");
        listeners.add(dbvideo, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uvideo.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UVideo a = postSnapshot.getValue(UVideo.class);
                    uvideo.add(a);
                }

                String[] uploads = new String[uvideo.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uvideo.get(i).getName_video() + "\n"
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
                String[] parts = selectedFromList.split("\n");
                String part1 = parts[1]; // UID книги
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse(part1));
                startActivity(browserIntent);
//                switch (position) {
//                    case 0:
//                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        listeners.removeAll();
        super.onDestroy();
    }
}
