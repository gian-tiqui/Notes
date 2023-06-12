package com.giantiqs.notesnoob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.giantiqs.notesnoob.Activities.LoginActivity;
import com.giantiqs.notesnoob.Activities.NoteDetailsActivity;
import com.giantiqs.notesnoob.Classes.Note;
import com.giantiqs.notesnoob.Classes.NoteAdaptor;
import com.giantiqs.notesnoob.Utilities.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    NoteAdaptor noteAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);

        addNoteBtn.setOnClickListener(v -> startActivity(
                new Intent(
                MainActivity.this,
                NoteDetailsActivity.class
                )
        ));

        menuBtn.setOnClickListener(v -> showMenu());

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = Utility
                .getCollectionRefsForNotes()
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteAdaptor = new NoteAdaptor(options, this);
        recyclerView.setAdapter(noteAdaptor);
    }

    @Override
    protected void onStart() {
        super.onStart();

        noteAdaptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        noteAdaptor.stopListening();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();

        noteAdaptor.notifyDataSetChanged();
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuBtn);

        popupMenu.getMenu().add("Logout");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle()=="Logout") {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

                return true;
            }

            return false;
        });


    }
}