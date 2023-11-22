package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constants.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constants.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class ContactActivity : AppCompatActivity() {

    private val binding: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        val receivedContact = intent.getParcelableExtra<Contact>(EXTRA_CONTACT)
        receivedContact?.let { received ->
            val viewContact = intent.getBooleanExtra(EXTRA_VIEW_CONTACT, false)
            with(binding) {
                if (viewContact) {
                    nameEt.isEnabled = false
                    addressEt.isEnabled = false
                    phoneEt.isEnabled = false
                    emailEt.isEnabled = false
                    saveBt.visibility = GONE
                }
                nameEt.setText(received.name)
                addressEt.setText(received.address)
                phoneEt.setText(received.phone)
                emailEt.setText(received.email)
            }
        }
        setupClick()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_details)
    }

    private fun setupClick() = binding.saveBt.setOnClickListener { saveContact() }

    private fun saveContact() = with(binding) {
        val receivedContact = intent.getParcelableExtra<Contact>(EXTRA_CONTACT)
        val contact = Contact(
            id = receivedContact?.id?:hashCode(),
            name = nameEt.text.toString(),
            address = addressEt.text.toString(),
            phone = phoneEt.text.toString(),
            email = emailEt.text.toString()
        )
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_CONTACT, contact)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}