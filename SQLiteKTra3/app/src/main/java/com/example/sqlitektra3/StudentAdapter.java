package com.example.sqlitektra3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<DatabaseHelper.StudentCourseInfo> {
    private Context context;
    private List<DatabaseHelper.StudentCourseInfo> studentList;

    public StudentAdapter(Context context, List<DatabaseHelper.StudentCourseInfo> studentList) {
        super(context, R.layout.student_list_item, studentList);
        this.context = context;
        this.studentList = studentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.student_list_item, parent, false);
        }

        DatabaseHelper.StudentCourseInfo student = studentList.get(position);

        TextView idTextView = convertView.findViewById(R.id.text_id);
        TextView nameTextView = convertView.findViewById(R.id.text_name);
        TextView startDateTextView = convertView.findViewById(R.id.text_start_date);
        TextView endDateTextView = convertView.findViewById(R.id.text_end_date);

        idTextView.setText(String.valueOf(student.id));
        nameTextView.setText(student.fullName);
        startDateTextView.setText(student.startDate);
        endDateTextView.setText(student.endDate);

        return convertView;
    }
}
