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
import in.rajgrihisingh.model.ExpenseList;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ExpenseListAdpater extends BaseAdapter {
    private Context mContext;
    ArrayList<ExpenseList> mylist = new ArrayList<>();

    public ExpenseListAdpater(ArrayList<ExpenseList> itemArray, Context mContext) {
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
        private TextView id,date, amount,supervisor_name,site_name,ex_ids;
        private TextView ex_bill_no,ex_image,ex_name;
        private TextView ex_remarks,ex_site_id,ex_expense_head;
        private ImageView bill_screenshot;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ExpenseListAdpater.ViewHolder view;
        LayoutInflater inflator = null;
        if (convertView == null) {
            view = new ExpenseListAdpater.ViewHolder();
            try {
                inflator = ((Activity) mContext).getLayoutInflater();

                convertView = inflator.inflate(R.layout.expenses_list_row, null);
                view.id = (TextView) convertView.findViewById(R.id.ex_id);
                view.ex_ids = (TextView) convertView.findViewById(R.id.ex_expense_head_id);
                view.date = (TextView) convertView.findViewById(R.id.ex_date);
                view.amount = (TextView) convertView.findViewById(R.id.ex_amount);
                view.supervisor_name = (TextView) convertView.findViewById(R.id.ex_supervisors);
                view.site_name = (TextView) convertView.findViewById(R.id.ex_site_name);
                view.ex_bill_no = (TextView) convertView.findViewById(R.id.ex_bill_no);
                view.ex_image = (TextView) convertView.findViewById(R.id.ex_image);
                view.ex_name = (TextView) convertView.findViewById(R.id.ex_name);
                view.ex_remarks = (TextView) convertView.findViewById(R.id.ex_remarks);
                view.ex_site_id = (TextView) convertView.findViewById(R.id.ex_site_id);
                view.ex_expense_head = (TextView) convertView.findViewById(R.id.ex_expense_head);
                view.bill_screenshot = (ImageView) convertView.findViewById(R.id.bill_screenshot);

                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.id.setTag(position);
            view.date.setText("Date : "+mylist.get(position).getDate());
            view.amount.setText("Name : "+mylist.get(position).getAmount());
            view.supervisor_name.setText("Supervisor Name : "+mylist.get(position).getSupervisor());
            view.site_name.setText("Expense Name : "+mylist.get(position).getSite_name());
            view.ex_ids.setText("Id : "+mylist.get(position).getExpense_head_id());
            view.ex_bill_no.setText("Bill No : "+mylist.get(position).getBill_no());
            view.ex_image.setText(mylist.get(position).getImage());
            view.ex_name.setText("Amount : "+mylist.get(position).getName());
            view.ex_remarks.setText("Remarks : "+mylist.get(position).getRemarks());
            view.ex_site_id.setText("Site Id : "+mylist.get(position).getSite_id());
            view.ex_expense_head.setText("Site : "+mylist.get(position).getExpense_head());

            try {
                Picasso.get().load(mylist.get(position).getSupervisor())
                        .error(R.drawable.ic_no_image)
                        .into(view.bill_screenshot);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
