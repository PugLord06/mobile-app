package com.eduvos.campusapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.eduvos.campusapp.R
import com.eduvos.campusapp.activities.MainActivity
import com.eduvos.campusapp.database.AppDatabase
import com.eduvos.campusapp.databinding.FragmentProfileBinding
import com.eduvos.campusapp.models.User
import com.eduvos.campusapp.utils.SharedPreferencesManager
import com.eduvos.campusapp.utils.toast
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var database: AppDatabase
    private lateinit var prefsManager: SharedPreferencesManager
    private var currentUser: User? = null
    private var isEditMode = false
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        database = AppDatabase.getDatabase(requireContext())
        prefsManager = SharedPreferencesManager(requireContext())
        
        setupViews()
        loadUserProfile()
    }
    
    private fun setupViews() {
        binding.btnEdit.setOnClickListener {
            toggleEditMode()
        }
        
        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
        
        // Initially disable all edit texts
        setEditableFields(false)
    }
    
    private fun loadUserProfile() {
        val studentNumber = prefsManager.getStudentNumber() ?: return
        
        database.userDao().getUserLiveData(studentNumber).observe(viewLifecycleOwner) { user ->
            user?.let {
                currentUser = it
                displayUserInfo(it)
            }
        }
    }
    
    private fun displayUserInfo(user: User) {
        binding.apply {
            tvStudentNumber.text = user.studentNumber
            etFullName.setText(user.fullName)
            etEmail.setText(user.email)
            etPhone.setText(user.phone ?: "")
            etFaculty.setText(user.faculty ?: "")
            etProgram.setText(user.program ?: "")
            etYear.setText(user.yearOfStudy.toString())
            
            // Display initials in profile image placeholder
            val initials = user.fullName.split(" ")
                .take(2)
                .map { it.firstOrNull()?.uppercase() ?: "" }
                .joinToString("")
            tvInitials.text = initials
        }
    }
    
    private fun toggleEditMode() {
        isEditMode = !isEditMode
        setEditableFields(isEditMode)
        
        if (isEditMode) {
            binding.btnEdit.text = "Save"
            binding.btnEdit.setIconResource(R.drawable.ic_save)
        } else {
            // Save changes
            saveProfile()
            binding.btnEdit.text = "Edit Profile"
            binding.btnEdit.setIconResource(R.drawable.ic_edit)
        }
    }
    
    private fun setEditableFields(enabled: Boolean) {
        binding.apply {
            etFullName.isEnabled = enabled
            etPhone.isEnabled = enabled
            etFaculty.isEnabled = enabled
            etProgram.isEnabled = enabled
            etYear.isEnabled = enabled
            
            // Email is not editable
            etEmail.isEnabled = false
        }
    }
    
    private fun saveProfile() {
        currentUser?.let { user ->
            val updatedUser = user.copy(
                fullName = binding.etFullName.text.toString().trim(),
                phone = binding.etPhone.text.toString().trim().ifEmpty { null },
                faculty = binding.etFaculty.text.toString().trim().ifEmpty { null },
                program = binding.etProgram.text.toString().trim().ifEmpty { null },
                yearOfStudy = binding.etYear.text.toString().toIntOrNull() ?: 1
            )
            
            lifecycleScope.launch {
                try {
                    database.userDao().update(updatedUser)
                    prefsManager.setUserName(updatedUser.fullName)
                    requireContext().toast(getString(R.string.success_profile_updated))
                } catch (e: Exception) {
                    requireContext().toast("Failed to update profile")
                }
            }
        }
    }
    
    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                (requireActivity() as MainActivity).logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}