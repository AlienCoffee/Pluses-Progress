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
        Log.i ("RegPage", "" + data.CODE);
    }

    public void click () {
        RequestForm form = new RequestForm ("http://pluses.tk/");

        Bundle args = new Bundle ();
        args.putSerializable ("form", form);
        getSupportLoaderManager ().restartLoader (0, args, this);
    }

    public static void register(View view){

    }
}
