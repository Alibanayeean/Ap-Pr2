package back.course;

import back.enums.HomeWorkOrExamType;

import java.util.Arrays;
import java.util.LinkedList;

public class EducationalMaterials {
    private int id;
    private String name;
    private LinkedList<String> addressesInServerOrText;
    private LinkedList<HomeWorkOrExamType> fileType;
    private String beginTime; //yyyy-mm-dd-hh-mm-ss;
    private int numFiles;

    public EducationalMaterials(int id, String name, String beginTime, LinkedList<String> addressesInServerOrText, LinkedList<HomeWorkOrExamType> fileType, int numFiles) {
        this.id = id;
        this.name = name;
        this.addressesInServerOrText = addressesInServerOrText;
        this.beginTime = beginTime;
        this.numFiles = numFiles;
        this.fileType = fileType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public LinkedList<String> getAddressesInServerOrText() {
        return addressesInServerOrText;
    }

    public void setAddressesInServerOrText(LinkedList<String> addressesInServerOrText) {
        this.addressesInServerOrText = addressesInServerOrText;
    }

    public int getNumFiles() {
        return numFiles;
    }

    public void setNumFiles(int numFiles) {
        this.numFiles = numFiles;
    }

    public LinkedList<HomeWorkOrExamType> getFileType() {
        return fileType;
    }

    public void setFileType(LinkedList<HomeWorkOrExamType> fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString(){

        return "name: " + name + "  fileName: " + Arrays.toString(addressesInServerOrText.toArray()) + "  beginTime: " + beginTime;
    }
}
