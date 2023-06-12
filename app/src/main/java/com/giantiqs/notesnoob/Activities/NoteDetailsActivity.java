package com.giantiqs.notesnoob.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.giantiqs.notesnoob.Classes.Note;
import com.giantiqs.notesnoob.R;
import com.giantiqs.notesnoob.Utilities.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditTxt, contentEditTxt;
    ImageButton saveNoteBtn;
    TextView titleTxtView, deleteNoteTxtView;
    String title, content, docId;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditTxt = findViewById(R.id.notes_title_text);
        contentEditTxt = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        titleTxtView = findViewById(R.id.page_title);
        deleteNoteTxtView = findViewById(R.id.delete_note_btn);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        if (isEditMode) {
            titleTxtView.setText("Edit your note");
            deleteNoteTxtView.setVisibility(View.VISIBLE);
        }

        titleEditTxt.setText(title);
        contentEditTxt.setText(content);

        saveNoteBtn.setOnClickListener(v -> saveNote());

        deleteNoteTxtView.setOnClickListener(v -> deleteNote());
    }

    private void deleteNote() {

        DocumentReference documentReference;

        documentReference= Utility.getCollectionRefsForNotes().document(docId);


        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utility.showToast(NoteDetailsActivity.this, "Note Deleted");
                finish();
            } else {
                Utility.showToast(NoteDetailsActivity.this, "Failed deleting note");
            }
        });
    }

    private void saveNote() {
        String noteTitle = titleEditTxt.getText().toString();
        String noteContent = contentEditTxt.getText().toString();

        if (noteTitle.isEmpty()) {
            titleEditTxt.setError("Title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }

    private void saveNoteToFirebase(Note note) {

        DocumentReference documentReference;

        if (isEditMode) {
            documentReference= Utility.getCollectionRefsForNotes().document(docId);
        } else {
            documentReference= Utility.getCollectionRefsForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utility.showToast(NoteDetailsActivity.this, "Note added");
                finish();
            } else {
                Utility.showToast(NoteDetailsActivity.this, "Failed adding note");
            }
        });
    }
}
