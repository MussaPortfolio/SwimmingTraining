package com.example.swimmingtraining;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_login extends AppCompatActivity {

    EditText login, passw;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        login = findViewById(R.id.lo);
        passw = findViewById(R.id.pa);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    // Вход через Firebase Auth; права администратора проверяются по узлу admins/{uid}
    public void go(View view) {
        String email = login.getText().toString().trim();
        String password = passw.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Пожалуйста заполните необходимые поля", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkAdmin();
                        } else {
                            Toast.makeText(getApplicationContext(), "Неверный логин или пароль.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkAdmin() {
        FirebaseUser current = firebaseAuth.getCurrentUser();
        if (current == null) return;
        FirebaseDatabase.getInstance().getReference("admins").child(current.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (Boolean.TRUE.equals(dataSnapshot.getValue(Boolean.class))) {
                            startActivity(new Intent(Admin_login.this, AdminPanel.class));
                        } else {
                            firebaseAuth.signOut();
                            Toast.makeText(getApplicationContext(), "Нет прав администратора.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        firebaseAuth.signOut();
                        Log.w("Failed to read value.", error.toException());
                        Toast.makeText(getApplicationContext(), "Нет прав администратора.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
