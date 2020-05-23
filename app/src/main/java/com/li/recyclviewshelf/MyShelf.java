package com.li.recyclviewshelf;

/**
 * @author ${Li}
 * 版本：1.0
 * 创建日期：2020/3/25 12
 * 描述：
 */
public class MyShelf {
    private int size ;
    private int length;
    private boolean choose;

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
