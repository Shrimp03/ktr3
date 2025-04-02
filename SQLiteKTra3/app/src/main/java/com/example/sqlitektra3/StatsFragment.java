package com.example.sqlitektra3;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {
    private Spinner courseSpinner;
    private CheckBox activeCheckBox;
    private ListView studentListView;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, null);

        courseSpinner = view.findViewById(R.id.spinner_courses);
        activeCheckBox = view.findViewById(R.id.checkbox_active);
        studentListView = view.findViewById(R.id.list_students);
        dbHelper = new DatabaseHelper(getContext());

        loadCourses();

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadStudents();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        activeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> loadCourses());

        return view;
    }

    private void loadCourses() {
        List<String> courseList = dbHelper.getAllCourses(activeCheckBox.isChecked());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, courseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);
    }

    private void loadStudents() {
        String selectedCourse = courseSpinner.getSelectedItem().toString();
        List<String> studentList = dbHelper.getStudentsByCourse(selectedCourse);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, studentList);
        studentListView.setAdapter(adapter);
    }
}