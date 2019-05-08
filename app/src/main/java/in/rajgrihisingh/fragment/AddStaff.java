package in.rajgrihisingh.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import in.rajgrihisingh.R;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AddStaff extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    CircleImageView profile_image;
    Button add_img,add_date;

    Intent intent ;
    public  static final int RequestPermissionCode  = 1 ;
    EditText date_of_joining;
    private int mYear, mMonth, mDay;

    RadioGroup sex,type;
    RadioButton skill_type,nonskill_type,m_sex_type,f_sex_type;
    private Staff.OnFragmentInteractionListener mListener;
    EditText staff_names,father_names,staff_age,staff_address;
    EditText staff_city,staff_state,staff_mobile,staff_salary,staff_designation,staff_email;

    String staff_namess,father_namess,staff_ages,staff_addresss,sexs="male",dates;
    String staff_citys,staff_states,staff_mobiles,staff_salarys,staff_designations,image,types="Skilled",staff_emails;
    Button staff_data_save;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    String token;

    public AddStaff() {
    }

    public static AddStaff newInstance(String param1, String param2) {
        AddStaff fragment = new AddStaff();
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
        View view = inflater.inflate(R.layout.staff_information, container, false);

        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        add_img = (Button) view.findViewById(R.id.add_img);
        date_of_joining=(EditText)view.findViewById(R.id.date_of_joining);
        add_date=(Button)view.findViewById(R.id.add_date);
        sex=(RadioGroup)view.findViewById(R.id.check_type);
        type=(RadioGroup)view.findViewById(R.id.sex_type);
        skill_type=(RadioButton) view.findViewById(R.id.skill_type);
        nonskill_type=(RadioButton) view.findViewById(R.id.nonskill_type);
        m_sex_type=(RadioButton) view.findViewById(R.id.m_sex_type);
        f_sex_type=(RadioButton) view.findViewById(R.id.f_sex_type);

        staff_data_save = (Button) view.findViewById(R.id.staff_data_save);
        staff_names= (EditText) view.findViewById(R.id.staff_names);
        father_names= (EditText) view.findViewById(R.id.father_names);
        staff_age= (EditText) view.findViewById(R.id.staff_age);
        staff_address= (EditText) view.findViewById(R.id.staff_address);
        staff_email= (EditText) view.findViewById(R.id.staff_email);

        staff_city= (EditText) view.findViewById(R.id.staff_city);
        staff_state= (EditText) view.findViewById(R.id.staff_state);

        staff_mobile= (EditText) view.findViewById(R.id.staff_mobile);
        staff_salary= (EditText) view.findViewById(R.id.staff_salary);
        staff_designation= (EditText) view.findViewById(R.id.staff_designation);

        add_date.setOnClickListener(this);
        add_img.setOnClickListener(this);
        staff_data_save.setOnClickListener(this);

        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.skill_type:
                        types=  "Skilled";
                        Log.d("s",types);
                        break;
                    case R.id.nonskill_type:
                        types=  "NonSkilled";
                        Log.d("s",types);
                        break;
                }
            }
        });

        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
          public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
        case R.id.m_sex_type:
            sexs = "male";
            Log.d("s",sexs);
        break;
        case R.id.f_sex_type:
            sexs = "female";
            Log.d("s",sexs);
        break;

        }
        }
        });

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token=sp.getString("keyid", "");

        EnableRuntimePermission();
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
        if (v == add_img) {

            intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 7);

        }if(v == add_date){
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            date_of_joining.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            dates=String.valueOf(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
         if(v == staff_data_save) {
            attemptLogin();
        }
    }
    private void attemptLogin() {
        staff_namess = staff_names.getText().toString();
        father_namess = father_names.getText().toString();
        staff_ages = staff_age.getText().toString();
        staff_addresss = staff_address.getText().toString();

        staff_citys = staff_city.getText().toString();
        staff_states = staff_state.getText().toString();

        staff_mobiles = staff_mobile.getText().toString();
        staff_salarys = staff_salary.getText().toString();
        staff_designations = staff_designation.getText().toString();
        staff_emails=staff_email.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(staff_namess)) {
            staff_names.setError(getString(R.string.error_field_required));
            focusView = staff_names;
            cancel = true;
        }
        if (TextUtils.isEmpty(staff_ages)) {
            staff_age.setError(getString(R.string.error_field_required));
            focusView = staff_age;
            cancel = true;
        }
        if (TextUtils.isEmpty(staff_addresss)) {
            staff_address.setError(getString(R.string.error_field_required));
            focusView = staff_address;
            cancel = true;
        }
        if (TextUtils.isEmpty(staff_citys)) {
            staff_city.setError(getString(R.string.error_field_required));
            focusView = staff_city;
            cancel = true;
        }
        if (TextUtils.isEmpty(staff_states)) {
            staff_state.setError(getString(R.string.error_field_required));
            focusView = staff_state;
            cancel = true;
        }
        if (TextUtils.isEmpty(staff_mobiles)) {
            staff_mobile.setError(getString(R.string.error_field_required));
            focusView = staff_mobile;
            cancel = true;
        }
        if (TextUtils.isEmpty(staff_salarys)) {
            staff_salary.setError(getString(R.string.error_field_required));
            focusView = staff_salary;
            cancel = true;
        }
        if (TextUtils.isEmpty(staff_designations)) {
            staff_designation.setError(getString(R.string.error_field_required));
            focusView = staff_designation;
            cancel = true;
        }
        if (cancel) {

            focusView.requestFocus();

        }else {

            AddStaffs();

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
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            image=getStringImage(bitmap);
            profile_image.setImageBitmap(bitmap);
        }
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(), "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    private void AddStaffs() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADDSTAFF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                staff_names.setText("");
                                father_names.setText("");
                                staff_age.setText("");
                                staff_address.setText("");
                                staff_city.setText("");
                                staff_state.setText("");
                                staff_mobile.setText("");
                                staff_salary.setText("");
                                staff_designation.setText("");
                                staff_email.setText("");
                                profile_image.setImageResource(R.drawable.no_image_available);
                                image = "";

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
                params.put("photo", image);
                params.put("type", types);
                params.put("name", staff_namess);
                params.put("father",father_namess);
                params.put("age ", staff_ages);
                params.put("gender", sexs);
                params.put("mobile", staff_mobiles);
                params.put("email", staff_emails);
                params.put("address",staff_addresss);
                params.put("town", staff_citys);
                params.put("district", staff_citys);
                params.put("state", staff_states);
                params.put("doj",dates);
                params.put("salary", staff_salarys);
                params.put("designation", staff_designations);

                return params;
            }

        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }
}
