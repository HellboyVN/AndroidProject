package com.workout.workout.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.workout.workout.R;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.managers.UserManager;
import com.workout.workout.modal.User;
import com.workout.workout.util.AppUtil;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = LoginActivity.class.getName();
    private Button buttonLogin;
    private Button buttonSignUp;
    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextPassword;
    private FirebaseAuth mAuth;
    private AuthStateListener mAuthListener;
    private ProgressBar progressBar;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login);
        this.mAuth = FirebaseAuth.getInstance();
        this.mAuthListener = new AuthStateListener() {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(LoginActivity.TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    PersistenceManager.setDisplayName(user.getDisplayName());
                    PersistenceManager.setEmail(user.getEmail());
                    PersistenceManager.setUserUid(user.getUid());
                    if (user.getPhotoUrl() != null) {
                        PersistenceManager.setPhotoUrl(user.getPhotoUrl().toString());
                    }
                    PersistenceManager.setUserSignedIn(true);
                    User localUser = new User("","","","","","","","");
                    localUser.setDisplay_name(user.getDisplayName());
                    localUser.setEmail(user.getEmail());
                    localUser.setIdentifier("");
                    localUser.setMobile_number(PersistenceManager.getPhoneNumber());
                    localUser.setUser_uid(user.getUid());
                    localUser.setPhoto_url(PersistenceManager.getPhotoUrl());
                    UserManager.createNewUserInFirebaseDB(localUser);
                    return;
                }
                Log.d(LoginActivity.TAG, "onAuthStateChanged:signed_out");
            }
        };
        initializeView();
    }

    public void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.mAuthListener);
    }

    public void onStop() {
        super.onStop();
        if (this.mAuthListener != null) {
            this.mAuth.removeAuthStateListener(this.mAuthListener);
        }
    }

    private void initializeView() {
        this.textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        this.textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        this.editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        this.editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        this.buttonLogin = (Button) findViewById(R.id.buttonLogin);
        this.buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        this.editTextName = (EditText) findViewById(R.id.editTextName);
        this.buttonLogin.setOnClickListener(this);
        this.buttonSignUp.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                handleLogin();
                return;
            case R.id.buttonSignUp:
                handleSignUp();
                return;
            default:
                return;
        }
    }

    private void handleLogin() {
        String name = this.editTextName.getText().toString();
        String email = this.editTextEmail.getText().toString();
        String password = this.editTextPassword.getText().toString();
        if (AppUtil.isEmpty(name)) {
            this.textInputLayoutName.setError("Enter name");
        } else if (AppUtil.isEmpty(email)) {
            this.textInputLayoutEmail.setError("Enter email");
        } else if (!AppUtil.isEmailValid(email)) {
            this.textInputLayoutEmail.setError("Enter valid email");
        } else if (AppUtil.isEmpty(password)) {
            this.textInputLayoutEmail.setError(null);
            this.textInputLayoutPassword.setError("Enter password");
        } else if (password.length() < 6) {
            this.textInputLayoutPassword.setError("Password must contains atleast 6 character/number");
        } else {
            this.textInputLayoutEmail.setError(null);
            this.textInputLayoutPassword.setError(null);
            this.progressBar.setVisibility(View.VISIBLE);
            signIn(name, email, password);
        }
    }

    private void handleSignUp() {
        String name = this.editTextName.getText().toString();
        String email = this.editTextEmail.getText().toString();
        String password = this.editTextPassword.getText().toString();
        if (AppUtil.isEmpty(name)) {
            this.textInputLayoutName.setError("Enter name");
        } else if (AppUtil.isEmpty(email)) {
            this.textInputLayoutName.setError(null);
            this.textInputLayoutEmail.setError("Enter Email");
        } else if (!AppUtil.isEmailValid(email)) {
            this.textInputLayoutEmail.setError("Enter valid email");
        } else if (AppUtil.isEmpty(password)) {
            this.textInputLayoutEmail.setError(null);
            this.textInputLayoutPassword.setError("Enter password");
        } else if (password.length() < 6) {
            this.textInputLayoutPassword.setError("Password must contains atleast 6 character/number");
        } else {
            this.textInputLayoutName.setError(null);
            this.textInputLayoutEmail.setError(null);
            this.textInputLayoutPassword.setError(null);
            this.progressBar.setVisibility(View.VISIBLE);
            signUp(name, email, password);
        }
    }

    private void signIn(final String name, String email, String password) {
        this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = LoginActivity.this.mAuth.getCurrentUser();
                    Uri profileUrl = user.getPhotoUrl();
                    String email = user.getEmail();
                    String uid = user.getUid();
                    String phoneNumber = user.getPhoneNumber();
                    String profilePicUrl = "";
                    if (profileUrl != null) {
                        profilePicUrl = profileUrl.toString();
                    }
                    String providerId = (String) user.getProviders().get(0);
                    PersistenceManager.setDisplayName(name);
                    PersistenceManager.setEmail(email);
                    PersistenceManager.setUserUid(uid);
                    PersistenceManager.setPhotoUrl(profilePicUrl);
                    PersistenceManager.setProviderId(providerId);
                    PersistenceManager.setPhoneNumber(phoneNumber);
                    PersistenceManager.setUserSignedIn(true);
                    LoginActivity.this.progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                    final User localUser = new User("","","","","","","","");
                    localUser.setDisplay_name(name);
                    localUser.setEmail(email);
                    localUser.setIdentifier("");
                    localUser.setMobile_number(phoneNumber);
                    localUser.setUser_uid(uid);
                    localUser.setProvider_id(providerId);
                    localUser.setPhoto_url(profilePicUrl);
                    user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                        public void onSuccess(GetTokenResult result) {
                            localUser.setToken(result.getToken());
                            UserManager.createNewUserInFirebaseDB(localUser);
                            LoginActivity.this.finish();
                        }
                    });
                    return;
                }
                Toast.makeText(LoginActivity.this, "Authentication failed.\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                LoginActivity.this.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void signUp(final String name, String email, String password) {
        this.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(LoginActivity.TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
                    FirebaseUser user = LoginActivity.this.mAuth.getCurrentUser();
                    Uri photoUrl = user.getPhotoUrl();
                    PersistenceManager.setDisplayName(name);
                    PersistenceManager.setEmail(user.getEmail());
                    PersistenceManager.setUserUid(user.getUid());
                    if (photoUrl != null) {
                        PersistenceManager.setPhotoUrl(photoUrl.toString());
                    }
                    PersistenceManager.setProviderId((String) user.getProviders().get(0));
                    PersistenceManager.setPhoneNumber(user.getPhoneNumber());
                    PersistenceManager.setUserSignedIn(true);
                    LoginActivity.this.progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Sign up successful.", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                    return;
                }
                Toast.makeText(LoginActivity.this, "Authentication failed.\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                LoginActivity.this.progressBar.setVisibility(View.GONE);
            }
        });
    }
}
