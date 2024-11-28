package com.example.selftest.demo7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selftest.R;

public class Demo7Activity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo7);
        // EditText
        EditText editText = (EditText) findViewById(R.id.filter_et);
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        }});
        // TextView
        TextView textView = (TextView) findViewById(R.id.demo7_tv);
        String text = "这里是一段用来展示效果的文字";
        SpannableString span = new SpannableString(text);
        span.setSpan(new ForegroundColorSpan(0xFFFF0000), 1, 3, 0);
        span.setSpan(new BackgroundColorSpan(0xFF00FF00), 4, 6, 0);
        span.setSpan(new AbsoluteSizeSpan(50), 2, 5, 0);
        span.setSpan(new UnderlineSpan(), 7, 10, 0);
        span.setSpan(new MyClickableSpan(), 11, 13, 0);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(span);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(Demo7Activity.this, "你点击了TextView！", Toast.LENGTH_LONG).show();
    }

    class MyClickableSpan extends ClickableSpan {
        public MyClickableSpan() {
        }

        @Override
        public void onClick(@NonNull View widget) {
            Toast.makeText(Demo7Activity.this, "你点击了下划线！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.RED);
            ds.setUnderlineText(true);
        }
    }
}