package com.secure.practice.entity.vo;

import java.util.List;

public class Page<T> {
    Integer total;
    List<T> list;

    @Override
    public String toString() {
        return "Page{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
