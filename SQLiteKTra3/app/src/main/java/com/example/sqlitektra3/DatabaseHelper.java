package com.example.sqlitektra3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CourseDB";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COURSE_TABLE = "CREATE TABLE Course (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "courseName TEXT," +
                "startDate TEXT," +
                "endDate TEXT," +
                "isActive INTEGER)";
        db.execSQL(CREATE_COURSE_TABLE);

        String CREATE_STUDENT_TABLE = "CREATE TABLE Student (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fullName TEXT," +
                "email TEXT," +
                "password TEXT)";
        db.execSQL(CREATE_STUDENT_TABLE);

        String CREATE_REGISTRATION_TABLE = "CREATE TABLE Registration (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "studentId INTEGER," +
                "courseId INTEGER," +
                "registrationDate TEXT," +
                "FOREIGN KEY(studentId) REFERENCES Student(id)," +
                "FOREIGN KEY(courseId) REFERENCES Course(id))";
        db.execSQL(CREATE_REGISTRATION_TABLE);

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO Course (courseName, startDate, endDate, isActive) VALUES ('Android Cơ bản', '2025-04-01', '2025-06-01', 1)");
        db.execSQL("INSERT INTO Course (courseName, startDate, endDate, isActive) VALUES ('Java Nâng cao', '2025-05-01', '2025-07-01', 0)");
        db.execSQL("INSERT INTO Course (courseName, startDate, endDate, isActive) VALUES ('Kotlin', '2025-06-01', '2025-08-01', 1)");

        db.execSQL("INSERT INTO Student (fullName, email, password) VALUES ('Nguyễn Văn A', 'a@gmail.com', '123456')");
        db.execSQL("INSERT INTO Student (fullName, email, password) VALUES ('Trần Thị B', 'b@gmail.com', '654321')");

        db.execSQL("INSERT INTO Registration (studentId, courseId, registrationDate) VALUES (1, 1, '2025-04-02')");
        db.execSQL("INSERT INTO Registration (studentId, courseId, registrationDate) VALUES (2, 3, '2025-04-02')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Course");
        db.execSQL("DROP TABLE IF EXISTS Student");
        db.execSQL("DROP TABLE IF EXISTS Registration");
        onCreate(db);
    }

    // Lấy danh sách khóa học
    public List<String> getAllCourses(boolean isActive) {
        List<String> courseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT courseName FROM Course WHERE isActive = ? ORDER BY startDate",
                new String[]{isActive ? "1" : "0"});
        while (cursor.moveToNext()) {
            courseList.add(cursor.getString(0));
        }
        cursor.close();
        return courseList;
    }

    // Lấy danh sách học viên theo khóa học
    public List<String> getStudentsByCourse(String courseName) {
        List<String> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.fullName FROM Student s " +
                "JOIN Registration r ON s.id = r.studentId " +
                "JOIN Course c ON c.id = r.courseId " +
                "WHERE c.courseName = ?";
        Cursor cursor = db.rawQuery(query, new String[]{courseName});
        while (cursor.moveToNext()) {
            studentList.add(cursor.getString(0));
        }
        cursor.close();
        return studentList;
    }

    // Thêm đăng ký khóa học
    public void registerCourse(int studentId, String courseName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Course WHERE courseName = ?", new String[]{courseName});
        int courseId = -1;
        if (cursor.moveToFirst()) {
            courseId = cursor.getInt(0);
        }
        cursor.close();

        if (courseId != -1) {
            ContentValues values = new ContentValues();
            values.put("studentId", studentId);
            values.put("courseId", courseId);
            values.put("registrationDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            db.insert("Registration", null, values);
        }
    }

    // Kiểm tra đăng nhập
    public int login(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Student WHERE email = ? AND password = ?",
                new String[]{email, password});
        int studentId = -1;
        if (cursor.moveToFirst()) {
            studentId = cursor.getInt(0);
        }
        cursor.close();
        return studentId;
    }
}