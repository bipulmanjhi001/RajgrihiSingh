package in.rajgrihisingh.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import in.rajgrihisingh.R;
import in.rajgrihisingh.model.ViewPurchaseList;
import java.util.ArrayList;

public class ViewPurchaseAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ViewPurchaseList> mylist = new ArrayList<>();

    public ViewPurchaseAdapter(ArrayList<ViewPurchaseList> itemArray, Context mContext) {
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

        private TextView purchase_id,gross_amount,invoice_no,supplier_id;
        private TextView purchase_date,total_amount,supplier_name,site_name;

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
                convertView = inflator.inflate(R.layout.view_purchase_list_row, null);

                view.purchase_id = (TextView) convertView.findViewById(R.id.purchase_id);
                view.purchase_date = (TextView) convertView.findViewById(R.id.purchase_date);
                view.gross_amount = (TextView) convertView.findViewById(R.id.gross_amount);
                view.invoice_no = (TextView) convertView.findViewById(R.id.invoice_no);
                view.total_amount = (TextView) convertView.findViewById(R.id.total_amount);
                view.supplier_name = (TextView) convertView.findViewById(R.id.supplier_name);
                view.site_name = (TextView) convertView.findViewById(R.id.site_name);
                view.supplier_id = (TextView) convertView.findViewById(R.id.supplier_id);

                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {

            view.purchase_id.setTag(position);
            view.purchase_date.setText("Purchase Date : "+mylist.get(position).getDate());
            view.gross_amount.setText("Gross Amount : "+mylist.get(position).getGross_amount());
            view.invoice_no.setText("Invoice No : "+mylist.get(position).getInvoice_no());

            view.total_amount.setText("Total Amount : "+mylist.get(position).getTotal_amount());
            view.supplier_name.setText("Supplier Name : "+mylist.get(position).getSupplier_name());
            view.site_name.setText("Site Name : "+mylist.get(position).getSite_name());
            view.supplier_id.setText("Supplier Id : "+mylist.get(position).getSupplier_id());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}