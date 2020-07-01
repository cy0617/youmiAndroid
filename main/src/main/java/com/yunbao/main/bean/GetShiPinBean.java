package com.yunbao.main.bean;

public class GetShiPinBean {

    /**
     * code : 0
     * msg :
     * data : {"id":"3","thumb":"http://qiniu.yczbfx.com/default/20200604/c78c745151ae7bedfc8a7cc79c33ff9e.mp4","min":"38"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * thumb : http://qiniu.yczbfx.com/default/20200604/c78c745151ae7bedfc8a7cc79c33ff9e.mp4
         * min : 38
         */

        private String id;
        private String thumb;
        private String min;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }
    }
}
