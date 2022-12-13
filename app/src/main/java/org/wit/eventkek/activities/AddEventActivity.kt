package org.wit.eventkek.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.eventkek.R
import org.wit.eventkek.databinding.ActivityAddEventBinding
import org.wit.eventkek.helpers.showImagePicker
import org.wit.eventkek.main.MainApp
import org.wit.eventkek.models.EventModel
import timber.log.Timber.i

class AddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding
    var event = EventModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Add_Event Activity started...")

        if (intent.hasExtra("event_edit")) {
            edit = true
            event = intent.extras?.getParcelable("event_edit")!!
            binding.eventTitle.setText(event.title)
            binding.description.setText(event.description)
            binding.btnAdd.setText(R.string.save_event)
            Picasso.get()
                .load(event.image)
                .into(binding.eventImage)
            if (event.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_event_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            event.title = binding.eventTitle.text.toString()
            event.description = binding.description.text.toString()
            if (event.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_event_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.events.update(event.copy())
                } else {
                    app.events.create(event.copy())
                }
            }
            i("add Button Pressed: $event")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_event, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            event.image = result.data!!.data!!
                            Picasso.get()
                                .load(event.image)
                                .into(binding.eventImage)
                            binding.chooseImage.setText(R.string.change_event_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}