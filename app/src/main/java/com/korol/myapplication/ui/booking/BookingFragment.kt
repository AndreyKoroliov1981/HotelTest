package com.korol.myapplication.ui.booking

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.korol.domain.booking.model.Booking
import com.korol.myapplication.R
import com.korol.myapplication.app.App
import com.korol.myapplication.common.IsErrorData
import com.korol.myapplication.databinding.FragmentBookingBinding
import com.korol.myapplication.ui.booking.model.Person
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import java.util.Locale

class BookingFragment : Fragment(R.layout.fragment_booking) {
    private val viewBinding: FragmentBookingBinding by viewBinding()

    @javax.inject.Inject
    lateinit var vmFactory: BookingViewModelFactory
    private lateinit var viewModel: BookingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.appComponent.injectBookingFragment(this)
        viewModel = ViewModelProvider(this, vmFactory)[BookingViewModel::class.java]
        setButtonBackListeners()
        setButtonPay()
        setPullToRefresh()
        settingPhoneAndEmailField()
        initFirstTourist()
        viewBinding.iitFirst.tvNumberTourist.text = getString(
            R.string.textForNumbersTourist,
            resources.getStringArray(R.array.digitString)[0],
        )
        setCloseInfoFirstTouristListener()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.collect {
                        if (it.booking != null) {
                            fillInfoBooking(it.booking)
                            fillPayTour(it.booking)
                            updateCvFirstTourist(it.isOpenViewPerson[0], it.persons[0])
                        }
                        viewBinding.pbLoad.isVisible = it.dataLoading
                        if (it.incorrectPhone != null) {
                            if (it.incorrectPhone) {
                                val colorValue = ContextCompat
                                    .getColor(
                                        requireContext(),
                                        R.color.color_background_input_field,
                                    )
                                viewBinding.inputPhone.llInputContainer.setBackgroundColor(
                                    colorValue,
                                )
                            } else {
                                val colorValue =
                                    ContextCompat.getColor(requireContext(), R.color.error_input)
                                viewBinding.inputPhone.llInputContainer.setBackgroundColor(
                                    colorValue,
                                )
                            }
                        }
                        if (it.incorrectEmail != null) {
                            if (it.incorrectEmail) {
                                val colorValue = ContextCompat
                                    .getColor(
                                        requireContext(),
                                        R.color.color_background_input_field,
                                    )
                                viewBinding.inputEmail.llInputContainer.setBackgroundColor(
                                    colorValue,
                                )
                            } else {
                                val colorValue =
                                    ContextCompat.getColor(requireContext(), R.color.error_input)
                                viewBinding.inputEmail.llInputContainer.setBackgroundColor(
                                    colorValue,
                                )
                            }
                        }
                    }
                }
                launch {
                    viewModel.sideEffect.collectLatest { sideEffect ->
                        when (sideEffect) {
                            is IsErrorData -> {
                                writeError(view, sideEffect.errorMessage)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initFirstTourist() {
        viewBinding.iitFirst.inputName.tilInputField.hint = getString(R.string.textNamePerson)
        viewBinding.iitFirst.inputSurname.tilInputField.hint = getString(R.string.textSurnamePerson)
        viewBinding.iitFirst.birthday.tilInputField.hint = getString(R.string.textBirthdayPerson)
        viewBinding.iitFirst.citizenship.tilInputField.hint = getString(R.string.textCitizenshipPerson)
        viewBinding.iitFirst.numberPassport.tilInputField.hint = getString(R.string.textNumberPassportPerson)
        viewBinding.iitFirst.validityPeriodPassport
            .tilInputField.hint = getString(R.string.textValidityPeriodPassportPerson)
    }

    private fun settingPhoneAndEmailField() {
        val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        mask.isHideHardcodedHead = false
        mask.placeholder = '*'
        mask.isShowingEmptySlots = true
        val formatWatcher: FormatWatcher = MaskFormatWatcher(mask)
        formatWatcher.installOn(viewBinding.inputPhone.tietInputField)
        viewBinding.inputPhone.tietInputField.inputType = InputType.TYPE_CLASS_PHONE
        viewBinding.inputPhone.tilInputField.hint = getString(R.string.textNumberPhone)
        viewBinding.inputPhone.tietInputField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.checkPhoneNumber(
                    viewBinding.inputPhone.tietInputField.text.toString(),
                )
            }
        }

        viewBinding.inputEmail.tietInputField.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        viewBinding.inputEmail.tilInputField.hint = getString(R.string.textEmail)
        viewBinding.inputEmail.tietInputField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.checkEmail(
                    viewBinding.inputEmail.tietInputField.text.toString(),
                )
            }
        }
    }

    private fun updateCvFirstTourist(isOpen: Boolean, person: Person) {
        if (isOpen) {
            viewBinding.iitFirst.ivArrow.setImageResource(R.drawable.ic_arrow_up)
            viewBinding.iitFirst.llContainerField.visibility = View.VISIBLE
        } else {
            viewBinding.iitFirst.ivArrow.setImageResource(R.drawable.ic_arrow_down)
            viewBinding.iitFirst.llContainerField.visibility = View.GONE
        }
    }

    private fun setCloseInfoFirstTouristListener() {
        viewBinding.iitFirst.ivArrow.setOnClickListener {
            viewModel.onClickFirstTouristArrow()
        }
    }

    private fun fillPayTour(booking: Booking) {
        with(viewBinding) {
            tvPayTour.text = getString(
                R.string.textForRoomPrice,
                String.format(
                    Locale.FRANCE,
                    "%,d",
                    booking.tourPrice,
                ),
            )
            tvPayFuelCharge.text = getString(
                R.string.textForRoomPrice,
                String.format(
                    Locale.FRANCE,
                    "%,d",
                    booking.fuelCharge,
                ),
            )
            tvPayServiceCharge.text = getString(
                R.string.textForRoomPrice,
                String.format(
                    Locale.FRANCE,
                    "%,d",
                    booking.serviceCharge,
                ),
            )
            tvInPay.text = getString(
                R.string.textForRoomPrice,
                String.format(
                    Locale.FRANCE,
                    "%,d",
                    viewModel.stateFlow.value.fullPay,
                ),
            )
        }
    }

    private fun fillInfoBooking(booking: Booking) {
        with(viewBinding) {
            tvRatingDigit.text = booking.horating.toString()
            tvRatingName.text = booking.ratingName
            tvNameHotel.text = booking.hotelName
            tvAddressHotel.text = booking.hotelAddress
            tvDeparture.text = booking.departure
            tvArrivalCountry.text = booking.arrivalCountry
            tvDateRange.text = this@BookingFragment.getString(
                R.string.textForDates,
                booking.tourDateStart,
                booking.tourDateStop,
            )
            tvNumberOfNights.text = this@BookingFragment.getString(
                R.string.textForNumbersOfNight,
                booking.numberOfNights.toString(),
            )
            tvHotelName.text = booking.hotelName
            tvRoomName.text = booking.room
            tvNutrition.text = booking.nutrition
        }
    }

    private fun setPullToRefresh() {
        viewBinding.srRefresh.setOnRefreshListener {
            viewBinding.srRefresh.isRefreshing = true
            viewModel.refreshLoad()
            viewBinding.srRefresh.isRefreshing = false
        }
    }

    private fun setButtonBackListeners() {
        viewBinding.ivBack.setOnClickListener {
            Navigation.findNavController(viewBinding.root).popBackStack()
        }
    }

    private fun setButtonPay() {
        viewBinding.btnPayRoom.setOnClickListener {
            val action = BookingFragmentDirections.actionFragmentBookingToFragmentOrderPay()
            Navigation.findNavController(viewBinding.root).navigate(action)
        }
    }

    private fun writeError(view: View, error: String) {
        val snackBarView =
            Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.error_text))
                .setAction("Retry") {
                    viewModel.onClickSendRequest()
                }
        val sbView = snackBarView.view
        val params = sbView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        sbView.layoutParams = params
        snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBarView.show()
    }
}