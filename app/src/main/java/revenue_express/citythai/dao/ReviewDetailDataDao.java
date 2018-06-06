package revenue_express.citythai.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class ReviewDetailDataDao {

    String imgFoodReview, imgCaptionReview;

    public ReviewDetailDataDao(String imgFoodReview, String imgCaptionReview) {
        this.imgFoodReview = imgFoodReview;
        this.imgCaptionReview = imgCaptionReview;
    }

    public String getImgFoodReview() {
        return imgFoodReview;
    }

    public void setImgFoodReview(String imgFoodReview) {
        this.imgFoodReview = imgFoodReview;
    }

    public String getImgCaptionReview() {
        return imgCaptionReview;
    }

    public void setImgCaptionReview(String imgCaptionReview) {
        this.imgCaptionReview = imgCaptionReview;
    }
}
