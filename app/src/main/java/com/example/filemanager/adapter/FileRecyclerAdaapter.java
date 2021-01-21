package com.example.filemanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileRecyclerAdaapter extends RecyclerView.Adapter<FileRecyclerAdaapter.FileHolder> {

    private List<File> files = new ArrayList<>();

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        return new FileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileHolder holder, int position) {
        File file = files.get(position);
        holder.setFiles(file);
    }

    public void addFiles(List<File> fileList) {
        this.files.clear();
        this.files.addAll(fileList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class FileHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        TextView fileCount;
        CheckBox checkBox;

        public FileHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.file_text_name);
            fileCount = itemView.findViewById(R.id.file_count);
            checkBox = itemView.findViewById(R.id.file_check_box);
        }

        public void setFiles(final File file) {
            fileName.setText(file.getName());
            if (file.list() != null)
                fileCount.setText(file.list().length + "");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.fileItemClicked(file);
                    }
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    listener.selectedFiles(file, b);
                }
            });
        }
    }


    public void setFileItemClickListener(IFileClickListener fileItemClickListener){
        this.listener = fileItemClickListener;
    }

    @Nullable
    IFileClickListener listener;
    public interface IFileClickListener{
        void fileItemClicked(File file);
        void selectedFiles(File file, boolean isSelected);
    }
}
