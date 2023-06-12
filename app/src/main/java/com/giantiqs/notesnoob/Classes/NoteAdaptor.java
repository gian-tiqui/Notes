package com.giantiqs.notesnoob.Classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.giantiqs.notesnoob.Activities.NoteDetailsActivity;
import com.giantiqs.notesnoob.R;
import com.giantiqs.notesnoob.Utilities.Utility;

public class NoteAdaptor extends FirestoreRecyclerAdapter<Note, NoteAdaptor.NoteViewHolder> {

    Context context;

    public NoteAdaptor(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);

        this.context = context;
    }

    @Override
    protected void onBindViewHolder(
            @NonNull NoteViewHolder holder,
            int position,
            @NonNull Note note) {

        holder.titleTxtView.setText(note.getTitle());
        holder.contentTxtView.setText(note.getContent());
        holder.timestampTxtView.setText(Utility.timestampToString(note.getTimestamp()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteDetailsActivity.class);

            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());

            String docId = this.getSnapshots().getSnapshot(position).getId();

            intent.putExtra("docId", docId);

            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_note_item, parent, false);

        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleTxtView, contentTxtView, timestampTxtView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxtView = itemView.findViewById(R.id.note_title_txt_view);
            contentTxtView = itemView.findViewById(R.id.note_content_txt_view);
            timestampTxtView = itemView.findViewById(R.id.timestamp_txt_view);


        }
    }
}
