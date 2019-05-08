package in.rajgrihisingh.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import in.rajgrihisingh.R;
import in.rajgrihisingh.activity.MainActivity;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;
import static android.content.Context.MODE_PRIVATE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.location.LocationListener;
import android.view.View.OnClickListener;


public class Boring extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String token;
    LocationManager locationManager;
    EditText calendarss, block, panchayat, village, skilled, boring_pipe_qty;
    EditText tola, locationss, cashing, depth, water, start_rpm;
    EditText end_rpm, hour, choose_items, typess, diseal, non_skilled;

    String calendars, blocks, panchayats, villages, skilleds;
    String tolas, locations, cashings, depths, waters, start_rpms;
    String end_rpms, hours, choose_itemss, typesss, diseals, non_skilleds;
    Button boring_date, submit_boring_report, item, type, locationsss;

    private int mYear, mMonth, mDay;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    ArrayList item_ids = new ArrayList();
    ArrayList item_names = new ArrayList();
    ArrayList type_ids = new ArrayList();
    ArrayList type_names = new ArrayList();
    ListView Type, Item;
    String getitem_namesId, gettype_namesId;

    private OnFragmentInteractionListener mListener;
    public Boring() {
    }

    public static Boring newInstance(String param1, String param2) {
        Boring fragment = new Boring();
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
        View view = inflater.inflate(R.layout.boring_details, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        calendarss = (EditText) view.findViewById(R.id.calendar);
        block = (EditText) view.findViewById(R.id.block);
        panchayat = (EditText) view.findViewById(R.id.panchayat);
        village = (EditText) view.findViewById(R.id.village);
        skilled = (EditText) view.findViewById(R.id.skilled);

        tola = (EditText) view.findViewById(R.id.tola);
        locationss = (EditText) view.findViewById(R.id.location);
        cashing = (EditText) view.findViewById(R.id.cashing);
        depth = (EditText) view.findViewById(R.id.depth);

        Items();

        locationsss = (Button) view.findViewById(R.id.locationss);
        locationsss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        item=(Button) view.findViewById(R.id.item);
        item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupItem(v);
            }
        });

        type=(Button) view.findViewById(R.id.type);
        type.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupType(v);
            }
        });

        water=(EditText)view.findViewById(R.id.water);
        end_rpm=(EditText)view.findViewById(R.id.end_rpm);
        hour=(EditText)view.findViewById(R.id.hour);

        choose_items=(EditText)view.findViewById(R.id.choose_items);
        typess=(EditText)view.findViewById(R.id.typess);
        diseal=(EditText)view.findViewById(R.id.diseal);

        boring_pipe_qty=(EditText)view.findViewById(R.id.boring_pipe_qty);
        non_skilled=(EditText)view.findViewById(R.id.non_skilled);
        start_rpm=(EditText)view.findViewById(R.id.start_rpm);
        boring_date=(Button)view.findViewById(R.id.boring_date);

        boring_date.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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
                                calendarss.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        submit_boring_report=(Button)view.findViewById(R.id.submit_boring_report);
        submit_boring_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptBoring();
            }
        });

        return view;
    }

    void getLocation() {
            Dexter.withActivity(getActivity())
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            GPS();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            // check for permanent denial of permission
                            if (response.isPermanentlyDenied()) {

                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();


    }
    private void GPS() {


    }

    private void attemptBoring() {
        calendars = calendarss.getText().toString();
        blocks = block.getText().toString();

        panchayats = panchayat.getText().toString();
        villages = village.getText().toString();
        start_rpms=start_rpm.getText().toString();

        skilleds = skilled.getText().toString();
        tolas = tola.getText().toString();
        locations = locationss.getText().toString();

        cashings = cashing.getText().toString();
        depths = depth.getText().toString();
        waters=water.getText().toString();

        end_rpms = end_rpm.getText().toString();
        hours = hour.getText().toString();
        choose_itemss = choose_items.getText().toString();

        typesss = typess.getText().toString();
        diseals = diseal.getText().toString();
        non_skilleds=non_skilled.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(calendars)) {
            calendarss.setError(getString(R.string.error_field_required));
            focusView = calendarss;
            cancel = true;
        }
        if (TextUtils.isEmpty(blocks)) {
            block.setError(getString(R.string.error_field_required));
            focusView = block;
            cancel = true;
        }
        if (TextUtils.isEmpty(panchayats)) {
            panchayat.setError(getString(R.string.error_field_required));
            focusView = panchayat;
            cancel = true;
        }
        if (TextUtils.isEmpty(villages)) {
            village.setError(getString(R.string.error_field_required));
            focusView = village;
            cancel = true;
        }
        if (TextUtils.isEmpty(start_rpms)) {
            start_rpm.setError(getString(R.string.error_field_required));
            focusView = start_rpm;
            cancel = true;
        }
        if (TextUtils.isEmpty(skilleds)) {
            skilled.setError(getString(R.string.error_field_required));
            focusView = skilled;
            cancel = true;
        }
        if (TextUtils.isEmpty(tolas)) {
            tola.setError(getString(R.string.error_field_required));
            focusView = tola;
            cancel = true;
        }
        if (TextUtils.isEmpty(locations)) {
            locationss.setError(getString(R.string.error_field_required));
            focusView = locationss;
            cancel = true;
        }
        if (TextUtils.isEmpty(cashings)) {
            cashing.setError(getString(R.string.error_field_required));
            focusView = cashing;
            cancel = true;
        }
        if (TextUtils.isEmpty(depths)) {
            depth.setError(getString(R.string.error_field_required));
            focusView = depth;
            cancel = true;
        }
        if (TextUtils.isEmpty(waters)) {
            water.setError(getString(R.string.error_field_required));
            focusView = water;
            cancel = true;
        }
        if (TextUtils.isEmpty(end_rpms)) {
            end_rpm.setError(getString(R.string.error_field_required));
            focusView = end_rpm;
            cancel = true;
        }
        if (TextUtils.isEmpty(hours)) {
            hour.setError(getString(R.string.error_field_required));
            focusView = hour;
            cancel = true;
        }
        if (TextUtils.isEmpty(choose_itemss)) {
            choose_items.setError(getString(R.string.error_field_required));
            focusView = choose_items;
            cancel = true;
        }
        if (TextUtils.isEmpty(typesss)) {
            typess.setError(getString(R.string.error_field_required));
            focusView = typess;
            cancel = true;
        }
        if (TextUtils.isEmpty(diseals)) {
            diseal.setError(getString(R.string.error_field_required));
            focusView = diseal;
            cancel = true;
        }
        if (TextUtils.isEmpty(non_skilleds)) {
            non_skilled.setError(getString(R.string.error_field_required));
            focusView = non_skilled;
            cancel = true;
        }
        if (cancel) {

            focusView.requestFocus();

        }else {
            AddBoring();
        }
    }
    private void AddBoring() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADDBORING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                calendarss.setText("");
                                block.setText("");
                                panchayat.setText("");
                                village.setText("");
                                start_rpm.setText("");
                                skilled.setText("");
                                tola.setText("");
                                locationss.setText("");
                                cashing.setText("");
                                depth.setText("");
                                water.setText("");
                                end_rpm.setText("");
                                hour.setText("");
                                choose_items.setText("");
                                typess.setText("");
                                diseal.setText("");
                                non_skilled.setText("");

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
                params.put("block", blocks);
                params.put("panchayat", panchayats);
                params.put("village", villages);
                params.put("tola",tolas);
                params.put("location", locations);
                params.put("casing", cashings);
                params.put("depth", depths);
                params.put("water", waters);
                params.put("start_rpm",start_rpms);
                params.put("end_rpm", end_rpms);
                params.put("hour", hours);
                params.put("item_type_id", gettype_namesId);
                params.put("diesel",diseals);
                params.put("skilled", skilleds);
                params.put("non_skilled", non_skilleds);

                return params;
            }

        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

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

                choose_items.setText(item_names.get(position).toString());
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
                typess.setText(type_names.get(position).toString());
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