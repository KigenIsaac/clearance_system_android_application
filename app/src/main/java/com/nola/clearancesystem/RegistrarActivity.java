package com.nola.clearancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RegistrarActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        recyclerView=findViewById(R.id.registrarrecyclerview);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        List<Student> list =new ArrayList<>();
        recyclerView.setLayoutManager(manager);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("registrarQueue")
                .get()
                        .addOnCompleteListener(task -> {
                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                list.add(new Student(documentSnapshot.getString("regno"), documentSnapshot.getString("name")));
                            }
                            recyclerView.setAdapter(new StudentAdapter(list));
                        })
                        .addOnFailureListener(e -> {

                        });


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you want to logout?");
        builder.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }
}