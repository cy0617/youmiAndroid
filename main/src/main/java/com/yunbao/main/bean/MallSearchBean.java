package com.yunbao.main.bean;

import java.util.List;

public class MallSearchBean {
    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":{"list":[{"id":"29","name":"一次性三层医用级口罩","sale_nums":"0","hits":"0","issale":"1","goods_url":"","istalking":"0","thumb":"http://qiniu.yczbfx.com/54660521_IOS_20200710002606_commodity_title_image0_cover.png","price":"90"}]}}
     * msg :
     */
    /**
     * code : 0
     * msg :
     * info : {"list":[{"id":"29","name":"一次性三层医用级口罩","sale_nums":"0","hits":"0","issale":"1","goods_url":"","istalking":"0","thumb":"http://qiniu.yczbfx.com/54660521_IOS_20200710002606_commodity_title_image0_cover.png","price":"90"}]}
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 29
         * name : 一次性三层医用级口罩
         * sale_nums : 0
         * hits : 0
         * issale : 1
         * goods_url :
         * istalking : 0
         * thumb : http://qiniu.yczbfx.com/54660521_IOS_20200710002606_commodity_title_image0_cover.png
         * price : 90
         */

        private String id;
        private String name;
        private String thumb;
        private String sale_nums;
        private String price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getSale_nums() {
            return sale_nums;
        }

        public void setSale_nums(String sale_nums) {
            this.sale_nums = sale_nums;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
