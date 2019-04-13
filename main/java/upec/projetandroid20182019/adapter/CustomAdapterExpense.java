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
import upec.projetandroid20182019.model.Expense;


public class CustomAdapterExpense extends ArrayAdapter<Expense> {

    private Context context;
    private int ressource;
    private List<Expense> listExpense;

    public CustomAdapterExpense(Context context, int resource, List<Expense> objects) {
        super(context, resource, objects);
        this.context = context;
        this.ressource = resource;
        this.listExpense = objects;
    }

    public void setData(List<Expense> objects) {
        clear();
        addAll(objects);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_expense,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.itTitle = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.itAmount = (TextView) convertView.findViewById(R.id.item_amount);
            viewHolder.itName = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.itDate = (TextView) convertView.findViewById(R.id.item_date);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Expense expense = listExpense.get(position);
        viewHolder.itTitle.setText(expense.getTitle());
        viewHolder.itAmount.setText(Double.toString(expense.getAmount()));
        viewHolder.itName.setText(expense.getNameParticipant());
        viewHolder.itDate.setText(expense.getDate());
        Log.d("CustomAdapter","GetView: " + (position + 1));
        return convertView;
    }

    public class ViewHolder {
        private TextView itTitle;
        private TextView itAmount;
        private TextView itName;
        private TextView itDate;
    }
}
