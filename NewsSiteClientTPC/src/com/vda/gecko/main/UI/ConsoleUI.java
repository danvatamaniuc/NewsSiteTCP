package com.vda.gecko.main.UI;

import com.vda.gecko.data.manager.Manager;
import com.vda.gecko.data.transfer.Paginator;
import com.vda.gecko.main.domain.News;
import com.vda.gecko.main.domain.dto.NewsDto;
import com.vda.gecko.main.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by 1 on 10/8/2015.
 */
public class ConsoleUI {

    private Manager manager;

    public ConsoleUI(Manager manager) {
        this.manager = manager;
    }

    public void run(){
        String command;
        boolean work = true;

        while (work) {
            displayMenu();

            Scanner in = new Scanner(System.in);
            command = in.nextLine();

            switch (command) {
                case Constants.INPUT_END_CURRENT: work = false;
                    break;
                case "1": this.addNews();
                    break;
                case "2": this.displayAllNews();
                    break;
                case "3": this.displayPaginatedNews();
                    break;
            }
        }

//        displayMenu();
    }

    private void displayMenu() {
        System.out.println("Menu------------------");
        System.out.println("1: Add news");
        System.out.println("2: Display all news");
        System.out.println("3: Display news by page");
        System.out.println("x: Exit");
    }

    private void addNews(){
        String title;
        String content;

        Scanner scanIn = new Scanner(System.in);

        try {
            System.out.println("Titlul:");
            title = scanIn.next();

            System.out.println("Continut");
            content = scanIn.next();

            News news = new News(title, content);

            manager.save(news);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayAllNews(){
        ArrayList<News> allNews = (ArrayList) manager.getAllNews();

        for (News news : allNews){
            System.out.println(news.getTitle());
            System.out.println(news.getContent());
        }

        if (allNews.isEmpty()) {
            System.out.println("No news added!");
        }
    }

    private void displayPaginatedNews(){

        //choose sorting order
        System.out.println("Choose the sorting order:");
        System.out.println(Constants.SORT_ASCENDING + " for ascending");
        System.out.println(Constants.SORT_DESCENDING + " for descending");

        //read the sort order
        //TODO: have to check if declaring a new scanner each time you want to read smth is necessary
        Scanner inSort = new Scanner(System.in);
        String sortOrder = inSort.nextLine();

        Paginator<NewsDto> paginator = manager.getNewsPages(sortOrder);

        String command = Constants.NO_INITIAL_INPUT;

        while (true){

            // process user input
            //by default displays first page
            if (command.equals(Constants.PAGE_FORWARD)){
                if (paginator.canPageForward()) {
                    paginator.nextPage();
                } else {
                    System.out.println("Nu mai sunt pagini la dreapta!");
                }
            } else if (command.equals(Constants.PAGE_BACKWARDS)){
                if (paginator.canPageBackwards()){
                    paginator.previousPage();
                } else {
                    System.out.println("Nu mai sunt pagini la stanga!");
                }
            } else if (command.equals(Constants.INPUT_END_CURRENT)){
                break;
            }

            //get the page from the paginator
            List<NewsDto> news = paginator.getPage();

            //print the elements of said page
            for (NewsDto item : news) {
                System.out.println(item.toString());
            }

            System.out.println(Constants.PAGE_BACKWARDS +
                    "- back one page | " +
                    Constants.PAGE_FORWARD +
                    "- forward one page | " +
                    Constants.INPUT_END_CURRENT +
                    "- stop displaying news");

            Scanner in = new Scanner(System.in);
            command = in.nextLine();

        }
    }
}
