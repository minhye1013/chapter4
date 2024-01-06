package com.gdsc_android.chapter4

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.gdsc_android.chapter4.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //adapter는 리스트나 배열로 있는 데이터를 UI로 연결시켜주는 애
        binding.bloodTypeSpinner.adapter = ArrayAdapter.createFromResource(this, R.array.blood_types, android.R.layout.simple_list_item_1)

        binding.birthdateLayer.setOnClickListener {
            val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayofMonth ->
                binding.birthdateValueTextView.text = "$year-${month.inc()}-$dayofMonth"
            }
            DatePickerDialog(this, listener,2000,1,1).show()
        }

        //주의사항 노출 체크 박스 여부에 따라 주의사항이 보여지는지 안보여지는지 결정
        binding.warningCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.warningEditText.isVisible = isChecked
        }
        binding.warningEditText.isVisible = binding.warningCheckBox.isChecked


        //saveButton 누르면 데이터 저장하기
        binding.saveButton.setOnClickListener {
            saveData()
            finish()
        }

    }
    //onCreate() 함수가 길어져 따로 뺐다
    private fun saveData() {
        with(getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE).edit()) {
            putString(NAME, binding.nameEditText.text.toString())
            putString(BLOOD_TYPE,getBloodType())
            putString(EMERGENCY_CONTACT, binding.emergencyContactEditText.text.toString())
            putString(BIRTHDATE, binding.birthdateValueTextView.text.toString())
            putString(WARNING,getWarning())
            apply()   //.commit()은 데이터를 저장하는 동안 사용자의 동작을 막을 수 없기 때문에 .apply()를 통해 비동기적으로 다른 스레드를 열어서 저장을 시켜줌
        }

        //editor가 너무 반복되어 보기 싫으니 스코프 함수로 대체하겠다 (위에)
//        val editor = getSharedPreferences("userInformation", Context.MODE_PRIVATE).edit()
        //putString() 내 첫 값은 하드코딩하지 않고 Const.kt 파일로 따로 빼서 상수 관리 (적용 버전 위에)
//        editor.putString("name", binding.nameEditText.text.toString())
//        editor.putString("bloodType",)
//        editor.putString("emergencyContact", binding.emergencyContactEditText.text.toString())
//        editor.putString("birthDate", binding.birthdateTextView.text.toString())
//        editor.putString("warning",)
//        editor.apply()   //.commit()은 데이터를 저장하는 동안 사용자의 동작을 막을 수 없기 때문에 .apply()를 통해 비동기적으로 다른 스레드를 열어서 저장을 시켜줌

        Toast.makeText(this, "저장이 완료되었습니다.",Toast.LENGTH_SHORT).show()
    }

    private fun getBloodType() : String {
        val bloodAlphabet = binding.bloodTypeSpinner.selectedItem.toString()
        val bloodSign = if(binding.bloodTypePlus.isChecked) "+" else "-"
        return "$bloodSign$bloodAlphabet"
    }

    private fun getWarning () : String {
        return if(binding.warningCheckBox.isChecked) binding.warningEditText.text.toString() else ""
    }
}