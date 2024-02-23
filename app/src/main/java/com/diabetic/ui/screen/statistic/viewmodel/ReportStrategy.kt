package com.diabetic.ui.screen.statistic.viewmodel

import com.diabetic.application.command.PrepareReport
import java.io.OutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone

sealed class ReportStrategy<T> {
    abstract fun fetch(filter: LongRange?): List<T>

    protected val LongRange.from get() = this.first
    protected val LongRange.to get() = this.last

    protected fun Long.asFromLocalDateTime(): LocalDateTime = this.asLocalDateTime()
        .toLocalDate()
        .atTime(0, 0)

    protected fun Long.asToLocalDateTime(): LocalDateTime = this.asLocalDateTime()
        .toLocalDate()
        .atTime(23, 59)

    protected fun Long.asLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        TimeZone.getDefault().toZoneId()
    )

    protected abstract val handler: PrepareReport.Handler

    fun generateReportName(filter: LongRange?): String{
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
}