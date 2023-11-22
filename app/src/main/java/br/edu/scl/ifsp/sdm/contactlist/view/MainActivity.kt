package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactRvAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constants.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constants.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity(), OnContactClickListener {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    //Adapter
    private val contactAdapter: ContactRvAdapter by lazy {
        ContactRvAdapter(contactList, this)
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        startContactActivityForResult()
        fillContacts()
        setupAdapter()
        registerForContextMenu(binding.contactsRv)
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

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onContactClick(position: Int) {
        startActivity(Intent(this, ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contactList[position])
            putExtra(EXTRA_VIEW_CONTACT, true)
        })
    }

    override fun onRemoveContactMeuItemClick(position: Int) {
        contactList.removeAt(position)
        contactAdapter.notifyItemRemoved(position)
        Toast.makeText(this, getString(R.string.contact_removed), Toast.LENGTH_SHORT).show()
    }

    override fun onEditContactMeuItemClick(position: Int) {
        carl.launch(Intent(this, ContactActivity::class.java).apply {
            putExtra(EXTRA_CONTACT, contactList[position])
        })
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
        contactsRv.adapter = contactAdapter
        contactsRv.layoutManager = LinearLayoutManager(this@MainActivity)
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
        contact?.also { newOrEditContact ->
            if (contactList.any { it.id == newOrEditContact.id }) {
                val position = contactList.indexOfFirst { it.id == newOrEditContact.id }
                contactList[position] = newOrEditContact
                contactAdapter.notifyItemChanged(position)
            } else {
                contactList.add(newOrEditContact)
                contactAdapter.notifyItemInserted(contactList.lastIndex)
            }
        }
    }
}