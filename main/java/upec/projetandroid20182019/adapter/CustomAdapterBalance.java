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
import upec.projetandroid20182019.model.Balance;

public class CustomAdapterBalance extends ArrayAdapter<Balance> {
    private Context context;
    private int ressource;
    private List<Balance> listBalance;

    public CustomAdapterBalance( Context context, int resource, List<Balance> objects) {
        super(context, resource, objects);
        this.context = context;
        this.ressource = resource;
        this.listBalance = objects;
    }

    public void setData(List<Balance> objects) {
        clear();
        addAll(objects);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_balance,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.balanceName = (TextView) convertView.findViewById(R.id.balance_name);
            viewHolder.balanceAmount = (TextView) convertView.findViewById(R.id.balance_amount);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Balance balance = listBalance.get(position);
        viewHolder.balanceName.setText(balance.getName());
        double total = balance.getAmount();
        viewHolder.balanceAmount.setText(Double.toString(total));
        Log.d("CustomAdapter","GetView: " + (position + 1));
        return convertView;
    }

    public class ViewHolder {
        private TextView balanceName;
        private TextView balanceAmount;
    }
}
