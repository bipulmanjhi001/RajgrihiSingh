package in.rajgrihisingh.tablayout;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import in.rajgrihisingh.R;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.signature.CaptureSignature;
import in.rajgrihisingh.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static android.content.Context.MODE_PRIVATE;

public class Issue_Details extends Fragment  {
    private Button issuer_signature,reciver_signature,item;
    private Button class_type;
    private CalendarView calendar_issue;
    private TextView issue_datetext,di_name;
    private EditText vr_no,vendor_name,di_type,qty_type;
    String get_vr_no,getvendor_name,getdi_type,getqty_type;

    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    String name,email,token;
    ArrayList item_ids = new ArrayList();
    ArrayList item_names = new ArrayList();

    ArrayList type_ids = new ArrayList();
    ArrayList type_names = new ArrayList();
    ListView Type,Item;
    String getitem_namesId,gettype_namesId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.issue_details, container, false);

        issuer_signature=(Button)rootView.findViewById(R.id.issuer_signature);
        reciver_signature=(Button)rootView.findViewById(R.id.reciver_signature);
        class_type=(Button)rootView.findViewById(R.id.class_type);
        item=(Button)rootView.findViewById(R.id.item);


        calendar_issue=(CalendarView)rootView.findViewById(R.id.calendar_issue);
        issue_datetext=(TextView)rootView.findViewById(R.id.issue_datetext);

        vr_no= (EditText)rootView.findViewById(R.id.vr_no);
        vendor_name= (EditText)rootView.findViewById(R.id.vendor_name);
        di_type= (EditText)rootView.findViewById(R.id.di_type);
        qty_type= (EditText)rootView.findViewById(R.id.qty_type);
        di_name=(TextView)rootView.findViewById(R.id.di_name);

        Call();

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        name = sp.getString("keyusername", "");
        token=sp.getString("keyid", "");
        Log.d("keyid",token);
        email=sp.getString("email", "");

        Items();
        return rootView;
    }
    private void attemptLogin() {
        getvendor_name = vendor_name.getText().toString();
        get_vr_no = vr_no.getText().toString();
        getdi_type = di_type.getText().toString();
        getqty_type = qty_type.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getvendor_name)) {
            vendor_name.setError(getString(R.string.error_field_required));
            focusView = vendor_name;
            cancel = true;
        }
        if (TextUtils.isEmpty(getdi_type)) {
            di_type.setError(getString(R.string.error_field_required));
            focusView = di_type;
            cancel = true;
        }
        if (TextUtils.isEmpty(getqty_type)) {
            qty_type.setError(getString(R.string.error_field_required));
            focusView = qty_type;
            cancel = true;
        }
        if (TextUtils.isEmpty(get_vr_no)) {
            vr_no.setError(getString(R.string.error_field_required));
            focusView = vr_no;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }
    }
    void Call(){
        issuer_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vr =vr_no.getText().toString();
                String name= vendor_name.getText().toString();
                String type=di_type.getText().toString();
                String qty= qty_type.getText().toString();

                if(!vr.isEmpty() && !name.isEmpty() && !type.isEmpty() && !qty.isEmpty()) {

                    Intent intent = new Intent(getActivity().getBaseContext(), CaptureSignature.class);
                    intent.putExtra("sign_name", "issuer_sign");
                    intent.putExtra("vendor_name", name);
                    intent.putExtra("item_type_id",gettype_namesId);
                    intent.putExtra("quantity",qty);
                    intent.putExtra("vr_no",vr);
                    getActivity().startActivity(intent);

                    vr_no.setText("");
                    vendor_name.setText("");
                    di_type.setText("");
                    qty_type.setText("");

                }else {
                    attemptLogin();
                }
            }
        });
        reciver_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vr =vr_no.getText().toString();
                String name= vendor_name.getText().toString();
                String type=di_type.getText().toString();
                String qty= qty_type.getText().toString();

                if(!vr.isEmpty() && !name.isEmpty() && !type.isEmpty() && !qty.isEmpty()) {

                    Intent intent = new Intent(getActivity().getBaseContext(), CaptureSignature.class);
                    intent.putExtra("sign_name", "receiver_sign");
                    intent.putExtra("vendor_name", name);
                    intent.putExtra("item_type_id",gettype_namesId);
                    intent.putExtra("quantity",qty);
                    intent.putExtra("vr_no",vr);
                    getActivity().startActivity(intent);

                }else {
                    attemptLogin();
                }
            }
        });
        calendar_issue.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                String date=String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                issue_datetext.setText("Issue Date : "+date);

            }
        });

        class_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupType(v);
            }
        });
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupItem(v);
            }
        });
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

                di_name.setText("Item : "+item_names.get(position).toString());
                getitem_namesId=item_ids.get(position).toString();
                ItemType();
                dialog.dismiss();
                item_ids.clear();
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
                di_type.setText(type_names.get(position).toString());
                gettype_namesId=type_ids.get(position).toString();
                dialog.dismiss();

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
        type_names.clear();
        type_ids.clear();
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
}
