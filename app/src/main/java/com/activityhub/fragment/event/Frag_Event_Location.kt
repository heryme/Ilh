package com.activityhub.fragment.event

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.fragment.home.Frag_Event_Directory
import com.activityhub.model.Event
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.toolbar.*

class Frag_Event_Location : Frag_Base(), View.OnClickListener {

    private val TAG = Frag_Event_Location::class.java.name
    private var eventData: Event.Data? = null
    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_event_location, container, false)
        }
        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()
        initMap()

        return rootView
    }

    override fun initComponent() {

        Log.e(TAG, "backStackEntryCount--->" + activity!!.supportFragmentManager.backStackEntryCount)
    }


    override fun initToolbar() {
        setHasOptionsMenu(true)
        (activity as Act_Home).setToolbarTitle(getString(R.string.location), false, true)
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.visibility = View.VISIBLE
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.text = getString(R.string.direction)
    }

    override fun initListeners() {
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.setOnClickListener(this)
    }

    override fun initData() {
        val bundle = arguments
        eventData = bundle!!.getSerializable(Frag_Event_Directory.eventData) as Event.Data
        Log.e(TAG, "eventData---->$eventData")
    }

    override fun initIDs(rootView: View) {
    }

    override fun onClick(view: View?) {
        when (view) {
            (activity as Act_Home).tvbarHomeActivityToolbarLocation -> {
                val uri = "http://maps.google.co.in/maps?q=" + eventData?.location
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                context!!.startActivity(intent)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapLocationFragment) as SupportMapFragment  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear() //clear old markers

            val googlePlex = CameraPosition.builder()
                    .target(LatLng(eventData?.latitude?.toDouble()!!, eventData?.longitude?.toDouble()!!))
                    .zoom(14f)
                    .bearing(0f)
                    .tilt(45f)
                    .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null)

//            this.mMap.setMyLocationEnabled(true)

            mMap.addMarker(MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_black))
                    .position(LatLng(eventData?.latitude?.toDouble()!!, eventData?.longitude?.toDouble()!!))
                    .title(eventData?.location))
        }

    }

    override fun onStop() {
        super.onStop()
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.visibility = View.GONE
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.text = ""
    }

    override fun onResume() {
        super.onResume()
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.visibility = View.VISIBLE
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.text = getString(R.string.direction)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.visibility = View.GONE
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.text = ""
    }

}

