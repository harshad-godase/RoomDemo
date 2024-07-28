package com.example.roomdemo

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.databinding.DialogUpdateBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        val employeeDAO = (application as EmployeeApp).db.employeeDao()

        binding?.btnAdd?.setOnClickListener {
            addRecord(employeeDAO)
        }

        lifecycleScope.launch {
            employeeDAO.featchAllEmployees().collect{
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list,employeeDAO)
            }
        }

    }

    fun addRecord(employeeDAO: EmployeeDAO){

        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailId?.text.toString()

        if (email.isNotEmpty()&&name.isNotEmpty()){
            lifecycleScope.launch {
                employeeDAO.insert(EmployeeEntity(name = name, email = email))
                Toast.makeText(applicationContext,"Record saved", Toast.LENGTH_LONG).show()

                binding?.etName?.text?.clear()
                binding?.etEmailId?.text?.clear()
            }

        }else{
            Toast.makeText(applicationContext,"Name or Email cannot be blank", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupListOfDataIntoRecyclerView(
        employeeList: ArrayList<EmployeeEntity>,employeeDAO: EmployeeDAO) {

        if (employeeList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(employeeList, { updateId ->
                updateRecordDialog(updateId, employeeDAO)
            }, { deleteId ->
                deleteRecordAlertDialog(deleteId, employeeDAO)
            })
            // Set the LayoutManager that this RecyclerView will use.
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            // adapter instance is set to the recyclerview to inflate the items.
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        } else {

            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }
  private  fun updateRecordDialog(id:Int,employeeDAO: EmployeeDAO){
      val updateDialog = Dialog(this, R.style.Theme_Dialog)
      updateDialog.setCancelable(false)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch {

            employeeDAO.featchEmployeesbyId(id).collect {
                if (it != null){
                    binding.etUpdateName.setText(it.name)
                    binding.etUpdateEmailId.setText(it.email)
                }
            }
        }
        binding.tvUpdate.setOnClickListener {
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()

            if (name.isNotEmpty()&&email.isNotEmpty()){

                lifecycleScope.launch {
                    employeeDAO.update(EmployeeEntity(id,name,email))
                    Toast.makeText(applicationContext,"Record Updated",Toast.LENGTH_LONG).show()
                    updateDialog.dismiss()
                }
            }else{
                Toast.makeText(applicationContext,"Name or Email cannot be blank",Toast.LENGTH_LONG).show()
            }
        }

        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

   private fun deleteRecordAlertDialog(id:Int,employeeDAO: EmployeeDAO){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){
            dialogInterface,_ ->
            lifecycleScope.launch {
                employeeDAO.delete(EmployeeEntity(id))
                Toast.makeText(applicationContext,"Record deleted successfully",Toast.LENGTH_LONG).show()

            }
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No"){
            dialogInterface,which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }
}
