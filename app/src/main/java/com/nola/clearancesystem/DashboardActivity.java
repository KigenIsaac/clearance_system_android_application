package com.nola.clearancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DashboardActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        progress = findViewById(R.id.progress);
        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this);
        builder.setMessage("Do you want to logout");
        builder.setPositiveButton("Logout", (dialog, which) -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            FirebaseAuth.getInstance().signOut();
        });
        builder.setNegativeButton("Exit", (dialog, which) -> {
            super.onBackPressed();
        });
        builder.show();
    }

    public void requestClearance(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                if(Objects.equals(doc.getString("email"), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())){
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", doc.getString("name"));
                    map.put("regno", doc.getString("regno"));
                    if(Objects.equals(doc.getString("clearanceStatus"), "requested")){
                        Toast.makeText(this, "Clearance can only be requested once", Toast.LENGTH_SHORT).show();
                    }
                    else if(Objects.equals(doc.getString("clearanceStatus"), "not requested")){
                        db.collection("deanOfFacultyQueue").add(map).addOnCompleteListener(documentReference -> {
                            db.collection("users").document(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())).update("clearanceStatus", "requested");
                            Toast.makeText(this, "Clearance requested successfully", Toast.LENGTH_LONG).show();
                        });
                    }
                    else if(Objects.equals(doc.getString("clearanceStatus"), "cleared")){
                        Toast.makeText(this, "You have already been cleared", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void showProgress(View view) {
        Button button = findViewById(R.id.mybtn);
        ProgressBar progressBar = findViewById(R.id.progressbar);
        TextView textView = findViewById(R.id.progress);
        if(progressBar.getVisibility() == View.VISIBLE && textView.getVisibility() == View.VISIBLE){
            button.setText("Show progress");
            textView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
        else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Long l = task.getResult().getLong("clearanceProgress");
                    progressBar.setProgress(Math.toIntExact(l));
                    progress.setText(l + "%");
                }
            });
            button.setText("Hide progress");
            textView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}