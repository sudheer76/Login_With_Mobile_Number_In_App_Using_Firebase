package com.hashcodes.narishakti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


/**
       Created by Sudheer on 02.12.2019.
  */
public class Login_Screen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText getMobNumber;
    private Button loginNextButton;
    private final static int RESOLVE_HINT = 1011;
    private String mobNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        getMobNumber =findViewById(R.id.login_get_number);
        loginNextButton =findViewById(R.id.login_next);

        loginNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userMobileNumber;

                userMobileNumber= getMobNumber.getText().toString();

                if (userMobileNumber.isEmpty()){
                    getMobNumber.setError("Enter your mobile number");

                } else if (userMobileNumber.length()<10){
                    getMobNumber.setError("Enter valid number");

                } else{
                    Intent a = new Intent(Login_Screen.this, OTP_Screen.class);
                    a.putExtra("moblilenumber","+91"+userMobileNumber );
                    startActivity(a);
                }
            }
        });

        getPhone();

    }

    private void getPhone() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) Login_Screen.this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) Login_Screen.this)
                .build();
        googleApiClient.connect();
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.auth.api.credentials.Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {

                    mobNumber = credential.getId();
                    String newString = mobNumber.replace("+91", "");
                    getMobNumber.setText(newString);


                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "err: "+connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

}





