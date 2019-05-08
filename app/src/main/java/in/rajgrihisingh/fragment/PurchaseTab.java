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

public class PurchaseTab extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TabAdapter adapters;
    TabLayout tabLayouts;
    ViewPager viewPagers;

    private OnFragmentInteractionListener mListener;
    public PurchaseTab() {
    }

    // TODO: Rename and change types and number of parameters
    public static PurchaseTab newInstance(String param1, String param2) {
        PurchaseTab fragment = new PurchaseTab();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.payment_tablayout, container, false);

        viewPagers = (ViewPager) rootView.findViewById(R.id.paymentview);
        tabLayouts = (TabLayout) rootView.findViewById(R.id.paymenttab);
        adapters = new TabAdapter(getActivity().getSupportFragmentManager());

        adapters.addFragment(new AddPurchase(), "Add Purchase");
        adapters.addFragment(new ViewPurchase(), "View Purchase");
        viewPagers.setAdapter(adapters);
        tabLayouts.setupWithViewPager(viewPagers);
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