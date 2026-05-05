package com.example.tugas4databasee.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas4databasee.R
import com.example.tugas4databasee.database.AppDatabase
import com.example.tugas4databasee.database.dao.StudentDao
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var studentDao: StudentDao
    private lateinit var adapter: StudentAdapter
    private lateinit var rvSearch: RecyclerView
    private lateinit var etSearch: TextInputEditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        studentDao = AppDatabase.getDatabase(requireContext()).studentDao()
        rvSearch = view.findViewById(R.id.rv_search)
        etSearch = view.findViewById(R.id.et_search)

        rvSearch.layoutManager = LinearLayoutManager(requireContext())
        adapter = StudentAdapter(emptyList(), { _ -> }, { _ -> }) // Mode search, tombol edit/delete dinonaktifkan sementara
        rvSearch.adapter = adapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchStudent(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        searchStudent("")
    }

    private fun searchStudent(keyword: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val result = if (keyword.isEmpty()) studentDao.getAllStudents() else studentDao.searchStudents(keyword)
            withContext(Dispatchers.Main) { adapter.updateData(result) }
        }
    }
}