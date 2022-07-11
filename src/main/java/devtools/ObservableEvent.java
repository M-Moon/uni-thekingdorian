package devtools;

public class ObservableEvent 
{

    public static final int DEFAULT_EVENT = 12345;

    protected Object source;
    protected int id;
    protected String command;
    
    public ObservableEvent(Object source, int id, String command) {
        this.source = source;
        this.id = id;
        this.command = command;
    }

    public ObservableEvent(Object source, int id) {
        this(source, id, "DEFAULT EVENT");
    }

    public Object getSource() {
        return source;
    }

    public int getEventID(){
        return id;
    }

    public void setCommand(String cmd) {
        this.command = cmd;
    }

    public String getCommand() {
        return command;
    }
}
