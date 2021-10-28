package ru.laptev;

import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InMemoryFileSystem {
    private static String createPathRegex = "CREATEPATH\\s(?<pathname>.*)";
    private static String createFileRegex = "CREATEFILE\\s(?<pathname>.*)\\s(?<filename>.*)\\s(?<filecontent>.*)";
    private static String showDirectoryRegex = "SHOW\\s(?<pathname>.*)";
    private static String deleteFileRegex = "DELETE\\s(?<fullpathname>.*)";
    private static String searchFileRegex = "SEARCH\\s(?<filename>.*)";

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
        String s;
        while (!(s = bufferedReader.readLine()).equals("EXIT")) {
            if (s.matches(createPathRegex)) {
                Matcher matcher = Pattern.compile(createPathRegex).matcher(s);
                matcher.find();
                Path data = fs.getPath(matcher.group("pathname"));
                Files.createDirectory(data);
                System.out.println("Directory is created!");
            }

            if (s.matches(createFileRegex)) {
                Matcher matcher = Pattern.compile(createPathRegex).matcher(s);
                matcher.find();
                Path data = fs.getPath(matcher.group("pathname"));
                Path hello = data.resolve(matcher.group("filename"));
                Files.write(hello, ImmutableList.of(matcher.group("filecontent")), StandardCharsets.UTF_8);
                System.out.println("File is created!");
            }

            if (s.matches(showDirectoryRegex)) {
                Matcher matcher = Pattern.compile(createPathRegex).matcher(s);
                matcher.find();
                Files.list(fs.getPath(matcher.group("pathname"))).forEach(file -> {
                    try {
                        System.out.println(String.format("%s (%db)", file, Files.readAllBytes(file).length));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            if (s.matches(deleteFileRegex)) {
                Matcher matcher = Pattern.compile(createPathRegex).matcher(s);
                matcher.find();
                boolean fullpathname = Files.deleteIfExists(Paths.get(matcher.group("fullpathname")));
                if (fullpathname) {
                    System.out.println("File is deleted!");
                } else {
                    System.out.println("File not found!");
                }
            }

            if (s.matches(searchFileRegex)) {
                Matcher matcher = Pattern.compile(searchFileRegex).matcher(s);
                matcher.find();
                //sorry, I wasn't fast enough...
            }


        }
    }
}
