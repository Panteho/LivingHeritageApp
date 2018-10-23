package com.people_patterns.livingheritage

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.people_patterns.livingheritage.base.BaseActivity
import com.people_patterns.livingheritage.model.Tree
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_tag_tree.*
import org.jetbrains.annotations.NotNull
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TagTreeActivity : BaseActivity() {

    lateinit var txtCompanyName: TextView;
    lateinit var txtLocation: TextView;
    lateinit var edtTreeName: EditText;
    lateinit var edtTreeDescription: EditText;
    lateinit var btnTagTree: Button;
    lateinit var ivLogo: ImageView;

    var mCurrentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_tree)
        initViews()
        showAddTreeDialog()
    }

    private fun initViews() {
        txtCompanyName = findViewById(R.id.txt_company_name)
        txtLocation = findViewById(R.id.txt_location)
        edtTreeName = findViewById(R.id.edt_treename)
        edtTreeDescription = findViewById(R.id.edt_treedescription)
        btnTagTree = findViewById(R.id.btn_tagtree)
        ivLogo = findViewById(R.id.iv_logo)
        ivLogo.setOnClickListener {
            dispatchTakePictureIntent()
        }
        btnTagTree.setOnClickListener{
            var lat = getbaseApp().location.latitude
            var long = getbaseApp().location.longitude
            if(intent.hasExtra("latitude")) {
                lat = intent.getDoubleExtra("latitude", 0.0)
                long = intent.getDoubleExtra("longitude", 0.0)
            }
            val userId = loggedInEmail;
            val tree = Tree(edtTreeName.text.toString(), "", edtTreeDescription.text.toString(), userId, "", lat, long)
            showProgress()
            if (mCurrentPhotoPath != null) {
                uploadImage(File(mCurrentPhotoPath ), tree, object : UploadFileCallback {
                    override fun uploadFailed() {

                    }

                    override fun uploadSuccess() {
                        tree.imagePath = Uri.parse(mCurrentPhotoPath).lastPathSegment;
                        createTree(tree, {
                            hideProgress()
                            finish()
                        })
                    }
                })
            }
            else {
                createTree(tree, {
                    hideProgress()
                    finish()
                })
            }
        }
    }

    private fun showAddTreeDialog() {
        if (getbaseApp().location != null) {
            txtLocation.text = "Latitude:" + getbaseApp().location.latitude + "Longitude:" + getbaseApp().location.longitude
        }
        else {
            txtLocation.text = "Make sure you have enabled location services and also granted required permissions"
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.people_patterns.livingheritage",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            val compressedImageFile = Compressor(this).compressToFile(File(mCurrentPhotoPath));
            mCurrentPhotoPath = compressedImageFile.absolutePath
            ivLogo.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath))
        }
    }
}
