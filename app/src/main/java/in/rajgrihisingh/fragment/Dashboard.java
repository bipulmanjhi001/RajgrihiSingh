package in.rajgrihisingh.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.PieChart;
import in.rajgrihisingh.R;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Dashboard extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    int Transfer,Expense,Balance;
    String token;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    PieChart mChart;
    ArrayList<Integer> arrayList=new ArrayList<Integer>();
    String[] xValues = {"Transfer", "Balance", "Expense"};

    // colors for different sections in pieChart
    public static  final int[] MY_COLORS = {
            Color.rgb(13,125,213),
            Color.rgb(33,150,243),

            Color.rgb(242,4,4),

            Color.rgb(66,133,244),
            Color.rgb(215,60,55)
    };
    private OnFragmentInteractionListener mListener;

    public Dashboard() {
    }

    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_dashboard, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        Chart();
        mChart = (PieChart)view.findViewById(R.id.chart);



        return view;
    }
    public void setDataForPieChart() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < arrayList.size(); i++)
            yVals1.add(new Entry(arrayList.get(i), i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i]);
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2);
        dataSet.setSelectionShift(5);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : MY_COLORS)
            colors.add(c);
        dataSet.setColors(colors);
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();
        mChart.animateXY(1400, 1400);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }


    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value) + "";
        }
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

    public void Chart(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ACCOUNTDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {

                                    JSONObject userJson = obj.getJSONObject("account");
                                    Transfer = Integer.parseInt(userJson.getString("transfer"));
                                    Expense = Integer.parseInt(userJson.getString("expense"));
                                    Balance = Integer.parseInt(userJson.getString("balance"));
                                    arrayList.add(Transfer);
                                    arrayList.add(Balance);
                                    arrayList.add(Expense);
                            }else {
                                Toast.makeText(getActivity(),"Chart empty",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mChart.setDescription("");
                        mChart.setRotationEnabled(true);
                        setDataForPieChart();
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
                return params;
            }

        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
