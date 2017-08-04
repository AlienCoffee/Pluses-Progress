package tk.pluses.plusesprogress;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import tk.pluses.plusesprogress.io.RequestForm;
import tk.pluses.plusesprogress.io.RequestIO;
import tk.pluses.plusesprogress.io.RequestResult;

public class RegisterPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks <RequestResult> {

    private Button registerButton;
    private TextView answerMessage;

    private EditText phoneField,
                        passwordField,
                        inviteCodeFiled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        registerButton = (Button) findViewById (R.id.button2);
        registerButton.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) { register (v); }
        });

        answerMessage   = (TextView) findViewById (R.id.answerMessageView);
        phoneField      = (EditText) findViewById (R.id.editText5);
        passwordField   = (EditText) findViewById (R.id.editText6);
        inviteCodeFiled = (EditText) findViewById (R.id.editText3);
    }

    @Override
    public Loader<RequestResult> onCreateLoader (int id, Bundle args) {
        return new RequestIO (this, (RequestForm) args.get ("form"));
    }

    @Override
    public void onLoaderReset (Loader <RequestResult> loader) {

    }

    @Override
    public void onLoadFinished (Loader <RequestResult> loader, RequestResult data) {
        Log.i ("RegPage", data.TYPE + " (code: " + data.CODE + ")");
        if (data.TYPE.equals ("Error")) {
            Log.e ("RegPage", "Message: " + data.getField ("name")
                                + " (comment: " + data.getField ("message") + ")");
            if (data.CODE >= 3000) {
                String message = data.getField ("message");
                answerMessage.setText (data.getField ("name")
                                        + (message != null && message.length () > 0
                                            ? ": " + message
                                            : ""));
                answerMessage.setTextColor (Color.RED);
            }
        }
        registerButton.setEnabled (true);
    }

    public void register (View view) {
        answerMessage.setText ("");
        registerButton.setEnabled (false);

        String phone = phoneField.getText ().toString (),
                password = passwordField.getText ().toString (),
                code = inviteCodeFiled.getText ().toString ();

        RequestForm form = new RequestForm ("http://pluses.tk/api.users.createUser");
        form.addParam ("token", "def");
        Log.i ("RegPage", phone);
        form.addParam ("phone", phone);
        try {
            MessageDigest digest = MessageDigest.getInstance ("MD5");

            // Adding extra salt to password
            password = "salt1" + password + "salt2";
            digest.update (password.getBytes ());

            // Converting back to string
            BigInteger text = new BigInteger (1, digest.digest ());
            password = text.toString (16);
        } catch (NoSuchAlgorithmException nsae) {
            answerMessage.setText ("Internal error. Please contact with developers");
            answerMessage.setTextColor (Color.RED);
            return;
        }
        Log.i ("RegPage", password);
        form.addParam ("password", password);
        if (code != null && code.length () > 0) {
            Log.i ("RegPage", code);
            form.addParam ("invite_code", code);
        }

        Bundle args = new Bundle ();
        args.putSerializable ("form", form);
        getSupportLoaderManager ().restartLoader (0, args, this);
    }
}
