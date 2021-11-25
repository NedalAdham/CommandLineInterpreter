// group S3
// Nader Atef  20190575
// Paula Adel  20190139
// Nedal Adham 20190585

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Arrays;
import java.util.*;
import java.nio.file.*;
import static java.lang.System.*;


class Parser {
    String commandName;
    String[] args;
    //This method will divide the input into commandName and args
    // where "input" is the string command entered by the user
    public boolean parse(String command){
        if(command.isEmpty()){
            return false;
        }
        else{

            String[] temp = command.split(" (?=(([^'\"]*['\"]){2})*[^'\"]*$)");
            commandName = temp[0];
            int size = temp.length;
            args = Arrays.copyOfRange(temp,1,size);
            return true;
        }
    }
    public String getCommandName(){
        return commandName;
    }
    public String[] getArgs(){
        return args;
    }
}
public class Terminal {
    static Parser parser = new Parser();
    // A scanner to take input from the user
    Scanner input = new Scanner(in);

    public static String echo(String[] args) {
        return String.join(" ", args);
    }


    public static String pwd() {
        return System.getProperty("user.dir");
    }

    public static void cd (String[] args)throws IOException{

        Path currentRelativePath = Paths.get("user.dir");

        //String currentRelativePath = Paths.get("").toAbsolutePath().toString();
        try {
            if (args.length == 0) {
                Path.of(setProperty("user.dir", getProperty("user.home")));
            } else if (args[0].equals("..")) {
                if (pwd().length() > 2){
                    String example = pwd();
                    String lol = example.substring(example.lastIndexOf("\\") );
                    String parent = example.replace(lol , "");
                    setProperty("user.dir",parent);}
                else {
                    out.println("there is no parent directory");
                }


            } else {
                if (String.join(" ", args).contains("\\")) {
                    System.setProperty("user.dir", String.join(" ", args));
                    //setCurrentDirectory(String.join(" ", args));
                }
                else
                {
                    System.setProperty("user.dir", pwd() + "\\" + String.join(" ", args));
                }
            }
        }catch (IllegalArgumentException e){
            out.println(e);
        }
    }

    public static void cp(String[] args) throws IOException {

        if (args.length != 0){
            if (args[0].equals("-r")){
                try {

                    File d1 = new File(args[1]);
                    for(File f: d1.listFiles()){
                        if (!f.isDirectory()){
                            String []touchargs = {args[2]+"\\"+f.getName()};
                            touch(touchargs);
                            String[] cpargs = {args[1]+"\\"+f.getName(),args[2]+"\\"+f.getName()};
                            cp(cpargs);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                InputStream is = null;
                OutputStream os = null;
                try {
                    is = new FileInputStream(  args[0]);
                    os = new FileOutputStream( args[1]);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }

                    is.close();
                    os.close();
                    out.println("Copied succesfuly");
                }
                catch (Exception er)
                {
                    er.printStackTrace();
                }
            }
        }

    }



    public static void mkdir(String[] args) {
        for (int i = 0; i < args.length; i++) {
            File f1;
            if (args[i].substring(1,2).equals(":") ){
                f1 = new File(args[i].replaceAll("\"",""));
            }else{
                f1 = new File(pwd()+"\\"+ args[i].replaceAll("\"",""));
            }
            if (f1.exists()) {
                System.out.println(args[i] + " already exists");
            } else if (f1.mkdir()) {
                System.out.println(args[i] + " created successfully");
            } else {
                System.out.println("error creating " + args[i]);
            }
        }
    }

    public static void rmdir(String[] args) {
        if (args[0].equals("*")) {
            File[] files = new File(pwd()).listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    if (file.list().length == 0) {
                        if (!file.delete()) {
                            out.println("couldn't delete " + file.getName());
                        }
                    }
                }
            }
            out.println("done");
        } else {
            File f1 = new File(args[0]);
            if (f1.isDirectory()) {
                if (f1.list().length == 0) {
                    if (!f1.delete()) {
                        out.println("couldn't delete " + f1.getName());
                    } else {
                        out.println("deleted successfully");
                    }
                } else {
                    out.println(args[0] + " is not empty");
                }
            } else {
                out.println(args[0] + " is not a directory");
            }
        }
    }

    public static String cat(String[] args) {
        String outString = "";
        for (String arg : args) {
            File f = new File(arg);
            if (!f.exists()) {
                outString += arg + " doesn't exist\n";
                continue;
            }
            try {
                Scanner fS = new Scanner(f);
                while (fS.hasNextLine()) {
                    outString += fS.nextLine();
                }
                fS.close();

            } catch (FileNotFoundException e) {
                out.println(e);
            }
        }
        return outString;
    }

    public static void ls(String[] args) {
        File f = new File(pwd());
        // Creates an array in which we will store the names of files and directories
        String[] pathnames;
        // Populates the array with names of files and directories
        pathnames = f.list();
        //sorts string array in alphabetical order or ascending order
        assert pathnames != null;
        if (args.length == 0){

            Arrays.sort(pathnames);
            //prints the sorted string array in ascending order
//            System.out.println(Arrays.toString(pathnames));
            for (String pathname : pathnames ){
                out.println(pathname);
            }
        }else{
            if (args[0].equals("-r")){
                Arrays.sort(pathnames, Collections.reverseOrder());
                for (String pathname : pathnames ){
                    out.println(pathname);
                }
            }else{
                out.println("ls takes no arguments or -r");
            }
        }

    }


    public static void rm(String[] args) {
        File f = new File(pwd() +"\\" + args[0]);
        f.delete();

    }

    public static  void touch(String[] args) throws IOException {
        File f = new File(pwd()+"\\"+args[0]);
//        File f = new File("./"+args[0]);
        f.createNewFile();
        out.println("created successfully");
    }

    // This method will choose the suitable command method to be called
    public static void chooseCommandAction() throws IOException {
        Scanner input = new Scanner(System.in);
        while (true) {
            String command = input.nextLine();
            parser.parse(command);
            String commandName = parser.getCommandName();
            if (commandName.equals("exit")) {
                break;
            }
            String[] cmdargs = parser.getArgs();
            if (commandName.equals("cd")) {
                cd(cmdargs);
            } else if (commandName.equals("pwd")) {
                String currentPath = pwd();
                System.out.println(currentPath);
            } else if (commandName.equals("mkdir")) {
                mkdir(cmdargs);
            } else if (commandName.equals("rmdir")) {
                rmdir(cmdargs);
            } else if (commandName.equals("cat")) {
                String outString = cat(cmdargs);
                out.println(outString);
            }else if (commandName.equals("ls")){
                ls(cmdargs);
            }
            else if (commandName.equals("echo"))
            {
                String echo = echo(cmdargs);
                out.println(echo);
            }
            else if (commandName.equals("cp"))
            {
                cp(cmdargs);
                //out.println(args[0]);
            }
            else if(commandName.equals("rm")){
                rm(cmdargs);
            }else if(commandName.equals("touch")){
                touch(cmdargs);
            }else{
                out.println("command not recognized");
            }
        }
    }
    public  static void main(String[] args) throws IOException {
        chooseCommandAction();
    }
}