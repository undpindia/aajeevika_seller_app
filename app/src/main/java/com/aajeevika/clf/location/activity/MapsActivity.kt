package com.aajeevika.clf.location.activity

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aajeevika.clf.R
import com.aajeevika.clf.databinding.ActivityMapsBinding
import com.aajeevika.clf.location.constants.*
import com.aajeevika.clf.location.helper.FusedLocationFetcher
import com.aajeevika.clf.location.viewModel.LocationViewModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction
import com.aajeevika.clf.utility.extension.gone
import com.aajeevika.clf.utility.extension.goto
import com.aajeevika.clf.utility.extension.visible
import com.aajeevika.clf.view.dialog.AlertDialog
import com.aajeevika.clf.location.constants.*
import com.aajeevika.clf.utility.Constant
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    FusedLocationFetcher.LocationChangeListener {
    private var mHomeLatLng: LatLng? = null
    private var mGoogleMap: GoogleMap? = null
    private var mIsAnimating: Boolean = false
    private var mCurrentLatLng: LatLng? = null
    private lateinit var mBinding: ActivityMapsBinding

    private val address by lazy { intent.getStringExtra(Constant.ADDRESS) ?: "" }
    private val mViewModel: LocationViewModel by lazy { getViewModel(clazz = LocationViewModel::class) }
    private val mFusedLocationFetcher: FusedLocationFetcher by lazy { FusedLocationFetcher(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        mBinding.lifecycleOwner = this
        initUi()
        initListener()
        initObserver()
    }

    private fun initUi() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationFetcher.setIsContinuesLocationUpdates(false)
        mFusedLocationFetcher.setIsPermissionMandatory(true)
        mFusedLocationFetcher.getCurrentLocation()
    }

    private fun initListener() {
        mBinding.backBtn.setOnClickListener {
            onBackPressed()
        }

        mBinding.cardCurrentLocation.setOnClickListener {
            animateCamera(mHomeLatLng)
        }

        mBinding.confirmBtn.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS, mBinding.tvFullAddress.text.trim().toString())
                putExtra(EXTRA_LATITUDE, mCurrentLatLng?.latitude ?: 0.0)
                putExtra(EXTRA_LONGITUDE, mCurrentLatLng?.longitude ?: 0.0)
            })
            finish()
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0
        mGoogleMap?.setOnCameraIdleListener {
            mGoogleMap?.cameraPosition?.target?.let {
                mCurrentLatLng = it

                mViewModel.getAddressFromLatLng(
                    hashMapOf(
                        "key" to getString(R.string.google_api_key),
                        "latlng" to "${it.latitude},${it.longitude}"
                    )
                )
            }
        }

        getLatLongFromAddress(address)
    }

    override fun onLocationChange(latitude: Double, longitude: Double) {
        mHomeLatLng = LatLng(latitude, longitude)
        if (mCurrentLatLng?.latitude == 0.0 && mCurrentLatLng?.longitude == 0.0) {
            animateCamera(mHomeLatLng)
        }
    }

    private fun getLatLongFromAddress(address: String) {
        val geoCoderList = Geocoder(this, Locale.getDefault()).getFromLocationName(address, 1)

        if(geoCoderList.isNotEmpty()) {
            val geoCoder = geoCoderList[0]
            mCurrentLatLng = LatLng(geoCoder.latitude, geoCoder.longitude)

            mBinding.confirmBtn.isEnabled = true
            animateCamera(mCurrentLatLng)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mFusedLocationFetcher.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun animateCamera(latLng: LatLng?) {
        if (!mIsAnimating && latLng != null) {
            mIsAnimating = true
            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(18F).build()
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            mGoogleMap?.animateCamera(cameraUpdate, object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    mIsAnimating = false
                }

                override fun onCancel() {
                    mIsAnimating = false
                }
            })
        }
    }

    private fun initObserver() {
        mViewModel.address.observe(this, {
            if (it.isNotEmpty()) {
                mBinding.tvFullAddress.text = it[0].formatted_address
            }
        })

        mViewModel.progressHandler.observe(this, {
            when (it ?: ProgressAction.NONE) {
                ProgressAction.PROGRESS_BAR -> {
                    findViewById<ProgressBar>(R.id.progress_bar)?.visible()
                }
                ProgressAction.DISMISS -> {
                    findViewById<ProgressBar>(R.id.progress_bar)?.gone()
                }
                ProgressAction.NONE -> {
                    findViewById<ProgressBar>(R.id.progress_bar)?.gone()
                }
                ProgressAction.PROGRESS_DIALOG -> {
                }
            }
        })

        mViewModel.errorMessage.observe(this, {
            when (it.errorType) {
                ErrorType.DIALOG -> {
                    AlertDialog(
                        context = this,
                        message = it.message,
                        positive = getString(R.string.ok),
                        cancelOnOutsideClick = false,
                    ).show()
                }
                ErrorType.MESSAGE -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                ErrorType.TOAST -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                ErrorType.NONE -> {
                }
            }
        })
    }
}
