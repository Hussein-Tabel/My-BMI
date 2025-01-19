package com.example.mybmi;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Objects;

public class TakeData extends AppCompatActivity {

    String gender;
    ImageButton btnmen,btnwomen;
    EditText id,name,weight,height;
    Button btninsert,btnselectone,btnselectall,btnupdate,btndelete;
    float bmi;
    String category;
    SQLiteDatabase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_take_data);

        id = findViewById(R.id.etID);
        name = findViewById(R.id.etName);
        weight = findViewById(R.id.etWeight);
        height = findViewById(R.id.etHeight);

        btnmen = findViewById(R.id.imgbtnmen);
        btnwomen = findViewById(R.id.imgbtnwomen);
        btninsert = findViewById(R.id.btnInsert);
        btnselectone=findViewById(R.id.btnSelectOne);
        btnselectall=findViewById(R.id.btnSelectAll);
        btnupdate=findViewById(R.id.btnUpdate);
        btndelete=findViewById(R.id.btnDelet);

        DB = openOrCreateDatabase("patientDB",MODE_PRIVATE,null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS patient(id VARCHAR, name VARCHAR, bmi REAL, weight REAL, height REAL, gender VARCHAR, category VARCHAR)");

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gender.isEmpty() ||
                        id.getText().toString().trim().isEmpty() ||
                        name.getText().toString().trim().isEmpty() ||
                        height.getText().toString().trim().isEmpty() ||
                        weight.getText().toString().trim().isEmpty()) {

                    Toast.makeText(TakeData.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor c = DB.rawQuery("SELECT * FROM patient WHERE id = '" + id.getText() + "'", null);
                if (c.getCount() > 0) {
                    Toast.makeText(TakeData.this, "Choose another ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                double weightValue = Double.parseDouble(weight.getText().toString());
                double heightValue = Double.parseDouble(height.getText().toString());

                bmi = (float) (weightValue / (heightValue * heightValue));

                if (bmi < 18.5) {
                    category = "Underweight";
                } else if (bmi >= 18.5 && bmi < 24.9) {
                    category = "Normal weight";
                } else if (bmi >= 25 && bmi < 29.9) {
                    category = "Overweight";
                } else {
                    category = "Obese";
                }

                DB.execSQL("INSERT INTO patient (id, name, bmi, weight, height, gender, category) VALUES ('" +
                        id.getText().toString() + "', '" +
                        name.getText().toString() + "', " +
                        bmi + ", " +
                        weight.getText().toString() + ", " +
                        height.getText().toString() + ", '" +
                        gender + "', '" +
                        category + "')");

                Toast.makeText(TakeData.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                Clear_Control();
            }
        });

        btnselectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = DB.rawQuery("SELECT * FROM patient", null);

                StringBuffer buffer = new StringBuffer();
                if(c.getCount() > 0){
                    c.moveToFirst();
                    do {
                        buffer.append("Patient ID: " + c.getString(0));
                        buffer.append("\nPatient Name: " + c.getString(1));
                        buffer.append("\nBMI : " + c.getFloat(2));
                        buffer.append("\nWeight: " + c.getFloat(3));
                        buffer.append("\nHeight: " + c.getFloat(4));
                        buffer.append("\nGender: " + c.getString(5));
                        buffer.append("\nCategory: " + c.getString(6));
                        buffer.append("\n-------------------------------------------\n");
                    } while (c.moveToNext());
                }
                Show_DB("All Patients", buffer.toString());
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.getText().toString().trim().isEmpty()){
                    Toast.makeText(TakeData.this,"Please select the ID you want to delete.",Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(TakeData.this)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this patient?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Cursor c = DB.rawQuery("SELECT * FROM patient WHERE id='" + id.getText() + "'", null);
                                if (c.getCount() > 0) {
                                    DB.execSQL("DELETE FROM patient WHERE id = '" + id.getText() + "'");
                                    Toast.makeText(TakeData.this, "Patient deleted", Toast.LENGTH_SHORT).show();
                                    Clear_Control();
                                    return;
                                }
                                Toast.makeText(TakeData.this,"This ID doses not exist", Toast.LENGTH_SHORT).show();
                                Clear_Control();
                                c.close();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        btnselectone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.getText().toString().trim().isEmpty()) {
                    Toast.makeText(TakeData.this, "Please select the ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor c = DB.rawQuery("SELECT * FROM patient WHERE id = '"+id.getText()+"'",null);

                if (c.getCount() > 0) {
                    c.moveToFirst();

                    if (c.getString(5) == "men"){
                        btnmen.performClick();
                    }else {
                        btnwomen.performClick();
                    }

                    name.setText(c.getString(1));
                    weight.setText(c.getString(3));
                    height.setText(c.getString(4));

                } else {
                    Toast.makeText(TakeData.this, "This Patient does not exist", Toast.LENGTH_SHORT).show();
                    Clear_Control();
                }
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(gender, "") ||
                        id.getText().toString().trim().isEmpty() ||
                        name.getText().toString().trim().isEmpty() ||
                        height.getText().toString().trim().isEmpty() ||
                        weight.getText().toString().trim().isEmpty()) {

                    Toast.makeText(TakeData.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                double weightValue = Double.parseDouble(weight.getText().toString());
                double heightValue = Double.parseDouble(height.getText().toString());

                Calculate_BMI();

                Cursor c = DB.rawQuery("SELECT * FROM patient WHERE id = '"+id.getText()+"'", null);
                if (c.getCount() > 0) {
                    Calculate_BMI();
                    DB.execSQL("UPDATE patient SET name = '" + name.getText() + "', " +
                            "bmi = " + bmi + ","+
                            "weight = '" + weight.getText().toString() + "', " +
                            "height = '" + height.getText().toString() + "', " +
                            "gender = '" + gender + "', " +
                            "category = '" + category + "' WHERE id = '" + id.getText() + "'");
                    Toast.makeText(TakeData.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                    Clear_Control();
                } else {
                    Toast.makeText(TakeData.this, "Patient not found with this ID", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void Clear_Control(){
        gender = "";
        id.setText("");
        name.setText("");
        weight.setText("");
        height.setText("");
    }

    public void Back(View view) {
        finish();
    }

    public void men(View view) {
        gender = "men";
    }

    public void women(View view) {
        gender = "women";
    }

    public void Calculate_BMI(){
        double weightValue = Double.parseDouble(weight.getText().toString());
        double heightValue = Double.parseDouble(height.getText().toString());

        bmi = (float) (weightValue / (heightValue * heightValue));

        if (bmi < 18.5) {
            category = "Underweight";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            category = "Normal weight";
        } else if (bmi >= 25 && bmi < 29.9) {
            category = "Overweight";
        } else {
            category = "Obese";
        }
    }
    public void Show_DB(String Title, String Msg){
        AlertDialog.Builder b = new AlertDialog.Builder((this));
        b.setCancelable(true);
        b.setTitle(Title);
        b.setIcon(R.drawable.db);
        b.setMessage(Msg);
        b.show();
    }

    public void btnCancel(View view) {
        Clear_Control();
    }
}