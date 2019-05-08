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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import in.rajgrihisingh.R;
import in.rajgrihisingh.adapter.ViewAttendanceList;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class AttendanceList extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private AttandanceTab.OnFragmentInteractionListener mListener;
    ListView loadattendance;
    ProgressBar pr_at_list;
    String token;
    ArrayList<ViewAttendanceList> attandances;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    ViewAttendanceAdapter viewAttendanceAdapter;
    FloatingActionButton floatingActionButton;

    public AttendanceList() {

    }
    public static AttendanceList newInstance(String param1, String param2) {
        AttendanceList fragment = new AttendanceList();
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
        View view = inflater.inflate(R.layout.view_attendance_list, container, false);

        loadattendance = (ListView) view.findViewById(R.id.view_attendance_list);
        loadattendance.setDivider(null);
        pr_at_list = (ProgressBar) view.findViewById(R.id.view_attendance_progress);
        attandances = new ArrayList<ViewAttendanceList>();

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.submit_view_attendance_list);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attandances.clear();
                ViewAttendance();
                ObjectAnimator.ofFloat(floatingActionButton, "rotation", 0f, 360f).setDuration(800).start();
            }
        });

        ViewAttendance();
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

    public class ViewAttendanceAdapter extends BaseAdapter {
        private Context mContext;
        ArrayList<ViewAttendanceList> mylist = new ArrayList<>();
        public ViewAttendanceAdapter(ArrayList<ViewAttendanceList> itemArray, Context mContext) {
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
            private TextView id,name,designation;
            private TextView count,mobile;
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
                    convertView = inflator.inflate(R.layout.view_attandance_list, null);

                    view.id = (TextView) convertView.findViewById(R.id.attandance_ids);
                    view.name = (TextView) convertView.findViewById(R.id.attandance_name);
                    view.count=(TextView)convertView.findViewById(R.id.attandance_count);

                    view.designation = (TextView) convertView.findViewById(R.id.attandance_designation);
                    view.mobile=(TextView)convertView.findViewById(R.id.attandance_mobile);

                    convertView.setTag(view);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            try {
                view.id.setTag(position);
                view.id.setText("ID : "+mylist.get(position).getId());
                view.name.setText("Name : "+mylist.get(position).getName());
                view.count.setText("Days Worked : "+mylist.get(position).getCount());
                view.designation.setText("Designation : "+mylist.get(position).getDesignation());
                view.mobile.setText("Mobile : "+mylist.get(position).getMobile());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
    private void ViewAttendance(){
        pr_at_list.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETSTAFFCOUNT+"/"+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("attendancelist");

                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("staff_name");
                                    String mobile=itemslist.getString("mobile");
                                    String designation=itemslist.getString("designation");
                                    String count = itemslist.getString("present");

                                    ViewAttendanceList attendance = new ViewAttendanceList(id, name, count,mobile,designation);
                                    attandances.add(attendance);
                                }
                                try {
                                    pr_at_list.setVisibility(View.GONE);
                                    viewAttendanceAdapter = new ViewAttendanceAdapter(attandances, getActivity());
                                    loadattendance.setAdapter(viewAttendanceAdapter);
                                    viewAttendanceAdapter.notifyDataSetChanged();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Toast.makeText(getActivity(), obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                            } catch(JSONException e){
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
}
