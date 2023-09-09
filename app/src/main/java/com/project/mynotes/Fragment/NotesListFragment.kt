package com.project.mynotes.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.mynotes.Adapter.NotesAdapter
import com.project.mynotes.Model.Notes
import com.project.mynotes.R
import com.project.mynotes.database.DbManager
import com.project.mynotes.databinding.FragmentNotesListBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NotesListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    val notesList = ArrayList<Notes>()
    lateinit var notesAdapter: NotesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {
        var recyclerView: RecyclerView? = null
        var image: ImageView? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)

        recyclerView = binding.NotesListRecycler
        image = binding.NotesListNotFound

        setHasOptionsMenu(true)
        //"%": يعني اريد كل شي في الداتابيز

        if (notesList.isEmpty()) {
            binding.NotesListRecycler.visibility = View.GONE
            binding.NotesListNotFound.visibility = View.VISIBLE
        }
        querySearch("%")

        return binding.root
    }

    @SuppressLint("Range")
    fun querySearch(noteTitle: String) {
        var dbManager = DbManager(this!!.activity!!)

        // selectionArgs : وفق ايش راح ارتب ؟
        val selectionArgs = arrayOf(noteTitle)

        // projection : الاعمدة اللي بدي اياها
        val projection = arrayOf("ID", "Title", "Description")

        val cursor = dbManager.query(projection, "Title like ?", selectionArgs, "Title")

        if (cursor.moveToFirst()) {
            notesList.clear()
            do {
                val id = cursor.getInt(cursor.getColumnIndex("ID"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val des = cursor.getString(cursor.getColumnIndex("Description"))

                binding.NotesListRecycler.visibility = View.VISIBLE
                binding.NotesListNotFound.visibility = View.GONE

                notesList.add(Notes(id, title, des))
            } while (cursor.moveToNext())
        }

        notesAdapter =
            NotesAdapter(notesList, this!!.activity!!, object : NotesAdapter.ClickListener {
                override fun delete(pos: Int) {
                    val NotesItem = notesList[pos]
                    Log.e("notesListF", notesList.isEmpty().toString())
                    Log.e("pos", pos.toString())

                    val dbManager = DbManager(requireContext())
                    val selectionArgs = arrayOf(NotesItem.id.toString())

                    val deletedRowCount = dbManager.delete("ID=?", selectionArgs)
                    Log.e("deletedRowCount", deletedRowCount.toString())

                    if (deletedRowCount == 1 && pos == 0) {

                        Log.e("delete", "delete")
                        notesList.clear()
                        // إذا فشل الحذف، يمكنك تنفيذ التحقق هنا
                        if (notesList.isEmpty()) {

                            Log.e("notesListA", notesList.isEmpty().toString())
//                            val yourRecyclerView = NotesListFragment.recyclerView
//                            val yourImage = NotesListFragment.image

                            binding.NotesListRecycler.visibility = View.GONE
                            binding.NotesListNotFound.visibility = View.VISIBLE
                        }
                    }
                    querySearch("%")
                }

                override fun edit(pos: Int) {
                    val notes = notesList[pos]
                    goToUpdate(notes)
                }

            })
        binding.NotesListRecycler.layoutManager = LinearLayoutManager(this!!.activity!!)
        binding.NotesListRecycler.adapter = notesAdapter


    }

    // عشان نظهر ال menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater?.inflate(R.menu.notes_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // عند الضغط على الايقونة تبعت ال menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item!!.itemId) {
            R.id.MenuAdd -> {
                findNavController().navigate(R.id.action_ListFragment_to_addFragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun goToUpdate(notes: Notes) {
        var bundle = Bundle()
        bundle.putInt("ID", notes.id!!)
        bundle.putString("Title", notes.title!!)
        bundle.putString("Description", notes.des!!)

        findNavController().navigate(R.id.action_ListFragment_to_addFragment, bundle)
    }
}