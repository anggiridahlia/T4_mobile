package com.example.tugas4databasee.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas4databasee.R
import com.example.tugas4databasee.database.AppDatabase
import com.example.tugas4databasee.database.dao.StudentDao
import com.example.tugas4databasee.database.entity.StudentEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var database: AppDatabase
    private lateinit var studentDao: StudentDao
    private lateinit var adapter: StudentAdapter
    private lateinit var rvStudents: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())
        studentDao = database.studentDao()

        rvStudents = view.findViewById(R.id.rv_students)
        rvStudents.layoutManager = LinearLayoutManager(requireContext())

        adapter = StudentAdapter(
            studentList = emptyList(),
            onDeleteClick = { student ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Data")
                    .setMessage("Yakin ingin menghapus ${student.name}?")
                    .setPositiveButton("Hapus") { _, _ ->
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                            studentDao.deleteById(student.id) // Hapus data

                            val updatedList = studentDao.getAllStudents()
                            withContext(Dispatchers.Main) {
                                adapter.updateData(updatedList) // Refresh layar
                                Toast.makeText(requireContext(), "${student.name} dihapus", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            },
            onItemClick = { student ->
                val bundle = Bundle().apply {
                    putInt("EXTRA_ID", student.id)
                    putString("EXTRA_NAME", student.name)
                    putString("EXTRA_NIM", student.nim)
                    putString("EXTRA_PRODI", student.prodi)
                    putString("EXTRA_EMAIL", student.email)
                    putString("EXTRA_SEMESTER", student.semester)
                }

                val formFragment = FormFragment()
                formFragment.arguments = bundle

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, formFragment)
                    .addToBackStack(null)
                    .commit()
            }
        )
        rvStudents.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            checkAndInsertSampleData()
            loadStudentData()
        }

        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FormFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private suspend fun loadStudentData() {
        withContext(Dispatchers.IO) {
            val studentList = studentDao.getAllStudents()
            withContext(Dispatchers.Main) {
                adapter.updateData(studentList)
            }
        }
    }

    private suspend fun checkAndInsertSampleData() {
        withContext(Dispatchers.IO) {
            if (studentDao.getStudentCount() == 0) {
                val dummyData = listOf(
                    StudentEntity(name = "Ahmad Fauzi", nim = "2024001", prodi = "Teknik Informatika", email = "ahmad@unram.ac.id", semester = "4"),
                    StudentEntity(name = "Budi Santoso", nim = "2024002", prodi = "Sistem Informasi", email = "budi@unram.ac.id", semester = "4"),
                    StudentEntity(name = "Clara Wijaya", nim = "2024003", prodi = "Teknik Informatika", email = "clara@unram.ac.id", semester = "2")
                )
                studentDao.insertAll(dummyData)
            }
        }
    }
}