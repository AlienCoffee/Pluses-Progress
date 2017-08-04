package tk.pluses.plusesprogress.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import tk.pluses.plusesprogress.JoinGroupPage;
import tk.pluses.plusesprogress.R;
import tk.pluses.plusesprogress.RegisterPage;
import tk.pluses.plusesprogress.io.RequestForm;
import tk.pluses.plusesprogress.io.RequestIO;
import tk.pluses.plusesprogress.io.RequestResult;
import tk.pluses.plusesprogress.user.UserEntity;

public class FragmentDiary extends Fragment implements LoaderManager.LoaderCallbacks <RequestResult> {

    String data[] = new String[] { "qwe", "qqq", "sss", "aaa" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // WARNING: was commented by Shemplo
        /*ArrayAdapter<String> adapter = new ArrayAdapter <> (getActivity (),
                android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);*/
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        if (UserEntity.getProperty ("token") == null
                || UserEntity.getProperty ("id") == null) {
            // User not logined
            // Here must be pop-up notification about it
            Log.e ("DiaryFragment", "User not logined");
            return;
        }

        RequestForm form = new RequestForm ("http://pluses.tk/api.users.getUserGroups");
        form.addParam ("token", UserEntity.getProperty ("token"));
        form.addParam ("user_id", UserEntity.getProperty ("id"));

        Bundle args = new Bundle ();
        args.putSerializable ("form", form);
        getLoaderManager ().restartLoader (0, args, this);

        view.findViewById (R.id.linkToJoinGroup).setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                Intent intent = new Intent (v.getContext (), JoinGroupPage.class);
                startActivity (intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public Loader<RequestResult> onCreateLoader (int id, Bundle args) {
        return new RequestIO (this.getContext (), (RequestForm) args.get ("form"));
    }

    @Override
    public void onLoadFinished (Loader <RequestResult> loader, RequestResult data) {
        Log.i ("DiaryFragment", data.TYPE + " (code: " + data.CODE + ")");
        Log.i ("DiaryFragment", "List: " + data.getField ("message"));
    }

    @Override
    public void onLoaderReset (Loader <RequestResult> loader) {}

}
