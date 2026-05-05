package com.example.tugas4databasee.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas4databasee.R
import com.example.tugas4databasee.database.entity.StudentEntity

class StudentAdapter(
    private var studentList: List<StudentEntity>,
    private val onDeleteClick: (StudentEntity) -> Unit,
    private val onItemClick: (StudentEntity) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvNim: TextView = itemView.findViewById(R.id.tv_nim)
        val tvProdi: TextView = itemView.findViewById(R.id.tv_prodi)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.tvName.text = student.name
        holder.tvNim.text = "NIM: ${student.nim}"
        holder.tvProdi.text = "Prodi: ${student.prodi}"

        holder.btnDelete.setOnClickListener {
            onDeleteClick(student)
        }

        holder.itemView.setOnClickListener {
            onItemClick(student)
        }
    }

    override fun getItemCount(): Int = studentList.size

    fun updateData(newList: List<StudentEntity>) {
        studentList = newList
        notifyDataSetChanged()
    }
}