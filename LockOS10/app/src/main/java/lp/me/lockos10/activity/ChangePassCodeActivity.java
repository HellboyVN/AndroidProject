package lp.me.lockos10.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import lp.me.lockos10.R;
import lp.me.lockos10.view.TextViewRobotoLight;

public class ChangePassCodeActivity extends Activity implements OnClickListener {
    private ImageView btn0;
    private ImageView btn1;
    private ImageView btn2;
    private ImageView btn3;
    private ImageView btn4;
    private ImageView btn5;
    private ImageView btn6;
    private ImageView btn7;
    private ImageView btn8;
    private ImageView btn9;
    private TextViewRobotoLight btnCancel;
    private StringBuilder code;
    private StringBuilder code2;
    private boolean first = false;
    private ImageView imgPass;
    private TextViewRobotoLight tvTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lock_passcode);
        this.btn0 = (ImageView) findViewById(R.id.btn0);
        this.btn0.setOnClickListener(this);
        this.btn1 = (ImageView) findViewById(R.id.btn1);
        this.btn1.setOnClickListener(this);
        this.btn2 = (ImageView) findViewById(R.id.btn2);
        this.btn2.setOnClickListener(this);
        this.btn3 = (ImageView) findViewById(R.id.btn3);
        this.btn3.setOnClickListener(this);
        this.btn4 = (ImageView) findViewById(R.id.btn4);
        this.btn4.setOnClickListener(this);
        this.btn5 = (ImageView) findViewById(R.id.btn5);
        this.btn5.setOnClickListener(this);
        this.btn6 = (ImageView) findViewById(R.id.btn6);
        this.btn6.setOnClickListener(this);
        this.btn7 = (ImageView) findViewById(R.id.btn7);
        this.btn7.setOnClickListener(this);
        this.btn8 = (ImageView) findViewById(R.id.btn8);
        this.btn8.setOnClickListener(this);
        this.btn9 = (ImageView) findViewById(R.id.btn9);
        this.btn9.setOnClickListener(this);
        this.tvTitle = (TextViewRobotoLight) findViewById(R.id.tvTitle);
        this.imgPass = (ImageView) findViewById(R.id.imgPass);
        this.btnCancel = (TextViewRobotoLight) findViewById(R.id.btnCancel);
        this.btnCancel.setOnClickListener(this);
        this.code = new StringBuilder();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                if (this.code.length() > 0) {
                    this.code.deleteCharAt(this.code.length() - 1);
                    setImageCode();
                    return;
                }
                finish();
                return;
            case R.id.btn1:
                if (this.code.length() < 4) {
                    this.code.append(1);
                    setImageCode();
                    return;
                }
                return;
            case R.id.btn2:
                if (this.code.length() < 4) {
                    this.code.append(2);
                    setImageCode();
                    return;
                }
                return;
            case R.id.btn3:
                if (this.code.length() < 4) {
                    this.code.append(3);
                    setImageCode();
                    return;
                }
                return;
            case R.id.btn4:
                if (this.code.length() < 4) {
                    this.code.append(4);
                    setImageCode();
                    return;
                }
                return;
            case R.id.btn5:
                if (this.code.length() < 4) {
                    this.code.append(5);
                    setImageCode();
                    return;
                }
                return;
            case R.id.btn6:
                if (this.code.length() < 4) {
                    this.code.append(6);
                    setImageCode();
                    return;
                }
                return;
            case R.id.btn7:
                if (this.code.length() < 4) {
                    this.code.append(7);
                    setImageCode();
                    return;
                }
                return;
            case R.id.btn8:
                if (this.code.length() < 4) {
                    this.code.append(8);
                    setImageCode();
                    return;
                }
                return;
            case R.id.btn9:
                if (this.code.length() < 4) {
                    this.code.append(9);
                    setImageCode();
                    return;
                }
                return;
            case R.id.btn0:
                if (this.code.length() < 4) {
                    this.code.append(0);
                    setImageCode();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void setImageCode() {
        switch (this.code.length()) {
            case 0:
                this.imgPass.setImageResource(R.drawable.pas7);
                return;
            case 1:
                this.imgPass.setImageResource(R.drawable.pas1);
                return;
            case 2:
                this.imgPass.setImageResource(R.drawable.pas2);
                return;
            case 3:
                this.imgPass.setImageResource(R.drawable.pas3);
                return;
            case 4:
                this.imgPass.setImageResource(R.drawable.pas4);
                if (!this.first) {
                    this.code2 = this.code;
                    repeatePass();
                    this.code = new StringBuilder();
                    setImageCode();
                    return;
                } else if (this.code.toString().equals(this.code2.toString())) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", this.code.toString());
                    setResult(-1, returnIntent);
                    finish();
                    return;
                } else {
                    this.code = new StringBuilder();
                    setImageCode();
                    return;
                }
//            case 5:
//                this.imgPass.setImageResource(R.drawable.pas5);
//                return;
//            case 6:
//                this.imgPass.setImageResource(R.drawable.pas6);
//                if (!this.first) {
//                    this.code2 = this.code;
//                    repeatePass();
//                    this.code = new StringBuilder();
//                    setImageCode();
//                    return;
//                } else if (this.code.toString().equals(this.code2.toString())) {
//                    Intent returnIntent = new Intent();
//                    returnIntent.putExtra("result", this.code.toString());
//                    setResult(-1, returnIntent);
//                    finish();
//                    return;
//                } else {
//                    this.code = new StringBuilder();
//                    setImageCode();
//                    return;
//                }
            default:
                return;
        }
    }

    public void repeatePass() {
        if (this.code.length() == 4 && !this.first) {
            this.tvTitle.setText(getString(R.string.repeat_enter_passcode));
            this.first = true;
        }
    }
}
