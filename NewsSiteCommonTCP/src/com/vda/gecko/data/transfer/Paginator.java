package com.vda.gecko.data.transfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 11/11/2015.
 * A class used for paginating the results of a query
 *
 * Properties: List<T> container - contains all the elements
 *                                  from which the pages are formed
 *             int currentStart - indicates the position from which the
 *                                  page should get its elements
 *             int previousStart - indicates the position from which
 *                                  the last page took its elements.
 *                                  useful for going back
 *             int pageSize      - page size. nr of elements to be sent
 *                                  at once
 *
 * Methods: setPageSize(int newPageSize) - sets the page size of the paginator
 *                                          the pages that the paginator will return
 *                                          will have this size
 *          setContent(List<T> content)  - specifies the container of the elements
 *                                          that are to be paginated
 *
 *          getPage()  - gets the current page from the paginator
 *          nextPage() - jumps to next page
 *          previousPage() - jumps to the previous page
 *          jumpToXPage(int x) - jumps to page x. Previous page becomes x-1
 *
 *          canPageForward() - checks if there are any pages left to be formed
 *                              same as hasNext of an iterator
 *          canPageBackwards() - checks if there are any pages that can be revisited
 *
 */
public class Paginator<T> implements Serializable{

    private List<T> container;
    private int currentStart;
    private int previousStart;
    private int pageSize;

    public Paginator(List<T> container) {

        this.container = container;

        //set the start of the paginator
        this.currentStart = 0;
        this.previousStart = -1;
        this.pageSize = pageSize;
    }

    public Paginator(int pageSize) {
        this.currentStart = 0;
        this.previousStart = -1;
        this.pageSize = pageSize;
    }

    //change the page size
    //Throws: IllegalArgumentException if the
    //        page size is set negative or 0
    public void setPageSize(int pageSize) {

        if (pageSize > 0) {
            this.pageSize = pageSize;
        } else {
            throw new IllegalArgumentException();
        }

    }

    //method to change the content in the container
    public void setContent(List<T> list){
        container = list;
    }

    public List<T> getPage(){

        int current = currentStart;

        int destination;

        //take care not to overflow the container
        if (current + pageSize > container.size()-1){
            destination = container.size();
        } else {
            destination = current + pageSize;
        }

        List<T> page = new ArrayList();

        //build the page
        while (current < destination){
            page.add(container.get(current));
            current++;
        }

        return page;
    }

    public Boolean canPageForward(){

        //return true if you can get another page
        return currentStart < container.size();
    }

    public Boolean canPageBackwards(){

        //returns true if you can get another page
        //going bacwards (e.g. from page 2 to 1)
        return currentStart > 0;
    }

    public void nextPage(){

        //save the previous page start
        previousStart = currentStart;

        currentStart += pageSize;
    }

    public void previousPage(){

        //revert to the page before the current one
        currentStart = currentStart - pageSize;

        previousStart = previousStart - pageSize;
    }

    public void jumpToXPage(int x){

        //check if the page exists
        if (x <= 0 || x * pageSize >= container.size()){
            throw new IllegalArgumentException("Page does not exist!");
        }

        currentStart = pageSize * x;
        previousStart = currentStart - pageSize;
    }

}
