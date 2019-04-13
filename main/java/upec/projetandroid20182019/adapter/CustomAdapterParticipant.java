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
import upec.projetandroid20182019.model.Participant;

public class CustomAdapterParticipant extends ArrayAdapter<Participant> {

    private Context context;
    private int ressource;
    private List<Participant> listParticipant;

    public CustomAdapterParticipant( Context context, int resource, List<Participant> objects) {
        super(context, resource, objects);
        this.context = context;
        this.ressource = resource;
        this.listParticipant = objects;
    }

    public void setData(List<Participant> objects) {
        clear();
        addAll(objects);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_participant,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.edit_participant_out);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Participant participant = listParticipant.get(position);
        viewHolder.tvName.setText(participant.getName());
        Log.d("CustomAdapter","GetView: " + (position + 1));
        return convertView;
    }

    public class ViewHolder {
        private TextView tvName;
    }
}
