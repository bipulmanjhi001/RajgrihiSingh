package in.rajgrihisingh.fragment;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import in.rajgrihisingh.R;
import in.rajgrihisingh.adapter.ViewPurchaseAdapter;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.ViewPurchaseList;
import in.rajgrihisingh.model.VolleySingleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ViewPurchase extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ListView purchase_view_list;
    ProgressBar view_purchase_list_pro;
    FloatingActionButton view_purchase_refresh;

    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    String token;
    String getID;
    ArrayList<ViewPurchaseList> viewPurchaseLists;
    ViewPurchaseAdapter viewPurchaseAdapter;
    ArrayList value=new ArrayList();
    String id ;
    String quantitys ;
    String uoms;
    String rates;
    String discounts;
    String amounts;
    String item_types;
    String item_names;
    String item_type_ids;

    private OnFragmentInteractionListener mListener;

    public ViewPurchase() {
    }

    public static ViewPurchase newInstance(String param1, String param2) {
        ViewPurchase fragment = new ViewPurchase();
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
        View view = inflater.inflate(R.layout.view_purchase, container, false);

        purchase_view_list = (ListView) view.findViewById(R.id.view_purchase_list);
        purchase_view_list.setDivider(null);
        view_purchase_list_pro= (ProgressBar) view.findViewById(R.id.view_purchase_list_pro);
        view_purchase_refresh=(FloatingActionButton)view.findViewById(R.id.view_purchase_refresh);
        viewPurchaseLists = new ArrayList<ViewPurchaseList>();

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        view_purchase_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPurchaseLists.clear();
                LoadViewPurchase();
                ObjectAnimator.ofFloat(view_purchase_refresh, "rotation", 0f, 360f).setDuration(800).start();
            }
        });

        purchase_view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getID = String.valueOf(value.get(position));
                ViewPurchaseDetails();
            }
        });


        LoadViewPurchase();

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

    private void LoadViewPurchase(){
        view_purchase_list_pro.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GETVIEWPURCHASE+"/"+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("purchaselist");

                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    value.add(id);
                                    String date = itemslist.getString("date");
                                    String supplier_id = itemslist.getString("supplier_id");
                                    String invoice_no = itemslist.getString("invoice_no");
                                    String gross_amount = itemslist.getString("gross_amount");
                                    String total_amount = itemslist.getString("total_amount");
                                    String supplier_name = itemslist.getString("supplier_name");
                                    String site_name = itemslist.getString("site_name");
                                    ViewPurchaseList viewPurchaseList = new ViewPurchaseList(id, date, supplier_id, invoice_no, gross_amount, total_amount, supplier_name, site_name);
                                    viewPurchaseLists.add(viewPurchaseList);
                                }
                            }else {
                                Toast.makeText(getActivity(), "Check details again.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            view_purchase_list_pro.setVisibility(View.GONE);
                            viewPurchaseAdapter = new ViewPurchaseAdapter(viewPurchaseLists, getActivity());
                            purchase_view_list.setAdapter(viewPurchaseAdapter);
                            viewPurchaseAdapter.notifyDataSetChanged();
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

    private void ViewPurchaseDetails(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_VIEWPURCHASEDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")){
                            JSONArray userJson = obj.getJSONArray("products");

                            for(int i=0; i<userJson.length(); i++) {
                                JSONObject itemslist = userJson.getJSONObject(i);
                                id = itemslist.getString("id");
                                quantitys = itemslist.getString("quantity");
                                uoms = itemslist.getString("uom");
                                rates = itemslist.getString("rate");
                                discounts = itemslist.getString("discount");
                                amounts = itemslist.getString("amount");
                                item_types = itemslist.getString("item_type");
                                item_names = itemslist.getString("item_name");
                                item_type_ids = itemslist.getString("item_type_id");
                            }
                            try {
                                final Dialog dialog = new Dialog(getActivity());
                                dialog.setContentView(R.layout.purchase_details_popup);
                                dialog.setTitle("Purchase Details");
                                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                                TextView quantity = (TextView) dialog.findViewById(R.id.quantity);
                                quantity.setText("Quantitys  : "+  quantitys);
                                TextView uom = (TextView) dialog.findViewById(R.id.uom);
                                uom.setText("UOM  : "+  uoms);
                                TextView rate = (TextView) dialog.findViewById(R.id.rate);
                                rate.setText("Rates  : "+  rates);
                                TextView discount = (TextView) dialog.findViewById(R.id.discount);
                                discount.setText("Discounts  : "+  discounts);
                                TextView amount = (TextView) dialog.findViewById(R.id.amount);
                                amount.setText("Amounts  : "+  amounts);
                                TextView item_type = (TextView) dialog.findViewById(R.id.item_type);
                                item_type.setText("Item Types  : "+  item_types);
                                TextView item_name = (TextView) dialog.findViewById(R.id.item_name);
                                item_name.setText("Item Names  : "+  item_names);
                                TextView item_type_id = (TextView) dialog.findViewById(R.id.item_type_id);
                                item_type_id.setText("Item Type Ids  : "+  item_type_ids);

                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            }else{
                                Toast.makeText(getActivity(), "Check details again.", Toast.LENGTH_SHORT).show();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("token", token);
                params.put("purchase_id", getID);

                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}