package com.example.permissions

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ListView
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                Array(1) { android.Manifest.permission.READ_CONTACTS },
                111)//resize array and add permissions to call if you want more than 1.
        // Must also be in the Manifest xml. Request code is user-defined (111 here)
        }
        else
            readcontacts()

        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readcontacts()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    val cols = listOf<String>(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone._ID
    ).toTypedArray()


    private fun readcontacts() {

        var from = listOf<String>(cols[0],cols[1]).toTypedArray()
        var to = intArrayOf(android.R.id.text1,android.R.id.text2)
        var rs = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI ,
        cols,
            null,
            null,
                cols[0])
        var adapter = SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_2,
            rs,
            from,
            to,
            0)

        val contractLV: ListView = findViewById(R.id.listviewContacts)
        contractLV.adapter = adapter
        val contactSV : SearchView = findViewById(R.id.searchviewContacts)

        contactSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onQueryTextChange(q: String?): Boolean {
                var rs = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    cols,
                "${cols[0]} LIKE ?",
                Array(1){"%$q%"},
                cols[0])
                adapter.changeCursor(rs)
                return false

            }

            override fun onQueryTextSubmit(q: String?): Boolean {
                TODO("Not yet implemented")
            }
        }

        )
        /*if(result?.moveToNext() == true) {
            val contactToast =
                    Toast.makeText(applicationContext,result.getString(1),Toast.LENGTH_LONG);
            contactToast.show()
            }
         */


    }
}



