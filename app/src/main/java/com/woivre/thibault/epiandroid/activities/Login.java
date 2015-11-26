package com.woivre.thibault.epiandroid.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.woivre.thibault.epiandroid.R;
import com.woivre.thibault.epiandroid.objects.*;
import com.woivre.thibault.epiandroid.request.RequestManager;

public class Login extends AppCompatActivity {

    public String Login;
    public String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* REMOVE TITLE FROM APPLICATION */
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /* LOGIN FUNCTION */

    public void loginSubmit(View view)
    {
        /* TRY TO CONNECT TO API */

        EditText loginInput = (EditText)findViewById(R.id.login_input);
        EditText passwordInput = (EditText)findViewById(R.id.password_input);

        String login = loginInput.getText().toString();
        String password = passwordInput.getText().toString();

        try {
            EPIJSONObject JObj = RequestManager.LoginRequest(login, password);
            if (JObj instanceof EPIError)
            {
                Log.d("JSONOBJ", ((EPIError) JObj).error.code.toString());
                TextView loginMessages = (TextView)findViewById(R.id.login_messages);

                loginMessages.setText(R.string.wrong_creditentials);
                loginMessages.setVisibility(View.VISIBLE);
            }
            else if (JObj instanceof EPIToken) //TODO change sharedpreferences to keep login and password or create file to store data
            {
                Log.d("Token", ((EPIToken)JObj).token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
