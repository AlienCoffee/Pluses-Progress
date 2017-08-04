package tk.pluses.plusesprogress.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import tk.pluses.plusesprogress.R;
import tk.pluses.plusesprogress.RegisterPage;
import tk.pluses.plusesprogress.io.RequestForm;
import tk.pluses.plusesprogress.io.RequestIO;
import tk.pluses.plusesprogress.io.RequestResult;

/**
 * Created by Андрей on 13.07.2017.
 */

public class FragmentAuth extends Fragment implements LoaderManager.LoaderCallbacks <RequestResult> {

    private Button loginButton;
    private TextView answerMessage,
                        toRegisterPage;
    private EditText phoneField,
                        passwordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton = (Button) view.findViewById (R.id.loginButton);
        loginButton.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) { auth (v); }
        });

        answerMessage  = (TextView) view.findViewById (R.id.answerMessageView);
        toRegisterPage = (TextView) view.findViewById (R.id.linkToRegisterPage);
        toRegisterPage.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                Intent intent = new Intent (v.getContext (), RegisterPage.class);
                startActivity (intent);
            }
        });
        phoneField     = (EditText) view.findViewById (R.id.phoneField);
        passwordField  = (EditText) view.findViewById (R.id.passwordField);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public Loader<RequestResult> onCreateLoader (int id, Bundle args) {
        return new RequestIO (this.getContext (), (RequestForm) args.get ("form"));
    }

    @Override
    public void onLoaderReset (Loader <RequestResult> loader) {
        loginButton.setEnabled (true);
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
        loginButton.setEnabled (true);
    }

    private void auth (View view) {
        answerMessage.setText ("");
        loginButton.setEnabled (false);

        String phone = phoneField.getText ().toString (),
                password = passwordField.getText ().toString ();

        RequestForm form = new RequestForm ("http://pluses.tk/api.users.authorizeUser");
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

        Bundle args = new Bundle ();
        args.putSerializable ("form", form);
        getLoaderManager ().restartLoader (0, args, this);
    }

}
