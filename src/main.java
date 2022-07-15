import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class main {


    public static class Parser {

        String commandName;
        String[] args;

        public Parser() {
            Scanner scanner = new Scanner(System.in);
            System.out.print(">>");
            String input = scanner.nextLine();
            parse(input);


        }

        public boolean parse(String input) {


            this.args = input.split(" ", 2);
            if (args.length > 1) {
                if (args[0].equals("ls") && args[1].equals("-r")) {
                    this.args = input.split(" ");
                }
            }

            if (args[0].equals("mkdir")) {
                this.args = input.split(" ");
            }
            if (args[0].equals("cp")) {
                this.args = input.split(" ");
            }
            if (args[0].equals("cat")) {
                this.args = input.split(" ");
            }

            if ("".equals(input) || "".equals(args[0])) {
                System.out.println("Please enter a valid command!");
                return false;
            }

            try {

                commandName = args[0];
                if ("exit".equalsIgnoreCase(commandName)) {
                    System.exit(0);
                }
                return true;
            } catch (Exception e) {
                System.out.println("Please enter a valid command!");

            }
            return false;
        }


        public String getCommandName() {
            return commandName;
        }

        public String[] getArgs() {
            return args;
        }
    }


    public static class Terminal {
        static String lastCom;

        Parser parser = new Parser();
        private static File curLocation = new File(System.getProperty("user.dir"));


        public Terminal() throws IOException {
            if ("pwd".equals(parser.args[0])) {
                pwd();
                lastCom = parser.args[0];
            } else if ("echo".equals(parser.args[0])) {
                echo();
                lastCom = parser.args[0];
            } else if ("cd".equals(parser.args[0])) {
                cd();
            } else if ("ls".equals(parser.args[0])) {
                if (parser.args.length > 1) {
                    if (parser.args[1].equals("-r")) {
                        ls_r();
                    }
                } else
                    ls();
            } else if ("mkdir".equals(parser.args[0])) {
                mkdir();
            } else if ("rmdir".equals(parser.args[0])) {
                rmdir();
            } else if ("touch".equals(parser.args[0])) {
                touch();
            } else if ("cp".equals(parser.args[0])) {
                if (parser.args.length > 1) {
                    if (parser.args[1].equals("-r")) {
                        cp_r();
                    }
                } else
                    cp();
            } else if (parser.args[0].equals("rm")) {
                rm();
            } else if (parser.args[0].equals("cat")) {
                cat();
            } else {
                System.out.println("Not Valid Command!");
            }


        }

        public void pwd() {
            if (parser.args.length > 1) {
                System.out.println("Too many args!");

            } else {
                if (curLocation == null) {

                    curLocation = new File(System.getProperty("user.dir"));
                    System.out.println(curLocation);
                } else {
                    System.out.println(curLocation);
                }


            }

        }

        public void echo() {
            try {
                if (parser.args.length > 2) {
                    System.out.println("Too many args!");
                } else {
                    System.out.println(parser.args[1]);

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        public void cd() {

            try {

                if (parser.args.length > 2) {
                    System.out.println("Too many args!");
                } else if (parser.args.length == 1) {

                    curLocation = new File(System.getProperty("user.home"));
                    System.out.println(curLocation);
                }
                if (parser.args.length == 2) {
                    String sourcePath = parser.args[1];
                    if (sourcePath.equals("..")) {
                        if(curLocation.getParentFile() != null)
                        {
                             curLocation = curLocation.getParentFile();
                            System.out.println(curLocation);
                        }else
                        {
                            System.out.println(curLocation);
                        }


                    } else {

                        File target = new File(parser.args[1]);

                        if (target.isAbsolute()) {
                            if (target.exists() && target.isDirectory()) {

                                curLocation = target;
                                System.out.println(curLocation);
                            } else if (target.exists() && !target.isDirectory()) {
                                throw new IllegalArgumentException(String.format("---------%s isn't a dir.", target));
                            } else {
                                throw new FileNotFoundException(String.format("---------%s doesn't exist.", target));
                            }
                        } else {

                            String path = String.format("%s/%s", curLocation.getAbsolutePath(), target);
                            target = new File(path);
                            if (target.exists()) {
                                if (!target.isDirectory()) {
                                    throw new IllegalArgumentException(String.format("---------%s isn't a dir.", target));
                                }

                                curLocation = target;
                                System.out.println(curLocation);
                            } else {
                                throw new FileNotFoundException(String.format("------------%s doesn't exist.", target));
                            }
                        }

                    }

                }

            } catch (Exception e) {
                System.out.println(e);
            }


        }

        public void ls() {

            if (parser.args.length > 1) {
                System.out.println("Too many args!");
            } else {

                if (curLocation == null) {
                    System.out.println("use cd command to get a directory first");
                } else {
                    ArrayList<String> content = new ArrayList<>(Arrays.asList(curLocation.list()));
                    Collections.sort(content);
                    int count = 1;
                    for (String s : content) {
                        System.out.println(count + ". " + s);
                        count++;
                    }
                }

            }
        }

        public void ls_r() {

            if (parser.args.length > 2) {
                System.out.println("Too many args!");
            } else {

                if (curLocation == null) {
                    System.out.println("use cd command to get a directory first");
                } else {
                    ArrayList<String> content = new ArrayList<>(Arrays.asList(curLocation.list()));
                    content.sort(Collections.reverseOrder());
                    int count = 1;
                    for (String s : content) {
                        System.out.println(count + ". " + s);
                        count++;
                    }
                }

            }
        }

        public void mkdir() {
            if (parser.args.length <= 1) {
                System.out.println("Not enough args!");
            } else {
                StringBuilder names = new StringBuilder();
                for (int i = 1; i < parser.args.length; i++) {
                    names.append(parser.args[i]).append(" ");

                }

                String[] args = names.toString().split(" ");

                for (String arg : args) {
                    File target = new File(arg);

                    if (target.isAbsolute()) {
                        if (target.exists()) {
                            System.out.println("Already there!");
                        } else {
                            File file = new File(String.valueOf(target));
                            if (file.mkdir()) {
                                System.out.println("Created");
                            } else
                                System.out.println("Error");
                        }
                    } else {
                        String path = String.format("%s/%s", curLocation.getAbsolutePath(), target);
                        target = new File(path);
                        if (target.exists()) {
                            System.out.println("Already exist!");
                        } else {

                            if (target.mkdir()) {
                                System.out.println("Created");
                            } else
                                System.out.println("Error");
                        }
                    }

                }


            }
        }

        public void rmdir() {
            if (parser.args.length > 2) {
                System.out.println("Too many arguments!");
            } else if (parser.args.length == 1) {
                System.out.println("Not enough arguments!");
            } else {
                if (parser.args[1].equals("*")) {

                    ArrayList<String> content = new ArrayList<String>(Arrays.asList(curLocation.list()));
                    Collections.sort(content);

                    for (File f : curLocation.listFiles()) {


                        if (f.isDirectory() && f.length() == 0) {
                            f.delete();
                            if (!f.exists()) {
                                System.out.println("Directory is not Empty");
                            } else {
                                System.out.println("Empty files have been deleted!");
                            }
                        }

                    }
                    System.out.println("Empty files have been deleted!");
                } else {
                    File target = new File(parser.args[1]);
                    String path = String.format("%s/%s", curLocation.getAbsolutePath(), target);
                    target = new File(path);
                    if (target.exists()) {

                        if (target.length() == 0 && target.isDirectory()) {
                            target.delete();
                            if (!target.exists()) {
                                System.out.println("Empty files have been deleted!");
                            } else {
                                System.out.println("Directory is not Empty");
                            }
                        }


                    } else {
                        System.out.println("There is no such a directory!");
                    }


                }
            }


        }


        public void touch() throws IOException {
            if (parser.args.length > 2) {
                System.out.println("Too many arguments!");
            } else {
                StringBuilder names = new StringBuilder();
                for (int i = 1; i < parser.args.length; i++) {
                    names.append(parser.args[i]).append(" ");

                }

                String[] args = names.toString().split(" ");

                for (String arg : args) {
                    File target = new File(arg);

                    if (target.isAbsolute()) {
                        if (target.exists()) {
                            System.out.println("Already there!");
                        } else {
                            File file = new File(String.valueOf(target));
                            target.createNewFile();
                            if (file.createNewFile()) {
                                System.out.println("Created");
                            }
                        }
                    } else {
                        String path = String.format("%s/%s", curLocation.getAbsolutePath(), target);
                        target = new File(path);
                        if (target.exists()) {
                            System.out.println("Already exist!");
                        } else {
                            target.createNewFile();
                            if (target.createNewFile()) {
                                System.out.println("Created");
                            }
                        }
                    }

                }


            }
        }


        public void cp() {


            if (parser.args.length > 3) {
                System.out.println("Too many arguments!");
            } else if (parser.args.length < 3) {
                System.out.println("Not enough arguments");
            } else {
                File source = new File(parser.args[1]);
                File dest = new File(parser.args[2]);


                if (source.isAbsolute() && source.isFile()) {

                    try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {



                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer, 0, length);

                        }
                        System.out.println("Copied successfully!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    String sourcePath = String.format("%s/%s", curLocation.getAbsolutePath(), source);
                    String destPath = String.format("%s/%s", curLocation.getAbsolutePath(), dest);
                    source = new File(sourcePath);
                    dest = new File(destPath);
                    if (source.exists()) {
                        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = is.read(buffer)) > 0) {
                                os.write(buffer, 0, length);

                            }
                            System.out.println("Copied successfully!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                        System.out.println("File does not exist!");
                }


            }


        }


        public void copy(File sourceLocation, File targetLocation) throws IOException {
            if (sourceLocation.isDirectory()) {
                copyDirectory(sourceLocation, targetLocation);
            } else {
                copyFile(sourceLocation, targetLocation);
            }
        }

        private void copyDirectory(File source, File target) throws IOException {
            if (!target.exists()) {
                target.mkdir();
            }

            for (String f : source.list()) {
                copy(new File(source, f), new File(target, f));
            }
        }

        private void copyFile(File source, File target) throws IOException {
            try (
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target)
            ) {
                byte[] buf = new byte[1024];
                int length;
                while ((length = in.read(buf)) > 0) {
                    out.write(buf, 0, length);
                }
            }
        }


        public void cp_r() throws IOException {
            if (parser.args.length > 4) {
                System.out.println("Too many arguments!");
            } else if (parser.args.length < 4) {
                System.out.println("Not enough arguments!");
            } else {
                File source = new File(parser.args[2]);
                File dest = new File(parser.args[3]);


                if (source.isAbsolute()) {

                    try {

                        copy(source, dest);


                        System.out.println("Directory Copied!");

                    } catch (Exception e) {
                        System.out.println(e);
                    }


                } else {

                    String sourcePath = String.format("%s/%s", curLocation.getAbsolutePath(), source);
                    String destPath = String.format("%s/%s", curLocation.getAbsolutePath(), dest);
                    source = new File(sourcePath);
                    dest = new File(destPath);
                    if (source.exists() && source.isDirectory()) {

                        //copy the directory
                        copy(source, dest);

                    }


                }


            }
        }


        public void rm() {
            if (parser.args.length > 2) {
                System.out.println("Too many arguments");
            } else if (parser.args.length < 2) {
                System.out.println("Not enough arguments!");
            } else {
                File target = new File(parser.args[1]);
                if (target.isAbsolute()) {
                    System.out.println("Enter only the file name!");
                } else {
                    String path = String.format("%s/%s", curLocation.getAbsolutePath(), target);
                    target = new File(path);
                    System.out.println(target);
                    if (target.exists() && !target.isDirectory()) {
                        boolean status = target.delete();
                        if (status) {
                            System.out.println("Deleted");
                        } else {
                            System.out.println("File can not be deleted!");
                        }
                    } else {
                        System.out.println("rm Command can not remove directories!");
                    }
                }

            }


        }


        public void cat() {
            StringBuilder result = new StringBuilder();
            if (parser.args.length > 3) {
                System.out.println("Too many arguments!");
            } else if (parser.args.length < 2) {
                System.out.println("Not enough arguments!");
            } else {
                for (int i = 1; i < parser.args.length; i++) {
                    File target = new File(parser.args[i]);
                    if (target.isAbsolute() && target.isFile()) {

                        try {
                            Scanner in = new Scanner(target);
                            while (in.hasNext()) {
                                result.append(in.nextLine()).append("\n");
                            }

                            in.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        String path = String.format("%s/%s", curLocation.getAbsolutePath(), target);
                        target = new File(path);
                        if (target.exists() && target.isFile()) {

                            try {
                                Scanner in = new Scanner(target);
                                while (in.hasNext()) {
                                    result.append(in.nextLine()).append("\n");
                                }

                                in.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            System.out.println("File not found!");
                        }
                    }

                }
                System.out.println(result);
            }


        }


    }


    public static void main(String[] args) throws IOException {

        while (true) {
             new Terminal();
        }

    }


}
