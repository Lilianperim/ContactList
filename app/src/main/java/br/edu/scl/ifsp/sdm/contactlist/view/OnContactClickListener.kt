package br.edu.scl.ifsp.sdm.contactlist.view

sealed interface OnContactClickListener {
    fun onContactClick(position: Int)
    fun onRemoveContactMeuItemClick(position: Int)
    fun onEditContactMeuItemClick(position: Int)
}