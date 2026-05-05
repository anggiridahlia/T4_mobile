package com.example.tugas4databasee.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tugas4databasee.database.entity.StudentEntity

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(student: StudentEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(students: List<StudentEntity>)

    @Query("SELECT * FROM student_table ORDER BY name ASC")
    fun getAllStudents(): List<StudentEntity>

    @Query("SELECT * FROM student_table WHERE id = :id")
    fun getStudentById(id: Int): StudentEntity?

    @Query("SELECT * FROM student_table WHERE name LIKE '%' || :keyword || '%' OR nim LIKE '%' || :keyword || '%'")
    fun searchStudents(keyword: String): List<StudentEntity>

    @Update
    fun update(student: StudentEntity)

    @Query("DELETE FROM student_table WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM student_table")
    fun getStudentCount(): Int
}