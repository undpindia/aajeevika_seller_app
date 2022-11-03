package com.aajeevika.clf.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityChangeAddressBinding
import com.aajeevika.clf.location.activity.MapsActivity
import com.aajeevika.clf.location.constants.EXTRA_LATITUDE
import com.aajeevika.clf.location.constants.EXTRA_LONGITUDE
import com.aajeevika.clf.model.data_model.AddressData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.view.dialog.AlertDialog
import com.aajeevika.clf.view.profile.viewmodel.ProfileViewModel
import java.lang.StringBuilder

class ActivityChangeAddress : BaseActivityVM<ActivityChangeAddressBinding, ProfileViewModel>(
    R.layout.activity_change_address,
    ProfileViewModel::class
) {
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var isInitialized = false
    private var isDistrictReceived = false
    private val addressData by lazy { intent.getSerializableExtra(Constant.ADDRESS) as? AddressData }

    private val mapResultCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK) {
            it.data?.let { data ->
                lat = data.getDoubleExtra(EXTRA_LATITUDE, 0.0)
                long = data.getDoubleExtra(EXTRA_LONGITUDE, 0.0)

                viewDataBinding.run {
                    val addressLineOne = inputAddressLineOne.text.toString().trim()
                    val addressLineTwo = inputAddressLineTwo.text.toString().trim()
                    val country = inputCountry.text.toString().trim()
                    val state = inputState.text.toString().trim()
                    val district = inputDistrict.text.toString().trim()
                    val pin = inputPin.text.toString().trim()
                    val block = inputBlock.text.toString().trim()

                    validateRegisteredAddressInfo(addressLineOne, country, state, district, pin, block)?.let { error ->
                        root.showMessage(error)
                    } ?: run {
                        saveBtn.isEnabled = false
                        val districtId = viewModel.districtLiveData.value?.firstOrNull { it.name == district }?.id ?: -1
                        val blockId = viewModel.blockLiveData.value?.firstOrNull { it.name == block }?.id ?: -1

                        viewModel.updateAddress(
                            addressData?.id ?: -1,
                            addressLineOne,
                            addressLineTwo,
                            addressData?.countryId ?: -1,
                            addressData?.stateId ?: -1,
                            districtId,
                            blockId,
                            pin.toInt(),
                            lat,
                            long,
                        )
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.address = addressData

        viewModel.getDistrict()
    }

    override fun observeData() {
        super.observeData()

        viewModel.districtLiveData.observe(this, { list ->
            if(!isDistrictReceived) {
                val districtName = viewDataBinding.inputDistrict.text.toString().trim()
                list.firstOrNull { it.name == districtName }?.id?.let {
                    viewModel.getBlock(it)
                }
            }

            isDistrictReceived = true
            viewDataBinding.districtList = list.map { it.name }.toCollection(ArrayList<String>())
        })

        viewModel.blockLiveData.observe(this, { list ->
            viewDataBinding.blockList = list.map { it.name }.toCollection(ArrayList<String>())
        })

        viewModel.updateAddressStatusLiveData.observe(this, {
            viewDataBinding.run {
                val addressLineOne = inputAddressLineOne.text.toString().trim()
                val addressLineTwo = inputAddressLineTwo.text.toString().trim()
                val country = inputCountry.text.toString().trim()
                val state = inputCountry.text.toString().trim()
                val district = inputDistrict.text.toString().trim()
                val pin = inputPin.text.toString().trim()
                val block = inputBlock.text.toString().trim()

                val addressStringBuilder = StringBuilder()
                if(addressLineOne.isNotEmpty()) addressStringBuilder.append("$addressLineOne, ")
                if(addressLineTwo.isNotEmpty()) addressStringBuilder.append("$addressLineTwo, ")
                if(district.isNotEmpty()) addressStringBuilder.append("$district, ")
                if(state.isNotEmpty()) addressStringBuilder.append("$state, ")
                if(country.isNotEmpty()) addressStringBuilder.append("$country, ")
                if(block.isNotEmpty()) addressStringBuilder.append("$block, ")
                if(pin.isNotEmpty()) addressStringBuilder.append(pin)

                preferencesHelper.address = addressStringBuilder.toString()

                AlertDialog(
                    context = this@ActivityChangeAddress,
                    cancelOnOutsideClick = false,
                    message = it,
                    positive = getString(R.string.ok),
                    positiveClick = {
                        setResult(RESULT_OK)
                        finish()
                    }
                ).show()
            }
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            inputDistrict.doOnTextChanged { text, _, _, _ ->
                if(isInitialized) inputBlock.text = null
                else isInitialized = true

                text?.toString()?.trim()?.let { district ->
                    viewModel.districtLiveData.value?.firstOrNull { it.name == district }?.id?.let {
                        viewModel.getBlock(it)
                    }
                }
            }

            saveBtn.setOnClickListener {
                val addressLineOne = inputAddressLineOne.text.toString().trim()
                val addressLineTwo = inputAddressLineTwo.text.toString().trim()
                val country = inputCountry.text.toString().trim()
                val state = inputState.text.toString().trim()
                val district = inputDistrict.text.toString().trim()
                val pin = inputPin.text.toString().trim()
                val block = inputBlock.text.toString().trim()

                val formattedAddress = "$addressLineOne, $addressLineTwo, $block, $district, $state, $country - $pin"

                val intent = Intent(this@ActivityChangeAddress, MapsActivity::class.java)
                intent.putExtra(Constant.ADDRESS, formattedAddress)
                mapResultCallback.launch(intent)
            }
        }
    }

    private fun validateRegisteredAddressInfo(addressLineOne: String, country: String, state: String, district: String, pin: String, block: String): String? {
        return if (addressLineOne.isEmpty() || country.isEmpty() || state.isEmpty() || district.isEmpty() || pin.isEmpty() || block.isEmpty())
            getString(R.string.fill_all_the_fields)
        else
            null
    }
}