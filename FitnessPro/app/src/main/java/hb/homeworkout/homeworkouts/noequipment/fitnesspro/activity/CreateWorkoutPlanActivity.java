package hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseHelper;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.PersistenceManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Plan;

public class CreateWorkoutPlanActivity extends BaseActivity {
    private Button buttonCreatePlan;
    private EditText editTextDescription;
    private EditText editTextName;
    private Plan plan;
    private TextInputLayout textInputLayoutPlanDescription;
    private TextInputLayout textInputLayoutPlanName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_create_workout_plan);
        initializeView();
    }

    private void initializeView() {
        this.editTextName = (EditText) findViewById(R.id.editTextName);
        this.editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        this.textInputLayoutPlanName = (TextInputLayout) findViewById(R.id.textInputLayoutPlanName);
        this.textInputLayoutPlanDescription = (TextInputLayout) findViewById(R.id.textInputLayoutPlanDescription);
        this.buttonCreatePlan = (Button) findViewById(R.id.buttonCreatePlan);
        this.buttonCreatePlan.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CreateWorkoutPlanActivity.this.handleCreatePlanButton();
            }
        });
        this.editTextName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(CreateWorkoutPlanActivity.this.editTextName.getText().toString())) {
                    CreateWorkoutPlanActivity.this.textInputLayoutPlanName.setError(null);
                }
            }

            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void handleCreatePlanButton() {
        String planName = this.editTextName.getText().toString();
        String planDescription = this.editTextDescription.getText().toString();
//        try {
//            Answers.getInstance().logContentView((ContentViewEvent) ((ContentViewEvent) new ContentViewEvent().putContentName("Create plan clicked").putCustomAttribute("created plan name", planName)).putCustomAttribute("plan description", planDescription));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (TextUtils.isEmpty(planName)) {
            this.textInputLayoutPlanName.setError("Please enter plan name first");
            return;
        }
        this.textInputLayoutPlanName.setError(null);
        this.plan = new Plan();
        this.plan.setPlan_id(PersistenceManager.getNewPlanId());
        this.plan.setPlan_type("mine");
        this.plan.setPlan_name(planName);
        this.plan.setPlan_description(planDescription);
        if (DatabaseHelper.getInstance(this).createNewPlan(this.plan)) {
            Toast.makeText(this, "Plan created successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
