package com.example.aplikasirutetravel.ui.kondisi_jalan

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aplikasirutetravel.R
import com.example.aplikasirutetravel.data.source.local.entity.KondisiJalanEntity
import com.example.aplikasirutetravel.databinding.FragmentKondisiJalanBinding
import com.example.aplikasirutetravel.utils.gone
import com.example.aplikasirutetravel.viewmodel.KondisiJalanViewModel
import com.example.aplikasirutetravel.viewmodel.ViewModelFactory
import com.example.aplikasirutetravel.vo.Status
import com.google.gson.Gson
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import timber.log.Timber

class KondisiJalanFragment : Fragment() {

    private var _binding: FragmentKondisiJalanBinding? = null
    private val binding get() = _binding

    private lateinit var mapboxMap: MapboxMap
    private lateinit var symbolManager: SymbolManager
    private lateinit var locationComponent: LocationComponent
    private lateinit var mylocation: LatLng
    private lateinit var permissionsManager: PermissionsManager
    private var currentRoute: DirectionsRoute? = null
    private lateinit var navigationMapRoute: NavigationMapRoute

    companion object {
        private const val ICON_ID = "ICON_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKondisiJalanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())

        val viewModel = ViewModelProvider(this, factory)[KondisiJalanViewModel::class.java]

        binding?.mapView?.onCreate(savedInstanceState)
        binding?.mapView?.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                showMyLocation(style)
                symbolManager = SymbolManager(binding?.mapView!!, mapboxMap, style)
                symbolManager.iconAllowOverlap = true

                navigationMapRoute = NavigationMapRoute(
                    null,
                    binding?.mapView!!,
                    mapboxMap,
                    R.style.NavigationMapRoute
                )

                viewModel.getAllKondisiJalan().observe(this, { kondisiJalan ->
                    when (kondisiJalan.status) {
                        Status.SUCCESS -> {
                            Timber.d("cek ${kondisiJalan.data?.size}")

                            if (kondisiJalan.data != null) {
                                if (kondisiJalan.data.isNotEmpty()) {
                                    showMarker(kondisiJalan.data)
                                }
                            } else {
                                Toast.makeText(activity, "Data tidak ditemukan", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            binding?.ivCari?.setOnClickListener {

                                Timber.d("cek text ${binding?.edtCari?.text.toString()}")
                                viewModel.getAllKondisiJalanSearch(binding?.edtCari?.text.toString())
                                    .observe(this, { kondisiJalan ->
                                        Timber.d("cek kondisi ${kondisiJalan.data?.size}")

                                        when (kondisiJalan.status) {
                                            Status.SUCCESS -> {
                                                Timber.d("cek search ${kondisiJalan.data?.size}")
                                                if (kondisiJalan.data != null) {
                                                    if (kondisiJalan.data.isNotEmpty()) {
                                                        showMarker(kondisiJalan.data)
                                                    }
                                                } else {
                                                    Toast.makeText(
                                                        activity,
                                                        "Data tidak ditemukan",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                            Status.ERROR -> {
                                                Timber.d("cek search error ${kondisiJalan.message}")
                                            }
                                            Status.LOADING -> {

                                            }
                                        }
                                    })
                            }
                        }

                        Status.LOADING -> {

                        }

                        Status.ERROR -> {
                            Timber.d("cek error ${kondisiJalan.message}")
                        }
                    }
                })

                binding?.ivCurrentLocation?.setOnClickListener {
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 12.0))
//                    binding?.btnNavigation?.gone()
                    binding?.btnDetail?.gone()
                    navigationMapRoute.updateRouteVisibilityTo(false)
                }
            }
        }
    }

    private fun showMarker(data: List<KondisiJalanEntity>?) {
        symbolManager.deleteAll()
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            style.addImage(
                ICON_ID,
                BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_default)
            )

            data?.let {
                if (it.size > 1) {
                    Timber.d("cekk size ${it.size}")
                    symbolManager = SymbolManager(binding?.mapView!!, mapboxMap, style)
                    symbolManager.iconAllowOverlap = true
                    val latLngBoundsBuilder = LatLngBounds.Builder()
                    val options = ArrayList<SymbolOptions>()
                    data.forEach { data ->
                        Timber.d("cek lat banyaj ${data.latitude.toDouble()} dan ${data.longitude.toDouble()} ")
                        latLngBoundsBuilder.include(
                            LatLng(
                                data.latitude.toDouble(),
                                data.longitude.toDouble()
                            )
                        )
                        options.add(
                            SymbolOptions()
                                .withLatLng(
                                    LatLng(
                                        data.latitude.toDouble(),
                                        data.longitude.toDouble()
                                    )
                                )
                                .withIconImage(ICON_ID)
                                .withData(Gson().toJsonTree(data))
                                .withTextField(data.nama_lokasi)
                                .withTextHaloWidth(5.0f)
                                .withTextAnchor("top")
                                .withTextOffset(arrayOf(0f, 1.5f))
                        )
                    }
                    symbolManager.create(options)
                    val latLngBounds = latLngBoundsBuilder.build()
                    mapboxMap.easeCamera(
                        CameraUpdateFactory.newLatLngBounds(latLngBounds, 50),
                        5000
                    )
                    symbolManager.addClickListener { symbol ->
                        val dataKondisiJalan =
                            Gson().fromJson(symbol.data, KondisiJalanEntity::class.java)
                        val arg = Bundle()
                        arg.putString(
                            DetailKondisiJalanFragment.ID_KONDISI_JALAN,
                            dataKondisiJalan.id_kondisi_jalan
                        )
                        findNavController().navigate(R.id.detailKondisiJalanFragment, arg)
//                val intent = Intent(this, DetailTourismActivity::class.java)
//                intent.putExtra(DetailTourismActivity.EXTRA_DATA, data)
//                startActivity(intent)

                        Timber.d("oiii $data")

//                        val origin = Point.fromLngLat(mylocation.longitude, mylocation.latitude)
//                        val destination = Point.fromLngLat(
//                            dataKondisiJalan.longitude.toDouble(),
//                            dataKondisiJalan.latitude.toDouble()
//                        )
//                        requestRoute(origin, destination)

//                        binding?.btnNavigation?.visible()
//                        binding?.btnDetail?.visible()

//                        binding?.btnNavigation?.setOnClickListener {
//                            val simulateRoute = false
//
//                            val options = NavigationLauncherOptions.builder()
//                                .directionsRoute(currentRoute)
//                                .shouldSimulateRoute(simulateRoute)
//                                .build()
//
//                            NavigationLauncher.startNavigation(activity, options)
//                        }

                        binding?.btnDetail?.setOnClickListener {
                            val dataKondisiJalan =
                                Gson().fromJson(symbol.data, KondisiJalanEntity::class.java)
                            val arg = Bundle()
                            arg.putString(
                                DetailKondisiJalanFragment.ID_KONDISI_JALAN,
                                dataKondisiJalan.id_kondisi_jalan
                            )
                            findNavController().navigate(R.id.detailKondisiJalanFragment, arg)
                        }
                    }
                } else {
                    navigationMapRoute.updateRouteVisibilityTo(false)
                    symbolManager = SymbolManager(binding?.mapView!!, mapboxMap, style)
                    symbolManager.iconAllowOverlap = true
                    symbolManager.create(
                        SymbolOptions()
                            .withLatLng(
                                LatLng(
                                    data[0].latitude.toDouble(),
                                    data[0].longitude.toDouble()
                                )
                            )
                            .withIconImage(ICON_ID)
                            .withData(Gson().toJsonTree(data))
                            .withTextField(data[0].nama_lokasi)
                            .withTextHaloWidth(5.0f)
                            .withTextAnchor("top")
                            .withTextOffset(arrayOf(0f, 1.5f))
                    )

                    symbolManager.addClickListener { symbol ->
                        val arg = Bundle()
                        arg.putString(
                            DetailKondisiJalanFragment.ID_KONDISI_JALAN,
                            data[0].id_kondisi_jalan
                        )
                        findNavController().navigate(R.id.detailKondisiJalanFragment, arg)

//                        val origin = Point.fromLngLat(mylocation.longitude, mylocation.latitude)
//                        val destination = Point.fromLngLat(
//                            data[0].longitude.toDouble(),
//                            data[0].latitude.toDouble()
//                        )
//                        requestRoute(origin, destination)

//                        binding?.btnNavigation?.visible()
//                        binding?.btnDetail?.visible()

//                        binding?.btnNavigation?.setOnClickListener {
//                            val simulateRoute = false
//
//                            val options = NavigationLauncherOptions.builder()
//                                .directionsRoute(currentRoute)
//                                .shouldSimulateRoute(simulateRoute)
//                                .build()
//
//                            NavigationLauncher.startNavigation(activity, options)
//                        }

                        binding?.btnDetail?.setOnClickListener {
                            val arg = Bundle()
                            arg.putString(
                                DetailKondisiJalanFragment.ID_KONDISI_JALAN,
                                data[0].id_kondisi_jalan
                            )
                            findNavController().navigate(R.id.detailKondisiJalanFragment, arg)
                        }
                    }
//                    mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(data[0].latitude.toDouble(), data[0].longitude.toDouble()), 8.0))
                }
            }


        }
    }

    @SuppressLint("MissingPermission")
    private fun showMyLocation(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(activity)) {
            val locationComponentOptions = activity?.let {
                LocationComponentOptions.builder(it)
                    .pulseEnabled(true)
                    .pulseColor(Color.BLUE)
                    .pulseAlpha(.4f)
                    .pulseInterpolator(BounceInterpolator())
                    .build()
            }
            val locationComponentActivationOptions = activity?.let {
                LocationComponentActivationOptions
                    .builder(it, style)
                    .locationComponentOptions(locationComponentOptions)
                    .build()
            }
            locationComponent = mapboxMap.locationComponent
            if (locationComponentActivationOptions != null) {
                locationComponent.activateLocationComponent(locationComponentActivationOptions)
            }
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS
            mylocation = LatLng(
                -0.386039,
                102.554871
            )

//            if (locationComponent != null){
//                locationComponent.isLocationComponentEnabled = true
//                locationComponent.cameraMode = CameraMode.TRACKING
//                locationComponent.renderMode = RenderMode.COMPASS
//                mylocation = LatLng(
//                    -0.386039,
//                    102.554871
//                )
//            } else {
//                mylocation = LatLng(
//                    -0.386039,
//                    102.554871
//                )
//            }
            Timber.d("my location $mylocation")
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 6.0))
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
                    Toast.makeText(
                        activity,
                        "Anda harus mengizinkan location permission untuk menggunakan aplikasi ini",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        mapboxMap.getStyle { style ->
                            showMyLocation(style)
                        }
                    } else {
                        activity?.finish()
                    }
                }
            })
            permissionsManager.requestLocationPermissions(activity)
        }
    }

    private fun requestRoute(origin: Point, destination: Point) {
        navigationMapRoute.updateRouteVisibilityTo(false)
        NavigationRoute.builder(activity)
            .accessToken(getString(R.string.mapbox_access_token))
            .origin(origin)
            .destination(destination)
            .build()
            .getRoute(object : retrofit2.Callback<DirectionsResponse> {
                override fun onResponse(
                    call: retrofit2.Call<DirectionsResponse>,
                    response: retrofit2.Response<DirectionsResponse>
                ) {
                    if (response.body() == null) {
                        Toast.makeText(
                            activity,
                            "No routes found, make sure you set the right user and access token.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    } else if (response.body()?.routes()?.size == 0) {
                        Toast.makeText(activity, "No routes found.", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }

                    currentRoute = response.body()?.routes()?.get(0)

                    navigationMapRoute.addRoute(currentRoute)
                }

                override fun onFailure(call: retrofit2.Call<DirectionsResponse>, t: Throwable) {
                    Toast.makeText(activity, "Error : $t", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onStart() {
        super.onStart()
        binding?.mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding?.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding?.mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding?.mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.mapView?.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapView?.onLowMemory()
    }
}