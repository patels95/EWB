package com.gai.ewbbu.ewb.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.model.ParseConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends ActionBarActivity {

    @BindView(R.id.firstName) EditText mFirstName;
    @BindView(R.id.lastName) EditText mLastName;
    @BindView(R.id.registerEmail) EditText mEmail;
    @BindView(R.id.registerPassword) EditText mPassword;
    @BindView(R.id.registerPassword2) EditText mPasswordAgain;
    @BindView(R.id.submitRegister) Button mSubmitRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);


        mSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validationError = validate();
                // no error -> sign up user
                if(!validationError){
                    //Set up a progress dialog
                    final ProgressDialog dlg = new ProgressDialog(SignUpActivity.this);
                    dlg.setTitle("Please wait.");
                    dlg.setMessage("Signing Up. Please wait.");
                    dlg.show();

                    ParseUser.enableRevocableSessionInBackground();
                    // create a new Parse user
                    ParseUser user = new ParseUser();
                    user.setEmail(mEmail.getText().toString());
                    user.setUsername(mEmail.getText().toString());
                    user.setPassword(mPassword.getText().toString());
                    user.put(ParseConstants.FIRST_NAME, mFirstName.getText().toString());
                    user.put(ParseConstants.LAST_NAME, mLastName.getText().toString());

                    // set user's role
                    if (mEmail.getText().toString().equals("patels95@bu.edu") ||
                            mEmail.getText().toString().equals("ewb@bu.edu")) {
                        user.put(ParseConstants.ADMIN, true);
                    }
                    else {
                        user.put(ParseConstants.ADMIN, false);
                    }

                    // call the Parse sign up method
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startHome() {
        Intent intent = new Intent(this, HomeOldActivity.class);
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
