package upec.projetandroid20182019;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

import upec.projetandroid20182019.data.DBExpense;
import upec.projetandroid20182019.data.DBParticipant;
import upec.projetandroid20182019.data.DBPlan;



public class NewPlan extends AppCompatActivity {

    public static final String NEW_ID_PLAN = "NEW_ID_PLAN";

    private Button buttonAdd;
    private LinearLayout llParticipant, llContainer;
    private TextInputLayout inputLayoutTitle, inputLayoutDescription, inputLayoutName, inputLayoutParticipant;
    private EditText editTitle, editDescription, editName, editParticipantIn;
    private DBParticipant dbParticipant;
    private DBPlan dbPlan;
    private DBExpense dbExpense;
    private AutoCompleteTextView autoCompleteTextView;
    private ImageView imageView;
    private ArrayList<String> list_participant = new ArrayList<>();
    private int idPlan = 0;

    private static final String[] currencys = new String[]{"EUR", "USD", "GBP"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);

        initWidget();
        createCurrency(); //choose currency
        add_participant(); //add and remove participant
    }

    public void initWidget(){
        dbPlan = new DBPlan(this);
        dbParticipant = new DBParticipant(this);
        dbExpense = new DBExpense(this);

        llParticipant = findViewById(R.id.ll_participant);
        llContainer = findViewById(R.id.container);
        autoCompleteTextView = findViewById(R.id.actv);
        buttonAdd = findViewById(R.id.add);
        imageView = findViewById(R.id.image);

        inputLayoutTitle = findViewById(R.id.input_layout_title);
        inputLayoutName = findViewById(R.id.input_layout_name);
        inputLayoutParticipant = findViewById(R.id.input_layout_participant);
        inputLayoutDescription = findViewById(R.id.input_layout_description);

        editTitle = findViewById(R.id.edit_title);
        editDescription = findViewById(R.id.edit_description);
        editName = findViewById(R.id.edit_name);
        editParticipantIn = findViewById(R.id.edit_participant_in);
    }

    //choose currency
    public void createCurrency(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, currencys);
        autoCompleteTextView.setAdapter(adapter);

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();
            }
        });
    }

    //check title plan
    private boolean validateTitle() {
        if (editTitle.getText().toString().trim().isEmpty()) {
            inputLayoutTitle.setError("The title be at least 1 character long");
            return false;
        } else {
            inputLayoutTitle.setErrorEnabled(false);
        }
        return true;
    }

    //check title plan
    private boolean validateDescription() {
        if (editDescription.getText().toString().trim().isEmpty()) {
            inputLayoutDescription.setError("The title be at least 1 character long");
            return false;
        } else {
            inputLayoutDescription.setErrorEnabled(false);
        }
        return true;
    }

    //check my name
    private boolean validateName() {
        if (editName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Invalid first name");
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    //check name participant
    private boolean validateParticipant() {
        if (editParticipantIn.getText().toString().trim().isEmpty()) {
            inputLayoutParticipant.setError("Invalid first name");
            return false;
        } else {
            inputLayoutParticipant.setErrorEnabled(false);
        }
        return true;
    }

    //save datebase into table list_plan and list_partticipant
    public void saveData(View v){
        if (!validateTitle() || !validateName() || !validateName() || autoCompleteTextView.getText().toString().isEmpty() ){
            return;
        }
        idPlan = dbPlan.insertData(editTitle.getText().toString(),
                editDescription.getText().toString(),
                autoCompleteTextView.getText().toString());
        dbParticipant.insertParticipant(list_participant, idPlan);
        for(String name: list_participant){
            int result = dbExpense.insertNameAmount(idPlan,0,name);
        }
        setResult(RESULT_OK);
        finish();
    }

    //add view to add participant
    public void Ajouter_clicked(View v){
        if (!validateName()){
            return;
        } else {
            list_participant.add(editName.getText().toString());
            llParticipant.setVisibility(View.VISIBLE);
        }
    }

    //add participant and remove participant
    public void add_participant(){
        buttonAdd.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                if (!validateParticipant()){
                    return;
                } else {
                    final LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.item_list_participant, null);
                    final TextView editParticipantOut = (TextView) addView.findViewById(R.id.edit_participant_out);

                    if(editParticipantOut != null) {
                        Log.d("Participant", "success to add participant");
                    }
                    String text = editParticipantIn.getText().toString();
                    while (list_participant.contains(text)){
                        return;
                    }
                    editParticipantOut.setText(text);
                    list_participant.add(editParticipantIn.getText().toString());

                    //remove name participant in listview and in database
                    ImageButton buttonRemove = (ImageButton) addView.findViewById(R.id.remove);
                    buttonRemove.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((LinearLayout) addView.getParent()).removeView(addView);
                            EditText nameRemove =  addView.findViewById(R.id.edit_participant_out);
                            String name = (String)(nameRemove.getText().toString());
                            list_participant.remove(name);
                        }
                    });

                    llContainer.addView(addView, 0);
                }
            }
        });

        LayoutTransition transition = new LayoutTransition();
        llContainer.setLayoutTransition(transition);
    }

    //back Main
    public void back_button(View v){
        Intent itback = new Intent(this, MainActivity.class);
        startActivityForResult(itback,1);
    }

}
