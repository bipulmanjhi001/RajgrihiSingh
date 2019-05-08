package in.rajgrihisingh.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import in.rajgrihisingh.model.Attandance;
import in.rajgrihisingh.model.VolleySingleton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class Attendance extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private AttandanceTab.OnFragmentInteractionListener mListener;
    ListView loadattendance;
    ProgressBar pr_at_list;
    String token;
    AttendanceAdapter attendanceAdapter;
    ArrayList<Attandance> attandances;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    String date;
    String data="";
    FloatingActionButton floatingActionButton;
    public Attendance() {
    }

    public static Attendance newInstance(String param1, String param2) {
        Attendance fragment = new Attendance();
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
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        loadattendance = (ListView) view.findViewById(R.id.attendance_list);
        loadattendance.setDivider(null);
        pr_at_list = (ProgressBar) view.findViewById(R.id.pr_at_list);
        attandances = new ArrayList<Attandance>();
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.submit_attendance);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!data.isEmpty()) {
                    Submit_Data();
                    ObjectAnimator.ofFloat(floatingActionButton, "rotation", 0f, 360f).setDuration(800).start();
                }else {
                    data="";
                }
            }
        });
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy"+"-"+"MM"+"-"+"dd");
        date = mdformat.format(calendar.getTime());

        CallAttendance();
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
    public class AttendanceAdapter extends BaseAdapter {
        private Context mContext;
        ArrayList<Attandance> mylist = new ArrayList<>();
        JSONObject jObjectData;
        public AttendanceAdapter(ArrayList<Attandance> itemArray, Context mContext) {
            super();
            this.mContext = mContext;
            mylist = itemArray;
        }
        @Override
        public int getCount() {
            return mylist.size();
        }
        @Override
        public String getItem(int position) {
            return mylist.get(position).toString();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            private TextView id,name;
            private CheckBox checkBox;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder view;
            LayoutInflater inflator = null;
            if (convertView == null) {
                view = new ViewHolder();
                try {
                    inflator = ((Activity) mContext).getLayoutInflater();
                    convertView = inflator.inflate(R.layout.attandance_list, null);
                    view.id = (TextView) convertView.findViewById(R.id.attandance_id);
                    view.name = (TextView) convertView.findViewById(R.id.attandance_name);
                    view.checkBox=(CheckBox)convertView.findViewById(R.id.attandance_check);
                    view.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                try {
                                    jObjectData = new JSONObject();
                                    jObjectData.put("id", mylist.get(position).getIda());
                                    jObjectData.put("date", date);
                                } catch (Exception e) {
                                }
                                if (data.length() > 5) {
                                    data = data.concat(jObjectData.toString());
                                    Log.d("data",data);
                                } else {
                                    data = jObjectData.toString();
                                    Log.d("data",data);
                                }
                            }
                        }
                    });
                    convertView.setTag(view);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            try {
                view.id.setTag(position);
                view.id.setText(mylist.get(position).getIda());
                view.name.setText(mylist.get(position).getName());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
    private void CallAttendance(){
        pr_at_list.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETSTAFF+"/"+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray userJson = obj.getJSONArray("stafflist");

                            for(int i=0; i<userJson.length(); i++) {
                                JSONObject itemslist = userJson.getJSONObject(i);
                                String id=itemslist.getString("id");
                                String name = itemslist.getString("name");
                                Attandance attendance = new Attandance(id,name,false);
                                attandances.add(attendance);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            pr_at_list.setVisibility(View.GONE);
                            attendanceAdapter = new AttendanceAdapter(attandances, getActivity());
                            loadattendance.setAdapter(attendanceAdapter);
                            attendanceAdapter.notifyDataSetChanged();
                        }catch (NullPointerException e){
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

    public void Submit_Data() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETATENDANCE,
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
                params.put("token",token);
                params.put("attendance",data);

                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }
}