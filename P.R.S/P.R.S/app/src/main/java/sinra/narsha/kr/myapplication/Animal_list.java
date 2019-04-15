package sinra.narsha.kr.myapplication;

public class Animal_list {

    String petType;
    String petName;
    String petAge;
    String petGender;
    String PET_IDX;

    public Animal_list(String petType, String petName, String petAge, String petGender, String PET_IDX) {
        this.petType = petType;
        this.petName = petName;
        this.petAge = petAge;
        this.petGender = petGender;
        this.PET_IDX = PET_IDX;
    }

    public String getPET_IDX() {
        return PET_IDX;
    }

    public void setPET_IDX(String PET_IDX) {
        this.PET_IDX = PET_IDX;
    }


    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetAge() {
        return petAge;
    }

    public void setPetAge(String petAge) {
        this.petAge = petAge;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }
}
