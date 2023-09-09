package com.project.mynotes.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.project.mynotes.Fragment.NotesListFragment
import com.project.mynotes.Model.Notes
import com.project.mynotes.R
import com.project.mynotes.database.DbManager
import com.project.mynotes.databinding.ItemNotesBinding

class NotesAdapter(
    private val NotesList: ArrayList<Notes>,
    private val context: Context,
    private val listener: ClickListener
) : RecyclerView.Adapter<NotesAdapter.NotesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {
        val binding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesHolder, position: Int) {
        val NotesItem = NotesList[position]

        holder.title.text = NotesItem.title
        holder.des.text = NotesItem.des

        holder.imgEdit.setOnClickListener {
            listener.edit(holder.adapterPosition)
        }

        holder.imgDelete.setOnClickListener {
            listener.delete(holder.adapterPosition)

        }


    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    inner class NotesHolder(private val binding: ItemNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.ItemTitle
        val imgEdit = binding.ImgEdit
        val imgDelete = binding.ImgDelete
        val des = binding.ItemDes

    }

    interface ClickListener {

        fun delete(pos: Int)

        fun edit(pos: Int)
    }
}

//fun goToUpdate(NotesItem:Notes) {
//    findNavController().navigate(R.id.action_ListFragment_to_addFragment)
//
//
//}

