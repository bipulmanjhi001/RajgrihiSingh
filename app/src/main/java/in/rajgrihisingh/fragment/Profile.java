package in.rajgrihisingh.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import in.rajgrihisingh.R;
import static android.content.Context.MODE_PRIVATE;

public class Profile extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String MY_PREFS_NAME = "Profilesinghpref";

    private String mParam1;
    private String mParam2;
    TextView profileNames,supervisor_names,descriptions,addresss;
    private OnFragmentInteractionListener mListener;

    public Profile() {
    }

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        View view= inflater.inflate(R.layout.profile_details, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String s_name = prefs.getString("s_name", null);
        String address = prefs.getString("address", null);
        String description = prefs.getString("description", null);
        String supervisor_name = prefs.getString("supervisor_name", null);

        profileNames=(TextView)view.findViewById(R.id.profileName);
        profileNames.setText(s_name);
        supervisor_names=(TextView)view.findViewById(R.id.supervisor_name);
        supervisor_names.setText(supervisor_name);
        descriptions=(TextView)view.findViewById(R.id.description);
        descriptions.setText(description);
        addresss=(TextView)view.findViewById(R.id.address);
        addresss.setText(address);

        return view;
    }
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
        void onFragmentInteraction(Uri uri);
    }
}
