package dev.echo.utils.bungee.api;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeContext {

    
    @Getter
    protected final CommandSender sender;


    
    @Getter
    protected final String[] arguments;

    
    @Setter
    @Getter
    private int min;

    
    @Setter
    @Getter
    private int max;

    
    @Setter
    private String perm;


    
    public BungeeContext(CommandSender sender, String[] arguments) {

        
        this.sender = sender;

        
        this.arguments = arguments;
    }

    public String getString(int i) {
        
        try {
            
            return arguments[i - 1];

            
        }catch (Exception e){

            
            return "";
        }
    }

    
    public String getString(int i,int f){
        
        try {
            return arguments[(i - 1 == f) ? i - 1 : f];
        }catch (Exception e){
            return "";
        }
    }
    public Integer getInteger(int i){
        try {
            return Integer.parseInt(arguments[i - 1]);
        }catch (Exception ignored){
        }

        return 0;
    }

    public double getDouble(int i){

        
        try {
            
            return getInteger(i).doubleValue();

            
        }catch (Exception e){

            
            return 0;
        }
    }

    public double getDouble(int i,int f){

        
        try {
            
            return getInteger(i,f).doubleValue();

            
        }catch (Exception e){

            
            return 0;
        }
    }

    
    public Integer getInteger(int i,int f){

        
        if(hasArg(i)){
           return Integer.parseInt(arguments[(i - 1 == f) ? i - 1 : f]);
        }
        
        return 0;
    }

    public int getArgsLength(){

        return arguments.length;
    }

    
    public boolean hasArg(int i){
        
        return !(arguments[i - 1].isEmpty() || arguments[i - 1] == null);
    }

    
    public boolean isOverMax(int i){

        
        return (i - 1) > max;
    }

    
    public boolean isUnderMin(int i){
        
        return (i - 1) < min;
    }

    
    public ProxiedPlayer getPlayer(){
        
        return isSenderPlayer(sender) ? (ProxiedPlayer) sender : null;
    }


    public boolean isSenderPlayer(CommandSender sender){
        return sender instanceof ProxiedPlayer;
    }
    public boolean isSenderPlayer(){
        return sender instanceof ProxiedPlayer;
    }
    public boolean isPermissible(String perm) {
        return this.sender.hasPermission(perm);
    }

    public String getStringBuilder(int starting, int max) {
        StringBuilder builder = new StringBuilder();

        for(int i = (starting - 1); i < max; i++){
            builder.append(arguments[i]).append(' ');
        }
        return builder.toString();
    }

}
