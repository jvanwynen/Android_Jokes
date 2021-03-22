package com.joost.joke_exercise.framework.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteDialogFragment : DialogFragment() {

    private val viewModel: DeleteViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("confirm deletion")
            .setMessage(viewModel.message)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes"){ _, _ ->
                viewModel.onConfirmClicked()
            }
            .create()

}