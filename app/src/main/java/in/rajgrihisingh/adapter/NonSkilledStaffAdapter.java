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
import in.rajgrihisingh.model.NonSkilledList;

import java.util.ArrayList;

public class NonSkilledStaffAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<NonSkilledList> mylist = new ArrayList<>();

    public NonSkilledStaffAdapter(ArrayList<NonSkilledList> itemArray, Context mContext) {
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
        final ViewHolder view;
        LayoutInflater inflator = null;
        if (convertView == null) {
            view = new ViewHolder();
            try {

                inflator = ((Activity) mContext).getLayoutInflater();
                convertView = inflator.inflate(R.layout.non_skilledstafflist, null);
                view.id = (TextView) convertView.findViewById(R.id.nonstaff_id);
                view.mobile = (TextView) convertView.findViewById(R.id.nonstaff_mobile);
                view.address = (TextView) convertView.findViewById(R.id.nonstaff_address);
                view.name = (TextView) convertView.findViewById(R.id.nonstaff_name);
                view.designation = (TextView) convertView.findViewById(R.id.nonstaff_designation);
                view.image = (ImageView)convertView.findViewById(R.id.nonstaff_image);

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

