public class Grep {
    public String execute(String[] inputSegments) throws ShellCommand.ShellCommandException{
        // TODO: implement grep command
        if(inputSegments.length < 3){
            throw new ShellCommand.ShellCommandException("Invalid number of arguments for grep command");
        } else if(inputSegments[1].equals("-i")){
            // TODO: implement case-insensitive grep command
            this.execute_i(inputSegments);
        } else if(inputSegments[1].equals("-v")){
            this.execute_v(inputSegments);
        } else if(inputSegments[1].equals("-n")){
            this.execute_n(inputSegments);
        }

        
        return "";
    }
    

    public String execute_i(String[] inputSegments) throws ShellCommand.ShellCommandException{
        // TODO: implement case-insensitive grep command
        

        return "";
    }
    
    public String execute_v(String[] inputSegments) throws ShellCommand.ShellCommandException{
        // TODO: implement inverse grep command
        return "";
    }
    
    public String execute_n(String[] inputSegments) throws ShellCommand.ShellCommandException{
        // TODO: implement line number grep command
        return "";
    }


}
