package uk.co.dryhome.service.dto;

public class CustomerOrderStatsDTO {

    private Stat month;
    private Stat year;
    private Stat past12Months;

    public CustomerOrderStatsDTO() {
    }

    public CustomerOrderStatsDTO(Stat month, Stat year, Stat past12Months) {
        this.month = month;
        this.year = year;
        this.past12Months = past12Months;
    }

    @Override
    public String toString() {
        return "CustomerOrderStatsDTO{" +
            "month=" + month +
            ", year=" + year +
            ", past12Months=" + past12Months +
            '}';
    }

    public Stat getMonth() {
        return month;
    }

    public void setMonth(Stat month) {
        this.month = month;
    }

    public Stat getYear() {
        return year;
    }

    public void setYear(Stat year) {
        this.year = year;
    }

    public Stat getPast12Months() {
        return past12Months;
    }

    public void setPast12Months(Stat past12Months) {
        this.past12Months = past12Months;
    }
}
