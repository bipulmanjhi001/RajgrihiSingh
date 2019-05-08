package in.rajgrihisingh.tablayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import in.rajgrihisingh.R;
import in.rajgrihisingh.signature.FinalSignature;

public class Man_Power extends Fragment  {

    private Button laying_vendor_sign,authorized_sign;
    EditText skilled_employee,non_skilled_employee;
    String skilled_employees,non_skilled_employees,sign;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.man_power, container, false);

        authorized_sign=(Button)rootView.findViewById(R.id.authorized_sign);
        laying_vendor_sign=(Button)rootView.findViewById(R.id.laying_vendor_sign);
        non_skilled_employee=(EditText) rootView.findViewById(R.id.non_skilled_employee);
        skilled_employee=(EditText) rootView.findViewById(R.id.skilled_employee);

        authorized_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign="auth_sign";
                skilled_employees = skilled_employee.getText().toString();
                non_skilled_employees = non_skilled_employee.getText().toString();

                if(!skilled_employees.isEmpty() && !non_skilled_employees.isEmpty()) {

                    Intent intent = new Intent(getActivity(), FinalSignature.class);
                    intent.putExtra("sign_name", sign);
                    intent.putExtra("skilled",skilled_employees);
                    intent.putExtra("non_skilled",non_skilled_employees);
                    startActivity(intent);

                    skilled_employee.setText("");
                    non_skilled_employee.setText("");

                }else {
                    attemptLogin();
                }

            }
        });

        laying_vendor_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign="vendor_sign";
                skilled_employees = skilled_employee.getText().toString();
                non_skilled_employees = non_skilled_employee.getText().toString();

                if(!skilled_employees.isEmpty() && !non_skilled_employees.isEmpty()) {
                    Intent intent = new Intent(getActivity(), FinalSignature.class);
                    intent.putExtra("sign_name", sign);
                    intent.putExtra("skilled",skilled_employees);
                    intent.putExtra("non_skilled",non_skilled_employees);
                    startActivity(intent);

                }else {
                    attemptLogin();
                }
            }
        });

        return rootView;
    }
    private void attemptLogin() {

        skilled_employees = skilled_employee.getText().toString();
        non_skilled_employees = non_skilled_employee.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(skilled_employees)) {
            skilled_employee.setError(getString(R.string.error_field_required));
            focusView = skilled_employee;
            cancel = true;
        }
        if (TextUtils.isEmpty(non_skilled_employees)) {
            non_skilled_employee.setError(getString(R.string.error_field_required));
            focusView = non_skilled_employee;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }
    }
}