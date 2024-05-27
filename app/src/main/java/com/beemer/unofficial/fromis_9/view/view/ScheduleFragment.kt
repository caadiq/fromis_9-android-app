package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.CalendarDayBinding
import com.beemer.unofficial.fromis_9.databinding.CalendarHeaderBinding
import com.beemer.unofficial.fromis_9.databinding.FragmentScheduleBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class ScheduleFragment : Fragment() {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarView: CalendarView

    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy년 M월", Locale.KOREA)

    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = binding.calendarView
        setupCalendar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCalendar() {
        val daysOfWeek = daysOfWeek()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(200)
        val endMonth = currentMonth.plusMonths(200)
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

                binding.txtDate.text = selectedDate.format(DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREA))
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
}