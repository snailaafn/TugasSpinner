package com.example.tugasspinner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract.Calendars
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tugasspinner.databinding.ActivityMainBinding
import com.example.tugasspinner.databinding.ActivitySecondBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SecondActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivitySecondBinding
    private val calendar = Calendar.getInstance()
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var selectedRepeat: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stringRepeat = resources.getStringArray(R.array.repeat)
        val adapterSpinnerRepeat = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stringRepeat)
        binding.spinnerRepeat.adapter = adapterSpinnerRepeat
        binding.spinnerRepeat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedRepeat = stringRepeat.get(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val selection = "Once"
        val spinnerPosition: Int = adapterSpinnerRepeat.getPosition(selection)
        binding.spinnerRepeat.setSelection(spinnerPosition)
    }

    fun showCalendar(view: View) {
        val datePickerDialog = DatePickerDialog(
            this, this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)

        val dateFormat = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
        selectedDate = dateFormat.format(calendar.time)

        binding.txtSelectDate.text = selectedDate
    }

    fun showTimePicker(view: View){
        val timePickerDialog = TimePickerDialog(
            this, this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        binding.txtTimePicker.text = selectedTime
    }

    fun showAlert(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("SimpliRemind")
        builder.setMessage("Do you want to add this as new task?")
        builder.setPositiveButton("Yes"){
                dialog, _ ->
            val intent = Intent(this, ThirdActivity::class.java)
            val title = binding.insertTitle.text.toString()
            intent.putExtra("SELECTED_DATE", selectedDate)
            intent.putExtra("SELECTED_TIME", selectedTime)
            intent.putExtra("SELECTED_REPEAT", selectedRepeat)
            intent.putExtra("TITLE", title)
            startActivity(intent)
        }
        builder.setNeutralButton("No"){
                dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}