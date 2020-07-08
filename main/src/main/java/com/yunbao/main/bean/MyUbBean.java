package com.yunbao.main.bean;

public class MyUbBean {

            /**
             * id : 284
             * type : 0
             * action : 1
             * uid : 54660521
             * touid : 13702
             * giftid : 74
             * giftcount : 1
             * totalcoin : 1
             * showid : 1594127025
             * addtime : 1594127330
             * mark : 0
             * userinfo : {"id":"54660521","user_nicename":"向阳而生","avatar":"http://qiniu.yczbfx.com/20200705074336_ed17f904b6e2388a13d40000885a8814?imageView2/2/w/600/h/600","avatar_thumb":"http://qiniu.yczbfx.com/20200705074336_ed17f904b6e2388a13d40000885a8814?imageView2/2/w/200/h/200","sex":"0","signature":"以目标为导向  过程只是风景","consumption":"219","votestotal":"61","province":"","city":"好像在火星","birthday":"","user_status":"1","issuper":"0","location":"","ky_score":"0.2521","mobile":"13311111111","level":"3","level_anchor":"2","vip":{"type":"0"},"liang":{"name":"0"}}
             * touserinfo : {"id":"13702","user_nicename":"段子哥","avatar":"http://qiniu.yczbfx.com/20200707205707_38f97d897db3ab1c61d61fb1c3635060?imageView2/2/w/600/h/600","avatar_thumb":"http://qiniu.yczbfx.com/20200707205707_38f97d897db3ab1c61d61fb1c3635060?imageView2/2/w/200/h/200","sex":"0","signature":"这家伙很懒，什么都没留下","consumption":"0","votestotal":"13","province":"","city":"好像在火星","birthday":"1996-08-19","user_status":"1","issuper":"0","location":"陕西省西安市雁塔区","ky_score":"0.0972","mobile":"18717334864","level":"1","level_anchor":"2","vip":{"type":"0"},"liang":{"name":"0"}}
             * giftinfo : {"giftname":"星星"}
             */

            private String id;
            private String type;
            private String action;
            private String uid;
            private String touid;
            private String giftid;
            private String giftcount;
            private String totalcoin;
            private String showid;
            private String addtime;
            private String mark;
            private UserinfoBean userinfo;
            private TouserinfoBean touserinfo;
            private GiftinfoBean giftinfo;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getTouid() {
                return touid;
            }

            public void setTouid(String touid) {
                this.touid = touid;
            }

            public String getGiftid() {
                return giftid;
            }

            public void setGiftid(String giftid) {
                this.giftid = giftid;
            }

            public String getGiftcount() {
                return giftcount;
            }

            public void setGiftcount(String giftcount) {
                this.giftcount = giftcount;
            }

            public String getTotalcoin() {
                return totalcoin;
            }

            public void setTotalcoin(String totalcoin) {
                this.totalcoin = totalcoin;
            }

            public String getShowid() {
                return showid;
            }

            public void setShowid(String showid) {
                this.showid = showid;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public UserinfoBean getUserinfo() {
                return userinfo;
            }

            public void setUserinfo(UserinfoBean userinfo) {
                this.userinfo = userinfo;
            }

            public TouserinfoBean getTouserinfo() {
                return touserinfo;
            }

            public void setTouserinfo(TouserinfoBean touserinfo) {
                this.touserinfo = touserinfo;
            }

            public GiftinfoBean getGiftinfo() {
                return giftinfo;
            }

            public void setGiftinfo(GiftinfoBean giftinfo) {
                this.giftinfo = giftinfo;
            }

            public static class UserinfoBean {
                /**
                 * id : 54660521
                 * user_nicename : 向阳而生
                 * avatar : http://qiniu.yczbfx.com/20200705074336_ed17f904b6e2388a13d40000885a8814?imageView2/2/w/600/h/600
                 * avatar_thumb : http://qiniu.yczbfx.com/20200705074336_ed17f904b6e2388a13d40000885a8814?imageView2/2/w/200/h/200
                 * sex : 0
                 * signature : 以目标为导向  过程只是风景
                 * consumption : 219
                 * votestotal : 61
                 * province :
                 * city : 好像在火星
                 * birthday :
                 * user_status : 1
                 * issuper : 0
                 * location :
                 * ky_score : 0.2521
                 * mobile : 13311111111
                 * level : 3
                 * level_anchor : 2
                 * vip : {"type":"0"}
                 * liang : {"name":"0"}
                 */

                private String id;
                private String user_nicename;
                private String avatar;
                private String avatar_thumb;
                private String sex;
                private String signature;
                private String consumption;
                private String votestotal;
                private String province;
                private String city;
                private String birthday;
                private String user_status;
                private String issuper;
                private String location;
                private String ky_score;
                private String mobile;
                private String level;
                private String level_anchor;
                private VipBean vip;
                private LiangBean liang;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
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

                public String getAvatar_thumb() {
                    return avatar_thumb;
                }

                public void setAvatar_thumb(String avatar_thumb) {
                    this.avatar_thumb = avatar_thumb;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public String getSignature() {
                    return signature;
                }

                public void setSignature(String signature) {
                    this.signature = signature;
                }

                public String getConsumption() {
                    return consumption;
                }

                public void setConsumption(String consumption) {
                    this.consumption = consumption;
                }

                public String getVotestotal() {
                    return votestotal;
                }

                public void setVotestotal(String votestotal) {
                    this.votestotal = votestotal;
                }

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

                public String getBirthday() {
                    return birthday;
                }

                public void setBirthday(String birthday) {
                    this.birthday = birthday;
                }

                public String getUser_status() {
                    return user_status;
                }

                public void setUser_status(String user_status) {
                    this.user_status = user_status;
                }

                public String getIssuper() {
                    return issuper;
                }

                public void setIssuper(String issuper) {
                    this.issuper = issuper;
                }

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public String getKy_score() {
                    return ky_score;
                }

                public void setKy_score(String ky_score) {
                    this.ky_score = ky_score;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public String getLevel() {
                    return level;
                }

                public void setLevel(String level) {
                    this.level = level;
                }

                public String getLevel_anchor() {
                    return level_anchor;
                }

                public void setLevel_anchor(String level_anchor) {
                    this.level_anchor = level_anchor;
                }

                public VipBean getVip() {
                    return vip;
                }

                public void setVip(VipBean vip) {
                    this.vip = vip;
                }

                public LiangBean getLiang() {
                    return liang;
                }

                public void setLiang(LiangBean liang) {
                    this.liang = liang;
                }

                public static class VipBean {
                    /**
                     * type : 0
                     */

                    private String type;

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }
                }

                public static class LiangBean {
                    /**
                     * name : 0
                     */

                    private String name;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }
            }

            public static class TouserinfoBean {
                /**
                 * id : 13702
                 * user_nicename : 段子哥
                 * avatar : http://qiniu.yczbfx.com/20200707205707_38f97d897db3ab1c61d61fb1c3635060?imageView2/2/w/600/h/600
                 * avatar_thumb : http://qiniu.yczbfx.com/20200707205707_38f97d897db3ab1c61d61fb1c3635060?imageView2/2/w/200/h/200
                 * sex : 0
                 * signature : 这家伙很懒，什么都没留下
                 * consumption : 0
                 * votestotal : 13
                 * province :
                 * city : 好像在火星
                 * birthday : 1996-08-19
                 * user_status : 1
                 * issuper : 0
                 * location : 陕西省西安市雁塔区
                 * ky_score : 0.0972
                 * mobile : 18717334864
                 * level : 1
                 * level_anchor : 2
                 * vip : {"type":"0"}
                 * liang : {"name":"0"}
                 */

                private String id;
                private String user_nicename;
                private String avatar;
                private String avatar_thumb;
                private String sex;
                private String signature;
                private String consumption;
                private String votestotal;
                private String province;
                private String city;
                private String birthday;
                private String user_status;
                private String issuper;
                private String location;
                private String ky_score;
                private String mobile;
                private String level;
                private String level_anchor;
                private VipBeanX vip;
                private LiangBeanX liang;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
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

                public String getAvatar_thumb() {
                    return avatar_thumb;
                }

                public void setAvatar_thumb(String avatar_thumb) {
                    this.avatar_thumb = avatar_thumb;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public String getSignature() {
                    return signature;
                }

                public void setSignature(String signature) {
                    this.signature = signature;
                }

                public String getConsumption() {
                    return consumption;
                }

                public void setConsumption(String consumption) {
                    this.consumption = consumption;
                }

                public String getVotestotal() {
                    return votestotal;
                }

                public void setVotestotal(String votestotal) {
                    this.votestotal = votestotal;
                }

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

                public String getBirthday() {
                    return birthday;
                }

                public void setBirthday(String birthday) {
                    this.birthday = birthday;
                }

                public String getUser_status() {
                    return user_status;
                }

                public void setUser_status(String user_status) {
                    this.user_status = user_status;
                }

                public String getIssuper() {
                    return issuper;
                }

                public void setIssuper(String issuper) {
                    this.issuper = issuper;
                }

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public String getKy_score() {
                    return ky_score;
                }

                public void setKy_score(String ky_score) {
                    this.ky_score = ky_score;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public String getLevel() {
                    return level;
                }

                public void setLevel(String level) {
                    this.level = level;
                }

                public String getLevel_anchor() {
                    return level_anchor;
                }

                public void setLevel_anchor(String level_anchor) {
                    this.level_anchor = level_anchor;
                }

                public VipBeanX getVip() {
                    return vip;
                }

                public void setVip(VipBeanX vip) {
                    this.vip = vip;
                }

                public LiangBeanX getLiang() {
                    return liang;
                }

                public void setLiang(LiangBeanX liang) {
                    this.liang = liang;
                }

                public static class VipBeanX {
                    /**
                     * type : 0
                     */

                    private String type;

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }
                }

                public static class LiangBeanX {
                    /**
                     * name : 0
                     */

                    private String name;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }
            }

            public static class GiftinfoBean {
                /**
                 * giftname : 星星
                 */

                private String giftname;

                public String getGiftname() {
                    return giftname;
                }

                public void setGiftname(String giftname) {
                    this.giftname = giftname;
                }
            }

}
