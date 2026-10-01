package com.example.swimmingtraining;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.*;

public class Registration extends AppCompatActivity implements ValueEventListener{
    private final FirebaseListeners listeners = new FirebaseListeners();

    EditText email,password,familia,name,otchestvo,dr, login, stuge, about;
    Button registerButton,loginButton;
    //public static String rol1;
    FirebaseAuth firebaseAuth;
    Spinner rol;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user = database.getReference("users");
    DatabaseReference roli = database.getReference("rol");
    DatabaseReference conformity = database.getReference("conformity");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Объявление компонентов GUI
        familia = (EditText) findViewById(R.id.familia);
        name = (EditText) findViewById(R.id.name);
        otchestvo = (EditText) findViewById(R.id.otchestvo);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.passw);
        dr = (EditText) findViewById(R.id.dr);
        registerButton = (Button) findViewById(R.id.reg);
        loginButton = (Button) findViewById(R.id.vhod);
        rol = findViewById(R.id.rol);
        stuge = findViewById(R.id.stuge);
        about = findViewById(R.id.about);
        login = (EditText) findViewById(R.id.login);

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void reg(View view) {
        //Перенос данных в строковые значения
        final String familia1 = familia.getText().toString();
        final String name1 = name.getText().toString();
        final String otchestvo1  = otchestvo.getText().toString();
        final String email1 = email.getText().toString();
        String password1 = password.getText().toString();
        final String dr1 = dr.getText().toString();
        final String srol = rol.getSelectedItem().toString();
        final String login1 = login.getText().toString();
        final String sstuge = stuge.getText().toString();
        final String sabout = about.getText().toString();

        //Выделение синим цветом строк
        familia.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        name.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        otchestvo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        dr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stuge.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        about.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        //Проверка на заполненость данных пользователем
        if(TextUtils.isEmpty(familia1)){
            familia.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            makeText(getApplicationContext(),"Пожалуйста заполните необходимые поля", LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(name1)){
            name.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            makeText(getApplicationContext(),"Пожалуйста заполните необходимые поля", LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(otchestvo1)){
            otchestvo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            makeText(getApplicationContext(),"Пожалуйста заполните необходимые поля", LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email1)){
            email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            makeText(getApplicationContext(),"Пожалуйста заполните необходимые поля", LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password1)){
            password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            makeText(getApplicationContext(),"Пожалуйста заполните необходимые поля", LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(dr1)){
            dr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            makeText(getApplicationContext(),"Пожалуйста заполните необходимые поля", LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(dr1)){
            stuge.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            makeText(getApplicationContext(),"Пожалуйста заполните необходимые поля", LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(dr1)){
            about.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            makeText(getApplicationContext(),"Пожалуйста заполните необходимые поля", LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase mDatabaseReference = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabaseReference1 = mDatabaseReference.getReference("users");



        //Проверка на наличие аутентификатора
        firebaseAuth.createUserWithEmailAndPassword(email1,password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if(currentFirebaseUser !=null)

                            {
                                //внос данных в таблицу user -> бд
                                Upload uploadSportsmanUser = new Upload(name1,familia1, otchestvo1, dr1, login1, email1, srol, "0",sstuge,sabout);
                                mDatabaseReference1.child(firebaseAuth.getUid()).setValue(uploadSportsmanUser);
                            } else {
                            }

                            //Вход к нужному пользователю
                            DatabaseReference uid = user.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            listeners.add(uid, new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final String rol1 = dataSnapshot.child("rol").getValue(String.class);

                                    if (rol1.equals("Sportsman")){
                                        String uid = firebaseAuth.getUid();
                                        UList ulistik = new UList(familia1, name1, otchestvo1,uid);

                                        //Запись спортсменов в БД
                                        mDatabaseReference1.child(firebaseAuth.getUid()).child("trainer").setValue(" ");

                                        //Запись в список спортсменов
                                        roli
                                                .child("sportsman")
                                                .child(firebaseAuth.getUid())
                                                .setValue(ulistik);

                                        //Перехдод на активити
                                        Intent intent = new Intent(Registration.this, Main_sportsman.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if (rol1.equals("Trainer")){

                                        //Создание таблицы запросов
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference user = database.getReference("Zapros");
                                        user
                                                .child(firebaseAuth.getUid())
                                                .setValue(" ");
                                        String uid = firebaseAuth.getUid();
                                        UList ulistik = new UList(familia1, name1, otchestvo1,uid);

                                        //Поле спортсменов
                                        mDatabaseReference1.child(firebaseAuth.getUid()).child("sportsman").setValue(" ");

                                        //Запись в саисок тренеров
                                        roli
                                                .child("trainer")
                                                .child(firebaseAuth.getUid())
                                                .setValue(ulistik);

                                        //Переход на активити
                                        Intent intent = new Intent(Registration.this, Main_trainer.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w("Failed to read value.", error.toException());
                                }   
                            });


                        }
                        else{
                            makeText(getApplicationContext(),"Данный E-mail уже зарегистрирован", LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        listeners.removeAll();
        super.onDestroy();
    }
}
