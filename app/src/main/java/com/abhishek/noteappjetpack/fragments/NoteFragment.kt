package com.abhishek.noteappjetpack.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishek.noteappjetpack.R
import com.abhishek.noteappjetpack.adapter.NoteListAdapter
import com.abhishek.noteappjetpack.databinding.FragmentNoteBinding
import com.abhishek.noteappjetpack.models.NoteEntity
import com.abhishek.noteappjetpack.utils.NEW_NOTE_ID
import com.abhishek.noteappjetpack.utils.SELECTED_NOTES_KEY
import com.abhishek.noteappjetpack.utils.TAG
import com.abhishek.noteappjetpack.viewmodel.NoteViewModel

class NoteFragment : Fragment(),
    NoteListAdapter.ListItemListener {

    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: FragmentNoteBinding
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)

        requireActivity().title = getString(R.string.app_name)

        binding = FragmentNoteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(
                context, LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }

        viewModel.notesList?.observe(viewLifecycleOwner, Observer {
            Log.i("noteLogging", it.toString())
            adapter = NoteListAdapter(it, this@NoteFragment)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)

            val selectedNotes =
                savedInstanceState?.getParcelableArrayList<NoteEntity>(SELECTED_NOTES_KEY)
            adapter.selectedNotes.addAll(selectedNotes ?: emptyList())
        })

        binding.floatingActionButton.setOnClickListener {
            editNote(NEW_NOTE_ID)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId =
            if (this::adapter.isInitialized &&
                adapter.selectedNotes.isNotEmpty()
            ) {
                R.menu.selected
            } else {
                R.menu.main
            }
        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sample_data -> addSampleData()
            R.id.action_delete -> deleteSelectedNotes()
            R.id.action_delete_all -> deleteAllNotes()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAllNotes(): Boolean {
        viewModel.deleteAllNotes()
        return true
    }

    private fun deleteSelectedNotes(): Boolean {
        viewModel.deleteNotes(adapter.selectedNotes)
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.selectedNotes.clear()
            requireActivity().invalidateOptionsMenu()
        }, 100)
        return true
    }

    private fun addSampleData(): Boolean {
        viewModel.addSampleData()
        return true
    }

    override fun editNote(noteId: Int) {
        Log.i(TAG, "onItemClick: received note id $noteId")
        val action = NoteFragmentDirections.actionEditNote(noteId)
        findNavController().navigate(action)
    }

    override fun onItemSelectionChanged() {
        requireActivity().invalidateOptionsMenu()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (this::adapter.isInitialized) {
            outState.putParcelableArrayList(
                SELECTED_NOTES_KEY,
                adapter.selectedNotes
            )
        }
        super.onSaveInstanceState(outState)
    }

}