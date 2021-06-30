package com.example.aplikasirutetravel.ui.angkutan

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasirutetravel.R
import com.example.aplikasirutetravel.data.source.local.entity.TrayekEntity
import com.example.aplikasirutetravel.data.source.remote.response.Asal
import com.example.aplikasirutetravel.databinding.FragmentAngkutan2Binding
import com.example.aplikasirutetravel.utils.visible
import com.example.aplikasirutetravel.viewmodel.TrayekViewModel
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
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import okhttp3.internal.checkOffsetAndCount
import timber.log.Timber

class Angkutan2Fragment : Fragment() , AdapterView.OnItemSelectedListener{

    private var _binding: FragmentAngkutan2Binding? = null
    private val binding get() = _binding

    private lateinit var mapboxMap: MapboxMap
    private lateinit var symbolManager: SymbolManager
    private lateinit var locationComponent: LocationComponent
    private lateinit var mylocation: LatLng
    private lateinit var permissionsManager: PermissionsManager
    private var currentRoute: DirectionsRoute? = null
    private lateinit var navigationMapRoute: NavigationMapRoute
    private lateinit var viewModel: TrayekViewModel
    lateinit var asalList : List<Asal>
    var asalString = ArrayList<String>()
    var trayekList = ArrayList<TrayekEntity>()
    val NEW_SPINNER_ID = 1

    companion object {
        private const val ICON_ID = "ICON_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAngkutan2Binding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())

        viewModel = ViewModelProvider(this, factory)[TrayekViewModel::class.java]

        binding?.mapView?.onCreate(savedInstanceState)
        binding?.mapView?.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
//                showMyLocation(style)
                symbolManager = SymbolManager(binding?.mapView!!, mapboxMap, style)
                symbolManager.iconAllowOverlap = true

                navigationMapRoute = NavigationMapRoute(
                    null,
                    binding?.mapView!!,
                    mapboxMap,
                    R.style.NavigationMapRoute
                )

                viewModel.getAllTrayek().observe(this, { trayek ->
                    when (trayek.status) {
                        Status.SUCCESS -> {
                            Timber.d("cek ${trayek.data?.size}")

                            if (trayek.data != null) {
                                if (trayek.data.isNotEmpty()) {
                                    trayekList.clear()
                                    trayekList.addAll(trayek.data)
                                    showMarker(trayekList)
                                }
                            } else {
                                Toast.makeText(activity, "Data tidak ditemukan", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }

                        Status.LOADING -> {

                        }

                        Status.ERROR -> {
                            Timber.d("cek error ${trayek.message}")
                        }
                    }
                })

                viewModel.getAllAsal().observe(this, { asal ->
                    when (asal.status) {
                        Status.SUCCESS -> {
                            Timber.d("cek asalllllll ${asal.data?.size}")
                            Timber.d("cek asalllllll ${asal.data}")

                            if (asal.data != null) {

                                if (asal.data.isNotEmpty()) {
                                    asalList = asal.data

                                    for (i in asal.data.indices){
                                        asalString.add(asal.data[i].asal)
                                    }

                                    Timber.d("cek dsdsdsd $asalString")
                                    var aa = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, asalString)
                                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                                    with(binding!!.spAsal)
                                    {
                                        adapter = aa
                                        setSelection(0, false)
                                        onItemSelectedListener = this@Angkutan2Fragment
                                        prompt = "Pilih Asal"
                                        gravity = Gravity.CENTER

                                    }

                                    val spinner = Spinner(activity)
                                    spinner.id = NEW_SPINNER_ID

                                    val ll = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                                    ll.setMargins(10, 40, 10, 10)
                                    binding?.linearLayout?.addView(spinner)

//                                    aa = ArrayAdapter(activity!!, R.layout.spinner_right_aligned, asalString)
//                                    aa.setDropDownViewResource(R.layout.spinner_right_aligned)
//
//                                    with(spinner)
//                                    {
//                                        adapter = aa
//                                        setSelection(0, false)
//                                        onItemSelectedListener = this@Angkutan2Fragment
//                                        layoutParams = ll
//                                        prompt = "Pilih Asal"
//                                        setPopupBackgroundResource(R.color.material_grey_600)
//                                    }
                                }
                            } else {
                                Toast.makeText(activity, "Data tidak ditemukan", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }

                        Status.LOADING -> {

                        }

                        Status.ERROR -> {
                            Timber.d("cek error ${asal.message}")
                        }
                    }
                })

            }
        }




    }

    private fun showMarker(data: List<TrayekEntity>?) {
        symbolManager.deleteAll()
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            style.addImage(
                ICON_ID,
                BitmapFactory.decodeResource(resources, R.drawable.map_marker_light), false
            )

            data?.let {
                if (it.size > 1) {
                    Timber.d("cekk size ${it.size}")
                    symbolManager = SymbolManager(binding?.mapView!!, mapboxMap, style)
                    symbolManager.iconAllowOverlap = true
                    val latLngBoundsBuilder = LatLngBounds.Builder()
                    val options = ArrayList<SymbolOptions>()
                    data.forEach { data ->
                        Timber.d("cek lat banyaj ${data.latitude_asal.toDouble()} dan ${data.longitude_asal.toDouble()} ")
                        latLngBoundsBuilder.include(
                            LatLng(
                                data.latitude_asal.toDouble(),
                                data.longitude_asal.toDouble()
                            )
                        )
                        options.add(
                            SymbolOptions()
                                .withLatLng(
                                    LatLng(
                                        data.latitude_asal.toDouble(),
                                        data.longitude_asal.toDouble()
                                    )
                                )
                                .withIconImage(ICON_ID)
                                .withData(Gson().toJsonTree(data))
                                .withTextField(data.nama_trayek)
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
                        val dataTrayek =
                            Gson().fromJson(symbol.data, TrayekEntity::class.java)

                        val origin = Point.fromLngLat(
                            dataTrayek.longitude_asal.toDouble(),
                            dataTrayek.latitude_asal.toDouble()
                        )
                        val destination = Point.fromLngLat(
                            dataTrayek.longitude_tujuan.toDouble(),
                            dataTrayek.latitude_tujuan.toDouble()
                        )
                        requestRoute(origin, destination)

                        binding?.llDetail?.visible()
                        binding?.tvNamaPerusahaan?.text = "Nama Perusahaan: ${dataTrayek.nama_perusahaan}"
                        binding?.tvNamaTrayek?.text = "Nama Trayek: ${dataTrayek.nama_trayek}"
                        binding?.tvAsalTujuan?.text = "Asal: ${dataTrayek.asal}, Tujuan: ${dataTrayek.tujuan}"
                        binding?.tvHariJam?.text = "Hari ${dataTrayek.hari}, Jam: ${dataTrayek.jam}"

                        binding?.btnNavigation?.setOnClickListener {
                            val simulateRoute = false

                            val option = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(simulateRoute)
                                .build()

                            NavigationLauncher.startNavigation(activity, option)
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
                                    data[0].latitude_asal.toDouble(),
                                    data[0].longitude_asal.toDouble()
                                )
                            )
                            .withIconImage(ICON_ID)
                            .withData(Gson().toJsonTree(data))
                            .withTextField(data[0].nama_trayek)
                            .withTextHaloWidth(5.0f)
                            .withTextAnchor("top")
                            .withTextOffset(arrayOf(0f, 1.5f))
                    )

                    symbolManager.addClickListener { symbol ->

                        val origin = Point.fromLngLat(
                            data[0].longitude_asal.toDouble(),
                            data[0].latitude_asal.toDouble()
                        )
                        val destination = Point.fromLngLat(
                            data[0].longitude_tujuan.toDouble(),
                            data[0].latitude_tujuan.toDouble()
                        )
                        requestRoute(origin, destination)

                        binding?.llDetail?.visible()
                        binding?.tvNamaPerusahaan?.text = "Nama Perusahaan: ${data[0].nama_perusahaan}"
                        binding?.tvNamaTrayek?.text = "Nama Trayek: ${data[0].nama_trayek}"
                        binding?.tvAsalTujuan?.text = "Asal: ${data[0].asal}, Tujuan: ${data[0].tujuan}"
                        binding?.tvHariJam?.text = "Hari: ${data[0].hari}, Jam: ${data[0].jam}"

                        binding?.btnNavigation?.setOnClickListener {
                            val simulateRoute = false

                            val option = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(simulateRoute)
                                .build()

                            NavigationLauncher.startNavigation(activity, option)
                        }
                    }
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (view?.id) {
            1 -> {
                showToast(message = "Spinner 2 Position:${position} and asalList: ${asalString[position]}")
            }
            else -> {
                viewModel.getAllTrayekByAsal(asalString[position]).observe(this, { trayek ->
                    when (trayek.status) {
                        Status.SUCCESS -> {
                            Timber.d("cekss     ${trayek.data?.size}")
                            Timber.d("cek asasasas ${trayek.data}")

                            if (trayek.data != null) {
                                trayekList.clear()
                                if (trayek.data.isNotEmpty()) {
                                    for(i in trayek.data.indices){
                                        val trayekEntity = TrayekEntity(
                                            trayek.data[i].id_trayek,
                                            trayek.data[i].nama_trayek,
                                            trayek.data[i].asal,
                                            trayek.data[i].tujuan,
                                            trayek.data[i].id_jadwal,
                                            trayek.data[i].latitude_asal,
                                            trayek.data[i].longitude_asal,
                                            trayek.data[i].latitude_tujuan,
                                            trayek.data[i].longitude_tujuan,
                                            trayek.data[i].status,
                                            trayek.data[i].created_at,
                                            trayek.data[i].updated_at,
                                            trayek.data[i].jam,
                                            trayek.data[i].hari,
                                            trayek.data[i].nama_perusahaan,
                                        )

                                        trayekList.add(trayekEntity)
                                    }

                                    showMarker(trayekList)
                                }
                            } else {
                                Toast.makeText(activity, "Data tidak ditemukan", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }

                        Status.LOADING -> {

                        }

                        Status.ERROR -> {
                            Timber.d("cek error ${trayek.message}")
                        }
                    }
                })
                showToast(message = "Spinner 1 Position:${position} and asalList: ${asalString[position]}")
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        showToast(message = "Nothing selected")
    }

    private fun showToast(context: Context = activity!!.applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
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

    override fun onDestroy() {
        super.onDestroy()
        binding?.mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapView?.onLowMemory()
    }
    
}