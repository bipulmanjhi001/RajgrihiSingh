package in.rajgrihisingh.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import in.rajgrihisingh.R;
import in.rajgrihisingh.adapter.StaffAdapter;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.StaffList;
import in.rajgrihisingh.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class SkilledStaff extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private Staff.OnFragmentInteractionListener mListener;
    ListView skilled_staff_list;
    ProgressBar skilled_staff_list_progress;
    String token;
    ArrayList<StaffList> staffLists;
    StaffAdapter adapter;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";

    FloatingActionButton floatingActionButton;
    public SkilledStaff() {
    }

    public static SkilledStaff newInstance(String param1, String param2) {
        SkilledStaff fragment = new SkilledStaff();
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
        View view = inflater.inflate(R.layout.skilled_staff, container, false);

        skilled_staff_list = (ListView) view.findViewById(R.id.skilled_staff_list);
        skilled_staff_list.setDivider(null);
        skilled_staff_list_progress = (ProgressBar) view.findViewById(R.id.skilled_staff_list_progress);
        staffLists = new ArrayList<StaffList>();

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        staffLists=new ArrayList<StaffList>();
        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.skilled_staff_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staffLists.clear();
                Skilled();
                ObjectAnimator.ofFloat(floatingActionButton, "rotation", 0f, 360f).setDuration(800).start();

            }
        });

        Skilled();
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
    private void Skilled(){
        skilled_staff_list_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETSTAFF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray userJson = obj.getJSONArray("stafflist");

                            for(int i=0; i<userJson.length(); i++) {

                                JSONObject itemslist = userJson.getJSONObject(i);
                                String name = itemslist.getString("name");
                                String mobile=itemslist.getString("mobile");
                                String address=itemslist.getString("address");
                                String designation = itemslist.getString("designation");
                                String gender=itemslist.getString("gender");

                                StaffList staffList = new StaffList(name,mobile,address,designation,gender);
                                staffLists.add(staffList);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            skilled_staff_list_progress.setVisibility(View.GONE);
                            adapter = new StaffAdapter(staffLists, getActivity());
                            skilled_staff_list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
        {
           @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",token);
                params.put("type", "Skilled");
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}

