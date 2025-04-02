package com.example.sqlitektra3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fabRegister = findViewById(R.id.fab_register);
        dbHelper = new DatabaseHelper(this);

        loadFragment(new CourseFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            if (item.getItemId() == R.id.nav_courses) {
                fragment = new CourseFragment();
            } else if (item.getItemId() == R.id.nav_stats) {
                fragment = new StatsFragment();
            }
            return loadFragment(fragment);
        });

        fabRegister.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int studentId = prefs.getInt("studentId", -1);
            if (studentId == -1) {
                showLoginDialog();
            } else {
                showRegisterCourseDialog(studentId);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_login, null);
        EditText emailEditText = view.findViewById(R.id.edit_email);
        EditText passwordEditText = view.findViewById(R.id.edit_password);

        builder.setView(view)
                .setPositiveButton("Đăng nhập", (dialog, which) -> {
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    int studentId = dbHelper.login(email, password);
                    if (studentId != -1) {
                        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        prefs.edit().putInt("studentId", studentId).apply();
                        showRegisterCourseDialog(studentId);
                    } else {
                        Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showRegisterCourseDialog(int studentId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_register_course, null);
        Spinner courseSpinner = view.findViewById(R.id.spinner_courses);
        List<String> courseList = dbHelper.getAllCourses(true); // Chỉ lấy khóa học đã kích hoạt
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);

        builder.setView(view)
                .setPositiveButton("Đăng ký", (dialog, which) -> {
                    String selectedCourse = courseSpinner.getSelectedItem().toString();
                    dbHelper.registerCourse(studentId, selectedCourse);
                    Toast.makeText(this, "Đăng ký thành công: " + selectedCourse, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}