package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.CalendarDayBinding
import com.beemer.unofficial.fromis_9.databinding.CalendarHeaderBinding
import com.beemer.unofficial.fromis_9.databinding.FragmentScheduleBinding
import com.beemer.unofficial.fromis_9.model.dto.ScheduleListDto
import com.beemer.unofficial.fromis_9.view.adapter.ScheduleListAdapter
import com.beemer.unofficial.fromis_9.viewmodel.ScheduleViewModel
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@AndroidEntryPoint
class ScheduleFragment : Fragment() {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val scheduleViewModel : ScheduleViewModel by viewModels()

    private val scheduleListAdapter = ScheduleListAdapter()

    private lateinit var calendarView: CalendarView

    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy년 M월", Locale.KOREA)

    private var selectedDate: LocalDate = LocalDate.now()
    private var currentYear: Int = YearMonth.now().year

    private val scheduleList = mutableListOf<ScheduleListDto>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = binding.calendarView

        setupView()
        setupCalendar()
        setupRecyclerView()
        setupViewModel()

        scheduleViewModel.getScheduleList(currentYear, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.txtYearMonth.setOnClickListener {
            showYearMonthPickerDialog()
        }

        binding.imgDown.setOnClickListener {
            showYearMonthPickerDialog()
        }
    }

    private fun setupCalendar() {
        val daysOfWeek = daysOfWeek()
        val currentMonth = YearMonth.now()
        val startMonth = YearMonth.of(2018, 1)
        val endMonth = YearMonth.of(2050, 12)
        configureBinders(daysOfWeek)

        calendarView.apply {
            setup(startMonth, endMonth, daysOfWeek.first())
            scrollToMonth(currentMonth)

            monthScrollListener = { month ->
                binding.txtYearMonth.text = month.yearMonth.format(yearMonthFormatter)
                // 현재 달은 오늘, 다른 달은 1일로 초기화
                val oldDate = selectedDate
                selectedDate = if (month.yearMonth == currentMonth) LocalDate.now() else month.yearMonth.atDay(1)
                calendarView.notifyDateChanged(selectedDate)
                calendarView.notifyDateChanged(oldDate)

                // 년도가 바뀌었을 때만 바뀐 년도의 일정 목록을 가져옴
                if (month.yearMonth.year != currentYear) {
                    currentYear = month.yearMonth.year
                    scheduleViewModel.getScheduleList(currentYear, null)
                }
            }
        }

        binding.txtYearMonth.text = currentMonth.format(yearMonthFormatter)

        binding.imgPrev.setOnClickListener {
            calendarView.findFirstVisibleMonth()?.let {
                calendarView.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }

        binding.imgNext.setOnClickListener {
            calendarView.findFirstVisibleMonth()?.let {
                calendarView.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }
    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val binding = CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.date != selectedDate && day.position == DayPosition.MonthDate) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        calendarView.notifyDateChanged(day.date)
                        calendarView.notifyDateChanged(oldDate)
                        if (this@ScheduleFragment.binding.scrollView.scrollY != 0)
                            this@ScheduleFragment.binding.scrollView.smoothScrollTo(0, 0)
                    }
                }
            }
        }

        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data

                container.binding.txtCalendarDay.apply {
                    text = data.date.dayOfMonth.toString()
                    setTextColor(when {
                        data.date.dayOfWeek == DayOfWeek.SUNDAY && data.position == DayPosition.MonthDate -> resources.getColor(R.color.red, null)
                        data.date.dayOfWeek == DayOfWeek.SATURDAY && data.position == DayPosition.MonthDate -> resources.getColor(R.color.blue, null)
                        data.position == DayPosition.MonthDate -> resources.getColor(R.color.dark_gray, null)
                        else -> resources.getColor(R.color.light_gray, null)
                    })
                }

                container.binding.viewSelectedDay.visibility = if (data.date == selectedDate && data.position == DayPosition.MonthDate) View.VISIBLE else View.GONE

                val dailySchedules = scheduleList.filter { it.dateTime.contains(data.date.toString()) }
                val indicators = listOf(container.binding.viewIndicator1, container.binding.viewIndicator2, container.binding.viewIndicator3)

                indicators.forEachIndexed { index, indicator ->
                    if (index < dailySchedules.size && data.position == DayPosition.MonthDate) {
                        indicator.visibility = View.VISIBLE
                        val drawable = indicator.background as GradientDrawable
                        drawable.setColor(Color.parseColor("#${dailySchedules[index].color}"))
                        indicator.background = drawable
                    } else {
                        indicator.visibility = View.GONE
                    }
                }

                binding.txtDate.text = selectedDate.format(DateTimeFormatter.ofPattern("d일 (E)", Locale.KOREA))

                scheduleListAdapter.setItemList(scheduleList.filter {
                    it.dateTime.contains(selectedDate.toString())
                })

                binding.txtNoSchedule.visibility = if (scheduleListAdapter.itemCount == 0) View.VISIBLE else View.GONE
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val headerLayout = CalendarHeaderBinding.bind(view).layoutHeader.root
        }

        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                if (container.headerLayout.tag == null) {
                    container.headerLayout.tag = data.yearMonth
                    container.headerLayout.children.map { it as TextView }
                        .forEachIndexed { index, tv ->
                            tv.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.KOREA)
                            tv.setTextColor(when (daysOfWeek[index]) {
                                DayOfWeek.SUNDAY -> resources.getColor(R.color.red, null)
                                DayOfWeek.SATURDAY -> resources.getColor(R.color.blue, null)
                                else -> resources.getColor(R.color.dark_gray, null)
                            })
                        }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = scheduleListAdapter
            itemAnimator = null
        }

        scheduleListAdapter.setOnItemClickListener { item, _ ->
            item.url?.let {
                val intent = Intent(Intent.ACTION_VIEW, it.toUri())
                val packageManager = requireContext().packageManager
                val activities = packageManager.queryIntentActivities(intent, 0)
                val isIntentSafe = activities.isNotEmpty()

                if (isIntentSafe)
                    startActivity(intent)
                else
                    Toast.makeText(requireContext(), "해당 URL을 열 수 있는 앱이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupViewModel() {
        scheduleViewModel.scheduleList.observe(viewLifecycleOwner) { list ->
            scheduleList.clear()
            scheduleList.addAll(list)
            calendarView.notifyCalendarChanged()
        }
    }

    private fun showYearMonthPickerDialog() {
        YearMonthPickerDialog(yearMonth = selectedDate.yearMonth,
            onConfirm = {
                calendarView.scrollToMonth(it)
            }
        ).show(childFragmentManager, "YearMonthPickerDialog")
    }
}