package upec.projetandroid20182019.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import upec.projetandroid20182019.R;
import upec.projetandroid20182019.model.Plan;

public class CustomAdapter extends ArrayAdapter<Plan> {
    private Context context;
    private int ressource;
    private List<Plan> listPlan;

    public CustomAdapter( Context context, int resource, List<Plan> objects) {
        super(context, resource, objects);
        this.context = context;
        this.ressource = resource;
        this.listPlan = objects;
    }

    public void setData(List<Plan> objects) {
        clear();
        addAll(objects);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_plan,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvDescrition = (TextView) convertView.findViewById(R.id.tv_description);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Plan plan = listPlan.get(position);
        viewHolder.tvTitle.setText(plan.getTitle());
        viewHolder.tvDescrition.setText(plan.getDescription());
        Log.d("CustomAdapter","GetView: " + (position + 1));
        return convertView;
    }

    public class ViewHolder {
        private TextView tvTitle;
        private TextView tvDescrition;
    }
}
