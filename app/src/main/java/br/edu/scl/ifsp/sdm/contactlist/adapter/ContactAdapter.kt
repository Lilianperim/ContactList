package br.edu.scl.ifsp.sdm.contactlist.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.databinding.TitleContactBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class ContactAdapter(context: Context, private val contactList: MutableList<Contact>) :
    ArrayAdapter<Contact>(context, R.layout.title_contact, contactList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contact = contactList[position]
        var binding: TitleContactBinding

        var contactTitleView = convertView
        if (contactTitleView == null) {
            binding = TitleContactBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            contactTitleView = binding.root
            val titleContactViewHolder = TitleContactHolder(binding.nameTV, binding.emailTV)
            contactTitleView.tag = titleContactViewHolder
        }

        val holder = contactTitleView.tag as TitleContactHolder
        holder.nameTv.text = contact.name
        holder.emailTv.text = contact.email

        return contactTitleView
    }

    private data class TitleContactHolder(val nameTv: TextView, val emailTv: TextView)
}