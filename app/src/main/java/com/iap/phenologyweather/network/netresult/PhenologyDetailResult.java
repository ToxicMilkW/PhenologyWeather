package com.iap.phenologyweather.network.netresult;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chenxueqing on 2017/4/19.
 */

public class PhenologyDetailResult {

    /**
     * status : ok
     * data : {"province":"北京市","city":"顺义区","district":"","species_name":"蝴蝶","species_image":"http://research.iap.ac.cn:8888/images/species/animals/蝴蝶.jpg","species_description":"蝴蝶一般色彩鲜艳，身上有好多条纹，色彩较丰富，翅膀和身体有各种花斑。蝴蝶翅膀上的鳞片不仅能使蝴蝶艳丽无比、还像是蝴蝶的一件雨衣。因为蝴蝶翅膀的鳞片里含有丰富的脂肪，能把蝴蝶保护起来，所以即使下小雨时，蝴蝶也能飞行。","watch_time_description":"六月下旬","predict_watch_time_description":"六月下旬至七月下旬","predict_description":"","spot_id":1,"spot_name":"七彩蝶园","spot_primary_description":"集蝴蝶养殖、观赏、科普教育及其他文化活动为一体.","spot_description":"七彩蝶园是亚洲大型活体蝴蝶观赏园，位于北京顺义，占地1000余亩，养殖名优蝴蝶30余种，年产蝴蝶约500万只。七彩蝶园是2009年北京市文化创意产业重点项目，它集蝴蝶养殖、观赏、科普教育及其他文化活动为一体，是全国唯一以蝴蝶为主题的亲子教育基地和科普教育基地。","poetry_image":"http://research.iap.ac.cn:8888/images/species/animals/poetry/蝴蝶.jpg","spot_location":[{"spot_id":50,"latitude":33.75,"longitude":112.38}]}
     */

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private PhenologyDetail phenologyDetail;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PhenologyDetail getPhenologyDetail() {
        return phenologyDetail;
    }

    public void setPhenologyDetail(PhenologyDetail phenologyDetail) {
        this.phenologyDetail = phenologyDetail;
    }

    public static class PhenologyDetail {
        /**
         * province : 北京市
         * city : 顺义区
         * district :
         * species_name : 蝴蝶
         * species_image : http://research.iap.ac.cn:8888/images/species/animals/蝴蝶.jpg
         * species_description : 蝴蝶一般色彩鲜艳，身上有好多条纹，色彩较丰富，翅膀和身体有各种花斑。蝴蝶翅膀上的鳞片不仅能使蝴蝶艳丽无比、还像是蝴蝶的一件雨衣。因为蝴蝶翅膀的鳞片里含有丰富的脂肪，能把蝴蝶保护起来，所以即使下小雨时，蝴蝶也能飞行。
         * watch_time_description : 六月下旬
         * predict_watch_time_description : 六月下旬至七月下旬
         * predict_description :
         * spot_id : 1
         * spot_name : 七彩蝶园
         * spot_primary_description : 集蝴蝶养殖、观赏、科普教育及其他文化活动为一体.
         * spot_description : 七彩蝶园是亚洲大型活体蝴蝶观赏园，位于北京顺义，占地1000余亩，养殖名优蝴蝶30余种，年产蝴蝶约500万只。七彩蝶园是2009年北京市文化创意产业重点项目，它集蝴蝶养殖、观赏、科普教育及其他文化活动为一体，是全国唯一以蝴蝶为主题的亲子教育基地和科普教育基地。
         * poetry_image : http://research.iap.ac.cn:8888/images/species/animals/poetry/蝴蝶.jpg
         * spot_location : [{"spot_id":50,"latitude":33.75,"longitude":112.38}]
         */

        @SerializedName("province")
        private String province;
        @SerializedName("city")
        private String city;
        @SerializedName("district")
        private String district;
        @SerializedName("species_name")
        private String species_name;
        @SerializedName("species_image")
        private String species_image;
        @SerializedName("species_description")
        private String species_description;
        @SerializedName("watch_time_description")
        private String watch_time_description;
        @SerializedName("predict_watch_time_description")
        private String predict_watch_time_description;
        @SerializedName("predict_description")
        private String predict_description;
        @SerializedName("spot_id")
        private int spot_id;
        @SerializedName("spot_name")
        private String spot_name;
        @SerializedName("spot_primary_description")
        private String spot_primary_description;
        @SerializedName("spot_description")
        private String spot_description;
        @SerializedName("poetry_image")
        private String poetry_image;
        @SerializedName("spot_location")
        private List<SpotLocation> spot_location;

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

        public String getSpecies_name() {
            return species_name;
        }

        public void setSpecies_name(String species_name) {
            this.species_name = species_name;
        }

        public String getSpecies_image() {
            return species_image;
        }

        public void setSpecies_image(String species_image) {
            this.species_image = species_image;
        }

        public String getSpecies_description() {
            return species_description;
        }

        public void setSpecies_description(String species_description) {
            this.species_description = species_description;
        }

        public String getWatch_time_description() {
            return watch_time_description;
        }

        public void setWatch_time_description(String watch_time_description) {
            this.watch_time_description = watch_time_description;
        }

        public String getPredict_watch_time_description() {
            return predict_watch_time_description;
        }

        public void setPredict_watch_time_description(String predict_watch_time_description) {
            this.predict_watch_time_description = predict_watch_time_description;
        }

        public String getPredict_description() {
            return predict_description;
        }

        public void setPredict_description(String predict_description) {
            this.predict_description = predict_description;
        }

        public int getSpot_id() {
            return spot_id;
        }

        public void setSpot_id(int spot_id) {
            this.spot_id = spot_id;
        }

        public String getSpot_name() {
            return spot_name;
        }

        public void setSpot_name(String spot_name) {
            this.spot_name = spot_name;
        }

        public String getSpot_primary_description() {
            return spot_primary_description;
        }

        public void setSpot_primary_description(String spot_primary_description) {
            this.spot_primary_description = spot_primary_description;
        }

        public String getSpot_description() {
            return spot_description;
        }

        public void setSpot_description(String spot_description) {
            this.spot_description = spot_description;
        }

        public String getPoetry_image() {
            return poetry_image;
        }

        public void setPoetry_image(String poetry_image) {
            this.poetry_image = poetry_image;
        }

        public List<SpotLocation> getSpot_location() {
            return spot_location;
        }

        public void setSpot_location(List<SpotLocation> spot_location) {
            this.spot_location = spot_location;
        }

        public static class SpotLocation {
            /**
             * spot_id : 50
             * latitude : 33.75
             * longitude : 112.38
             */

            @SerializedName("spot_id")
            private int spot_id;
            @SerializedName("latitude")
            private double latitude;
            @SerializedName("longitude")
            private double longitude;

            public int getSpot_id() {
                return spot_id;
            }

            public void setSpot_id(int spot_id) {
                this.spot_id = spot_id;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }
        }
    }
}
