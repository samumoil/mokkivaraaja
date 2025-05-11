package com.github.samumoil.mokkivaraaja.domain.handler;

import com.github.samumoil.mokkivaraaja.domain.database.DatabaseWorker;
import com.github.samumoil.mokkivaraaja.domain.object.ReportData;

public class ReportHandler {

    private static DatabaseWorker databaseWorker;
    public ReportHandler (DatabaseWorker dbw) {
        this.databaseWorker = dbw;
    }


    public static ReportData getReport(){
        return databaseWorker.getReportData();
    }


}
