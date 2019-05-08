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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import in.rajgrihisingh.R;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;
import in.rajgrihisingh.signature.Labour_Sign;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ExpensesDetails extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    Button add_expenses,click_bill,add_expenses_item_names,add_expenses_date,add_sign;
    EditText expense_date,expense_item_name;
    EditText bill_name,particular_name,bill_amount,bill_now;
    private int mYear, mMonth, mDay;
    LinearLayout visible_bill;
    ImageView imageView ;
    Intent intent;

    public  static final int RequestPermissionCode  = 1 ;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    String name,token,getId,bill_nos;
    String expense_dates,expense_item_names,bill_names,particular_names,bill_amounts;
    ArrayList ids = new ArrayList();
    ListView items;
    ArrayList expense_heads = new ArrayList();
    Bitmap bitmap;
    String image="";
    private Expenses.OnFragmentInteractionListener mListener;
    public ExpensesDetails() {
    }

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        View view = inflater.inflate(R.layout.expenses_details, container, false);

        add_expenses=(Button)view.findViewById(R.id.add_expenses);
        click_bill=(Button)view.findViewById(R.id.click_bill);
        expense_date=(EditText)view.findViewById(R.id.expense_date);

        bill_now=(EditText)view.findViewById(R.id.bill_no);
        expense_item_name=(EditText)view.findViewById(R.id.expense_item_name);
        bill_name=(EditText)view.findViewById(R.id.bill_name);
        particular_name=(EditText)view.findViewById(R.id.particular_name);
        bill_amount=(EditText)view.findViewById(R.id.bill_amount);
        visible_bill=(LinearLayout)view.findViewById(R.id.visible_bill);
        imageView=(ImageView)view.findViewById(R.id.show_img);
        add_expenses_date=(Button)view.findViewById(R.id.add_expenses_date);


        EnableRuntimePermission();
        click_bill.setOnClickListener(this);
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        name = sp.getString("keyusername", "");
        token=sp.getString("keyid", "");

        CallValue();
        add_expenses_item_names=(Button)view.findViewById(R.id.add_expenses_item_name);
        add_expenses_item_names.setOnClickListener(this);
        add_expenses.setOnClickListener(this);
        add_expenses_date.setOnClickListener(this);

        add_sign=(Button)view.findViewById(R.id.add_sign);
        add_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Labour_Sign.class);
                startActivity(intent);
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

    @Override
    public void onClick(View v) {
        if (v == add_expenses_date) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            expense_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if(v == click_bill){
            intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 7);
        }

        if(v == add_expenses_item_names){
            showPopupClassType(v);
        }

        if(v == add_expenses){
            attemptLogin();
        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7 && resultCode == RESULT_OK) {
            visible_bill.setVisibility(View.VISIBLE);
            bitmap = (Bitmap) data.getExtras().get("data");
            image = getStringImage(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA))
        {
            Toast.makeText(getActivity(),"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(),"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void showPopupClassType(View view) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.list_dialog);
        items= (ListView) dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, expense_heads);
        items.setAdapter(adapter);
        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expense_item_name.setText(expense_heads.get(position).toString());
                getId=ids.get(position).toString();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void attemptLogin() {
        expense_dates = expense_date.getText().toString();
        expense_item_names = expense_item_name.getText().toString();
        bill_names = bill_name.getText().toString();
        particular_names = particular_name.getText().toString();
        bill_amounts = bill_amount.getText().toString();
        bill_nos = bill_now.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(expense_dates)) {
            expense_date.setError(getString(R.string.error_field_required));
            focusView = expense_date;
            cancel = true;
        }
        if (TextUtils.isEmpty(expense_item_names)) {
            expense_item_name.setError(getString(R.string.error_field_required));
            focusView = expense_item_name;
            cancel = true;
        }
        if (TextUtils.isEmpty(bill_names)) {
            bill_name.setError(getString(R.string.error_field_required));
            focusView = bill_name;
            cancel = true;
        }
        if (TextUtils.isEmpty(bill_amounts)) {
            bill_amount.setError(getString(R.string.error_field_required));
            focusView = bill_amount;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();

        }else {
            AddExpenses();
        }
    }

    public void CallValue(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_EXPENDHEAD+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray userJson = obj.getJSONArray("expense_heads");

                            for(int i=0; i<userJson.length(); i++) {

                                JSONObject itemslist = userJson.getJSONObject(i);
                                String id = itemslist.getString("id");
                                ids.add(id);
                                String expense_head = itemslist.getString("expense_head");
                                expense_heads.add(expense_head);

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

    private void AddExpenses(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADDEXPENSE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                Toast.makeText(getActivity(), obj.getString("message"),Toast.LENGTH_SHORT).show();

                                bill_name.setText("");
                                particular_name.setText("");
                                bill_amount.setText("");
                                bill_now.setText("");
                                expense_date.setText("");
                                expense_item_name.setText("");
                                visible_bill.setVisibility(View.GONE);
                                image="";

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
                params.put("token", token);
                params.put("expense_head_id", getId);
                params.put("date", expense_dates);
                params.put("bill_no", bill_nos);
                params.put("image",image);
                params.put("name", bill_names);
                params.put("remarks", particular_names);
                params.put("amount", bill_amounts);

                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
