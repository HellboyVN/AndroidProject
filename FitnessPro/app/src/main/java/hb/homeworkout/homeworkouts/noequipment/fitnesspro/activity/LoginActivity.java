package hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = LoginActivity.class.getName();
    private Button buttonLogin;
    private Button buttonSignUp;
    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextPassword;
    private ProgressBar progressBar;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login);
        initializeView();
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
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

    }

    private void signUp(final String name, String email, String password) {
    }
}
