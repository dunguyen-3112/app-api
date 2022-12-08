package com.test.cdcn_appmobile.ui.main.settings.profile

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.data.models.User
import com.test.cdcn_appmobile.databinding.FragmentProfileBinding
import com.test.cdcn_appmobile.extension.backToPreFragment
import com.test.cdcn_appmobile.extension.setVisibility
import com.test.cdcn_appmobile.ui.main.settings.UserViewModel
import com.test.cdcn_appmobile.utils.Constant
import com.test.cdcn_appmobile.utils.InjectorUtil

class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    private var userViewModel: UserViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        userViewModel =
            ViewModelProvider(
                requireActivity(),
                InjectorUtil.provideUserViewModelFactory()
            )[UserViewModel::class.java]

        binding?.run {
            tvEmail.text = Constant.USER.email
            tvUsername.text = Constant.USER.userName
            edtName.text = Editable.Factory.getInstance()
                .newEditable(Constant.USER.name)
        }
    }

    private fun initListener() {
        binding?.run {
            btnBack.setOnClickListener {
                requireActivity().backToPreFragment()
            }

            btnUpdate.setOnClickListener {
                userViewModel?.run {
                    if (edtName.text.toString().isNotEmpty()) {
                        val user = User(
                            Constant.USER.id,
                            edtName.text.toString(),
                            Constant.USER.userName,
                            Constant.USER.email,
                            Constant.USER.password,
                            Constant.USER.tokenJWT
                        )
                        viewBg.setVisibility(true)
                        progressBar.setVisibility(true)
                        updateUser(
                            resources.getString(
                                R.string.tokenJWT,
                                Constant.USER.tokenJWT
                            ),
                            user
                        ) { message ->
                            viewBg.setVisibility(false)
                            progressBar.setVisibility(false)
                            message?.let {
                                context?.let {
                                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        context?.run {
                            Toast.makeText(this, "Vui lòng nhập tên!!!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

}