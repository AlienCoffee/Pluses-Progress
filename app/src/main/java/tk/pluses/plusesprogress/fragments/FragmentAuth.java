package tk.pluses.plusesprogress.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.pluses.plusesprogress.R;

/**
 * Created by Андрей on 13.07.2017.
 */



public class FragmentAuth extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onClick (View v) {

    }
}
