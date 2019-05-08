package in.rajgrihisingh.tablayout;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import in.rajgrihisingh.R;
import in.rajgrihisingh.adapter.Fitting_Band;
import in.rajgrihisingh.adapter.Siuice_Valve;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.interfaces.AdapterCallback;
import in.rajgrihisingh.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class Special_Fittings extends Fragment implements AdapterCallback {
    String token;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    ArrayList ids;
    ArrayList item_types;
    ArrayList item_ids;
    ArrayList item_names;

    ArrayList ids2;
    ArrayList item_types2;
    ArrayList item_ids2;
    ArrayList item_names2;

    ListView special_fitting_list, special_fitting_list2;
    ArrayList<Fitting_Band> fitting_bands;
    ArrayList<Siuice_Valve> siuice_valves;
    ProgressBar progressBar;
    Button submit_fitting;
    TextView f_text, s_text;
    FittingAdapter adapter;
    SiuiceAdapter adapters;
    String siuice_data="", fitting_data="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.special_fittings, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");
        CallValue();
        special_fitting_list = (ListView) rootView.findViewById(R.id.special_fitting_list);
        special_fitting_list2 = (ListView) rootView.findViewById(R.id.special_fitting_list2);
        special_fitting_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string = adapter.getItem(position);
                Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
            }
        });

        progressBar = (ProgressBar) rootView.findViewById(R.id.loader);
        s_text = (TextView) rootView.findViewById(R.id.s_text);
        f_text = (TextView) rootView.findViewById(R.id.f_text);

        ids = new ArrayList();
        item_types = new ArrayList();
        item_ids = new ArrayList();
        item_names = new ArrayList();
        ids2 = new ArrayList();
        item_types2 = new ArrayList();
        item_ids2 = new ArrayList();
        item_names2 = new ArrayList();
        siuice_valves = new ArrayList<Siuice_Valve>();
        fitting_bands = new ArrayList<Fitting_Band>();

        submit_fitting = (Button) rootView.findViewById(R.id.submit_fitting);
        submit_fitting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetAll();
            }
        });

        return rootView;
    }

    public void CallValue() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GET_SPECIALFITTING + "/" + token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray userJson = obj.getJSONArray("special_fitting");

                            for (int i = 0; i < userJson.length(); i++) {

                                JSONObject itemslist = userJson.getJSONObject(i);
                                String id = itemslist.getString("id");
                                ids.add(id);
                                String item_type = itemslist.getString("item_type");
                                item_types.add(item_type);
                                String item_id = itemslist.getString("item_id");
                                item_ids.add(item_id);
                                String item_name = itemslist.getString("item_name");
                                item_names.add(item_name);

                                Fitting_Band fitting_band = new Fitting_Band(id, item_type, false);
                                fitting_bands.add(fitting_band);

                            }
                            JSONArray valveJson = obj.getJSONArray("siuice_valve");
                            for (int i = 0; i < valveJson.length(); i++) {

                                JSONObject itemslist = valveJson.getJSONObject(i);
                                String id = itemslist.getString("id");
                                ids2.add(id);
                                String item_type = itemslist.getString("item_type");
                                item_types2.add(item_type);
                                String item_id = itemslist.getString("item_id");
                                item_ids2.add(item_id);
                                String item_name = itemslist.getString("item_name");
                                item_names2.add(item_name);

                                Siuice_Valve siuice_valve = new Siuice_Valve(id, item_type, false);
                                siuice_valves.add(siuice_valve);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressBar.setVisibility(View.GONE);
                        s_text.setVisibility(View.VISIBLE);
                        adapter = new FittingAdapter(fitting_bands, getActivity());
                        special_fitting_list.setAdapter(adapter);

                        f_text.setVisibility(View.VISIBLE);
                        adapters = new SiuiceAdapter(siuice_valves, getActivity());
                        special_fitting_list2.setAdapter(adapters);

                        ListUtils.setDynamicHeight(special_fitting_list);
                        ListUtils.setDynamicHeight(special_fitting_list2);
                        submit_fitting.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onMethodCallback(String s) {
        Log.d("S", s);
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    public void GetAll() {
        Submit_Data();
    }
    public class FittingAdapter extends BaseAdapter {
        private Context mContext;
        ArrayList<Fitting_Band> mylist = new ArrayList<>();
        String valueList;
        JSONObject jObjectData;

        public FittingAdapter(ArrayList<Fitting_Band> itemArray, Context mContext) {
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
            public EditText fitting_num;
            public CheckBox tick;
            public TextView get_id;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder view;
            LayoutInflater inflator = null;
            if (convertView == null) {
                view = new ViewHolder();
           try{
                inflator=((Activity) mContext).getLayoutInflater();
                convertView = inflator.inflate(R.layout.fitting_list, null);
                view.fitting_num = (EditText) convertView.findViewById(R.id.fitting_number);
                view.get_id = (TextView) convertView.findViewById(R.id.get_id);
                view.tick = (CheckBox) convertView.findViewById(R.id.fitting_check);
                view.tick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int getPosition = (Integer) buttonView.getTag();
                        mylist.get(getPosition).setChecked(buttonView.isChecked());
                        if (isChecked) {
                            try {
                                jObjectData = new JSONObject();
                                jObjectData.put("id", mylist.get(position).getId());
                                jObjectData.put("value", valueList);
                            } catch (Exception e) {

                            }
                            if (fitting_data.length() > 5) {
                                fitting_data = fitting_data.concat(jObjectData.toString());
                            } else {
                                fitting_data = jObjectData.toString();
                            }
                        } else {

                        }
                    }
                });
                convertView.setTag(view);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            try {
                view.tick.setTag(position);
                view.fitting_num.setTag(position);
                view.tick.setText(mylist.get(position).getType());
                view.get_id.setText(mylist.get(position).getId());
                view.tick.setChecked(mylist.get(position).isChecked());
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            view.fitting_num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    valueList = s.toString();
                }
            });

            return convertView;
        }
    }
    public class SiuiceAdapter extends BaseAdapter {

        private Context mContext;
        ArrayList<Siuice_Valve> mylists = new ArrayList<>();
        String valueList2;
        JSONObject jObjectData;

        public SiuiceAdapter(ArrayList<Siuice_Valve> itemArray, Context mContext) {
            super();
            this.mContext = mContext;
            mylists = itemArray;
        }

        @Override
        public int getCount() {
            return mylists.size();
        }

        @Override
        public String getItem(int position) {
            return mylists.get(position).toString();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            public EditText fitting_num;
            public CheckBox tick;
            public TextView get_id;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder view = null;
            LayoutInflater inflator = null;

            if (convertView == null) {
                view = new ViewHolder();
                try{
                 inflator=((Activity) mContext).getLayoutInflater();
                convertView = inflator.inflate(R.layout.fitting_list, null);
                view.fitting_num = (EditText) convertView.findViewById(R.id.fitting_number);
                view.get_id = (TextView) convertView.findViewById(R.id.get_id);
                view.tick = (CheckBox) convertView.findViewById(R.id.fitting_check);
                view.tick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int getPosition = (Integer) buttonView.getTag();
                        mylists.get(getPosition).setChecked(buttonView.isChecked());

                        if (isChecked) {
                            try {
                                jObjectData = new JSONObject();
                                jObjectData.put("id", mylists.get(position).getId());
                                jObjectData.put("value", valueList2);
                            } catch (Exception e) {
                            }
                            if (siuice_data.length() > 5) {
                                siuice_data = siuice_data.concat(jObjectData.toString());
                            } else {
                                siuice_data = jObjectData.toString();
                            }
                        }
                    }
                });
                convertView.setTag(view);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            try{
            view.tick.setTag(position);
            view.tick.setText(mylists.get(position).getType());
            view.get_id.setText(mylists.get(position).getId());
            view.tick.setChecked(mylists.get(position).isChecked());
           }catch (NullPointerException e){
            e.printStackTrace();
           }
            view.fitting_num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    valueList2 = s.toString();
                }
            });
            return convertView;
        }
    }
    public void Submit_Data() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD_SPECIALFITTING,
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
                params.put("token", token);
                params.put("special_fittings", fitting_data);
                params.put("siuice_valve", siuice_data);

                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
