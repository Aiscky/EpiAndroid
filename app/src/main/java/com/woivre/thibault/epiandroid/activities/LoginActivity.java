package com.woivre.thibault.epiandroid.activities;

import android.content.Context;
import android.content.Intent;
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
import com.woivre.thibault.epiandroid.request.EPINetworkException;
import com.woivre.thibault.epiandroid.request.IUpdateViewOnPostExecute;
import com.woivre.thibault.epiandroid.request.RequestManager;
import com.woivre.thibault.epiandroid.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    public String Login;
    public String Password;
    public String Token;

    public static final String TOKEN = "TOKEN";
    public static final String LOGIN = "LOGIN";
    public static final String PASSWORD = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* REMOVE TITLE FROM APPLICATION */

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
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
        TextView loginMessages = (TextView)findViewById(R.id.login_messages);
        loginMessages.setVisibility(View.GONE);

        /* TRY TO CONNECT TO API */

        EditText loginInput = (EditText)findViewById(R.id.login_input);
        EditText passwordInput = (EditText)findViewById(R.id.password_input);

        this.Login = "woivre_t";
        this.Password = "1lEJtLxG";

        /*String login = loginInput.getText().toString();
        String password = passwordInput.getText().toString();*/

        try
        {
            RequestManager.LoginRequest(this.Login, this.Password, new UpdateViewLogIn(this));
        } catch (EPINetworkException e) {
            loginMessages.setText(R.string.not_connected);
            loginMessages.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            loginMessages.setText(R.string.app_error);
            loginMessages.setVisibility(View.VISIBLE);
        }
    }

    /* UpdateView OnPostExecute */

    public class UpdateViewLogIn implements IUpdateViewOnPostExecute
    {
        private Context mContext;

        public UpdateViewLogIn(Context context)
        {
            mContext = context;
        }

        @Override
        public void UpdateView(EPIJSONObject[] objs) {
            TextView loginMessages = (TextView)findViewById(R.id.login_messages);

            if (objs.length != 0 && objs[0] instanceof EPIError)
            {
                loginMessages.setText(R.string.wrong_creditentials);
                loginMessages.setVisibility(View.VISIBLE);
            }
            else if (objs[0] instanceof EPIToken)
            {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra(TOKEN, ((EPIToken)objs[0]).token);
                intent.putExtra(LOGIN, Login);
                intent.putExtra(PASSWORD, Password);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}
