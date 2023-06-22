package com.example.dataregistration

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dataregistration.databinding.ActivityMainBinding
import com.example.dataregistration.databinding.DeleteDialogBoxBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var db: MyDataBaseHelper
    lateinit var rcvmain : RecyclerView
    var list = ArrayList<ModelClass>()

    lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = MyDataBaseHelper(this)


        initview()
    }

    private fun initview() {

        binding.drawermenu.setOnClickListener {
            binding.navigationdrawer.openDrawer(GravityCompat.START)
        }

        binding.addbtn.setOnClickListener {
            var addregistry = Intent(this,Registratiom_Form_Activity::class.java)
            startActivity(addregistry)
            finish()
        }


        list = db.display()
        var title = "Edit details"
        var donebtn = "update"
        adapter = UserAdapter(list,{

            val edit = Intent(this@MainActivity,Registratiom_Form_Activity::class.java)
            edit.putExtra("id",it.id)
            edit.putExtra("fname",it.fname)
            edit.putExtra("lname",it.lname)
            edit.putExtra("age",it.age)
            edit.putExtra("address",it.address)
            edit.putExtra("email",it.email)
            edit.putExtra("phone",it.phone)
            edit.putExtra("gender",it.gender)
            edit.putExtra("status",it.status)
            edit.putExtra("title",title)
            edit.putExtra("update",donebtn)
            edit.putExtra("updateRecord", true)
            startActivity(edit)

            Log.e("TAG", "editinvoke: " +it.id+ " " +it.fname+ " " + it.lname+ " "+it.age)
        },{ id ->
            val dialog = Dialog(this)
            val dialogBinding: DeleteDialogBoxBinding =
                DeleteDialogBoxBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


            dialogBinding.btndeletecancel.setOnClickListener {
                Toast.makeText(this, "Your Transaction has been Canceled", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
            }

            dialogBinding.btndelete.setOnClickListener {
                db.deleteData(id)
                Toast.makeText(this, "Record Deleted", Toast.LENGTH_SHORT).show()
                list = db.display()
                adapter.updatedate(list)
                Log.e("TAG", "deleted record" + id)

                Toast.makeText(this, "Your Transaction has been Deleted", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            dialog.show()
        })
        var LayoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rcvmain.layoutManager=LayoutManager
        binding.rcvmain.adapter=adapter

        list = db.display()
        adapter.updatedate(list)


    }
}