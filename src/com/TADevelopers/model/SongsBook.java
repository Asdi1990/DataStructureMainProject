package com.TADevelopers.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Asad Abbas on 5/26/2019.
 */
public class SongsBook {
    List<Song> mSongs;

    public SongsBook() {
        mSongs = new ArrayList<Song>();
    }

    public void exportTo(String fileName) {
        try (
                FileOutputStream fos = new FileOutputStream(fileName);
                PrintWriter writer = new PrintWriter(fos);
        ) {
            for (Song song : mSongs) {
                writer.printf("%s|%s|%s %n", song.getmArtist(), song.getmTitle(),
                        song.getmVideoUrl());
            }

        } catch (IOException ioe) {
            System.out.printf("Problem saving %s %n", fileName);
            ioe.printStackTrace();
        }
    }

    public void importFrom(String fileName) {
        try (
                FileInputStream fis = new FileInputStream(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split("\\|");
                addSong(new Song(args[0], args[1], args[2]));
            }

        } catch (IOException ioe) {
            System.out.printf("Problem loading file %s ....", fileName);
            ioe.printStackTrace();
        }
    }

    public void addSong(Song song) {
        mSongs.add(song);
    }

    public int getSongCount() {
        return mSongs.size();
    }

    // Fix me this should be cached
    private Map<String, List<Song>> byArtist() {
        Map<String, List<Song>> byArtist = new TreeMap<>();
        for (Song song : mSongs) {
            List<Song> artistsSongs = byArtist.get(song.getmArtist());
            if (artistsSongs == null) {
                artistsSongs = new ArrayList<>();
                byArtist.put(song.getmArtist(), artistsSongs);
            }
            artistsSongs.add(song);
        }
        return byArtist;
    }

    public Set<String> getArtists() {
        return byArtist().keySet();
    }

    public List<Song> getSongsForArtist(String artistName) {
        List<Song> songs = byArtist().get(artistName);
        songs.sort(new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                if (o1.equals(o2)) {
                    return 0;
                }
                return o1.getmTitle().compareTo(o2.getmTitle());
            }
        });
        return songs;
    }
}
