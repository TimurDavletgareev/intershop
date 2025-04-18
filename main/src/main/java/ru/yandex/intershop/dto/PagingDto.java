package ru.yandex.intershop.dto;

import lombok.Setter;

@Setter
public class PagingDto {

    private Integer pageNumber;
    private Integer pageSize;
    private Boolean hasNext;
    private Boolean hasPrevious;

    public Integer pageNumber() {
        return pageNumber;
    }

    public Integer pageSize() {
        return pageSize;
    }

    public Boolean hasNext() {
        return hasNext;
    }

    public Boolean hasPrevious() {
        return hasPrevious;
    }
}
