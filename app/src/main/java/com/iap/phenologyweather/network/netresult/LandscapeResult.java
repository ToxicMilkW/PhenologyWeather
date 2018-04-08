package com.iap.phenologyweather.network.netresult;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chenxueqing on 2017/4/20.
 */

public class LandscapeResult {

    /**
     * status : ok
     * data : {"province":"北京市","city":"顺义区","district":"","name":"七彩蝶园","address":"北京市顺义区七彩蝶园","description":"七彩蝶园是亚洲大型活体蝴蝶观赏园，位于北京顺义，占地1000余亩，养殖名优蝴蝶30余种，年产蝴蝶约500万只。七彩蝶园是2009年北京市文化创意产业重点项目，它集蝴蝶养殖、观赏、科普教育及其他文化活动为一体，是全国唯一以蝴蝶为主题的亲子教育基地和科普教育基地。","image":"http://research.iap.ac.cn:8888/images/default/昌平区.jpg","feature":"集蝴蝶养殖、观赏、科普教育及其他文化活动为一体.","funny_image":"http://research.iap.ac.cn:8888/images/species/animals/蝴蝶.jpg","poetry_image":"http://research.iap.ac.cn:8888/images/species/animals/蝴蝶.jpg","associated_spot":[{"spot_id":1,"name":"天鹅"}]}
     */
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private Landscape data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Landscape getData() {
        return data;
    }

    public void setData(Landscape data) {
        this.data = data;
    }

    public static class Landscape {
        /**
         * province : 北京市
         * city : 顺义区
         * district :
         * name : 七彩蝶园
         * address : 北京市顺义区七彩蝶园
         * description : 七彩蝶园是亚洲大型活体蝴蝶观赏园，位于北京顺义，占地1000余亩，养殖名优蝴蝶30余种，年产蝴蝶约500万只。七彩蝶园是2009年北京市文化创意产业重点项目，它集蝴蝶养殖、观赏、科普教育及其他文化活动为一体，是全国唯一以蝴蝶为主题的亲子教育基地和科普教育基地。
         * image : http://research.iap.ac.cn:8888/images/default/昌平区.jpg
         * feature : 集蝴蝶养殖、观赏、科普教育及其他文化活动为一体.
         * funny_image : http://research.iap.ac.cn:8888/images/species/animals/蝴蝶.jpg
         * poetry_image : http://research.iap.ac.cn:8888/images/species/animals/蝴蝶.jpg
         * associated_spot : [{"spot_id":1,"name":"天鹅"}]
         */
        @SerializedName("province")
        private String province;
        @SerializedName("city")
        private String city;
        @SerializedName("district")
        private String district;
        @SerializedName("name")
        private String name;
        @SerializedName("address")
        private String address;
        @SerializedName("description")
        private String description;
        @SerializedName("image")
        private String image;
        @SerializedName("feature")
        private String feature;
        @SerializedName("funny_image")
        private String funny_image;
        @SerializedName("poetry_image")
        private String poetry_image;
        @SerializedName("associated_spot")
        private List<AssociatedSpot> associated_spot;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getFunny_image() {
            return funny_image;
        }

        public void setFunny_image(String funny_image) {
            this.funny_image = funny_image;
        }

        public String getPoetry_image() {
            return poetry_image;
        }

        public void setPoetry_image(String poetry_image) {
            this.poetry_image = poetry_image;
        }

        public List<AssociatedSpot> getAssociated_spot() {
            return associated_spot;
        }

        public void setAssociated_spot(List<AssociatedSpot> associated_spot) {
            this.associated_spot = associated_spot;
        }

        public static class AssociatedSpot {
            /**
             * spot_id : 1
             * name : 天鹅
             */

            @SerializedName("spot_id")
            private int spot_id;
            @SerializedName("name")
            private String name;

            public int getSpot_id() {
                return spot_id;
            }

            public void setSpot_id(int spot_id) {
                this.spot_id = spot_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
