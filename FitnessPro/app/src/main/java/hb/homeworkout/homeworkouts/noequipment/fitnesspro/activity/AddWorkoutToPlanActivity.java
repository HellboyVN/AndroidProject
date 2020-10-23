package hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter.AddWorkoutToPlanBodyPartSpinnerAdapter;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter.AddWorkoutToPlanExerciseSpinnerAdapter;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.constant.AppConstants;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseHelper;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Plan;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Training;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Workout;

import static android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS;

public class AddWorkoutToPlanActivity extends BaseActivity {
    private AddWorkoutToPlanBodyPartSpinnerAdapter addWorkoutToPlanBodyPartSpinnerAdapter;
    private AddWorkoutToPlanExerciseSpinnerAdapter addWorkoutToPlanExerciseSpinnerAdapter;
    private ArrayList<Training> bodyPartList = new ArrayList();
    private Button buttonAddPlan;
    private Button buttonAddWorkout;
    private CardView cardViewNoPlansAdded;
    private CardView cardViewPlan;
    private CardView cardViewSetsAndReps;
    private CardView cardViewWorkout;
    private EditText editTextReps;
    private EditText editTextSets;
    private Plan plan;
    private ArrayList<Plan> planList = new ArrayList();
    private Spinner spinnerBodyPart;
    private Spinner spinnerDay;
    private Spinner spinnerExercise;
    private Spinner spinnerPlan;
    private TextInputLayout textInputLayoutReps;
    private TextInputLayout textInputLayoutSets;
    private Workout workout;
    private ArrayList<Workout> workoutList = new ArrayList();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( (int) R.layout.activity_add_workout_to_plan);
        getData();
        initializeView();
        setData();
    }

    private void getData() {
        this.plan = (Plan) getIntent().getParcelableExtra("PLAN_OBJECT");
        this.workout = (Workout) getIntent().getParcelableExtra(AppConstants.WORKOUT_OBJECT);
    }

    private void setData() {
        if (this.workout != null) {
            this.spinnerBodyPart.setSelection(getPositionOfBodyPart(this.workout.getBody_part_name()), true);
        }
    }

    private void initializeView() {
        this.spinnerBodyPart = (Spinner) findViewById(R.id.spinnerBodyPart);
        this.spinnerExercise = (Spinner) findViewById(R.id.spinnerExercise);
        this.buttonAddWorkout = (Button) findViewById(R.id.buttonAddWorkout);
        this.textInputLayoutSets = (TextInputLayout) findViewById(R.id.textInputLayoutSets);
        this.textInputLayoutReps = (TextInputLayout) findViewById(R.id.textInputLayoutReps);
        this.editTextSets = (EditText) findViewById(R.id.editTextSets);
        this.editTextReps = (EditText) findViewById(R.id.editTextReps);
        this.spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
        this.spinnerPlan = (Spinner) findViewById(R.id.spinnerPlan);
        this.cardViewPlan = (CardView) findViewById(R.id.cardViewPlan);
        this.cardViewWorkout = (CardView) findViewById(R.id.cardViewWorkout);
        this.cardViewSetsAndReps = (CardView) findViewById(R.id.cardViewSetsAndReps);
        this.cardViewNoPlansAdded = (CardView) findViewById(R.id.cardViewNoPlansAdded);
        this.buttonAddPlan = (Button) findViewById(R.id.buttonAddPlan);
        ScrollView view = (ScrollView) findViewById(R.id.scrollView);
        view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        prepareAndSetupPlanSpinner();
        if (this.planList.size() <= 1) {
            hideMainLayout();
            showNoPlanLayout();
        } else {
            hideNoPlanLayout();
            showMainLayout();
        }
        if (this.workout == null) {
            this.cardViewPlan.setVisibility(View.GONE);
        } else if (this.planList.size() > 1) {
            this.cardViewPlan.setVisibility(View.VISIBLE);
        }
        prepareAndSetupDaySpinner();
        prepareBodyPartListForSpinner();
        prepareWorkoutListForSpinner();
        this.addWorkoutToPlanBodyPartSpinnerAdapter = new AddWorkoutToPlanBodyPartSpinnerAdapter(this, this.bodyPartList);
        this.spinnerBodyPart.setAdapter(this.addWorkoutToPlanBodyPartSpinnerAdapter);
        this.addWorkoutToPlanExerciseSpinnerAdapter = new AddWorkoutToPlanExerciseSpinnerAdapter(this, this.workoutList);
        this.spinnerExercise.setAdapter(this.addWorkoutToPlanExerciseSpinnerAdapter);
        this.spinnerBodyPart.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    //evans
                    ArrayList<Workout> workoutArrayList = DatabaseHelper.getInstance(AddWorkoutToPlanActivity.this).getWorkoutListForBodyPart(((Training) AddWorkoutToPlanActivity.this.bodyPartList.get(position)).getPart_name());
                    AddWorkoutToPlanActivity.this.workoutList.clear();
                    Workout workout = new Workout();
                    workout.setWorkout_name("Select Exercise");
                    AddWorkoutToPlanActivity.this.workoutList.add(workout);
                    AddWorkoutToPlanActivity.this.workoutList.addAll(workoutArrayList);
                    AddWorkoutToPlanActivity.this.addWorkoutToPlanExerciseSpinnerAdapter.notifyDataSetChanged();
                } else {
                    AddWorkoutToPlanActivity.this.prepareWorkoutListForSpinner();
                    AddWorkoutToPlanActivity.this.addWorkoutToPlanExerciseSpinnerAdapter.notifyDataSetChanged();
                }
                if (AddWorkoutToPlanActivity.this.workout != null) {
                    AddWorkoutToPlanActivity.this.spinnerExercise.setSelection(AddWorkoutToPlanActivity.this.getPositionOfWorkout(AddWorkoutToPlanActivity.this.workout.getWorkout_name()), true);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.buttonAddWorkout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddWorkoutToPlanActivity.this.handleAddWorkoutButton();
            }
        });
        this.buttonAddPlan.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddWorkoutToPlanActivity.this.startActivity(new Intent(AddWorkoutToPlanActivity.this, CreateWorkoutPlanActivity.class));
                AddWorkoutToPlanActivity.this.finish();
            }
        });
    }

    private void handleAddWorkoutButton() {
        Plan plan;
        String day;
        String sets = this.editTextSets.getText().toString();
        String reps = this.editTextReps.getText().toString();
        if (this.workout == null) {
            plan = this.plan;
            day = plan.getDay();
        } else {
            day = (String) this.spinnerDay.getSelectedItem();
            plan = (Plan) this.spinnerPlan.getSelectedItem();
        }
        String planName = plan.getPlan_name();
        int spinnerPlanPosition = this.spinnerPlan.getSelectedItemPosition();
        int spinnerDayPosition = this.spinnerDay.getSelectedItemPosition();
        int spinnerBodyPartPosition = this.spinnerBodyPart.getSelectedItemPosition();
        int spinnerExercisePosition = this.spinnerExercise.getSelectedItemPosition();
        if (this.workout != null && spinnerPlanPosition == 0) {
            Toast.makeText(this, "Please select plan", Toast.LENGTH_SHORT).show();
        } else if (this.workout != null && spinnerDayPosition == 0) {
            Toast.makeText(this, "Please select day", Toast.LENGTH_SHORT).show();
        } else if (spinnerBodyPartPosition == 0) {
            Toast.makeText(this, "Please select muscle", Toast.LENGTH_SHORT).show();
        } else if (spinnerExercisePosition == 0) {
            Toast.makeText(this, "Please select exercise", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sets)) {
            this.textInputLayoutSets.setError("");
            Toast.makeText(this, "Please enter sets", Toast.LENGTH_SHORT).show();
            this.editTextSets.requestFocus();
        } else if (TextUtils.isEmpty(reps)) {
            this.textInputLayoutSets.setError(null);
            this.textInputLayoutReps.setError("");
            Toast.makeText(this, "Please enter reps", Toast.LENGTH_SHORT).show();
            this.editTextReps.requestFocus();
        } else if (Integer.parseInt(this.editTextReps.getText().toString()) < 1) {
            this.textInputLayoutSets.setError(null);
            this.textInputLayoutReps.setError("");
            Toast.makeText(this, "Please enter valid reps", Toast.LENGTH_SHORT).show();
            this.editTextReps.requestFocus();
        } else {
            this.textInputLayoutSets.setError(null);
            this.textInputLayoutReps.setError(null);
            String setsAndReps = "";
            for (int i = 0; i < Integer.parseInt(sets); i++) {
                if (Integer.parseInt(sets) - 1 == i) {
                    setsAndReps = setsAndReps + reps;
                } else {
                    setsAndReps = setsAndReps + reps + " - ";
                }
            }
            plan.setSets_and_reps(setsAndReps);
            plan.setDay(day);
            Workout workout = (Workout) this.spinnerExercise.getSelectedItem();
            if (DatabaseHelper.getInstance(this).isWorkoutAlreadyAddedForDay(workout, plan)) {
                Toast.makeText(this, "Workout already added in " + planName + " for " + day, Toast.LENGTH_SHORT).show();
            } else if (DatabaseHelper.getInstance(this).addWorkoutToPlan(workout, plan)) {
                Toast.makeText(this, "Workout added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void prepareBodyPartListForSpinner() {
        this.bodyPartList.clear();
        Training training = new Training(new String(),new String());
        training.setPart_name("Select Muscle");
        this.bodyPartList.add(training);
        this.bodyPartList.addAll(DatabaseHelper.getInstance(this).getBodyPartList());
    }

    private void prepareWorkoutListForSpinner() {
        this.workoutList.clear();
        Workout workout = new Workout();
        workout.setWorkout_name("Select Exercise");
        this.workoutList.add(workout);
    }

    private int getPositionOfBodyPart(String bodyPartName) {
        for (int i = 0; i < this.bodyPartList.size(); i++) {
            if (((Training) this.bodyPartList.get(i)).getPart_name().equals(bodyPartName)) {
                return i;
            }
        }
        return 0;
    }

    private int getPositionOfWorkout(String workoutName) {
        for (int i = 0; i < this.workoutList.size(); i++) {
            if (((Workout) this.workoutList.get(i)).getWorkout_name().equals(workoutName)) {
                return i;
            }
        }
        return 0;
    }

    private void prepareAndSetupDaySpinner() {
        ArrayList<String> dayList = new ArrayList();
        dayList.add("Select day");
        dayList.add("Monday");
        dayList.add("Tuesday");
        dayList.add("Wednesday");
        dayList.add("Thursday");
        dayList.add("Friday");
        dayList.add("Saturday");
        dayList.add("Sunday");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerDay.setAdapter(arrayAdapter);
    }

    private void prepareAndSetupPlanSpinner() {
        this.planList.clear();
        Plan plan = new Plan();
        plan.setPlan_name("Select Plan");
        this.planList.add(plan);
        this.planList.addAll(DatabaseHelper.getInstance(this).getMyPlansWorkoutList());
        ArrayAdapter<Plan> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, this.planList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerPlan.setAdapter(arrayAdapter);
    }

    private void hideMainLayout() {
        this.cardViewPlan.setVisibility(View.GONE);
        this.cardViewWorkout.setVisibility(View.GONE);
        this.cardViewSetsAndReps.setVisibility(View.GONE);
        this.buttonAddWorkout.setVisibility(View.GONE);
    }

    private void showMainLayout() {
        this.cardViewPlan.setVisibility(View.VISIBLE);
        this.cardViewWorkout.setVisibility(View.VISIBLE);
        this.cardViewSetsAndReps.setVisibility(View.VISIBLE);
        this.buttonAddWorkout.setVisibility(View.VISIBLE);
    }

    private void hideNoPlanLayout() {
        this.cardViewNoPlansAdded.setVisibility(View.GONE);
        this.buttonAddPlan.setVisibility(View.GONE);
    }

    private void showNoPlanLayout() {
        this.cardViewNoPlansAdded.setVisibility(View.VISIBLE);
        this.buttonAddPlan.setVisibility(View.VISIBLE);
    }
}
