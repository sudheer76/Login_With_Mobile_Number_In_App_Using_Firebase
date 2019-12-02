package com.hashcodes.narishakti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


/**
 Created by Sudheer on 02.12.2019.
 */

public class OTP_Screen extends AppCompatActivity {

    private EditText otpEditText;
    private Button otpNextButton;
    private FirebaseAuth firebaseAuth;
    private String verificationId;
    private ProgressBar otpProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);

        otpEditText = findViewById(R.id.otp_edit_text);
        otpNextButton = findViewById(R.id.otp_next);
        otpProgressBar = findViewById(R.id.otp_prograssbar);

        firebaseAuth=FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String mobNumber = intent.getStringExtra("moblilenumber");

        sendotp(mobNumber);

        otpNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otpProgressBar.setVisibility(View.VISIBLE);
                String code = otpEditText.getText().toString();
                if (code.isEmpty()){

                }else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    void  sendotp(String mobnum){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobnum,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId=s;
                    }
                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(),"Verification Failed",Toast.LENGTH_LONG).show();

                    }
                });        // OnVerificationStateChangedCallbacksPhoneAuthActivity.java
             }

        private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(OTP_Screen.this,LogOut.class);
                                startActivity(i);
                                finish();
                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                }
                            }
                        }
                    });
        }
}
