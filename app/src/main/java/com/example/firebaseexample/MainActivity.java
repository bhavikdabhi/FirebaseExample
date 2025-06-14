package com.example.firebaseexample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
private EditText et,et2;
private Button btn,btnread,btnUpdate;
private TextView  textv,textv2;

private DatabaseReference rootDatabaseref;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        et=findViewById(R.id.input);
        et2=findViewById(R.id.input2);
        btn=findViewById(R.id.btn);
        btnread = findViewById(R.id.btnRea);
        btnUpdate = findViewById(R.id.btnupdate);
        textv = findViewById(R.id.txtr);
        textv2 = findViewById(R.id.txtr2);

        rootDatabaseref= FirebaseDatabase.getInstance().getReference().child("User");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hashmap = new HashMap();

                String idStr = et.getText().toString().trim();
                String name = et2.getText().toString();

                if (idStr.isEmpty() || name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Both ID and Name are required", Toast.LENGTH_SHORT).show();
                    return;              }
                int id;
                try {
                    id = Integer.parseInt(idStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid ID format", Toast.LENGTH_SHORT).show();
                    return;
                }
                hashmap.put("ID",id);
                hashmap.put("Name",name);
                rootDatabaseref.child("User1").setValue(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this,"Data add successfully",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Fail to Insert Data",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootDatabaseref.child("User1").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        
                        if (snapshot.exists()) {
                            Map<String,Object> map = (Map<String, Object>) snapshot.getValue();
                            Object id = map.get("ID");
                            String name =(String) map.get("Name");
                            textv.setText(""+id);
                            textv2.setText(name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
btnUpdate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        HashMap hashmap = new HashMap();

        String idStr = et.getText().toString().trim();
        String name = et2.getText().toString().trim();

        if (idStr.isEmpty() && name.isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter ID or Name to update", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                hashmap.put("ID", id);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Invalid ID format", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!name.isEmpty()) {
            hashmap.put("Name", name);
        }

        rootDatabaseref.child("User1").updateChildren(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this,"Data update successfully",Toast.LENGTH_LONG).show();
            }
        });
    }
});
    }
}