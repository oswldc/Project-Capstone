package com.dicoding.gencara.ui.practice

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.gencara.BuildConfig
import com.dicoding.gencara.R
import com.dicoding.gencara.data.image_config.ImageRetrofit
import com.dicoding.gencara.data.response.PredictionResponse
import com.dicoding.gencara.databinding.ActivitySignPracticeBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class SignPracticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignPracticeBinding
    private lateinit var questionNames: Array<String>
    private var currentPosition: Int = 0
    private val REQUEST_CODE_CAMERA = 123
    private val imageRetrofit = ImageRetrofit()
    private var file: File? = null
    private var uri: Uri? = null
    private val alphabetImages = intArrayOf(
        R.drawable.a,
        R.drawable.b,
        R.drawable.c,
        R.drawable.d,
        R.drawable.e,
        R.drawable.f,
        R.drawable.g,
        R.drawable.h,
        R.drawable.i,
        R.drawable.j,
        R.drawable.k,
        R.drawable.l,
        R.drawable.m,
        R.drawable.n,
        R.drawable.o,
        R.drawable.p,
        R.drawable.q,
        R.drawable.r,
        R.drawable.s,
        R.drawable.t,
        R.drawable.u,
        R.drawable.v,
        R.drawable.w,
        R.drawable.x,
        R.drawable.y,
        R.drawable.z
    )
    private val alphabetNames = arrayOf(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z"
    )
    private fun getRandomAlphabetData(): Pair<Int, String> {
        val randomIndex = (alphabetImages.indices).random()
        return Pair(alphabetImages[randomIndex], alphabetNames[randomIndex])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuName = intent.getStringExtra("MENU_NAME")
        val menuIcon = intent.getIntExtra("MENU_ICON", -1)
        questionNames = resources.getStringArray(R.array.data_question)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnPrev.setOnClickListener {
            if (currentPosition > 0) {
                currentPosition--
                updateContent()
            }
        }

        binding.btnNext.apply {
            isEnabled = false
            setOnClickListener {
                if (currentPosition < questionNames.size - 1) {
                    binding.ivCamera.setImageBitmap(null)
                    binding.etDetectionResult.setText(" ")
                    currentPosition++
                    updateContent()
                }
            }
        }

        binding.btnCamera.setOnClickListener {
            checkCameraPermission()
        }

        updateContent()
    }

    private fun updateContent() {
        binding.btnPrev.visibility =
            if (currentPosition == 0) android.view.View.GONE else android.view.View.VISIBLE
        binding.btnNext.visibility =
            if (currentPosition == questionNames.size - 1) android.view.View.GONE else android.view.View.VISIBLE
        binding.btnNext.setColorFilter(ContextCompat.getColor(this@SignPracticeActivity, android.R.color.darker_gray))

        val (alphabetImage, alphabetName) = getRandomAlphabetData()
        binding.ivIcon.setImageResource(alphabetImage)
        binding.tvSignName.text = alphabetName
        file = null
        uri = null
    }

    private fun checkCameraPermission(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
        else{
            capturePhoto()
        }
    }

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            capturePhoto()
        }

    private fun capturePhoto(){
        file = File(externalCacheDir, "My_Captured_Photo.png")
        if(file!!.exists()) {
            file!!.delete()
        }
        file!!.createNewFile()
        uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
            file!!)

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

//    private fun openCamera() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(packageManager) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
//        } else {
//            // Tidak ada aplikasi kamera yang tersedia
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            val bitmap = BitmapFactory.decodeStream(
                contentResolver.openInputStream(uri!!))
            binding.ivCamera.setImageBitmap(bitmap)

            sendImageToAPI(bitmap)
        }
    }

    private fun sendImageToAPI(imageBitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val imageBytes = stream.toByteArray()

        val requestBody = RequestBody.create("image/png".toMediaTypeOrNull(), imageBytes)
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            file?.name.orEmpty(),
            requestBody
        )
        val call = imageRetrofit.instance.postPredict(multipartBody)
        call.enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(
                call: Call<PredictionResponse>,
                response: Response<PredictionResponse>
            ) {
                if (response.isSuccessful) {
                    val prediction = response.body()?.prediction
                    runOnUiThread {
                        binding.etDetectionResult.setText(prediction)
                        if (prediction.isNullOrEmpty().not()) {
                            binding.btnNext.isEnabled = true
                            binding.btnNext.clearColorFilter()
                            Toast.makeText(
                                this@SignPracticeActivity,
                                "Berhasil",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    // Handle error response
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                runOnUiThread {
                    binding.etDetectionResult.setText("Gagal menghubungkan ke API")
                }
            }
        })
    }
}