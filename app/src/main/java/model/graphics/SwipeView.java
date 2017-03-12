package model.graphics;

import java.util.ArrayList;

public class SwipeView {

    int Page;
    ArrayList<Integer> ScrollPosition;
    boolean empty;

    public SwipeView() {
        Page=0;
        ScrollPosition = new ArrayList<Integer>();
        ScrollPosition.add(0);
        empty = true;
    }
    public SwipeView(int Page,int ScrollBar[]) {
        this.Page = Page;
        ScrollPosition = new ArrayList<Integer>();
        for(int i=0;i<ScrollBar.length;i++){
            ScrollPosition.add(ScrollBar[i]);
        }
        empty = false;
    }
    public int getPage() {
        return Page;
    }
    public int[] getScrollPosition() {
        int array[] = new int[ScrollPosition.size()];
        for(int i=0;i<array.length;i++){
            array[i] = ScrollPosition.get(i);}
        return array;
    }
    public void setPage(int page) {
        Page = page;
    }
    public void setScrollPosition(int[] scrollPosition) {
        ScrollPosition = new ArrayList<Integer>();
        for(int i=0;i<scrollPosition.length;i++){
            ScrollPosition.add(scrollPosition[i]);
        }
    }
    public void setScrollPosition(int index,int Position) {
        ScrollPosition.set(index, Position);
    }
    public int getScrollPosition(int index) {
        return ScrollPosition.get(index);
    }
    public void addScroll(int Position){
        ScrollPosition.add(Position);
    }
    public void addZeros(int difference){
        for(int i=0; i<difference; i++){
            ScrollPosition.add(0);
        }
    }
    public boolean isEmpty() {
        return empty;
    }
    public int size(){
        return ScrollPosition.size();
    }
}

