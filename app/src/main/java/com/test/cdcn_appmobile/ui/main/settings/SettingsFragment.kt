package com.test.cdcn_appmobile.ui.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.data.models.User
import com.test.cdcn_appmobile.databinding.FragmentSettingsBinding
import com.test.cdcn_appmobile.extension.addFragment
import com.test.cdcn_appmobile.extension.replaceFragment
import com.test.cdcn_appmobile.ui.launch.login.LoginFragment
import com.test.cdcn_appmobile.ui.main.settings.changepass.ChangePasswordFragment
import com.test.cdcn_appmobile.ui.main.settings.profile.ProfileFragment
import com.test.cdcn_appmobile.ui.main.transactions.TransactionsViewModel
import com.test.cdcn_appmobile.utils.Constant

/*
 * Created by tuyen.dang on 11/27/2022
 */

class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        binding?.run {

        }
    }

    private fun initListener() {
        binding?.run {
            btnProfile.setOnClickListener {
                requireActivity().addFragment(
                    R.id.ctReplaceFragment,
                    ProfileFragment(),
                    true
                )
            }

            btnChangePass.setOnClickListener {
                requireActivity().addFragment(
                    R.id.ctReplaceFragment,
                    ChangePasswordFragment(),
                    true
                )
            }

            btnLogOut.setOnClickListener {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

                builder.setMessage("Bạn có chắc muốn đăng xuất ?")

                builder.setPositiveButton(
                    "Có"
                ) { dialog, _ ->
                    dialog.dismiss()
                    Constant.USER = User("", "", "", "", "", "")
                    TransactionsViewModel.clearDay()
                    requireActivity().replaceFragment(
                        R.id.ctReplaceFragment,
                        LoginFragment(),
                        false
                    )
                }

                builder.setNegativeButton(
                    "Không"
                ) { dialog, _ -> dialog.dismiss() }
                builder.show()
            }
        }
    }

}
