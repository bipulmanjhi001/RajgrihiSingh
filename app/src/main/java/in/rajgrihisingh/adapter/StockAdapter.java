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
import in.rajgrihisingh.model.StockList;
import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.MyViewHolder> implements Filterable,RecyclerView.OnItemTouchListener {

    ArrayList<StockList> stockLists;
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
    public StockAdapter(Context context, OnItemClickListener listener) {
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
        private TextView item_type_id,item_types,site_ids,item_names,quantitys;

        public MyViewHolder(View view) {
            super(view);
            item_type_id = (TextView) view.findViewById(R.id.item_type_id);
            item_types = (TextView) view.findViewById(R.id.item_types);
            site_ids = (TextView) view.findViewById(R.id.site_ids);
            item_names=(TextView)view.findViewById(R.id.item_names);
            quantitys=(TextView)view.findViewById(R.id.quantitys);
        }
    }
    public StockAdapter(ArrayList<StockList> stockLists, Context context) {
        this.mcontext = context;
        this.stockLists = stockLists;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        StockList items = stockLists.get(position);
        holder.item_type_id.setText("Item Type Id : "+items.getItem_type_id());
        holder.item_types.setText("Item : "+items.getItem_type());
        holder.site_ids.setText("Site Id : "+items.getSite_id());
        holder.item_names.setText("Item Type : "+items.getItem_name());
        holder.quantitys.setText("Quantity : "+items.getQuantity());
    }
    @Override
    public int getItemCount() {
        return stockLists.size();
    }
}