package sinra.narsha.kr.myapplication.frag_view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sinra.narsha.kr.myapplication.R;
import sinra.narsha.kr.myapplication.frag_view.FirstFragment;

public class ThirdFragment extends Fragment {

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_third, container, false);
    }
}