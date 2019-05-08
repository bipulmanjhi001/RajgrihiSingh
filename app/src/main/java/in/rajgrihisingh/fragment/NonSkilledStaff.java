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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import in.rajgrihisingh.R;
import in.rajgrihisingh.adapter.NonSkilledStaffAdapter;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.NonSkilledList;
import in.rajgrihisingh.model.VolleySingleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class NonSkilledStaff extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private Staff.OnFragmentInteractionListener mListener;
    ListView nonskilled_staff_list;
    ProgressBar nonskilled_staff_progress;
    String token;
    ArrayList<NonSkilledList> nonSkilledStaffs;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    NonSkilledStaffAdapter adapter;
    FloatingActionButton floatingActionButton;

    public static NonSkilledStaff newInstance(String param1, String param2) {
        NonSkilledStaff fragment = new NonSkilledStaff();
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
        View view = inflater.inflate(R.layout.nonskilledstaff, container, false);

        nonskilled_staff_list = (ListView) view.findViewById(R.id.nonskilled_staff_list);
        nonskilled_staff_list.setDivider(null);
        nonskilled_staff_progress = (ProgressBar) view.findViewById(R.id.nonskilled_staff_progress);

        nonSkilledStaffs = new ArrayList<NonSkilledList>();
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.skilled_staff_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonSkilledStaffs.clear();
                nonSkilled();
                ObjectAnimator.ofFloat(floatingActionButton, "rotation", 0f, 360f).setDuration(800).start();
            }
        });

        nonSkilled();
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
   private void nonSkilled(){
        nonskilled_staff_progress.setVisibility(View.VISIBLE);
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

                                NonSkilledList nonSkilledList = new NonSkilledList(name,mobile,address,designation,gender);
                                nonSkilledStaffs.add(nonSkilledList);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            nonskilled_staff_progress.setVisibility(View.GONE);
                            adapter = new NonSkilledStaffAdapter(nonSkilledStaffs, getActivity());
                            nonskilled_staff_list.setAdapter(adapter);
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
                params.put("type", "NonSkilled");
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}

