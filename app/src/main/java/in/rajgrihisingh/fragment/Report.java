package in.rajgrihisingh.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import in.rajgrihisingh.R;
import in.rajgrihisingh.adapter.TabAdapter;
import in.rajgrihisingh.tablayout.Issue_Details;
import in.rajgrihisingh.tablayout.JCB;
import in.rajgrihisingh.tablayout.Laying_Details;
import in.rajgrihisingh.tablayout.Man_Power;
import in.rajgrihisingh.tablayout.Special_Fittings;

public class Report extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TabAdapter adapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    private OnFragmentInteractionListener mListener;

    public Report() {
    }
    // TODO: Rename and change types and number of parameters
    public static Report newInstance(String param1, String param2) {
        Report fragment = new Report();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.di_pipe_laying_report, container, false);

        viewPager = (ViewPager)rootView.findViewById(R.id.viewPager);
        tabLayout = (TabLayout)rootView.findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getActivity().getSupportFragmentManager());

        adapter.addFragment(new Issue_Details(), "Issue Details");
        adapter.addFragment(new Laying_Details(), "Laying Details");
        adapter.addFragment(new Special_Fittings(), "Special Fittings");
        adapter.addFragment(new JCB(), "JCB Details");
        adapter.addFragment(new Man_Power(), "Man Power");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
