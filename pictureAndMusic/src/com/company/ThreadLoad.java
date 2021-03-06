package com.company;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
public class ThreadLoad implements Runnable {
    Thread animal;
    private static final String IN_FILE_TXT = "src\\ru\\demo\\downloadmusic\\inFile.txt";
    private static final String OUT_FILE_TXT = "src\\ru\\demo\\downloadmusic\\outFile.txt";
    private static final String PATH_TO_MUSIC = "src\\ru\\demo\\downloadmusic\\music";

    private static final String IN_FILE_TXT1 = "src\\ru\\demo\\downloadpicture\\inFile.txt";
    private static final String OUT_FILE_TXT1 = "src\\ru\\demo\\downloadpicture\\outFile.txt";
    private static final String PATH_TO_MUSIC1 = "src\\ru\\demo\\downloadpicture\\pic";

    public ThreadLoad(String name) {
        animal = new Thread(this, name);
        //Создание потока
        System.out.println("Поток " + name + " запущен");
        //Вывод названия потока
        animal.start();}
        //Запуск потока

    @Override
    public void run() {
        try {
            String Url;
            try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));  //Считывание пути файла с сылкой
                 BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILE_TXT))) {
                while ((Url = inFile.readLine()) != null) {  //Считывать пока не кончится
                    URL url = new URL(Url);

                    String result;
                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                        result = bufferedReader.lines().collect(Collectors.joining("\n")); }
                    Pattern email_pattern = Pattern.compile("\\s*(?<=data-url\\s?=\\s?\")[^>]*/*(?=\")");  //Паттерн ссылки
                    Matcher matcher = email_pattern.matcher(result);
                    int i = 0;
                    while (matcher.find() && i < 1) {
                        outFile.write(matcher.group() + "\r\n");
                        i++;}
                }
            } catch (IOException e) {
                e.printStackTrace();}
            try (BufferedReader musicFile = new BufferedReader(new FileReader(OUT_FILE_TXT))) {
                String music;
                int count = 0;
                try { while ((music = musicFile.readLine()) != null) {
                        downloadUsingNIO(music, PATH_TO_MUSIC + (count) + ".mp3"); //Скачивание музыки
                        count++;}                                                      //и присваивание именяни music
                } catch (IOException e) {
                    e.printStackTrace();}
            } catch (IOException e) {
                e.printStackTrace();}

        } finally {
            String Url;
            try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT1));
                 BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILE_TXT1))) {
                while ((Url = inFile.readLine()) != null) {
                    URL url = new URL(Url);

                    String result;
                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                        result = bufferedReader.lines().collect(Collectors.joining("\n")); }
                    Pattern email_pattern = Pattern.compile("\\s*(?<=src\\s?=\\s?\")[^>]*/*\\.(jpeg|jpg|png|gif)(?=\")");
                    Matcher matcher = email_pattern.matcher(result);
                    int i = 0;
                    while (matcher.find() && i < 2) {
                        outFile.write(matcher.group() + "\r\n");
                        i++;}
                }
            } catch (IOException e) {
                e.printStackTrace();}
            try (BufferedReader musicFile = new BufferedReader(new FileReader(OUT_FILE_TXT1))) {
                String pic;
                int count = 0;
                try { while ((pic = musicFile.readLine()) != null) {
                        downloadUsingNIO(pic, PATH_TO_MUSIC1 + (count) + ".jpeg");
                        count++;}
                } catch (IOException e) {
                    e.printStackTrace();}
            } catch (IOException e) {
                e.printStackTrace();}}}

    private static void downloadUsingNIO(String strUrl, String file) throws IOException {  //Метод скачивания обьекта
        URL url = new URL(strUrl);                                                         //через url ссылку
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());
        FileOutputStream stream = new FileOutputStream(file);
        stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        stream.close();
        byteChannel.close();
    }
}