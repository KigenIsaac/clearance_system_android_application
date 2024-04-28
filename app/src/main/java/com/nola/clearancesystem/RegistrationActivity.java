package com.nola.clearancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    EditText name, regno, email, password;
    Spinner type_of_user, type_of_admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name = findViewById(R.id.name);
        regno = findViewById(R.id.regno);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        type_of_user=findViewById(R.id.typeofuser);
        type_of_admin=findViewById(R.id.typeofadmin);
        type_of_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(type_of_user.getSelectedItem().toString().equals("Student")){
                    type_of_admin.setVisibility(View.GONE);
                    regno.setVisibility(View.VISIBLE);
                }
                else {
                    type_of_admin.setVisibility(View.VISIBLE);
                    regno.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void openLogin(View view) {
        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void register(View view) {
        String enteredName = name.getText().toString();
        String enteredEmail = email.getText().toString();
        String enteredPassword = password.getText().toString();
        String enteredRegNo = regno.getText().toString();
        String enteredusertype=type_of_user.getSelectedItem().toString();
        String enteredadminuser=type_of_admin.getSelectedItem().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(enteredEmail, enteredPassword).addOnSuccessListener(authResult -> {
            if(enteredusertype.equals("Admin")){
                HashMap<String, Object> data = new HashMap<>();
                data.put("name", enteredName);
                data.put("email", enteredEmail);
                data.put("admintype", enteredadminuser);
                data.put("type", enteredusertype);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference reference = db.collection("users").document(enteredEmail);
                reference.set(data);
            }
            else{
                HashMap<String, Object> data = new HashMap<>();
                data.put("name", enteredName);
                data.put("email", enteredEmail);
                data.put("regno", enteredRegNo);
                data.put("type", enteredusertype);
                data.put("clearanceStatus", "not requested");
                data.put("clearanceProgress", 0);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference reference = db.collection("users").document(enteredEmail);
                reference.set(data);
            }

            Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
    }
}