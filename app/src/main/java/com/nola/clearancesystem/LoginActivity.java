package com.nola.clearancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText Email,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Email=findViewById(R.id.email);
        Password=findViewById(R.id.password);
    }

    public void login(View view) {
        String email=Email.getText().toString();
        String password=Password.getText().toString();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference reference=db.collection("users").document(email);
            reference.get().addOnCompleteListener((task)->{
                if(task.isSuccessful() && Objects.equals(task.getResult().getString("type"), "Admin")){
                    if(Objects.equals(task.getResult().getString("admintype"), "Registrar")){
                        Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),RegistrarActivity.class) ;
                        startActivity(intent);
                        finish();
                    }
                   else if(Objects.equals(task.getResult().getString("admintype"), "Library")){
                        Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),LibraryActivity.class) ;
                        startActivity(intent);
                        finish();
                    }
                    else if(Objects.equals(task.getResult().getString("admintype"), "Dean of faculty")){
                        Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(), DeanOfFacultyActivity.class) ;
                        startActivity(intent);
                        finish();
                    }
                    else if(Objects.equals(task.getResult().getString("admintype"), "Student affairs")){
                        Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(), StudentAffairsActivity.class) ;
                        startActivity(intent);
                        finish();
                    }
                    else  if(Objects.equals(task.getResult().getString("admintype"), "Finance")){
                        Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),FinanceActivity.class) ;
                        startActivity(intent);
                        finish();
                    }
                }
                else if(task.isSuccessful() && Objects.equals(task.getResult().getString("type"), "Student")){
                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),DashboardActivity.class) ;
                    startActivity(intent);
                    finish();
                }
            });
        }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG ).show());

    }
    public void openRegistration(View view) {
        Intent intent=new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}