package top.devonte.note.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import top.devonte.note.R;

public class ForgetPassActivity extends AppCompatActivity {

    private static final String TAG = "ForgetPassActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
    }
}
