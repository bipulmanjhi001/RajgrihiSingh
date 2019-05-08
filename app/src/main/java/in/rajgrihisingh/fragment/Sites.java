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
import in.rajgrihisingh.R;
import in.rajgrihisingh.adapter.SitesAdapter;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class Sites extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    RecyclerView loadallsites;
    ProgressBar load_sites;
    private OnFragmentInteractionListener mListener;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<in.rajgrihisingh.adapter.Sites> siteList;
    SitesAdapter adapter;
    String token;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";

    public Sites() {

    }
    public static Sites newInstance(String param1, String param2) {
        Sites fragment = new Sites();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.sites_details, container, false);

        loadallsites=(RecyclerView)view.findViewById(R.id.loadallsites);
        load_sites=(ProgressBar)view.findViewById(R.id.load_sites);
        loadallsites.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        loadallsites.setLayoutManager(layoutManager);
        siteList = new ArrayList<in.rajgrihisingh.adapter.Sites>();
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    public void sendRequest(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GETSITES+"/"+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray userJson = obj.getJSONArray("sites");

                            for(int i=0; i<userJson.length(); i++) {

                                JSONObject itemslist = userJson.getJSONObject(i);
                                String id = itemslist.getString("id");
                                String name = itemslist.getString("name");
                                String type = itemslist.getString("type");
                                String address = itemslist.getString("address");

                                String description = itemslist.getString("description");
                                String supervisor_name = itemslist.getString("supervisor_name");

                                in.rajgrihisingh.adapter.Sites sites=new in.rajgrihisingh.adapter.Sites(id,name,type,address,description,supervisor_name);
                                siteList.add(sites);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        load_sites.setVisibility(View.GONE);
                        adapter=new SitesAdapter(siteList,getActivity());
                        loadallsites.setAdapter(adapter);
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
