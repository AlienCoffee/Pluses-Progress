package tk.pluses.plusesprogress;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import tk.pluses.plusesprogress.io.RequestForm;
import tk.pluses.plusesprogress.io.RequestIO;
import tk.pluses.plusesprogress.io.RequestResult;

public class RegisterPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks <RequestResult> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Sending request to the server
        // It can be called in each click on button (f. e.)
        click ();
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
        // This method will be called when answer of error received
        // First argument is not important (not need to use)
        // Second argument is object that represents answer from server
        // (How to use it, read in sources)
        Log.i ("RegPage", "" + data.CODE);
    }

    public void click () {
        // Making new request form
        RequestForm form = new RequestForm ("http://pluses.tk/");
        // Here can be adding of arguments like:
        // form.addParam ("key", "value");

        // Default tuple for calling constructor
        Bundle args = new Bundle ();
        // Adding our form to request builder
        args.putSerializable ("form", form);
        // Tell android to send new request right now
        getSupportLoaderManager ().restartLoader (0, args, this);
    }

    public static void register(View view){

    }
}
