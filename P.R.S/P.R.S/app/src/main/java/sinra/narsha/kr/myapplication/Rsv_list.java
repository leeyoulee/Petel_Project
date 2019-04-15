package sinra.narsha.kr.myapplication;

public class Rsv_list {
    String START_TIME;
    String END_TIME;
    String MARKET_NO;
    String REV_IDX;
    String MARKET_NAME;
    String PET_NAME;
    String PET_IDX;

    public Rsv_list(String START_TIME, String END_TIME, String MARKET_NO, String REV_IDX, String MARKET_NAME, String PET_NAME, String PET_IDX) {
        this.START_TIME = START_TIME;
        this.END_TIME = END_TIME;
        this.MARKET_NO = MARKET_NO;
        this.REV_IDX = REV_IDX;
        this.MARKET_NAME = MARKET_NAME;
        this.PET_NAME = PET_NAME;
        this.PET_IDX = PET_IDX;
    }

    public String getPET_IDX() { return PET_IDX; }

    public void setPET_IDX(String PET_IDX) { this.PET_IDX = PET_IDX; }

    public String getMARKET_NAME() {
        return MARKET_NAME;
    }

    public void setMARKET_NAME(String MARKET_NAME) {
        this.MARKET_NAME = MARKET_NAME;
    }

    public String getPET_NAME() {
        return PET_NAME;
    }

    public void setPET_NAME(String PET_NAME) {
        this.PET_NAME = PET_NAME;
    }

    public String getREV_IDX() {
        return REV_IDX;
    }

    public void setREV_IDX(String REV_IDX) {
        this.REV_IDX = REV_IDX;
    }

    public String getMARKET_NO() {
        return MARKET_NO;
    }

    public void setMARKET_NO(String MARKET_NO) {
        this.MARKET_NO = MARKET_NO;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }
}
