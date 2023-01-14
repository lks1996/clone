package toy.review.domain;

public class WeatherDTO {
    private Long sky;
    private Long temp;
    private Long pty;
    private String rn1;
    private Long lgt;
    private Long reh;
    private Long vec;
    private Long wsd;

    public Long getSky() {
        return sky;
    }

    public void setSky(Long sky) {
        this.sky = sky;
    }

    public Long getTemp() {
        return temp;
    }

    public void setTemp(Long temp) {
        this.temp = temp;
    }

    public Long getPty() {
        return pty;
    }

    public void setPty(Long pty) {
        this.pty = pty;
    }

    public String getRn1() {
        return rn1;
    }

    public void setRn1(String rn1) {
        this.rn1 = rn1;
    }

    public Long getLgt() {
        return lgt;
    }

    public void setLgt(Long lgt) {
        this.lgt = lgt;
    }

    public Long getReh() {
        return reh;
    }

    public void setReh(Long reh) {
        this.reh = reh;
    }

    public Long getVec() {
        return vec;
    }

    public void setVec(Long vec) {
        this.vec = vec;
    }

    public Long getWsd() {
        return wsd;
    }

    public void setWsd(Long wsd) {
        this.wsd = wsd;
    }
}
