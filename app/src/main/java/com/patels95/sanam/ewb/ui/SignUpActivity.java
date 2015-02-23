package com.patels95.sanam.ewb.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.patels95.sanam.ewb.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignUpActivity extends ActionBarActivity {

    @InjectView(R.id.firstName) EditText mFirstName;
    @InjectView(R.id.lastName) EditText mLastName;
    @InjectView(R.id.registerEmail) EditText mEmail;
    @InjectView(R.id.registerPassword) EditText mPassword;
    @InjectView(R.id.registerPassword2) EditText mPasswordAgain;
    @InjectView(R.id.submitRegister) Button mSubmitRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);


        mSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validationError = validate();
                //no error -> sign up user
                if(!validationError){
                    //Set up a progress dialog
                    final ProgressDialog dlg = new ProgressDialog(SignUpActivity.this);
                    dlg.setTitle("Please wait.");
                    dlg.setMessage("Signing Up. Please wait.");
                    dlg.show();

                    //Create a new Parse user
                    ParseUser user = new ParseUser();
                    user.setEmail(mEmail.getText().toString());
                    user.setUsername(mEmail.getText().toString());
                    user.setPassword(mPassword.getText().toString());
                    user.put("FirstName", mFirstName.getText().toString());
                    user.put("LastName", mLastName.getText().toString());

                    //Call the Parse sign up method
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            dlg.dismiss();
                            if(e != null){
                                //display error message
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            else{
                                //start the home activity
                                startHome();
                            }
                        }
                    });
            }
        }
      });
    }

    private void startHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private boolean validate() {
        boolean validationError = false;
        StringBuilder validationErrorMsg = new StringBuilder("Please ");

        if(mFirstName.getText().toString().length() == 0){
            validationError = true;
            validationErrorMsg.append("enter your first name");
        }
        if(mLastName.getText().toString().length() == 0){
            if(validationError){
                validationErrorMsg.append(", and ");
            }
            validationError = true;
            validationErrorMsg.append("enter your last name");
        }
        if(mEmail.getText().toString().length() == 0){
            if(validationError) {
                validationErrorMsg.append(", and ");
            }
                validationError = true;
                validationErrorMsg.append("enter your email");
            }
        if(mPassword.getText().toString().length() == 0){
            if(validationError) {
                validationErrorMsg.append(", and ");
            }
            validationError = true;
            validationErrorMsg.append("enter your password");
        }
        if(!mPassword.getText().toString().equals(mPasswordAgain.getText().toString())){
            if(validationError) {
                validationErrorMsg.append(", and ");
            }
            validationError = true;
            validationErrorMsg.append("passwords are not matching");
        }
        if(validationError){
            Toast.makeText(this, validationErrorMsg.toString(), Toast.LENGTH_LONG).show();
        }

        return validationError;
    }

}
