package in.rajgrihisingh.fragment;


import android.app.Dialog;
import android.content.Context;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class PaidSalary extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    EditText employee_name,from_date,to_date,pay_date,pay_method;
    EditText other_amt,con_amt;

    String employee_names,from_dates,to_dates,pay_dates,pay_methods;
    String  basic_salarys,other_amts,con_amts,total_incs,total_deds,net_pays;
    String av_salarys,ded_amts;
    String token,getId;

    Button employee_name_click,add_salary,get_type,get_calc;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";

    ArrayList item_types = new ArrayList();
    ArrayList ids = new ArrayList();
    ListView items;
    ArrayList types = new ArrayList();

    TextView basic_salary,ded_amt,net_pay,av_salary,total_inc,total_ded;
    Integer n1=0,total_income=0,total_deded=0,net_payeds=0;

    private OnFragmentInteractionListener mListener;

    public PaidSalary() {
    }

    public static PaidSalary newInstance(String param1, String param2) {
        PaidSalary fragment = new PaidSalary();
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
        View view= inflater.inflate(R.layout.paid_salary, container, false);

        employee_name=(EditText)view.findViewById(R.id.employee_name);
        from_date=(EditText)view.findViewById(R.id.from_date);
        to_date=(EditText)view.findViewById(R.id.to_date);

        pay_date=(EditText)view.findViewById(R.id.pay_date);
        pay_method=(EditText)view.findViewById(R.id.pay_method);

        basic_salary=(TextView)view.findViewById(R.id.basic_salary);
        get_type=(Button) view.findViewById(R.id.get_type);
        get_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                types.clear();
                showPopupType(v);
            }
        });
        ded_amt=(TextView)view.findViewById(R.id.ded_amt);
        other_amt=(EditText)view.findViewById(R.id.other_amt);
        con_amt=(EditText)view.findViewById(R.id.con_amt);
        total_inc=(TextView)view.findViewById(R.id.total_inc);
        total_ded=(TextView)view.findViewById(R.id.total_ded);
        net_pay=(TextView)view.findViewById(R.id.net_pay);
        av_salary=(TextView)view.findViewById(R.id.av_salary);
        get_calc=(Button)view.findViewById(R.id.get_calc);
        get_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckDetails();
            }
        });
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        pay_date.setText(thisDate);

        CallValue();

        employee_name_click=(Button) view.findViewById(R.id.employee_name_click);
        employee_name_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupClassType(v);
            }
        });

        add_salary=(Button)view.findViewById(R.id.add_salary);
        add_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSalary();
            }
        });

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token=sp.getString("keyid", "");

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

    private void Calculate(){

    }
    private void CheckDetails() {
        other_amts = other_amt.getText().toString();
        con_amts = con_amt.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(other_amts)) {
            other_amt.setError(getString(R.string.error_field_required));
            focusView = other_amt;
            cancel = true;
        }
        if (TextUtils.isEmpty(con_amts)) {
            con_amt.setError(getString(R.string.error_field_required));
            focusView = con_amt;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }else {
            try {
                n1 = Integer.parseInt(basic_salarys);
                total_income = n1 + Integer.parseInt(other_amts);
                total_deded = Integer.parseInt(con_amts) + Integer.parseInt(ded_amts);
                net_payeds = total_income - total_deded;
                net_pay.setText(String.valueOf(net_payeds));
                total_ded.setText(String.valueOf(total_deded));
                total_inc.setText(String.valueOf(total_income));
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getActivity(), "Check details again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void attemptSalary() {
        employee_names = employee_name.getText().toString();
        from_dates = from_date.getText().toString();
        to_dates = to_date.getText().toString();
        pay_dates = pay_date.getText().toString();

        pay_methods = pay_method.getText().toString();
        basic_salarys = basic_salary.getText().toString();
        ded_amts = ded_amt.getText().toString();

        total_incs = total_inc.getText().toString();
        total_deds = total_ded.getText().toString();
        net_pays=net_pay.getText().toString();
        av_salarys=av_salary.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(employee_names)) {
            employee_name.setError(getString(R.string.error_field_required));
            focusView = employee_name;
            cancel = true;
        }
        if (TextUtils.isEmpty(pay_dates)) {
            pay_date.setError(getString(R.string.error_field_required));
            focusView = pay_date;
            cancel = true;
        }
        if (TextUtils.isEmpty(pay_methods)) {
            pay_method.setError(getString(R.string.error_field_required));
            focusView = pay_method;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }else {
            AddSalary();
        }
    }
    private void GetSalaryDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETSALARYDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONObject userJson = obj.getJSONObject("salarydetails");

                                    from_date.setText(userJson.getString("date_from"));
                                    to_date.setText(userJson.getString("date_to"));
                                    basic_salary.setText(userJson.getString("basic"));
                                    basic_salarys=userJson.getString("basic");
                                    ded_amt.setText(userJson.getString("deductions"));
                                    ded_amts=userJson.getString("deductions");
                                    av_salary.setText(userJson.getString("days"));

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
                params.put("staff_id", getId);

                return params;
            }

        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void AddSalary() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADDSALARY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                employee_name.setText("");
                                from_date.setText("");
                                to_date.setText("");
                                pay_date.setText("");
                                basic_salary.setText("");
                                total_inc.setText("");
                                net_pay.setText("");
                                av_salary.setText("");
                                ded_amt.setText("");
                                other_amt.setText("");
                                pay_method.setText("");
                                con_amt.setText("");
                                total_ded.setText("");

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
                params.put("staff_id", getId);
                params.put("date_from", from_dates);
                params.put("date_to", to_dates);

                params.put("date",pay_dates);
                params.put("basic", basic_salarys);
                params.put("total_income", total_incs);

                params.put("net_payment", net_pays);
                params.put("deductions", ded_amts);
                params.put("other_income",other_amts);

                params.put("payment_mode", pay_methods);
                params.put("contributions", con_amts);
                params.put("total_deduction", total_deds);
                params.put("advance","0");

                return params;
            }

        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void showPopupClassType(View view) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.list_dialog);
        dialog.setTitle("List..");
        items= (ListView) dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, item_types);
        items.setAdapter(adapter);
        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                employee_name.setText(item_types.get(position).toString());
                getId=ids.get(position).toString();
                GetSalaryDetails();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showPopupType(View view) {
        types.add("Cash");
        types.add("Bank Transfers");
        types.add("Card");
        types.add("Cheque");
        types.add("Mobile Payments");

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.list_dialog);
        dialog.setTitle("List..");
        items= (ListView) dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, types);
        items.setAdapter(adapter);
        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pay_method.setText(types.get(position).toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void CallValue(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETSTAFF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d("response",response);
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

}
