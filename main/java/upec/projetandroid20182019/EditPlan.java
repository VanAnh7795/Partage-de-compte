package upec.projetandroid20182019;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import upec.projetandroid20182019.adapter.CustomAdapterParticipant;
import upec.projetandroid20182019.data.DBParticipant;
import upec.projetandroid20182019.model.Participant;

public class EditPlan extends AppCompatActivity {

    public static final String ID_PLAN_EDIT = "ID_PLAN_EDIT";


    private int idPlan;
    private DBParticipant dbParticipant;
    private List<Participant> participantList = new ArrayList<>();
    private CustomAdapterParticipant customAdapterParticipant;
    private ListView lvParticipant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);

        lvParticipant = findViewById(R.id.lvParticipant);

        if (getIntent().hasExtra(ID_PLAN_EDIT)) {
            idPlan = getIntent().getIntExtra(ID_PLAN_EDIT, 0);
        }

        dbParticipant = new DBParticipant(this);
        participantList = dbParticipant.getAllParticipant(idPlan);

        setAdapter();

    }

    private void setAdapter(){
        if(customAdapterParticipant == null){
            customAdapterParticipant = new CustomAdapterParticipant(this,R.layout.item_list_participant, participantList ); /*custom listView*/
            lvParticipant.setAdapter(customAdapterParticipant); /*add CustomListView to ListView*/
        } else {
            customAdapterParticipant.setData(participantList); /*if values change, application will update*/
            lvParticipant.setSelection(customAdapterParticipant.getCount()-1);
        }
    }

    //back MainActivity
    public void editPlanBack (View v){
        finish();
    }

}
