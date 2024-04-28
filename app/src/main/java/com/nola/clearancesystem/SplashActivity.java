package com.nola.clearancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler=new Handler();
        handler.postDelayed(() -> {
            FirebaseAuth auth=FirebaseAuth.getInstance();
            FirebaseUser user=auth.getCurrentUser();
            if(user!=null){
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference reference=db.collection("users").document(Objects.requireNonNull(user.getEmail()));
                reference.get().addOnCompleteListener((task)->{
                if(task.isSuccessful() && Objects.equals(task.getResult().getString("type"), "Admin")){
                    if(Objects.equals(task.getResult().getString("admintype"), "Registrar")){
                        Intent intent=new Intent(getApplicationContext(),RegistrarActivity.class) ;
                        startActivity(intent);
                        finish();
                    }
                    else if(Objects.equals(task.getResult().getString("admintype"), "library")){
                        Intent intent=new Intent(getApplicationContext(),LibraryActivity.class) ;
                        startActivity(intent);
                        finish();
                    }
                    else if(Objects.equals(task.getResult().getString("admintype"), "Dean of faculty")){
                        Intent intent=new Intent(getApplicationContext(), DeanOfFacultyActivity.class) ;
                        startActivity(intent);
                        finish();
                    }
                    else if(Objects.equals(task.getResult().getString("admintype"), "Student affairs")){
                        Intent intent=new Intent(getApplicationContext(), StudentAffairsActivity.class) ;
                        startActivity(intent);
                        finish();
                    }
                    else  if(Objects.equals(task.getResult().getString("admintype"), "Finance")){
                        Intent intent=new Intent(getApplicationContext(),FinanceActivity.class) ;
                        startActivity(intent);
                        finish();
                    }
                }
                else if(task.isSuccessful() && Objects.equals(task.getResult().getString("type"), "Student")){
                    Intent intent=new Intent(getApplicationContext(),DashboardActivity.class) ;
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG ).show());

            }
            else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }


        },2000);

    }
}