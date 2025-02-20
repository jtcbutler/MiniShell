import java.io.File;
import java.util.Scanner;

public class Grep extends ShellCommand{
    @Override
    public String execute() throws ShellCommand.ShellCommandException{
        // TODO: implement grep command
        if(this.arguments.length < 3){
            throw new ShellCommand.ShellCommandException("Invalid number of arguments for grep command");
        } else if(this.arguments[0].equals("-i")){
            // TODO: implement case-insensitive grep command
            execute_i();
        } else if(this.arguments[0].equals("-v")){
            execute_v();
        } else if(this.arguments[0].equals("-n")){
            execute_n();
        }

        
        return "";
    }
    

    public String execute_i() throws ShellCommand.ShellCommandException{
        //create a file object for the file to be searched
        File file = new File(this.arguments[2]);
        // 
        try{
	        Scanner sc = new Scanner(file);
            while(sc.hasNext()){
                String tmp = sc.nextLine();
                if (tmp.toLowerCase().contains(this.arguments[1].toLowerCase())){
                    System.out.println(tmp);
                }
		    }   
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "";
    }
    
    public String execute_v() throws ShellCommand.ShellCommandException{
        // TODO: implement inverse grep command
        File file = new File(this.arguments[2]);
        // 
        try{
	        Scanner sc = new Scanner(file);
            while(sc.hasNext()){
                String tmp = sc.nextLine();
                if (!tmp.contains(this.arguments[1])){
                    System.out.println(tmp);
                }
		    }   
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "";

    }
    
    public String execute_n() throws ShellCommand.ShellCommandException{
        int line_counter=0;
        //create a file object for the file to be searched
        File file = new File(this.arguments[2]);
        // 
        try{
            Scanner sc = new Scanner(file);
            while(sc.hasNext()){
                line_counter++;
                String tmp = sc.nextLine();
                if (tmp.contains(this.arguments[1])){
                    System.out.println(line_counter + ": " + tmp);
                }
            }   
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    return "";
    }


}
