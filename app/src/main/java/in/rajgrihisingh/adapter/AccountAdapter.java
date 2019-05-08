package in.rajgrihisingh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import in.rajgrihisingh.R;
import in.rajgrihisingh.model.Account;
import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> implements Filterable,RecyclerView.OnItemTouchListener {

    ArrayList<Account> accounts;
    private OnItemClickListener mListener;
    GestureDetector mGestureDetector;
    Context mcontext;

    @Override
    public Filter getFilter() {
        return null;
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    public AccountAdapter(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }
    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept){
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView id,date, amount,supervisor_name,site_name;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.ids);
            date = (TextView) view.findViewById(R.id.date);
            amount = (TextView) view.findViewById(R.id.amount);
            supervisor_name=(TextView)view.findViewById(R.id.supervisor_names2);
            site_name=(TextView)view.findViewById(R.id.site_name);
        }
    }
    public AccountAdapter(ArrayList<Account> itemsList, Context context) {
        this.mcontext = context;
        this.accounts = itemsList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Account items = accounts.get(position);
        holder.id.setText(items.getId());
        holder.date.setText("Date : "+items.getDate());
        holder.amount.setText("Amount : "+items.getAmount());
        holder.supervisor_name.setText("Name : "+items.getSupervisor_name());
        holder.site_name.setText("Site : "+items.getSite_name());
    }
    @Override
    public int getItemCount() {
        return accounts.size();
    }
}

