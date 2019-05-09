package in.rajgrihisingh.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import in.rajgrihisingh.R;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;
import in.rajgrihisingh.signature.Labour_Sign;

import static android.content.Context.MODE_PRIVATE;

public class Labour_Payment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    private static final String SHARED_PREF_NAME2 = "LabourSigns";
    String token,getId,labour_dates,labour_names,labour_amounts,labour_remarks,dates;
    EditText labour_date,labour_name,labour_amount,labour_remark;
    Button pay_date_click,labour_name_click,labour_date_click;

    ArrayList item_types = new ArrayList();
    ArrayList ids = new ArrayList();
    ListView items;
    ArrayList types = new ArrayList();
    private int mYear, mMonth, mDay;
    Button add_sign;
    String sign="";
    private OnFragmentInteractionListener mListener;

    public Labour_Payment() {
    }

    // TODO: Rename and change types and number of parameters
    public static Labour_Payment newInstance(String param1, String param2) {
        Labour_Payment fragment = new Labour_Payment();
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
        View rootView = inflater.inflate(R.layout.labour_payment, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token=sp.getString("keyid", "");

        SharedPreferences ls = getActivity().getSharedPreferences("Labour_Signs", MODE_PRIVATE);
        sign=ls.getString("Labour_Sign", "");
        Log.d("sign", sign);

        labour_date=(EditText)rootView.findViewById(R.id.labour_dates);
        labour_name=(EditText)rootView.findViewById(R.id.labour_names);
        labour_amount=(EditText)rootView.findViewById(R.id.labour_amounts);
        labour_remark=(EditText)rootView.findViewById(R.id.labour_remarks);
        labour_date_click=(Button)rootView.findViewById(R.id.labour_date_click);
        labour_date_click.setOnClickListener(new View.OnClickListener() {
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
                                labour_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                dates=String.valueOf(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        add_sign=(Button)rootView.findViewById(R.id.add_sign);

        CallValue();
        pay_date_click=(Button)rootView.findViewById(R.id.pay_date_click);
        labour_name_click=(Button)rootView.findViewById(R.id.labour_name_click);
        labour_name_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupClassType(v);
            }
        });

        pay_date_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSalary();
            }
        });
        add_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Labour_Sign.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void attemptSalary() {
        labour_dates = labour_date.getText().toString();
        labour_names = labour_name.getText().toString();
        labour_amounts = labour_amount.getText().toString();
        labour_remarks = labour_remark.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(labour_dates)) {
            labour_date.setError(getString(R.string.error_field_required));
            focusView = labour_date;
            cancel = true;
        }
        if (TextUtils.isEmpty(labour_names)) {
            labour_name.setError(getString(R.string.error_field_required));
            focusView = labour_name;
            cancel = true;
        }
        if (TextUtils.isEmpty(labour_amounts)) {
            labour_amount.setError(getString(R.string.error_field_required));
            focusView = labour_amount;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }else {
            AddLabourPayment();
        }
    }
    private void AddLabourPayment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LABOURPAYMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                labour_date.setText("");
                                labour_name.setText("");
                                labour_amount.setText("");
                                labour_remark.setText("");
                                sign="";
                                SharedPreferences settings = getActivity().getSharedPreferences("Labour_Sign", MODE_PRIVATE);
                                settings.edit().clear().apply();

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
                params.put("date", labour_dates);
                params.put("staff_id", getId);
                params.put("amount", labour_amounts);
                params.put("remarks", labour_remarks);
                params.put("sign", sign);

                return params;
            }

        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    public void CallValue(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETSTAFF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("stafflist");
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    ids.add(id);
                                    String name = itemslist.getString("name");
                                    item_types.add(name);
                                }
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
                params.put("token",token);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    private void showPopupClassType(View view) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.list_dialog);
        items= (ListView) dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, item_types);
        items.setAdapter(adapter);
        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                labour_name.setText(item_types.get(position).toString());
                getId=ids.get(position).toString();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}