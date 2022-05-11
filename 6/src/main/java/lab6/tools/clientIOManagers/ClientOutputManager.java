package lab6.tools.clientIOManagers;

import lab6.tools.ServerRequest;

/**
 * Класс, выводящий сообщения пользователю
 */
public class ClientOutputManager {
    private Boolean manualMode;

    public ClientOutputManager() {
        manualMode = true;
    }

    public ClientOutputManager(Boolean manualMode) {
        this.manualMode = manualMode;
    }

    private String highlightedStyle(Object message){
        return "\033[35m" + message + "\033[0m";
    }
    
    private String errorStyle(Object message){
        return "\033[31m" + message + "\033[0m";
    }
    
    public void print_LN_ManualModeSimpleMessage(Object message){
        if (manualMode) System.out.println(message);
    }

    public void printManualModeSimpleMessage(Object message){
        if (manualMode) System.out.print(message);
    }

    public void print_LN_ScriptModeSimpleMessage(Object message){
        if (!manualMode) System.out.println(message);
    }

    public void printScriptModeSimpleMessage(Object message){
        if (!manualMode) System.out.print(message);
    }

    public void print_LN_ManualModeHighlightedMessage(Object message){
        print_LN_ManualModeSimpleMessage(highlightedStyle(message));
    }

    public void printManualModeHighlightedMessage(Object message){
        printManualModeSimpleMessage(highlightedStyle(message));
    }

    public void print_LN_ScriptModeHighlightedMessage(Object message){
        print_LN_ScriptModeSimpleMessage(highlightedStyle(message));
    }

    public void printScriptModeHighlightedMessage(Object message){
        printScriptModeSimpleMessage(highlightedStyle(message));
    }

    public void print_LN_ManualModeError(Object message){
        print_LN_ManualModeSimpleMessage(errorStyle(message));
    }

    public void printManualModeError(Object message){
        printManualModeSimpleMessage(errorStyle(message));
    }

    public void print_LN_ScriptModeError(Object message){
        print_LN_ScriptModeSimpleMessage(errorStyle(message));
    }

    public void printScriptModeError(Object message){
        printScriptModeSimpleMessage(errorStyle(message));
    }

    public void setManualMode() {
        manualMode = true;
    }

    public void setScriptMode(){
        manualMode = false;
    }

    public boolean isManualMode(){
        return manualMode;
    }

    public boolean isScriptMode(){
        return !manualMode;
    }

    public void printServerRequest(ServerRequest serverRequest) {
        System.out.print(serverRequest.getMessage());
    }
}
