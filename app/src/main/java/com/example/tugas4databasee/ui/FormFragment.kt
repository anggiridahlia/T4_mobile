package com.example.tugas4databasee.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tugas4databasee.R
import com.example.tugas4databasee.database.AppDatabase
import com.example.tugas4databasee.database.dao.StudentDao
import com.example.tugas4databasee.database.entity.StudentEntity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormFragment : Fragment(R.layout.fragment_form) {

    private lateinit var database: AppDatabase
    private lateinit var studentDao: StudentDao

    private lateinit var etName: TextInputEditText
    private lateinit var etNim: TextInputEditText
    private lateinit var spinnerProdi: AutoCompleteTextView
    private lateinit var etEmail: TextInputEditText
    private lateinit var etSemester: TextInputEditText
    private lateinit var btnSave: MaterialButton
    private lateinit var toolbarForm: MaterialToolbar // Variabel kotak biru

    private var studentIdToEdit: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())
        studentDao = database.studentDao()

        etName = view.findViewById(R.id.et_name)
        etNim = view.findViewById(R.id.et_nim)
        spinnerProdi = view.findViewById(R.id.spinner_prodi)
        etEmail = view.findViewById(R.id.et_email)
        etSemester = view.findViewById(R.id.et_semester)
        btnSave = view.findViewById(R.id.btn_save)

        // Panggil Toolbar
        toolbarForm = view.findViewById(R.id.toolbar_form)

        // Aksi saat panah kembali diklik
        toolbarForm.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val prodiList = arrayOf("Teknik Informatika", "Sistem Informasi", "Teknik Elektro", "Teknik Sipil", "Matematika")
        val adapterProdi = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, prodiList)
        spinnerProdi.setAdapter(adapterProdi)

        arguments?.let {
            studentIdToEdit = it.getInt("EXTRA_ID", -1)
            if (studentIdToEdit != -1) {
                // Ubah teks di dalam kotak biru kalau lagi ngedit
                toolbarForm.title = "Edit Mahasiswa"
                btnSave.text = "UPDATE DATA"

                etName.setText(it.getString("EXTRA_NAME"))
                etNim.setText(it.getString("EXTRA_NIM"))
                spinnerProdi.setText(it.getString("EXTRA_PRODI"), false)
                etEmail.setText(it.getString("EXTRA_EMAIL"))
                etSemester.setText(it.getString("EXTRA_SEMESTER"))
            } else {
                studentIdToEdit = null
            }
        }

        btnSave.setOnClickListener {
            saveStudentData()
        }
    }

    private fun saveStudentData() {
        val name = etName.text.toString().trim()
        val nim = etNim.text.toString().trim()
        val prodi = spinnerProdi.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val semester = etSemester.text.toString().trim()

        if (name.isEmpty() || nim.isEmpty() || prodi.isEmpty() || email.isEmpty() || semester.isEmpty()) {
            Toast.makeText(requireContext(), "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val newStudent = StudentEntity(
                id = studentIdToEdit ?: 0,
                name = name,
                nim = nim,
                prodi = prodi,
                email = email,
                semester = semester
            )

            if (studentIdToEdit != null) {
                studentDao.update(newStudent)
            } else {
                studentDao.insert(newStudent)
            }

            withContext(Dispatchers.Main) {
                val pesan = if (studentIdToEdit != null) "Data berhasil diupdate!" else "Data berhasil disimpan!"
                Toast.makeText(requireContext(), pesan, Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }
}