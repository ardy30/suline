package boeren.com.appsuline.app.bmedical.appsuline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class BaseListAdapter<T> extends BaseAdapter{
    private Context context;
    private ArrayList<T> objects;
    private LayoutInflater inflater;


    public BaseListAdapter(Context context,ArrayList<T> objects){
        this.context=context;
        this.objects=objects;
        this.inflater=LayoutInflater.from(this.context);
    }
    public Context getContext() {
        return context;
    }

    public void requestRefresh()
    {
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public T getItem(int position) {
        return objects.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=this.inflateView(inflater,position,parent);
            convertView.setTag(getViewHolder(convertView,position));
        }
        populateView(convertView.getTag(),position);
        return convertView;
    }

    public ArrayList<T> getObjects() {
        return objects;
    }

    public abstract View inflateView(LayoutInflater inflater,int position,ViewGroup parent);
    public abstract Object getViewHolder(View convertView,int position);
    public abstract void populateView(Object viewHolder,int position);
}
