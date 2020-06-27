package com.yunbao.common.bean;

public class BuyRecordBean {


    private long id;
    private long goodsid;
    private String spec_thumb;
    private String goods_name;
    private String price;
    private String total;
    private String status;
    private String status_name;
    private long addtime;
    private String nums;

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(long goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getSpec_thumb() {
        return spec_thumb;
    }

    public void setSpec_thumb(String spec_thumb) {
        this.spec_thumb = spec_thumb;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
