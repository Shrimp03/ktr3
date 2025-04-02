package com.example.sqlitektra3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.List;

public class CourseFragment extends Fragment {
    private ListView listView;
    private EditText searchEditText;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses, null);

        listView = view.findViewById(R.id.list_courses);
        searchEditText = view.findViewById(R.id.search_course);
        dbHelper = new DatabaseHelper(getContext());

        loadCourses();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void loadCourses() {
        List<String> courseList = dbHelper.getAllCourses(true); // Chỉ lấy khóa học đã kích hoạt
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, courseList);
        listView.setAdapter(adapter);
    }
}