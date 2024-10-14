package com.example.sqlpractice2hw

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlpractice2hw.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    private var products: MutableList<Product> = mutableListOf()
    private var listViewAdapter: ListAdapter? = null
    private val db = DBHelper(this, null)

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.secondActivitySaveButtonBTN.setOnClickListener{

            saveProduct()
        }

        binding.secondActivityDeleteButtonBTN.setOnClickListener{
            db.removeAll()
            products.clear()
            clearFields()
            viewDataAdapter()
        }

        binding.secondActivityExitButtonBTN.setOnClickListener {
            this.finishAffinity()
        }

        binding.secondActivityMainListViewLV.onItemClickListener =
            AdapterView.OnItemClickListener(){
            parent, view, position, id ->

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Внимание!")
                    .setMessage("Предполагаемые действия")
                    .setNegativeButton("Закрыть",null)
                    .setPositiveButton("Удалить"){ _,_ ->
                        val product = listViewAdapter!!.getItem(position)
                        if (product != null) {
                            db.deleteProduct(product)
                        }
                        viewDataAdapter()
                        Toast.makeText(this,"Запись удалена!", Toast.LENGTH_SHORT).show()
                    }
                    .setNeutralButton("Обновить"){ _,_ -> updateRecord() }

                builder.create().show()
        }


    }

    override fun onResume() {
        super.onResume()

        binding.secondActivityUpdateButtonBTN.setOnClickListener {
            updateRecord()
        }

        binding.secondActivityDeleteButtonBTN.setOnClickListener {
            deleteRecord()
        }


    }

    @SuppressLint("MissingInflatedId")
    private fun updateRecord(){

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog,null)
        dialogBuilder.setView(dialogView)

        val editName = dialogView.findViewById<EditText>(R.id.updateDialogNameProductEditTextET)
        val editId = dialogView.findViewById<EditText>(R.id.updateDialogIdProductTextET)
        val editWeight = dialogView.findViewById<EditText>(R.id.updateDialogWeightEditTextET)
        val editPrice = dialogView.findViewById<EditText>(R.id.updateDialogPriceEditTextET)

        dialogBuilder.setTitle("Обновить запись")
        dialogBuilder.setMessage("Введите данные ниже")
        dialogBuilder.setPositiveButton("Обновить") { _,_ ->

            val updateId = editId.text.toString()
            val updateName = editName.text.toString()
            val updateWeight = editWeight.text.toString()
            val updatePrice = editPrice.text.toString()

            if (updateId.trim() != "" && updateName.trim() != "" && updateWeight.trim()
                != "" && updatePrice.trim() != "") {
                val product = Product(Integer.parseInt(updateId), updateName,
                    updateWeight,updatePrice)

                db.updateProduct(product)
                viewDataAdapter()
                Toast.makeText(this,"Данные обновлены", Toast.LENGTH_SHORT).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена") { dialog, which ->
        }
        dialogBuilder.create().show()



    }

    private fun deleteRecord(){

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog,null)
        dialogBuilder.setView(dialogView)

        val chooseDeleteId = dialogView.findViewById<EditText>(R.id.deleteDialogWeightEditTextET)
        dialogBuilder.setTitle("Удалить запись")
        dialogBuilder.setMessage("Введите идентификатор")
        dialogBuilder.setPositiveButton("Удалить") { _,_ ->
            val deletedId = chooseDeleteId.text.toString()
            if (deletedId.trim() != ""){
                val product = Product(Integer.parseInt(deletedId), "","","")
                db.deleteProduct(product)
                viewDataAdapter()
                Toast.makeText(this, "Запись удалена", Toast.LENGTH_SHORT).show()
            }

        }
        dialogBuilder.setNegativeButton("Отмена") {_,_ ->}
        dialogBuilder.create().show()


    }

    private fun saveProduct() {
        viewDataAdapter()

        if (binding.secondActivityProductEditTextET.text.isNotEmpty()
            && binding.secondActivityWeightEditTextET.text.isNotEmpty()
            && binding.secondActivityPriceEditTextET.text.isNotEmpty()
        ) {

            val id = binding.secondActivityProductIdEditTextET.text.toString()
            val name = binding.secondActivityProductEditTextET.text.toString()
            val weight = binding.secondActivityWeightEditTextET.text.toString()
            val price = binding.secondActivityPriceEditTextET.text.toString()

            if (name.trim() != "" && weight.trim() != ""
                && price.trim() != ""
            ) {
                val product = Product(id.toInt(),name, weight, price)
                db.addProduct(product)
                viewDataAdapter()
                clearFields()

                Toast.makeText(this, "Запись сохранена", Toast.LENGTH_SHORT).show()
            }


        } else {
            Toast.makeText(
                this, "Заполните необходимые поля",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun viewDataAdapter() {
        products = db.getInfo()
        listViewAdapter = ListAdapter(this, products)
        binding.secondActivityMainListViewLV.adapter = listViewAdapter
        listViewAdapter!!.notifyDataSetChanged()

    }

    private fun clearFields() {
        binding.secondActivityProductIdEditTextET.text.clear()
        binding.secondActivityProductEditTextET.text.clear()
        binding.secondActivityWeightEditTextET.text.clear()
        binding.secondActivityPriceEditTextET.text.clear()
    }


}