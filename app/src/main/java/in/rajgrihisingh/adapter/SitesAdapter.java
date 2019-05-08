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
import java.util.ArrayList;

public class SitesAdapter extends RecyclerView.Adapter<SitesAdapter.MyViewHolder> implements Filterable,RecyclerView.OnItemTouchListener {

    private ArrayList<Sites> siteList;
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

    public SitesAdapter(Context context, OnItemClickListener listener) {
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
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView id,name, description,supervisor_name,supervisor;
        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.id);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            supervisor_name=(TextView)view.findViewById(R.id.supervisor_name);
            supervisor=(TextView)view.findViewById(R.id.supervisor);
        }
    }

    public SitesAdapter(ArrayList<Sites> itemsList, Context context) {
        this.mcontext = context;
        this.siteList = itemsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sites_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
       Sites items = siteList.get(position);

        holder.id.setText("Id : "+items.getId());
        holder.name.setText("Name : "+items.getName());
        holder.description.setText("Address : "+items.getDescription());
        holder.supervisor_name.setText("Project : "+items.getSupervisor_name());
        holder.supervisor.setText("Name : "+items.getSupervisor());

    }

    @Override
    public int getItemCount() {
        return siteList.size();
    }
}

