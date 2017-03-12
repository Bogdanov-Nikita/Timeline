package model.search;

public class SearchItemContainer {
    /**
     * aggregation: true = "AND" false = "OR"
     */
    boolean	aggregation;
    String Data[];
    /**
     *If Default Constructor aggregation = false, Author = new String[]{""}
     * */
    public SearchItemContainer() {
        aggregation = false;
        Data = new String[]{""};
    }
    public SearchItemContainer(String Author[],boolean aggregation) {
        this.Data = Author;
        this.aggregation = aggregation;
    }
    public void setAggregation(boolean aggregation) {
        this.aggregation = aggregation;
    }
    public String[] getData() {
        return Data;
    }
    public boolean isAggregation() {
        return aggregation;
    }
    public boolean isEmpty(){
        if(this.Data != null){
            for(int i=0;i<Data.length;i++){
                if(Data[i]!=null){
                    if(!Data[i].equals("")){
                        return false;
                    }
                }
            }
            return true;
        }
        else{return true;}
    }
}

