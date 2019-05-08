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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import in.rajgrihisingh.R;
import in.rajgrihisingh.adapter.ViewPaidAdapter;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.SalaryList;
import in.rajgrihisingh.model.VolleySingleton;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class ViewPaid extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ListView view_salary_list;
    FloatingActionButton view_salary_refresh;
    ProgressBar view_salary_list_pro;
    private OnFragmentInteractionListener mListener;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    String token;
    ArrayList<SalaryList> salaryLists;
    ViewPaidAdapter adapter;

    public ViewPaid() {
    }

    public static ViewPaid newInstance(String param1, String param2) {
        ViewPaid fragment = new ViewPaid();
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
        View view= inflater.inflate(R.layout.view_salary, container, false);

        view_salary_refresh=(FloatingActionButton)view.findViewById(R.id.view_salary_refresh);
        view_salary_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salaryLists.clear();
                 CallPaidList();
                ObjectAnimator.ofFloat(view_salary_refresh, "rotation", 0f, 360f).setDuration(800).start();
            }
        });

        view_salary_list=(ListView)view.findViewById(R.id.view_salary_list);
        view_salary_list_pro=(ProgressBar)view.findViewById(R.id.view_salary_list_pro);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");
        salaryLists=new ArrayList<SalaryList>();
        CallPaidList();
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

    public void CallPaidList(){
        view_salary_list_pro.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_VIEWSALARY+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("salarylist");

                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String date = itemslist.getString("date");
                                    String staff_name = itemslist.getString("staff_name");
                                    String amount = itemslist.getString("amount");

                                    SalaryList salaryList = new SalaryList(id, date, staff_name, amount);
                                    salaryLists.add(salaryList);

                                }
                                try {
                                    view_salary_list_pro.setVisibility(View.GONE);
                                    adapter = new ViewPaidAdapter(salaryLists, getActivity());
                                    view_salary_list.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }
                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
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
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
