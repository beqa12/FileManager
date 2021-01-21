package com.example.filemanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.filemanager.adapter.FileRecyclerAdaapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FileRecyclerAdaapter.IFileClickListener {
    private FileRecyclerAdaapter adaapter;
    private RecyclerView fileRecycler;
    private File currentFile;
    private Button addFileBtn;
    private Button delete;
    Button file;
    Button folder;
    EditText input;
    private List<File> checkedFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        delete = findViewById(R.id.delete_btn);
        addFileBtn = findViewById(R.id.add_file);
        fileRecycler = findViewById(R.id.file_recycler);
        adaapter = new FileRecyclerAdaapter();
        fileRecycler.setLayoutManager(new LinearLayoutManager(this));
        fileRecycler.setAdapter(adaapter);
        adaapter.addFiles(Arrays.asList(getFiles()));
        adaapter.setFileItemClickListener(this);
        setClickClicklisteners();
    }

    private void setClickClicklisteners() {
        addFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                dialog.setTitle("ფაილის შექმნა");
                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_layout, null, false);
                dialog.setView(v);
                file = v.findViewById(R.id.file);
                folder = v.findViewById(R.id.folder);
                input = v.findViewById(R.id.file_edit_text);
                folder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String fileName = input.getText().toString();
                        File file = new File(currentFile.getPath(), fileName);
                        try {
                            if (file.mkdir()) {
                                dialog.dismiss();
                                adaapter.addFiles(Arrays.asList(currentFile.listFiles()));
                                Toast.makeText(MainActivity.this, "Sheiqmna", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Ar Sheiqmna", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String fileName = input.getText().toString();
                        File file = new File(currentFile.getPath(), fileName);
                        try {
                            if (file.createNewFile()) {
                                dialog.dismiss();
                                adaapter.addFiles(Arrays.asList(currentFile.listFiles()));
                                Toast.makeText(MainActivity.this, "Sheiqmna", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Ar Sheiqmna", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog.show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < checkedFiles.size(); i++) {
                    File file = checkedFiles.get(i);
                    checkedFiles.remove(file);
                    file.delete();
                }
                adaapter.addFiles(Arrays.asList(currentFile.listFiles()));
            }
        });
    }

    private File[] getFiles() {
        return getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS);
    }


    @Override
    public void fileItemClicked(File file) {
        currentFile = file;
        if (currentFile.isDirectory()) {
            adaapter.addFiles(Arrays.asList(file.listFiles()));
        }
    }

    @Override
    public void selectedFiles(File file, boolean isSelected) {
        if (isSelected) {
            checkedFiles.add(file);
        } else {
            checkedFiles.remove(file);
        }
    }

    @Override
    public void onBackPressed() {
        if (currentFile != null) {
            adaapter.addFiles(Arrays.asList(currentFile.getParentFile().listFiles()));
            currentFile = currentFile.getParentFile();
        } else {
            super.onBackPressed();
        }
    }
}