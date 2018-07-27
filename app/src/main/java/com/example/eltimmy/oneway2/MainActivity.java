package com.example.eltimmy.oneway2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private EditText email,pass;


    private DatabaseReference databaseReference;


// aywa el tomyaaaaaaaaaaaaaaaaaaaaaaaaaaa
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
            
        email=(EditText) findViewById(R.id.Email);
        pass=(EditText) findViewById(R.id.Password);

        databaseReference = database.getInstance().getReference();              // D firebase Auth,DB,Storage
        mAuth = FirebaseAuth.getInstance();


        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user!=null)
                {
                    //user signed in
                    // Toast.makeText(getApplicationContext(),"signed in",Toast.LENGTH_LONG).show();


                }
                else
                {
                    //user signed out
                    //Toast.makeText(getApplicationContext(),"signed out",Toast.LENGTH_LONG).show();
                }
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    public void Sign_In(View view) {



        String Em,Pa;
        Em=email.getText().toString();
        Pa=pass.getText().toString();
        if (!Em.equals("")&&!Pa.equals(""))
        {
            mAuth.signInWithEmailAndPassword(Em,Pa)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                //String parent;
                                final FirebaseUser user =mAuth.getCurrentUser();
                                //Toast.makeText(getApplicationContext(),"Sign in"+User.getUid(),Toast.LENGTH_LONG).show();

                                databaseReference.child("passengers").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists())
                                        {
                                            //is passenger
                                            String Name = dataSnapshot.child("Name").getValue(String.class);
                                            String Email= dataSnapshot.child("Email").getValue(String.class);
                                            String Phone= dataSnapshot.child("Phone_Number").getValue(String.class);
                                            String Password= dataSnapshot.child("Password").getValue(String.class);
                                             Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_LONG).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), MapsActivity.class));

                                            }
                                            else
                                        {
                                            //not passenger
                                            Toast.makeText(getApplicationContext(),"you are not passenger",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Sign in failed",Toast.LENGTH_LONG).show();

                            }
                        }
                    });


        }
        else
        {
            Toast.makeText(getApplicationContext(),"please fill your data",Toast.LENGTH_LONG).show();
        }
    }
    public void Sign_Up(View view) {
        finish();
        startActivity(new Intent(this,SignUp.class));
        // aywa el tomya el g7daaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa lololol
        
    }
}
