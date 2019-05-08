package in.rajgrihisingh.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import in.rajgrihisingh.R;
import in.rajgrihisingh.model.SalaryList;
import java.util.ArrayList;

public class ViewPaidAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<SalaryList> mylist = new ArrayList<>();

    public ViewPaidAdapter(ArrayList<SalaryList> itemArray, Context mContext) {
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
        private TextView paid_id, paid_date, paid_staff_name,paid_amount;
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
                convertView = inflator.inflate(R.layout.view_paid_adapter, null);
                view.paid_id = (TextView) convertView.findViewById(R.id.paid_id);
                view.paid_date = (TextView) convertView.findViewById(R.id.paid_date);
                view.paid_staff_name = (TextView) convertView.findViewById(R.id.paid_staff_name);
                view.paid_amount = (TextView) convertView.findViewById(R.id.paid_amount);

                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.paid_id.setTag(position);

            view.paid_id.setText(mylist.get(position).getId());
            view.paid_date.setText("Date : "+mylist.get(position).getDate());
            view.paid_staff_name.setText("Name : "+mylist.get(position).getStaff_name());
            view.paid_amount.setText("Amount : "+mylist.get(position).getAmount());


        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}