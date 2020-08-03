package edu.mines.csci448.lab.criminalintent.ui.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import edu.mines.csci448.lab.criminalintent.R
import edu.mines.csci448.lab.criminalintent.data.Crime
import kotlinx.android.synthetic.main.fragment_detail.*
import java.util.*

private const val LOG_TAG = "448.CrimeDetailFragment"
private const val ARG_CRIME_ID = "crime_id"
private const val REQUEST_CODE_CONTACT: Int = 1
private const val REQUEST_CODE_PERMISSION_READ_CONTACTS = 2
private const val REQUIRED_CONTACTS_PERMISSION =
    android.Manifest.permission.READ_CONTACTS

class CrimeDetailFragment : Fragment() {

    private val logTag = "448.CrimeDetailFrag"
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton : Button
    private lateinit var solvedCheckBox : CheckBox
    private lateinit var crimeSuspectButton : Button;
    private lateinit var crimeCallButton : Button;
    private lateinit var crimeReportButton : Button;
    private lateinit var crimeDetailViewModel: CrimeDetailViewModel
    private val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
        if(crime.suspect != null ) {
            crimeSuspectButton.text = crime.suspect
        }
        crimeCallButton.isEnabled = crime.suspectNumber != null

    }

    private fun generateCrimeReport() : String {
        val dateString = DateFormat.format("EEE, MMM dd", crime.date)
        val solvedString = if(crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val suspectString = if(crime.suspect == null) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }
        return getString(R.string.crime_report, crime.title,
            dateString, solvedString, suspectString)
    }

    private fun hasContactsPermission() =
        ContextCompat.checkSelfPermission(requireContext(),
            REQUIRED_CONTACTS_PERMISSION) == PackageManager.PERMISSION_GRANTED

    // TODO 3 override all Fragment Lifecycle methods and log their occurrance
    override fun onAttach(context : Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        crime = Crime()

        val factory = CrimeDetailViewModelFactory(requireContext())
        crimeDetailViewModel = ViewModelProvider(this, factory).get(CrimeDetailViewModel::class.java)

        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)
        Log.d(LOG_TAG, "args bundle crime ID: $crimeId")
        //Eventually, load crime from database
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag,"onCreateView() called")
        val view : View = inflater.inflate(R.layout.fragment_detail, container, false)
        titleField = view.findViewById(R.id.crime_title_edit_text)
        dateButton = view.findViewById(R.id.crime_date_button)
        solvedCheckBox = view.findViewById(R.id.crime_solved_checkbox)

        crimeSuspectButton = view.findViewById(R.id.crime_suspect_button)
        crimeCallButton = view.findViewById(R.id.crime_call_button)
        crimeReportButton = view.findViewById(R.id.crime_report_button)

        dateButton.apply {
            text = crime.date.toString()
            isEnabled = false
        }

        crimeReportButton.setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, generateCrimeReport())
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.crime_report_subject))
            intent = Intent.createChooser(intent, getString(R.string.send_report))
            startActivity(intent)
        }
        //pickContactIntent.addCategory(Intent.CATEGORY_HOME)
        crimeSuspectButton.isEnabled = requireActivity().packageManager
            .resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY) != null
        crimeSuspectButton.setOnClickListener {
            if( !hasContactsPermission() ) {
                Log.d(LOG_TAG, "user has NOT granted permission to access contacts")
                // should we show an explanation?
                if( ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), REQUIRED_CONTACTS_PERMISSION ) ) {
                    Log.d(LOG_TAG, "show an explanation")
                    Toast.makeText(requireContext(),
                        R.string.crime_reason_for_contacts,
                        Toast.LENGTH_LONG).show()
                } else {
                    Log.d(LOG_TAG, "no explanation needed, request permission")
                    requestPermissions(listOf(REQUIRED_CONTACTS_PERMISSION).toTypedArray(), REQUEST_CODE_PERMISSION_READ_CONTACTS)
                }
            } else {
                Log.d(LOG_TAG, "user has granted permission to access contacts")
                launchContactsIntent()
            }
        }

        crimeCallButton.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            val phoneNumberUri = Uri.parse( "tel:${crime.suspectNumber}" )
            callIntent.data = phoneNumberUri
            startActivity( callIntent )
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            Observer {crime -> crime?.let {
                this.crime = crime
                updateUI()
            }})
        Log.d(logTag, "onViewCreated() called")
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(logTag,"onActivityCreated() called")
    }
    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged( sequence: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {

            }
        }
        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.apply {
            setOnCheckedChangeListener{_, isChecked -> crime.isSolved = isChecked}
        }
    }

    override fun onActivityResult(requestCode:	Int,	resultCode:	Int,	data:	Intent?)	{
        if(resultCode !=	Activity.RESULT_OK)	{
            return
        } else {
            if(requestCode	==	REQUEST_CODE_CONTACT)	{
                if(data	==	null)	{
                    return
                }
                val contactUri = data.data ?: return
                // specify which fields you want your query to return values for
                val queryFields = listOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID)
                // perform your query, the contactUri is like a "where" clause
                val cursor = requireActivity().contentResolver.query(contactUri,
                    queryFields.toTypedArray(),
                    null, null, null)
                cursor?.use {contactIter ->
                    // double check that you got results
                    if(contactIter.count == 0) {
                        return
                    }
                    // pull out the first column of the first row of data
                    // that is the contact's name
                    contactIter.moveToFirst()
                    val suspect = contactIter.getString(0)
                    crime.suspect = suspect // set the crime's suspect field
                    crimeSuspectButton.text = suspect // change the button text
                    // pull out the second column – that is the contact's ID
                    val contactID = contactIter.getString(1)
                    val phoneURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    val phoneNumberQueryFields = listOf(
                        ContactsContract.CommonDataKinds.Phone.NUMBER )
                    val whereClause = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
                    val phoneQueryParameters = listOf( contactID.toString() )
                    val phoneCursor = requireActivity().contentResolver
                        .query(phoneURI, phoneNumberQueryFields.toTypedArray(),
                            whereClause, phoneQueryParameters.toTypedArray(),
                            null)
                    phoneCursor?.use { phoneIter ->
                        if (phoneIter.count > 0) {
                            phoneIter.moveToFirst()
                            crime.suspectNumber = phoneIter.getString(0)
                            crimeCallButton.isEnabled = true
                        } else {
                            // no phone number found
                            crime.suspectNumber = null
                            crimeCallButton.isEnabled = false
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        if( requestCode == REQUEST_CODE_PERMISSION_READ_CONTACTS ) {
            if( hasContactsPermission() ) {
                // permission was granted, yay! Do the task you need to
                crimeSuspectButton.isEnabled = true
                launchContactsIntent()
            } else {
                // permission denied, boo!
                // Disable the functionality that depends on this permission
                crimeSuspectButton.isEnabled = false
                Toast.makeText(activity,
                    R.string.crime_reason_for_contacts,
                    Toast.LENGTH_LONG).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode,
                permissions, grantResults)
        }
    }

    private fun launchContactsIntent() {
        startActivityForResult(pickContactIntent, REQUEST_CODE_CONTACT)
    }

    override fun onResume() {
        super.onResume()
        Log.d(logTag, "onResume() called")
    }

    override fun onPause() {
        Log.d(logTag, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(logTag, "onStop() called")
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    override fun onDestroyView() {
        Log.d(logTag, "onDestroyView called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy() called")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(logTag, "onDetach() called")
        super.onDetach()
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeDetailFragment().apply {
                arguments = args
            }
        }
    }
}