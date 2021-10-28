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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
        String s;

        String createPathRegex = "CREATEPATH\\s(.*)";
        String createFileRegex = "CREATEFILE\\s(.*)\\s(.*)\\s(.*)";
        String showDirectoryRegex = "SHOW\\s(.*)";

        while (!(s = bufferedReader.readLine()).equals("EXIT")) {
            if (s.matches(createPathRegex)) {
                Matcher matcher = Pattern.compile(createPathRegex).matcher(s);
                matcher.find();
                Path data = fs.getPath(matcher.group(1));
                Files.createDirectory(data);
            }

            if (s.matches(createFileRegex)) {
                Matcher matcher = Pattern.compile(createPathRegex).matcher(s);
                matcher.find();
                Path data = fs.getPath(matcher.group(1));
                Path hello = data.resolve(matcher.group(2));
                Files.write(hello, ImmutableList.of(matcher.group(3)), StandardCharsets.UTF_8);
            }

            if (s.matches(showDirectoryRegex)) {
                Matcher matcher = Pattern.compile(createPathRegex).matcher(s);
                matcher.find();
                Files.list(fs.getPath(matcher.group(1))).forEach(file -> {
                    try {
                        System.out.println(String.format("%s (%db)", file, Files.readAllBytes(file).length));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
