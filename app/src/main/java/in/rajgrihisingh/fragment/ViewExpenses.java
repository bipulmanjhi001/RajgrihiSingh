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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import in.rajgrihisingh.R;
import in.rajgrihisingh.adapter.ExpenseListAdpater;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.ExpenseList;
import in.rajgrihisingh.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class ViewExpenses extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private Expenses.OnFragmentInteractionListener mListener;
    ProgressBar pr_at_list;
    String token;
    ExpenseListAdpater adapter;
    ListView expense_list;
    ArrayList<ExpenseList> expenseListss;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    String image;
    FloatingActionButton floatingActionButton;
    public ViewExpenses() {
    }

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        View view = inflater.inflate(R.layout.expense_view, container, false);
        expense_list = (ListView) view.findViewById(R.id.expense__list);
        expense_list.setDivider(null);
        pr_at_list = (ProgressBar) view.findViewById(R.id.expense_at_list);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");
        expenseListss=new ArrayList<ExpenseList>();

        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.expense_staff);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseListss.clear();
                CallList();
                ObjectAnimator.ofFloat(floatingActionButton, "rotation", 0f, 360f).setDuration(800).start();

            }
        });

        CallList();
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

    public void CallList(){
        pr_at_list.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_SHOWEXPENSE+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray userJson = obj.getJSONArray("expenses");

                            for(int i=0; i<userJson.length(); i++) {

                                JSONObject itemslist = userJson.getJSONObject(i);
                                String id = itemslist.getString("id");
                                String expense_head_id = itemslist.getString("expense_head_id");
                                String date=itemslist.getString("date");
                                String bill_no = itemslist.getString("bill_no");
                                image = itemslist.getString("image");
                                String name = itemslist.getString("name");
                                String remarks=itemslist.getString("remarks");
                                String amount=itemslist.getString("amount");
                                String site_id = itemslist.getString("site_id");
                                String supervisor=itemslist.getString("supervisor");
                                String expense_head = itemslist.getString("expense_head");
                                String site_name=itemslist.getString("site_name");

                                ExpenseList expenseList = new ExpenseList(id,expense_head_id,date,bill_no,image,name,remarks,amount,site_id,supervisor,expense_head,site_name);
                                expenseListss.add(expenseList);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            pr_at_list.setVisibility(View.GONE);
                            adapter = new ExpenseListAdpater(expenseListss, getActivity());
                            expense_list.setAdapter(adapter);
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
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
