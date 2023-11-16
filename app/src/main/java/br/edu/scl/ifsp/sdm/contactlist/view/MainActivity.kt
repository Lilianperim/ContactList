package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constants.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    //Adapter
    private val contactAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(
            this,
            android.R.layout.simple_expandable_list_item_1,
            contactList.map { it.toString() })
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        startContactActivityForResult()
        fillContacts()
        setupAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addContactMi -> {
                carl.launch(Intent(this, ContactActivity::class.java))
                true
            }
            else -> false
        }
    }

    private fun fillContacts() {
        for (i in 1..10) {
            contactList.add(
                Contact(
                    id = i,
                    name = "Nome $i",
                    address = "Endereço $i",
                    phone = "Telefone $i",
                    email = "Email $i"
                )
            )
        }
    }

    private fun setupAdapter() = with(binding) {
        contactsLv.adapter = contactAdapter
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_list)
    }

    private fun startContactActivityForResult() {
        carl =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                    verifyContactIsNullAndAddOrEdit(contact)
                }
            }
    }

    private fun verifyContactIsNullAndAddOrEdit(contact: Contact?) {
        contact?.also {
            if (contactList.any { it.id == contact.id }) {
                //Editar
            } else {
                contactList.add(contact)
                contactAdapter.add(contact.toString())
            }
            contactAdapter.notifyDataSetChanged()
        }
    }
}