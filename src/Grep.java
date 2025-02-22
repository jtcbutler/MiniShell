import java.io.File;
import java.util.Scanner;
//import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class Grep extends ShellCommand{

    @Override
    public String execute() throws ShellException{
        // TODO: implement grep command
        if(this.arguments.length < 1 ) {
            throw new ShellException("Invalid number of arguments for grep command");
        } else if(this.arguments.length ==1){
            return execute_stdin();
        }else if(this.arguments[0].equals("-i")){
            return execute_i();
        } else if(this.arguments[0].equals("-v")){
            return execute_v();
        } else if(this.arguments[0].equals("-n")){
            return execute_n();
        } else if (this.arguments.length == 2 & isFile(this.arguments[1])) {
            return execute_without_flag();

        }else{
            return execute_pipeline();
        }
    }

    public boolean isFile(String path){
        File file = new File(path);
        return file.isFile();
    }
    
    public String execute_without_flag() throws ShellException{
        String total_return = "";
        File file = new File(this.arguments[1]);
        // 
        try{
            Scanner sc = new Scanner(file);
            while(sc.hasNext()){
                String tmp = sc.nextLine();
                if (tmp.contains(this.arguments[0])){
                    total_return += tmp + "\n";
                }
            }   
        } catch (Exception e){
            throw new ShellException(e.getMessage());
        }
        return total_return;
    }

    public String execute_stdin() throws ShellException{
        Scanner scanner = new Scanner(System.in);
        while (true) { 
            String str = scanner.nextLine();
            if (str.equals("exit") || str.equals("quit")) {
                break;
            }
            if (str.contains(this.arguments[0])){
                System.out.println(str);
            }
        }
        return "";
    }


    public String execute_pipeline() throws ShellException{
        String total_return = "";
        if(this.arguments.length < 2){
            throw new ShellException("Invalid number of arguments for grep command");
        }else{
            String[] element_list = this.arguments[1].split("\\s+");
            for (String element : element_list){
                if (element.contains(this.arguments[0])){
                    total_return += element + "\n";
                }
            }
            return total_return;
        }
    }

    public String execute_i() throws ShellException{
        //create a file object for the file to be searched
        String total_return = "";
        if(arguments.length < 3){
            throw new ShellException("Invalid number of arguments for grep command");
        } else if (isFile(this.arguments[2])){
            File file = new File(this.arguments[2]);
            // 
            try{
                Scanner sc = new Scanner(file);
                while(sc.hasNext()){
                    String tmp = sc.nextLine();
                    if (tmp.toLowerCase().contains(this.arguments[1].toLowerCase())){
                        total_return += tmp + "\n";
                    }
                }   
            } catch (Exception e){
                throw new ShellException(e.getMessage());
            }
            
        }
        else{

            if(this.arguments[2].toLowerCase().contains(this.arguments[1].toLowerCase())){
                total_return = this.arguments[2];
            }

        }
        return total_return;
    }
    
    public String execute_v() throws ShellException{
        String total_return = "";
        if(this.arguments.length < 3){
            throw new ShellException("Invalid number of arguments for grep command");
        }else{
            if(isFile(this.arguments[2])){
                File file = new File(this.arguments[2]);
            // 
            try{
                Scanner sc = new Scanner(file);
                while(sc.hasNext()){
                    String tmp = sc.nextLine();
                    if (!tmp.contains(this.arguments[1])){
                        total_return += tmp + "\n";
                    }
                }   
            } catch (Exception e){
                throw new ShellException(e.getMessage());
            }

            }else {
                String[] element_list = this.arguments[2].split("\\s+");
                for (String element : element_list){
                    if (!element.contains(this.arguments[1])){
                        total_return += element ;
                    }
                }
            }
            
        }
        return total_return;
    }
    
    public String execute_n() throws ShellException{
        String total_return = "";
        if(this.arguments.length < 3){
            throw new ShellException("Invalid number of arguments for grep command");
        }else{
            int line_counter=0;
            if(isFile(this.arguments[2])){   
            File file = new File(this.arguments[2]);
            // 
            try{
                Scanner sc = new Scanner(file);
                while(sc.hasNext()){
                    line_counter++;
                    String tmp = sc.nextLine();
                    if (tmp.contains(this.arguments[1])){
                        total_return += line_counter + ": " + tmp + "\n";
                    }
                }   
                } catch (Exception e){
                    throw new ShellException(e.getMessage());
                }
            } else{
                String[] element_list = this.arguments[2].split("\\s+");
                for (String element : element_list){
                    line_counter++;
                    if (element.contains(this.arguments[1])){
                        total_return += line_counter + ": " + element + "\n";
                    }
                }
                
            }
        }

            return total_return;
    }


}
