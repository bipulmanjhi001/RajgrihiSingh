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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import in.rajgrihisingh.R;
import in.rajgrihisingh.adapter.StockAdapter;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.StockList;
import in.rajgrihisingh.model.VolleySingleton;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class ShowStock extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RecyclerView stockaccount;
    ProgressBar load_account;

    ArrayList<StockList> stockListss;
    StockAdapter stockAdapter;
    String token, id;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    String item_type_id, item_type, site_ids, item_name, site_names, quantity;

    public ShowStock() {
    }

    public static ShowStock newInstance(String param1, String param2) {
        ShowStock fragment = new ShowStock();
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
        View view = inflater.inflate(R.layout.fragment_stock, container, false);

        load_account = (ProgressBar) view.findViewById(R.id.load_account2);
        stockaccount = (RecyclerView) view.findViewById(R.id.stockaccount);
        stockaccount.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        stockaccount.setLayoutManager(mLayoutManager2);

        stockListss = new ArrayList<StockList>();
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");
        stockRequest();

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

    public void stockRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GETSTOCK + "/" + token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray userJson = obj.getJSONArray("stock");

                            for (int i = 0; i < userJson.length(); i++) {

                                JSONObject itemslist = userJson.getJSONObject(i);
                                item_type_id = itemslist.getString("item_type_id");
                                item_type = itemslist.getString("item_type");
                                site_ids = itemslist.getString("site_id");
                                item_name = itemslist.getString("item_name");
                                site_names = itemslist.getString("site_name");
                                quantity = itemslist.getString("quantity");

                                try {
                                    StockList stockList = new StockList(item_type_id, item_type, site_ids, item_name, site_names, quantity);
                                    stockListss.add(stockList);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                            load_account.setVisibility(View.GONE);
                            stockAdapter = new StockAdapter(stockListss, getActivity());
                            stockaccount.setAdapter(stockAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}