package com.itcast.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itcast.notepad.adapter.NotepadAdapter;
import com.itcast.notepad.bean.NotepadBean;
import com.itcast.notepad.database.SQLiteHelper;

import java.util.List;

public class NotepadActivity extends AppCompatActivity {

    ListView listView;
    List<NotepadBean> list;
    SQLiteHelper mSQLiteHelper;
    NotepadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        listView=findViewById(R.id.listview);
        ImageView add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NotepadActivity.this,RecordActivity.class);
                startActivityForResult(intent,1);
            }
        });
        initData();
    }

    protected void initData() {
        mSQLiteHelper=new SQLiteHelper(this);
        showQueryData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotepadBean notepadBean=list.get(position);
                Intent intent=new Intent(NotepadActivity.this,RecordActivity.class);
                intent.putExtra("id",notepadBean.getId());
                intent.putExtra("time",notepadBean.getNotepadTime());
                intent.putExtra("content",notepadBean.getNotepadContent());
                NotepadActivity.this.startActivityForResult(intent,1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder=new AlertDialog.Builder(NotepadActivity.this)
                        .setMessage("是否刪除此記錄!")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NotepadBean notepadBean=list.get(position);
                                if(mSQLiteHelper.delent(notepadBean.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(NotepadActivity.this, "刪除成功!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog=builder.create();
                dialog.show();
                return true;
            }
        });
    }


    private void showQueryData(){
        if(list!=null){
            list.clear();
            Log.d("NotepadActivity", "空");
        }
        list=mSQLiteHelper.query();
        adapter=new NotepadAdapter(this,list);
        listView.setAdapter(adapter);
        Log.d("NotepadActivity", "list.size():" + list.size());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==2){
            showQueryData();
        }
    }
}
