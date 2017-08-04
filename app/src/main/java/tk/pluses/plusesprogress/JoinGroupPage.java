package tk.pluses.plusesprogress;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import tk.pluses.plusesprogress.io.RequestForm;
import tk.pluses.plusesprogress.io.RequestIO;
import tk.pluses.plusesprogress.io.RequestResult;
import tk.pluses.plusesprogress.user.UserEntity;

public class JoinGroupPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks <RequestResult> {

    private EditText inviteCode;
    private Button joinButton;
    private TextView answerMessage;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_join_group_page);

        inviteCode    = (EditText) findViewById (R.id.inviteCodeField);
        joinButton    = (Button) findViewById (R.id.joinGroupButton);
        joinButton.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) { _join (v); }
        });
        answerMessage = (TextView) findViewById (R.id.answerMessageView);
    }

    @Override
    public Loader<RequestResult> onCreateLoader (int id, Bundle args) {
        return new RequestIO (this.getBaseContext (), (RequestForm) args.get ("form"));
    }

    @Override
    public void onLoadFinished (Loader <RequestResult> loader, RequestResult data) {
        Log.i ("AuthFragment", data.TYPE + " (code: " + data.CODE + ")");
        if (data.TYPE.equals ("Error")) {
            Log.e ("AuthFragment", "Message: " + data.getField ("name")
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
        joinButton.setEnabled (true);
    }

    @Override
    public void onLoaderReset (Loader <RequestResult> loader) {
        joinButton.setEnabled (true);
    }

    private void _join (View view) {
        answerMessage.setText ("");
        joinButton.setEnabled (false);
        String code = inviteCode.getText ().toString ();

        RequestForm form = new RequestForm ("http://pluses.tk/api.groups.joinGroup");
        form.addParam ("token", UserEntity.getProperty ("token"));
        Log.i ("JoinGroupPage", "Code: " + code);
        form.addParam ("invite_code", code);

        Bundle args = new Bundle ();
        args.putSerializable ("form", form);
        getSupportLoaderManager ().restartLoader (0, args, this);
    }
}
