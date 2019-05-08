package in.rajgrihisingh.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
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
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class AddPurchase extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private CalendarView calendar_payment;
    TextView purchase_dates;

    EditText supplier,suppler_mobile,supplier_invoice,rate,uom;
    EditText discount,items_sites,item_type,quantity,gst_rate,hsn;
    Button add_purchase,choose_supplier,choose_item,choose_type;

    String suppliers,suppler_mobiles,supplier_invoices,rates,gst_rates,hsns;
    String discounts,items_sitess,item_types,quantitys,uoms,getsuppler_mob;

    String token;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";

    ArrayList item_ids = new ArrayList();
    ArrayList item_names = new ArrayList();
    ArrayList type_ids = new ArrayList();
    ArrayList type_names = new ArrayList();
    ArrayList suppler_ids = new ArrayList();
    ArrayList suppler_names = new ArrayList();
    ArrayList suppler_mob = new ArrayList();

    ListView Type,Item,Supplier;
    String getitem_namesId,getsuppler_namesId,gettype_namesId;

    private OnFragmentInteractionListener mListener;

    public AddPurchase() {
    }

    public static AddPurchase newInstance(String param1, String param2) {
        AddPurchase fragment = new AddPurchase();
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
        View view= inflater.inflate(R.layout.purchase_details, container, false);

        calendar_payment=(CalendarView)view.findViewById(R.id.purchase_calendar);
        purchase_dates=(TextView)view.findViewById(R.id.purchase_date);
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        calendar_payment.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date=String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                purchase_dates.setText("Purchase Date : "+date);
            }
        });
        SupplierDetails();
        choose_supplier=(Button)view.findViewById(R.id.choose_supplier);
        choose_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupSupplier(v);
            }
        });
        choose_item=(Button)view.findViewById(R.id.choose_item);
        choose_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupItem(v);
            }
        });
        choose_type=(Button)view.findViewById(R.id.choose_type);
        choose_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupType(v);
            }
        });

        uom=(EditText)view.findViewById(R.id.uom);
        add_purchase=(Button)view.findViewById(R.id.add_purchase);
        add_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptPurchase();
            }
        });
        supplier=(EditText)view.findViewById(R.id.supplier);
        suppler_mobile=(EditText)view.findViewById(R.id.suppler_mobile);
        supplier_invoice=(EditText)view.findViewById(R.id.supplier_invoice);
        rate=(EditText)view.findViewById(R.id.rate);

        gst_rate=(EditText)view.findViewById(R.id.gst_rate);
        discount=(EditText)view.findViewById(R.id.discount);
        discount=(EditText)view.findViewById(R.id.discount);
        items_sites=(EditText)view.findViewById(R.id.items_sites);
        item_type=(EditText)view.findViewById(R.id.item_type);
        quantity=(EditText)view.findViewById(R.id.quantity);
        hsn=(EditText)view.findViewById(R.id.hsn);

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
    private void attemptPurchase() {
        suppliers = supplier.getText().toString();
        suppler_mobiles = suppler_mobile.getText().toString();
        supplier_invoices = supplier_invoice.getText().toString();
        rates = rate.getText().toString();

        hsns = hsn.getText().toString();
        discounts = discount.getText().toString();
        items_sitess = items_sites.getText().toString();
        item_types = item_type.getText().toString();
        quantitys = quantity.getText().toString();
        uoms = uom.getText().toString();
        gst_rates = gst_rate.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(suppliers)) {
            supplier.setError(getString(R.string.error_field_required));
            focusView = supplier;
            cancel = true;
        }
        if (TextUtils.isEmpty(suppler_mobiles)) {
            suppler_mobile.setError(getString(R.string.error_field_required));
            focusView = suppler_mobile;
            cancel = true;
        }
        if (TextUtils.isEmpty(supplier_invoices)) {
            supplier_invoice.setError(getString(R.string.error_field_required));
            focusView = supplier_invoice;
            cancel = true;
        }
        if (TextUtils.isEmpty(items_sitess)) {
            items_sites.setError(getString(R.string.error_field_required));
            focusView = items_sites;
            cancel = true;
        }
        if (TextUtils.isEmpty(item_types)) {
            item_type.setError(getString(R.string.error_field_required));
            focusView = item_type;
            cancel = true;
        }
        if (TextUtils.isEmpty(quantitys)) {
            quantity.setError(getString(R.string.error_field_required));
            focusView = quantity;
            cancel = true;
        }
        if (TextUtils.isEmpty(uoms)) {
            uom.setError(getString(R.string.error_field_required));
            focusView = uom;
            cancel = true;
        }
        if (TextUtils.isEmpty(discounts)) {
            discount.setError(getString(R.string.error_field_required));
            focusView = discount;
            cancel = true;
        }
        if (TextUtils.isEmpty(gst_rates)) {
            gst_rate.setError(getString(R.string.error_field_required));
            focusView = gst_rate;
            cancel = true;
        }
        if (cancel) {

            focusView.requestFocus();

        }else {
            AddPurchases();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    private void showPopupSupplier(View view) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.supplier_list_dialog);
        Supplier= (ListView) dialog.findViewById(R.id.supplier_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, suppler_names);
        Supplier.setAdapter(adapter);
        Supplier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                supplier.setText(suppler_names.get(position).toString());
                getsuppler_mob=suppler_mob.get(position).toString();
                getsuppler_namesId=suppler_ids.get(position).toString();
                suppler_mobile.setText(getsuppler_mob);
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void showPopupItem(View view) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.list_dialog);
        Item= (ListView) dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, item_names);
        Item.setAdapter(adapter);
        Item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                items_sites.setText(item_names.get(position).toString());
                getitem_namesId=item_ids.get(position).toString();
                ItemType();
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void showPopupType(View view) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.type_list_dialog);
        Type= (ListView) dialog.findViewById(R.id.type_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, type_names);
        Type.setAdapter(adapter);
        Type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item_type.setText(type_names.get(position).toString());
                gettype_namesId=type_ids.get(position).toString();
                dialog.dismiss();
                type_names.clear();
                type_ids.clear();
            }
        });
        dialog.show();
    }

    public void Items(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GETPURCHASEITEMS+"/"+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("items");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String item_type = itemslist.getString("item_name");
                                    item_names.add(item_type);
                                    String item_id = itemslist.getString("id");
                                    item_ids.add(item_id);

                                }
                            }else {
                                Toast.makeText(getActivity(),"No supplier added",Toast.LENGTH_SHORT).show();
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

    public void ItemType(){
        type_ids.clear();
        type_names.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETTEMSTYPELIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("item_types");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String item_id = itemslist.getString("id");
                                    type_ids.add(item_id);
                                    String item_type = itemslist.getString("item_type");
                                    type_names.add(item_type);

                                }
                            }else {
                                Toast.makeText(getActivity(),"No supplier added",Toast.LENGTH_SHORT).show();
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
                params.put("item_id", getitem_namesId);

                return params;
            }

        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    public void SupplierDetails(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GETSUPPLIERS+"/"+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("suppliers");

                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    suppler_ids.add(id);
                                    String name = itemslist.getString("name");
                                    suppler_names.add(name);
                                    String mob = itemslist.getString("mobile");
                                    suppler_mob.add(mob);
                                }
                            }else {
                                Toast.makeText(getActivity(),"No supplier added",Toast.LENGTH_SHORT).show();
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
        Items();
    }

    private void AddPurchases() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADDPURCHASE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                supplier.setText("");
                                suppler_mobile.setText("");
                                supplier_invoice.setText("");
                                discount.setText("");
                                gst_rate.setText("");
                                items_sites.setText("");
                                item_type.setText("");
                                hsn.setText("");
                                quantity.setText("");
                                uom.setText("");

                            } else {
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
                        Toast.makeText(getActivity(), "Check details again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("token", token);
                params.put("supplier_id", getsuppler_namesId);
                params.put("supplier_name", suppliers);
                params.put("supplier_mobile", suppler_mobiles);
                params.put("invoice_no",supplier_invoices);
                params.put("item_id", getitem_namesId);
                params.put("item_type_id", gettype_namesId);
                params.put("hsn", hsns);
                params.put("quantity", quantitys);
                params.put("uom", uoms);
                params.put("rate",rates);
                params.put("discount",discounts);
                params.put("gst_rate",gst_rates);

                return params;
            }

        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
