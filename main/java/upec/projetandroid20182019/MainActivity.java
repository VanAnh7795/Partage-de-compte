package upec.projetandroid20182019;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import upec.projetandroid20182019.adapter.CustomAdapter;
import upec.projetandroid20182019.data.DBExpense;
import upec.projetandroid20182019.data.DBParticipant;
import upec.projetandroid20182019.data.DBPlan;
import upec.projetandroid20182019.model.Expense;
import upec.projetandroid20182019.model.Participant;
import upec.projetandroid20182019.model.Plan;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_NEW_TRICOUNT = 0;
    private ListView lvListPlan;
    private CustomAdapter customAdapter;
    private List<Plan> planList;
    private List<Participant> participantList;
    private List<Expense> expenseList;
    private DBPlan dbPlan;
    private DBParticipant dbParticipant;
    private DBExpense dbExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbPlan = new DBPlan(this);
        dbParticipant = new DBParticipant(this);
        dbExpense = new DBExpense(this);
        lvListPlan = (ListView) findViewById(R.id.list_view);
        planList = dbPlan.getAllPlan(); //get all of plan from database

        setAdapter(); //adapter plan list
        moveToDetailPlan();
        holdToDelete();
    }

    //create a new plan
    public void addPlan(View v){
        Intent itadd = new Intent(this, NewPlan.class);
        startActivityForResult(itadd,REQUEST_NEW_TRICOUNT);
    }

    //check result from new plan and update list plan
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_TRICOUNT) {
            if (resultCode == RESULT_OK) {
                updateListPlan();
            }
        }
    }

    private void setAdapter(){
        if(customAdapter == null){
            customAdapter = new CustomAdapter(this,R.layout.item_list_plan, planList);
            lvListPlan.setAdapter(customAdapter);
        } else {
            customAdapter.setData(planList);
            lvListPlan.setSelection(customAdapter.getCount()-1);
        }
    }

    //update list plan
    public void updateListPlan(){
        planList = dbPlan.getAllPlan();
        customAdapter.setData(planList);
    }

    //delete plan and delete
    private void holdToDelete(){
        lvListPlan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                AlertDialog myQuittingDialogBox =new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete")
                        .setMessage("Do you want to Delete")
                        .setIcon(R.drawable.ic_delete_black_24dp)

                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                final Plan plan = planList.get(position);
                                int idPlan = plan.getId();

                                participantList = dbParticipant.getAllParticipant(idPlan);

                                for (Participant participant: participantList){
                                    dbParticipant.deleteParticipantByIdPlan(participant);
                                }

                                expenseList = dbExpense.getAllExpense(idPlan);

                                for (Expense expense: expenseList){
                                    dbExpense.deleteExpenseByIdPlan(expense);
                                }

                                int result = dbPlan.deletePlanByID(idPlan);
                                if(result > 0) {
                                    Toast.makeText(getApplicationContext(),"Delete successfully",Toast.LENGTH_LONG).show();
                                    updateListPlan();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Delete failed",Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            }

                        })

                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();
                myQuittingDialogBox.show();
                return true;
            }
        });

    }

    public void moveToDetailPlan(){
        lvListPlan.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Plan plan = planList.get(position);
                Log.d("Mainactivity", "clickedItem : "+ plan.getId());
                Intent intentDetail = new Intent(getApplicationContext(), DetailPlan.class);
                intentDetail.putExtra(DetailPlan.ID_PLAN_DETAIL, plan.getId());
                startActivity(intentDetail);
            }
        });
    }


}
