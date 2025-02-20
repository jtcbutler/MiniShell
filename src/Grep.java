import java.io.File;
import java.util.Scanner;
import java.util.Arrays;

public class Grep extends ShellCommand{
    @Override
    public String execute() throws ShellException{
        // TODO: implement grep command
<<<<<<< HEAD
        if(this.arguments.length <= 1 ) {
            throw new ShellCommand.ShellCommandException("Invalid number of arguments for grep command");
=======
        if(this.arguments.length < 3 || this.arguments.length ==2) {
            throw new ShellException("Invalid number of arguments for grep command");
>>>>>>> refs/remotes/origin/main
        } else if(this.arguments[0].equals("-i")){
            // TODO: implement case-insensitive grep command
           return execute_i();
        } else if(this.arguments[0].equals("-v")){
           return execute_v();
        } else if(this.arguments[0].equals("-n")){
           return execute_n();
        } else{
           return execute_default();
        }
    }
    

    public String execute_default() throws ShellCommand.ShellCommandException{
        String total_return = "";
        String[] element_list = this.arguments[1].split("\\s+");
        for (String element : element_list){
            if (element.contains(this.arguments[0])){
                total_return += element + "\n";
            }
        }
        return total_return;

    }

    public String execute_i() throws ShellException{
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
    
    public String execute_v() throws ShellException{
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
    
    public String execute_n() throws ShellException{
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
