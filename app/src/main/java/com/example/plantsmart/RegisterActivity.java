package com.example.plantsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.HashMap;
import java.util.Map;
import maes.tech.intentanim.CustomIntent;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    long maxid = 0;
    Space marginSpace;
    LinearLayout linearLayout, forLogin;
    TextView Title, enterTitle, RegistrationTitle2, CreateAccount;
    EditText nameE, emailE, passE, confirmpassE, focused;
    FrameLayout name, email, pass, confirmpass, forReg;
    ScrollView scrollView;
    ImageView back;

    //    ImageView background;
    DatabaseReference reff;
    Boolean register = false;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    boolean fail = false;
    public static final String SHARED_PREFS = "sharedPrefs";
    DocumentReference noteRef;

    public void onback(View view) {
        onBackPressed();
    }

    public void Register(View view) {
        if (register) {
            final String name = nameE.getText().toString().trim();
            final String email = emailE.getText().toString().trim();
            String password = passE.getText().toString().trim();
            String ConfinrmPassword = confirmpassE.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                nameE.setError("Name is Required.");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                emailE.setError("Email address is Required.");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passE.setError("Password is Required.");
                return;
            }

            if (password.length() < 6) {
                passE.setError("Password Must be >= 6 Characters");
                return;
            }

            if (password.compareTo(ConfinrmPassword) != 0) {
                confirmpassE.setError("Password is not similar.");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // register the user in firebase

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        reff.child(String.valueOf(maxid + 1)).child("name").setValue(name);
                        reff.child(String.valueOf(maxid + 1)).child("email").setValue(email);

                        user.put("Id", Long.toString(maxid + 1));
                        saveData(Long.toString(maxid + 1), "Id");

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                fail = false;
                                Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                                progressBar.setVisibility(View.GONE);
                                fail = true;
                            }
                        });
                        if (!fail) {
                            saveData("0","Selection");
                            startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }


            });
        } else {

            final String email = emailE.getText().toString().trim();
            String password = passE.getText().toString().trim();


            if (TextUtils.isEmpty(email)) {
                emailE.setError("Email is Required.");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passE.setError("Password is Required.");
                return;
            }

            if (password.length() < 6) {
                passE.setError("Password Must be >= 6 Characters");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // authenticate the user

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        saveId();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });
        }
    }

    public void Login(View view) {
        clearFocus();
        if (register) {
            name.setVisibility(View.GONE);
            confirmpass.setVisibility(View.GONE);
            emailE.setHint("Email address");
            passE.setHint("Password");
            enterTitle.setText("Sign in");
            enterTitle.setShadowLayer(5, 3, 3, Color.BLACK);
            linearLayout.setVisibility(View.VISIBLE);
            Title.setVisibility(View.VISIBLE);
            forReg.setVisibility(View.GONE);
            forLogin.setVisibility(View.VISIBLE);
            register = false;

        } else {
            name.setVisibility(View.VISIBLE);
            confirmpass.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            emailE.setHint("Email address (Must remember)");
            passE.setHint("Password (Must remember)");
            forReg.setVisibility(View.VISIBLE);
            forLogin.setVisibility(View.GONE);
            enterTitle.setText("Create");
            enterTitle.setShadowLayer(5, 3, 3, Color.BLACK);
            register = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniFunc();
        initBackground();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
            finish();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initBackground() {
        marginSpace = findViewById(R.id.marginSpace);
        back = findViewById(R.id.scrollView2);
        scrollView = findViewById(R.id.scrole);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) marginSpace.getLayoutParams();
        params2.height = height - dpToPx(150);
        marginSpace.setLayoutParams(params2);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                clearFocus();
                return false;
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                clearFocus();
                return false;
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void clearFocus() {
        if (focused != null) {
            focused.clearFocus();
            hideKeyboard(focused);
        }
    }

    public void iniFunc() {
        RegistrationTitle2 = findViewById(R.id.RegistrationTitle2);
        RegistrationTitle2.setShadowLayer(5, 3, 3, Color.BLACK);
        CreateAccount = findViewById(R.id.CreateAccount);
        CreateAccount.setShadowLayer(5, 3, 3, Color.BLACK);
        forLogin = findViewById(R.id.forLoginTitle);
        forReg = findViewById(R.id.forRegisTiltle);
        enterTitle = findViewById(R.id.enterTitle);
        enterTitle.setShadowLayer(5, 3, 3, Color.BLACK);
        linearLayout = findViewById(R.id.forLogin);
        name = findViewById(R.id.name);
        Title = findViewById(R.id.RegistrationTitle);
        Title.setShadowLayer(5, 3, 3, Color.BLACK);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        confirmpass = findViewById(R.id.conPass);
        nameE = findViewById(R.id.username);
        nameE.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focused = nameE;
                }
            }
        });
        emailE = findViewById(R.id.emailadress);
        emailE.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focused = emailE;
                }
            }
        });
        passE = findViewById(R.id.password);
        passE.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focused = passE;
                }
            }
        });
        confirmpassE = findViewById(R.id.confirmpassword);
        confirmpassE.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focused = confirmpassE;
                }
            }
        });
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxid = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void saveData(String data, String name) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, data);
        editor.apply();
    }

    public void saveId() {
        String userID = fAuth.getCurrentUser().getUid();
        noteRef = fStore.collection("users").document(userID);
        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(RegisterActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    FirebaseAuth.getInstance().signOut();//logout
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (documentSnapshot.exists()) {
                    String Id = documentSnapshot.getString("Id");
                    saveData(Id, "Id");
                    saveData("0","Selection");
                    Toast.makeText(RegisterActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                    finish();
                }
            }
        });
    }

    public int dpToPx(int dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        return (int) px;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "left-to-right");
    }

    @Override
    public void onBackPressed() {
        if (register) {
            Login(null);
        }
    }
}
