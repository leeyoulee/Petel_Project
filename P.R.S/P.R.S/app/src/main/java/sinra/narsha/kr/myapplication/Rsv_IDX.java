package sinra.narsha.kr.myapplication;

public class Rsv_IDX {
   String PET_IDX;
   String PET_NAME;

    public Rsv_IDX(String PET_IDX, String PET_NAME) {
        this.PET_IDX = PET_IDX;
        this.PET_NAME = PET_NAME;
    }

    public String getPET_IDX() {
        return PET_IDX;
    }

    public void setPET_IDX(String PET_IDX) {
        this.PET_IDX = PET_IDX;
    }

    public String getPET_NAME() {
        return PET_NAME;
    }

    public void setPET_NAME(String PET_NAME) {
        this.PET_NAME = PET_NAME;
    }
    //to display object as a string in spinner
    @Override
    public String toString() {
        return PET_NAME;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Rsv_IDX){
            Rsv_IDX rsv_IDX = (Rsv_IDX )obj;
            if(rsv_IDX.getPET_NAME().equals(PET_NAME)) {
                String PET_IDX = rsv_IDX.getPET_IDX();
            }
                return true;
        }
        return false;
    }

}
