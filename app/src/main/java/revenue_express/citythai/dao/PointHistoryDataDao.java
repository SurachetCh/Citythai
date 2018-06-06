package revenue_express.citythai.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class PointHistoryDataDao {

    String PointStar,PointDollar, Status, Date;

    public PointHistoryDataDao(String PointStar, String PointDollar, String Status, String Date) {
        this.PointStar = PointStar;
        this.PointDollar = PointDollar;
        this.Status = Status;
        this.Date = Date;
    }

    public String getPointStar() {
        return PointStar;
    }

    public void setPointStar(String pointStar) {
        PointStar = pointStar;
    }

    public String getPointDollar() {
        return PointDollar;
    }

    public void setPointDollar(String pointDollar) {
        PointDollar = pointDollar;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
