package com.TADevelopers;

import com.TADevelopers.model.Song;
import com.TADevelopers.model.SongRequest;
import com.TADevelopers.model.SongsBook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Asad Abbas on 5/27/2019.
 */
public class KaraokeMachine {
    private SongsBook mSongBook;
    private BufferedReader mReader;
    private Queue<SongRequest> mSongQueue;
    private Map<String, String> mMenu;

    public KaraokeMachine(SongsBook songBook) {
        mSongBook = songBook;
        mReader = new BufferedReader(new InputStreamReader(System.in));
        mSongQueue = new ArrayDeque<SongRequest>();
        mMenu = new HashMap<String, String>();
        mMenu.put("add", "Add a new song to the SongBook");
        mMenu.put("choose", "Choose a song to sing");
        mMenu.put("play", "Play next song in the queue");
        mMenu.put("quit", "Exit Program");
    }

    private String promptAction() throws IOException {
        System.out.printf("There are %d songs available in SongBook and %d in the Queue : %n" +
                "Your options are : %n", mSongBook.getSongCount(), mSongQueue.size());
        for (Map.Entry<String, String> option : mMenu.entrySet()) {
            System.out.printf("%s - %s %n", option.getKey(), option.getValue());
        }
        System.out.println("What do you wanna do?");
        String choice = mReader.readLine();
        return choice.trim().toLowerCase();
    }

    public void run() {
        String choice = "";
        do {
            try {
                choice = promptAction();
                switch (choice) {
                    case "add":
                        Song song = promptNewSong();
                        mSongBook.addSong(song);
                        System.out.printf("%s added to list. %n%n", song);
                        break;
                    case "choose":
                        String singerName = promptForSingerName();
                        String artist = promptForArtist();
                        Song artistSong = promptSongForArtist(artist);
                        SongRequest songRequest = new SongRequest(singerName, artistSong);
                        if (mSongQueue.contains(songRequest)) {
                            System.out.printf("%n%nWhoops %s already requested %s %n"
                            ,singerName,artistSong);
                            break;
                        }
                        mSongQueue.add(songRequest);
                        System.out.printf("You Chose : %s %n", artistSong);
                        break;
                    case "play":
                        playNext();
                        break;
                    case "quit":
                        System.out.println("Thanks for playing !");
                        break;
                    default:
                        System.out.printf("Your choice '%s' is not known to us%n%n", choice);
                }
            } catch (IOException e) {
                System.out.println("Problem with input");
                e.printStackTrace();
            }

        } while (!choice.equals("quit"));
    }

    private String promptForSingerName() throws IOException {
        System.out.print("Enter the Singer Name :   ");
        return mReader.readLine();
    }

    private Song promptNewSong() throws IOException {
        System.out.println("Enter artist name : ");
        String artist = mReader.readLine();
        System.out.println("Enter song name : ");
        String song = mReader.readLine();
        System.out.println("Enter Video URL : ");
        String videoUrl = mReader.readLine();
        return new Song(artist, song, videoUrl);
    }

    private String promptForArtist() throws IOException {
        System.out.println("Available Artist : ");
        List<String> artists = new ArrayList<>(mSongBook.getArtists());
        int index = promptForIndex(artists);
        return artists.get(index);

    }

    private Song promptSongForArtist(String artist) throws IOException {
        List<Song> songs = mSongBook.getSongsForArtist(artist);
        List<String> songTitle = new ArrayList<>();
        for (Song song : songs) {
            songTitle.add(song.getmTitle());

        }
        System.out.printf("Available Songs for %s : %n", artist);
        int index = promptForIndex(songTitle);
        return songs.get(index);

    }

    private int promptForIndex(List<String> options) throws IOException {
        int counter = 1;
        for (String option : options) {
            System.out.printf("%d.)  %s %n", counter, option);
            counter++;
        }
        System.out.println("Your Choice : ");
        String optionAsString = mReader.readLine();
        int choice = Integer.parseInt(optionAsString);
        return choice - 1;
    }

    private void playNext() {
        SongRequest songRequest = mSongQueue.poll();
        if (songRequest == null) {
            System.out.println("No songRequest in queue.Please add some.");
        } else {
            Song song = songRequest.getSong();
            System.out.printf("Are u Ready %s? Open %s to play %s by %s %n"
                    ,songRequest.getSingerName(), song.getmVideoUrl(), song.getmTitle(), song.getmArtist());
        }
    }

}
