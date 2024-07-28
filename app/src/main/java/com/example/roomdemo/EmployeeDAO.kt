package com.example.roomdemo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDAO {
    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)

    @Update
    suspend fun  update(employeeEntity: EmployeeEntity)

    @Delete
    suspend fun delete (employeeEntity: EmployeeEntity)

    @Query("select*From `employee-table`")
    fun featchAllEmployees():Flow<List<EmployeeEntity>>

    @Query("select*From `employee-table`where id=:id")
    fun featchEmployeesbyId(id:Int):Flow<EmployeeEntity>

}