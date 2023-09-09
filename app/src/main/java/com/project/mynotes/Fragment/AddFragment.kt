package com.project.mynotes.Fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.mynotes.R
import com.project.mynotes.database.DbManager
import com.project.mynotes.databinding.FragmentAddBinding

private const val ARG_ID = "ID"
private const val ARG_TITLE = "Title"
private const val ARG_DES = "Description"

class AddFragment : Fragment() {
    private var ID: String? = null
    private var Title: String? = null
    private var Description: String? = null

    var id:Int?=0

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ID = it.getString(ARG_ID)
            Title = it.getString(ARG_TITLE)
            Description = it.getString(ARG_DES)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.FragAddBtnAdd.setOnClickListener({
            addNotes()
        })

        // check if edit
        id = arguments?.getInt("ID")
        if(id != 0){
            val title = arguments?.getString("Title")
            val des = arguments?.getString("Description")
            // setText(title) : لانو قيمة بقدر اعدل عليها
            binding.FragAddTitle.setText(title)
            binding.FragAddDes.setText(des)
        }

        if (id != null && id!! > 0) {
            // تم التعديل، عرض نص "Edit" على زر الإضافة
            binding.FragAddBtnAdd.setText("Edit")
        } else {
            // لم يتم التعديل، عرض نص "Add" على زر الإضافة
            binding.FragAddBtnAdd.setText("Add")
        }

        return binding.root
    }

    fun addNotes(){
        val title = binding.FragAddTitle.text.toString()
        val des = binding.FragAddDes.text.toString()

        val dbManager = DbManager(this!!.activity!!)

        // نجهز ال content value
        val values = ContentValues()
        values.put("Description", des)
        values.put("Title", title)

        if (id != null && id!! > 0) {
            // تم التعديل على السجل، قم بتحديثه بدلاً من إعادة إدراجه
            val selection = "ID = ?"
            val selectionArgs = arrayOf(id.toString())
            val rowCount = dbManager.update(values, selection, selectionArgs)

            if (rowCount > 0) {
                Log.e("dbManager", "updated")
            } else {
                Log.e("dbManager", "update failed")
            }
        } else {

            // لم يتم التعديل، إذا قم بإدراج السجل
            val newId = dbManager.insertNote(values)

            if (newId > 0) {
                Log.e("dbManager", "inserted")
            } else {
                Log.e("dbManager", "insert failed")
            }
        }

        findNavController().navigate(R.id.action_addFragment_to_ListFragment)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, ID)
                    putString(ARG_TITLE, Title)
                    putString(ARG_DES, Description)
                }
            }
    }


    // عشان نظهر ال menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater?.inflate(R.menu.notes_add,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // عند الضغط على الايقونة تبعت ال menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item!!.itemId){
            R.id.MenuBack -> {
                findNavController().navigate(R.id.action_addFragment_to_ListFragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}