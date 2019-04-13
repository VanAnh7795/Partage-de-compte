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
import upec.projetandroid20182019.model.Paid;


public class CustomAdapterPaid extends ArrayAdapter<Paid> {
    private Context context;
    private int ressource;
    private List<Paid> listPaid;

    public CustomAdapterPaid( Context context, int resource, List<Paid> objects) {
        super(context, resource, objects);
        this.context = context;
        this.ressource = resource;
        this.listPaid = objects;
    }


    public void setData(List<Paid> objects) {
        clear();
        addAll(objects);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_paid,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.namePay = (TextView) convertView.findViewById(R.id.who_pay);
            viewHolder.nameReceive = (TextView) convertView.findViewById(R.id.for_whom);
            viewHolder.amountPay = (TextView) convertView.findViewById(R.id.amount_pay);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Paid paid = listPaid.get(position);
        viewHolder.namePay.setText(paid.getNamePay());
        viewHolder.nameReceive.setText(paid.getNameReceive());
        viewHolder.amountPay.setText(Double.toString(paid.getAmount()));
        Log.d("CustomAdapter","GetView: " + (position + 1));
        return convertView;
    }

    public class ViewHolder {
        private TextView namePay;
        private TextView nameReceive;
        private TextView amountPay;
    }
}
