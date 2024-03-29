package com.diabetic.ui.screen.statistic.viewmodel

import com.diabetic.application.command.PrepareReport
import com.diabetic.domain.model.time.datetime
import java.io.OutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone

sealed class ReportStrategy<out T> {
    abstract fun fetch(filter: LongRange?): List<@UnsafeVariance T>

    protected val LongRange.from get() = this.first
    protected val LongRange.to get() = this.last

    protected fun Long.asFromLocalDateTime(): LocalDateTime = this.datetime
        .toLocalDate()
        .atTime(0, 0)

    protected fun Long.asToLocalDateTime(): LocalDateTime = this.datetime
        .toLocalDate()
        .atTime(23, 59)

    protected abstract val handler: PrepareReport.Handler

    fun generateReportName(filter: LongRange?): String {
        return handler.handle(
            PrepareReport.GenerateFileNameCommand(
                if (filter == null) null else Pair(
                    filter.from.asFromLocalDateTime(),
                    filter.to.asToLocalDateTime()
                )
            )
        )
    }

    fun generateReport(filter: LongRange?, stream: OutputStream) {
        return handler.handle(
            PrepareReport.WriteReportCommand(
                if (filter == null) null else Pair(
                    filter.from.asFromLocalDateTime(),
                    filter.to.asToLocalDateTime()
                ),
                stream
            )
        )
    }

    abstract fun delete(element: @UnsafeVariance T)
}