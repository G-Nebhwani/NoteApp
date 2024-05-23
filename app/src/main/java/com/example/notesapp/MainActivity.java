package com.example.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private static final String PREFS_NAME="NotePrefs";
private static final String KEY_NOTE_COUNT="NoteCount";
private LinearLayout notesContainer;
private List<Note> noteList;
Button saveButton;
//first change
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesContainer=findViewById(R.id.notesContainer);
        saveButton=findViewById(R.id.SaveButton);

        noteList=new ArrayList<>();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
        loadNotesFromPreferences();
        DisplayNote();

    }

    private void DisplayNote() {
        for(Note note :noteList)
        {
            createNoteView(note);
        }
    }

    private void loadNotesFromPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        int notesCount =sharedPreferences.getInt(KEY_NOTE_COUNT,0);

        for(int i=0;i<notesCount;i++)
        {
            String title=sharedPreferences.getString("Note_Title"+i," ");
            String content=sharedPreferences.getString("Note_Content"+i," ");

            Note note=new Note();
            note.setTitle(title);
            note.setContent(content);

            noteList.add(note);
        }
    }

    private void saveNote() {
        EditText titleEditText=findViewById(R.id.titleEditText);
        EditText contentEditText=findViewById(R.id.contentEditText);

        String title=titleEditText.getText().toString();
        String content=contentEditText.getText().toString();

        if(!title.isEmpty() && !content.isEmpty()){
            Note note=new Note();
            note.setTitle(title);
            note.setContent(content);


            noteList.add(note);

            saveNoteToPreferences();
            createNoteView(note);

            clearInputFeild();
        }
    }

    private void clearInputFeild() {
        EditText titleEditText=findViewById(R.id.titleEditText);
        EditText contentEditText=findViewById(R.id.contentEditText);

        titleEditText.getText().clear();
        contentEditText.getText().clear();

    }

    private void createNoteView(final Note note) {
        View noteView =getLayoutInflater().inflate(R.layout.note_item,null);
        TextView titleTxtView=noteView.findViewById(R.id.TitleTxtView);
        TextView contentTextView=noteView.findViewById(R.id.contentTxtView);

        titleTxtView.setText(note.getTitle());
        contentTextView.setText(note.getContent());

        noteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDeleteDialog(note);
                return true;
            }
        });

        notesContainer.addView(noteView);
    }

    private void showDeleteDialog(final Note note) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Delete this Note");
        builder.setMessage("Are you sure to delete this note ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteNoteAndReferesh(note);
            }
        });

        builder.setNegativeButton("Cancle",null);
        builder.show();
    }

    private void DeleteNoteAndReferesh(Note note ) {
        noteList.remove(note);
        saveNoteToPreferences();
        
        RefreshNoteView();
    }

    private void RefreshNoteView() {
        notesContainer.removeAllViews();
        DisplayNote();
    }

    private void saveNoteToPreferences() {
        SharedPreferences sharedPreferences= getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt(KEY_NOTE_COUNT,noteList.size());

        for(int i=0; i<noteList.size();i++)
        {
            Note note= noteList.get(i);
            editor.putString("Note_title_"+ i ,note.getTitle());
            editor.putString("Note_Content_"+ i ,note.getContent());
        }
        editor.apply();
    }
}