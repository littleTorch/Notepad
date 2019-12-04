package com.itcast.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itcast.notepad.database.SQLiteHelper;
import com.itcast.notepad.utils.DBUtils;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mNoteBack;
    private TextView mNoteName;
    private TextView mTvTime;
    private EditText mNoteContent;
    private TextView mDelete;
    private TextView mNoteSave;

    SQLiteHelper mSQLiteHelper;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initView();
        initData();
    }

    private void initView() {
        mNoteBack = (Button) findViewById(R.id.note_back);
        mNoteBack.setOnClickListener(this);
        mNoteName = (TextView) findViewById(R.id.note_name);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mNoteContent = (EditText) findViewById(R.id.note_content);
        mDelete = (TextView) findViewById(R.id.delete);
        mDelete.setOnClickListener(this);
        mNoteSave = (TextView) findViewById(R.id.note_save);
        mNoteSave.setOnClickListener(this);
    }

    protected void initData() {
        mSQLiteHelper=new SQLiteHelper(this);
        mNoteName.setText("添加记录");
        Intent intent=getIntent();
        if (intent!=null){
            id=intent.getStringExtra("id");
            if (id!=null){
                mNoteName.setText("修改记录");
                mNoteContent.setText(intent.getStringExtra("content"));
                mTvTime.setText(intent.getStringExtra("time"));
                mTvTime.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.note_back:
                finish();
                break;
            case R.id.delete:
                mNoteContent.setText("");
                break;
            case R.id.note_save:
                String noteContent=mNoteContent.getText().toString().trim();
                if (id!=null){
                    if (noteContent.length()>0){
                        if (mSQLiteHelper.updateData(id,noteContent,DBUtils.getTime())){
                            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                            setResult(2);
                            finish();
                        }else{
                            Toast.makeText(this, "修改失敗", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "修改不能為空", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (noteContent.length() > 0) {
                        if (mSQLiteHelper.insertData(noteContent, DBUtils.getTime())) {
                            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                            setResult(2);
                            finish();
                        } else {
                            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "修改内容不能为空!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
