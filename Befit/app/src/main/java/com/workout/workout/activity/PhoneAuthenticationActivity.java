package com.workout.workout.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import com.hbb20.CountryCodePicker;
import com.workout.workout.R;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.managers.UserManager;
import com.workout.workout.modal.User;
import com.workout.workout.util.AppUtil;
import java.util.concurrent.TimeUnit;

public class PhoneAuthenticationActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = PhoneAuthenticationActivity.class.getName();
    private Button buttonResendCode;
    private Button buttonSignInPhoneNumber;
    private Button buttonVerifyCode;
    private CountryCodePicker countryCodePicker;
    private EditText editTextCode;
    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private LinearLayout linearLayoutPhoneNumber;
    private FirebaseAuth mAuth;
    private OnVerificationStateChangedCallbacks mCallbacks;
    private ForceResendingToken mResendToken;
    String mVerificationId;
    private ProgressBar progressBar;
    private TextInputLayout textInputLayoutCode;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutPhoneNumber;
    private TextView textViewEditPhoneNumber;
    private TextView textViewMessage;
    private TextView textViewMessageNumber;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_phone_authentication);
        initializeView();
    }

    private void initializeView() {
        this.textInputLayoutPhoneNumber = (TextInputLayout) findViewById(R.id.textInputLayoutPhoneNumber);
        this.editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        this.buttonSignInPhoneNumber = (Button) findViewById(R.id.buttonSignInPhoneNumber);
        this.textViewEditPhoneNumber = (TextView) findViewById(R.id.textViewEditPhoneNumber);
        this.textViewMessage = (TextView) findViewById(R.id.textViewMessage);
        this.buttonVerifyCode = (Button) findViewById(R.id.buttonVerifyCode);
        this.textInputLayoutCode = (TextInputLayout) findViewById(R.id.textInputLayoutCode);
        this.editTextCode = (EditText) findViewById(R.id.editTextCode);
        this.linearLayoutPhoneNumber = (LinearLayout) findViewById(R.id.linearLayoutPhoneNumber);
        this.textViewMessageNumber = (TextView) findViewById(R.id.textViewMessageNumber);
        this.buttonResendCode = (Button) findViewById(R.id.buttonResendCode);
        this.countryCodePicker = (CountryCodePicker) findViewById(R.id.countryCodePicker);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        this.editTextName = (EditText) findViewById(R.id.editTextName);
        this.buttonSignInPhoneNumber.setOnClickListener(this);
        this.buttonVerifyCode.setOnClickListener(this);
        this.textViewEditPhoneNumber.setOnClickListener(this);
        this.buttonResendCode.setOnClickListener(this);
        showNumberLayouts();
        hideCodeLayouts();
        this.mAuth = FirebaseAuth.getInstance();
        this.mCallbacks = new OnVerificationStateChangedCallbacks() {
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.w(PhoneAuthenticationActivity.TAG, "onVerificationCompleted:" + credential);
                PhoneAuthenticationActivity.this.signInWithPhoneAuthCredential(credential);
            }

            public void onVerificationFailed(FirebaseException e) {
                Log.w(PhoneAuthenticationActivity.TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    PhoneAuthenticationActivity.this.textInputLayoutPhoneNumber.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(PhoneAuthenticationActivity.this.findViewById(16908290), (CharSequence) "Quota exceeded.", -Toast.LENGTH_LONG).show();
                }
            }

            public void onCodeSent(String verificationId, ForceResendingToken token) {
                Log.w(PhoneAuthenticationActivity.TAG, "onCodeSent:" + verificationId);
                PhoneAuthenticationActivity.this.mVerificationId = verificationId;
                PhoneAuthenticationActivity.this.mResendToken = token;
                PhoneAuthenticationActivity.this.progressBar.setVisibility(View.GONE);
            }
        };
    }

    private String getCompleteMobileNumber() {
        String completePhoneNumber = this.countryCodePicker.getSelectedCountryCodeWithPlus().concat(this.editTextPhoneNumber.getText().toString());
        Log.w(TAG, "Complete mobile number = " + completePhoneNumber);
        return completePhoneNumber;
    }

    private void hideNumberLayouts() {
        this.textInputLayoutPhoneNumber.setVisibility(View.GONE);
        this.editTextPhoneNumber.setVisibility(View.GONE);
        this.buttonSignInPhoneNumber.setVisibility(View.GONE);
        this.linearLayoutPhoneNumber.setVisibility(View.GONE);
        this.textInputLayoutName.setVisibility(View.GONE);
        this.editTextName.setVisibility(View.GONE);
    }

    private void showNumberLayouts() {
        this.textInputLayoutPhoneNumber.setVisibility(View.VISIBLE);
        this.editTextPhoneNumber.setVisibility(View.VISIBLE);
        this.buttonSignInPhoneNumber.setVisibility(View.VISIBLE);
        this.linearLayoutPhoneNumber.setVisibility(View.VISIBLE);
        this.textInputLayoutName.setVisibility(View.VISIBLE);
        this.editTextName.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.GONE);
    }

    private void showCodeLayouts() {
        this.textInputLayoutCode.setVisibility(View.VISIBLE);
        this.editTextCode.setVisibility(View.VISIBLE);
        this.textViewEditPhoneNumber.setVisibility(View.VISIBLE);
        this.textViewMessage.setVisibility(View.VISIBLE);
        this.buttonVerifyCode.setVisibility(View.VISIBLE);
        this.textViewMessageNumber.setVisibility(View.VISIBLE);
        this.buttonResendCode.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.textViewMessageNumber.setText(getCompleteMobileNumber());
    }

    private void hideCodeLayouts() {
        this.textInputLayoutCode.setVisibility(View.GONE);
        this.editTextCode.setVisibility(View.GONE);
        this.textViewEditPhoneNumber.setVisibility(View.GONE);
        this.textViewMessage.setVisibility(View.GONE);
        this.buttonVerifyCode.setVisibility(View.GONE);
        this.textViewMessageNumber.setVisibility(View.GONE);
        this.buttonResendCode.setVisibility(View.GONE);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignInPhoneNumber:
                handleSignInPhoneNumber();
                return;
            case R.id.buttonVerifyCode:
                handleVerifyCode();
                return;
            case R.id.textViewEditPhoneNumber:
                handleEditPhoneNumber();
                return;
            case R.id.buttonResendCode:
                handleResendCode();
                return;
            default:
                return;
        }
    }

    private void handleVerifyCode() {
        String code = this.editTextCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            this.textInputLayoutCode.setError("Code cannot be empty");
        } else if (!AppUtil.isEmpty(this.mVerificationId)) {
            verifyPhoneNumberWithCode(this.mVerificationId, code);
        }
    }

    private void handleSignInPhoneNumber() {
        String name = this.editTextName.getText().toString();
        String phoneNumber = this.editTextPhoneNumber.getText().toString();
        String completePhoneNumber = getCompleteMobileNumber();
        if (AppUtil.isEmpty(name)) {
            this.textInputLayoutName.setError("Enter name");
        } else if (AppUtil.isEmpty(this.editTextPhoneNumber.getText().toString())) {
            this.textInputLayoutPhoneNumber.setError("Enter phone number");
            this.textInputLayoutName.setError(null);
        } else if (phoneNumber.length() < 4) {
            this.textInputLayoutPhoneNumber.setError("Invalid phone number");
            this.textInputLayoutName.setError(null);
        } else {
            this.textInputLayoutPhoneNumber.setError(null);
            this.textInputLayoutName.setError(null);
            hideNumberLayouts();
            showCodeLayouts();
            startPhoneNumberVerification(completePhoneNumber);
        }
    }

    private void handleEditPhoneNumber() {
        hideCodeLayouts();
        showNumberLayouts();
    }

    private void handleResendCode() {
        resendVerificationCode(getCompleteMobileNumber(), this.mResendToken);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        final String name = this.editTextName.getText().toString();
        String receivedSMSCode = credential.getSmsCode();
        this.editTextCode.setText(receivedSMSCode);
        Log.w(TAG, "SMS Code Received : " + receivedSMSCode);
        this.mAuth.signInWithCredential(credential).addOnCompleteListener((Activity) this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.w(PhoneAuthenticationActivity.TAG, "signInWithCredential:success");
                    FirebaseUser user = ((AuthResult) task.getResult()).getUser();
                    Uri profileUrl = user.getPhotoUrl();
                    String email = user.getEmail();
                    String uid = user.getUid();
                    String profilePicUrl = "";
                    String phoneNumber = user.getPhoneNumber();
                    String providerId = (String) user.getProviders().get(0);
                    PersistenceManager.setDisplayName(name);
                    PersistenceManager.setEmail(email);
                    PersistenceManager.setUserUid(uid);
                    if (profileUrl != null) {
                        profilePicUrl = profileUrl.toString();
                    }
                    PersistenceManager.setPhotoUrl(profilePicUrl);
                    PersistenceManager.setProviderId(providerId);
                    PersistenceManager.setPhoneNumber(phoneNumber);
                    PersistenceManager.setUserSignedIn(true);
                    Toast.makeText(PhoneAuthenticationActivity.this, "Successfully Signed in", Toast.LENGTH_SHORT).show();
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
                            PhoneAuthenticationActivity.this.finish();
                        }
                    });
                    return;
                }
                Log.w(PhoneAuthenticationActivity.TAG, "signInWithCredential:failure", task.getException());
                Toast.makeText(PhoneAuthenticationActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    PhoneAuthenticationActivity.this.textInputLayoutCode.setError("Invalid code.");
                }
            }
        });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, (Activity) this, this.mCallbacks);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationId, code));
    }

    private void resendVerificationCode(String phoneNumber, ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, (Activity) this, this.mCallbacks, token);
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return true;
        }
        this.textInputLayoutPhoneNumber.setError("Invalid phone number");
        return false;
    }

    public void onStart() {
        super.onStart();
        if (this.mAuth.getCurrentUser() == null) {
        }
    }
}
