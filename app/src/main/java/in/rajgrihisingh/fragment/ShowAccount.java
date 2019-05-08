package in.rajgrihisingh.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import in.rajgrihisingh.R;
import in.rajgrihisingh.adapter.AccountAdapter;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.Account;
import in.rajgrihisingh.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class ShowAccount extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RecyclerView loadaccounts;
    ProgressBar load_account;
    ArrayList<Account> accounts;
    AccountAdapter adapter;
    String token,id,date,site_id,amount,supervisor_name,site_name;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";

    public ShowAccount() {
    }
    public static ShowAccount newInstance(String param1, String param2) {
        ShowAccount fragment = new ShowAccount();
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
        View view= inflater.inflate(R.layout.fragment_account, container, false);

        loadaccounts=(RecyclerView)view.findViewById(R.id.loadaccount);
        loadaccounts.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        loadaccounts.setLayoutManager(mLayoutManager);
        load_account=(ProgressBar)view.findViewById(R.id.load_account);
        accounts = new ArrayList<Account>();

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token=sp.getString("keyid", "");
        sendRequest();

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
    public void sendRequest(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_MONEYTRANSFER+"/"+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("transfers");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    id = itemslist.getString("id");
                                    date = itemslist.getString("date");
                                    site_id = itemslist.getString("site_id");
                                    amount = itemslist.getString("amount");
                                    supervisor_name = itemslist.getString("supervisor_name");
                                    site_name = itemslist.getString("site_name");

                                    Account account = new Account(id, date, amount, supervisor_name, site_name);
                                    accounts.add(account);
                                }
                                load_account.setVisibility(View.GONE);
                                adapter=new AccountAdapter(accounts,getActivity());
                                loadaccounts.setAdapter(adapter);
                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"),Toast.LENGTH_SHORT).show();
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
