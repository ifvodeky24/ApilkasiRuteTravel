package com.example.aplikasirutetravel.ui.angkutan

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
import com.example.aplikasirutetravel.R
import com.example.aplikasirutetravel.data.source.local.entity.TrayekEntity
import com.example.aplikasirutetravel.databinding.FragmentDetailAngkutanBinding
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
import timber.log.Timber

class DetailAngkutanFragment : Fragment() {
    private var _binding: FragmentDetailAngkutanBinding? = null
    private val binding get() = _binding
    private lateinit var symbolManager: SymbolManager
    private var data: TrayekEntity? = null
    private lateinit var navigationMapRoute: NavigationMapRoute
    private var currentRoute: DirectionsRoute? = null
    private lateinit var locationComponent: LocationComponent
    private lateinit var mylocation: LatLng
    private lateinit var permissionsManager: PermissionsManager

    companion object {
        const val ANGKUTAN = "angkutan"
        private const val ICON_ID = "ICON_ID"
    }

    private lateinit var mapboxMap: MapboxMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailAngkutanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            data = it.getParcelable(ANGKUTAN)
            Timber.d("cek data yuk $data")
        }

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

                style.addImage(
                    ICON_ID,
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.mapbox_marker_icon_default
                    )
                )

                showAsal()
                showTujuan()
                showRoute()
            }
        }
    }

    private fun showRoute() {

//        val destination = Point.fromLngLat(mylocation.longitude, mylocation.latitude)
        val destination = Point.fromLngLat(data!!.longitude_tujuan.toDouble(), data!!.latitude_tujuan.toDouble())
        val origin = Point.fromLngLat(data!!.longitude_asal.toDouble(), data!!.latitude_asal.toDouble())
//        val origin = Point.fromLngLat(mylocation.longitude, mylocation.latitude)
        Timber.d("oopp $origin yyeee $destination")
        requestRoute(origin, destination)
    }

    private fun showTujuan() {
        if (data != null) {
            val tujuan =
                LatLng(data!!.latitude_tujuan.toDouble(), data!!.longitude_tujuan.toDouble())
            symbolManager.create(
                SymbolOptions()
                    .withLatLng(LatLng(tujuan.latitude, tujuan.longitude))
                    .withIconImage(ICON_ID)
                    .withIconSize(1.5f)
                    .withIconOffset(arrayOf(0f, -1.5f))
                    .withTextField(data!!.tujuan)
                    .withTextHaloColor("rgba(255, 255, 255, 100)")
                    .withTextHaloWidth(5.0f)
                    .withTextAnchor("top")
                    .withTextOffset(arrayOf(0f, 1.5f))
                    .withDraggable(false)
            )
        }
    }

    private fun showAsal() {
        if (data != null) {
            val asal = LatLng(data!!.latitude_asal.toDouble(), data!!.longitude_asal.toDouble())
            symbolManager.create(
                SymbolOptions()
                    .withLatLng(LatLng(asal.latitude, asal.longitude))
                    .withIconImage(ICON_ID)
                    .withIconSize(1.5f)
                    .withIconOffset(arrayOf(0f, -1.5f))
                    .withTextField(data!!.asal)
                    .withTextHaloColor("rgba(255, 255, 255, 100)")
                    .withTextHaloWidth(5.0f)
                    .withTextAnchor("top")
                    .withTextOffset(arrayOf(0f, 1.5f))
                    .withDraggable(false)
            )

            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(asal, 8.0))
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

                    Timber.d("currr $currentRoute")

                    navigationMapRoute.addRoute(currentRoute)
                }

                override fun onFailure(call: retrofit2.Call<DirectionsResponse>, t: Throwable) {
                    Toast.makeText(activity, "Error : $t", Toast.LENGTH_SHORT).show()
                }
            })
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
                locationComponent.lastKnownLocation?.latitude as Double,
                locationComponent.lastKnownLocation?.longitude as Double
            )
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