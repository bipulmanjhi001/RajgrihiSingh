package in.rajgrihisingh.tablayout;

import android.app.Dialog;
import android.content.SharedPreferences;
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
import in.rajgrihisingh.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static android.content.Context.MODE_PRIVATE;

public class Laying_Details extends Fragment {

    private CalendarView laying_calendar;
    private TextView laying_date,di_laying_name;
    private Button di_class,submit_laying_report,di_class_select;
    private EditText zone,locations,nodes,di_class_type,laying_type;
    String zones,locationss,nodesss,di_class_types,laying_types;
    String date,token;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";

    ArrayList type_idss = new ArrayList();
    ArrayList type_namess = new ArrayList();
    ListView Type,Item;
    ArrayList item_idss = new ArrayList();
    ArrayList item_namess= new ArrayList();
    String getitem_namesId,gettype_namesId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.laying_report, container, false);

        laying_calendar=(CalendarView)rootView.findViewById(R.id.laying_calendar);
        laying_date=(TextView)rootView.findViewById(R.id.laying_date);
        di_class=(Button)rootView.findViewById(R.id.di_class_item);
        zone=(EditText)rootView.findViewById(R.id.zone);
        locations=(EditText)rootView.findViewById(R.id.locations);
        nodes=(EditText)rootView.findViewById(R.id.nodes);
        di_class_type=(EditText)rootView.findViewById(R.id.di_class_type);
        laying_type=(EditText)rootView.findViewById(R.id.laying_type);
        di_laying_name=(TextView)rootView.findViewById(R.id.di_laying_name);
        submit_laying_report=(Button)rootView.findViewById(R.id.submit_laying_report);
        di_class_select=(Button)rootView.findViewById(R.id.di_class_types);
        Call();
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token=sp.getString("keyid", "");
        Items();

        return rootView;
    }

    private void Call(){
        laying_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date=String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                laying_date.setText("Laying Date : "+date);
                    }
                  });

        di_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupItem(v);
            }
        });
        di_class_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupType(v);
                    }
                });

        submit_laying_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void showPopupItem(View view) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.list_dialog);
        Item= (ListView) dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, item_namess);
        Item.setAdapter(adapter);
        Item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                di_laying_name.setText("Item : "+item_namess.get(position).toString());
                getitem_namesId=item_idss.get(position).toString();
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
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, type_namess);
        Type.setAdapter(adapter);
        Type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                di_class_type.setText(type_namess.get(position).toString());
                gettype_namesId=type_idss.get(position).toString();
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
                                    item_namess.add(item_type);
                                    String item_id = itemslist.getString("id");
                                    item_idss.add(item_id);

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
        type_namess.clear();
        type_idss.clear();
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
                                    type_idss.add(item_id);
                                    String item_type = itemslist.getString("item_type");
                                    type_namess.add(item_type);

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
    private void attemptLogin() {
        zones = zone.getText().toString();
        locationss = locations.getText().toString();
        nodesss = nodes.getText().toString();
        di_class_types = di_class_type.getText().toString();
        laying_types=laying_type.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(locationss)) {
            locations.setError(getString(R.string.error_field_required));
            focusView = locations;
            cancel = true;
        }
        if (TextUtils.isEmpty(di_class_types)) {
            di_class_type.setError(getString(R.string.error_field_required));
            focusView = di_class_type;
            cancel = true;
        }
        if (TextUtils.isEmpty(laying_types)) {
            laying_type.setError(getString(R.string.error_field_required));
            focusView = laying_type;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }else {
            Authenticate();
        }
    }
    public void Authenticate(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD_LAYING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                Toast.makeText(getActivity(), obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Error",Toast.LENGTH_SHORT).show();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("date", date);
                params.put("zone", zones);
                params.put("location", locationss);
                params.put("node_to_node", nodesss);
                params.put("item_type_id",gettype_namesId);
                params.put("laying", laying_types);
                params.put("token", token);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        zone.setText("");
        locations.setText("");
        nodes.setText("");
        di_class_type.setText("");
        laying_type.setText("");
    }
}
