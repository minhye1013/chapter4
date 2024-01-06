package com.gdsc_android.chapter4

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.gdsc_android.chapter4.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        val editButton = binding.floatingActionButton.setOnClickListener {
//            setContentView(R.layout.activity_input)
//        }

        binding.goInputActivityButton.setOnClickListener{
            //명시적 인텐트
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

        //초기화 버튼 구현
        binding.deleteButton.setOnClickListener {
            deleteData()
        }

        //전화번호 버튼 누르면 전화 걸 수 있게
        binding.emergencyContactLayer.setOnClickListener{
            //유저의 핸드폰에 어떤 전화 앱을 사용하는지 모르기 때문에 정확한 지시 어려워 암시적 인텐트
            with(Intent(Intent.ACTION_VIEW)) {
                val phoneNumber = binding.emergencyContactValueTextView.text.toString()
                    .replace("-","")
                data = Uri.parse("tel:$phoneNumber")
                startActivity(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getDataUiUpdate()
    }

    private fun getDataUiUpdate() {
        //userInformation같은 키값은 하드코딩하지 않고 따로 파일로 저장해두는 게 편함
        with(getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE)) {
            binding.nameValueTextView.text = getString(NAME,"미입력")
            binding.birthdateValueTextView.text = getString(BIRTHDATE,"미입력")
            binding.bloodtypeValueTextView.text = getString(BLOOD_TYPE,"미입력")
            binding.emergencyContactValueTextView.text = getString(EMERGENCY_CONTACT,"미입력")
            val warning = getString(WARNING,"")

            //binding 어쩌구가 너무 반복되고 복잡하니까 A상태면 B 실행해주고, B상태면 A 실행해줘 명령
            binding.warningTextView.isVisible = !warning.isNullOrEmpty()
            binding.warningValueTextView.isVisible = !warning.isNullOrEmpty()

            if(!warning.isNullOrEmpty()) {
//                binding.textview5.isVisible = false
//                binding.commentValueTextView.isVisible = false
//            } else {
//                binding.textview5.isVisible = true
//                binding.commentValueTextView.isVisible = true
                binding.warningValueTextView.text = warning
            }
        }
    }

    private fun deleteData() {
        with(getSharedPreferences(USER_INFORMATION, MODE_PRIVATE).edit()) {
            clear()
            apply()   //얘가 있어야 화면이 진짜로 초기화된 데이터로 보임
            getDataUiUpdate()
        }
        Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show()
    }
}