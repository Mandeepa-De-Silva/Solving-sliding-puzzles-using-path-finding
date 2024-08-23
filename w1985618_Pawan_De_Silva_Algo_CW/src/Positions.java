/**
 Name: Pawan De Silva
 IIT ID: 20221846
 UoW ID: w1985618
 */

public class Positions {
    private final int rowNum;
    private final int colNum;
    public Positions parent;
    public String directions;

    public Positions(int rowNum, int colNum){
        this.rowNum = rowNum;
        this.colNum = colNum;
    }

    public Positions getParent(){
        return parent;
    }

    public void setParent(Positions parent){
        this.parent = parent;
    }
    public int getRowNum(){
        return rowNum;
    }
    public int getColNum(){
        return colNum;
    }
}
