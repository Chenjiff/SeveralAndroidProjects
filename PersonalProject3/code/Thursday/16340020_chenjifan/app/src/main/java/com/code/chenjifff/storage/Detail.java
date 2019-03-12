package com.code.chenjifff.storage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Detail extends AppCompatActivity implements View.OnClickListener {
    EditText text;
    static final String STORAGE_FILE = "data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Button saveButton = (Button) this.findViewById(R.id.saveButton);
        Button loadButton = (Button) this.findViewById(R.id.loadButton);
        Button clearButton = (Button) this.findViewById(R.id.clearButton);
        text = (EditText) this.findViewById(R.id.inputText);
        saveButton.setOnClickListener(this);
        loadButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                saveFile(text.getText().toString(), STORAGE_FILE);
                Toast.makeText(this, "Save successfully.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.loadButton:
                try {
                    String data = readFile(STORAGE_FILE);
                    text.setText(data);
                    Toast.makeText(this, "Load successfully.", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Toast.makeText(this, "Fail to load file.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.clearButton:
                text.setText("");
                break;
            default:
                break;
        }
    }

    public void saveFile(String string, String fileName) {
        try {
            FileOutputStream fo = openFileOutput(fileName, MODE_PRIVATE);
            fo.write(string.getBytes());
            fo.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFile (String fileName) throws Exception {
        FileInputStream fi = openFileInput(fileName);
        byte[] dataBytes = new byte[fi.available()];
        String dataString = new String();
        int len = 0;
        //读取完遇到eof之后会重上次读完的地方重新开始读取，因而读完一次之后len变为-1
        while ((len = fi.read(dataBytes)) > 0) {
            //注意byte并没有toString()函数
            dataString = new String(dataBytes, 0, len);
        }
        fi.close();
        return dataString;
    }
}
