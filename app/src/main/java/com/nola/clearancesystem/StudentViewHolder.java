package com.nola.clearancesystem;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StudentViewHolder extends RecyclerView.ViewHolder {
    private final OnDataChangeListener onDataChangeListener = new OnDataChangeListener() {
        @Override
        public void onDataChanged() {

        }
    };
    TextView myRegno, myName;
    Button btn;
    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);
        myName=itemView.findViewById(R.id.name);
        myRegno=itemView.findViewById(R.id.regno);
        btn = itemView.findViewById(R.id.button);
    }
    public void bind(Student student){
        myRegno.setText(student.getRegno());
        myName.setText(student.getName());
        btn.setOnClickListener(v -> {
            processClearance();
        });
    }
    public void processClearance() {
        Context context = itemView.getContext();
        HashMap<String, String> map = new HashMap<>();
        map.put("name", myName.getText().toString());
        map.put("regno", myRegno.getText().toString());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("deanOfFacultyQueue").add(map).addOnSuccessListener(documentReference -> {
            db.collection("users").get().addOnCompleteListener(task -> {
                for(DocumentSnapshot doc : task.getResult()){
                    if(Objects.equals(doc.getString("regno"), myRegno.getText().toString())){
                        Long l = doc.getLong("clearanceProgress");
                        Log.d("TAG", "processClearance: " + l);
                        if(l == 0){
                            db.collection("users").document(Objects.requireNonNull(doc.getString("email"))).update("clearanceProgress", 20)
                                    .addOnSuccessListener(unused -> {
                                        db.collection("deanOfFacultyQueue")
                                                .whereEqualTo("regno", myRegno.getText().toString())
                                                .get()
                                                .addOnCompleteListener(task12 -> {
                                                    if(task12.isSuccessful()){
                                                        for(DocumentSnapshot documentSnapshot: task12.getResult()){
                                                            db.collection("deanOfFacultyQueue").document(documentSnapshot.getId()).delete().addOnSuccessListener(unused1 -> db.collection("libraryQueue").add(map).addOnCompleteListener(task1 -> {
                                                                onDataChangeListener.onDataChanged();
                                                                Toast.makeText(context, "Student cleared successfully.", Toast.LENGTH_LONG).show();
                                                            }));
                                                        }
                                                    }
                                                });
                                    });
                        }
                        else if(l == 20){
                            db.collection("users").document(Objects.requireNonNull(doc.getString("email"))).update("clearanceProgress", 40)
                                    .addOnSuccessListener(unused -> {
                                        db.collection("libraryQueue")
                                                .whereEqualTo("regno", myRegno.getText().toString())
                                                .get()
                                                .addOnCompleteListener(task12 -> {
                                                    if(task12.isSuccessful()){
                                                        for(DocumentSnapshot documentSnapshot: task12.getResult()){
                                                            db.collection("libraryQueue").document(documentSnapshot.getId()).delete().addOnSuccessListener(unused1 -> db.collection("studentAffairsQueue").add(map).addOnCompleteListener(task1 -> {
                                                                onDataChangeListener.onDataChanged();
                                                                Toast.makeText(context, "Student cleared successfully.", Toast.LENGTH_LONG).show();
                                                            }));
                                                        }
                                                    }
                                                });
                                    });
                        }
                        else if(l == 40){
                            db.collection("users").document(Objects.requireNonNull(doc.getString("email"))).update("clearanceProgress", 60)
                                    .addOnSuccessListener(unused -> {
                                        db.collection("studentAffairsQueue")
                                                .whereEqualTo("regno", myRegno.getText().toString())
                                                .get()
                                                .addOnCompleteListener(task12 -> {
                                                    if(task12.isSuccessful()){
                                                        for(DocumentSnapshot documentSnapshot: task12.getResult()){
                                                            db.collection("studentAffairsQueue").document(documentSnapshot.getId()).delete().addOnSuccessListener(unused1 -> db.collection("financeQueue").add(map).addOnCompleteListener(task1 -> {
                                                                onDataChangeListener.onDataChanged();
                                                                Toast.makeText(context, "Student cleared successfully.", Toast.LENGTH_LONG).show();
                                                            }));
                                                        }
                                                    }
                                                });
                                    });
                        }
                        else if(l == 60){
                            db.collection("users").document(Objects.requireNonNull(doc.getString("email"))).update("clearanceProgress", 80)
                                    .addOnSuccessListener(unused -> {
                                        db.collection("financeQueue")
                                                .whereEqualTo("regno", myRegno.getText().toString())
                                                .get()
                                                .addOnCompleteListener(task12 -> {
                                                    if(task12.isSuccessful()){
                                                        for(DocumentSnapshot documentSnapshot: task12.getResult()){
                                                            db.collection("financeQueue").document(documentSnapshot.getId()).delete().addOnSuccessListener(unused1 -> db.collection("registrarQueue").add(map).addOnCompleteListener(task1 -> {
                                                                onDataChangeListener.onDataChanged();
                                                                Toast.makeText(context, "Student cleared successfully.", Toast.LENGTH_LONG).show();
                                                            }));
                                                        }
                                                    }
                                                });
                                    });
                        }
                        else if(l == 80){
                            db.collection("users").document(Objects.requireNonNull(doc.getString("email"))).update("clearanceProgress", 100)
                                    .addOnSuccessListener(unused -> {
                                        db.collection("registrarQueue")
                                                .whereEqualTo("regno", myRegno.getText().toString())
                                                .get()
                                                .addOnCompleteListener(task12 -> {
                                                    if(task12.isSuccessful()){
                                                        for(DocumentSnapshot documentSnapshot: task12.getResult()){
                                                            db.collection("registrarQueue").document(documentSnapshot.getId()).delete().addOnCompleteListener(task13 -> {
                                                                db.collection("users").document(Objects.requireNonNull(doc.getString("email"))).update("clearanceStatus", "cleared");
                                                                onDataChangeListener.onDataChanged();
                                                                Toast.makeText(context, "Student cleared successfully.", Toast.LENGTH_LONG).show();
                                                            });
                                                        }
                                                    }
                                                });
                                    });
                        }
                    }
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }
}
