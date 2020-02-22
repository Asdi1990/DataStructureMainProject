package com.TADevelopers;


import com.TADevelopers.model.Song;
import com.TADevelopers.model.SongsBook;
import sun.net.www.http.HttpClient;

public class Karaoke {

    public static void main(String[] args) {
        // write your code here
        SongsBook sBook = new SongsBook();
        //below method is used to save files in text formats
        sBook.importFrom("songs.txt");
        KaraokeMachine kM = new KaraokeMachine(sBook);
        kM.run();
        System.out.println("Saving...");
        sBook.exportTo("songs.txt");


    }
}
