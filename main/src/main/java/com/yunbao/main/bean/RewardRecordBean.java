package com.yunbao.main.bean;

public class RewardRecordBean {
    //    /**
//     * ret : 200
//     * data : {"code":0,"msg":"","info":[{"id":"15","uid":"60484847","pid":"86407252","createtime":"2020-06-25 14:50:41","endtime":"2020-06-25 14:51:03","status":"2","money":"100.00","n_level":"0","t_level":"1","img":"http://qiniu.yczbfx.com/20200625145048_933af294f386eb9ea3d331489b4c3dc3?imageView2/2/w/600/h/600","updatetime":"2020-06-25 14:50:51","suser":false},{"id":"14","uid":"22034130","pid":"86407252","createtime":"2020-06-25 14:42:26","endtime":"2020-06-25 14:44:17","status":"2","money":"100.00","n_level":"0","t_level":"1","img":"http://qiniu.yczbfx.com/20200625144406_84f75107322cca732eff5668ffbce2db?imageView2/2/w/600/h/600","updatetime":"2020-06-25 14:44:08","suser":{"weixin":"1511881","mobile":"15068868668","user_nicename":"手机用户8668","avatar":"http://qiniu.yczbfx.com/20200625144406_84f75107322cca732eff5668ffbce2db?imageView2/2/w/600/h/600"},"mobile":"150****8668","weixin":"1511881","user_nicename":"手机用户8668","avatar":"http://qiniu.yczbfx.com/20200625144406_84f75107322cca732eff5668ffbce2db?imageView2/2/w/600/h/600"}]}
//     * msg :
//     */
//
//    private int ret;
//    private DataBean data;
//    private String msg;
//
//    public int getRet() {
//        return ret;
//    }
//
//    public void setRet(int ret) {
//        this.ret = ret;
//    }
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public static class DataBean {
//        /**
//         * code : 0
//         * msg :
//         * info : [{"id":"15","uid":"60484847","pid":"86407252","createtime":"2020-06-25 14:50:41","endtime":"2020-06-25 14:51:03","status":"2","money":"100.00","n_level":"0","t_level":"1","img":"http://qiniu.yczbfx.com/20200625145048_933af294f386eb9ea3d331489b4c3dc3?imageView2/2/w/600/h/600","updatetime":"2020-06-25 14:50:51","suser":false},{"id":"14","uid":"22034130","pid":"86407252","createtime":"2020-06-25 14:42:26","endtime":"2020-06-25 14:44:17","status":"2","money":"100.00","n_level":"0","t_level":"1","img":"http://qiniu.yczbfx.com/20200625144406_84f75107322cca732eff5668ffbce2db?imageView2/2/w/600/h/600","updatetime":"2020-06-25 14:44:08","suser":{"weixin":"1511881","mobile":"15068868668","user_nicename":"手机用户8668","avatar":"http://qiniu.yczbfx.com/20200625144406_84f75107322cca732eff5668ffbce2db?imageView2/2/w/600/h/600"},"mobile":"150****8668","weixin":"1511881","user_nicename":"手机用户8668","avatar":"http://qiniu.yczbfx.com/20200625144406_84f75107322cca732eff5668ffbce2db?imageView2/2/w/600/h/600"}]
//         */
//
//        private int code;
//        private String msg;
//        private List<InfoBean> info;
//
//        public int getCode() {
//            return code;
//        }
//
//        public void setCode(int code) {
//            this.code = code;
//        }
//
//        public String getMsg() {
//            return msg;
//        }
//
//        public void setMsg(String msg) {
//            this.msg = msg;
//        }
//
//        public List<InfoBean> getInfo() {
//            return info;
//        }
//
//        public void setInfo(List<InfoBean> info) {
//            this.info = info;
//        }
//
//        public static class InfoBean {
//            /**
//             * id : 15
//             * uid : 60484847
//             * pid : 86407252
//             * createtime : 2020-06-25 14:50:41
//             * endtime : 2020-06-25 14:51:03
//             * status : 2
//             * money : 100.00
//             * n_level : 0
//             * t_level : 1
//             * img : http://qiniu.yczbfx.com/20200625145048_933af294f386eb9ea3d331489b4c3dc3?imageView2/2/w/600/h/600
//             * updatetime : 2020-06-25 14:50:51
//             * suser : false
//             * mobile : 150****8668
//             * weixin : 1511881
//             * user_nicename : 手机用户8668
//             * avatar : http://qiniu.yczbfx.com/20200625144406_84f75107322cca732eff5668ffbce2db?imageView2/2/w/600/h/600
//             */
//
    private String id;
    private String uid;
    private String pid;
    private String createtime;
    private String endtime;
    private String status;
    private String money;
    private String n_level;
    private String t_level;
    private String img;
    private String updatetime;
    private boolean suser;
    private String mobile;
    private String weixin;
    private String user_nicename;
    private String avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getN_level() {
        return n_level;
    }

    public void setN_level(String n_level) {
        this.n_level = n_level;
    }

    public String getT_level() {
        return t_level;
    }

    public void setT_level(String t_level) {
        this.t_level = t_level;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public boolean isSuser() {
        return suser;
    }

    public void setSuser(boolean suser) {
        this.suser = suser;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
