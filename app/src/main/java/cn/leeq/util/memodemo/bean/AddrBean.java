package cn.leeq.util.memodemo.bean;

/**
 * Created by LeeQ
 * Date : 2016-8-17
 * Name : CourierKdb
 * Use :
 */
public class AddrBean {
    private int id;
    private String addrName;
    private int provinceId;
    private int cityId;
    private int areaId;
    public boolean isChoose;
    public AddrBean() {

    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName;
    }
}
