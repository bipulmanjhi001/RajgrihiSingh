package in.rajgrihisingh.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import in.rajgrihisingh.R;
import in.rajgrihisingh.model.StaffList;
import java.util.ArrayList;

public class StaffAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<StaffList> mylist = new ArrayList<>();

    public StaffAdapter(ArrayList<StaffList> itemArray, Context mContext) {
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
        private TextView id,mobile, address,name,designation;
        private ImageView image;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final StaffAdapter.ViewHolder view;
        LayoutInflater inflator = null;
        if (convertView == null) {
            view = new StaffAdapter.ViewHolder();
            try {

                inflator = ((Activity) mContext).getLayoutInflater();
                convertView = inflator.inflate(R.layout.staff_show_list, null);
                view.id = (TextView) convertView.findViewById(R.id.staff_id);
                view.mobile = (TextView) convertView.findViewById(R.id.staff_mobile);
                view.address = (TextView) convertView.findViewById(R.id.staff_address);
                view.name = (TextView) convertView.findViewById(R.id.staff_name);
                view.designation = (TextView) convertView.findViewById(R.id.staff_designation);
                view.image = (ImageView)convertView.findViewById(R.id.staff_image);

                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.id.setTag(position);
            view.mobile.setText("Mobile : "+mylist.get(position).getMobile());
            view.address.setText("Address : "+mylist.get(position).getAddress());
            view.name.setText("Name : "+mylist.get(position).getName());
            view.designation.setText("Designation : "+mylist.get(position).getDesignation());


        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
