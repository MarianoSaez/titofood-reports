package ar.edu.um.fi.programacion2.reports.asyncTasks.exceptions;

public class ExpiredReportException extends RuntimeException {

    public ExpiredReportException(String msg) {
        super(msg);
    }
}
