package in.rajgrihisingh.fragment;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.rajgrihisingh.R;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;
import static android.content.Context.MODE_PRIVATE;

public class Manufacture extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    EditText manufacture_date,raw_itemss,raw_item_qty,ready_quantity,manpower;
    Button submit_manufacture,get_raw_quantity,ready_item_type_id,
            raw_item_type_id,raw_item_id,get_date;

    private int mYear, mMonth, mDay;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";

    ArrayList item_ids = new ArrayList();
    ArrayList item_names = new ArrayList();
    ArrayList type_ids = new ArrayList();
    ArrayList type_names = new ArrayList();
    ListView Type,Item;

    String getitem_namesId,gettype_namesId,token;
    String manufacture_dates,raw_items,raw_item_types,raw_quantitys,ready_quantitys,manpowers;

    private OnFragmentInteractionListener mListener;

    ArrayList item_ids2 = new ArrayList();
    ArrayList item_names2 = new ArrayList();
    ArrayList type_ids2 = new ArrayList();
    ArrayList type_names2 = new ArrayList();
    ListView Type2,Item2;

    String getitem_namesId2,gettype_namesId2;
    public Manufacture() {
    }

    public static Manufacture newInstance(String param1, String param2) {
        Manufacture fragment = new Manufacture();
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
        View view = inflater.inflate(R.layout.manufacture_details, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        manufacture_date=(EditText)view.findViewById(R.id.manufacture_date);
        raw_itemss=(EditText)view.findViewById(R.id.raw_item);
        raw_item_qty=(EditText)view.findViewById(R.id.raw_item_qty);
        ready_quantity=(EditText)view.findViewById(R.id.ready_quantitys);
        manpower=(EditText)view.findViewById(R.id.manpower);

        submit_manufacture=(Button)view.findViewById(R.id.submit_manufacture);
        get_raw_quantity=(Button)view.findViewById(R.id.get_raw_quantity);
        raw_item_type_id=(Button)view.findViewById(R.id.raw_item_type_id);

        ready_item_type_id=(Button)view.findViewById(R.id.ready_item_type_id);
        raw_item_id=(Button)view.findViewById(R.id.raw_item_id);
        get_date=(Button)view.findViewById(R.id.get_date);

        Items();
        Items2();
        get_date.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                manufacture_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        get_raw_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupItem2(v);
            }
        });

        ready_item_type_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupType2(v);
            }
        });

        raw_item_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupItem(v);
            }
        });

        raw_item_type_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupType(v);
            }
        });
        submit_manufacture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptManufacture();
            }
        });

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

    private void showPopupItem(View view) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.list_dialog);
        Item= (ListView) dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, item_names);
        Item.setAdapter(adapter);
        Item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //raw_item.setText(item_names.get(position).toString());
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
                raw_itemss.setText(type_names.get(position).toString());
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

    private void showPopupItem2(View view) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.list_dialog);
        Item2= (ListView) dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, item_names2);
        Item2.setAdapter(adapter);
        Item2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //raw_item.setText(item_names.get(position).toString());
                getitem_namesId2=item_ids2.get(position).toString();
                ItemType2();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void showPopupType2(View view) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.type_list_dialog);
        Type2= (ListView) dialog.findViewById(R.id.type_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, type_names2);
        Type2.setAdapter(adapter);
        Type2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gettype_namesId2=type_ids2.get(position).toString();
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public void Items2(){
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
                                    item_names2.add(item_type);
                                    String item_id = itemslist.getString("id");
                                    item_ids2.add(item_id);

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

    public void ItemType2(){
        type_names2.clear();
        type_ids2.clear();
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
                                    type_ids2.add(item_id);
                                    String item_type = itemslist.getString("item_type");
                                    type_names2.add(item_type);

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
                params.put("item_id", getitem_namesId2);

                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }



    private void attemptManufacture() {

        manufacture_dates = manufacture_date.getText().toString();
        raw_items = raw_itemss.getText().toString();
        raw_item_types = raw_item_qty.getText().toString();
        ready_quantitys=ready_quantity.getText().toString();
        manpowers=manpower.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(manufacture_dates)) {
            manufacture_date.setError(getString(R.string.error_field_required));
            focusView = manufacture_date;
            cancel = true;
        }
        if (TextUtils.isEmpty(raw_items)) {
            raw_itemss.setError(getString(R.string.error_field_required));
            focusView = raw_itemss;
            cancel = true;
        }
        if (TextUtils.isEmpty(raw_item_types)) {
            raw_item_qty.setError(getString(R.string.error_field_required));
            focusView = raw_item_qty;
            cancel = true;
        }
        if (TextUtils.isEmpty(ready_quantitys)) {
            ready_quantity.setError(getString(R.string.error_field_required));
            focusView = ready_quantity;
            cancel = true;
        }
        if (TextUtils.isEmpty(manpowers)) {
            manpower.setError(getString(R.string.error_field_required));
            focusView = manpower;
            cancel = true;
        }
        if (cancel) {

            focusView.requestFocus();

        }else {

            AddManufacture();

        }
    }
    private void AddManufacture() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_Manufacture,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                manufacture_date.setText("");
                                raw_itemss.setText("");
                                raw_item_qty.setText("");
                                ready_quantity.setText("");
                                manpower.setText("");

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
                params.put("date", manufacture_dates);
                params.put("manpower", manpowers);
                params.put("raw_item_type_id", gettype_namesId);
                params.put("raw_quantity", raw_item_types);
                params.put("ready_item_type_id",gettype_namesId2);
                params.put("ready_quantity", ready_quantitys);

                return params;
            }

        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }
}