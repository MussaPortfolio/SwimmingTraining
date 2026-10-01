package com.example.swimmingtraining;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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


public class Login extends AppCompatActivity {
    EditText email,password;
    Button registerButton,loginButton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user = database.getReference("users");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.reg);
        loginButton = (Button) findViewById(R.id.vhod);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1 = email.getText().toString();
                String password1 = password.getText().toString();

                if(TextUtils.isEmpty(email1)){
                    email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    Toast.makeText(getApplicationContext(),"Пожалуйста заполните необходимые поля",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password1)){
                    password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    Toast.makeText(getApplicationContext(),"Пожалуйста заполните необходимые поля",Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email1,password1)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    openByRole();
                                }
                                else{
                                    email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                                    password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                                    Toast.makeText(getApplicationContext(),"E-mail или пароль не правильны",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        if(firebaseAuth.getCurrentUser()!=null){
            openByRole();
        }
    }

    // Читает роль один раз и открывает нужный экран
    private void openByRole() {
        FirebaseUser current = firebaseAuth.getCurrentUser();
        if (current == null) return;
        user.child(current.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String rol = dataSnapshot.child("rol").getValue(String.class);
                Intent intent;
                if ("Sportsman".equals(rol)) {
                    intent = new Intent(Login.this, Main_sportsman.class);
                } else if ("Trainer".equals(rol)) {
                    intent = new Intent(Login.this, Main_trainer.class);
                } else {
                    Toast.makeText(getApplicationContext(), "Роль пользователя не определена", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Failed to read value.", error.toException());
            }
        });
    }


    public void gost(View view) {
        Intent intent = new Intent(Login.this, GoustMenu.class);
        startActivity(intent);
    }

    public void go_admin(View view) {
        Intent intent = new Intent(Login.this, Admin_login.class);
        startActivity(intent);
    }
}