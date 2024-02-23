package com.diabetic.application.command

import com.diabetic.domain.model.FoodIntakeRepository
import com.diabetic.domain.service.ReportMeta
import com.diabetic.domain.service.generateFoodIntakeReport
import java.io.OutputStream

class PrepareFoodIntakeReport : PrepareReport() {
    class Handler(
        private val repository: FoodIntakeRepository
    ) : PrepareReport.Handler() {
        override val template: String = "Food_intakes_report.%s.xlsx"
        override val sheetName: String = "Отчет по приемам пищи"

        override fun OutputStream.writeReport(
            command: WriteReportCommand,
            meta: ReportMeta
        ): OutputStream {
            val foodIntakes = repository.run {
                if (command.range === null) fetch() else fetch(
                    command.range.from,
                    command.range.to
                )
            }

            return this.generateFoodIntakeReport(
                foodIntakes,
                meta
            )
        }
    }
}